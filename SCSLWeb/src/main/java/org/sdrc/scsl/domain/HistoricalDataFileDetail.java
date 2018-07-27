package org.sdrc.scsl.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

/**
 * @author Sarita This entity class contains all the files uploaded / rejected in historical data module for future references
 * {@link CreatedDate : 22-03-2018} 
 */
@Entity
@Table(name = "historical_data_file_detail")
public class HistoricalDataFileDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer historicalDataFileDetailId;
	
	@Column(name="created_date", nullable = false)
	private Timestamp createdDate;
	
	//files that are uploaded into the system will have this id
	@ManyToOne
	@JoinColumn(name="txn_submission_id_fk", nullable = true)
	private TXNSubmissionManagement tXNSubmissionManagement;
	
	@Column(name="file_path", length=200)
	private String filePath;

	public Integer getHistoricalDataFileDetailId() {
		return historicalDataFileDetailId;
	}

	public void setHistoricalDataFileDetailId(Integer historicalDataFileDetailId) {
		this.historicalDataFileDetailId = historicalDataFileDetailId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public TXNSubmissionManagement gettXNSubmissionManagement() {
		return tXNSubmissionManagement;
	}

	public void settXNSubmissionManagement(TXNSubmissionManagement tXNSubmissionManagement) {
		this.tXNSubmissionManagement = tXNSubmissionManagement;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
}
