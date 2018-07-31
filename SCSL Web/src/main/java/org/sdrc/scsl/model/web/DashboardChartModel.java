package org.sdrc.scsl.model.web;

/** 
* @author Sarita Panigrahi, created on: 27-Sep-2017
* This model class is used to contain the data for dashboard charts.
*/
public class DashboardChartModel {

	//chart type: UCL/LCL/CL
	private Integer timeNid;
	private Integer indicatorOrder;
	private String key;
	private String date;
	private Double value;
	private Double andhraValue;
	private Double telanganaValue;
	private String name;
	private String source;
	private String pdsas; //for facility view. if any
	
	public Integer getIndicatorOrder() {
		return indicatorOrder;
	}
	public void setIndicatorOrder(Integer indicatorOrder) {
		this.indicatorOrder = indicatorOrder;
	}
	public Integer getTimeNid() {
		return timeNid;
	}
	public void setTimeNid(Integer timeNid) {
		this.timeNid = timeNid;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Double getAndhraValue() {
		return andhraValue;
	}
	public void setAndhraValue(Double andhraValue) {
		this.andhraValue = andhraValue;
	}
	public Double getTelanganaValue() {
		return telanganaValue;
	}
	public void setTelanganaValue(Double telanganaValue) {
		this.telanganaValue = telanganaValue;
	}
	public String getPdsas() {
		return pdsas;
	}
	public void setPdsas(String pdsas) {
		this.pdsas = pdsas;
	}
	
}
