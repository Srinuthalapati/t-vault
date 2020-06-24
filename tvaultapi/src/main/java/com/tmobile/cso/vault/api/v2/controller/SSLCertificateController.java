//=========================================================================
//Copyright 2020 T-Mobile, US
//
//Licensed under the Apache License, Version 2.0 (the "License")
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//See the readme.txt file for additional language around disclaimer of warranties.
//=========================================================================
package com.tmobile.cso.vault.api.v2.controller;


import com.tmobile.cso.vault.api.model.CertManagerLoginRequest;
import com.tmobile.cso.vault.api.model.SSLCertificateRequest;
import com.tmobile.cso.vault.api.model.UserDetails;
import com.tmobile.cso.vault.api.service.SSLCertificateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin
@Api( description = "SSL Certificate  Management Controller", position = 15)
public class SSLCertificateController {

	@Autowired
	private SSLCertificateService sslCertificateService;

	/**
	 * To authenticate with Certificate Lifecycle Manager
	 * @param certManagetLoginRequest
	 * @return
	 */
	@ApiOperation(value = "${SSLCertificateController.login.value}", notes = "${SSLCertificateController.login.notes}")
	@PostMapping(value="/v2/auth/sslcert/login",produces="application/json")
	public ResponseEntity<String> authenticate(@RequestBody CertManagerLoginRequest certManagetLoginRequest) throws Exception {
		return sslCertificateService.authenticate(certManagetLoginRequest);
	}
	/**
	 * To Create SSL Certificate
	 * @param sslCertificateRequest
	 * @return
	 */
	@ApiOperation(value = "${SSLCertificateController.sslcreate.value}", notes = "${SSLCertificateController.sslcreate.notes}")
	@PostMapping(value="/v2/sslcert",consumes="application/json",produces="application/json")
	public ResponseEntity<String> generateSSLCertificate(HttpServletRequest request, @RequestHeader(value=
			"vault-token") String token,@Valid @RequestBody SSLCertificateRequest sslCertificateRequest)  {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return sslCertificateService.generateSSLCertificate(sslCertificateRequest,userDetails,token);
	}
	
	/**
	 * To get list of certificates in a container
	 * @param request
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "${SSLCertificateController.getssl.value}", notes = "${SSLCertificateController.getssl.notes}")
	@GetMapping(value="/v2/sslcert", produces="application/json")
	public ResponseEntity<String> getCertificates(HttpServletRequest request, @RequestHeader(value="vault-token") String token, @RequestParam(name="certificateName", required = false) String certName)throws Exception{
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return sslCertificateService.getServiceCertificates(token, userDetails, certName);
	}

	/**
	 * Get target system list.
	 * @param request
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "${CertificateController.getTargetSystemList.value}", notes = "${CertificateController.getTargetSystemList.notes}")
	@GetMapping(value = "/sslcert/targetsystems", produces = "application/json")
	public ResponseEntity<String> getTargetSystemList(HttpServletRequest request, @RequestHeader(value = "vault-token") String token) throws Exception {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return sslCertificateService.getTargetSystemList(token, userDetails);
	}

	/**
	 * Get service list from a target system.
	 * @param request
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "${CertificateController.getTargetSystemServiceList.value}", notes = "${CertificateController.getTargetSystemServiceList.value}")
	@GetMapping(value = "/sslcert/targetsystems/{targetsystem_id}/targetsystemservices", produces = "application/json")
	public ResponseEntity<String> getTargetSystemServiceList(HttpServletRequest request, @RequestHeader(value = "vault-token") String token, @PathVariable("targetsystem_id") String targetSystemId) throws Exception {
		UserDetails userDetails = (UserDetails) ((HttpServletRequest) request).getAttribute("UserDetails");
		return sslCertificateService.getTargetSystemServiceList(token, userDetails, targetSystemId);
	}

}