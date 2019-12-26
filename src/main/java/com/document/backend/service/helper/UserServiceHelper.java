package com.document.backend.service.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.document.backend.constants.CommonConstants;
import com.document.backend.constants.ErrorCodes;
import com.document.backend.data.models.Clients;
import com.document.backend.packets.ChangePasswordRequest;
import com.document.backend.packets.ClientCreationRequest;
import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.GenericResponse;
import com.document.backend.utils.CommonUtils;

import lombok.extern.log4j.Log4j;

/**
 * @author navneet
 *
 */
@Service
@Log4j
public class UserServiceHelper {
	
	@Autowired private CommonUtils commonUtils;
	
	/**This method validates the client creation request
	 * @param request
	 * clientName	:: Cannot be empty or duplicate and must only contain alphabets
	 * password		:: Cannot be empty (as of now no password policy has been implemented)
	 * @return
	 */
	public ClientResponse<GenericResponse> validateUserRequest(ClientCreationRequest request){
		if(null==request){
			log.info("Client Creation Request cannot be empty.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Client Creation Request cannot be empty.", ErrorCodes.INVALID_REQUEST.getCode(), null);
		}
		if(null==request.getClientName() || request.getClientName().isEmpty()){
			log.info("Client name cannot be empty.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Client name cannot be empty.", ErrorCodes.INVALID_CLIENT_NAME.getCode(), null);
		}
		if(!commonUtils.isName(request.getClientName())){
			log.info("Client name not valid.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Client name not valid.", ErrorCodes.INVALID_CLIENT_NAME.getCode(), null);
		}
		Map<String, Object> params=new HashMap<>();
		params.put("clientName", request.getClientName());
		Clients client=commonUtils.findOne(params, Clients.class);
		if(null!=client){
			log.info("Client already exists.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Client already exists.", ErrorCodes.DUPLICATE_CLIENT_NAME.getCode(), null);
		}
		if(null==request.getPassword() || request.getPassword().isEmpty()){
			log.info("Password cannot be empty.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Password cannot be empty.", ErrorCodes.INVALID_PASSWORD.getCode(), null);
		}
		log.info("Client Creation Request validated.");
		return new ClientResponse<>(CommonConstants.SUCCESS, "Client Creation Request validated.", null, null);
	}
	
	/**This method validated the password change request
	 * @param request
	 * clientName	:: Cannot be empty and client must exist in database
	 * password		:: Cannot be empty, must match with the existing 
	 * newPassword	:: Cannot be empty, must not match with 'password'
	 * @return
	 */
	public ClientResponse<GenericResponse> validateChangePasswordRequest(ChangePasswordRequest request) {
		if (null == request) {
			log.info("Client Creation Request cannot be empty.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Client Creation Request cannot be empty.",
					ErrorCodes.INVALID_REQUEST.getCode(), null);
		}
		if (null == request.getClientName() || request.getClientName().isEmpty()) {
			log.info("Client name cannot be empty.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Client name cannot be empty.",
					ErrorCodes.INVALID_CLIENT_NAME.getCode(), null);
		}
		if (!commonUtils.isName(request.getClientName())) {
			log.info("Client name not valid.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Client name not valid.",
					ErrorCodes.INVALID_CLIENT_NAME.getCode(), null);
		}
		if (null == request.getNewPassword() || request.getNewPassword().isEmpty()) {
			log.info("New Password cannot be empty.");
			return new ClientResponse<>(CommonConstants.FAILURE, "New Password cannot be empty.",
					ErrorCodes.INVALID_REQUEST.getCode(), null);
		}
		if (request.getPassword().equals(request.getNewPassword())) {
			log.info("Old Password cannot be same as new Password.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Old Password cannot be same as new Password.",
					ErrorCodes.INVALID_PASSWORD.getCode(), null);
		}
		Map<String, Object> params = new HashMap<>();
		params.put("clientName", request.getClientName());
		Clients client = commonUtils.findOne(params, Clients.class);
		if(null==client) {
			log.info("Invalid client name.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Invalid client name.",
					ErrorCodes.INVALID_CLIENT_NAME.getCode(), null);
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (!passwordEncoder.matches(request.getPassword(), client.getPassword())) {
			log.info("Password does not match.");
			return new ClientResponse<>(CommonConstants.FAILURE, "Password does not match.",
					ErrorCodes.WRONG_PASSWORD.getCode(), null);
		}
		log.info("Change Password request validated.");
		return new ClientResponse<>(CommonConstants.SUCCESS, "Change Password request validated.", null, null);

	}
	
}
