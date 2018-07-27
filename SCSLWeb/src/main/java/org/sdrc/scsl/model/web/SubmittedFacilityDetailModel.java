package org.sdrc.scsl.model.web;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
*	this model class will be used to view each submission 
*/
public class SubmittedFacilityDetailModel {

	private String indicatorType;
	private String indicatorName;
	private String t1PercentValue;
	private String t2PercentValue;
	private Integer numeratorValue;
	private Integer denominatorValue;
	private String percentValue;
	private String description;
	private String cssClass;
	private Boolean isProfile;
	
	public Boolean getIsProfile() {
		return isProfile;
	}
	public void setIsProfile(Boolean isProfile) {
		this.isProfile = isProfile;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIndicatorType() {
		return indicatorType;
	}
	public String getIndicatorName() {
		return indicatorName;
	}
	public String getT1PercentValue() {
		return t1PercentValue;
	}
	public String getT2PercentValue() {
		return t2PercentValue;
	}
	public Integer getNumeratorValue() {
		return numeratorValue;
	}
	public Integer getDenominatorValue() {
		return denominatorValue;
	}
	public String getPercentValue() {
		return percentValue;
	}
	public void setIndicatorType(String indicatorType) {
		this.indicatorType = indicatorType;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	public void setT1PercentValue(String t1PercentValue) {
		this.t1PercentValue = t1PercentValue;
	}
	public void setT2PercentValue(String t2PercentValue) {
		this.t2PercentValue = t2PercentValue;
	}
	public void setNumeratorValue(Integer numeratorValue) {
		this.numeratorValue = numeratorValue;
	}
	public void setDenominatorValue(Integer denominatorValue) {
		this.denominatorValue = denominatorValue;
	}
	public void setPercentValue(String percentValue) {
		this.percentValue = percentValue;
	}
	
	
}
