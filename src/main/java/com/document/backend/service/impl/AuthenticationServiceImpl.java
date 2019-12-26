package com.document.backend.service.impl;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.document.backend.constants.CommonConstants;
import com.document.backend.constants.ErrorCodes;
import com.document.backend.data.models.Clients;
import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.GenericResponse;
import com.document.backend.service.AuthenticationService;
import com.document.backend.utils.CommonUtils;

import lombok.extern.log4j.Log4j;
@Service
@Log4j
public class AuthenticationServiceImpl implements AuthenticationService{
	
	@Autowired private CommonUtils commonUtils;

	@Override
	public ClientResponse<GenericResponse> authenticateClient(String clientName, String password) {
		ClientResponse<GenericResponse> clientResponse=new ClientResponse<>();
		try {
			if (null != clientName && !clientName.isEmpty() && null != password && !password.isEmpty()) {
				Map<String, Object> params = new HashMap<>();
				params.put("clientName", clientName);
				Clients client = commonUtils.findOne(params, Clients.class);
				if (null != client) {
					BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
					if(passwordEncoder.matches(password, client.getPassword())){
						clientResponse=new ClientResponse<>(CommonConstants.SUCCESS, "Client Name and password validated.", null, null);
					}else{
						log.info("Password not correct. Please try again.");
						clientResponse=new ClientResponse<>(CommonConstants.FAILURE, "Password not correct. Please try again.", ErrorCodes.WRONG_PASSWORD.getCode(), null);
					}
				}else{
					log.info("Client Name not correct. Please try again.");
					clientResponse=new ClientResponse<>(CommonConstants.FAILURE, "Client Name not correct. Please try again.", ErrorCodes.INVALID_CLIENT_NAME.getCode(), null);
				}
			}else{
				log.info("Client Name and Password cannot be empty.");
				clientResponse=new ClientResponse<>(CommonConstants.FAILURE, "Client Name and Password cannot be empty.", ErrorCodes.INVALID_REQUEST.getCode(), null);
			}
		}catch(Exception e){
			log.error("An Error occurred::",e);
			clientResponse=new ClientResponse<>(CommonConstants.ERROR, "An Error occurred.", ErrorCodes.GENERIC.getCode(), null);
		}
		return clientResponse;
	}

}
