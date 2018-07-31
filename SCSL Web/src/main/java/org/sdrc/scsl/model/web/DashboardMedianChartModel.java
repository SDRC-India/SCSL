package org.sdrc.scsl.model.web;

/** 
* @author Sarita Panigrahi, created on: 11-Oct-2017
* This model class holds median/P-Average and controlchart model
*/
public class DashboardMedianChartModel {

	private Double pAverage;
	private DashboardChartModel dashboardChartModel;
	
	public Double getpAverage() {
		return pAverage;
	}
	public void setpAverage(Double pAverage) {
		this.pAverage = pAverage;
	}
	public DashboardChartModel getDashboardChartModel() {
		return dashboardChartModel;
	}
	public void setDashboardChartModel(DashboardChartModel dashboardChartModel) {
		this.dashboardChartModel = dashboardChartModel;
	}
	
	
}
