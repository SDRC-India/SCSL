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
 * 		    this entity
 *          will hold all the aggregatedData data of SNCU/NICU data entry 
 */
@Entity
@Table(name="aggregate_data")
public class AggregateData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer dataId;
	
	@Column(name="created_date")
	private Timestamp createdDate;
	
	@Column(name = "numerator_value")
	private Long numeratorValue;
	
	@Column(name = "denominator_value")
	private Long denominatorValue;
	
	private Double percentage;
	
//	********  bi-directional many-to-one association to TypeDetail for aggregation type*******
	
	//contains facility type/size or Total
	@ManyToOne
	@JoinColumn(name="aggregate_type_fk")
	private TypeDetail aggreagteType;
	
	@Column
	private Integer wave;
	
	
//	********  bi-directional many-to-one association to Area *******
	
	@ManyToOne
	@JoinColumn(name="area_id_fk")
	private Area area;
	
//	********  bi-directional many-to-one association to Indicator *******
	
	@ManyToOne
	@JoinColumn(name="indicator_id_fk")
	private Indicator indicator;
	
//	********  bi-directional many-to-one association to TimePeriod *******
	
	@ManyToOne
	@JoinColumn(name="timeperiod_id_fk")
	private TimePeriod timePeriod;

	//GETTER SETTER **********************
	
	public Integer getDataId() {
		return dataId;
	}

	public TypeDetail getAggreagteType() {
		return aggreagteType;
	}

	public void setAggreagteType(TypeDetail aggreagteType) {
		this.aggreagteType = aggreagteType;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public Long getNumeratorValue() {
		return numeratorValue;
	}

	public Long getDenominatorValue() {
		return denominatorValue;
	}

	public Double getPercentage() {
		return percentage;
	}

	public Area getArea() {
		return area;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setNumeratorValue(Long numeratorValue) {
		this.numeratorValue = numeratorValue;
	}

	public void setDenominatorValue(Long denominatorValue) {
		this.denominatorValue = denominatorValue;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public Integer getWave() {
		return wave;
	}

	public void setWave(Integer wave) {
		this.wave = wave;
	}

}
