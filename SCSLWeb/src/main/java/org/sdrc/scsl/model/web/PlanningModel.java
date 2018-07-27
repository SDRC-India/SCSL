/**
 * 
 */
package org.sdrc.scsl.model.web;

import java.util.List;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class PlanningModel {

	// List of state and district
	private List<AreaWebModel> areaModel;
	
	 
	//List of all the facilities
	private List<FacilityPlanningModel> facilityPlanningModel;
	
	// startDate of week
	private String startDate;

	// endDate of week
	private String endDate;

	// server date 
	private String serverDate;


	public List<AreaWebModel> getAreaModel() {
		return areaModel;
	}

	public void setAreaModel(List<AreaWebModel> areaModel) {
		this.areaModel = areaModel;
	}

	public List<FacilityPlanningModel> getFacilityPlanningModel() {
		return facilityPlanningModel;
	}

	public void setFacilityPlanningModel(
			List<FacilityPlanningModel> facilityPlanningModel) {
		this.facilityPlanningModel = facilityPlanningModel;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getServerDate() {
		return serverDate;
	}

	public void setServerDate(String serverDate) {
		this.serverDate = serverDate;
	}
}
