package com.document.backend.service.impl;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.document.backend.constants.CommonConstants;
import com.document.backend.constants.ConfigConstants;
import com.document.backend.constants.ErrorCodes;
import com.document.backend.data.models.Clients;
import com.document.backend.packets.ChangePasswordRequest;
import com.document.backend.packets.ClientCreationRequest;
import com.document.backend.packets.ClientCreationResponse;
import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.GenericResponse;
import com.document.backend.service.UserService;
import com.document.backend.service.helper.UserServiceHelper;
import com.document.backend.utils.CommonUtils;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class UserServiceImpl implements UserService {
	
	@Autowired private CommonUtils commonUtils;
	@Autowired private UserServiceHelper userServiceHelper;
	@Autowired private ConfigConstants configConstants;

	@Override
	public ClientResponse<ClientCreationResponse> addClient(ClientCreationRequest request) {
		ClientResponse<ClientCreationResponse> clientResponse=new ClientResponse<>();
		try{
			ClientResponse<GenericResponse> validateResponse=userServiceHelper.validateUserRequest(request);
			if(validateResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)){
				Clients client=new Clients();
				client.setRecordId(commonUtils.getNextSequenceId("Clients", Clients.class));
				client.setClientName(request.getClientName());
				BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
				client.setPassword(passwordEncoder.encode(request.getPassword()));
				commonUtils.save(client);
				log.info("Client created successfully.");
				ClientCreationResponse response=new ClientCreationResponse();
				clientResponse=new ClientResponse<>(CommonConstants.SUCCESS, "Client created successfully.", null, response);
			}else{
				clientResponse.setStatus(validateResponse.getStatus());
				clientResponse.setMessage(validateResponse.getMessage());
				clientResponse.setErrorCode(validateResponse.getErrorCode());
			}
		}catch(Exception e){
			log.error("An Error occurred::",e);
			clientResponse=new ClientResponse<>(CommonConstants.ERROR, "An Error occurred.", ErrorCodes.GENERIC.getCode(), null);
		}
		return clientResponse;
	}

	@Override
	public ClientResponse<GenericResponse> updatePassword(ChangePasswordRequest request) {
		ClientResponse<GenericResponse> clientResponse=new ClientResponse<>();
		try{
			clientResponse=userServiceHelper.validateChangePasswordRequest(request);
			if(clientResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)){
				Map<String, Object> params=new HashMap<>();
				params.put("clientName", request.getClientName());
				Clients client=commonUtils.findOne(params, Clients.class);
				BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
				client.setPassword(passwordEncoder.encode(request.getNewPassword()));
				commonUtils.save(client);
				log.info("Password updated.");
				clientResponse=new ClientResponse<>(CommonConstants.SUCCESS, "Password updated.", null, null);			
			}
		}catch(Exception e){
			log.error("An Error occurred::",e);
			clientResponse=new ClientResponse<>(CommonConstants.ERROR, "An Error occurred.", ErrorCodes.GENERIC.getCode(), null);
		}
		return clientResponse;
	}

	@Override
	public ClientResponse<GenericResponse> resetPassword(String clientName) {
		ClientResponse<GenericResponse> clientResponse=new ClientResponse<>();
		try{
			if(null!=clientName && !clientName.isEmpty()){
				Map<String, Object> params=new HashMap<>();
				params.put("clientName", clientName);
				Clients client=commonUtils.findOne(params, Clients.class);
				if(null!=client){
					BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
					client.setPassword(passwordEncoder.encode(configConstants.getResetPassword()));// TODO randomise and send via mail
					commonUtils.save(client);
					log.info("Password has been reset.");
					clientResponse=new ClientResponse<>(CommonConstants.SUCCESS, "Password has been reset.", null, null);
				}else{
					log.info("Client Name does not exist.");
					clientResponse=new ClientResponse<>(CommonConstants.FAILURE, "Client Name does not exist.", ErrorCodes.INVALID_CLIENT_NAME.getCode(), null);
				}
			}else{
				log.info("Request not valid. Must contain client name.");
				clientResponse=new ClientResponse<>(CommonConstants.FAILURE, "Request not valid. Must contain client name.", ErrorCodes.INVALID_REQUEST.getCode(), null);
			}
		}catch(Exception e){
			log.error("An Error occurred::",e);
			clientResponse=new ClientResponse<>(CommonConstants.ERROR, "An Error occurred.", ErrorCodes.GENERIC.getCode(), null);
		}
		return clientResponse;
	}

}
