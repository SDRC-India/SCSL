package org.sdrc.scsl.web.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.scsl.core.Authorize;
import org.sdrc.scsl.model.web.PDSADataModel;
import org.sdrc.scsl.model.web.PDSAModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.TXNPDSAModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.ValueModel;
import org.sdrc.scsl.service.PDSAService;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
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
 * @author Mandakini Biswal
 *
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * 
 * @author Sarita Panigrahi (sarita@sdrc.co.in)
 */
@Controller
public class PDSAController {

	@Autowired
	private PDSAService pdsaService;

	@Autowired
	private StateManager stateManager;

	@Autowired
	private ResourceBundleMessageSource errorMessageSource;

	@Authorize(feature = "pdsa", permission = "view")
	@RequestMapping(value = "/pdsa")
	public String getPDSAPage() {
		
		if(null!=((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL)))
			return "pdsa";
		else
			return "redirect:/";
	
	}

	@Authorize(feature = "pdsa", permission = "view")
	@RequestMapping(value = "/addPdsa")
	public String getAddPDSAPage() {
		
		if(null!=((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL)))
			return "addPdsa";
		else
			return "redirect:/";
	}

	@Authorize(feature = "pdsa", permission = "edit")
	@RequestMapping(value = "/savePDSADetails", method = RequestMethod.POST)
	@ResponseBody
	public ReturnModel savePDSADetails(@RequestBody PDSAModel pdsaModel) {
		boolean check = pdsaService.savePDSADetails(pdsaModel);
		ReturnModel returnModel = new ReturnModel();
		if (check) {
			returnModel.setErrorMessage(errorMessageSource.getMessage(
					Constants.Web.PDSA_ADD_SUCCESS, null, null));
			returnModel.setStatusCode(errorMessageSource.getMessage(
					Constants.Web.SUCCESS_STATUS_CODE, null, null));
			returnModel.setStatusMessage(errorMessageSource.getMessage(
					Constants.Web.BOOTSTRAP_ALERT_SUCCESS, null, null));
		} else {
			returnModel.setErrorMessage(errorMessageSource.getMessage(
					Constants.Web.PDSA_ADD_FAILED, null, null));
			returnModel.setStatusCode(errorMessageSource.getMessage(
					Constants.Web.FAILURE_STATUS_CODE, null, null));
			returnModel.setStatusMessage(errorMessageSource.getMessage(
					Constants.Web.BOOTSTRAP_ALERT_DANGER, null, null));
		}

		return returnModel;

		// public void addPDSAMessage()

	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param pdsaId
	 * @return
	 */
	@Authorize(feature = "pdsa", permission = "view")
	@RequestMapping("/getTXNPDSADetails")
	@ResponseBody
	public PDSADataModel getTXNPDSADetails(@RequestParam("pdsaId") int pdsaId) {

		return pdsaService.getTXNPdsaData(pdsaId);
	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param txnPDSAModel
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Authorize(feature = "pdsa", permission = "edit")
	@RequestMapping(value = "/saveTXNPDSADetails", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseBody
	public ReturnModel saveTXNPDSADetails(
			@RequestPart(value = "txnPdsa") List<LinkedHashMap> txnPDSAModel,
			@RequestPart(value = "file", required = false) MultipartFile file) {

		List<TXNPDSAModel> txnPDSAModels = new ArrayList<TXNPDSAModel>();

		for (LinkedHashMap map : txnPDSAModel) {
			TXNPDSAModel model = new TXNPDSAModel();
			model.setDueDate(map.get("dueDate").toString());
			model.setCreatedBy(map.get("createdBy") != null ? map.get(
					"createdBy").toString() : null);
			model.setCreatedDate(map.get("createdDate") != null ? map.get(
					"createdDate").toString() : null);
			model.setCssClass(map.get("cssClass") != null ? map.get("cssClass")
					.toString() : null);
			model.setTxnPDSAId(map.get("txnPDSAId") != null ? Integer
					.parseInt(map.get("txnPDSAId").toString()) : null);
			model.setNumeratorValue(map.get("numeratorValue") != null ? Integer
					.parseInt(map.get("numeratorValue").toString()) : null);
			model.setDenominatorValue(map.get("denominatorValue") != null ? Integer
					.parseInt(map.get("denominatorValue").toString()) : null);
			model.setPercentage(map.get("percentage") != null ? Double
					.parseDouble(map.get("percentage").toString()) : null);
			if(model.getDenominatorValue()!=null&&model.getDenominatorValue()==0)
			{
				model.setPercentage(0.0);
			}
			model.setPdsaId(map.get("pdsaId") != null ? Integer.parseInt(map
					.get("pdsaId").toString()) : null);
			txnPDSAModels.add(model);
		}
		if (file != null) {
			txnPDSAModels.get(0).setDocument(file);
		}
		boolean check = pdsaService.saveTXNPDSA(txnPDSAModels);
		ReturnModel returnModel = new ReturnModel();
		if (check) {
			returnModel.setErrorMessage(errorMessageSource.getMessage(
					Constants.Web.TXNPDSA_ADD_SUCCESS, null, null));
			returnModel.setStatusCode(errorMessageSource.getMessage(
					Constants.Web.SUCCESS_STATUS_CODE, null, null));
			returnModel.setStatusMessage(errorMessageSource.getMessage(
					Constants.Web.BOOTSTRAP_ALERT_SUCCESS, null, null));
		} else {
			returnModel.setErrorMessage(errorMessageSource.getMessage(
					Constants.Web.TXNPDSA_ADD_FAILED, null, null));
			returnModel.setStatusCode(errorMessageSource.getMessage(
					Constants.Web.FAILURE_STATUS_CODE, null, null));
			returnModel.setStatusMessage(errorMessageSource.getMessage(
					Constants.Web.BOOTSTRAP_ALERT_DANGER, null, null));
		}

		return returnModel;
	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	@Authorize(feature = "pdsa", permission = "view")
	@RequestMapping("/getClosingPDSAStatus")
	@ResponseBody
	public List<ValueModel> getClosingPDSAStatus() {
		return pdsaService.getClosingStatus();
	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param pdsaId
	 * @param description
	 * @param documents
	 * @param statusId
	 * @return
	 */
	@Authorize(feature = "pdsa", permission = "edit")
	@RequestMapping(value = "/closePDSA", method = RequestMethod.POST)
	@ResponseBody
	public ReturnModel closePDSA(@RequestParam("pdsaId") int pdsaId,
			@RequestParam("description") String description,
			@RequestParam("documents") MultipartFile[] documents,
			@RequestParam("statusId") int statusId) {

		boolean check = pdsaService.closingPDSA(pdsaId, description, documents,
				statusId);
		ReturnModel returnModel = new ReturnModel();
		if (check) {
			returnModel.setErrorMessage(errorMessageSource.getMessage(
					Constants.Web.PDSA_CLOSE_SUCCESS, null, null));
			returnModel.setStatusCode(errorMessageSource.getMessage(
					Constants.Web.SUCCESS_STATUS_CODE, null, null));
			returnModel.setStatusMessage(errorMessageSource.getMessage(
					Constants.Web.BOOTSTRAP_ALERT_SUCCESS, null, null));
		} else {
			returnModel.setErrorMessage(errorMessageSource.getMessage(
					Constants.Web.PDSA_CLOSE_ERROR, null, null));
			returnModel.setStatusCode(errorMessageSource.getMessage(
					Constants.Web.FAILURE_STATUS_CODE, null, null));
			returnModel.setStatusMessage(errorMessageSource.getMessage(
					Constants.Web.BOOTSTRAP_ALERT_DANGER, null, null));
		}

		return returnModel;
	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	@Authorize(feature = "pdsa", permission = "edit")
	@RequestMapping("generatePDSANumberAndDate")
	@ResponseBody
	public Map<String, String> generatePDSANumberAndDate() {
		return pdsaService.generatePDSANumberAndDate();
	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	 @Authorize(feature="pdsaSummary,submissionManagement", permission="view")
	@RequestMapping("getAllPDSAs")
	@ResponseBody
	public Map<String, Object> getAllPDSAs() {
		return pdsaService.getAllFacilityPDSADetails();
	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	@Authorize(feature = "pdsaSummary,pdsa", permission = "view")
	@RequestMapping("/getPDSAFilterOption")
	@ResponseBody
	public Map<String, Object>  getPDSAFilterOption() {
		return pdsaService.getAllFilterOptionForPDSASummaryReport();
	}

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param indicatorId
	 * @param changeIdea
	 * @return
	 */

	@Authorize(feature = "pdsa", permission = "edit")
	@RequestMapping(value = "/saveChangeIdea", method = RequestMethod.POST)
	@ResponseBody
	public ValueModel saveChangeIdea(
			@RequestParam("indicatorId") int indicatorId,
			@RequestParam("changeIdea") String changeIdea) {
		return pdsaService.saveChangeIdea(indicatorId, changeIdea);
	}

	@Authorize(feature = "pdsa", permission = "view")
	@RequestMapping("getBlankPDSAObject")
	@ResponseBody
	public PDSAModel getPDSAModel() {
		PDSAModel pdsaModel = new PDSAModel();
		return pdsaModel;
	}

	/**
	 * This method will be used to download the file
	 * 
	 * @param name
	 * @param response
	 * @throws IOException
	 */
	@Authorize(feature = "pdsaSummary,pdsa", permission = "view")
	@RequestMapping(value = "/downloadPDSADoc", method = RequestMethod.POST)
	public void downLoad(@RequestParam("fileName") String name,
			HttpServletResponse response) throws IOException {

		InputStream inputStream;
		String fileName = "";
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
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
