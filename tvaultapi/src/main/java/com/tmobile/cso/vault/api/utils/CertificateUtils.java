/** *******************************************************************************
*  Copyright 2020 T-Mobile, US
*   
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*  
*     http://www.apache.org/licenses/LICENSE-2.0
*  
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*  See the readme.txt file for additional language around disclaimer of warranties.
*********************************************************************************** */

package com.tmobile.cso.vault.api.utils;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.tmobile.cso.vault.api.common.SSLCertificateConstants;
import com.tmobile.cso.vault.api.controller.ControllerUtil;
import com.tmobile.cso.vault.api.exception.LogMessage;
import com.tmobile.cso.vault.api.model.SSLCertificateMetadataDetails;
import com.tmobile.cso.vault.api.model.UserDetails;
import com.tmobile.cso.vault.api.process.Response;

@Component
public class CertificateUtils {
	private Logger log = LogManager.getLogger(CertificateUtils.class);
			
	/**
	 * Checks whether a user can be added
	 * @param userDetails
	 * @param safeUser
	 * @return
	 */
	public boolean hasAddOrRemovePermission(UserDetails userDetails, SSLCertificateMetadataDetails certificateMetaData) {			
		if (ObjectUtils.isEmpty(certificateMetaData)) {
			return false;
		}		
		if (userDetails.isAdmin()) {			
			return checkIfCertificateAdmin(userDetails, certificateMetaData);
		}
		else {
			// Prevent the owner of the certificate to be denied...
			return checkIfNonCertificateAdmin(userDetails, certificateMetaData);
		}
	}

	/**
	 * @param userDetails
	 * @param certificateUser
	 * @param action
	 * @param certOwnerid
	 * @return
	 */
	private boolean checkIfNonCertificateAdmin(UserDetails userDetails, SSLCertificateMetadataDetails certificateMetaData) {
		boolean hasAccess = true;
		if (userDetails.getUsername() != null && userDetails.getUsername().equalsIgnoreCase(certificateMetaData.getCertOwnerNtid())) {
			hasAccess = true;			
		}else {
			// other normal users will not have permission as they are not the owner
			hasAccess = false;
		}
		return hasAccess;
	}

	/**
	 * @param certificateUser
	 * @param action
	 * @param certOwnerid
	 * @return
	 */
	private boolean checkIfCertificateAdmin(UserDetails userDetails, SSLCertificateMetadataDetails certificateMetaData) {
		boolean hasAccess = true;
		if (StringUtils.isEmpty(certificateMetaData.getCertOwnerNtid())) {
			// Null or empty user for owner
			// Existing certificate will not have ownerid
			// Certificate created by Certificateadmin will not have ownerid
			hasAccess = true;
		}
		else {
			// There is some owner assigned to the certificate
			if (certificateMetaData.getCertOwnerNtid().equalsIgnoreCase(userDetails.getUsername())) {
				hasAccess = true;				
			}
			else {
				hasAccess = false;
			}
		}
		return hasAccess;
	}
	
	/**
	 * Gets the metadata associated with a given certificate, requires an token which can perform this operation
	 * or token which has certificate admin capabilities
	 * @param token
	 * @param certificateName
	 * @return certificateMetadataDetails
	 */
	public SSLCertificateMetadataDetails getCertificateMetaData(String token, String certificateName){
		String certificatePath = SSLCertificateConstants.SSL_CERT_PATH + '/' + certificateName;
		log.debug(JSONUtil.getJSON(ImmutableMap.<String, String>builder().
				put(LogMessage.USER, ThreadLocalContext.getCurrentMap().get(LogMessage.USER)).
				put(LogMessage.ACTION, "Get Certificate Info").
				put(LogMessage.MESSAGE, String.format ("Trying to get Info for [%s]", certificatePath)).
				put(LogMessage.APIURL, ThreadLocalContext.getCurrentMap().get(LogMessage.APIURL)).
				build()));		
		Response response = ControllerUtil.getReqProcessor().process("/certmanager","{\"path\":\""+certificatePath+"\"}",token);
		// Create the Certificate bean
		SSLCertificateMetadataDetails certificateMetadataDetails = null;
		if(HttpStatus.OK.equals(response.getHttpstatus())){
			try {
				ObjectMapper objMapper = new ObjectMapper();
				JsonNode dataNode = objMapper.readTree(response.getResponse()).get("data");
				certificateMetadataDetails = getCertificateInfo(dataNode);
			} catch (IOException e) {
				log.error(JSONUtil.getJSON(ImmutableMap.<String, String>builder().
					      put(LogMessage.USER, ThreadLocalContext.getCurrentMap().get(LogMessage.USER)).
						  put(LogMessage.ACTION, "getCertificateMetaData").
					      put(LogMessage.MESSAGE, "Error while trying to get details about the certificate").
					      put(LogMessage.APIURL, ThreadLocalContext.getCurrentMap().get(LogMessage.APIURL)).
					      build()));			
			}
		}else {
			log.error(JSONUtil.getJSON(ImmutableMap.<String, String>builder().
				      put(LogMessage.USER, ThreadLocalContext.getCurrentMap().get(LogMessage.USER)).
					  put(LogMessage.ACTION, "getCertificateMetaData").
				      put(LogMessage.MESSAGE, "Error while trying to get certificate metadata").
				      put(LogMessage.APIURL, ThreadLocalContext.getCurrentMap().get(LogMessage.APIURL)).
				      build()));
		}
		log.debug(JSONUtil.getJSON(ImmutableMap.<String, String>builder().
				put(LogMessage.USER, ThreadLocalContext.getCurrentMap().get(LogMessage.USER)).
				put(LogMessage.ACTION, "Get Certificate metadata Info").
				put(LogMessage.MESSAGE, "Getting metaDataInfo completed").
				put(LogMessage.STATUS, response.getHttpstatus().toString()).
				put(LogMessage.APIURL, ThreadLocalContext.getCurrentMap().get(LogMessage.APIURL)).
				build()));
		return certificateMetadataDetails;
	}
	
	/**
	 * Prepare the SSLCertificateMetadataDetails object
	 * @param dataNode
	 * @return certificate
	 */
	public SSLCertificateMetadataDetails getCertificateInfo (JsonNode dataNode) {
		SSLCertificateMetadataDetails certificate = new SSLCertificateMetadataDetails();
		certificate.setCertificateName(dataNode.get("certificateName").asText());
		certificate.setCertificateId(dataNode.get("certificateId").asInt());
		if (null != dataNode.get("certType")) {
			certificate.setCertType(dataNode.get("certType").asText());
		}
		if (null != dataNode.get("akmid")) {
			certificate.setAkmid(dataNode.get("akmid").asText());
		}
		if (null != dataNode.get("applicationName")) {
			certificate.setApplicationName(dataNode.get("applicationName").asText());
		}
		if (null != dataNode.get("applicationOwnerEmailId")) {
			certificate.setApplicationOwnerEmailId(dataNode.get("applicationOwnerEmailId").asText());
		}
		if (null != dataNode.get("applicationTag")) {
			certificate.setApplicationTag(dataNode.get("applicationTag").asText());
		}

		if (null != dataNode.get("certCreatedBy")) {
			certificate.setCertCreatedBy(dataNode.get("certCreatedBy").asText());
		}
		
		if (null != dataNode.get("certOwnerEmailId")) {
			certificate.setCertOwnerEmailId(dataNode.get("certOwnerEmailId").asText());
		}

		if (null != dataNode.get("createDate")) {
			certificate.setCreateDate(dataNode.get("createDate").asText());
		}

		if (null != dataNode.get("certificateStatus")) {
			certificate.setCertificateStatus(dataNode.get("certificateStatus").asText());
		}

		if (null != dataNode.get("expiryDate")) {
			certificate.setExpiryDate(dataNode.get("expiryDate").asText());
		}	
		
		if (null != dataNode.get("certOwnerNtid")) {
			certificate.setCertOwnerNtid(dataNode.get("certOwnerNtid").asText());
		}
		
		return certificate;
	}	
	
}
