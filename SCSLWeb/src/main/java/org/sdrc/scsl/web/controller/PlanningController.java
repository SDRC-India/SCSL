/**
 * 
 */
package org.sdrc.scsl.web.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.scsl.core.Authorize;
import org.sdrc.scsl.model.web.PlanningModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.TxnPlanningModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.service.PlanningService;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@Controller
public class PlanningController {
	
	@Autowired
	private PlanningService planningService;

	@Autowired
	private StateManager stateManager;

	@Authorize(feature="planning",permission="view")
	@RequestMapping("/getPlanningData")
	@ResponseBody
	public PlanningModel getPlanningData()
	{
		return planningService.getPlanningData();
	}
	
	@Authorize(feature="planning",permission="view")
	@RequestMapping("/planning")
	public String getPlanningPage()
	{
		if(null!=((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL)))
			return "planning";
		else
			return "redirect:/";
	
	}
	
	
	@Authorize(feature="planning",permission="edit")
	@RequestMapping(value = "/releasePlanning", method = RequestMethod.POST)
	@ResponseBody
	public ReturnModel releasePlanning(@RequestParam("planningId")int planningId)throws Exception
	{
		return planningService.releasePlanning(planningId);
	}
	
	@Authorize(feature="planning",permission="edit")
	@RequestMapping(value = "/addPlannings", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseBody
	public ReturnModel addPlanning(@RequestPart("txnPlanningModel")TxnPlanningModel txnPlanningModel,@RequestPart("agenda")MultipartFile agenda)
	{
		txnPlanningModel.setDocument(agenda);
		return planningService.planFacility(txnPlanningModel);
	}
	
	@Authorize(feature="planning",permission="edit")
	@RequestMapping(value = "/closePlanning", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseBody
	public ReturnModel closePlanning(@RequestPart("txnPlanningModel")TxnPlanningModel txnPlanningModel,@RequestPart("tripReport")MultipartFile tripReport)
	{
		txnPlanningModel.setDocument(tripReport);
		return planningService.uploadPlanningReport(txnPlanningModel);
	}
	
	
	@Authorize(feature="planning",permission="view")
	@RequestMapping(value="/downloadPlanningFile",method=RequestMethod.POST)
	public void downloadPlanningFile(@RequestParam("fileName") String name,
			HttpServletResponse response) throws IOException {

		InputStream inputStream;
		String fileName = "";
		try {
			fileName = name.replaceAll("%3A", ":").replaceAll("%2F", "/")
					.replaceAll("%5C", "/").replaceAll("%2C", ",")
					.replaceAll("\\+", " ").replaceAll("%22", "")
					.replaceAll("%3F", "?").replaceAll("%3D", "=");
			inputStream = new FileInputStream(fileName);
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					new java.io.File(fileName).getName());
			response.setHeader(headerKey, headerValue);
			response.setContentType("application/octet-stream"); // for all file
																	// type
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			outputStream.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
