/**
 * 
 */
package org.sdrc.scsl.model.web;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class AssessmentHistory {

	// Id of planning
	private int planningId;
	
	// date of planning
	private String plannedDate;
	
	// date of visit of facility(if not visited will be  null)
	private String visitedDate;
	
	// path of agenda uploaded while planning
	private String agendaPath;
	
	// path of report uploaded after visit(Will be null if not visited)
	private String reportPath;
	
	// true if not visited and planning done by the logged in user  
	private boolean isReportUpload; 
	
	
	// userId of user 
	private int userId;
	
	// Name of user
	private String userName;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPlanningId() {
		return planningId;
	}

	public void setPlanningId(int planningId) {
		this.planningId = planningId;
	}

	public String getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(String plannedDate) {
		this.plannedDate = plannedDate;
	}

	public String getVisitedDate() {
		return visitedDate;
	}

	public void setVisitedDate(String visitedDate) {
		this.visitedDate = visitedDate;
	}

	public String getAgendaPath() {
		return agendaPath;
	}

	public void setAgendaPath(String agendaPath) {
		this.agendaPath = agendaPath;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public boolean isReportUpload() {
		return isReportUpload;
	}

	public void setReportUpload(boolean isReportUpload) {
		this.isReportUpload = isReportUpload;
	}
	
}
