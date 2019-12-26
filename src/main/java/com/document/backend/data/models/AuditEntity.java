package com.document.backend.data.models;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * @author navneet.prabhakar
 *
 */
@JsonIgnoreProperties({"id","new","createdDate","lastModifiedDate","createdBy","lastModifiedBy","version","iMillis","iChronology"})
public class AuditEntity implements Auditable<String, String> {
	private static final long serialVersionUID = -2852364378848640063L;
	@Id
	private String id;
	@Version
    private Long version;
	@Indexed
	@CreatedDate
	private DateTime createdDate;
	@LastModifiedDate
	private DateTime lastModifiedDate;
	private String createdBy;
	private String lastModifiedBy;
	
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	
	@Override
	public String getId() {
		return id;
	}
	public void setId(String Id) {
		this.id = Id;
	}
	@Override
	public boolean isNew() {
		return id == null;
	}
	@Override
	public String getCreatedBy() {
		return createdBy;
	}
	@Override
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Override
	public DateTime getCreatedDate() {
		return createdDate;
	}
	@Override
	public void setCreatedDate(DateTime creationDate) {
		this.createdDate = creationDate;
	}
	@Override
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	@Override
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	@Override
	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}
	@Override
	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
