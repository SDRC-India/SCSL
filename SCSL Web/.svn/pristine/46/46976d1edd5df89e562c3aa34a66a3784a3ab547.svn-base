package org.sdrc.scsl.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017 
*	this entity class will hold indicator-facility-timeperiod wise transactions of SNCU/NICU data
*/
@Entity
@Table(name="txn_sncu_data")
public class TXNSNCUData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer txnIndicatorId;
	
	@Column(name = "numerator_value")
	private Integer numeratorValue;
	
	@Column(name = "denominator_value")
	private Integer denominatorValue;
	
	private Double percentage;

	@Column(name="created_date")
	private Timestamp createdDate;
	

	@Column(name="is_live")
	private Boolean isLive;
	
	
//	********  bi-directional many-to-one association to TypeDetail *******
	
	@ManyToOne
	@JoinColumn(name="description_fk")
	private TypeDetail description;
	
	
//	********  bi-directional many-to-one association to IndicatorFacilityTimeperiodMapping *******
	
	@ManyToOne
	@JoinColumn(name="indicator_facility_mapping_id_fk")
	private IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping;
	
//	********  bi-directional many-to-one association to TXNSubmissionManagement *******
	@ManyToOne
	@JoinColumn(name="submission_id_fk")
	private TXNSubmissionManagement txnSubmissionManagement;

	//GETTER SETTER **********************
	
	public Integer getTxnIndicatorId() {
		return txnIndicatorId;
	}

	public Integer getNumeratorValue() {
		return numeratorValue;
	}

	public Integer getDenominatorValue() {
		return denominatorValue;
	}

	public Double getPercentage() {
		return percentage;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public IndicatorFacilityTimeperiodMapping getIndicatorFacilityTimeperiodMapping() {
		return indicatorFacilityTimeperiodMapping;
	}

	public void setTxnIndicatorId(Integer txnIndicatorId) {
		this.txnIndicatorId = txnIndicatorId;
	}

	public void setNumeratorValue(Integer numeratorValue) {
		this.numeratorValue = numeratorValue;
	}

	public void setDenominatorValue(Integer denominatorValue) {
		this.denominatorValue = denominatorValue;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public void setIndicatorFacilityTimeperiodMapping(
			IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping) {
		this.indicatorFacilityTimeperiodMapping = indicatorFacilityTimeperiodMapping;
	}

	public TXNSubmissionManagement getTxnSubmissionManagement() {
		return txnSubmissionManagement;
	}

	public void setTxnSubmissionManagement(TXNSubmissionManagement txnSubmissionManagement) {
		this.txnSubmissionManagement = txnSubmissionManagement;
	}

	public TypeDetail getDescription() {
		return description;
	}

	public void setDescription(TypeDetail description) {
		this.description = description;
	}

}
