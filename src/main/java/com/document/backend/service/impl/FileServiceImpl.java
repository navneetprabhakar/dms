package com.document.backend.service.impl;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.document.backend.constants.CommonConstants;
import com.document.backend.constants.ConfigConstants;
import com.document.backend.constants.ErrorCodes;
import com.document.backend.data.models.DocumentMapping;
import com.document.backend.packets.ClientResponse;
import com.document.backend.packets.DownloadRequest;
import com.document.backend.packets.GenericResponse;
import com.document.backend.packets.MultipleUploadResponse;
import com.document.backend.packets.UploadResponse;
import com.document.backend.service.FileService;
import com.document.backend.service.helper.FileServiceHelper;
import com.document.backend.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class FileServiceImpl implements FileService{
	
	@Autowired private FileServiceHelper fileServiceHelper;
	@Autowired private GridFsTemplate gridFsTemplate;
	@Autowired private CommonUtils commonUtils;
	@Autowired ConfigConstants configConstants;

	@Override
	public ClientResponse<UploadResponse> uploadFile(MultipartFile uploadfile) {
		ClientResponse<UploadResponse> clientResponse = new ClientResponse<>();
		log.debug("File upload initiated.");
		try {
			if (null != uploadfile && !uploadfile.isEmpty()) {
				Integer uploadId = fileServiceHelper.uploadFile(uploadfile);
				log.info("File Upload successful.");
				UploadResponse response = new UploadResponse(uploadId, uploadfile.getOriginalFilename());
				clientResponse = new ClientResponse<>(CommonConstants.SUCCESS,"File Upload successful.", null, response);
			} else {
				log.info("Please select a file!");
				clientResponse=new ClientResponse<>(CommonConstants.FAILURE, "Please select a file!", ErrorCodes.NO_FILE_SELECTED.getCode(), null);
			}
		} catch (Exception e) {
			log.error("An Error occurred while uploading file::", e);
			clientResponse=new ClientResponse<>(CommonConstants.ERROR, "An Error occurred while uploading file.", ErrorCodes.GENERIC.getCode(), null);
		}
		return clientResponse;
	}

	@Override
	public ClientResponse<GenericResponse> getFileByRecordId(DownloadRequest request, HttpServletResponse response) throws JsonProcessingException, IOException {
		ClientResponse<GenericResponse> clientResponse=new ClientResponse<>();
		try {
			log.info("Initiating Document download.");
			if (null != request && null != request.getDocumentId()) {
				log.info("Searching Document by document Id.");
				Map<String, Object> params=new HashMap<>();
				params.put("recordId", request.getDocumentId());
				DocumentMapping docMapping=commonUtils.findOne(params, DocumentMapping.class);
				if(null!=docMapping){
					log.info("Document found in database.");
					GridFSDBFile gridFsdbFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(docMapping.getDocId())));
					if(null!=gridFsdbFile){
						String mimeType = URLConnection.guessContentTypeFromName(docMapping.getDocumentName());
						if (mimeType == null) {
							mimeType = "application/octet-stream";
						}
						response.setContentType(mimeType);
						response.setHeader("Content-Disposition", String.format("inline; filename=\"" + docMapping.getDocumentName() + "\""));
						response.setContentLength((int) gridFsdbFile.getLength());
						gridFsdbFile.writeTo(response.getOutputStream());
					}else{
						log.info("Document not present in database.");
						clientResponse = new ClientResponse<>(CommonConstants.FAILURE,"Document not present in database.", ErrorCodes.DOC_NOT_FOUND.getCode(), null);
					}
				}else{
					log.info("Document Id not present in database.");
					clientResponse = new ClientResponse<>(CommonConstants.FAILURE,"Document Id not present in database.", ErrorCodes.DOC_NOT_FOUND.getCode(), null);
				}
			} else {
				log.info("Download Request not proper. Please enter the document Id.");
				clientResponse = new ClientResponse<>(CommonConstants.FAILURE,"Download Request not proper. Please enter the document Id.", ErrorCodes.INVALID_REQUEST.getCode(), null);
				response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(clientResponse));
			}
		}catch(Exception e){
			log.error("An Error occurred::",e);
			clientResponse=new ClientResponse<>(CommonConstants.ERROR, "An Error occurred.", ErrorCodes.GENERIC.getCode(), null);
			response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(clientResponse));
		}
		return clientResponse;
		
	}

	@Override
	public ClientResponse<MultipleUploadResponse> uploadMultipleFiles(List<MultipartFile> files) {
		ClientResponse<MultipleUploadResponse> clientResponse=new ClientResponse<>();
		try {
			if(null!=files && !files.isEmpty()) {
				List<UploadResponse> list=new ArrayList<>();
				for(MultipartFile uploadfile:files) {
					Integer uploadId=fileServiceHelper.uploadFile(uploadfile);
					log.info("File name:"+uploadfile.getOriginalFilename()+" uploaded succesfully.");
					list.add(new UploadResponse(uploadId, uploadfile.getOriginalFilename()));
				}
				log.info("Total files uploaded successfully:"+files.size());
				clientResponse=new ClientResponse<>(CommonConstants.SUCCESS, "Total files uploaded successfully:"+files.size(), null, new MultipleUploadResponse(list));
			}else {
				log.info("Multiple file upload request cannot be empty.");
				clientResponse=new ClientResponse<>(CommonConstants.FAILURE, "Multiple file upload request cannot be empty.", ErrorCodes.NO_FILE_SELECTED.getCode(), null);
			}
		}catch(Exception e) {
			log.error("An Error occurred while uploading multiple files::",e);
			clientResponse=new ClientResponse<>(CommonConstants.ERROR, "An Error occurred while uploading multiple files.", ErrorCodes.GENERIC.getCode(), null);
		}
		return clientResponse;
	}
	

}
