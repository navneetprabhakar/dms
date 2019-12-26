package com.document.backend.packets;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientRequest<T> {
	private T data;
}
