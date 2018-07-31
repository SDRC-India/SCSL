package org.sdrc.scsl.service;

import java.util.List;
import java.util.Map;

import org.sdrc.scsl.model.web.AreaWebModel;
import org.sdrc.scsl.model.web.ExceptionModel;
import org.sdrc.scsl.model.web.IndicatorModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.SNCUIndicatorDataModel;
import org.sdrc.scsl.model.web.ValueObject;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sarita
 *  This service class gets called when user upload/download legacy/historical data into/from the system
 *
 */
public interface UploadHistoricalDataService {

	/**
	 * @param startTp
	 * @param endTp
	 * @param facilityId
	 * @param facilityName
	 * @param startTimeperiodName
	 * @param endTimePeriodName
	 * @param hasLr
	 * @return
	 * @throws Exception
	 * this method gets called when user download the excel template,
	 * if data is already present for the selected parameters
	 * in the system it will have those data in the file else it will simply download blank template
	 */
	String readDataAndDownloadExcel(Integer startTp, Integer endTp, Integer facilityId, String facilityName,
			String startTimeperiodName, String endTimePeriodName, Boolean hasLr) throws Exception;

	/**
	 * @return get all area
	 */
	List<AreaWebModel> getAllArea();

	/**
	 * @param xlsFile
	 * @return
	 * @throws Exception
	 *  this method calls when user uploads excel file
	 */
	List<ExceptionModel> uploadDataExcel(MultipartFile xlsFile) throws Exception;

//	String updateDiscrepantData(Integer facilityId,  Integer toTpId);

	/**
	 * @param timeperiodId
	 * @param facilityId
	 * @param hasLr
	 * @return
	 * this method gets called when admin selects a area and time period to view legacy web data entry 
	 */
	SNCUIndicatorDataModel getSNCUIndicators(Integer timeperiodId, Integer facilityId, Boolean hasLr);

	/**
	 * @param sncuIndicatorModel
	 * @param timeperiodId
	 * @param facilityId
	 * @return
	 * saves legacy data from web data entry
	 */
	ReturnModel saveLegacySNCUIndicatorData(Map<String, List<IndicatorModel>> sncuIndicatorModel,
			Integer timeperiodId, Integer facilityId);

	/**
	 * @param sncuIndicatorModels
	 * @param timerperiodId
	 * @param facilityId
	 * add indicator from historical web module
	 */
	void addIndicatorFacilityTimePeriodMapping(List<ValueObject> sncuIndicatorModels, Integer timerperiodId, Integer facilityId);
}
