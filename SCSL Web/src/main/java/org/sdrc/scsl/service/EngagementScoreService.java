package org.sdrc.scsl.service;

import java.util.List;

import org.sdrc.scsl.model.web.EngagementAreaModel;
import org.sdrc.scsl.model.web.LineChartDataModel;
import org.sdrc.scsl.model.web.MSTEngagementScoreAndFacilityCollectionModel;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 *This service will hold all the methods required in engagement score data entry
 */

public interface EngagementScoreService {

	/**
	 * This method will return the Master Engagement Score and Facility area and
	 * Timeperiod Facility Area will be according to facility assigned to that
	 * user TimePeriod List will be within the Facility Object ,as each Facility
	 * all the Timeperiod
	 * 
	 * @return
	 */
	MSTEngagementScoreAndFacilityCollectionModel getMSTEngagementScoreData();

	/**
	 * This method will persist the engagement score for a selected Facility and
	 * selected Timperiod and selected score from the score option
	 * 
	 * @param engagementScoreId
	 * @param facilityId
	 * @param timeperiodId
	 * @return
	 */
	boolean persistTXNEngagementScore(int engagementScoreId,
			int facilityId, int timeperiodId);

	/**
	 * This method will return the last 12 submission of engagement score for a
	 * selected Facility(Area)
	 * 
	 * @param facilityId
	 * @return
	 */
	List<LineChartDataModel> getLineChartEngagementScoreOfAFacility(
			int facilityId);
	
	/**
	 * @return This method will provide all google map facility view geographic data of the engagement score for the particular facility for the last reported month.
	 */
	List<EngagementAreaModel> getEngagementAreaModel();
	
	/**
	 * This method will send the mail for the pending 
	 */
	boolean pendingEngagementScoreMailService();
}
