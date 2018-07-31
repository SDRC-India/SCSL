package org.sdrc.scsl.service;

import java.util.List;
import java.util.Map;

import org.sdrc.scsl.domain.TXNSubmissionManagement;
import org.sdrc.scsl.model.mobile.TimePeriodModel;
import org.sdrc.scsl.model.web.IndicatorModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.SNCUIndicatorDataModel;
import org.sdrc.scsl.model.web.ValueObject;

/**
 * @author Mandakini Biswal
 * @author Subrata(subrata@sdrc.co.in)
 * @author Sarita(sarita@sdrc.co.in)
 * @author Harsh pratyush(harsh@sdrc.co.in)
 *This service is responsible for all the transaction made in sncu/nicu data entry
 */

public interface SNCUService {

//	ReturnModel saveSNCUIndicator(Map<String, List<IndicatorModel>> sncuIndicatorModel, Integer timeperiodId);
	
	/**
	 * @param timePeriod
	 * @param facilityId
	 * @param hasLr
	 * @return
	 * View for getting indicators name, core area, numerator, denominator add indicator view 
	 */
	SNCUIndicatorDataModel getSNCUIndicator(Integer timePeriod, Integer facilityId, Boolean hasLr);
		
	/**
	 * @param sncuIndicatorModel
	 * @param timerperiodId
	 * View for updating indicators in add indicator view. 
	 */
	void updateIndicatorNames(List<ValueObject> sncuIndicatorModel, Integer timerperiodId);
	
	/**
	 * @param sncuIndicatorModel
	 * @param timePeriod
	 * @return
	 * save sncu data
	 */
	ReturnModel updateIndicatorOfFacility(Map<String, List<IndicatorModel>> sncuIndicatorModel, Integer timePeriod);
	
	/**
	 * @return get latest timeperiod
	 */
	TimePeriodModel getCurrentTimePeriod();

	void updateFacilityIndicatorMapping() throws Exception;

	void updateMasterFacilityIndicatorMapping(int timeperiodId) throws Exception;

	void createTimeperiod() throws Exception;

	/**
	 * @param facilityId
	 * @param timePeriod
	 * @param isEnable
	 * @param hasLr
	 * @return
	 *  get blank data entry form
	 */
	Map<String, Map<String, List<IndicatorModel>>> getBlankForm(Integer facilityId, Integer timePeriod,
			Boolean isEnable, Boolean hasLr);

	/**
	 * @param listOfIndicators
	 * @param isEnable
	 * @param facilityId
	 * @param timePeriod
	 * @param hasLr
	 * @return
	 *  get pre fetched data entry from
	 */
	Map<String, Map<String, List<IndicatorModel>>> getData(List<Object[]> listOfIndicators, Boolean isEnable,
			Integer facilityId, Integer timePeriod, Boolean hasLr);

	/**
	 * @param txnSubmissionId
	 * @param refTxnSubmissionId
	 * @param sncuIndicatorModel
	 * @param noOfDeliveryIndicators
	 * @param noOfLiveBirthIndicators
	 */
	void setSNCUIndicatorModel(Integer txnSubmissionId, Integer refTxnSubmissionId,
			Map<String, List<IndicatorModel>> sncuIndicatorModel, List<Integer> noOfDeliveryIndicators,
			List<Integer> noOfLiveBirthIndicators);

	/**
	 * @param sncuIndicatorModel
	 * @param timeperiodId
	 * @param noOfDeliveryIndicators
	 * @param noOfLiveBirthIndicators
	 * @param isLegacy
	 * @param facilityId
	 * @return
	 * ave the sncu profile entry and data entry data
	 */
	ReturnModel saveSNCUIndicator(Map<String, List<IndicatorModel>> sncuIndicatorModel, Integer timeperiodId,
			List<Integer> noOfDeliveryIndicators, List<Integer> noOfLiveBirthIndicators, Boolean isLegacy,
			Integer facilityId);

	/**
	 * @param facilityId
	 * @param timePeriod
	 * @param createdBy
	 * @param isLegacy
	 * @return
	 */
	TXNSubmissionManagement setTXNSubmissionManagement(Integer facilityId, Integer timePeriod, String createdBy,
			Boolean isLegacy);

	/**
	 * @param timerperiodId
	 * @param facilityId
	 * @param hasLr
	 * @return
	 * View for getting rest of indicators add indicator view
	 */
	Map<String, List<ValueObject>> getRestIndicators(Integer timerperiodId, Integer facilityId, Boolean hasLr);
}
