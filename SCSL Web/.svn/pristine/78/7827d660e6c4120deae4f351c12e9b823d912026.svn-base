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
 * @author Sarita Panigrahi(sarita@sdrc.co.in) this entity class will hold
 *         INDICATOR wise change idea in PDSA DATA ENTRY
 */
@Entity
@Table(name = "change_idea")
public class ChangeIdea implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer changeIdeaId;

	@Column(length = 1000)
	private String description;

	@Column(name = "created_by", length=100)
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	// ******** bi-directional many-to-one association to Indicator *******

	@ManyToOne
	@JoinColumn(name = "indicator_id_fk")
	private Indicator indicator;

	// ******** bi-directional one-to-many association to PDSA *******

	@OneToMany(mappedBy = "changeIdea")
	private List<PDSA> pdsas;

	// ******** bi-directional one-to-many association to ArchivePDSA *******

//	@OneToMany(mappedBy = "arcChangeIdea")
//	private List<ArchivePDSA> archivePDSAs;

	// Parameterized constructor is defined
	public ChangeIdea(Integer changeIdeaId2) {
		super();
		this.changeIdeaId = changeIdeaId2;
	}

	// default Constructor
	public ChangeIdea() {

	}

	// GETTER SETTER **********************

	public Integer getChangeIdeaId() {
		return changeIdeaId;
	}

	public void setChangeIdeaId(Integer changeIdeaId) {
		this.changeIdeaId = changeIdeaId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public List<PDSA> getPdsas() {
		return pdsas;
	}

//	public List<ArchivePDSA> getArchivePDSAs() {
//		return archivePDSAs;
//	}

	public void setPdsas(List<PDSA> pdsas) {
		this.pdsas = pdsas;
	}

//	public void setArchivePDSAs(List<ArchivePDSA> archivePDSAs) {
//		this.archivePDSAs = archivePDSAs;
//	}

}
