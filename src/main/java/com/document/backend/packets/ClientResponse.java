package com.document.backend.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse<T> {
	private String status;
	private String message;
	private String errorCode;
	private T data;

}
