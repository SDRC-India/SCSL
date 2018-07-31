package org.sdrc.scsl.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
* 	Created on 22-04-2017
*	this entity will hold all the master data related to engagement score
* @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
*/
@Entity
@Table(name="mst_engagement_score")
public class MSTEngagementScore implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer mstEngagementScoreId;
	
	private String progress;
	
	@Column(length=1000)
	private String definition;
	
	private float score;
	
	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;
	
	@Column(name = "updated_date")
	private Timestamp updatedDate;
	
//	******** bi-directional one-to-many association to TXNEngagementScore *******
	
	@OneToMany(mappedBy="engagementScore")
	private List<TXNEngagementScore> txnEngagementScores;

	//GETTER SETTER **********************
	
	
	public Integer getMstEngagementScoreId() {
		return mstEngagementScoreId;
	}

	public String getProgress() {
		return progress;
	}

	public String getDefinition() {
		return definition;
	}

	public float getScore() {
		return score;
	}

	public List<TXNEngagementScore> getTxnEngagementScores() {
		return txnEngagementScores;
	}

	public void setMstEngagementScoreId(Integer mstEngagementScoreId) {
		this.mstEngagementScoreId = mstEngagementScoreId;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public void setTxnEngagementScores(List<TXNEngagementScore> txnEngagementScores) {
		this.txnEngagementScores = txnEngagementScores;
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
