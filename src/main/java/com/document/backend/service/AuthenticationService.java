package com.document.backend.service;

import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.GenericResponse;

/**
 * @author navneet
 *
 */
public interface AuthenticationService {
	
	/**This method authenticates the client credentials from database 
	 * @param clientName
	 * @param password
	 * @return
	 */
	public ClientResponse<GenericResponse> authenticateClient(String clientName,String password);

}
