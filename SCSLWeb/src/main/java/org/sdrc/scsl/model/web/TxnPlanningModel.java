/**
 * 
 */
package org.sdrc.scsl.model.web;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * This model will be used for planning and upload report while uploading visit planning data
 */
public class TxnPlanningModel {
	
	// will be zero while doing planning
	private int planningId;
	
	//contains agenda while planning and visit report while uploading visit report
	private MultipartFile document; 
	
	
	// while planning it will hold the date for which planning
	//while uploading visit report it will hold the date of visit
	private String date; 
	
	// contains the email ids tag filled up by user at time of planning  
	private String tagEmailIds;
	
	// will only be available at the time of the uploading of trip report
	private String tripDescription;
	
	// while planning this model will hold the id of facility for which planning is planned
	private int facilityId;

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public int getPlanningId() {
		return planningId;
	}

	public void setPlanningId(int planningId) {
		this.planningId = planningId;
	}

	public MultipartFile getDocument() {
		return document;
	}

	public void setDocument(MultipartFile document) {
		this.document = document;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTagEmailIds() {
		return tagEmailIds;
	}

	public void setTagEmailIds(String tagEmailIds) {
		this.tagEmailIds = tagEmailIds;
	}

	public String getTripDescription() {
		return tripDescription;
	}

	public void setTripDescription(String tripDescription) {
		this.tripDescription = tripDescription;
	}
	
}
