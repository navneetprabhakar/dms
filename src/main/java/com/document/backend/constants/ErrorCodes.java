package com.document.backend.constants;

/**Enum for system generated error codes
 * @author navneet
 *
 */
public enum ErrorCodes {
	GENERIC("100"),
	UNABLE_TO_READ("101"),
	NO_FILE_SELECTED("102"),
	INVALID_REQUEST("103"),
	DOC_NOT_FOUND("104"),
	INVALID_HEADER("105"),
	WRONG_PASSWORD("106"),
	INVALID_CLIENT_NAME("107"),
	INVALID_PASSWORD("108"),
	DUPLICATE_CLIENT_NAME("109");
	private String code;
	private ErrorCodes(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}
	
}
