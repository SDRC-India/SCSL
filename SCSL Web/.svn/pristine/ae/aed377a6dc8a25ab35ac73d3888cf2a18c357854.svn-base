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
 * @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017 this entity
 *         will hold all the transaction data related to engagement score for a
 *         timeperiod and facility
 *         UNIQUE KEY COMBINATION (Area-TimePeriod)
 *         ALTER TABLE txn_engagement_score ADD UNIQUE (facility_id_fk, timeperiod_id_fk)
 */
@Entity
@Table(name="txn_engagement_score")
public class TXNEngagementScore implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer txnEngagementScoreId;
	
	@Column(name="created_by", length=100)
	private String createdBy;
	
	@Column(name="created_date")
	private Timestamp createdDate;
	
	//added on 17-05-2018 (when submitted from web, it is true. in case of mobile- null
	@Column(name="is_web")
	private Boolean isWeb;
	
//	********  bi-directional many-to-one association to MSTEngagementScore *******
	
	@ManyToOne
	@JoinColumn(name="enagagement_score_id_fk")
	private MSTEngagementScore engagementScore;

//	********  bi-directional many-to-one association to Area *******
	
	@ManyToOne
	@JoinColumn(name="facility_id_fk")
	private Area facility;
	
	
//	********  bi-directional many-to-one association to TimePeriod *******
	
	@ManyToOne
	@JoinColumn(name="timeperiod_id_fk")
	private TimePeriod timePeriod;

//	********  bi-directional many-to-one association to User *******
	//user fk added in phase-2 as engagement score AHI associates changes frequently. By keeping this fk we can back track submission detail
	@ManyToOne
	@JoinColumn(name="user_fk")
	private MSTUser user;
	
	//GETTER SETTER **********************
	
	public Integer getTxnEngagementScoreId() {
		return txnEngagementScoreId;
	}


	public Timestamp getCreatedDate() {
		return createdDate;
	}


	public MSTEngagementScore getEngagementScore() {
		return engagementScore;
	}


	public Area getFacility() {
		return facility;
	}


	public TimePeriod getTimePeriod() {
		return timePeriod;
	}


	public void setTxnEngagementScoreId(Integer txnEngagementScoreId) {
		this.txnEngagementScoreId = txnEngagementScoreId;
	}


	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}


	public void setEngagementScore(MSTEngagementScore engagementScore) {
		this.engagementScore = engagementScore;
	}


	public void setFacility(Area facility) {
		this.facility = facility;
	}


	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public MSTUser getUser() {
		return user;
	}


	public void setUser(MSTUser user) {
		this.user = user;
	}


	public Boolean getIsWeb() {
		return isWeb;
	}


	public void setIsWeb(Boolean isWeb) {
		this.isWeb = isWeb;
	}

}
