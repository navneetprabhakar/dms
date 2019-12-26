package com.document.backend.packets;

import lombok.Data;

@Data
public class ClientCreationRequest {
	private String clientName;
	private String password;
	
}
