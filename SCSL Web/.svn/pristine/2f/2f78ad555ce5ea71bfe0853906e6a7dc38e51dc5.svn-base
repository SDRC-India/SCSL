package org.sdrc.scsl.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017 this entity class will hold wave
 *         wise timeperiods
 *         
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)        
 *         
 */
@Entity
@Table(name="timePeriod")
public class TimePeriod implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer timePeriodId;
	
	@Column(name = "time_period", length=30)
	private String timePeriod;
	
	@Column(name = "strart_date", nullable = false)
	private Date startDate;
	
	@Column(name = "end_date", nullable = false)
	private Date endDate;
	
	@Column(name = "periodicity", length=10)
	private String periodicity;
	
	@Column(name = "wave", nullable = false)
	private int wave;
	
	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;
	
//	******** bi-directional one-to-many association to IndicatorFacilityTimeperiodMapping *******
	
	@OneToMany(mappedBy="timePeriod")
	private List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings;
	
//	******** bi-directional one-to-many association to TXNEngagementScore *******
	
	@OneToMany(mappedBy="timePeriod")
	private List<TXNEngagementScore> txnEngagementScores;
	
//	******** bi-directional one-to-many association to TXNSubmissionManagement *******
	
	@OneToMany(mappedBy="timePeriod")
	private List<TXNSubmissionManagement> txnSubmissionManagements;
	
//	******** bi-directional one-to-many association to AggregateData *******
	
//	@OneToMany(mappedBy="timePeriod")
//	private List<AggregateData> aggregateDatas;
	
//	******** bi-directional one-to-many association to ArchiveAggregateData *******
	
//	@OneToMany(mappedBy="arcTimePeriod")
//	private List<ArchiveAggregateData> archiveAggregateDatas;
	
	public TimePeriod(Integer timePeriod2) {
		this.timePeriodId=timePeriod2;
	}
	public TimePeriod(){
		
	}

	//GETTER SETTER **********************
	

	public Integer getTimePeriodId() {
		return timePeriodId;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public int getWave() {
		return wave;
	}

	public List<IndicatorFacilityTimeperiodMapping> getIndicatorFacilityTimeperiodMappings() {
		return indicatorFacilityTimeperiodMappings;
	}

	public List<TXNEngagementScore> getTxnEngagementScores() {
		return txnEngagementScores;
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

	public void setTimePeriodId(Integer timePeriodId) {
		this.timePeriodId = timePeriodId;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate2) {
		this.endDate = endDate2;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

	public void setIndicatorFacilityTimeperiodMappings(
			List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings) {
		this.indicatorFacilityTimeperiodMappings = indicatorFacilityTimeperiodMappings;
	}

	public void setTxnEngagementScores(List<TXNEngagementScore> txnEngagementScores) {
		this.txnEngagementScores = txnEngagementScores;
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
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((periodicity == null) ? 0 : periodicity.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((timePeriod == null) ? 0 : timePeriod.hashCode());
		result = prime * result + ((timePeriodId == null) ? 0 : timePeriodId.hashCode());
		result = prime * result + wave;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimePeriod other = (TimePeriod) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (periodicity == null) {
			if (other.periodicity != null)
				return false;
		} else if (!periodicity.equals(other.periodicity))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (timePeriod == null) {
			if (other.timePeriod != null)
				return false;
		} else if (!timePeriod.equals(other.timePeriod))
			return false;
		if (timePeriodId == null) {
			if (other.timePeriodId != null)
				return false;
		} else if (!timePeriodId.equals(other.timePeriodId))
			return false;
		if (wave != other.wave)
			return false;
		return true;
	}	
	
	
}
