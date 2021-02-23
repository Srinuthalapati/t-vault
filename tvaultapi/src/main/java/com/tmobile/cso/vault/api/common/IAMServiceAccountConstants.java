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
package com.tmobile.cso.vault.api.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * IAMServiceAccountConstants Constants
 */
public final class IAMServiceAccountConstants {

	private IAMServiceAccountConstants() {
	}

	public static final String IAM_SVCC_ACC_META_PATH = "metadata/iamsvcacc/";
	public static final String IAM_SVCC_ACC_PATH = "iamsvcacc/";
	public static final String IAMSVCACC_POLICY_PREFIX = "iamsvcacc_";

	public static final String ERROR_INVALID_ACCESS_POLICY_MSG = "Invalid access policy";
	public static final String ADD_USER_TO_IAMSVCACC_MSG = "Add User to IAM Service Account";
	public static final String ADD_GROUP_TO_IAMSVCACC_MSG = "Add Group to IAM Service Account";
	public static final String SSL_OWNER_PERMISSION_EXCEPTION = "Add sudo permission to certificate owner failed";
	public static final String ADD_APPROLE_TO_IAMSVCACC_MSG = "Add Approle to IAM Service Account";
	public static final String REMOVE_USER_FROM_IAMSVCACC_MSG = "Remove User from IAM Service Account";
	public static final String REMOVE_GROUP_FROM_IAMSVCACC_MSG = "Remove Group from IAM Service Account";
	public static final String IAM_SVCACC_POLICY_CREATION_TITLE = "Policies Creation For IAM Service Account";
	public static final String IAM_SVCACC_CREATION_TITLE = "Onboard IAM Service Account";
	public static final String REMOVE_APPROLE_TO_IAMSVCACC_MSG = "Remove Approle from IAM Service Account";
	public static final String IAM_SVCACC_OFFBOARD_CREATION_TITLE = "Offboard IAM Service Account";
	public static final String ROTATE_IAM_SVCACC_TITLE = "Rotate IAM Service Account Secrets";
	public static final String ADD_AWS_ROLE_MSG = "Add AWS Role to IAM Service Account";
	public static final String REMOVE_AWS_ROLE_MSG = "Remove AWS Role from IAM Service Account";
	
	public static final String IAM_ROTATE_MSG_STRING = "rotate";
	public static final String IAM_GROUP_MSG_STRING = "groups";
	public static final String IAM_ACCESS_MSG_STRING = "access";
	public static final String USERNAME_PARAM_STRING = "{\"username\":\"";

	public static final String IAM_EMAIL_TEMPLATE_NAME = "IAMEmailtemplate";
	public static final String IAM_ONBOARD_EMAIL_SUBJECT="Onboarding IAM Service Account %s is successful";
	public static final String IAM_SECRET_FOLDER_PREFIX = "secret_";
	public static final String IAM_AUTH_TOKEN_PREFIX = "cloud-iam";

	public static final Map<String, String> IAM_EMAIL_TEMPLATE_IMAGE_IDS;
	static {
		IAM_EMAIL_TEMPLATE_IMAGE_IDS = Collections.synchronizedMap(new HashMap<String, String>());
		IAM_EMAIL_TEMPLATE_IMAGE_IDS.put("iammanagetab", "templates/images/iammanagetab.png");
		IAM_EMAIL_TEMPLATE_IMAGE_IDS.put("iamviewlink", "templates/images/iamviewlink.png");
		IAM_EMAIL_TEMPLATE_IMAGE_IDS.put("update", "templates/images/update.png");
		IAM_EMAIL_TEMPLATE_IMAGE_IDS.put("iamactivate", "templates/images/iamactivate.png");
		IAM_EMAIL_TEMPLATE_IMAGE_IDS.put("iampermission", "templates/images/iampermission.png");
		IAM_EMAIL_TEMPLATE_IMAGE_IDS.put("permissiontab", "templates/images/permissiontab.png");
		IAM_EMAIL_TEMPLATE_IMAGE_IDS.put("adduser", "templates/images/adduser.png");
		IAM_EMAIL_TEMPLATE_IMAGE_IDS.put("iamrotate", "templates/images/iamrotate.png");
	}
	
	public static final String ACCESS_KEY_ID = "accessKeyId";
	public static final String OWNER_NT_ID = "owner_ntid";
	
	public static final String ROTATE_IAM_SECRET = "rotateIAMSecret";
	public static final String GET_IAMAPPROLE_TOKEN = "getIAMApproleToken";
	public static final String DELETE_AWSROLE_ASSOCIATION = "Remove AwsRole Association On IAM Service Account Offboard";
}
