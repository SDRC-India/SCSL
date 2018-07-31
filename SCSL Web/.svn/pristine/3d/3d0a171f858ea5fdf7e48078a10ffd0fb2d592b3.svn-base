package org.sdrc.scsl.service;

import java.util.List;
import java.util.Map;

import org.sdrc.scsl.model.mobile.AreaModel;
import org.sdrc.scsl.model.web.TimePeriodModel;

/**
 * @author Debi
 * @author Subrat
 * @author Sarita
 * This interface is responsible for reporting module
 */
public interface ReportService {
	
	/**
	 * @param viewName
	 * @return get list of area
	 */
	Map<String, Object>  getAreaForReportFilterData (String viewName);
	
	/**
	 * @return get all facility type and size
	 */
	Map<String, List<AreaModel>> getFacilityTypeAndSize();

	/**
	 * @param viewName
	 * @param periodicity
	 * @return  get timeperiod list
	 */
	List<TimePeriodModel> getAllTimePeriodForReport(String viewName, String periodicity);

	/**
	 * @param facilityId
	 * @param reporttypeId
	 * @param districtId
	 * @param facilityTypeId
	 * @param facilitySizeId
	 * @param stateId
	 * @param startDate
	 * @param endDate
	 * @param periodicity
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 * @throws Exception
	 *  this method gets called when user click on download report of any report type
	 */
	String createReport(Integer facilityId, Integer reporttypeId, Integer districtId, Integer facilityTypeId,
			Integer facilitySizeId, Integer stateId, String startDate, String endDate, String periodicity,String startDateStr,String endDateStr)
			throws Exception;

}
