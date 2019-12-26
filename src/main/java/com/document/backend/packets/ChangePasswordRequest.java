package com.document.backend.packets;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ChangePasswordRequest extends ClientCreationRequest{
	private String newPassword;

}
