package com.document.backend.data.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Document(collection="Document_Mapping")
public class DocumentMapping extends AuditEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer recordId;
	private String documentName;
	private String documentType;
	private String docId;
}
