package org.sdrc.scsl.web.controller;

import java.util.List;
import java.util.Map;

import org.sdrc.scsl.core.Authorize;
import org.sdrc.scsl.model.web.BoxChart;
import org.sdrc.scsl.model.web.DashboardChartModel;
import org.sdrc.scsl.model.web.DashboardLandingModel;
import org.sdrc.scsl.model.web.IndicatorModel;
import org.sdrc.scsl.model.web.PDSAModel;
import org.sdrc.scsl.model.web.PDSASummaryModel;
import org.sdrc.scsl.model.web.SmallMultipleExcelModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.ValueModel;
import org.sdrc.scsl.service.DashboardService;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in) This controller class will hold
 *         all the mappings used in dashboard section created on: 21-09-2017
 *
 */
@Controller
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private StateManager stateManager;

	/**
	 * @author Sarita Panigrahi, created on: 25-Sep-2017
	 * @return load the dashboard home jsp if the user is authorized
	 */
	@Authorize(feature = "dashboardHome", permission = "view")
	@RequestMapping("dashboardHome")
	public String getDashboardHomePage() {
		return "dashboardHome";

	}

	/**
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @return load the facility view jsp if the user is authorized
	 */
	@Authorize(feature = "dashboardFacilityView", permission = "view")
	@RequestMapping("dashboardFacilityView")
	public String getFacilityViewPage() {
		return "dashboardFacilityView";

	}

	// pdsaSummary view
	/**
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @return load the pdsaSummary view jsp if the user is authorized
	 */
	@Authorize(feature = "pdsaSummary", permission = "view")
	@RequestMapping("pdsaSummary")
	public String getPdsaSummaryView() {
		return "pdsaSummary";

	}

	/**
	 * @author Sarita Panigrahi, created on: 11-Oct-2017
	 * @return load the small multiple view jsp if the user is authorized
	 */
	@Authorize(feature = "dashboardChartView", permission = "view")
	@RequestMapping("smallMultiple")
	public String getSmallMultipleView() {
		return "dashboardChartView";

	}

	/**
	 * @author Sarita Panigrahi, created on: 25-Sep-2017
	 * @return get all grid value
	 */
	@Authorize(feature = "dashboardHome", permission = "view")
	@RequestMapping("getAllLandingBoxValue")
	@ResponseBody
	public List<BoxChart> getAllLandingBoxValue() {
		return dashboardService.getAllLandingBoxValue();

	}

	/**
	 * @author Sarita Panigrahi, created on: 27-Sep-2017
	 * @param dashboardLandingModel
	 * @return get dashboard home excel file path
	 */
	@Authorize(feature = "dashboardHome", permission = "view")
	@RequestMapping(value = "getDashboardHomeExcelFilePath", method = RequestMethod.POST)
	@ResponseBody
	public String getDashboardHomeExcelFilePath(@RequestBody DashboardLandingModel dashboardLandingModel) {
		return dashboardService.getDashboardHomeExcel(dashboardLandingModel);
	}

	/**
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param pdsaId
	 * @return get control chart data
	 */
	@Authorize(feature = "pdsaSummary", permission = "view")
	@RequestMapping(value = "getPDSASummaryChart")
	@ResponseBody
	public Map<String, Object> getPDSASummaryChart(@RequestParam("pdsaId") Integer pdsaId) {
		return dashboardService.getPDSAControlChart(pdsaId);
	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param facilityId
	 * @return get each facility PDSAs
	 */
	@Authorize(feature = "pdsaSummary", permission = "view")
	@RequestMapping("getPDSAForFacility")
	@ResponseBody
	public List<PDSAModel> getPDSAForFacility(
			@RequestParam(value = "facilityId", required = false) Integer facilityId) {

		if (facilityId == null) {
			facilityId = ((UserModel) stateManager.getValue(Constants.Web.USER_PRINCIPAL)).getFacilityId(); // for
																											// data
																											// entry
																											// user
		}

		return dashboardService.getAllPDSAWithinAFacility(facilityId);
	}

	/**
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param pdsaSummaryModel
	 * @return
	 */
	@Authorize(feature = "pdsaSummary", permission = "view")
	@RequestMapping(value = "pdsaSummaryExcelFilePath", method = RequestMethod.POST)
	@ResponseBody
	public String getPDSASummaryExcelFilePath(@RequestBody PDSASummaryModel pdsaSummaryModel) {
		return dashboardService.getPDSASummaryExcelFilePath(pdsaSummaryModel);
	}

	/**
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param areaId
	 * @return Gives all small multiple indicator chart for a given facility
	 */
	@Authorize(feature = "dashboardChartView", permission = "view")
	@RequestMapping(value = "facilityChart", method = RequestMethod.GET)
	@ResponseBody
	public List<List<DashboardChartModel>> getDashboardFacilityChart(@RequestParam("areaId") Integer areaId,
			@RequestParam("hasLR") Boolean hasLR) {
		return dashboardService.getDashboardFacilityCharts(areaId, hasLR);
	}

	/**
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param indicatorId
	 * @param stateId
	 * @param districtId
	 * @param facilityTypeId
	 * @param facilitySizeId
	 * @param waveParam
	 * @param periodicity
	 * @param indType
	 * @return Gives all small multiple facility chart for a given indicator
	 */
	@Authorize(feature = "dashboardChartView", permission = "view")
	@RequestMapping(value = "indicatorChart", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<List<DashboardChartModel>>> getDashboardIndicatorViewCharts(
			@RequestParam(value = "indicatorId", required = true) Integer indicatorId,
			@RequestParam(value = "stateId", required = false) Integer stateId,
			@RequestParam(value = "districtId", required = false) Integer districtId,
			@RequestParam(value = "facilityTypeId", required = false) Integer facilityTypeId,
			@RequestParam(value = "facilitySizeId", required = false) Integer facilitySizeId,
			@RequestParam(value = "waveParam", required = false) Integer waveParam,
			@RequestParam(value = "periodicity", required = true) String periodicity,
			@RequestParam(value = "indType", required = false) String indType) {
		return dashboardService.getDashboardIndicatorViewCharts(indicatorId, stateId, districtId, facilityTypeId,
				facilitySizeId, waveParam, periodicity, indType);
	}

	/**
	 * @author Sarita Panigrahi, created on: 11-Oct-2017
	 * @param indicatorId
	 * @return gives all related indicators in small multiple indicator view
	 */
	@Authorize(feature = "dashboardChartView", permission = "view")
	@RequestMapping(value = "relatedIndicators", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<IndicatorModel>> getRelatedIndicators(
			@RequestParam(value = "indicatorId", required = true) Integer indicatorId) {
		return dashboardService.getRelatedIndicators(indicatorId);
	}

	/**
	 * @author Sarita Panigrahi, created on: 23-Oct-2017
	 * @param smallMultipleExcelModel
	 * @return
	 */
	@Authorize(feature = "dashboardChartView", permission = "view")
	@RequestMapping(value = "smallMultipleExcelFilePath", method = RequestMethod.POST)
	@ResponseBody
	public String getSmallMultipleExcelFilePath(@RequestBody SmallMultipleExcelModel smallMultipleExcelModel) {
		return dashboardService.getSmallMultipleExcelFilePath(smallMultipleExcelModel);
	}

	/**
	 * @author Sarita Panigrahi, created on: 23-Oct-2017
	 * @return
	 */
	@Authorize(feature = "report,dashboardChartView,pdsaSummary,dashboardFacilityView", permission = "view")
	@RequestMapping("/getAllIndicatorDetails")
	@ResponseBody
	public Map<String, Map<String, List<ValueModel>>> getIndicatordetails() {
		return dashboardService.getAllIndicator();

	}

	/**
	 * @author Sarita Panigrahi, created on: 23-Oct-2017
	 * @param chartSvgs
	 * @return
	 */
	@Authorize(feature = "dashboardFacilityView", permission = "view")
	@RequestMapping(value = "getGoogleMapTrendPath", method = RequestMethod.POST)
	@ResponseBody
	public String getGoogleMapTrendPath(@RequestBody List<String> chartSvgs,
			@RequestParam("areaName") String areaName) {
		return dashboardService.downloadTrendImage(chartSvgs, areaName);
	}
}
