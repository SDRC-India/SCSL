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
* @author Sarita Panigrahi(sarita@sdrc.co.in)  Created on 22-04-2017 
*	this is an ARCHIVE OF PDSA entity
*	it will hold all the archived data of PDSA 
*/
@Entity
@Table(name="archive_pdsa")
public class ArchivePDSA implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer arcPdsaId;
	
	@Column(name="pdsa_number", nullable=false)
	private String arcPdsaNumber;

	@Column(length=1000)
	private String arcName;
	
	@Column(length=1000)
	private String arcSummary;
	
	@Column(name = "strart_date")
	private Timestamp arcStartDate;
	
	@Column(name = "end_date")
	private Timestamp arcEndDate;
	
	@Column(name = "first_doc_filepath", length=150)
	private String arcFirstDocFilepath;
	
	@Column(name = "last_doc_filepath", length=150)
	private String arcLastDocFilepath;
	
	@Column(name = "other_doc_filepath", length=150)
	private String arcOtherDocFilepath;
	
	@Column(name="clossing_remarks", length=400)
	private String arcClossingRemarks;
	
	@Column(name="created_date")
	private Timestamp arcCreatedDate;
	
	@Column(name="created_by", length=100)
	private String arcCreatedBy;

	@Column(name="updated_date")
	private Timestamp arcUpdatedDate;
	
	@Column(name="updated_by", length=100)
	private String arcUpdatedBy;
	
//	********  bi-directional many-to-one association to Indicator *******
	
	@ManyToOne
	@JoinColumn(name="indicator_id_fk")
	private Indicator arcIndicator;
	
//	********  bi-directional many-to-one association to ChangeIdea *******
	
	@ManyToOne
	@JoinColumn(name="changeIdea_fk")
	private ChangeIdea arcChangeIdea;

	
//	@ManyToOne
	@Column(name="arc_frequency")
	private int arcFrequency;
	
	@ManyToOne
	@JoinColumn(name="status_fk")
	private TypeDetail arcStatus;

//	********  bi-directional many-to-one association to Area *******
	
	@ManyToOne
	@JoinColumn(name="facility_id_fk")
	private Area arcFacility;

/*//	********  bi-directional one-to-many association to TXNPDSA *******
	
	@OneToMany(mappedBy="pdsa")
	private List<TXNPDSA> arcTxnpdsas;*/
	
//	**************************getter setter*********************************
	
	//GETTER SETTER **********************
	
	public Integer getArcPdsaId() {
		return arcPdsaId;
	}

	public String getArcPdsaNumber() {
		return arcPdsaNumber;
	}

	public String getArcName() {
		return arcName;
	}

	public String getArcSummary() {
		return arcSummary;
	}

	public Timestamp getArcStartDate() {
		return arcStartDate;
	}

	public Timestamp getArcEndDate() {
		return arcEndDate;
	}

	public String getArcFirstDocFilepath() {
		return arcFirstDocFilepath;
	}

	public String getArcLastDocFilepath() {
		return arcLastDocFilepath;
	}

	public String getArcOtherDocFilepath() {
		return arcOtherDocFilepath;
	}

	public String getArcClossingRemarks() {
		return arcClossingRemarks;
	}

	public Timestamp getArcCreatedDate() {
		return arcCreatedDate;
	}

	public String getArcCreatedBy() {
		return arcCreatedBy;
	}

	public Timestamp getArcUpdatedDate() {
		return arcUpdatedDate;
	}

	public String getArcUpdatedBy() {
		return arcUpdatedBy;
	}

	public Indicator getArcIndicator() {
		return arcIndicator;
	}

	public ChangeIdea getArcChangeIdea() {
		return arcChangeIdea;
	}

	public TypeDetail getArcStatus() {
		return arcStatus;
	}

	public Area getArcFacility() {
		return arcFacility;
	}

	public void setArcPdsaId(Integer arcPdsaId) {
		this.arcPdsaId = arcPdsaId;
	}

	public void setArcPdsaNumber(String arcPdsaNumber) {
		this.arcPdsaNumber = arcPdsaNumber;
	}

	public void setArcName(String arcName) {
		this.arcName = arcName;
	}

	public void setArcSummary(String arcSummary) {
		this.arcSummary = arcSummary;
	}

	public void setArcStartDate(Timestamp arcStartDate) {
		this.arcStartDate = arcStartDate;
	}

	public void setArcEndDate(Timestamp arcEndDate) {
		this.arcEndDate = arcEndDate;
	}

	public void setArcFirstDocFilepath(String arcFirstDocFilepath) {
		this.arcFirstDocFilepath = arcFirstDocFilepath;
	}

	public void setArcLastDocFilepath(String arcLastDocFilepath) {
		this.arcLastDocFilepath = arcLastDocFilepath;
	}

	public void setArcOtherDocFilepath(String arcOtherDocFilepath) {
		this.arcOtherDocFilepath = arcOtherDocFilepath;
	}

	public void setArcClossingRemarks(String arcClossingRemarks) {
		this.arcClossingRemarks = arcClossingRemarks;
	}

	public void setArcCreatedDate(Timestamp arcCreatedDate) {
		this.arcCreatedDate = arcCreatedDate;
	}

	public void setArcCreatedBy(String arcCreatedBy) {
		this.arcCreatedBy = arcCreatedBy;
	}

	public void setArcUpdatedDate(Timestamp arcUpdatedDate) {
		this.arcUpdatedDate = arcUpdatedDate;
	}

	public void setArcUpdatedBy(String arcUpdatedBy) {
		this.arcUpdatedBy = arcUpdatedBy;
	}

	public void setArcIndicator(Indicator arcIndicator) {
		this.arcIndicator = arcIndicator;
	}

	public void setArcChangeIdea(ChangeIdea arcChangeIdea) {
		this.arcChangeIdea = arcChangeIdea;
	}

	public int getArcFrequency() {
		return arcFrequency;
	}

	public void setArcFrequency(int arcFrequency) {
		this.arcFrequency = arcFrequency;
	}

	public void setArcStatus(TypeDetail arcStatus) {
		this.arcStatus = arcStatus;
	}

	public void setArcFacility(Area arcFacility) {
		this.arcFacility = arcFacility;
	}
	
}
