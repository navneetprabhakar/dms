package com.document.backend.data.models;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author navneet.prabhakar
 *
 */
@Document(collection = "counters")
public class Counters extends AuditEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -119798982467190427L;
	/**
	 * 
	 */
	private String name;
	private Integer seqId = 1;
	/**
	 * @return the seqId
	 */
	public Integer getSeqId() {
		return seqId;
	}
	/**
	 * @param seqId the seqId to set
	 */
	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}

