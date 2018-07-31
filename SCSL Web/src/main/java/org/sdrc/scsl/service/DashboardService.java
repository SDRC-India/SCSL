package org.sdrc.scsl.service;

import java.util.List;
import java.util.Map;

import org.sdrc.scsl.model.web.BoxChart;
import org.sdrc.scsl.model.web.DashboardChartModel;
import org.sdrc.scsl.model.web.DashboardLandingModel;
import org.sdrc.scsl.model.web.IndicatorModel;
import org.sdrc.scsl.model.web.PDSAModel;
import org.sdrc.scsl.model.web.PDSASummaryModel;
import org.sdrc.scsl.model.web.SmallMultipleExcelModel;
import org.sdrc.scsl.model.web.ValueModel;

/** 
 * @author Sarita Panigrahi, created on: 21-Sep-2017
 * This service holds all the
 *         methods to be called in dashboard home, facility view, small multiple
 *         and pdsa summary created on: 21-09-2017
 *
 */
public interface DashboardService {
	/**
	* @author Sarita Panigrahi(sarita@sdrc.co.in)
	* Returns all the kpi values to Dashboard Home top line grid/box view
	*/
	List<BoxChart> getAllLandingBoxValue();

	/** 
	 * @author Sarita Panigrahi, created on: 27-Sep-2017
	 * @param dashboardLandingModel
	 * @return Excel for landing page (includes data values in tabular format and inline chart images) in XLSX
	 */
	String getDashboardHomeExcel(DashboardLandingModel dashboardLandingModel);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param pdsaId
	 * @return
	 * get control chart for each PDSA in pdsa summary view
	 */
	Map<String, Object> getPDSAControlChart(Integer pdsaId);
	
	/**This method will return all the PDSA within a given facility
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @author Sarita Panigrahi
	 * @param facilityId
	 * @return
	 */
	List<PDSAModel> getAllPDSAWithinAFacility(int facilityId);


	/** 
	 * @author Sarita Panigrahi, created on: 10-Oct-2017
	 * @param pdsaSummaryModel
	 * @return  Get PDSA summary chart for the selected PDSA
	 */
	String getPDSASummaryExcelFilePath(PDSASummaryModel pdsaSummaryModel);

	/** 
	 * @author Sarita Panigrahi, created on: 11-Oct-2017
	 * update all deo pdsa txn table
	 */
	void updateAllDeoPDSATxn();

	/** 
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param facilityId
	 * @return
	 * Gives all indicator data for a given facility
	 */
	List<List<DashboardChartModel>> getDashboardFacilityCharts(Integer facilityId, Boolean hasLr);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param indicatorId
	 * @param stateId
	 * @param districtId
	 * @param facilityTypeId
	 * @param facilitySizeId
	 * @param waveParam
	 * @param periodicity
	 * @return
	 * Gives all facility data for a given indicator
	 */
	Map<String, List<List<DashboardChartModel>>> getDashboardIndicatorViewCharts(Integer indicatorId, Integer stateId,
			Integer districtId, Integer facilityTypeId, Integer facilitySizeId, Integer waveParam, String periodicity, String indType);

	/** 
	 * @author Sarita Panigrahi, created on: 10-Oct-2017
	 * @param indicatorId
	 * @return  get the list of related indicator for a particular indicator
	 */
	Map<String, List<IndicatorModel>> getRelatedIndicators(Integer indicatorId);

	/** 
	 * @author Sarita Panigrahi, created on: 11-Oct-2017
	 * @param smallMultipleExcelModel
	 * @return
	 * get small multiple dwonload excel file
	 */
	String getSmallMultipleExcelFilePath(SmallMultipleExcelModel smallMultipleExcelModel);

	/** 
	 * @author Sarita Panigrahi, created on: 23-Oct-2017
	 * @return
	 * get list of indicators
	 */
	Map<String, Map<String, List<ValueModel>>> getAllIndicator();

	/** 
	 * @author Sarita Panigrahi, created on: 23-Oct-2017
	 * @param chartSvgs
	 * @param areaName
	 * @return
	 * download google map view engagement score chart as an image file
	 */
	String downloadTrendImage(List<String> chartSvgs, String areaName);

}
