package org.sdrc.scsl.model.web;


/** 
 * @author Sarita Panigrahi, created on: 24-Sep-2017
 * This model class is used to send data to dashboard home top line grid
 */
public class BoxChart {
	
	private String name;
	private Long andhraValue;
	private Long telanganaValue;
	private String source;
	private String telanganaSource;
	private String andhraSource;
	private String unit;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAndhraValue() {
		return andhraValue;
	}
	public void setAndhraValue(Long andhraValue) {
		this.andhraValue = andhraValue;
	}
	public Long getTelanganaValue() {
		return telanganaValue;
	}
	public void setTelanganaValue(Long telanganaValue) {
		this.telanganaValue = telanganaValue;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTelanganaSource() {
		return telanganaSource;
	}
	public void setTelanganaSource(String telanganaSource) {
		this.telanganaSource = telanganaSource;
	}
	public String getAndhraSource() {
		return andhraSource;
	}
	public void setAndhraSource(String andhraSource) {
		this.andhraSource = andhraSource;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	

}
