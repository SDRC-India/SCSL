package org.sdrc.scsl.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.Indicator;
import org.sdrc.scsl.domain.MSTUser;
import org.sdrc.scsl.domain.TXNSNCUData;
import org.sdrc.scsl.domain.TXNSubmissionManagement;
import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.domain.TypeDetail;
import org.sdrc.scsl.domain.UserAreaMapping;
import org.sdrc.scsl.model.web.MailModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.RoleFeaturePermissionSchemeModel;
import org.sdrc.scsl.model.web.SubmittedDataModel;
import org.sdrc.scsl.model.web.SubmittedFacilityDetailModel;
import org.sdrc.scsl.model.web.UserAreaModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.MSTUserRepository;
import org.sdrc.scsl.repository.TXNSNCUDataRepository;
import org.sdrc.scsl.repository.TXNSubmissionManagementRepository;
import org.sdrc.scsl.repository.TimePeriodRepository;
import org.sdrc.scsl.repository.UserAreaMappingRepository;
import org.sdrc.scsl.repository.UserRoleFeaturePermissionMappingRepository;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 * @author Harsh Pratyush(harsh@sdrc.co.in) This serviceimpl will hold all the
 *         methods for submission management view page
 *
 */
@Service
public class SubmissionManagementServiceImpl implements
		SubmissionManagementService {

	@Autowired
	private TXNSubmissionManagementRepository tXNSubmissionManagementRepository;

	@Autowired
	private TXNSNCUDataRepository txnsncuDataRepository;

	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private ResourceBundleMessageSource errorMessageSource;

	@Autowired
	private ResourceBundleMessageSource messages;
	
	@Autowired 
	private StateManager stateManager;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private UserAreaMappingRepository userAreaMappingRepository;
	
	@Autowired
	private UserRoleFeaturePermissionMappingRepository userRoleFeaturePermissionMappingRepository;
	
	@Autowired
	private MSTUserRepository mstUserRepository;

	private SimpleDateFormat simpleDateformater = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_FORMATTER_HYPHEN));
	
	private SimpleDateFormat simpleDateformaterWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static final Logger LOGGER=Logger.getLogger(SubmissionManagementServiceImpl.class);
	
	@Autowired
	private ResourceBundleMessageSource notification;
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in) created on 25-04-2017 get all
	 *         DEO submitted data to superintendent
	 */
	@Override
	public List<SubmittedDataModel> getAllSubmissionsForSuperintendent() {

		// we will fetch last 12 months data from db
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.add(Calendar.MONTH, -12);
		startDateCalendar.set(Calendar.DATE, 1);

		Timestamp startDate = new java.sql.Timestamp(startDateCalendar
				.getTime().getTime());
		Timestamp endDate = new java.sql.Timestamp(new Date().getTime()); // current
																			// date
		UserModel userModel=(UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		Integer facilityId = userModel.getFacilityId(); // we will get it from statemanager later
		LocalDate now = LocalDate.now();
		List<SubmittedDataModel> submittedDataModels = new ArrayList<>();
		for (TXNSubmissionManagement txnSubmissionManagement : tXNSubmissionManagementRepository
				.findByCreatedDateBetweenAndFacilityAreaIdOrderByCreatedDateDesc(startDate, endDate,
						facilityId)) {

			SubmittedDataModel submittedDataModel = new SubmittedDataModel();
			submittedDataModel.setTxnSubmissionId(txnSubmissionManagement
					.getTxnSubmissionId());
			submittedDataModel.setFacilityId(facilityId);
			submittedDataModel.setFacilityName(txnSubmissionManagement
					.getFacility().getAreaName());
			submittedDataModel.setFacilityType(txnSubmissionManagement
					.getFacility().getFacilityType().getTypeDetail());
			submittedDataModel.setFacilitySize(txnSubmissionManagement
					.getFacility().getFacilitySize().getTypeDetail());
			submittedDataModel.setLatest(txnSubmissionManagement.getIsLatest());
			submittedDataModel.setRemarkByMnE(txnSubmissionManagement
					.getRemarkByMnE());
			submittedDataModel
					.setRemarkBySuperintendent(txnSubmissionManagement
							.getRemarkBySuperintendent());
			submittedDataModel.setStatusMnE(null != txnSubmissionManagement
					.getStatusMne() ? txnSubmissionManagement.getStatusMne()
					.getTypeDetail() : null);
			submittedDataModel
					.setStatusSuperintendent(null != txnSubmissionManagement
							.getStatusSuperintendent() ? txnSubmissionManagement
							.getStatusSuperintendent().getTypeDetail() : null);
			submittedDataModel.setTimePeriodId(txnSubmissionManagement
					.getTimePeriod().getTimePeriodId());
			submittedDataModel.setTimePeriod(txnSubmissionManagement
					.getTimePeriod().getTimePeriod());
			submittedDataModel.setUploadDate(simpleDateformater
					.format(txnSubmissionManagement.getCreatedDate()));
			submittedDataModel.setRefSubmissionId(txnSubmissionManagement
					.getRefSubmissionId());

		/////date check----
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(txnSubmissionManagement.getCreatedDate());
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1; // starts from 0 index
//			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

			LocalDate txnCreatedDate = LocalDate.of(year, month, 1); //keep the day as 1 so that diff.getMonths() will work correctly
			Period diff = Period.between(txnCreatedDate, now);

			// if the date is less than 21 of current month
			// and pending for superintendent and latest then set enable true
			// if the date is greater than 20 but the submission is pending--
			// that means mne has rejected
			// the submissions (as it should auto approve after 20 )
			// in that case make enable true
			submittedDataModel.setEnableSuperintendent((txnSubmissionManagement.getIsLatest()
					&& now.getDayOfMonth() > Integer
							.parseInt(messages.getMessage(Constants.Web.LAST_DATE_SUP, null, null))
					&& txnSubmissionManagement.getStatusSuperintendent().getTypeDetail()
							.equals(messages.getMessage(Constants.Web.PENDING_STATUS_TYPE_NAME, null, null)))
					|| (now.getDayOfMonth() < Integer
							.parseInt(messages.getMessage(Constants.Web.SUPERINTENDENT_APPROVAL_LASTDATE, null, null))
							&& diff.getMonths() == 0 && diff.getYears() == 0
							&& txnSubmissionManagement.getStatusSuperintendent().getTypeDetail()
									.equals(messages.getMessage(Constants.Web.PENDING_STATUS_TYPE_NAME, null, null))
							&& txnSubmissionManagement.getIsLatest() ? true : false));
			submittedDataModels.add(submittedDataModel);
		}

		return submittedDataModels;

	}

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in) created on 26-04-2017 get all
	 *  		DEO submitted data to superintendent
	 *          superintendent approved/ auto-approved data to MnE
	 */
	@Override
	public List<SubmittedDataModel> getAllSubmissionsForSuperintendentMnE() {

		// we will fetch last 12 months data from db
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.add(Calendar.MONTH, -12);
		startDateCalendar.set(Calendar.DATE, 1);

		Timestamp startDate = new java.sql.Timestamp(startDateCalendar
				.getTime().getTime());
		Timestamp endDate = new java.sql.Timestamp(new Date().getTime()); // current
																			// date
		Map<Integer, Area> areaIdNameMap = new HashMap<>();
		
		LocalDate now = LocalDate.now();
		List<Area> areas = areaRepository.findAll();

		// inner map contains area parent id and area name
		for (Area area : areas) {
			areaIdNameMap.put(area.getAreaId(), area);
		}

		List<TXNSubmissionManagement> submissionManagements = null;
		UserModel userModel=(UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		
		List<UserAreaModel> userAreaModels = userModel != null ? userModel.getUserAreaModels() : null;
		for (UserAreaModel userAreaModel : userAreaModels) {
			if (userAreaModel.getUserRoleFeaturePermissionMappings() != null) {
				for (int i = 0; i < userAreaModel.getUserRoleFeaturePermissionMappings().size(); i++) {
					RoleFeaturePermissionSchemeModel roleFeaturePermissionSchemeModel = userAreaModel.getUserRoleFeaturePermissionMappings()
							.get(i).getRoleFeaturePermissionSchemeModel();
					//superintendent is assigned to one facility only
					if (roleFeaturePermissionSchemeModel.getRole().getRoleName().contains(messages.getMessage(Constants.Web.SUPERINTENDENT_ROLE_NAME,null,null))) {
						submissionManagements = tXNSubmissionManagementRepository
								.findByCreatedDateBetweenAndFacilityAreaIdOrderByCreatedDateDesc(startDate, endDate,
										userModel.getFacilityId());
						break; //roleFeaturePermissionSchemeModel loop break
					}else if(roleFeaturePermissionSchemeModel.getRole().getRoleName().contains(messages.getMessage(Constants.Web.MNE_ROLE_NAME,null,null))){
						// do not show to MnE if MnE status is null as the superintendent
						// status is pending or rejected
						submissionManagements = tXNSubmissionManagementRepository
								.findByCreatedDateBetweenAndStatusSuperintendentTypeDetailIdInOrderByCreatedDateDesc(startDate,
										endDate,
										Arrays.asList(
												Integer.parseInt(messages.getMessage(
														Constants.Web.SUBMISSION_STATUS_APPROVED, null, null)),
												Integer.parseInt(messages.getMessage(
														Constants.Web.SUBMISSION_STATUS_AUTO_APPROVED, null, null)),
												Integer.parseInt(messages.getMessage(
														Constants.Web.SUBMISSION_LEGACY_STATUS, null, null)))); 
						break; //roleFeaturePermissionSchemeModel loop break
					}
					
				}

			}
		}
		
		List<SubmittedDataModel> submittedDataModels = new ArrayList<>();
		Map<String,SubmittedDataModel> submittedDataModelsMap=new LinkedHashMap<>();

		for (TXNSubmissionManagement txnSubmissionManagement : submissionManagements) {

			
			SubmittedDataModel submittedDataModel = new SubmittedDataModel();
			submittedDataModel.setTxnSubmissionId(txnSubmissionManagement
					.getTxnSubmissionId());
			submittedDataModel.setFacilityId(txnSubmissionManagement
					.getFacility().getAreaId());
			submittedDataModel.setFacilityName(txnSubmissionManagement
					.getFacility().getAreaName());
			submittedDataModel.setFacilityType(txnSubmissionManagement
					.getFacility().getFacilityType().getTypeDetail());
			submittedDataModel.setFacilitySize(txnSubmissionManagement
					.getFacility().getFacilitySize().getTypeDetail());
			submittedDataModel.setLatest(txnSubmissionManagement.getIsLatest());
			submittedDataModel.setRemarkByMnE(txnSubmissionManagement
					.getRemarkByMnE());
			submittedDataModel
					.setRemarkBySuperintendent(txnSubmissionManagement
							.getRemarkBySuperintendent());
			submittedDataModel.setStatusMnE(null != txnSubmissionManagement
					.getStatusMne() ? txnSubmissionManagement.getStatusMne()
					.getTypeDetail() : null);
			submittedDataModel
					.setStatusSuperintendent(null != txnSubmissionManagement
							.getStatusSuperintendent() ? txnSubmissionManagement
							.getStatusSuperintendent().getTypeDetail() : null);
			submittedDataModel.setTimePeriodId(txnSubmissionManagement
					.getTimePeriod().getTimePeriodId());
			submittedDataModel.setTimePeriod(txnSubmissionManagement
					.getTimePeriod().getTimePeriod());
			submittedDataModel.setUploadDate(simpleDateformaterWithTime
					.format(txnSubmissionManagement.getCreatedDate()));
			submittedDataModel.setRefSubmissionId(txnSubmissionManagement
					.getRefSubmissionId());
			submittedDataModel.setDistrictId(txnSubmissionManagement
					.getFacility().getParentAreaId()); // facility's parent is
														// district
			// from district id get district name from areaIdNameMap
			submittedDataModel.setDistrictName(areaIdNameMap
					.get(txnSubmissionManagement.getFacility()
							.getParentAreaId()).getAreaName());
			// from district id get state id from areaIdParentIdMap
			Area currentIterationState = areaIdNameMap.get(areaIdNameMap
					.get(txnSubmissionManagement.getFacility()
							.getParentAreaId()).getParentAreaId());
			submittedDataModel.setStateId(currentIterationState.getAreaId());
			submittedDataModel.setStateName(currentIterationState.getAreaName());
			
			/////date check----
			
			Calendar cal = Calendar.getInstance();
		    cal.setTime(txnSubmissionManagement.getCreatedDate());
		    int year = cal.get(Calendar.YEAR);
		    int month = cal.get(Calendar.MONTH)+1; //starts from 0 index
//		    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		    
			LocalDate txnCreatedDate = LocalDate.of(year, month, 1);//keep the day as 1 so that diff.getMonths() will work correctly
			Period diff = Period.between(txnCreatedDate, now);
			
			// if the date is less than 21 of current month
			// and pending for superintendent and latest then set enable true
			//if the date is greater than 20 but the submission is pending-- that means mne has rejected 
			//the submissions (as it should auto approve after 20 )
			//in that case make enable true
			submittedDataModel.setEnableSuperintendent((txnSubmissionManagement.getIsLatest()
					&& now.getDayOfMonth() > Integer
							.parseInt(messages.getMessage(Constants.Web.LAST_DATE_SUP, null, null))
					&& txnSubmissionManagement.getStatusSuperintendent().getTypeDetail()
							.equals(messages.getMessage(Constants.Web.PENDING_STATUS_TYPE_NAME, null, null)))
					|| (now.getDayOfMonth() < Integer
							.parseInt(messages.getMessage(Constants.Web.SUPERINTENDENT_APPROVAL_LASTDATE, null, null))
							&& diff.getMonths() == 0 && diff.getYears() == 0
							&& txnSubmissionManagement.getStatusSuperintendent().getTypeDetail()
									.equals(messages.getMessage(Constants.Web.PENDING_STATUS_TYPE_NAME, null, null))
							&& txnSubmissionManagement.getIsLatest() ? true : false));

			// if the date is less than 25 of current month
			// and pending for mne and latest then set enable true
//			submittedDataModel.setEnableMnE(now.getDayOfMonth() < Integer
//					.parseInt(messages.getMessage(Constants.Web.MNE_APPROVAL_LASTDATE, null, null))
//					&& diff.getMonths() == 0 && diff.getYears() == 0 && txnSubmissionManagement.getStatusMne() != null
//					&& txnSubmissionManagement.getStatusMne().getTypeDetail()
//							.equals(messages.getMessage(Constants.Web.PENDING_STATUS_TYPE_NAME, null, null))
//					&& txnSubmissionManagement.getIsLatest() ? true : false);
			
			// if the date is less than last day of current month
			// and pending for mne and latest then set enable true
			submittedDataModel.setEnableMnE(now.getDayOfMonth() <= java.time.LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()
					&& diff.getMonths() == 0 && diff.getYears() == 0 && txnSubmissionManagement.getStatusMne() != null
					&& txnSubmissionManagement.getStatusMne().getTypeDetail()
							.equals(messages.getMessage(Constants.Web.PENDING_STATUS_TYPE_NAME, null, null))
					&& txnSubmissionManagement.getIsLatest() ? true : false);
			
			if(submittedDataModelsMap.containsKey(submittedDataModel.getFacilityId()+"_"+submittedDataModel.getTimePeriodId()))
			{
				if(submittedDataModelsMap.get(submittedDataModel.getFacilityId()+"_"+submittedDataModel.getTimePeriodId()).getSubmittedDataModelLogList()==null)
				{
					submittedDataModelsMap.get(submittedDataModel.getFacilityId()+"_"+submittedDataModel.getTimePeriodId()).setSubmittedDataModelLogList(new ArrayList<SubmittedDataModel>(Arrays.asList(submittedDataModel)));
				}
				else
				submittedDataModelsMap.get(submittedDataModel.getFacilityId()+"_"+submittedDataModel.getTimePeriodId()).getSubmittedDataModelLogList().add(submittedDataModel);
			}
			else
			{
					submittedDataModelsMap.put(submittedDataModel.getFacilityId()+"_"+submittedDataModel.getTimePeriodId(), submittedDataModel);
			}
		}

		submittedDataModels.addAll(submittedDataModelsMap.values());
		return submittedDataModels;

	}

	//filter profile and data entry indicators
	public static List<TXNSNCUData> filterIndicator(List<TXNSNCUData> txnsncuDatas, Predicate<TXNSNCUData> predicate){
		return txnsncuDatas.stream().filter(predicate).collect(Collectors.<TXNSNCUData>toList());
	}
	
	//predict for profile and data entry indicators
	public static Predicate<TXNSNCUData> isProfileDataEntryIndicator(Boolean isProfile){
		return p -> p.getIndicatorFacilityTimeperiodMapping().getIndicator().getIsProfile()==isProfile;
	}
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in) created on 27-04-2017 view
	 *         each submission data
	 */
	@Override
	@Transactional
	public List<SubmittedFacilityDetailModel> fetchSubmittedValues(
			Integer txnSubmissionId, Integer refSubmissionId,
			Integer facilityId, Integer timePeriodId) {

		// if refSubmissionId is there. fetch that (for superintendent and M&E aprrove/reject submissions
		List<TXNSNCUData> txnsncuDatas = txnsncuDataRepository
				.findByTxnSubmissionManagementTxnSubmissionIdOrderByIndicatorFacilityTimeperiodMappingIndicatorIndicatorOrderAsc(refSubmissionId == null ? txnSubmissionId
						: refSubmissionId);
		
		//
		List<TXNSNCUData> profileTxnsncuDatas =filterIndicator(txnsncuDatas, isProfileDataEntryIndicator(true));
		
		List<TXNSNCUData> dataEntryTxnsncuDatas =filterIndicator(txnsncuDatas, isProfileDataEntryIndicator(null));

		txnsncuDatas.clear();
		
		txnsncuDatas.addAll(profileTxnsncuDatas);
		txnsncuDatas.addAll(dataEntryTxnsncuDatas);
		
		// for monthly tp only
		List<TimePeriod> ids = timePeriodRepository
				.findTop2ByTimePeriodIdLessThanAndPeriodicityOrderByTimePeriodIdDesc(
						timePeriodId, messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null));

		// keep time nids only\
		List<Integer> timePeriodIds = new ArrayList<>();
		ids.forEach(timePeriod -> timePeriodIds.add(timePeriod
				.getTimePeriodId()));

		List<TXNSubmissionManagement> txnSubmissionManagements = tXNSubmissionManagementRepository
				.findByFacilityAreaIdAndTimePeriodTimePeriodIdInAndIsLatestTrue(
						facilityId, timePeriodIds);

		// indicator---- timeperiod----value
		Map<String, Double> indicatorTimePerecentValMap = new LinkedHashMap<>();

		for (TXNSubmissionManagement txnSubmissionManagement : txnSubmissionManagements) {
			// get all sncu data for that submission
			List<TXNSNCUData> oldTxnsncuDatas = txnsncuDataRepository.findByTxnSubmissionManagementTxnSubmissionId(
					txnSubmissionManagement.getRefSubmissionId() != null ? txnSubmissionManagement.getRefSubmissionId()
							: txnSubmissionManagement.getTxnSubmissionId());
			// for each sncu data keep in map
			for (TXNSNCUData txnsncuData : oldTxnsncuDatas) {
				indicatorTimePerecentValMap.put(
						txnsncuData.getIndicatorFacilityTimeperiodMapping().getIndicator().getIndicatorId() + "_"
								+ txnSubmissionManagement.getTimePeriod().getTimePeriodId(),
						txnsncuData.getPercentage());
			}
		}

		List<SubmittedFacilityDetailModel> submittedFacilityDetailModels = new ArrayList<>();
		for (TXNSNCUData txnsncuData : txnsncuDatas) {
			SubmittedFacilityDetailModel submittedFacilityDetailModel = new SubmittedFacilityDetailModel();
			Indicator indicator = txnsncuData
					.getIndicatorFacilityTimeperiodMapping().getIndicator();
			submittedFacilityDetailModel.setIsProfile(indicator.getIsProfile()); //if it is profile entry then no indicator type
			submittedFacilityDetailModel.setIndicatorType(null!=indicator
					.getIndicatorType() ? indicator
					.getIndicatorType().getTypeDetail() : "Profile");
			submittedFacilityDetailModel.setIndicatorName(indicator
					.getIndicatorName());

			// timePeriodIds.get(1) is t2 , timePeriodIds.get(0) is t1
			// if the indicator is not present in the previous time, put NA
			
			//for profile indicators 34, 35,36, 38,39,40, 43 DO NOT append % as they are number value
			submittedFacilityDetailModel.setT2PercentValue(
					timePeriodIds.size() > 1 ? indicatorTimePerecentValMap.containsKey(indicator.getIndicatorId() + "_"
							+ timePeriodIds.get(1))
									? null != indicatorTimePerecentValMap.get(indicator.getIndicatorId()
											+ "_" + timePeriodIds.get(1))
													? indicator.getIndicatorId() == 34
															|| indicator.getIndicatorId() == 35 
															|| indicator.getIndicatorId() == 36
															|| indicator.getIndicatorId() == 38
															|| indicator.getIndicatorId() == 39
															|| indicator.getIndicatorId() == 40
															|| indicator.getIndicatorId() == 43
																	? indicatorTimePerecentValMap
																			.get(indicator.getIndicatorId() + "_"
																					+ timePeriodIds.get(1))
																			.toString()
																	: indicatorTimePerecentValMap
																			.get(indicator.getIndicatorId() + "_"
																					+ timePeriodIds.get(1))
																			.toString()+ "%"
													: "N/A"
									: "N/A"
							: "N/A");

			submittedFacilityDetailModel.setT1PercentValue(
					timePeriodIds.size() > 0 ? indicatorTimePerecentValMap.containsKey(indicator.getIndicatorId() + "_"
							+ timePeriodIds.get(0))
									? null != indicatorTimePerecentValMap.get(indicator.getIndicatorId()
											+ "_" + timePeriodIds.get(0))
													? indicator.getIndicatorId() == 34
															|| indicator.getIndicatorId() == 35 
															|| indicator.getIndicatorId() == 36
															|| indicator.getIndicatorId() == 38
															|| indicator.getIndicatorId() == 39
															|| indicator.getIndicatorId() == 40
															|| indicator.getIndicatorId() == 43
																	? indicatorTimePerecentValMap
																			.get(indicator.getIndicatorId() + "_"
																					+ timePeriodIds.get(0))
																			.toString()
																	: indicatorTimePerecentValMap
																			.get(indicator.getIndicatorId() + "_"
																					+ timePeriodIds.get(0))
																			.toString()+ "%"
													: "N/A"
									: "N/A"
							: "N/A");

			//do not show numerator value for number type profile indicators
			if (indicator.getIndicatorId() != 34 && indicator.getIndicatorId() != 35 && indicator.getIndicatorId() != 36
					&& indicator.getIndicatorId() != 38 && indicator.getIndicatorId() != 39
							&& indicator.getIndicatorId() != 40 && indicator.getIndicatorId() != 43)
				submittedFacilityDetailModel.setNumeratorValue(txnsncuData.getNumeratorValue());
			
			submittedFacilityDetailModel.setDenominatorValue(txnsncuData
					.getDenominatorValue());
			
			//if the indicator type is intermediate and there is no percent value then put "NA"
			
			//if the ind type is profile entry
			//set number values with out decimal
			submittedFacilityDetailModel.setPercentValue(
					null == txnsncuData.getIndicatorFacilityTimeperiodMapping().getIndicator().getIndicatorType()
							? null == txnsncuData.getDenominatorValue()
									? Long.toString(Math.round(txnsncuData.getPercentage()))
									: null != txnsncuData.getDenominatorValue() && null != txnsncuData.getPercentage()
											? txnsncuData.getPercentage().toString() + "%" : ""
							: txnsncuData.getIndicatorFacilityTimeperiodMapping().getIndicator().getIndicatorType()
									.getTypeDetail()
									.equals(messages.getMessage(Constants.Web.INDICATOR_TYPE_INTERMEDIATE_NAME, null, null))
									&& (null == txnsncuData.getPercentage()) ? "N/A"
											: txnsncuData.getPercentage() != null
													? txnsncuData.getPercentage().toString() + "%" : "");
			submittedFacilityDetailModel.setDescription(txnsncuData.getDescription()!=null ? txnsncuData.getDescription().getTypeDetail() : "");
			submittedFacilityDetailModel.setCssClass(txnsncuData.getDescription()!=null ? "lightyellow" : "");
			submittedFacilityDetailModels.add(submittedFacilityDetailModel);
		}

		return submittedFacilityDetailModels;
	}

//	If the Superintendent rejects a submission, then the DEO only gets notified who in turn shall resubmit the submission
//	If the M&E lead rejects a submission, then the Superintendent,unit in charge,
//	corresponding associate, lead and the DEO gets notified and the DEO in turn shall resubmit the data.
//	The AHI M&E lead shall be notified about the data which are auto-approved and which are the data approved by the superintendent of the facility. 
	@Override
	@Transactional
	public ReturnModel approveOrRejectBySuperintendentAndMnE(
			Integer txnSubmissionId, String remarks, boolean isApprove) {

		UserModel userModel=(UserModel)stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		ReturnModel model = new ReturnModel();
		String message = "";
		List<String> toEmailIds = new ArrayList<>();
		List<String> ccEmailIds = new ArrayList<>();
		String toUserName = "";
		String subject = "";
		String returnMessage = "";
		String facilityName = "";
		try {
			// first update the TXNSubmissionManagement
			//if superintendent is the logged in user this will return deo 
			//if m&e is the logged in user this will return superintendent
			TXNSubmissionManagement txnSubmissionManagement = tXNSubmissionManagementRepository
					.findByTxnSubmissionId(txnSubmissionId);
			
			
			//we will check if it is a latest pending record or not//
			//for server side validation
			if (txnSubmissionManagement.getIsLatest() && (txnSubmissionManagement.getStatusSuperintendent()
					.getTypeDetail().equals(messages.getMessage(Constants.Web.PENDING_STATUS_TYPE_NAME, null, null))
					|| txnSubmissionManagement.getStatusMne().getTypeDetail()
							.equals(messages.getMessage(Constants.Web.PENDING_STATUS_TYPE_NAME, null, null)))){
				
				txnSubmissionManagement.setIsLatest(false);
				tXNSubmissionManagementRepository.save(txnSubmissionManagement);

				// we are checking the role accordingly we will set
				// remarkBySuperintendent, remarkByMnE, statusSuperintendent,
				// statusMnE
				TXNSubmissionManagement latestTxnSubmissionManagement = new TXNSubmissionManagement();
				latestTxnSubmissionManagement.setFacility(txnSubmissionManagement
						.getFacility());
				latestTxnSubmissionManagement.setCreatedBy(userModel.getUsername());
				latestTxnSubmissionManagement.setCreatedDate(new Timestamp(
						new Date().getTime()));
				latestTxnSubmissionManagement.setIsLatest(true);
				latestTxnSubmissionManagement.setTimePeriod(txnSubmissionManagement
						.getTimePeriod());
				latestTxnSubmissionManagement.setIsWeb(true);
				
				String roleName = userModel.getUserAreaModels().get(0).
						getUserRoleFeaturePermissionMappings().get(0)
						.getRoleFeaturePermissionSchemeModel().getRole().getRoleName();
				
				Calendar curDate = Calendar.getInstance();
				int dayOfMonth = curDate.get(Calendar.DAY_OF_MONTH);
				Calendar lastDateOfSubmission = Calendar.getInstance();
				
				MSTUser deoUser = null;
				
				//for mne
				if (!roleName.equals(messages.getMessage(Constants.Web.SUPERINTENDENT_ROLE_NAME,null,null))) {
					// set ref id as superintendent's ref
					latestTxnSubmissionManagement
							.setRefSubmissionId(txnSubmissionManagement
									.getRefSubmissionId());
					latestTxnSubmissionManagement.setStatusSuperintendent(txnSubmissionManagement.getStatusSuperintendent());
					latestTxnSubmissionManagement.setRemarkBySuperintendent(txnSubmissionManagement.getRemarkBySuperintendent());
					latestTxnSubmissionManagement.setRemarkByMnE(remarks);
					latestTxnSubmissionManagement
							.setStatusMne(isApprove ? new TypeDetail(Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED,null,null)))
									: new TypeDetail(Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_REJECTED,null,null)))); 
					
					
					//reference id of superintendent is the deo submission id
					TXNSubmissionManagement deoSubmission = tXNSubmissionManagementRepository.findByTxnSubmissionId(txnSubmissionManagement
									.getRefSubmissionId());
					
					//set mail properties
					
					List<UserAreaMapping> userAreaMappings = userAreaMappingRepository.findByFacilityAreaId(deoSubmission.getFacility().getAreaId());
					
					//send mail in case of rejection only
					if(!isApprove){
						for(UserAreaMapping userAreaMapping : userAreaMappings){
							
							//do not add deo in cc
							if(userAreaMapping.getUser().getEmail()!=null && !userAreaMapping.getUser().getUserName().contains("qitm_"))
								ccEmailIds.add(userAreaMapping.getUser().getEmail());
							
							if(userAreaMapping.getUser().getUserName().contains("qitm_")){
								deoUser = userAreaMapping.getUser();
							}
							
							//ahi associate role id if 5
							if (userAreaMapping.getUserRoleFeaturePermissionMappings().get(0)
									.getRoleFeaturePermissionScheme().getRole().getRoleId() == 5
									&& userAreaMapping.getUser().getLead() != null){
									MSTUser leadUser = mstUserRepository.findByUserIdAndIsLiveTrue(userAreaMapping.getUser().getLead());
									
									//add lead user email
									if(leadUser.getEmail()!=null){
										ccEmailIds.add(leadUser.getEmail());
									}
									
							}
						}
						
						//if mne rejects the last date of approval is 25 
//						lastDateOfSubmission.set(Calendar.DATE, Integer
//								.parseInt(messages.getMessage(Constants.Web.MNE_APPROVAL_LASTDATE, null, null))-1);
						
						//if mne rejects the last date of approval is last day of the month
						lastDateOfSubmission.set(Calendar.DATE, java.time.LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth());
						
						toEmailIds = Arrays.asList(deoUser.getEmail());
						
						
						subject = "SCSL: Submission rejected by AHI M&E" + " ("
								+ deoSubmission.getTimePeriod().getTimePeriod() + ")";
						facilityName = deoSubmission.getFacility().getAreaName();
						message =  "The submission uploaded for " + facilityName
										+ " on " + simpleDateformater.format(deoSubmission.getCreatedDate())
										+ " for the month " + deoSubmission.getTimePeriod().getTimePeriod()
										+ " has been rejected by AHI M&E.<br /><br />"
										+"Remarks: "+remarks +"<br /><br />"
										+ "Please upload the revised data by "
										+ simpleDateformater.format(lastDateOfSubmission.getTime());
						toUserName = deoUser.getName();
						returnMessage = errorMessageSource.getMessage(Constants.Web.SUBMISSION_REJECTED_MESSAGE, null,
								null) + "<br /> An email notification to the respective user(s) of " + facilityName
								+ " has been sent successfully.";
					}else
						returnMessage = errorMessageSource.getMessage(Constants.Web.SUBMISSION_APPROVED_MESSAGE, null,null);
					
					
				} else { //for superintendent
					latestTxnSubmissionManagement.setRefSubmissionId(txnSubmissionId);
					latestTxnSubmissionManagement.setRemarkBySuperintendent(remarks);
					latestTxnSubmissionManagement.setStatusSuperintendent(isApprove
							? new TypeDetail(Integer.parseInt(
									messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED, null, null)))
							: new TypeDetail(Integer.parseInt(
									messages.getMessage(Constants.Web.SUBMISSION_STATUS_REJECTED, null, null))));
					latestTxnSubmissionManagement.setStatusMne(isApprove
							? new TypeDetail(Integer
									.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_PENDING, null, null)))
							: null);
					
					//set mail properties 
					//to-deo, cc-no one
					
					for(UserAreaMapping userAreaMapping : txnSubmissionManagement.getFacility().getUserAreaMappings()){
						
						if(userAreaMapping.getUser().getUserName().contains("qitm"))
							deoUser = userAreaMapping.getUser();
					}
					
					//before or on 20th show last day of submission as 20th else 25th
					if(dayOfMonth<Integer
							.parseInt(messages.getMessage(Constants.Web.SUPERINTENDENT_APPROVAL_LASTDATE, null, null)))
						lastDateOfSubmission.set(Calendar.DATE, Integer
								.parseInt(messages.getMessage(Constants.Web.SUPERINTENDENT_APPROVAL_LASTDATE, null, null))-1); 
					else
//						lastDateOfSubmission.set(Calendar.DATE, Integer
//								.parseInt(messages.getMessage(Constants.Web.MNE_APPROVAL_LASTDATE, null, null))-1);
						lastDateOfSubmission.set(Calendar.DATE, java.time.LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth());
					
					//in case of rejection send mail only to deo, in case of approval send mail to mne
					toEmailIds = !isApprove ? Arrays.asList(deoUser.getEmail())
							: Arrays.asList(userRoleFeaturePermissionMappingRepository
									.findByRoleFeaturePermissionSchemeRoleRoleName(
											messages.getMessage(Constants.Web.MNE_ROLE_NAME, null, null))
									.get(0).getUserAreaMapping().getUser().getEmail()); // put
																						// M&E
																						// user
																						// mail
																						// here;
					facilityName = txnSubmissionManagement.getFacility().getAreaName();
					subject = isApprove
							? "SCSL: Submission approved by superintendent" + " ("
									+ txnSubmissionManagement.getTimePeriod().getTimePeriod() + ")"
							: "SCSL: Submission rejected by superintendent" + " ("
									+ txnSubmissionManagement.getTimePeriod().getTimePeriod() + ")";
					message = isApprove
							? "The submission uploaded for " + facilityName
									+ " on " + simpleDateformater.format(txnSubmissionManagement.getCreatedDate())
									+ " for the month " + txnSubmissionManagement.getTimePeriod().getTimePeriod()
									+ " has been approved by superintendent.<br /><br />"
									+ (null != remarks ? ("Remarks: " + remarks) : "") + "<br /><br />"
							: "The submission uploaded for " + facilityName
									+ " on " + simpleDateformater.format(txnSubmissionManagement.getCreatedDate())
									+ " for the month " + txnSubmissionManagement.getTimePeriod().getTimePeriod()
									+ " has been rejected by superintendent.<br /><br />" + "Remarks: " + remarks + "<br /><br />"
									+ "Please upload the revised data by "
									+ simpleDateformater.format(lastDateOfSubmission.getTime());
									
					toUserName = !isApprove ? deoUser.getName()
							: userRoleFeaturePermissionMappingRepository
									.findByRoleFeaturePermissionSchemeRoleRoleName(
											messages.getMessage(Constants.Web.MNE_ROLE_NAME, null, null))
									.get(0).getUserAreaMapping().getUser().getName();
					
					returnMessage = !isApprove
							? errorMessageSource.getMessage(Constants.Web.SUBMISSION_REJECTED_MESSAGE, null, null)
									+ "<br /> An email notification to " + toUserName + " has been sent successfully."
							: errorMessageSource.getMessage(Constants.Web.SUBMISSION_APPROVED_MESSAGE, null, null)
									+ "<br /> An email notification to " + toUserName + " has been sent successfully.";
									
				}

				tXNSubmissionManagementRepository.save(latestTxnSubmissionManagement);
				
				//mail method starts here---
				
				try {
					
					//do not send mail for mne - approve
					if(!(!roleName.equals(messages.getMessage(Constants.Web.SUPERINTENDENT_ROLE_NAME, null, null)) && isApprove))
						sendMail(toEmailIds, ccEmailIds, subject, message, toUserName);
					
					model.setStatusCode(errorMessageSource.getMessage(
							Constants.Web.SUCCESS_STATUS_CODE, null, null));
					model.setStatusMessage(returnMessage);
					
					return model;
					
				} catch (Exception e) {
					LOGGER.error("message",e);
					model.setStatusCode(errorMessageSource.getMessage(Constants.Web.SUCCESS_STATUS_CODE, null, null));
					
					//if role is superintendent check for approve or reject
					//if mne, only in case of rejection mail will send
					model.setStatusMessage(
							roleName.equals(messages.getMessage(Constants.Web.SUPERINTENDENT_ROLE_NAME, null, null))
									? isApprove
											? errorMessageSource.getMessage(Constants.Web.SUBMISSION_APPROVED_MESSAGE,
													null, null) + " <br /> The email notification to " + toUserName
													+ " has failed."
											: errorMessageSource.getMessage(Constants.Web.SUBMISSION_REJECTED_MESSAGE,
													null, null) + " <br /> The email notification to " + toUserName
													+ " has failed."
									: errorMessageSource.getMessage(Constants.Web.SUBMISSION_REJECTED_MESSAGE, null,
											null) + " <br /> The email notification to the respective user(s) of " + facilityName + " has failed.");
					
					return model;
				}

			}else{
				model.setStatusMessage(errorMessageSource.getMessage(
						Constants.Web.SUBMISSION_OLD_MESSAGE, null, null));
				model.setStatusCode(errorMessageSource.getMessage(
						Constants.Web.FAILURE_STATUS_CODE, null, null));
				return model;
			}
			
			
		} catch (Exception e) {
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			model.setStatusMessage(errorMessageSource.getMessage(
					Constants.Web.SUBMISSION_ERROR_MESSAGE, null, null));
			model.setStatusCode(errorMessageSource.getMessage(
					Constants.Web.FAILURE_STATUS_CODE, null, null));
			return model;
		}

	}
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.SubmissionManagementService#sendMail(java.util.List, java.util.List, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMail(List<String> toEmailIds, List<String> ccEmailIds, String subject, String message, String toUserName) throws Exception{
		
		MailModel mailModel = new MailModel();
		
		mailModel.setToEmailIds(toEmailIds);

		mailModel.setCcEmailIds(ccEmailIds);


		mailModel.setFromUserName("SCSL Admin" + "<br><br><p style=" + "font-size:10px" + ">"
				+ notification.getMessage(Constants.Web.EMAIL_DISCLAIMER, null, null) + "</p>");
		mailModel.setMessage(message);
		mailModel.setSubject(subject);
		mailModel.setToUserName(toUserName);

		// send the mail
		mailService.sendMail(mailModel);
	}

	//if superintendent has not approved the submission then auto-approve it
	/* (non-Javadoc)
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @see org.sdrc.scsl.service.SubmissionManagementService#autoApproveForSuperintendent()
	 */
//	The AHI M&E lead shall be notified about the data which are auto-approved and which are the data approved by the superintendent of the facility. 
	@Override
	@Transactional
	public boolean autoApproveForSuperintendent() {
//		Calendar startDateCalendar = Calendar.getInstance();
//		startDateCalendar.add(Calendar.MONTH, -1);
//		startDateCalendar.set(Calendar.DATE, 1);
		Date date = Calendar.getInstance().getTime();

		TimePeriod timePeriod = timePeriodRepository.lastMonthTimeperiod();
		int statusPendingSupritendent = Integer.parseInt(messages.getMessage(
				Constants.Web.SUBMISSION_STATUS_PENDING, null, null));
		List<TXNSubmissionManagement> submissionManagements = tXNSubmissionManagementRepository
				.findByIsLatestTrueAndTimePeriodTimePeriodIdAndStatusSuperintendentTypeDetailId(
						timePeriod.getTimePeriodId(), statusPendingSupritendent);

		List<TXNSubmissionManagement> submissionManagementsNew = new ArrayList<>();

		String message = "";
		List<String> toEmailIds;
		String subject = "";
		String facilityDetails = "";
		
		List<Area> areas = areaRepository.findAll();
		Map<Integer, Area> areaIdNameMap = new HashMap<>(); 
		// inner map contains area parent id and area name
		for (Area area : areas) {
			areaIdNameMap.put(area.getAreaId(), area);
		}
		
		int count = 1;
		for (TXNSubmissionManagement submissionManagement : submissionManagements) {
			TXNSubmissionManagement txnSubmissionManagement = new TXNSubmissionManagement();
			txnSubmissionManagement
					.setCreatedDate(new Timestamp(date.getTime()));
			txnSubmissionManagement.setStatusSuperintendent(new TypeDetail(
					Integer.parseInt(messages.getMessage(
							Constants.Web.SUBMISSION_STATUS_AUTO_APPROVED,
							null, null))));
			txnSubmissionManagement.setStatusMne(new TypeDetail(
					statusPendingSupritendent));
			txnSubmissionManagement.setCreatedBy(messages.getMessage(
					Constants.Web.SUBMISSION_AUTO_APPROVED_CREATED_BY, null,
					null));
			txnSubmissionManagement.setFacility(submissionManagement.getFacility());
			txnSubmissionManagement.setTimePeriod(submissionManagement.getTimePeriod());
			txnSubmissionManagement.setIsLatest(true);
			txnSubmissionManagement.setRefSubmissionId(submissionManagement.getTxnSubmissionId());
			txnSubmissionManagement.setIsWeb(true);
			submissionManagement.setIsLatest(false);

			submissionManagementsNew.add(txnSubmissionManagement);
			submissionManagementsNew.add(submissionManagement);
			
			//sl no. facility name, -district name (uploaded on )
			facilityDetails = facilityDetails+"<br />"+ count+". "+ submissionManagement.getFacility().getAreaName()+ ", " +
					areaIdNameMap.get(submissionManagement.getFacility().getParentAreaId()).getAreaName()+
					" ("+simpleDateformater.format(submissionManagement.getCreatedDate())+")"
					; //we are storing this for further mail sending 
			count ++;
		}

		tXNSubmissionManagementRepository.save(submissionManagementsNew);
		
		//if there is at least 1 pending then only send mail
		if(!submissionManagements.isEmpty()){
			toEmailIds = Arrays.asList(userRoleFeaturePermissionMappingRepository
					.findByRoleFeaturePermissionSchemeRoleRoleName(
							messages.getMessage(Constants.Web.MNE_ROLE_NAME, null, null))
					.get(0).getUserAreaMapping().getUser().getEmail()); // put M&E
																		// user mail
																		// here
			subject = "SCSL: Submissions auto-approved";
			message = "Please find below the details of facilities where the submissions were auto-approved for the month "
					+ timePeriod.getTimePeriod()+"." + "<br />" + facilityDetails;
			
			
			try {
				sendMail(toEmailIds, null, subject, message, "AHI M&E");
			} catch (Exception e) {
				LOGGER.error("message",e);
			}
		}
		
		
		return true;
	}



}
