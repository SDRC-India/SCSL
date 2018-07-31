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
 * @author Sarita Panigrahi(sarita@sdrc.co.in) 
 * 			Created on 22-04-2017 
 * 		    this entity is the ARCHIVE of AggregateData 
 */
@Entity
@Table(name="archive_aggregate_data")
public class ArchiveAggregateData  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer arcDataId;
	
	@Column(name="created_date")
	private Timestamp arcCreatedDate;
	
	@Column(name = "numerator_value")
	private Long arcNumeratorValue;
	
	@Column(name = "denominator_value")
	private Long arcDenominatorValue;
	
	private Double arcPercentage;
	
//	********  bi-directional many-to-one association to Area *******
	
	@ManyToOne
	@JoinColumn(name="area_id_fk")
	private Area arcArea;
	
//	********  bi-directional many-to-one association to Indicator *******
	
	@ManyToOne
	@JoinColumn(name="indicator_id_fk")
	private Indicator arcIndicator;
	
//	********  bi-directional many-to-one association to TimePeriod *******
	
	@ManyToOne
	@JoinColumn(name="timeperiod_id_fk")
	private TimePeriod arcTimePeriod;
	
//	********  bi-directional many-to-one association to TypeDetail *******
	
	@ManyToOne
	@JoinColumn(name="aggregate_type_fk")
	private TypeDetail arcAggreagteType;
	
	
	//GETTER SETTER **********************
	
	public Integer getArcDataId() {
		return arcDataId;
	}

	public Timestamp getArcCreatedDate() {
		return arcCreatedDate;
	}

	public Long getArcNumeratorValue() {
		return arcNumeratorValue;
	}

	public Long getArcDenominatorValue() {
		return arcDenominatorValue;
	}

	public Double getArcPercentage() {
		return arcPercentage;
	}

	public Area getArcArea() {
		return arcArea;
	}

	public Indicator getArcIndicator() {
		return arcIndicator;
	}

	public TimePeriod getArcTimePeriod() {
		return arcTimePeriod;
	}

	public void setArcDataId(Integer arcDataId) {
		this.arcDataId = arcDataId;
	}

	public void setArcCreatedDate(Timestamp arcCreatedDate) {
		this.arcCreatedDate = arcCreatedDate;
	}

	public void setArcNumeratorValue(Long arcNumeratorValue) {
		this.arcNumeratorValue = arcNumeratorValue;
	}

	public void setArcDenominatorValue(Long arcDenominatorValue) {
		this.arcDenominatorValue = arcDenominatorValue;
	}

	public void setArcPercentage(Double arcPercentage) {
		this.arcPercentage = arcPercentage;
	}

	public void setArcArea(Area arcArea) {
		this.arcArea = arcArea;
	}

	public void setArcIndicator(Indicator arcIndicator) {
		this.arcIndicator = arcIndicator;
	}

	public void setArcTimePeriod(TimePeriod arcTimePeriod) {
		this.arcTimePeriod = arcTimePeriod;
	}

}
