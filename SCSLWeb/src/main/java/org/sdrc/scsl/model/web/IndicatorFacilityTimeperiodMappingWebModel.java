package org.sdrc.scsl.model.web;

public class IndicatorFacilityTimeperiodMappingWebModel {

	private int indFacilityTpId;
	private String createdDate;
	private int facilityId;
	private int indicatorId;
	private int timePeriodId;
	
	public int getIndFacilityTpId() {
		return indFacilityTpId;
	}
	public void setIndFacilityTpId(int indFacilityTpId) {
		this.indFacilityTpId = indFacilityTpId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public int getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}
	public int getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(int indicatorId) {
		this.indicatorId = indicatorId;
	}
	public int getTimePeriodId() {
		return timePeriodId;
	}
	public void setTimePeriodId(int timePeriodId) {
		this.timePeriodId = timePeriodId;
	}
	@Override
	public String toString() {
		return "IndicatorFacilityTimeperiodMappingWebModel [indFacilityTpId=" + indFacilityTpId + ", createdDate="
				+ createdDate + ", facilityId=" + facilityId + ", indicatorId=" + indicatorId + ", timePeriodId="
				+ timePeriodId + "]";
	}
	
	
}
