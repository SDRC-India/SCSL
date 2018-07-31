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
 * @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017 this entity class will hold the
 *         transactions for every PDSA entry
 *         UNIQUE KEY COMBINATION (due_date, pdsa_id_fk)
 *         ALTER TABLE txn_pdsa ADD UNIQUE (due_date, pdsa_id_fk)
 */
@Entity
@Table(name = "txn_pdsa")
public class TXNPDSA implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer txnPDSAId;

	@Column(name = "numerator_value")
	private Integer numeratorValue;

	@Column(name = "denominator_value")
	private Integer denominatorValue;

	private Double percentage;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "created_by", length=100)
	private String createdBy;

	@Column(name = "due_date")
	private Timestamp dueDate;

	// ******** bi-directional many-to-one association to PDSA *******

	@ManyToOne
	@JoinColumn(name = "pdsa_id_fk")
	private PDSA pdsa;

	/*
	 * // ******** bi-directional many-to-one association to ArchivePDSA *******
	 * //ASK HAREKRISHNA
	 * 
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="arc_pdsa_id_fk") private ArchivePDSA archivePDSA;
	 */
	//GETTER SETTER **********************
	
	public Integer getTxnPDSAId() {
		return txnPDSAId;
	}

	public Double getPercentage() {
		return percentage;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Timestamp getDueDate() {
		return dueDate;
	}

	public PDSA getPdsa() {
		return pdsa;
	}

	public void setTxnPDSAId(Integer txnPDSAId) {
		this.txnPDSAId = txnPDSAId;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	public void setPdsa(PDSA pdsa) {
		this.pdsa = pdsa;
	}

	public Integer getNumeratorValue() {
		return numeratorValue;
	}

	public Integer getDenominatorValue() {
		return denominatorValue;
	}

	public void setNumeratorValue(Integer numeratorValue) {
		this.numeratorValue = numeratorValue;
	}

	public void setDenominatorValue(Integer denominatorValue) {
		this.denominatorValue = denominatorValue;
	}

}
