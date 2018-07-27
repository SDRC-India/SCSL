package org.sdrc.scsl.model.web;

import java.sql.Timestamp;

public class TXNSNCUDataModel {
	
	private Integer txnIndicatorId;
	
	private Integer numeratorValue;
	
	private Integer denominatorValue;
	
	private Double percentage;
	
	private Timestamp createdDate;
	
	private Boolean isLive;
	
	private Integer indicatorId;

	private Integer txnSubmissionId;

	public Integer getTxnIndicatorId() {
		return txnIndicatorId;
	}

	public void setTxnIndicatorId(Integer txnIndicatorId) {
		this.txnIndicatorId = txnIndicatorId;
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

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public Integer getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(Integer indicatorId) {
		this.indicatorId = indicatorId;
	}

	public Integer getTxnSubmissionId() {
		return txnSubmissionId;
	}

	public void setTxnSubmissionId(Integer txnSubmissionId) {
		this.txnSubmissionId = txnSubmissionId;
	}

	public TXNSNCUDataModel(Integer numeratorValue, Integer denominatorValue, Double percentage, Integer indicatorId) {
		super();
		this.numeratorValue = numeratorValue;
		this.denominatorValue = denominatorValue;
		this.percentage = percentage;
		this.indicatorId = indicatorId;
	}

	public TXNSNCUDataModel() {
		super();
	}
	
}
