package com.document.backend.packets;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientCreationResponse extends GenericResponse{
	private Integer clientId;
}
