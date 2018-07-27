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
* @author Sarita Panigrahi(sarita@sdrc.co.in)
*   Created on 22-04-2017 
*	this entity will hold all the PDSA data entry informations
*/
@Entity
@Table(name="pdsa")
public class PDSA implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer pdsaId;
	
	@Column(name="pdsa_number", nullable=false , unique=true)
	private String pdsaNumber;

	@Column(length=1000)
	private String name;
	
	@Column(length=1000)
	private String summary;
	
	@Column(name = "strart_date")
	private Timestamp startDate;
	
	@Column(name = "end_date")
	private Timestamp endDate;
	
	@Column(name = "first_doc_filepath", length=150)
	private String firstDocFilepath;
	
	@Column(name = "last_doc_filepath", length=150)
	private String lastDocFilepath;
	
	@Column(name = "other_doc_filepath", length=150)
	private String otherDocFilepath;
	
	@Column(name="clossing_remarks", length=400)
	private String clossingRemarks;
	
	@Column(name="created_date")
	private Timestamp createdDate;
	
	@Column(name="created_by", length=100)
	private String createdBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;
	
	@Column(name="updated_by", length=100)
	private String updatedBy;
	
	@Column(name="pdsa_frequency",nullable=false)
	private int pdsaFrequency;
	
//	********  bi-directional many-to-one association to Indicator *******
	
	@ManyToOne
	@JoinColumn(name="indicator_id_fk")
	private Indicator indicator;
	
//	********  bi-directional many-to-one association to ChangeIdea *******
	
	@ManyToOne
	@JoinColumn(name="changeIdea_fk")
	private ChangeIdea changeIdea;

//	********  bi-directional many-to-one association to TypeDetail *******
	
//	@ManyToOne
	@Column(name="frequency")
	private int frequency;
	
	@ManyToOne
	@JoinColumn(name="status_fk")
	private TypeDetail status;
	
//	********  bi-directional many-to-one association to Area *******
	
	@ManyToOne
	@JoinColumn(name="facility_id_fk")
	private Area facility;
	
//	********  bi-directional one-to-many association to TXNPDSA *******
	
	@OneToMany(mappedBy="pdsa")
	private List<TXNPDSA> txnpdsas;
	
//	**************************getter setter*********************************
	
	public Integer getPdsaId() {
		return pdsaId;
	}

	public Area getFacility() {
		return facility;
	}

	public void setFacility(Area facility) {
		this.facility = facility;
	}

	public String getPdsaNumber() {
		return pdsaNumber;
	}

	public String getName() {
		return name;
	}

	public String getSummary() {
		return summary;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public String getFirstDocFilepath() {
		return firstDocFilepath;
	}

	public String getLastDocFilepath() {
		return lastDocFilepath;
	}

	public String getOtherDocFilepath() {
		return otherDocFilepath;
	}

	public String getClossingRemarks() {
		return clossingRemarks;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public ChangeIdea getChangeIdea() {
		return changeIdea;
	}

	public TypeDetail getStatus() {
		return status;
	}

	public void setPdsaId(Integer pdsaId) {
		this.pdsaId = pdsaId;
	}

	public void setPdsaNumber(String pdsaNumber) {
		this.pdsaNumber = pdsaNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public void setFirstDocFilepath(String firstDocFilepath) {
		this.firstDocFilepath = firstDocFilepath;
	}

	public void setLastDocFilepath(String lastDocFilepath) {
		this.lastDocFilepath = lastDocFilepath;
	}

	public void setOtherDocFilepath(String otherDocFilepath) {
		this.otherDocFilepath = otherDocFilepath;
	}

	public void setClossingRemarks(String clossingRemarks) {
		this.clossingRemarks = clossingRemarks;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public void setChangeIdea(ChangeIdea changeIdea) {
		this.changeIdea = changeIdea;
	}
	
	public void setStatus(TypeDetail status) {
		this.status = status;
	}

	public List<TXNPDSA> getTxnpdsas() {
		return txnpdsas;
	}

	public void setTxnpdsas(List<TXNPDSA> txnpdsas) {
		this.txnpdsas = txnpdsas;
	}

	public int getPdsaFrequency() {
		return pdsaFrequency;
	}

	public void setPdsaFrequency(int pdsaFrequency) {
		this.pdsaFrequency = pdsaFrequency;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	

}
