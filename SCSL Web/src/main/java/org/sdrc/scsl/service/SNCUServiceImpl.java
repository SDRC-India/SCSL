package org.sdrc.scsl.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.Indicator;
import org.sdrc.scsl.domain.IndicatorFacilityTimeperiodMapping;
import org.sdrc.scsl.domain.TXNSNCUData;
import org.sdrc.scsl.domain.TXNSubmissionManagement;
import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.domain.TypeDetail;
import org.sdrc.scsl.model.mobile.TimePeriodModel;
import org.sdrc.scsl.model.web.IndicatorModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.SNCUIndicatorDataModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.ValueObject;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.IndicatorFacilityTimeperiodMappingRepository;
import org.sdrc.scsl.repository.IndicatorRepository;
import org.sdrc.scsl.repository.TXNSNCUDataRepository;
import org.sdrc.scsl.repository.TXNSubmissionManagementRepository;
import org.sdrc.scsl.repository.TimePeriodRepository;
import org.sdrc.scsl.repository.TypeDetailRepository;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @author Mandakini Biswal
 * @author Subrata(subrata@sdrc.co.in)
 * @author Sarita(sarita@sdrc.co.in)
 * @author Harsh pratyush(harsh@sdrc.co.in)
 * This service impl is responsible for all the transaction made in sncu/nicu data entry
 */

@Service
public class SNCUServiceImpl implements SNCUService {

	@Autowired
	private TXNSNCUDataRepository txnSNCUDataRepository;

	@Autowired
	private IndicatorFacilityTimeperiodMappingRepository indicatorFacilityTimeperiodMappingRepository;

	@Autowired
	private IndicatorRepository indicatorRepository;

	@Autowired
	private TypeDetailRepository typeDetailRepository;

	@Autowired
	private TimePeriodRepository timePeriodRepository;
	
	@Autowired
	private TXNSubmissionManagementRepository txnSubmissionManagementRepository;

	@Autowired
	private ResourceBundleMessageSource messages;
	
	@Autowired
	private ResourceBundleMessageSource errorMessageSource;

	@Autowired 
	private StateManager stateManager;
	
	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private AggregationService aggregationService;
	
	private static final Logger LOGGER=Logger.getLogger(SNCUServiceImpl.class);
	
	private SimpleDateFormat monthYearSdf = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.MONTH_SMALL_YEAR_FORMATTER_NO_HYPHEN));
	private SimpleDateFormat simpleDateformater = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_FORMATTER_HYPHEN));
	private DateFormat formatter = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_FORMATTER_HYPHEN));

	/*
	 * collect the List of SNCUIndicatorModel from controller into
	 * saveSNCUIndicator() as parameter
	 */

	/** save the sncu profile entry and data entry data
	 * @author Sarita Panigrahi, created on: 15-Oct-2017
	 * @param sncuIndicatorModel
	 * @param timeperiodId
	 * @return
	 */
	@Override
	@Transactional
	public ReturnModel saveSNCUIndicator(Map<String, List<IndicatorModel>> sncuIndicatorModel, Integer timeperiodId,
			List<Integer> noOfDeliveryIndicators, List<Integer> noOfLiveBirthIndicators, Boolean isLegacy,
			Integer facilityId) {
		ReturnModel model = new ReturnModel();
		try {
			
			//server side check, if the user will edit the disable fields by inspect in html 
			if(!sncuIndicatorModel.get(messages.getMessage(Constants.Web.INDICATOR_TYPE_INTERMEDIATE_NAME, null,null)).get(0).getIsEnable()){
				model.setErrorMessage(errorMessageSource.getMessage(Constants.Web.SUBMISSION_INVALID_ERROR_MESSAGE, null, null));
				model.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
				return model;
			}
			
			List<IndicatorModel> listOfIndicatorModel = new ArrayList<IndicatorModel>();
			for (Map.Entry<String, List<IndicatorModel>> mapOfModel : sncuIndicatorModel
					.entrySet()) {
				listOfIndicatorModel.addAll(mapOfModel.getValue());
			}
			UserModel userModel = ((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL));
			
			TXNSubmissionManagement txnSubmissionManagement = setTXNSubmissionManagement(
					facilityId, timeperiodId, userModel.getUsername(), isLegacy);

			/** Iterate the List to get one by one elements from List */
			List<TXNSNCUData> txnsncuDatas = new ArrayList<>();
			
			Integer noOfDelivery = null;
			Integer noOfLiveBirth = null;
			
			//iterate the loop before to get no of live birth and no of delivery value
			for (IndicatorModel modelData : listOfIndicatorModel) {
				
				//exclude profile entry indicator
				if(noOfDeliveryIndicators.contains(modelData.getIndicatorId()) && modelData.getDenominatorValue()!= null && null==modelData.getIsProfile())
					noOfDelivery = modelData.getDenominatorValue();
				
				else if(noOfLiveBirthIndicators.contains(modelData.getIndicatorId()) && modelData.getDenominatorValue()!= null && null==modelData.getIsProfile())
					noOfLiveBirth = modelData.getDenominatorValue();
				
				//once we got, break the loop
				if(null!=noOfDelivery && null!=noOfLiveBirth)
					break;
			}
			
			for (IndicatorModel modelData : listOfIndicatorModel) {
				// Create TXNSNCUData class object
				TXNSNCUData txnSNCUData = new TXNSNCUData();
				// Get createdDate from Model class and set into createdDate of
				// TXNSNCUData class
				txnSNCUData.setCreatedDate(new Timestamp(new Date().getTime()));
				// Get numeratorValue from Model class and set into numeratorValue
				// of TXNSNCUData class
				txnSNCUData.setNumeratorValue(modelData.getNumeratorValue());
				// Get denominatorValue from Model class and set into
				// denominatorValue of TXNSNCUData class
				txnSNCUData.setDenominatorValue(modelData.getDenominatorValue());
				// Get percentageValue from Model class and set into percentage of
				// TXNSNCUData class
				txnSNCUData.setPercentage(modelData.getPercentage());
				txnSNCUData.setIsLive(true);
				txnSNCUData
						.setIndicatorFacilityTimeperiodMapping(indicatorFacilityTimeperiodMappingRepository.findByIndFacilityTpId(
								modelData.getIndicatorFacilityTimeperiodId()));
				txnSNCUData.setTxnSubmissionManagement(txnSubmissionManagement);
				
				
//				check for if Numerator is greater than denominator
				if((modelData.getNumeratorValue()!= null && modelData.getDenominatorValue()!=null)&&
						modelData.getNumeratorValue() > modelData.getDenominatorValue())
					txnSNCUData.setDescription(new TypeDetail(Integer.parseInt(messages.getMessage(Constants.Web.WARNING_DESCRIPTION_MESSAGE_FIRST, null,null))));
				
				//check for Number of live births is greater than Number of deliveries
				else if(noOfLiveBirthIndicators.contains(modelData.getIndicatorId()) && null!= noOfDelivery && null!= noOfLiveBirth &&
						((null!= modelData.getDenominatorValue() && null==modelData.getIsProfile() ) ||
						null!=modelData.getIsProfile()) && noOfLiveBirth > noOfDelivery)
					txnSNCUData.setDescription(new TypeDetail(Integer.parseInt(messages.getMessage(Constants.Web.WARNING_DESCRIPTION_MESSAGE_SECOND, null,null))));
				
//				/check for Number of deliveries is less than Number of live births
				else if(noOfDeliveryIndicators.contains(modelData.getIndicatorId()) && null!= noOfDelivery && null!= noOfLiveBirth &&
						((null!= modelData.getDenominatorValue() && null==modelData.getIsProfile() ) ||
								null!=modelData.getIsProfile()) &&  noOfDelivery < noOfLiveBirth)
					txnSNCUData.setDescription(new TypeDetail(Integer.parseInt(messages.getMessage(Constants.Web.WARNING_DESCRIPTION_MESSAGE_THIRD, null,null))));
				
				txnsncuDatas.add(txnSNCUData);
			}
			txnSNCUDataRepository.save(txnsncuDatas);
			
			//in case of legacy web data entry aggregate data
			if(isLegacy){
				// once all the SNCU TXN data saved call aggregation
				aggregationService.aggregateAfterHistoricalDataUpload(Arrays.asList(timeperiodId), facilityId, Arrays.asList(timePeriodRepository.findByTimePeriodId(timeperiodId)));
			}
			model.setStatusCode(errorMessageSource.getMessage(Constants.Web.SUCCESS_STATUS_CODE, null, null));
			model.setStatusMessage(errorMessageSource.getMessage(Constants.Web.SNCU_SUBMISSION_SUCCESS, null, null));

			return model;
			
		} catch (Exception e) {
			LOGGER.info("EXCEPTION OCCURED IN saveSNCUIndicator METHOD FOR TIME PERIOD "+timeperiodId);
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			model.setErrorMessage(errorMessageSource.getMessage(Constants.Web.SUBMISSION_ERROR_MESSAGE, null, null));
			model.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
			return model;
		} 
		
	}

	/**
	 * @author Subrata(Subrata@sdrc.co.in) created on 26-04-2017 getting
	 *         indicators name, numerator , denominator,core area for process,
	 *         intermediate, outcome
	 */
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.SNCUService#getSNCUIndicator(java.lang.Integer)
	 * @author Sarita Panigrahi
	 * get all indicators to the view page
	 */
	@Override
	public SNCUIndicatorDataModel getSNCUIndicator(Integer timePeriod, Integer facilityId, Boolean hasLr) {

		UserModel userModel = ((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL));

		//new code added if data entry is for legacy timeperiod
		facilityId = facilityId!= null ? facilityId : userModel.getFacilityId();
		hasLr = hasLr!=null ? hasLr : userModel.isHasLr();
		Map<String, Map<String, List<IndicatorModel>>> mapOfIndicators = new LinkedHashMap<>();
		SNCUIndicatorDataModel sncuIndicatorDataModel=new SNCUIndicatorDataModel();
		
//		Integer dateMaxCheckMnE = Integer.valueOf(messages.getMessage
//				(Constants.Web.MNE_APPROVAL_LASTDATE, null,null)); //date is 25 max from message.properties file
		
		Integer dateMaxCheckMnE = java.time.LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); //max date is last day of month 
		Integer dateAfter15Check = Integer.valueOf(messages.getMessage
				(Constants.Web.AFTER_DATE_15, null, null)); //date is 16 
		Integer dateMaxSuperintendent = Integer.valueOf(messages.getMessage
				(Constants.Web.SUPERINTENDENT_APPROVAL_LASTDATE, null,null)); //max date is 20 for superintendent
		Integer dateMaxSuperintendentCheck = Integer.valueOf(messages.getMessage
				(Constants.Web.LAST_DATE_SUP, null,null)); //max date is 20 for superintendent
		LocalDateTime currentDate = LocalDateTime.now();
		
		Date date = new Date(); 
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int year = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH)+1;
	    String afterConvertDateMaxCheck = year+"-"+(month<10?"0"+month:month)+"-"+dateMaxCheckMnE+" 00:00:000";
	    String afterConvertDateAfter15Check = year+"-"+(month<10?"0"+month:month)+"-"+dateAfter15Check+" 00:00:000";
	    String afterConvertDateAfter20Check = year+"-"+(month<10?"0"+month:month)+"-"+dateMaxSuperintendent+" 00:00:000";
	    String afterConvertDateOn20Check = year+"-"+(month<10?"0"+month:month)+"-"+dateMaxSuperintendentCheck+" 00:00:000";
	    Boolean isEnable = null;
	    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssS");
	    
	    // Converted 16 and last day of month to LocalDateTime type
	    LocalDateTime convertedDateMaxCheck = LocalDateTime.parse(afterConvertDateMaxCheck, dateTimeFormatter); 
	    LocalDateTime convertDateAfter15Check = LocalDateTime.parse(afterConvertDateAfter15Check, dateTimeFormatter);
	    LocalDateTime convertDateAfter20Check = LocalDateTime.parse(afterConvertDateAfter20Check, dateTimeFormatter);
	    LocalDateTime convertDateOn20Check = LocalDateTime.parse(afterConvertDateOn20Check, dateTimeFormatter);
	    
		TXNSubmissionManagement txnManagement = txnSubmissionManagementRepository.findByStatus(facilityId, timePeriod);
		List<Object[]> listOfIndicators = new ArrayList<>();
		
		//if txnManagement is null then only get all sncu data
		if(null!= txnManagement){
			//we will take the RefSubmissionId if it is present else TxnSubmissionId
			Integer txnSubmissionId =  null!=txnManagement.getRefSubmissionId()?
					txnManagement.getRefSubmissionId() : txnManagement.getTxnSubmissionId() ;
					
			listOfIndicators = indicatorRepository.fetchIndicatorsName(facilityId, timePeriod, txnSubmissionId);
		}
		
//		checking if date is in between 1 to last day of month(including last day)
		if (currentDate.isEqual(convertedDateMaxCheck) || currentDate.isBefore(convertedDateMaxCheck)) {

			// checking if date is in between 16 to 25 and data not submitted
			if (((currentDate.isEqual(convertDateAfter15Check) || currentDate.isAfter(convertDateAfter15Check))
					&& (currentDate.isEqual(convertedDateMaxCheck) || currentDate.isBefore(convertedDateMaxCheck)))
					&& listOfIndicators.isEmpty()) {

				isEnable = false;
				mapOfIndicators = getBlankForm(facilityId, timePeriod, isEnable, hasLr); // showing  blank form and isEnable = false
				sncuIndicatorDataModel.setStatusMessage(
						messages.getMessage(Constants.Web.LAST_DATE_SUBMISSION_CROSSED_MESSAGE, null, null));
				sncuIndicatorDataModel
						.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_PENDING_CSS, null, null));

				// if current date is between 1 to 15 and data is not submitted
			} else if (currentDate.isBefore(convertDateAfter15Check) && listOfIndicators.isEmpty()){
				isEnable = true;
				mapOfIndicators = getBlankForm(facilityId, timePeriod, isEnable, hasLr); // showing blank form and isEnable true
			}
			// if date is 1 to 20 and rejected by superintendent (then isEnable = true, showing submitted data)
			if (currentDate.isBefore(convertDateAfter20Check)
					&& null != txnManagement && txnManagement.getStatusSuperintendent().getTypeDetail()
							.equals(messages.getMessage(Constants.Web.RECORD_TYPE, null, null))) {
				isEnable = true;
				mapOfIndicators = getData(listOfIndicators, isEnable, facilityId, timePeriod, hasLr);
			}
			// if date is after 20 and txnManagement created date is after 20
			//and rejected by superintendent that means mne must have rejected (then isEnable = true, showing submitted data)
			else if ((currentDate.isAfter(convertDateOn20Check)) && null != txnManagement
					&& txnManagement.getStatusSuperintendent().getTypeDetail()
							.equals(messages.getMessage(Constants.Web.RECORD_TYPE, null, null))
							&& txnManagement.getCreatedDate().after(Timestamp.valueOf(convertDateAfter20Check))) {
				isEnable = true;
				mapOfIndicators = getData(listOfIndicators, isEnable, facilityId, timePeriod, hasLr);

			}
			// if date is after 20 and rejected by superintendent (then isEnable = false, showing submitted data)
			else if ((currentDate.isAfter(convertDateOn20Check)) && null != txnManagement
					&& txnManagement.getStatusSuperintendent().getTypeDetail()
					.equals(messages.getMessage(Constants.Web.RECORD_TYPE, null, null))) {
				
				isEnable = false;
				mapOfIndicators = getData(listOfIndicators, isEnable, facilityId, timePeriod, hasLr);
			
			}
			// if date is 1 to 25 and rejected by mne (then isEnable = true, showing submitted data)
			else if((currentDate.isBefore(convertedDateMaxCheck)) && null != txnManagement
					&& txnManagement.getStatusMne()!=null && txnManagement.getStatusMne().getTypeDetail()
							.equals(messages.getMessage(Constants.Web.RECORD_TYPE, null, null))){
				
				isEnable = true;
				mapOfIndicators = getData(listOfIndicators, isEnable, facilityId, timePeriod, hasLr);
			}
			else if (null != txnManagement && !txnManagement.getStatusSuperintendent().getTypeDetail()
					.equals(messages.getMessage(Constants.Web.RECORD_TYPE, null, null))) {
				// if date is 1 to 25 and not rejected (then isEnable = false,
				// showing submitted data)
				if (txnManagement.getStatusMne() != null && txnManagement.getStatusMne().getTypeDetail()
						.equals(messages.getMessage(Constants.Web.RECORD_TYPE, null, null))) {
					isEnable = true;
					mapOfIndicators = getData(listOfIndicators, isEnable, facilityId, timePeriod, hasLr);
				} else {
					isEnable = false;
					mapOfIndicators = getData(listOfIndicators, isEnable, facilityId, timePeriod, hasLr);
				}
			}

			// after 25th
		} else if(currentDate.isAfter(convertedDateMaxCheck)){ // after 25th
			if(!listOfIndicators.isEmpty()){
				isEnable = false;
				mapOfIndicators = getData(listOfIndicators, isEnable, facilityId, timePeriod, hasLr); 
			} else{
				isEnable = false;
				mapOfIndicators = getBlankForm(facilityId, timePeriod, isEnable, hasLr); 
				sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.LAST_DATE_SUBMISSION_CROSSED_MESSAGE, null,null));
				sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_PENDING_CSS, null,null));
			}
		} 
		sncuIndicatorDataModel.setIndTypeIndicatorModelMap(mapOfIndicators);

	if(txnManagement!=null)
	{
		if(txnManagement.getStatusSuperintendent()!=null)
		{
			sncuIndicatorDataModel.setSupritendentStatus(txnManagement.getStatusSuperintendent().getTypeDetail());
		}
		if(txnManagement.getStatusMne()!=null)
		{
			sncuIndicatorDataModel.setMneStatus(txnManagement.getStatusMne().getTypeDetail());
		}
		
		if(txnManagement.getStatusMne()!=null)
		{
			if(txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED,null,null)))
			{
				sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_BY_MnE,null,null )
						+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
				sncuIndicatorDataModel.setRemark(txnManagement.getRemarkByMnE());
				sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
			}
			else if (txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING,null,null))
					&&txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED,null,null)))
			{
				sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_BY_SUPRITENDENT,null,null )
						+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
				sncuIndicatorDataModel.setRemark(txnManagement.getRemarkBySuperintendent());
				sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
			}
			
			else if(txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_REJECTED,null,null)))
			{
				sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_REJECTED_BY_MnE,null,null )
						+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
				sncuIndicatorDataModel.setRemark(txnManagement.getRemarkByMnE());
				sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_REJECTED_CSS, null,null));
			}
			
			else if(txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING,null,null))
					&&txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_AUTO_APPROVED,null,null)))
			{
				sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.STATUS_AUTO_APPROVED_SUPERITENDENT,null,null )
						+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
				sncuIndicatorDataModel.setRemark("");
				sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
				
				//legacy data upload
			}else if(txnManagement.getStatusMne().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_LEGACY_STATUS,null,null))){
				sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.STATUS_LEGACY_MNE,null,null )
						+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
				sncuIndicatorDataModel.setRemark("");
				sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
			}
		}
		else
		{
			if(txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_REJECTED,null,null)))
			{
				sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_REJECTED_BY_SUPRITENDENT,null,null )
						+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
				sncuIndicatorDataModel.setRemark(txnManagement.getRemarkBySuperintendent());
				sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_REJECTED_CSS, null,null));
			}
			else if(txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING,null,null)))
			{
				sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_PENDING,null,null ) 
						+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
				sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_PENDING_CSS, null,null));
			}
			else if (txnManagement.getStatusSuperintendent().getTypeDetailId()==Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED,null,null)))
			{
				sncuIndicatorDataModel.setStatusMessage(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_BY_SUPRITENDENT,null,null )
						+ " on "+ simpleDateformater.format(txnManagement.getCreatedDate()));
				sncuIndicatorDataModel.setRemark(txnManagement.getRemarkBySuperintendent());
				sncuIndicatorDataModel.setCssClass(messages.getMessage(Constants.Web.SUBMISSION_APPROVED_CSS, null,null));
			}
		}
	}
		if (mapOfIndicators.size() > 0) {
			// sncuIndicatorDataModel.setEnabled(mapOfIndicators.entrySet().iterator().next().getValue().entrySet().iterator().next().getValue().get(0).getIsEnable());
			sncuIndicatorDataModel.setEnabled(isEnable);
			if (!sncuIndicatorDataModel.getIndTypeIndicatorModelMap().get("dataEntry")
					.containsKey(messages.getMessage(Constants.Web.INDICATOR_TYPE_PROCESS_NAME, null, null))) {
				sncuIndicatorDataModel.getIndTypeIndicatorModelMap().get("dataEntry").put(
						messages.getMessage(Constants.Web.INDICATOR_TYPE_PROCESS_NAME, null, null),
						new ArrayList<IndicatorModel>());
			}
		}
	return sncuIndicatorDataModel;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.SNCUService#getData(java.util.List, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.Boolean)
	 * get pre fetched data entry from
	 */
	@Override
	public Map<String, Map<String, List<IndicatorModel>>> getData(List<Object[]> listOfIndicators, Boolean isEnable,
			Integer facilityId, Integer timePeriod, Boolean hasLr) {
		
		Map<String, List<IndicatorModel>> mapOfIndicators =  new LinkedHashMap<>();
		Map<String, Map<String, List<IndicatorModel>>> indTypeMapOfIndicators = new LinkedHashMap<>();
		List<IndicatorModel> listOfIndicatorModel = null;
		Map<String, List<IndicatorModel>>  newMapOfIndicators = new LinkedHashMap<>();
		
		for (Object[] object : listOfIndicators) {
			
			TypeDetail indType = ((Indicator) object[0]).getIndicatorType();
			
			if(null!= indType){ //data entry indicators 
				if (!mapOfIndicators.containsKey(indType.getTypeDetail())) {
					listOfIndicatorModel = new ArrayList<>();
					
					listOfIndicatorModel.add(setIndicatorModel(object,isEnable, hasLr)); // setting IndicatorModel(by setValue method) 
					mapOfIndicators.put(((Indicator) object[0]).getIndicatorType().getTypeDetail(), listOfIndicatorModel);
					
				} else {
					List<IndicatorModel> getIndicatorsList = mapOfIndicators.get(((Indicator) object[0]).getIndicatorType().getTypeDetail());
					
					getIndicatorsList.add(setIndicatorModel(object,isEnable, hasLr)); // setting IndicatorModel(by setValue method) 
				}
				
				indTypeMapOfIndicators.put("dataEntry", mapOfIndicators);
			}else{ //profile entry indicators
				
				listOfIndicatorModel = new ArrayList<>();
				
				listOfIndicatorModel.add(setIndicatorModel(object,isEnable, hasLr)); // setting IndicatorModel(by setValue method) 
				newMapOfIndicators.put(((Indicator) object[0]).getIndicatorOrder().toString(), listOfIndicatorModel);
				
				indTypeMapOfIndicators.put("profileEntry", newMapOfIndicators);
			}
			
		}
		
		
		//set the mappings those have been added after submission
		listOfIndicators = indicatorRepository.fetchIndicatorsNameNotInTxnSncu(facilityId, timePeriod);
		
		for (Object[] object : listOfIndicators) {
			
			Indicator indicator = (Indicator) object[0];
			
			if(null!=indicator.getIndicatorType()){
				if (!mapOfIndicators.containsKey(indicator.getIndicatorType().getTypeDetail())) {
					
					listOfIndicatorModel = new ArrayList<>();
					listOfIndicatorModel.add(setValueForBlankForm(object, isEnable, hasLr));
					
					mapOfIndicators.put((indicator).getIndicatorType().getTypeDetail(), listOfIndicatorModel);
					
				} else {
					
					List<IndicatorModel> getIndicatorsList = mapOfIndicators.get((indicator).getIndicatorType().getTypeDetail());
					getIndicatorsList.add(setValueForBlankForm(object, isEnable, hasLr));
				}
			}
			
			
		}
		
		//sort all process indicators
		
		if(null!=indTypeMapOfIndicators.get("dataEntry").get(messages.getMessage(Constants.Web.INDICATOR_TYPE_PROCESS_NAME, null,null)))
			indTypeMapOfIndicators.get("dataEntry").get(messages.getMessage(Constants.Web.INDICATOR_TYPE_PROCESS_NAME, null,null))
				.sort(Comparator.comparingInt(IndicatorModel :: getIndicatorOrder));
		
		return indTypeMapOfIndicators;
	}

	/**
	 * @param object
	 * @param isEnable
	 * @param hasLr
	 * @return
	 */
	private IndicatorModel setIndicatorModel(Object[] object, Boolean isEnable, 
			Boolean hasLr) {
		IndicatorModel indicatorModel = new IndicatorModel();
		TXNSNCUData txnSncuData = (TXNSNCUData) object[2];
		Indicator indicator = (Indicator) object[0];
		indicatorModel.setIndicatorName(indicator.getIndicatorName());
		indicatorModel.setNumeratorName(indicator.getNumerator());
		indicatorModel.setNumeratorValue(txnSncuData.getNumeratorValue());
		indicatorModel.setDenominatorName(indicator.getDenominator());
		indicatorModel.setDenominatorValue(txnSncuData.getDenominatorValue());
		indicatorModel.setIndicatorId(indicator.getIndicatorId());
		indicatorModel.setCoreAreaName(indicator.getCoreArea() == null ? null : indicator.getCoreArea().getTypeDetail());
		indicatorModel.setCoreAreaId(indicator.getCoreArea() == null ? null : indicator.getCoreArea().getTypeDetailId());
		indicatorModel.setIsRequired(indicator.getIsReqired() == null ? null : indicator.getIsReqired());
		indicatorModel.setExceptionRule(indicator.getExceptionRule() == null ? null : getExceptionRule(indicator.getExceptionRule()));
//		if both the num and deno is not null i.e 0 but percentage is null then set nan
		indicatorModel.setPercentage(txnSncuData.getNumeratorValue() != null && txnSncuData.getNumeratorValue() == 0
				&& txnSncuData.getDenominatorValue() != null && txnSncuData.getDenominatorValue() == 0 ? Double.valueOf(Double.NaN)
						: txnSncuData.getPercentage());
		indicatorModel.setIndicatorFacilityTimeperiodId(((IndicatorFacilityTimeperiodMapping) object[1]).getIndFacilityTpId());
		indicatorModel.setIsEnable(isEnable);
		indicatorModel.setIsProfile(indicator.getIsProfile());
		indicatorModel.setIsLr(null==indicator.getIsLr() ? true :
			indicator.getIsLr() && hasLr ? true : false); //if islr is false, disable that intermediate indicator
		indicatorModel.setIndicatorOrder(indicator.getIndicatorOrder());
		indicatorModel.setCssClass(null!= txnSncuData.getDescription() ? "warned" : "");
		
		//set css class in denominator if any warning exists
		indicatorModel.setDenominatorCss(null!= txnSncuData.getDescription() ? "warned" : "");
		
		//set css class in numerator if numerator value is not blank
		indicatorModel.setNumeratorCss(null!= txnSncuData.getDescription() && txnSncuData.getNumeratorValue()!= null ? "warned" : "");
		return indicatorModel;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.SNCUService#getBlankForm(java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.lang.Boolean)
	 * get blank data entry form
	 */
	@Override
	public Map<String, Map<String, List<IndicatorModel>>> getBlankForm(Integer facilityId, Integer timePeriod, Boolean isEnable, Boolean hasLr) {
		
		Map<String, Map<String, List<IndicatorModel>>> indTypeMapOfIndicators = new LinkedHashMap<>();
		Map<String, List<IndicatorModel>> mapOfIndicators = new LinkedHashMap<>();
		List<Object[]> listOfIndicators = null;
		List<IndicatorModel> listOfIndicatorModel = null;
		
		listOfIndicators = indicatorRepository.getIndicatorFacilityTimeperiodMappingByFacilityIdAndTimePeriodIdOrderByIndicatorOrder(facilityId, timePeriod);
		Map<String, List<IndicatorModel>>  newMapOfIndicators = new LinkedHashMap<>();
		
		for (Object[] object : listOfIndicators) {
			
			TypeDetail typeDetail = ((Indicator) object[0]).getIndicatorType();
			
			if(null!=typeDetail){ //data entry
				if (!mapOfIndicators.containsKey(typeDetail.getTypeDetail())) {
					
					listOfIndicatorModel = new ArrayList<>();
					listOfIndicatorModel.add(setValueForBlankForm(object, isEnable, hasLr));
					
					mapOfIndicators.put(typeDetail.getTypeDetail(), listOfIndicatorModel);
					
				} else {
					
					List<IndicatorModel> getIndicatorsList = mapOfIndicators.get(typeDetail.getTypeDetail());
					getIndicatorsList.add(setValueForBlankForm(object, isEnable, hasLr));
				}
				
				indTypeMapOfIndicators.put("dataEntry", mapOfIndicators);
			}else{ //profile entry
				listOfIndicatorModel = new ArrayList<>();
				listOfIndicatorModel.add(setValueForBlankForm(object, isEnable, hasLr));
				
				newMapOfIndicators.put( ((Indicator) object[0]).getIndicatorOrder().toString(), listOfIndicatorModel);
				
				indTypeMapOfIndicators.put("profileEntry", newMapOfIndicators);
			}
			
			
		}
		return indTypeMapOfIndicators;
	}

	/**
	 * @param object
	 * @param isEnable
	 * @param hasLr
	 * @return
	 */
	private IndicatorModel setValueForBlankForm(Object[] object, Boolean isEnable, Boolean hasLr) {
		IndicatorModel indicatorModel = new IndicatorModel();
		indicatorModel.setIndicatorName(((Indicator) object[0]).getIndicatorName());
		indicatorModel.setNumeratorName(((Indicator) object[0]).getNumerator());
		indicatorModel.setNumeratorValue(null);
		indicatorModel.setDenominatorName(((Indicator) object[0]).getDenominator());
		indicatorModel.setDenominatorValue(null);
		indicatorModel.setIndicatorId(((Indicator) object[0]).getIndicatorId());
		indicatorModel.setCoreAreaName(((Indicator) object[0]).getCoreArea() == null ? null : ((Indicator) object[0]).getCoreArea().getTypeDetail());
		indicatorModel.setCoreAreaId(((Indicator) object[0]).getCoreArea() == null ? null : ((Indicator) object[0]).getCoreArea().getTypeDetailId());
		indicatorModel.setIsRequired(((Indicator) object[0]).getIsReqired() == null ? null : ((Indicator) object[0]).getIsReqired());
		indicatorModel.setExceptionRule(((Indicator) object[0]).getExceptionRule() == null ? null : getExceptionRule(((Indicator) object[0]).getExceptionRule()));
		indicatorModel.setPercentage(null);
		indicatorModel.setIndicatorFacilityTimeperiodId(((IndicatorFacilityTimeperiodMapping) object[1]).getIndFacilityTpId());
		indicatorModel.setIsEnable(isEnable);
		indicatorModel.setIsProfile(((Indicator) object[0]).getIsProfile());
		indicatorModel.setIsLr(null==((Indicator) object[0]).getIsLr() ? true :
			((Indicator) object[0]).getIsLr() && hasLr ? true : false); //if the area has labor room and indicator has dependency then islr true//disable and put n/a
		indicatorModel.setIndicatorOrder(((Indicator) object[0]).getIndicatorOrder());
		return indicatorModel;
	}

	/**
	 * @author Subrata(Subrata@sdrc.co.in) created on 26-04-2017 converting
	 *         exception rule "id" to their respective "name" and putting in
	 *         ArrayList
	 */
	private List<String> getExceptionRule(String exceptionRule) {
		List<String> exceptionNames = new ArrayList<>();
		List<TypeDetail> getAllTypeDetails = typeDetailRepository.findAll();
		Map<Integer, String> typeDetails = new HashMap<>();
		for (TypeDetail typeDetail : getAllTypeDetails) {
			typeDetails.put(typeDetail.getTypeDetailId(),
					typeDetail.getTypeDetail());
		}
		String[] exceptionRules = exceptionRule.split(",");
		for (String combinedRule : exceptionRules) {
			String ruleStr = "";
			for (String eachRule : combinedRule.split("-")) {
				ruleStr= !ruleStr.equals("") ? ruleStr+"#"+typeDetails.get(Integer.valueOf(eachRule)) : typeDetails.get(Integer.valueOf(eachRule));
			}
			exceptionNames.add(ruleStr);
		}
		return exceptionNames;
	}

	/**
	 * @author Subrata(Subrata@sdrc.co.in) created on 26-04-2017 getting rest of
	 *         indicators from DB which are not available for a facility.
	 */
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.SNCUService#getRestIndicators(java.lang.Integer)
	 * @author Sarita Panigrahi updated on 05-08-2017
	 */
	@Override
	public Map<String, List<ValueObject>> getRestIndicators(Integer timerperiodId, Integer facilityId, Boolean hasLr) {
		UserModel userModel = ((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL));
		List<Indicator> restOfIndicators = null;
		//if lr is present in that facility then get all rest process indicator
		//if lr is not present then, get independent process indicators to labor room 
		
		hasLr = hasLr!=null ? hasLr : userModel.isHasLr();
		facilityId = facilityId != null? facilityId : userModel.getFacilityId();
		restOfIndicators = hasLr ?
				indicatorRepository.fetchIndicatorsNameByFacilityIdAndTimePeriodId(facilityId, timerperiodId,
						Integer.parseInt(messages.getMessage(
								Constants.Web.INDICATOR_TYPE_PROCESS, null, null))) :
					indicatorRepository.fetchIndicatorsNameByFacilityIdAndTimePeriodIdAndIsLrIsNull(facilityId, timerperiodId,
							Integer.parseInt(messages.getMessage(
									Constants.Web.INDICATOR_TYPE_PROCESS, null, null))); 
		Map<String, List<ValueObject>> restOfIndicatorsMap = new LinkedHashMap<>();
		List<ValueObject> valueObjects = null;
		for (Indicator indicator : restOfIndicators) {
			if (!restOfIndicatorsMap.containsKey(indicator.getCoreArea().getTypeDetail())) {
				valueObjects = new ArrayList<>();
				ValueObject valueObject = new ValueObject();
				valueObject.setKey(indicator.getIndicatorId().toString());
				valueObject.setValue(indicator.getIndicatorName());
				valueObject.setDescription(indicator.getCoreArea().getTypeDetail());
				valueObjects.add(valueObject);
				restOfIndicatorsMap.put(indicator.getCoreArea().getTypeDetail(), valueObjects);
			} else {
				List<ValueObject> valueObjeList = restOfIndicatorsMap.get(indicator.getCoreArea().getTypeDetail());
				ValueObject valueObject = new ValueObject();
				valueObject.setKey(indicator.getIndicatorId().toString());
				valueObject.setValue(indicator.getIndicatorName());
				valueObject.setDescription(indicator.getCoreArea().getTypeDetail());
				valueObjeList.add(valueObject);
			}
		}
		return restOfIndicatorsMap;
	}

	/**
	 * @author Subrata(Subrata@sdrc.co.in) created on 27-04-2017 saving single
	 *         or multiple indicators in DB
	 */
	@Override
	@Transactional
	public void updateIndicatorNames(List<ValueObject> sncuIndicatorModels, Integer timerperiodId) {
		
		try {
			UserModel userModel = ((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL));
			List<IndicatorFacilityTimeperiodMapping> listOfIndicatorFacilityTimperiod = new ArrayList<IndicatorFacilityTimeperiodMapping>();
			for (ValueObject valueObject : sncuIndicatorModels) {

				IndicatorFacilityTimeperiodMapping indFacilityTimeperiodMapping = new IndicatorFacilityTimeperiodMapping();
				indFacilityTimeperiodMapping.setCreatedDate(new Timestamp(new Date().getTime()));
				indFacilityTimeperiodMapping.setFacility(new Area(userModel.getFacilityId())); 
				indFacilityTimeperiodMapping.setIndicator(new Indicator(Integer.valueOf(valueObject.getKey())));
				indFacilityTimeperiodMapping.setTimePeriod(new TimePeriod(timerperiodId));
				indFacilityTimeperiodMapping.setIsLive(true);

				listOfIndicatorFacilityTimperiod.add(indFacilityTimeperiodMapping);
			}
			indicatorFacilityTimeperiodMappingRepository.save(listOfIndicatorFacilityTimperiod);
		} catch (Exception e) {
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}

	/**
	 * @author Subrata(Subrata@sdrc.co.in) created on 27-04-2017
	 * 	//save the sncu profile entry and data entry data
	 *
	 */
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.SNCUService#updateIndicatorOfFacility(java.util.Map, java.lang.Integer)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Transactional
	public ReturnModel updateIndicatorOfFacility(Map<String, List<IndicatorModel>> sncuIndicatorModel, Integer timePeriod) {
		UserModel userModel = ((UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL));
		ReturnModel model = new ReturnModel();
		
		List<Integer> noOfDeliveryIndicators = Arrays.asList(2,3,7,25,39); //only denominator and profile entry
		List<Integer> noOfLiveBirthIndicators = Arrays.asList(23,24,43); //only denominator and profile entry
		
		TXNSubmissionManagement existingTxnSubmissionManagement = txnSubmissionManagementRepository
				.findByFacilityAreaIdAndTimePeriodTimePeriodIdAndIsLatestTrue(userModel.getFacilityId(), timePeriod);

		
		TXNSubmissionManagement latestDeoTxnSubmission = txnSubmissionManagementRepository
				.findByIsLatestTrueAndFacilityAreaIdAndStatusSuperintendentTypeDetailIdAndTimePeriodTimePeriodId(userModel.getFacilityId(),
						Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING,null,null)), timePeriod);
		
		//(this scenario may occur while entering simultaneous data in app and web)
		if(null!=latestDeoTxnSubmission){
			model.setErrorMessage(errorMessageSource.getMessage(Constants.Web.SNCU_SUBMISSION_OLD_MESSAGE, null, null));
			model.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
			return model;
		}
		
		if (existingTxnSubmissionManagement == null) {
			return saveSNCUIndicator(sncuIndicatorModel, timePeriod, noOfDeliveryIndicators, noOfLiveBirthIndicators, false, userModel.getFacilityId());
		} else {
			try {
				existingTxnSubmissionManagement.setIsLatest(false);
				txnSubmissionManagementRepository.save(existingTxnSubmissionManagement);

				// updating txnSubmissionManagement
				TXNSubmissionManagement txnSubmissionManagement = setTXNSubmissionManagement(userModel.getFacilityId(), timePeriod, userModel.getUsername(), false);
				setSNCUIndicatorModel(txnSubmissionManagement.getTxnSubmissionId(),
						existingTxnSubmissionManagement.getRefSubmissionId(), sncuIndicatorModel, noOfDeliveryIndicators, noOfLiveBirthIndicators);
				model.setStatusCode(errorMessageSource.getMessage(Constants.Web.SUCCESS_STATUS_CODE, null, null));
				model.setStatusMessage(errorMessageSource.getMessage(Constants.Web.SNCU_SUBMISSION_SUCCESS, null, null));

				return model;
			} catch (Exception e) {
				LOGGER.error("message",e);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				model.setErrorMessage(errorMessageSource.getMessage(Constants.Web.SUBMISSION_ERROR_MESSAGE, null, null));
				model.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
				return model;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.SNCUService#setTXNSubmissionManagement(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Boolean)
	 * set TXNSubmissionManagement
	 */
	@Override
	public TXNSubmissionManagement setTXNSubmissionManagement(Integer facilityId, Integer timePeriod, String createdBy, Boolean isLegacy) {
		
		try {
			TXNSubmissionManagement txnSubmissionManagement = new TXNSubmissionManagement();
			txnSubmissionManagement.setCreatedDate(new Timestamp(new Date().getTime()));
			txnSubmissionManagement.setCreatedBy(createdBy);
			txnSubmissionManagement.setRemarkBySuperintendent(null);
			txnSubmissionManagement.setRemarkByMnE(null);
			txnSubmissionManagement.setIsLatest(true);
			txnSubmissionManagement.setRefSubmissionId(null);
			txnSubmissionManagement.setIsWeb(true);
			
			/*SET LEGACY STATUS 51 IN CASE OF LEGACY SNCU DATA ENTRY*/
			txnSubmissionManagement.setStatusSuperintendent(isLegacy
					? new TypeDetail(
							Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_LEGACY_STATUS, null, null)))
					: new TypeDetail(Integer
							.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING, null, null)))); // 19
																													// for
																													// pending
			txnSubmissionManagement.setStatusMne(isLegacy
					? new TypeDetail(
							Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_LEGACY_STATUS, null, null)))
					: null);
			txnSubmissionManagement.setFacility(new Area(facilityId));
			txnSubmissionManagement.setTimePeriod(new TimePeriod(timePeriod));
			txnSubmissionManagement = txnSubmissionManagementRepository.save(txnSubmissionManagement);
			return txnSubmissionManagement;
		} catch (Exception e) {
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		}
		
		
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.SNCUService#setSNCUIndicatorModel(java.lang.Integer, java.lang.Integer, java.util.Map, java.util.List, java.util.List)
	 */
	@Override
	@Transactional
	public void setSNCUIndicatorModel(Integer txnSubmissionId,	Integer refTxnSubmissionId,	
			Map<String, List<IndicatorModel>> sncuIndicatorModel, List<Integer> noOfDeliveryIndicators, List<Integer> noOfLiveBirthIndicators) {

		try {
			// setting isLive false in TXNSNCUData for previous data using previous
			// refTxnSubmissionId
			deactivingIsLive(refTxnSubmissionId);

			List<IndicatorModel> listOfIndicatorModel = new ArrayList<>();
			Integer noOfDelivery = null;
			Integer noOfLiveBirth = null;
			
			for (Map.Entry<String, List<IndicatorModel>> mapOfModel : sncuIndicatorModel.entrySet()) {
				listOfIndicatorModel.addAll(mapOfModel.getValue());
			}
			
			//iterate the loop before to get no of live birth and no of delivery value
			for (IndicatorModel modelData : listOfIndicatorModel) {
				
				if(noOfDeliveryIndicators.contains(modelData.getIndicatorId()) && modelData.getDenominatorValue()!= null)
					noOfDelivery = modelData.getDenominatorValue();
				
				else if(noOfLiveBirthIndicators.contains(modelData.getIndicatorId()) && modelData.getDenominatorValue()!= null)
					noOfLiveBirth = modelData.getDenominatorValue();
				
				//once we got, break the loop
				if(null!=noOfDelivery && null!=noOfLiveBirth)
					break;
			}
			
			// saving List of TXNSNCUData in DB
			List<TXNSNCUData> txnsncuDatas = new ArrayList<>();
			for (IndicatorModel modelData : listOfIndicatorModel) {
				TXNSNCUData txnSNCUData = new TXNSNCUData();
				txnSNCUData.setCreatedDate(new Timestamp(new Date().getTime()));
				txnSNCUData.setNumeratorValue(modelData.getNumeratorValue());
				txnSNCUData.setDenominatorValue(modelData.getDenominatorValue());
				txnSNCUData.setPercentage(modelData.getPercentage());
				txnSNCUData.setIsLive(true); //setting isLive = true for updating 
				txnSNCUData.setTxnIndicatorId(modelData.getIndicatorId());
				txnSNCUData.setIndicatorFacilityTimeperiodMapping(indicatorFacilityTimeperiodMappingRepository.findByIndFacilityTpId(modelData.getIndicatorFacilityTimeperiodId()));
				txnSNCUData.setTxnSubmissionManagement(new TXNSubmissionManagement(txnSubmissionId));
				
				
//				check for if Numerator is greater than denominator
				if((modelData.getNumeratorValue()!= null && modelData.getDenominatorValue()!=null)&&
						modelData.getNumeratorValue() > modelData.getDenominatorValue())
					txnSNCUData.setDescription(new TypeDetail(Integer.parseInt(messages.getMessage(Constants.Web.WARNING_DESCRIPTION_MESSAGE_FIRST, null,null))));
				
				//check for Number of live births is greater than Number of deliveries
				else if(noOfLiveBirthIndicators.contains(modelData.getIndicatorId()) && null!= noOfDelivery && null!= noOfLiveBirth &&
						((null!= modelData.getDenominatorValue() && null==modelData.getIsProfile() ) ||
								null!=modelData.getIsProfile())  && noOfLiveBirth > noOfDelivery)
					txnSNCUData.setDescription(new TypeDetail(Integer.parseInt(messages.getMessage(Constants.Web.WARNING_DESCRIPTION_MESSAGE_SECOND, null,null))));
				
//				/check for Number of deliveries is less than Number of live births
				else if(noOfDeliveryIndicators.contains(modelData.getIndicatorId()) && null!= noOfDelivery && null!= noOfLiveBirth &&
						((null!= modelData.getDenominatorValue() && null==modelData.getIsProfile() ) ||
								null!=modelData.getIsProfile())  &&  noOfDelivery < noOfLiveBirth)
					txnSNCUData.setDescription(new TypeDetail(Integer.parseInt(messages.getMessage(Constants.Web.WARNING_DESCRIPTION_MESSAGE_THIRD, null,null))));
				
				txnsncuDatas.add(txnSNCUData);
			}
			txnSNCUDataRepository.save(txnsncuDatas);
		} catch (Exception e) {
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}

	/**
	 * @param txnSubmissionId
	 * set false to overridden TXNSNCUData
	 */
	private void deactivingIsLive(Integer txnSubmissionId) {

		try {
			List<TXNSNCUData> txnsncuDatas = new ArrayList<>();
			List<TXNSNCUData> sncuIndicatorDatas = txnSNCUDataRepository.findByTxnSubmissionManagementTxnSubmissionIdAndIsLiveTrue(txnSubmissionId);

			for (TXNSNCUData txnSNCUData : sncuIndicatorDatas) {
				txnSNCUData.setIsLive(false);
				txnsncuDatas.add(txnSNCUData);
			}
			txnSNCUDataRepository.save(txnsncuDatas);
		} catch (Exception e) {
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}
	
	//

	/* (non-Javadoc)
	 * @author subrata (subrata@sdrc.co.in) 
	 * @see org.sdrc.scsl.service.SNCUService#getCurrentTimePeriod()
	 * get the last month time period
	 */
	@Override
	public TimePeriodModel getCurrentTimePeriod() {
		TimePeriod latestTimeperiods = timePeriodRepository
				.lastMonthTimeperiod();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");

		List<TimePeriodModel> listOfTimeperiod = new ArrayList<>();
		TimePeriodModel timePeriodModel = new TimePeriodModel();
		timePeriodModel.setTimePeriod(latestTimeperiods.getTimePeriod());
		timePeriodModel.setTimePeriodId(latestTimeperiods
				.getTimePeriodId());
		timePeriodModel
				.setEndDate(latestTimeperiods.getEndDate() == null ? null
						: dateFormat.format(latestTimeperiods
								.getEndDate()));
		timePeriodModel
				.setStartDate(latestTimeperiods.getStartDate() == null ? null
						: dateFormat.format(latestTimeperiods
								.getStartDate()));
		timePeriodModel.setWave(latestTimeperiods.getWave());
		timePeriodModel.setPeriodicity(latestTimeperiods
				.getPeriodicity());
		listOfTimeperiod.add(timePeriodModel);
		return timePeriodModel;
	}
	/**
	 * @author subrata (subrata@sdrc.co.in) 
	 *  Creating timeperiod in "TimePeriod" for every month first day at 12.00 AM 
	 * 
	 */
	@Override
	public void createTimeperiod() throws Exception {
		
		try {
			Calendar startDateCalendar = Calendar.getInstance();

			startDateCalendar.set(Calendar.DATE, 1);
			Date sDate = startDateCalendar.getTime();
			String startDateStr = simpleDateformater.format(sDate);
			Date startDate = formatter.parse(startDateStr + " 00:00:00.000");
			startDateCalendar.set(Calendar.DATE, startDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			startDateCalendar.set(Calendar.DATE, startDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date eDate = startDateCalendar.getTime();
			String endDateStr = simpleDateformater.format(eDate);
			Date endDate = formatter.parse(endDateStr + " 00:00:00.000");
			TimePeriod utTime = timePeriodRepository.findByStartDateAndEndDate(startDate, endDate);

			if (utTime == null) {
				TimePeriod utTimePeriod = new TimePeriod();
				utTimePeriod.setStartDate(startDate);
				utTimePeriod.setEndDate(endDate);
				utTimePeriod.setPeriodicity(messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null)); // for monthly aggregation
													// periodicity is 1 //quarter-
													// 3, year- 12
				utTimePeriod.setCreatedDate(new Timestamp(new Date().getTime()));
				utTimePeriod.setTimePeriod(monthYearSdf.format(startDate));
				utTimePeriod.setWave(2); // change later
				timePeriodRepository.save(utTimePeriod);
			}
		} catch (Exception e) {
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		
	}
	
	/**
	 * @author subrata (subrata@sdrc.co.in) 
	 *  Updating facility mapping for every facility in "IndicatorFacilityTimeperiodMapping" for every month first day at 12.01 AM 
	 *  then create a new time period
	 * 
	 */
	@Override
	@Transactional
	public void updateFacilityIndicatorMapping() throws Exception {
		
		try {

			List<TimePeriod> listOfTimePeriod = timePeriodRepository
					.findTop2ByPeriodicityOrderByTimePeriodIdDesc(messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null));
			Integer previousTimeperiodId = listOfTimePeriod.get(1).getTimePeriodId();
			List<IndicatorFacilityTimeperiodMapping> iftm = indicatorFacilityTimeperiodMappingRepository.findByTimePeriodTimePeriodIdAndIsLiveTrue(previousTimeperiodId);
			List<IndicatorFacilityTimeperiodMapping> toUpdateTimeperiodMapping = new ArrayList<>();
			
			List<IndicatorFacilityTimeperiodMapping> latestMapping = indicatorFacilityTimeperiodMappingRepository
					.findByTimePeriodTimePeriodIdAndIsLiveTrue(listOfTimePeriod.get(0).getTimePeriodId());
			
			if(latestMapping.isEmpty()){
				for (IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping : iftm) {
					IndicatorFacilityTimeperiodMapping facilityTimeperiodMapping = new IndicatorFacilityTimeperiodMapping();
					facilityTimeperiodMapping.setFacility(indicatorFacilityTimeperiodMapping.getFacility());
					facilityTimeperiodMapping.setTimePeriod(listOfTimePeriod.get(0));
					facilityTimeperiodMapping.setIndicator(indicatorFacilityTimeperiodMapping.getIndicator());
					facilityTimeperiodMapping.setCreatedDate(indicatorFacilityTimeperiodMapping.getCreatedDate());
					facilityTimeperiodMapping.setUpdatedDate(new Timestamp(new Date().getTime()));
					facilityTimeperiodMapping.setIsLive(true);
					
					toUpdateTimeperiodMapping.add(facilityTimeperiodMapping);
					
				}
				indicatorFacilityTimeperiodMappingRepository.save(toUpdateTimeperiodMapping);
			}
		} catch (Exception e) {
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		
	}
	
	/**
	 * @author subrata (subrata@sdrc.co.in) 
	 *  Updating master first time
	 *  facility mapping for every facility in "IndicatorFacilityTimeperiodMapping" for a time period
	 * 
	 */
	@Override
	@Transactional
	public void updateMasterFacilityIndicatorMapping(int timeperiodId) throws Exception {
		
		try {
			List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings = new ArrayList<IndicatorFacilityTimeperiodMapping>();
			
			TimePeriod timePeriod = timePeriodRepository.findByTimePeriodId(timeperiodId);
//			List<Indicator> indicators = indicatorRepository.findByIndicatorTypeTypeDetailIdInAndIsLrIsNull(Arrays.asList(17,18));
			//set all mapping for intermediate and outcome
			List<Indicator> indicators = indicatorRepository.findByIndicatorTypeTypeDetailIdInOrderByIndicatorOrderAsc(Arrays.asList(17,18));
			//set all profile mapping independent of LR
			List<Indicator> indicators2 = indicatorRepository.findByIsProfileTrueAndIsLrIsNull();
			indicators.addAll(indicators2);
			List<Area> areas = areaRepository.findByLevel(4);
			
			for (Area area : areas) {
				
				for (Indicator indicator : indicators) {
					IndicatorFacilityTimeperiodMapping facilityTimeperiodMapping = new IndicatorFacilityTimeperiodMapping();
					facilityTimeperiodMapping.setFacility(area);
					facilityTimeperiodMapping.setTimePeriod(timePeriod);
					facilityTimeperiodMapping.setIndicator(indicator);
					facilityTimeperiodMapping.setCreatedDate(new Timestamp(new Date().getTime()));
					facilityTimeperiodMapping.setIsLive(true);
					
					indicatorFacilityTimeperiodMappings.add(facilityTimeperiodMapping);
				}
				
//				if facility has labour room then add profile indicators dependent to labor room
				
				if(area.getHasLr()){
					for (Indicator indicator : indicatorRepository.findByIsProfileTrueAndIsLrTrue()){
						IndicatorFacilityTimeperiodMapping facilityTimeperiodMapping = new IndicatorFacilityTimeperiodMapping();
						facilityTimeperiodMapping.setFacility(area);
						facilityTimeperiodMapping.setTimePeriod(timePeriod);
						facilityTimeperiodMapping.setIndicator(indicator);
						facilityTimeperiodMapping.setCreatedDate(new Timestamp(new Date().getTime()));
						facilityTimeperiodMapping.setIsLive(true);
						
						indicatorFacilityTimeperiodMappings.add(facilityTimeperiodMapping);
					}
				}
			}
			indicatorFacilityTimeperiodMappingRepository.save(indicatorFacilityTimeperiodMappings);
		} catch (Exception e) {
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		
		
	}
	//comment internal use of  code
	/*public static void main(String[] args) throws IOException {
		
		String filename = "F:/DATA OLD/Sarita/The Workspace/SCSL/phase -II/mapping of intermediate and profile for legacy times/profile_mapping.xlsx";
		FileInputStream stream = new FileInputStream(
				new File(filename));
		
		//POIFSFileSystem preserveNodes holds all the macro objects
		XSSFWorkbook workbook = new XSSFWorkbook(stream);

		// Get first/desired sheet from the workbook
//		XSSFSheet sheet = workbook.getSheet("Sheet1");
		XSSFSheet sheet = workbook.getSheet("Sheet3");
//		XSSFSheet querySheet = workbook.getSheet("Sheet2");
		XSSFSheet querySheet = workbook.getSheet("Sheet4");
		
		String insertStatement = "INSERT INTO public.indicator_facility_timeperiod_mapping( created_date, "
				+ "facility_id_fk, timeperiod_id_fk, indicator_id_fk, is_live) select '2017-11-07 13:27:26.872',";
		
		String queryStr = ",'true' where not exists (select * from  indicator_facility_timeperiod_mapping where facility_id_fk = ";
		
		String query = "";
		
		int querySheetRowCount = 0;
		
//		for (int facilityRowCount = 1; facilityRowCount <= 87; facilityRowCount++) { // all facility
		for (int facilityRowCount = 1; facilityRowCount <= 8; facilityRowCount++) { //No LR facility
//		for (int facilityRowCount = 1; facilityRowCount <= 79; facilityRowCount++) {//LR facility
			Row facilityRow = sheet.getRow(facilityRowCount);

			Cell facilityCell = facilityRow.getCell(0);
			
			for(int tpRowCount = 1; tpRowCount <= 40; tpRowCount++){
				
				Row tpRow = sheet.getRow(tpRowCount);
				Cell timeCell = tpRow.getCell(1);
				
//				for(int indRowCount = 1; indRowCount <= 17; indRowCount++){//all intermediate
				for(int indRowCount = 1; indRowCount <= 3; indRowCount++){//all no lr profile inds
//				for(int indRowCount = 1; indRowCount <= 11; indRowCount++){//all lr profile inds
					
					Row indRow = sheet.getRow(indRowCount);
					Cell indCell = indRow.getCell(2);
					
					query = insertStatement + (int) facilityCell.getNumericCellValue() + "," +  (int) timeCell.getNumericCellValue()
							+ "," +  (int) indCell.getNumericCellValue() + queryStr +  (int) facilityCell.getNumericCellValue()
							+ " and timeperiod_id_fk = " +  (int) timeCell.getNumericCellValue() + " and indicator_id_fk = "
							+  (int) indCell.getNumericCellValue() + ");";
					
//					System.out.println(query);
					
					Row querySheetRow = querySheet.createRow(querySheetRowCount);
					Cell queryCell = querySheetRow.createCell(0);
					queryCell.setCellValue(query);
					
					
					querySheetRowCount++;
				}
				
			}

		}
			
		FileOutputStream fos = new FileOutputStream(new File(filename));
		workbook.write(fos);
		workbook.close();
		fos.close();
			 
	}*/
	
}
