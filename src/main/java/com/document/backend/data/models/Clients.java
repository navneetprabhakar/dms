package com.document.backend.data.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="Clients")
public class Clients extends AuditEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer recordId;
	private String clientName;
	private String password;
	
}
