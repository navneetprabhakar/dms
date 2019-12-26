package com.document.backend.packets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UploadResponse extends DownloadRequest {
	private String fileName;
	public UploadResponse(Integer documentId,String fileName) {
		super(documentId);
		this.fileName=fileName;
	}
}
