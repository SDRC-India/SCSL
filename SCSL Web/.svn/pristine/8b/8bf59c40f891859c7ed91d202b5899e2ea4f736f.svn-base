/**
 * 
 */
package org.sdrc.scsl.model.web;

import java.util.List;
import java.util.Map;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class FacilityPlanningModel {
	
	
	// facilityId
	private int facilityId;
	
	
	// name of the facility
	private String facilityName;
	
	
	// planning history of that facility for next week
	private List<ValueObject> plannedHistory;
	
	
	// this will contain name of user in key of map and its assessment history of that facility 
	private Map<String,List<AssessmentHistory>> assessmentHistory;
	
	// will be true if atleast one planning is available for next week
	private boolean isPlanned;
	
	// contains the dates available for that user  of that facility and its corresponding planningID
	private List<ValueObject> realeaseDate;
	
	
	// Id of district it belongs to
	private int parentId;
	
	private int stateId;
	

	//if for a facility for any logged in user if its any of the plan is pending then it will be true
	private boolean isPending;
	
	//List of pending planning available
	/*private List<ValueObject> pendingPlanning;*/
	

/*	public List<ValueObject> getPendingPlanning() {
		return pendingPlanning;
	}

	public void setPendingPlanning(List<ValueObject> pendingPlanning) {
		this.pendingPlanning = pendingPlanning;
	}*/

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public boolean isPending() {
		return isPending;
	}

	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}

	public List<ValueObject> getRealeaseDate() {
		return realeaseDate;
	}

	public void setRealeaseDate(List<ValueObject> realeaseDate) {
		this.realeaseDate = realeaseDate;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public List<ValueObject> getPlannedHistory() {
		return plannedHistory;
	}

	public void setPlannedHistory(List<ValueObject> plannedHistory) {
		this.plannedHistory = plannedHistory;
	}

	public Map<String, List<AssessmentHistory>> getAssessmentHistory() {
		return assessmentHistory;
	}

	public void setAssessmentHistory(Map<String, List<AssessmentHistory>> assessmentHistory) {
		this.assessmentHistory = assessmentHistory;
	}

	public boolean isPlanned() {
		return isPlanned;
	}

	public void setPlanned(boolean isPlanned) {
		this.isPlanned = isPlanned;
	}

}
