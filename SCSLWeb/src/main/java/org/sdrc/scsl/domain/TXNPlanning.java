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
 *         will hold the planning module related informations
 */
@Entity
@Table(name = "txn_planning")
public class TXNPlanning implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer planningId;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "agenda_report_filepath", length=1000)
	private String agendaReportFilepath;

	@Column(name = "trip_report_filepath", length=1000)
	private String tripReportFilepath;

	@Column(name = "tag_email")
	private String tagEmail;

	@Column(length=300)
	private String description;

	@Column(name = "plan_date")
	private Timestamp planDate;

	@Column(name = "visited_date")
	private Timestamp visitedDate;

	@Column(name = "is_live")
	private Boolean isLive;
	
	// ******** bi-directional many-to-one association to MSTUser // *******	
	@ManyToOne
	@JoinColumn(name="user_id_fk")
	private MSTUser mstUser;
	
	// ******** bi-directional many-to-one association to Area
		// *******
	@ManyToOne
	@JoinColumn(name="facility_id_fk")
	private Area facility;

	//GETTER SETTER **********************
	
	public Integer getPlanningId() {
		return planningId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public String getAgendaReportFilepath() {
		return agendaReportFilepath;
	}

	public String getTripReportFilepath() {
		return tripReportFilepath;
	}

	public String getTagEmail() {
		return tagEmail;
	}

	public String getDescription() {
		return description;
	}

	public Timestamp getPlanDate() {
		return planDate;
	}

	public Timestamp getVisitedDate() {
		return visitedDate;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public void setPlanningId(Integer planningId) {
		this.planningId = planningId;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setAgendaReportFilepath(String agendaReportFilepath) {
		this.agendaReportFilepath = agendaReportFilepath;
	}

	public void setTripReportFilepath(String tripReportFilepath) {
		this.tripReportFilepath = tripReportFilepath;
	}

	public void setTagEmail(String tagEmail) {
		this.tagEmail = tagEmail;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPlanDate(Timestamp planDate) {
		this.planDate = planDate;
	}

	public void setVisitedDate(Timestamp visitedDate) {
		this.visitedDate = visitedDate;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public MSTUser getMstUser() {
		return mstUser;
	}

	public void setMstUser(MSTUser mstUser) {
		this.mstUser = mstUser;
	}

	public Area getFacility() {
		return facility;
	}

	public void setFacility(Area facility) {
		this.facility = facility;
	}

}
