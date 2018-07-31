package org.sdrc.scsl.model.web;

import java.util.List;

/** 
* @author Sarita Panigrahi, created on: 11-Oct-2017
* this model class will be used while downloading excel in small multiple view
*/
public class SmallMultipleExcelModel {

	private List<String> chartSvgs; //hold the chart svgs
	private List<List<DashboardChartModel>> dashboardChartModels;//holds chart data
	private String facilityName; //in facility view indicatorName will hold indicator name and in indicator view - facilityName variable will hold the facility name
	private String indicatorName;
	//for indicator view
	//if selected
	private String wave;
	private String state;
	private String district;
	private String facilityType;
	private String facilitySize;
	private String indicatorType;
	private String coreArea;
	private String periodicity;
	
	public List<String> getChartSvgs() {
		return chartSvgs;
	}
	public void setChartSvgs(List<String> chartSvgs) {
		this.chartSvgs = chartSvgs;
	}
	public List<List<DashboardChartModel>> getDashboardChartModels() {
		return dashboardChartModels;
	}
	public void setDashboardChartModels(List<List<DashboardChartModel>> dashboardChartModels) {
		this.dashboardChartModels = dashboardChartModels;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	public String getWave() {
		return wave;
	}
	public void setWave(String wave) {
		this.wave = wave;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getFacilityType() {
		return facilityType;
	}
	public void setFacilityType(String facilityType) {
		this.facilityType = facilityType;
	}
	public String getFacilitySize() {
		return facilitySize;
	}
	public void setFacilitySize(String facilitySize) {
		this.facilitySize = facilitySize;
	}
	public String getIndicatorType() {
		return indicatorType;
	}
	public void setIndicatorType(String indicatorType) {
		this.indicatorType = indicatorType;
	}
	public String getCoreArea() {
		return coreArea;
	}
	public void setCoreArea(String coreArea) {
		this.coreArea = coreArea;
	}
	public String getPeriodicity() {
		return periodicity;
	}
	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}
	
	
}
