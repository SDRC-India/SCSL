package org.sdrc.scsl.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.sdrc.scsl.core.Authorize;
import org.sdrc.scsl.model.mobile.AreaModel;
import org.sdrc.scsl.model.web.TimePeriodModel;
import org.sdrc.scsl.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Debi
 * @author Subrat
 * @author Sarita
 *
 */
@Controller
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	private static final Logger LOGGER=Logger.getLogger(ReportController.class);
	
	/**
	 * @return
	 */
	@Authorize(feature = "report", permission = "view")
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public String getReportView(){
		return "report";
	}
	
	/**
	 * @param reporttypeId
	 * @param stateId
	 * @param districtId
	 * @param facilitypeId
	 * @param facilitysizeId
	 * @param startDate
	 * @param endDate
	 * @param facilityId
	 * @param periodicity
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 * @throws Exception
	 */
	@Authorize(feature="report", permission="view")
	@RequestMapping("/report")
	@ResponseBody
	public String createReporForRawData(@RequestParam(value = "reporttypeId", required = true)Integer reporttypeId ,
										@RequestParam(value = "stateId", required = false)Integer stateId,
										@RequestParam(value = "districtId", required = false)Integer districtId,
										@RequestParam(value = "facilitypeId", required = false) Integer facilitypeId,
										@RequestParam(value = "facilitysizeId", required = false)Integer facilitysizeId,
										@RequestParam(value = "startDate", required = true) String startDate,
										@RequestParam(value = "endDate", required = false)String endDate,
										@RequestParam(value = "facilityId", required = true)Integer facilityId,
										@RequestParam(value = "periodicity", required = false)String periodicity,
										@RequestParam(value = "startDateStr", required = true) String startDateStr,
										@RequestParam(value = "endDateStr", required = false)String endDateStr) throws Exception {
		
		return reportService.createReport(facilityId, reporttypeId, districtId, facilitypeId, facilitysizeId, stateId, startDate, endDate, periodicity, startDateStr, endDateStr);
	}
	
	
	/**
	 * @param periodicityId
	 * @return
	 */
	@Authorize(feature = "report", permission = "view")
	@RequestMapping("/getTimePeriodForReport")
	@ResponseBody
	public List<TimePeriodModel> getAllTimePeriodForReport(
			@RequestParam(value = "periodicityId", required = true) String periodicityId) {
		return reportService.getAllTimePeriodForReport("report", periodicityId);
	}
	
	/**
	 * @param viewName
	 * @return
	 */
	@Authorize(feature="report,dashboardChartView,pdsaSummary,dashboardFacilityView,dashboardHome", permission="view")
	@RequestMapping("/getAreaForReportFilterData")
	@ResponseBody
	public Map<String,Object> getAreaForReportFilterData(@RequestParam(value = "viewName", required = false) String viewName) {
		return reportService.getAreaForReportFilterData(viewName);
	}
	
	/**
	 * @param name
	 * @param response
	 */
	@Authorize(feature = "report,dashboardChartView,pdsaSummary,dashboardFacilityView,dashboardHome", permission = "view")
	@RequestMapping(value = "/downloadReport", method = RequestMethod.POST)
	public void downLoad(@RequestParam("fileName") String name,
			HttpServletResponse response) throws IOException{
         
		String fileName = "";
		InputStream inputStream = null;
		try {
			fileName = name.trim().replaceAll("/", "_").replaceAll("%3A", ":").replaceAll("%2F", "/")
					.replaceAll("%5C", "/").replaceAll("%2C", ",").replaceAll("\\\\", "/")
					.replaceAll("\\+", " ").replaceAll("%22", "")
					.replaceAll("%3F", "?").replaceAll("%3D", "=");
			
			fileName = new File(fileName).getAbsolutePath();
			File file = new File(fileName);
			/*if(file.exists()) {
				LOGGER.info("File exists in location (replaced filename) : "+fileName + "argument file name: "+ name);
			}else {
				LOGGER.info("File doesn't exist in location (replaced filename) : "+fileName + "argument file name: "+ name);	
			}*/
			
			inputStream = new FileInputStream(file);
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					new java.io.File(fileName).getName());
			response.setHeader(headerKey, headerValue);
			response.setContentType("application/octet-stream"); // for all file
																	// type
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			LOGGER.info("File doesn't exist in location (replaced filename) : "+fileName + "argument file name: "+ name);	
			LOGGER.error("report not found ", e);
		} catch (IOException e) {
			LOGGER.error("report io exception ", e);
		}
		finally 
		{
			new File(fileName).delete();
		}
		
	}
	
	/**
	 * @return
	 */
	@Authorize(feature="report,dashboardChartView,pdsaSummary,dashboardFacilityView,dashboardHome", permission="view")
	@RequestMapping("/getFacilityTypeAndSize")
	@ResponseBody
	public Map<String, List<AreaModel>> getFacilityTypeAndSize() {
		return reportService.getFacilityTypeAndSize();
	}

}
