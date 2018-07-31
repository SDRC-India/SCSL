package org.sdrc.scsl.model.web;

public class ExceptionModel {

	private String exceptionType;
	
	private String exceptionMessage;
	
	private String statusCode;

	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String toString() {
		return "ExceptionModel [exceptionType=" + exceptionType + ", exceptionMessage=" + exceptionMessage
				+ ", statusCode=" + statusCode + "]";
	}
	
	
	
}
