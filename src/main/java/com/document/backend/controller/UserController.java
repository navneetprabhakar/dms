package com.document.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.document.backend.packets.ChangePasswordRequest;
import com.document.backend.packets.ClientCreationRequest;
import com.document.backend.packets.ClientCreationResponse;
import com.document.backend.packets.ClientRequest;
import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.GenericResponse;
import com.document.backend.service.UserService;

/**
 * @author navneet
 *
 */
@RestController
public class UserController {
	
	@Autowired private UserService userService;
	
	@PostMapping("addClient")
	public ClientResponse<ClientCreationResponse> addClient(@RequestBody ClientRequest<ClientCreationRequest> clientRequest){
		return userService.addClient(clientRequest.getData());
	}
	@PostMapping("changePassword")
	public ClientResponse<GenericResponse> changePassword(@RequestBody ClientRequest<ChangePasswordRequest> clientRequest){
		return userService.updatePassword(clientRequest.getData());
	}
	@GetMapping("resetPassword/{clientName}")
	public ClientResponse<GenericResponse> resetPassword(@PathVariable String clientName){
		return userService.resetPassword(clientName);
	}
	

}
