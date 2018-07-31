package org.sdrc.scsl.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.sdrc.scsl.core.Authorize;
import org.sdrc.scsl.model.web.AreaWebModel;
import org.sdrc.scsl.model.web.ExceptionModel;
import org.sdrc.scsl.model.web.IndicatorModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.SNCUIndicatorDataModel;
import org.sdrc.scsl.model.web.TimePeriodModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.ValueObject;
import org.sdrc.scsl.service.ReportService;
import org.sdrc.scsl.service.SNCUService;
import org.sdrc.scsl.service.UploadHistoricalDataService;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sarita 
 * This controller will handle all legacy page handlers 
 *
 */
@Controller
public class UploadHistoricalDataController {

	@Autowired
	private UploadHistoricalDataService uploadHistoricalDataService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private ResourceBundleMessageSource messages;
	
	@Autowired
	private StateManager stateManager;
	
	@Autowired
	private SNCUService sncuService;
	
	private static final Logger LOGGER=Logger.getLogger(UploadHistoricalDataController.class);
	
	/**
	 * @param file
	 * @return
	 * @throws Exception
	 * upload historical data
	 */
	@Authorize(feature = "uploadHistoricalData", permission = "edit")
	@RequestMapping("/uploadHistoricalDataTemplateFile")
	public @ResponseBody List<ExceptionModel> uploadHistoricalDataUpload(@RequestPart(value = "file") MultipartFile file) throws Exception {

		try {
			return uploadHistoricalDataService.uploadDataExcel(file);
		} catch (BadCredentialsException e) {
			List<ExceptionModel> exceptionModelList = new ArrayList<>();

			ExceptionModel exceptionModel = new ExceptionModel();

			exceptionModel.setExceptionType("Invalid user");
			exceptionModel.setExceptionMessage("Not a valid user");
			exceptionModel.setStatusCode("510");

			exceptionModelList.add(exceptionModel);
			LOGGER.error("Invalid user error ", e);
			return exceptionModelList;
		}

	}

	/**
	 * @param facilityId
	 * @param facilityName
	 * @param startTimeperiod
	 * @param endTimePeriod
	 * @param startTimeperiodName
	 * @param endTimePeriodName
	 * @param hasLr
	 * @return
	 * get historical data file for selected parameters
	 */
	@Authorize(feature = "uploadHistoricalData", permission = "view")
	@RequestMapping("/getHistoricalDataTemplateFile")
	@ResponseBody
	public String uploadHistoricalDataDownload(@RequestParam("facilityId") Integer facilityId,
			@RequestParam("facilityName") String facilityName,
			@RequestParam("startTimeperiod") Integer startTimeperiod,
			@RequestParam(value="endTimePeriod", required = false) Integer endTimePeriod,
			@RequestParam("startTimeperiodName") String startTimeperiodName,
			@RequestParam(value="endTimePeriodName", required = false) String endTimePeriodName,
			@RequestParam("hasLr") Boolean hasLr) {

		try {
			return uploadHistoricalDataService.readDataAndDownloadExcel(startTimeperiod, endTimePeriod, facilityId, facilityName, startTimeperiodName, endTimePeriodName, hasLr);
		} catch (Exception e) {
			LOGGER.error("Error in getHistoricalDataTemplateFile ", e);
			return "";
		}
	}
	
	/**
	 * @param name
	 * @param response
	 * @throws IOException
	 * download historical template
	 */
	@Authorize(feature = "uploadHistoricalData", permission = "view")
	@RequestMapping(value = "/downloadHistoricalDataTemplateFile", method = RequestMethod.POST)
	public void downLoad(@RequestParam("fileName") String name,
			HttpServletResponse response) throws IOException {

		String fileName = "";
		InputStream inputStream = null;
		try {
			fileName = name.trim().replaceAll("/", "_").replaceAll("%3A", ":").replaceAll("%2F", "/")
					.replaceAll("%5C", "/").replaceAll("%2C", ",").replaceAll("\\\\", "/")
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
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {

			LOGGER.error("Error FileNotFoundException ", e);
		} catch (IOException e) {
			LOGGER.error("Error IOException ", e);
		}finally 
		{
			new File(fileName).delete();//delete the file once down
		}
		
	}
	
	/**
	 * @return give all area to drop down
	 */
	@Authorize(feature = "uploadHistoricalData", permission = "view")
	@RequestMapping(value = "/getAllAreaForHistoricalView", method = RequestMethod.GET)
	@ResponseBody
	public List<AreaWebModel> getAllArea(){
		return uploadHistoricalDataService.getAllArea();
	}
	
	/**
	 * @return uploadHistoricalData view
	 */
	@Authorize(feature = "uploadHistoricalData", permission = "view")
	@RequestMapping(value = "/uploadHistoricalData", method = RequestMethod.GET)
	public String getUploadHistoricalDataView(){
		
		if(null!=((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL)))
			return "uploadHistoricalData";
		else
			return "redirect:/";
		
		
	}
	
	/**
	 * @return timeperiod list
	 */
	@Authorize(feature = "uploadHistoricalData", permission = "view")
	@RequestMapping(value = "/getTimePeriodForLegacy", method = RequestMethod.GET)
	@ResponseBody
	public List<TimePeriodModel> getAllTimePeriodForReport() {
		return reportService.getAllTimePeriodForReport("legacy", 
				messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null));
	}
	
	/**
	 * @return legacySncuDataEntry view
	 */
	@Authorize(feature="uploadHistoricalData", permission="view")
	@RequestMapping(value = "/legacySncuDataEntry")
	public String getSncuDataEntry(){
		
		if(null!=((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL)))
			return "legacySncuDataEntry";
		else
			return "redirect:/";
	}
	
	/**
	 * @param timeperiodId
	 * @param facilityId
	 * @param hasLr
	 * @return
	 * when admin selects a area and time period to view legacy web data entry 
	 */
	@Authorize(feature="uploadHistoricalData", permission="view")
	@RequestMapping(value= {"/getLegacyWebDataEntryIndicators"}, method= {RequestMethod.GET})
	@ResponseBody
	public SNCUIndicatorDataModel getSNCUIndicators(@RequestParam("timeperiodId") Integer timeperiodId,
			@RequestParam(required=false,value="facilityId") Integer facilityId,
			@RequestParam(required=false,value="hasLr") Boolean hasLr ){
		return uploadHistoricalDataService.getSNCUIndicators(timeperiodId, facilityId, hasLr);
	}
	
	/**
	 * @param sncuIndicatorModel
	 * @param timeperiodId
	 * @param facilityId
	 * @return
	 * saves legacy data from web data entry
	 */
	@Authorize(feature="uploadHistoricalData", permission="edit")
	@RequestMapping(value= {"/saveLegacySNCUIndicatorData"}, method= {RequestMethod.POST})
	@ResponseBody
	public ReturnModel saveSNCUData(@RequestBody Map<String, List<IndicatorModel>> sncuIndicatorModel,
			  						@RequestParam("timeperiodId") Integer timeperiodId,
			  						@RequestParam("facilityId") Integer facilityId){
		return uploadHistoricalDataService.saveLegacySNCUIndicatorData(sncuIndicatorModel, timeperiodId, facilityId);
	}
	
	/**
	* @author Sarita(sarita@sdrc.co.in)
	*	View for getting rest of indicators add indicator modal
	*/
	@Authorize(feature="uploadHistoricalData", permission="view")
	@RequestMapping(value= {"/getLegacyRestIndicators"}, method= {RequestMethod.GET})
	@ResponseBody
	public Map<String, List<ValueObject>> getRestIndicators(@RequestParam("timeperiodId") Integer timerperiodId,
			@RequestParam("facilityId") Integer facilityId,
			@RequestParam("hasLr") Boolean hasLr){
		return sncuService.getRestIndicators(timerperiodId, facilityId, hasLr);
	}
	
	/**
	 * @param sncuIndicatorModels
	 * @param timerperiodId
	 * @param facilityId
	 * add indicator from historical web module
	 */
	@Authorize(feature="uploadHistoricalData", permission="edit")
	@RequestMapping(value= {"/addLegacyIndicatorMappings"}, method= {RequestMethod.POST})
	@ResponseBody
	public void addIndicatorFacilityTimePeriodMapping(@RequestBody List<ValueObject> sncuIndicatorModels, 
								@RequestParam("timeperiodId") Integer timerperiodId,
								@RequestParam("facilityId") Integer facilityId){
		uploadHistoricalDataService.addIndicatorFacilityTimePeriodMapping(sncuIndicatorModels, timerperiodId, facilityId);
	}

	

/*	@RequestMapping(value = "/updateDiscrepantData", method = RequestMethod.GET)
	@ResponseBody
	public String updateDiscrepantData(@RequestParam("facilityId") Integer facilityId,
//			@RequestParam("fromTp") Integer fromTp,
			@RequestParam("toTp") Integer toTp) {
		return uploadHistoricalDataService.updateDiscrepantData(facilityId, toTp);
	}*/

}
