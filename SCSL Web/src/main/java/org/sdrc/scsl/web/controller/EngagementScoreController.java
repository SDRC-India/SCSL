package org.sdrc.scsl.web.controller;

import java.util.List;

import org.sdrc.scsl.core.Authorize;
import org.sdrc.scsl.model.web.EngagementAreaModel;
import org.sdrc.scsl.model.web.LineChartDataModel;
import org.sdrc.scsl.model.web.MSTEngagementScoreAndFacilityCollectionModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.service.EngagementScoreService;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EngagementScoreController {
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 */
	@Autowired
	private EngagementScoreService engagementScoreService;
	
	@Autowired
	private StateManager stateManager;
	
	@Authorize(feature="engagementScore", permission="view")
	@RequestMapping("engagementScore")
	public String getEngagementScorePage()
	{
		if(null!=((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL)))
			return "engagementScore";
		else
			return "redirect:/";
		
	}
	
	@Authorize(feature="engagementScore", permission="view")
	@RequestMapping("getMSTEngagementScoreData")
	@ResponseBody
	public MSTEngagementScoreAndFacilityCollectionModel getMSTEngagementScoreData() {
		return engagementScoreService.getMSTEngagementScoreData();

	}

	@Authorize(feature="engagementScore", permission="edit")
	@RequestMapping("saveTXNEngagementScore")
	@ResponseBody
	public boolean saveTXNEngagementScore(
			@RequestParam(value = "engagementScoreId", required = true) int engagementScoreId,
			@RequestParam(value = "facilityId", required = true) int facilityId,
			@RequestParam(value = "timeperiodId", required = true) int timeperiodId) {
		return engagementScoreService.persistTXNEngagementScore(
				engagementScoreId, facilityId, timeperiodId);
	}

	@Authorize(feature="dashboardFacilityView,engagementScore", permission="view")
	@RequestMapping("getLineChartOfEngagementScore")
	@ResponseBody
	public List<LineChartDataModel> getLineChartOfEngagementScore(
			@RequestParam(value = "facilityId", required = true) int facilityId) {
		return engagementScoreService
				.getLineChartEngagementScoreOfAFacility(facilityId);
	}
	
	@Authorize(feature="dashboardFacilityView", permission="view")
	@RequestMapping("getEngagementScorePushPins")
	@ResponseBody
	public List<EngagementAreaModel> getEngagementAreaModel()
			 {
		return engagementScoreService.getEngagementAreaModel();
	}
	
	//remove this job later
//	@RequestMapping("sendEngPendingMail")
//	public @ResponseBody boolean sendEngPendingMail(){
//		return engagementScoreService.pendingEngagementScoreMailService();
//	}
}
