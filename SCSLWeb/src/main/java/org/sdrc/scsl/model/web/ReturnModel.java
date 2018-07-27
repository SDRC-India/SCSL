package org.sdrc.scsl.model.web;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
*	created date : 27-04-2017
*	this class will have status codes and status messages, error messages
*/
public class ReturnModel {

	private String statusCode;
	private String statusMessage;
	private String errorMessage;
	
	public String getStatusCode() {
		return statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
