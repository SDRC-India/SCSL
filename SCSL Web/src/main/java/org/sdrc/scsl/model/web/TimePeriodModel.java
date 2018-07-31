package org.sdrc.scsl.model.web;

/**
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class TimePeriodModel {

	private Integer timePeriodId;

	private String timePeriod;

	private String startDate;

	private String endDate;

	private String periodicity;

	private int wave;


	// for engagementScore page if user has filled the data for selected
	// timeperiod then cssClass will be disabled otherwise enabled-Used in
	// enabling or disabling the submit button
	private String cssClass;

	// it will contain the engagementScoreId if user has filled the data for selected
	// timeperiod otherwise zero
	private int mstEngagementScoreId;

	public Integer getTimePeriodId() {
		return timePeriodId;
	}

	public void setTimePeriodId(Integer timePeriodId) {
		this.timePeriodId = timePeriodId;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
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

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public int getMstEngagementScoreId() {
		return mstEngagementScoreId;
	}

	public void setMstEngagementScoreId(int mstEngagementScoreId) {
		this.mstEngagementScoreId = mstEngagementScoreId;
	}

}
