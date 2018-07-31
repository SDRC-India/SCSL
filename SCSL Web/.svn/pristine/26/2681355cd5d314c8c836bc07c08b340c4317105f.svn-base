package org.sdrc.scsl.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017 
*	entity for wave wise area, facility detail
* @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
*/
@Entity
@Table(name = "area")
public class Area implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Integer areaId;
	
	@Column(name="area_name")
	private String areaName;
	
	@Column(name="parent_area_id")
	private int parentAreaId;
	
	private int level;
	
	private Integer wave;
	
	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;
	
	@Column(name = "updated_date")
	private Timestamp updatedDate;
	
	//post UAT
	@Column(name="has_LR")
	private Boolean hasLr;
	
	@Column(name="gps")
	private String gps;
	
//	********  bi-directional many-to-one association to TypeDetail *******
	
	//public/ private
	@ManyToOne
	@JoinColumn(name="facility_type_fk")
	private TypeDetail facilityType;
	
	//small, large public/ private
	@ManyToOne
	@JoinColumn(name="facility_size_fk")
	private TypeDetail facilitySize;
	
//	********bi-directional one-to-many association to PDSA *******
	
	@OneToMany(mappedBy="facility")
	private List<PDSA> pdsas;
	
//	********bi-directional one-to-many association to ArchivePDSA *******
	
//	@OneToMany(mappedBy="arcFacility")
//	private List<ArchivePDSA> archivePDSAs;
//	
//	********bi-directional one-to-many association to IndicatorFacilityTimeperiodMapping *******
	
	@OneToMany(mappedBy="facility")
	private List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings;
	
//	********bi-directional one-to-many association to TXNEngagementScore *******
	
	@OneToMany(mappedBy="facility")
	private List<TXNEngagementScore> txnEngagementScores2;

	
//	********bi-directional one-to-many association to TXNSubmissionManagement *******
	
	@OneToMany(mappedBy="facility")
	private List<TXNSubmissionManagement> txnSubmissionManagements;
	
	
//	********bi-directional one-to-many association to AggregateData *******
	
//	@OneToMany(mappedBy="area")
//	private List<AggregateData> aggregateDatas;
	
//	********bi-directional one-to-many association to ArchiveAggregateData *******
	
//	@OneToMany(mappedBy="arcArea")
//	private List<ArchiveAggregateData> archiveAggregateDatas;
	
	
//	********bi-directional one-to-many association to UserAreaMapping *******
	
	@OneToMany(mappedBy="facility")
	private List<UserAreaMapping> userAreaMappings;
	
// ******* bi-directional one-to-many association to TXNPlanning*********
	@OneToMany(mappedBy="facility")
	private List<TXNPlanning> txnPlanning;
	
	//GETTER SETTER **********************
	
	public Area(Integer facilityId) {
		this.areaId=facilityId;
	}


	public String getGps() {
		return gps;
	}


	public void setGps(String gps) {
		this.gps = gps;
	}


	public Boolean getHasLr() {
		return hasLr;
	}


	public void setHasLr(Boolean hasLr) {
		this.hasLr = hasLr;
	}


	public Area(){
		
	}

	public Integer getAreaId() {
		return areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public int getParentAreaId() {
		return parentAreaId;
	}

	public int getLevel() {
		return level;
	}

	public Integer getWave() {
		return wave;
	}

	public TypeDetail getFacilityType() {
		return facilityType;
	}

	public TypeDetail getFacilitySize() {
		return facilitySize;
	}

	public List<PDSA> getPdsas() {
		return pdsas;
	}

//	public List<ArchivePDSA> getArchivePDSAs() {
//		return archivePDSAs;
//	}

	public List<IndicatorFacilityTimeperiodMapping> getIndicatorFacilityTimeperiodMappings() {
		return indicatorFacilityTimeperiodMappings;
	}

	public List<TXNEngagementScore> getTxnEngagementScores2() {
		return txnEngagementScores2;
	}

	public List<TXNSubmissionManagement> getTxnSubmissionManagements() {
		return txnSubmissionManagements;
	}

//	public List<AggregateData> getAggregateDatas() {
//		return aggregateDatas;
//	}
//
//	public List<ArchiveAggregateData> getArchiveAggregateDatas() {
//		return archiveAggregateDatas;
//	}

	public List<UserAreaMapping> getUserAreaMappings() {
		return userAreaMappings;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public void setParentAreaId(int parentAreaId) {
		this.parentAreaId = parentAreaId;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setWave(Integer wave) {
		this.wave = wave;
	}

	public void setFacilityType(TypeDetail facilityType) {
		this.facilityType = facilityType;
	}

	public void setFacilitySize(TypeDetail facilitySize) {
		this.facilitySize = facilitySize;
	}

	public void setPdsas(List<PDSA> pdsas) {
		this.pdsas = pdsas;
	}

//	public void setArchivePDSAs(List<ArchivePDSA> archivePDSAs) {
//		this.archivePDSAs = archivePDSAs;
//	}

	public void setIndicatorFacilityTimeperiodMappings(
			List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings) {
		this.indicatorFacilityTimeperiodMappings = indicatorFacilityTimeperiodMappings;
	}

	public void setTxnEngagementScores2(List<TXNEngagementScore> txnEngagementScores2) {
		this.txnEngagementScores2 = txnEngagementScores2;
	}

	public void setTxnSubmissionManagements(List<TXNSubmissionManagement> txnSubmissionManagements) {
		this.txnSubmissionManagements = txnSubmissionManagements;
	}

//	public void setAggregateDatas(List<AggregateData> aggregateDatas) {
//		this.aggregateDatas = aggregateDatas;
//	}
//
//	public void setArchiveAggregateDatas(List<ArchiveAggregateData> archiveAggregateDatas) {
//		this.archiveAggregateDatas = archiveAggregateDatas;
//	}

	public void setUserAreaMappings(List<UserAreaMapping> userAreaMappings) {
		this.userAreaMappings = userAreaMappings;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}


	public List<TXNPlanning> getTxnPlanning() {
		return txnPlanning;
	}


	public void setTxnPlanning(List<TXNPlanning> txnPlanning) {
		this.txnPlanning = txnPlanning;
	}
}
