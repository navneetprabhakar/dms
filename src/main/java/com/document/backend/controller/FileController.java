package com.document.backend.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.document.backend.packets.ClientRequest;
import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.DownloadRequest;
import com.document.backend.packets.GenericResponse;
import com.document.backend.packets.MultipleUploadResponse;
import com.document.backend.packets.UploadResponse;
import com.document.backend.service.FileService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author navneet
 *
 */
@RestController
public class FileController {
	
	@Autowired private FileService fileService;

	@PostMapping("uploadFile")
	public ClientResponse<UploadResponse> uploadFile(@RequestParam("file") MultipartFile uploadfile) {
		return fileService.uploadFile(uploadfile);
	}
	@PostMapping("uploadMultipleFiles")
	public ClientResponse<MultipleUploadResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] uploadFiles){
		List<MultipartFile> files=new ArrayList<>(Arrays.asList(uploadFiles));
		return fileService.uploadMultipleFiles(files);
	}
	@GetMapping("downloadFile/{docId}")
	public void downloadFile(@PathVariable Integer docId,HttpServletResponse response) throws JsonProcessingException, IOException{
		DownloadRequest request=new DownloadRequest();
		request.setDocumentId(docId);
		fileService.getFileByRecordId(request, response);
	}
	
	@PostMapping("downloadFile")
	public ClientResponse<GenericResponse> downloadFilePost(@RequestBody ClientRequest<DownloadRequest> clientRequest,HttpServletResponse response) throws JsonProcessingException, IOException{
		return fileService.getFileByRecordId(clientRequest.getData(), response);
	}
}
