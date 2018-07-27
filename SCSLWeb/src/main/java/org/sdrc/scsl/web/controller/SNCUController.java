package org.sdrc.scsl.web.controller;


import java.util.List;
import java.util.Map;

import org.sdrc.scsl.core.Authorize;
import org.sdrc.scsl.model.mobile.TimePeriodModel;
import org.sdrc.scsl.model.web.IndicatorModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.SNCUIndicatorDataModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.ValueObject;
import org.sdrc.scsl.service.SNCUService;
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
* @author Mandakini Biswal
* @author Subrata kumar 26/04/2017
*
*/

@Controller
public class SNCUController {
 
	@Autowired
	private SNCUService sncuService;
	
	@Autowired
	private StateManager stateManager;
	
	/*@RequestMapping("/saveSNCUIndicator")
	@ResponseBody
	public ReturnModel saveSNCUIndicator(@RequestBody Map<String, List<IndicatorModel>> sncuIndicatorModel,
								  @RequestParam("timeperiodId") Integer timeperiodId){
		return sncuService.saveSNCUIndicator(sncuIndicatorModel, timeperiodId);
	}*/
	
	@Authorize(feature="sncuDataEntry", permission="view")
	@RequestMapping(value = "/sncuDataEntry")
	public String getSncuDataEntry(){
		
		if(null!=((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL)))
			return "sncuDataEntry";
		else
			return "redirect:/";
	}
	
	/**
	* @author Subrata(Subrata@sdrc.co.in)
	*	View for getting indicators name, core area, numerator, denominator add indicator view 
	*/
	@Authorize(feature="sncuDataEntry", permission="view")
	@RequestMapping(value= {"/getIndicators"}, method= {RequestMethod.GET})
	@ResponseBody

	public SNCUIndicatorDataModel getSNCUIndicators(@RequestParam("timeperiodId") Integer timeperiodId,
			@RequestParam(required=false,value="facilityId") Integer facilityId,
			@RequestParam(required=false,value="hasLr") Boolean hasLr )throws Exception{
		return sncuService.getSNCUIndicator(timeperiodId, facilityId, hasLr);
	}
	/**
	* @author Subrata(Subrata@sdrc.co.in)
	*	View for getting rest of indicators add indicator view
	*/
	@Authorize(feature="sncuDataEntry", permission="view")
	@RequestMapping(value= {"/getRestIndicators"}, method= {RequestMethod.GET})
	@ResponseBody
	public Map<String, List<ValueObject>> getRestIndicators(@RequestParam("timeperiodId") Integer timerperiodId)throws Exception{
		return sncuService.getRestIndicators(timerperiodId, null, null);
	}
	/**
	* @author Subrata(Subrata@sdrc.co.in)
	*	View for updating indicators in add indicator view. 
	*/
	@Authorize(feature="sncuDataEntry", permission="edit")
	@RequestMapping(value= {"/updateIndicators"}, method= {RequestMethod.POST})
	@ResponseBody
	public void updateIndicators(@RequestBody List<ValueObject> sncuIndicatorModel, 
								@RequestParam("timeperiodId") Integer timerperiodId)throws Exception{
		sncuService.updateIndicatorNames(sncuIndicatorModel, timerperiodId);
	}
	
	/**
	* @author Subrata(Subrata@sdrc.co.in)
	*	save sncu data
	*/
	@Authorize(feature="sncuDataEntry", permission="edit")
	@RequestMapping(value= {"/saveSNCUIndicator"}, method= {RequestMethod.POST})
	@ResponseBody
	public ReturnModel saveSNCUData(@RequestBody Map<String, List<IndicatorModel>> sncuIndicatorModel,
			  						@RequestParam("timeperiodId") Integer timeperiodId){
		return sncuService.updateIndicatorOfFacility(sncuIndicatorModel, timeperiodId);
	}
	
	/**
	* @author Subrata(Subrata@sdrc.co.in)
	*	get latest timeperiod
	*/
	@Authorize(feature="sncuDataEntry", permission="view")
	@RequestMapping(value= {"/getTimePeriod"}, method= {RequestMethod.GET})
	@ResponseBody
	public TimePeriodModel getTimePeriod()throws Exception{
		return sncuService.getCurrentTimePeriod();
	}
	// =====createTimeperiod and updateindicatorfacility job test=================	
	/*@RequestMapping(value= {"/createTimePeriod"}, method= {RequestMethod.GET})
	@ResponseBody
	public void createTimePeriod()throws Exception{
		sncuService.createTimeperiod();
	}
	
	@RequestMapping(value= {"/updateFacility"}, method= {RequestMethod.GET})
	@ResponseBody
	public void updateFacility()throws Exception{
		sncuService.updateFacilityIndicatorMapping();
	}*/
	
	//we are keeping these page load calls here.. as submissionManagement controller has class level requestmapping
	
	/**
	 * @return submissionManagement view
	 */
	@Authorize(feature="submissionManagement", permission="view")
	@RequestMapping(value = "/submissionManagement")
	public String getSubmissionManagement(){
		return "submissionManagement";
	}
	
	/**
	 * @return viewSubmission page
	 */
	@Authorize(feature="submissionManagement", permission="view")
	@RequestMapping(value = "/viewSubmission")
	public String getViewSubmissionPage(){
		
		if(null!=((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL)))
			return "viewSubmission";
		else
			return "redirect:/";
		
	}
	
	/**
	 * @param timeperiodId
	 * @return
	 */
	/*@Authorize(feature = "dashboardHome", permission = "view")
	@RequestMapping(value = "/saveAllMapping")
	@ResponseBody
	public String saveAllMapping(@RequestParam("timeperiodId") int timeperiodId){
		try {
			sncuService.updateMasterFacilityIndicatorMapping(timeperiodId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "done";
	}*/
	
	@Authorize(feature = "dashboardHome", permission = "view")
	@RequestMapping(value = "/saveLatestMapping")
	@ResponseBody
	public String saveLatestMapping(){
		try {
			sncuService.updateFacilityIndicatorMapping();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Mapping done";
	}
	
	@Authorize(feature = "dashboardHome", permission = "view")
	@RequestMapping("/createTimePeriod")
	@ResponseBody
	public String createTimePeriod(){
		try {
			sncuService.createTimeperiod();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "TimePeriod created";
	}
}
