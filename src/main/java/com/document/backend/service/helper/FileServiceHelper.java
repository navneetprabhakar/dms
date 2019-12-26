package com.document.backend.service.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.document.backend.constants.ConfigConstants;
import com.document.backend.data.models.DocumentMapping;
import com.document.backend.utils.CommonUtils;

import lombok.extern.log4j.Log4j;
/**
 * @author navneet
 *
 */
@Service
@Log4j
public class FileServiceHelper {
	
	@Autowired private GridFsTemplate gridFsTemplate;
	@Autowired private ConfigConstants configConstants;
	@Autowired private CommonUtils commonUtils;
	
	/**This method saves the multipart file on the disk and returns the filepath
	 * @param file
	 * @return
	 */
	public String saveUploadedFile(MultipartFile file) {
		try {
			if (null != file && !file.isEmpty()) {
				byte[] bytes = file.getBytes();
				String filePath = configConstants.getFilePath() + file.getOriginalFilename();
				File create=new File(configConstants.getFilePath());
				if(!create.exists()){
					create.mkdirs();
				}
				Path path = Paths.get(filePath);
				Files.write(path, bytes);
				return filePath;
			}
		} catch (Exception e) {
			log.error("An Error occurred while saving file::", e);
		}
		return null;
	}
	
	/**This method saves the file in mongoDB along with the document mapping
	 * @param uploadfile
	 * @return
	 * @throws FileNotFoundException
	 */
	public Integer uploadFile(MultipartFile uploadfile) throws FileNotFoundException {
		String filePath = saveUploadedFile(uploadfile);
		if (null != filePath && !filePath.isEmpty()) {
			InputStream inputStream=new FileInputStream(filePath);
			String docId =  gridFsTemplate.store(inputStream, uploadfile.getOriginalFilename(), uploadfile.getContentType()).getId().toString();
			DocumentMapping docMapping=new DocumentMapping();
			docMapping.setRecordId(commonUtils.getNextSequenceId("Document_Mapping", DocumentMapping.class));
			docMapping.setDocId(docId);
			docMapping.setDocumentName(uploadfile.getOriginalFilename());
			docMapping.setDocumentType(uploadfile.getContentType());
			commonUtils.save(docMapping);
			return docMapping.getRecordId();
		}
		return null;
	}

}
