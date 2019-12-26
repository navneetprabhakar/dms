package com.document.backend.service;

import com.document.backend.packets.ChangePasswordRequest;
import com.document.backend.packets.ClientCreationRequest;
import com.document.backend.packets.ClientCreationResponse;
import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.GenericResponse;

/**
 * @author navneet
 *
 */
public interface UserService {
	
	/**This method adds new client in database
	 * @param request
	 * clientName	:: Name of the client being added.
	 * password		:: Authentication password for the same client.
	 * @return
	 */
	public ClientResponse<ClientCreationResponse> addClient(ClientCreationRequest request);
	
	/**This method updates the existing password for the client
	 * @param request
	 * clientName	:: Name of the client being added.
	 * password		:: Old Authentication password for the same client.
	 * newPassword	:: New Authentication password for the same client.
	 * @return
	 */
	public ClientResponse<GenericResponse> updatePassword(ChangePasswordRequest request);
	
	/**This method resets the password of the client and changes it to the default config value in properties file
	 * @param request
	 * @return
	 */
	public ClientResponse<GenericResponse> resetPassword(String clientName);
}
