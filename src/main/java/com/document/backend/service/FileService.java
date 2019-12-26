package com.document.backend.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.DownloadRequest;
import com.document.backend.packets.GenericResponse;
import com.document.backend.packets.MultipleUploadResponse;
import com.document.backend.packets.UploadResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author navneet
 *
 */
public interface FileService {
	
	/**This method uploads a single file in database and returns the fileName and document id corresponding to that file
	 * @param file
	 * @return
	 */
	public ClientResponse<UploadResponse> uploadFile(MultipartFile file);
	
	/**This method fetches the file by the document record id from database
	 * @param request
	 * @param response
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public ClientResponse<GenericResponse> getFileByRecordId(DownloadRequest request,HttpServletResponse response) throws JsonProcessingException, IOException;
	
	/**This method uploads multiple files in database and returns the fileName and document id corresponding to those files
	 * @param files
	 * @return
	 */
	public ClientResponse<MultipleUploadResponse> uploadMultipleFiles(List<MultipartFile> files);
}
