package org.sdrc.scsl.web.controller;

import java.util.List;

import org.sdrc.scsl.core.Authorize;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.SubmittedDataModel;
import org.sdrc.scsl.model.web.SubmittedFacilityDetailModel;
import org.sdrc.scsl.service.SubmissionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
*	this controller is specific for submission management view page related requests
*/
@Controller
@RequestMapping("/submissions")
public class SubmissionManagementController {

	@Autowired
	private SubmissionManagementService submissionManagementService;
	
	/**
	* @author Sarita Panigrahi(sarita@sdrc.co.in)
	*	superintendent view for submissions 
	*/
	
	@Authorize(feature="submissionManagement", permission="view")
	@RequestMapping("/superintendent")
	@ResponseBody
	public List<SubmittedDataModel> getAllSubmissionsForSuperintendent(){
		return submissionManagementService.getAllSubmissionsForSuperintendent();
	}
	
	/**
	 * MnE view for submission list in data table 
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param stateId
	 * @param districtId
	 * @param facilityId
	 * @return
	 */
	@Authorize(feature="submissionManagement", permission="view")
	@RequestMapping("/superintendentMnE")
	@ResponseBody
	public List<SubmittedDataModel> getAllSubmissionsForSuperintendentMnE(){
		return submissionManagementService.getAllSubmissionsForSuperintendentMnE();
	}
	
	/**
	 * View each submission in detail in submission management view
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @return
	 */
	@Authorize(feature="submissionManagement", permission="view")
	@RequestMapping(value="/viewSubmissionInicators", method=RequestMethod.POST)
	@ResponseBody
	public List<SubmittedFacilityDetailModel> viewSubmission(
			@RequestParam(value = "txnSubmissionId", required = false) Integer txnSubmissionId,
			@RequestParam(value = "refSubmissionId", required = false) Integer refSubmissionId,
			@RequestParam(value = "facilityId", required = false) Integer facilityId,
			@RequestParam(value = "timePeriodId", required = false) Integer timePeriodId) {
		
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("viewSubmission");
//		modelAndView.addObject("submission", submissionManagementService.fetchSubmittedValues(txnSubmissionId,
//				refSubmissionId, facilityId, timePeriodId));
		return submissionManagementService.fetchSubmittedValues(txnSubmissionId,
				refSubmissionId, facilityId, timePeriodId);
	}
	
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @return view page
	 */
	@Authorize(feature="submissionManagement", permission="view")
	@RequestMapping(value="/viewSubmissionInicators", method=RequestMethod.GET)
	public String viewDefaultSubmissionPage(){
		return "submissionManagement";
	}
	
	/** approve or reject a submission by Superintendent / MnE
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param txnSubmissionId
	 * @param remarks
	 * @param isApprove
	 * @return
	 */
	@Authorize(feature="submissionManagement", permission="approveReject")
	@RequestMapping(value="/approveOrReject",  method=RequestMethod.POST)
	@ResponseBody
	public ReturnModel approveOrRejectBySuperintendentAndMnE(@RequestParam(value="txnSubmissionId") Integer txnSubmissionId,
			@RequestParam(value="isApprove") Boolean isApprove, @RequestBody(required=false) String remarks){
		return submissionManagementService.approveOrRejectBySuperintendentAndMnE(txnSubmissionId, remarks, isApprove);
	}
	
	@RequestMapping(value="/autoAprroveSuperintrndent",  method=RequestMethod.GET)
	public @ResponseBody boolean autoAprroveSuperintrndent(){
		return submissionManagementService.autoApproveForSuperintendent();
	}
	
}
