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
 * @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017 
 * 		   this entity
 *         will hold submission details related to sncu/nicu data entry/ historical data
 */
@Entity
@Table(name="txn_submission_management")
public class TXNSubmissionManagement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer txnSubmissionId;
	
	@Column(name="created_date")
	private Timestamp createdDate;
	
	@Column(name="created_by", length=100)
	private String createdBy;
	
	@Column(name="remark_by_superintendent", length=1000)
	private String remarkBySuperintendent;
	
	@Column(name="remark_by_mne", length=1000)
	private String remarkByMnE;
	
	@Column(name="is_latest")
	private Boolean isLatest;
	
	//when user approves/rejects a submission a new row creates. this column hold the actual submission id in that case to get txn data of that submission
	@Column(name = "ref_submission_id")
	private Integer refSubmissionId;
	
	//added on 17-05-2018 (when submitted from web, it is true. in case of mobile- null
	@Column(name="is_web")
	private Boolean isWeb;
	
//	********  bi-directional many-to-one association to TypeDetail *******
	
	@ManyToOne
	@JoinColumn(name="status_superintendent_fk")
	private TypeDetail statusSuperintendent;
	
	@ManyToOne
	@JoinColumn(name="status_mne_fk")
	private TypeDetail statusMne;
	
//	********  bi-directional many-to-one association to Area *******
	
	@ManyToOne
	@JoinColumn(name="facility_id_fk")
	private Area facility;
	
//	********  bi-directional many-to-one association to TimePeriod *******
	
	@ManyToOne
	@JoinColumn(name="timeperiod_id_fk")
	private TimePeriod timePeriod;

	
//	******** bi-directional one-to-many association to TXNSNCUData *******
	
	@OneToMany(mappedBy="txnSubmissionManagement")
	private List<TXNSNCUData> txnsncuDatas;
	
	public TXNSubmissionManagement(Integer txnSubmissionId2) {
		this.txnSubmissionId = txnSubmissionId2;
	}
	
	public TXNSubmissionManagement() {
	}

	
	//GETTER SETTER **********************
	
	public Integer getTxnSubmissionId() {
		return txnSubmissionId;
	}

	public Integer getRefSubmissionId() {
		return refSubmissionId;
	}

	public void setRefSubmissionId(Integer refSubmissionId) {
		this.refSubmissionId = refSubmissionId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getRemarkBySuperintendent() {
		return remarkBySuperintendent;
	}

	public String getRemarkByMnE() {
		return remarkByMnE;
	}

	public Boolean getIsLatest() {
		return isLatest;
	}

	public TypeDetail getStatusSuperintendent() {
		return statusSuperintendent;
	}

	public TypeDetail getStatusMne() {
		return statusMne;
	}

	public Area getFacility() {
		return facility;
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}

	public void setTxnSubmissionId(Integer txnSubmissionId) {
		this.txnSubmissionId = txnSubmissionId;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setRemarkBySuperintendent(String remarkBySuperintendent) {
		this.remarkBySuperintendent = remarkBySuperintendent;
	}

	public void setRemarkByMnE(String remarkByMnE) {
		this.remarkByMnE = remarkByMnE;
	}

	public void setIsLatest(Boolean isLatest) {
		this.isLatest = isLatest;
	}

	public void setStatusSuperintendent(TypeDetail statusSuperintendent) {
		this.statusSuperintendent = statusSuperintendent;
	}

	public void setStatusMne(TypeDetail statusMnE) {
		this.statusMne = statusMnE;
	}

	public void setFacility(Area facility) {
		this.facility = facility;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}

	public List<TXNSNCUData> getTxnsncuDatas() {
		return txnsncuDatas;
	}

	public void setTxnsncuDatas(List<TXNSNCUData> txnsncuDatas) {
		this.txnsncuDatas = txnsncuDatas;
	}

	public Boolean getIsWeb() {
		return isWeb;
	}

	public void setIsWeb(Boolean isWeb) {
		this.isWeb = isWeb;
	}
}
