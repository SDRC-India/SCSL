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
 * @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017 this entity
 *         class holds all the typedetails for a type
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)        
 */

@Entity
@Table(name = "type_detail")
public class TypeDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer typeDetailId;

	@Column(name = "type_detail", length=100)
	private String typeDetail;

	@Column(length=2000)
	private String description;
	
	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;
	
	@Column(name = "updated_date")
	private Timestamp updatedDate;
	
	// ******** bi-directional many-to-one association to Type *******

	@ManyToOne
	@JoinColumn(name = "type_id_fk", nullable = false)
	private Type type;

	// ******** bi-directional one-to-many association to Indicator *******

	@OneToMany(mappedBy = "coreArea")
	private List<Indicator> indicators;

	@OneToMany(mappedBy = "indicatorType")
	private List<Indicator> indicators1;

	// ******** bi-directional one-to-many association to PDSA *******

//	@OneToMany(mappedBy = "frequency")
//	private List<PDSA> pdsas;

//	@OneToMany(mappedBy = "status")
//	private List<PDSA> pdsas2;

	// ******** bi-directional one-to-many association to ArchivePDSA *******
//	@OneToMany(mappedBy = "arcFrequency")
//	private List<ArchivePDSA> archivePDSAs;

//	@OneToMany(mappedBy = "arcStatus")
//	private List<ArchivePDSA> archivePDSAs2;

	// ******** bi-directional one-to-many association to Area *******
	@OneToMany(mappedBy = "facilityType")
	private List<Area> areas;

	@OneToMany(mappedBy = "facilitySize")
	private List<Area> areas2;

	// ******** bi-directional one-to-many association to
	// TXNSubmissionManagement *******
	@OneToMany(mappedBy = "statusSuperintendent")
	private List<TXNSubmissionManagement> txnSubmissionManagements;

	@OneToMany(mappedBy = "statusMne")
	private List<TXNSubmissionManagement> txnSubmissionManagements2;
	
	// ******** bi-directional one-to-many association to TXNSNCUData *******

//	@OneToMany(mappedBy = "description")
//	private List<TXNSNCUData> txnsncuDatas;
	
	// ******** bi-directional one-to-many association to AggregateData *******

//	@OneToMany(mappedBy = "aggreagteType")
//	private List<AggregateData> aggregateDatas;
	
	// ******** bi-directional one-to-many association to AgArchiveAggregateDatagregateData *******

//	@OneToMany(mappedBy = "arcAggreagteType")
//	private List<ArchiveAggregateData> archiveAggregateDatas;
//	
	public TypeDetail(Integer typeDetailId) {
		super();
		this.typeDetailId = typeDetailId;
	}


	public TypeDetail() {
	}
	
	
	// ******* GETTER SETTER *********************
	

	public Integer getTypeDetailId() {
		return typeDetailId;
	}

	public void setTypeDetailId(Integer typeDetailId) {
		this.typeDetailId = typeDetailId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTypeDetail() {
		return typeDetail;
	}

	public void setTypeDetail(String typeDetail) {
		this.typeDetail = typeDetail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Indicator> getIndicators() {
		return indicators;
	}

	public List<Indicator> getIndicators1() {
		return indicators1;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public List<Area> getAreas2() {
		return areas2;
	}

	public List<TXNSubmissionManagement> getTxnSubmissionManagements() {
		return txnSubmissionManagements;
	}

	public List<TXNSubmissionManagement> getTxnSubmissionManagements2() {
		return txnSubmissionManagements2;
	}

	public void setIndicators(List<Indicator> indicators) {
		this.indicators = indicators;
	}

	public void setIndicators1(List<Indicator> indicators1) {
		this.indicators1 = indicators1;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public void setAreas2(List<Area> areas2) {
		this.areas2 = areas2;
	}

	public void setTxnSubmissionManagements(List<TXNSubmissionManagement> txnSubmissionManagements) {
		this.txnSubmissionManagements = txnSubmissionManagements;
	}

	public void setTxnSubmissionManagements2(List<TXNSubmissionManagement> txnSubmissionManagements2) {
		this.txnSubmissionManagements2 = txnSubmissionManagements2;
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

}
