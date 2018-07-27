package org.sdrc.scsl.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017  This entity class will hold all
 *         the mappings of indicator--facility--timeperiod
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)   
 */
@Entity
@Table(name = "indicator_facility_timeperiod_mapping")
public class IndicatorFacilityTimeperiodMapping implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer indFacilityTpId;
	
	@Column(name="created_date", nullable = false)
	private Timestamp createdDate;
	
	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name="is_live")
	private Boolean isLive;
	
//	********  bi-directional many-to-one association to Area *******
	
	@ManyToOne
	@JoinColumn(name="facility_id_fk", nullable = false)
	private Area facility;
	
//	********  bi-directional many-to-one association to Indicator *******
	
	@ManyToOne
	@JoinColumn(name="indicator_id_fk", nullable = false)
	private Indicator indicator;
	
//	********  bi-directional many-to-one association to TimePeriod *******
	
	@ManyToOne
	@JoinColumn(name="timeperiod_id_fk", nullable = false)
	private TimePeriod timePeriod;
	
//	******** bi-directional one-to-many association to TXNIndicator *******
	
	public IndicatorFacilityTimeperiodMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IndicatorFacilityTimeperiodMapping(Integer indFacilityTpId) {
	super();
	this.indFacilityTpId = indFacilityTpId;
}

	@OneToMany(mappedBy="indicatorFacilityTimeperiodMapping")
	private List<TXNSNCUData> txnIndicators;

	//GETTER SETTER **********************
	
	public Integer getIndFacilityTpId() {
		return indFacilityTpId;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public Area getFacility() {
		return facility;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public List<TXNSNCUData> getTxnIndicators() {
		return txnIndicators;
	}

	public void setIndFacilityTpId(Integer indFacilityTpId) {
		this.indFacilityTpId = indFacilityTpId;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setFacility(Area facility) {
		this.facility = facility;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public void setTxnIndicators(List<TXNSNCUData> txnIndicators) {
		this.txnIndicators = txnIndicators;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
	
}
