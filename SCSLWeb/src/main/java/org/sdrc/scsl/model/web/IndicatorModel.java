package org.sdrc.scsl.model.web;

import java.util.List;

public class IndicatorModel {
	
	private Integer coreAreaId;
	private String coreAreaName;
	private Integer indicatorId;
	private String indicatorName;
	private String numeratorName;
	private Integer numeratorValue;
	private String denominatorName;
	private Integer denominatorValue;
	private Boolean isRequired;
	private List<String> exceptionRule;
	private Double percentage;
	private Boolean isEnable;
	private boolean isLive;
	private String createdDate;
	private Integer timePeriod;
	private Integer IndicatorFacilityTimeperiodId;
	private Integer submissionId;
	private Boolean isProfile;
	private Boolean isLr;
	private Integer indicatorOrder;
	private String cssClass;
	private String denominatorCss;
	private String numeratorCss;
	
	public String getDenominatorCss() {
		return denominatorCss;
	}
	public void setDenominatorCss(String denominatorCss) {
		this.denominatorCss = denominatorCss;
	}
	public String getNumeratorCss() {
		return numeratorCss;
	}
	public void setNumeratorCss(String numeratorCss) {
		this.numeratorCss = numeratorCss;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public Integer getIndicatorOrder() {
		return indicatorOrder;
	}
	public void setIndicatorOrder(Integer indicatorOrder) {
		this.indicatorOrder = indicatorOrder;
	}
	public Boolean getIsProfile() {
		return isProfile;
	}
	public void setIsProfile(Boolean isProfile) {
		this.isProfile = isProfile;
	}
	public Boolean getIsLr() {
		return isLr;
	}
	public void setIsLr(Boolean isLr) {
		this.isLr = isLr;
	}
	public Integer getCoreAreaId() {
		return coreAreaId;
	}
	public void setCoreAreaId(Integer coreAreaId) {
		this.coreAreaId = coreAreaId;
	}
	public String getCoreAreaName() {
		return coreAreaName;
	}
	public void setCoreAreaName(String coreAreaName) {
		this.coreAreaName = coreAreaName;
	}
	public Integer getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(Integer indicatorId) {
		this.indicatorId = indicatorId;
	}
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	public String getNumeratorName() {
		return numeratorName;
	}
	public void setNumeratorName(String numeratorName) {
		this.numeratorName = numeratorName;
	}
	public String getDenominatorName() {
		return denominatorName;
	}
	public void setDenominatorName(String denominatorName) {
		this.denominatorName = denominatorName;
	}
	public Boolean getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}
	public List<String> getExceptionRule() {
		return exceptionRule;
	}
	public void setExceptionRule(List<String> exceptionRule) {
		this.exceptionRule = exceptionRule;
	}
	public Integer getNumeratorValue() {
		return numeratorValue;
	}
	public void setNumeratorValue(Integer numeratorValue) {
		this.numeratorValue = numeratorValue;
	}
	public Integer getDenominatorValue() {
		return denominatorValue;
	}
	public void setDenominatorValue(Integer denominatorValue) {
		this.denominatorValue = denominatorValue;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	public Boolean getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(Integer timePeriod) {
		this.timePeriod = timePeriod;
	}
	public Integer getIndicatorFacilityTimeperiodId() {
		return IndicatorFacilityTimeperiodId;
	}
	public void setIndicatorFacilityTimeperiodId(
			Integer indicatorFacilityTimeperiodId) {
		IndicatorFacilityTimeperiodId = indicatorFacilityTimeperiodId;
	}
	public Integer getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
		
}
