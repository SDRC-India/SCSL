package org.sdrc.scsl.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.Indicator;
import org.sdrc.scsl.domain.IndicatorFacilityTimeperiodMapping;
import org.sdrc.scsl.domain.MSTEngagementScore;
import org.sdrc.scsl.domain.MSTUser;
import org.sdrc.scsl.domain.Role;
import org.sdrc.scsl.domain.RoleFeaturePermissionScheme;
import org.sdrc.scsl.domain.SYSConfiguration;
import org.sdrc.scsl.domain.TXNEngagementScore;
import org.sdrc.scsl.domain.TXNSNCUData;
import org.sdrc.scsl.domain.TXNSubmissionManagement;
import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.domain.Type;
import org.sdrc.scsl.domain.TypeDetail;
import org.sdrc.scsl.domain.UserAreaMapping;
import org.sdrc.scsl.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.scsl.model.mobile.AreaModel;
import org.sdrc.scsl.model.mobile.IndicatorFacilityTimeperiodMappingModel;
import org.sdrc.scsl.model.mobile.IndicatorModel;
import org.sdrc.scsl.model.mobile.LoginDataModel;
import org.sdrc.scsl.model.mobile.MSTEngagementScoreModel;
import org.sdrc.scsl.model.mobile.MasterDataModel;
import org.sdrc.scsl.model.mobile.SyncModel;
import org.sdrc.scsl.model.mobile.SyncResult;
import org.sdrc.scsl.model.mobile.TXNEngagementScoreModel;
import org.sdrc.scsl.model.mobile.TXNSNCUDataModel;
import org.sdrc.scsl.model.mobile.TimePeriodModel;
import org.sdrc.scsl.model.mobile.TypeDetailModel;
import org.sdrc.scsl.model.mobile.TypeModel;
import org.sdrc.scsl.model.mobile.UserModel;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.IndicatorFacilityTimeperiodMappingRepository;
import org.sdrc.scsl.repository.IndicatorRepository;
import org.sdrc.scsl.repository.MSTEngagementScoreRepository;
import org.sdrc.scsl.repository.MSTUserRepository;
import org.sdrc.scsl.repository.SYSConfigurationRepository;
import org.sdrc.scsl.repository.TXNEngagementScoreRepository;
import org.sdrc.scsl.repository.TXNSNCUDataRepository;
import org.sdrc.scsl.repository.TXNSubmissionManagementRepository;
import org.sdrc.scsl.repository.TimePeriodRepository;
import org.sdrc.scsl.repository.TypeDetailRepository;
import org.sdrc.scsl.repository.TypeRepository;
import org.sdrc.scsl.repository.UserAreaMappingRepository;
import org.sdrc.scsl.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 4:28:36 pm
 * @author subhadarshani Patra (subhadarshani@sdrc.co.in)
 */
@Service
public class MobileServiceImpl implements MobileService {

	private static final Logger logger = LoggerFactory
			.getLogger(MobileServiceImpl.class);

	@Autowired
	private ResourceBundleMessageSource messages;

	@Autowired
	private MSTUserRepository mSTUserRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private IndicatorRepository indicatorRepository;

	@Autowired
	private TypeRepository typeRepository;

	@Autowired
	private TypeDetailRepository typeDetailRepository;

	@Autowired
	private UserAreaMappingRepository userAreaMappingRepository;

	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private MSTEngagementScoreRepository mSTEngagementScoreRepository;

	@Autowired
	private IndicatorFacilityTimeperiodMappingRepository indicatorFacilityTimeperiodMappingRepository;

	@Autowired
	private TXNSNCUDataRepository txnsncuDataRepository;

	@Autowired
	private TXNEngagementScoreRepository txnEngagementScoreRepository;

	@Autowired
	private TXNSubmissionManagementRepository txnSubmissionManagementRepository;

	@Autowired
	private MessageDigestPasswordEncoder passwordEncoder;

	@Autowired
	private SYSConfigurationRepository sysConfigurationRepository;

	private SimpleDateFormat sdf = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.DATE_MONTH_YEAR_SIMPLEDATE_FORMATTER));
	private SimpleDateFormat sdfFull = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss Z");

	@Override
	public MasterDataModel getMasterData(LoginDataModel loginDataModel) {

		MasterDataModel masterDataModel = new MasterDataModel();
		try {

			// checking for null value
			if (loginDataModel != null) {
				// loginData is not null, going to fetch the user
				logger.info("Request has come for login. Username : "
						+ loginDataModel.getUsername() + " passowrd string: "
						+ loginDataModel.getPassword());

				// encode password
				String encodedPassword = passwordEncoder.encodePassword(
						loginDataModel.getUsername(),
						loginDataModel.getPassword());

				//
				MSTUser mSTuser = mSTUserRepository.findByUsernameAndPassword(
						loginDataModel.getUsername(), encodedPassword);

				// checking for null
				if (mSTuser != null) {

					// Getting user area mapping
					List<UserAreaMapping> userAreaMappings = userAreaMappingRepository
							.findByUserUserIdAndIsLiveTrue(mSTuser.getUserId()
									.intValue());
					if (userAreaMappings.size() > 0) {

						UserAreaMapping userAreaMapping = userAreaMappings
								.get(0);
						List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings = userAreaMapping
								.getUserRoleFeaturePermissionMappings();

						if (userRoleFeaturePermissionMappings.size() > 0) {
							UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping = userRoleFeaturePermissionMappings
									.get(0);
							RoleFeaturePermissionScheme roleFeaturePermissionScheme = userRoleFeaturePermissionMapping
									.getRoleFeaturePermissionScheme();
							Role role = roleFeaturePermissionScheme.getRole();

							// We got the user and it's mapping
							// We need to give following data
							// 1. Area list
							// 2. Indicators
							// 3. Types
							// 4. Type details
							// 5. Time periods
							// 6. User detail
							// 7. Engagement score master data
							// 8. IndicatorFacilityTimeperiodMapping

							// if(loginDataModel.getLastSyncDate() == null)

							Date lastSyncDate = loginDataModel
									.getLastSyncDate() != null ? sdfFull
									.parse(loginDataModel.getLastSyncDate())
									: null;

							UserModel userModel = new UserModel();
							userModel.setName(mSTuser.getName());

							if (role.getRoleCode().equals(
									messages.getMessage(
											Constants.Mobile.DEO_ROLE_CODE,
											null, null))) {
								userModel.setIsDEO(true);
							} else if (role
									.getRoleCode()
									.equals(messages
											.getMessage(
													Constants.Mobile.AHI_ASSOCIATE_ROLE_CODE,
													null, null))) {
								userModel.setIsDEO(false);
							} else {
								// bad user type, only deo and AHI associate are
								// allowed to use mobile data entry
								logger.warn("Role is not for mobile data entry for Username : "
										+ loginDataModel.getUsername()
										+ " passowrd string: "
										+ loginDataModel.getPassword());
								masterDataModel.setErrorCode(1);
								masterDataModel
										.setErrorMessage(messages
												.getMessage(
														Constants.Mobile.INVALID_USER_TYPE_LOGIN,
														null, null));
								return masterDataModel;
							}

							List<Integer> areaIds = new ArrayList<>();
							for (UserAreaMapping userAreaMappingLocal : userAreaMappings) {
								areaIds.add(userAreaMappingLocal.getFacility()
										.getAreaId().intValue());
							}
							userModel.setAreaIds(areaIds);
							masterDataModel.setUserModel(userModel);

							// Area list
							// We have to make sure that should we need to send
							// all the areas to mobile or some areas
							List<Area> areas = new ArrayList<>();
							if (lastSyncDate == null) {
								areas = areaRepository.findAll();
							} else {
								areas = areaRepository
										.findByLastSyncDate(lastSyncDate);
							}
							List<AreaModel> areaModels = new ArrayList<>();

							for (Area area : areas) {

								AreaModel areaModel = new AreaModel();
								areaModel
										.setAreaId(area.getAreaId() != null ? area
												.getAreaId().intValue() : 0);
								areaModel.setAreaName(area.getAreaName());
								areaModel.setFacilitySize(area
										.getFacilitySize() != null ? area
										.getFacilitySize().getTypeDetailId()
										: 0);
								areaModel.setFacilityType(area
										.getFacilityType() != null ? area
										.getFacilityType().getTypeDetailId()
										: 0);
								areaModel.setLevel(area.getLevel());
								areaModel.setParentAreaId(area
										.getParentAreaId());
								areaModel.setWave(area.getWave() != null ? area
										.getWave().intValue() : 0);
								areaModel
										.setHasLR(area.getHasLr() != null ? area
												.getHasLr() : false);
								areaModels.add(areaModel);
							}
							masterDataModel.setAreaModels(areaModels);

							// Indicators
							List<Indicator> indicators = new ArrayList<>();
							if (lastSyncDate == null) {
								indicators = indicatorRepository.findAll();
							} else {
								indicators = indicatorRepository
										.findByLastSyncDate(lastSyncDate);
							}
							List<IndicatorModel> indicatorModels = new ArrayList<>();
							for (Indicator indicator : indicators) {

								IndicatorModel indicatorModel = new IndicatorModel();
								indicatorModel.setCoreArea(indicator
										.getCoreArea() != null ? indicator
										.getCoreArea().getTypeDetailId()
										.intValue() : 0);
								indicatorModel.setDenominator(indicator
										.getDenominator() != null ? indicator
										.getDenominator() : null);
								indicatorModel.setExceptionRule(indicator
										.getExceptionRule() != null ? indicator
										.getExceptionRule() : null);
								indicatorModel.setIndicatorId(indicator
										.getIndicatorId() != null ? indicator
										.getIndicatorId() : null);
								indicatorModel.setIndicatorName(indicator
										.getIndicatorName() != null ? indicator
										.getIndicatorName() : null);
								indicatorModel.setIndicatorType(indicator
										.getIndicatorType() != null ? indicator
										.getIndicatorType().getTypeDetailId()
										.intValue() : null);
								// indicatorModel.setIntermediateIndicatorId(indicator.getIntermediateIndicatorId()
								// != null ?
								// indicator.getIntermediateIndicatorId().intValue()
								// : 0);
								indicatorModel.setIsReqired(indicator
										.getIsReqired() != null ? indicator
										.getIsReqired() : null);
								indicatorModel.setNumerator(indicator
										.getNumerator() != null ? indicator
										.getNumerator() : null);
								// indicatorModel.setOutcomeIndicatorId(indicator.getOutcomeIndicatorId()
								// != null ?
								// indicator.getOutcomeIndicatorId().intValue()
								// : 0);
								// indicatorModel.setProcessIndicatorId(indicator.getProcessIndicatorId()
								// != null ?
								// indicator.getProcessIndicatorId().intValue()
								// : 0);
								indicatorModel
										.setIndicatorOrder(indicator
												.getIndicatorOrder() != null ? indicator
												.getIndicatorOrder() : null);
								indicatorModel
										.setIsLr(indicator.getIsLr() != null ? indicator
												.getIsLr() : false);
								indicatorModel.setIsProfile(indicator
										.getIsProfile() != null ? indicator
										.getIsProfile() : false);
								indicatorModel.setRelatedIndicatorIds(indicator
										.getRelatedIndicatorIds());

								indicatorModels.add(indicatorModel);
							}
							masterDataModel.setIndicatorModels(indicatorModels);

							// Types
							List<Type> types = new ArrayList<>();
							if (lastSyncDate == null) {
								types = typeRepository.findAll();
							} else {
								types = typeRepository
										.findByLastSyncDate(lastSyncDate);
							}
							List<TypeModel> typeModels = new ArrayList<>();

							for (Type type : types) {
								TypeModel typeModel = new TypeModel();

								typeModel
										.setTypeId(type.getTypeId().intValue());
								typeModel.setTypeName(type.getTypeName());
								typeModel.setDescription(type.getDescription());

								typeModels.add(typeModel);
							}

							masterDataModel.setTypeModels(typeModels);

							// Type details
							List<TypeDetail> typeDetails = new ArrayList<>();
							if (lastSyncDate == null) {
								typeDetails = typeDetailRepository.findAll();
							} else {
								typeDetails = typeDetailRepository
										.findByLastSyncDate(lastSyncDate);
							}
							List<TypeDetailModel> typeDetailModels = new ArrayList<>();
							for (TypeDetail typeDetail : typeDetails) {
								TypeDetailModel typeDetailModel = new TypeDetailModel();
								typeDetailModel.setTypeDetailId(typeDetail
										.getTypeDetailId().intValue());
								typeDetailModel.setTypeDetail(typeDetail
										.getTypeDetail());
								typeDetailModel.setDescription(typeDetail
										.getDescription());
								typeDetailModel.setTypeId(typeDetail.getType()
										.getTypeId());
								typeDetailModels.add(typeDetailModel);
							}

							masterDataModel
									.setTypeDetailModels(typeDetailModels);

							// Time periods
							List<TimePeriod> timePeriods = new ArrayList<>();
							if (lastSyncDate == null) {
								// timePeriods = timePeriodRepository.findAll();
								timePeriods = timePeriodRepository
										.findAllByPeriodicityOrderByTimePeriodIdAsc(messages
												.getMessage(
														Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY,
														null, null)); // updated
																		// by
																		// sarita.
																		// as
																		// finally
																		// returns
																		// quarterly
																		// and
																		// yearly
																		// tps
																		// also
							} else {
								timePeriods = timePeriodRepository
										.findByLastSyncDate(lastSyncDate);
							}
							List<TimePeriodModel> timePeriodModels = new ArrayList<>();
							for (TimePeriod timePeriod : timePeriods) {

								TimePeriodModel timePeriodModel = new TimePeriodModel();
								timePeriodModel.setTimePeriodId(timePeriod
										.getTimePeriodId().intValue());
								timePeriodModel.setTimePeriod(timePeriod
										.getTimePeriod());
								timePeriodModel.setWave(timePeriod.getWave());
								timePeriodModel.setStartDate(timePeriod
										.getStartDate() != null ? sdf
										.format(timePeriod.getStartDate())
										: null);
								timePeriodModel
										.setEndDate(timePeriod.getEndDate() != null ? sdf
												.format(timePeriod.getEndDate())
												: null);
								timePeriodModel.setPeriodicity(timePeriod
										.getPeriodicity());
								timePeriodModels.add(timePeriodModel);

							}

							masterDataModel
									.setTimePeriodModels(timePeriodModels);

							// Engagement score master data
							List<MSTEngagementScore> mSTEngagementScores = new ArrayList<>();
							if (lastSyncDate == null) {
								mSTEngagementScores = mSTEngagementScoreRepository
										.findAll();
							} else {
								mSTEngagementScores = mSTEngagementScoreRepository
										.findByLastSyncDate(lastSyncDate);
							}
							List<MSTEngagementScoreModel> mSTEngagementScoreModels = new ArrayList<>();
							for (MSTEngagementScore mSTEngagementScore : mSTEngagementScores) {

								MSTEngagementScoreModel mSTEngagementScoreModel = new MSTEngagementScoreModel();
								mSTEngagementScoreModel
										.setMstEngagementScoreId(mSTEngagementScore
												.getMstEngagementScoreId()
												.intValue());
								mSTEngagementScoreModel
										.setProgress(mSTEngagementScore
												.getProgress());
								mSTEngagementScoreModel
										.setDefinition(mSTEngagementScore
												.getDefinition());
								mSTEngagementScoreModel
										.setScore(mSTEngagementScore.getScore());
								mSTEngagementScoreModels
										.add(mSTEngagementScoreModel);

							}
							masterDataModel
									.setmSTEngagementScoreModels(mSTEngagementScoreModels);

							// IndicatorFacilityTimeperiodMapping
							List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings = new ArrayList<>();
							TimePeriod timePeriod = timePeriodRepository
									.lastMonthTimeperiod();
							indicatorFacilityTimeperiodMappings = indicatorFacilityTimeperiodMappingRepository
									.findByAreaIdAndTimePeriodIsLiveTrue(
											userAreaMapping.getFacility()
													.getAreaId(),
											timePeriod != null ? timePeriod
													.getTimePeriodId() : 0);
							List<IndicatorFacilityTimeperiodMappingModel> indicatorFacilityTimeperiodMappingModels = new ArrayList<>();
							for (IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping : indicatorFacilityTimeperiodMappings) {
								IndicatorFacilityTimeperiodMappingModel indicatorFacilityTimeperiodMappingModel = new IndicatorFacilityTimeperiodMappingModel();
								indicatorFacilityTimeperiodMappingModel
										.setIndFacilityTpId(indicatorFacilityTimeperiodMapping
												.getIndFacilityTpId()
												.intValue());
								indicatorFacilityTimeperiodMappingModel
										.setFacilityId(indicatorFacilityTimeperiodMapping
												.getFacility().getAreaId()
												.intValue());
								indicatorFacilityTimeperiodMappingModel
										.setIndicatorId(indicatorFacilityTimeperiodMapping
												.getIndicator()
												.getIndicatorId().intValue());
								indicatorFacilityTimeperiodMappingModel
										.setTimePeriodId(indicatorFacilityTimeperiodMapping
												.getTimePeriod()
												.getTimePeriodId().intValue());
								indicatorFacilityTimeperiodMappingModel
										.setCreatedDate(indicatorFacilityTimeperiodMapping
												.getCreatedDate() != null ? sdfFull
												.format(indicatorFacilityTimeperiodMapping
														.getCreatedDate())
												: null);
								indicatorFacilityTimeperiodMappingModels
										.add(indicatorFacilityTimeperiodMappingModel);
							}
							masterDataModel
									.setIndicatorFacilityTimeperiodMappingModels(indicatorFacilityTimeperiodMappingModels);

							masterDataModel
									.setDeoDeadLine(Integer.parseInt(messages
											.getMessage(
													Constants.Mobile.SUBMISSION_DEADLINE_DATE,
													null, null)));
							masterDataModel
									.setSubDeadLine(Integer.parseInt(messages
											.getMessage(
													Constants.Mobile.REJECTION_SUBMISSION_DEADLINE_DATE_BY_SUP,
													null, null)));
							// masterDataModel.setMneDeadLine(Integer.parseInt(messages.getMessage(Constants.Mobile.REJECTION_SUBMISSION_DEADLINE_DATE_BY_MNE,
							// null, null)));
							masterDataModel.setMneDeadLine(LocalDate.now()
									.with(TemporalAdjusters.lastDayOfMonth())
									.getDayOfMonth());
							masterDataModel.setLastSyncDate(sdfFull
									.format(new Date()));
							return masterDataModel;

						} else {
							// no role mapped to the user
							logger.warn("No role mapped for Username : "
									+ loginDataModel.getUsername()
									+ " passowrd string: "
									+ loginDataModel.getPassword());
							masterDataModel.setErrorCode(1);
							masterDataModel
									.setErrorMessage(messages
											.getMessage(
													Constants.Mobile.INVALID_USER_ROLE_MAPPING,
													null, null));
							return masterDataModel;
						}

					} else {
						// size 0, no area mapping to this user
						logger.warn("No area mapping found for Username : "
								+ loginDataModel.getUsername()
								+ " passowrd string: "
								+ loginDataModel.getPassword());
						masterDataModel.setErrorCode(1);
						masterDataModel.setErrorMessage(messages.getMessage(
								Constants.Mobile.INVALID_USER_AREA_MAPPING,
								null, null));
						return masterDataModel;
					}

				} else {
					// No user found in given credentials

					logger.warn("No user found for Username : "
							+ loginDataModel.getUsername()
							+ " passowrd string: "
							+ loginDataModel.getPassword());
					masterDataModel.setErrorCode(1);
					masterDataModel.setErrorMessage(messages.getMessage(
							Constants.Mobile.INVALID_CREDENTIALS, null, null));
					return masterDataModel;
				}

			} else {
				// login data is null, going to send error invalid username and
				// password

				logger.warn("Request has come for login. LoginData is null");
				masterDataModel.setErrorCode(1);
				masterDataModel.setErrorMessage(messages.getMessage(
						Constants.Mobile.INVALID_CREDENTIALS, null, null));
				return masterDataModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception : " + e.getMessage());
			masterDataModel.setErrorCode(1);
			masterDataModel.setErrorMessage("Server error : " + e.getMessage());
			return masterDataModel;
		}
	}

	@Override
	public synchronized SyncResult sync(SyncModel syncModel) {
		// verify user
		SyncResult syncResult = new SyncResult();
		try {
			// checking for null value
			if (syncModel != null && syncModel.getLoginDataModel() != null
					&& syncModel.getLoginDataModel().getLastSyncDate() != null) {
				LoginDataModel loginDataModel = syncModel.getLoginDataModel();

				// checking app version
				// if latest version will not match with the mobile app version,
				// we will return error.
				// checking whether we have got the app version from mobile or
				// not
				String apiVersion = syncModel.getApiVersion();
				if (apiVersion == null) {

					syncResult
							.setErrorCode(Integer.parseInt(messages
									.getMessage(
											Constants.Mobile.API_VERSION_MISMATCH_ERROR_CODE,
											null, null)));
					logger.warn("App version is missing in sync request");
					syncResult.setErrorMessage(messages.getMessage(
							Constants.Mobile.API_VERSION_MISMATCH_ERROR_MESAGE,
							null, null));
					return syncResult;
				}

				List<SYSConfiguration> sysConfigurationList = sysConfigurationRepository.findAll();
				if (sysConfigurationList.isEmpty()||( !sysConfigurationList.isEmpty()&& sysConfigurationList.get(0)== null)) {
					logger.warn("Sys configuration is missing in sync request");
					syncResult
							.setErrorCode(Integer.parseInt(messages
									.getMessage(
											Constants.Mobile.SYSCONFIG_RETURN_ZERO_OBJECT_ERROR_CODE,
											null, null)));
					syncResult
							.setErrorMessage(messages
									.getMessage(
											Constants.Mobile.SYSCONFIG_RETURN_ZERO_OBJECT_ERROR_MESSAGE,
											null, null));
					return syncResult;
				}

				String currentAppVersion = sysConfigurationList.get(0).getApiVersion();
				if (currentAppVersion == null) {
					syncResult
							.setErrorCode(Integer.parseInt(messages
									.getMessage(
											Constants.Mobile.API_VERSION_MISSING_IN_SYSCONFIG_ERROR_CODE,
											null, null)));
					logger.warn("App version is missing in sync request");
					syncResult.setErrorMessage(messages.getMessage(
							Constants.Mobile.API_VERSION_MISSING_IN_SYSCONFIG_MESAGE,
							null, null));
					return syncResult;
				}
				if (!currentAppVersion.equals(apiVersion)) {
					syncResult
							.setErrorCode(Integer.parseInt(messages
									.getMessage(
											Constants.Mobile.API_VERSION_MISMATCH_ERROR_CODE,
											null, null)));
					logger.warn("App version is missing in sync request");
					syncResult.setErrorMessage(messages.getMessage(
							Constants.Mobile.API_VERSION_MISMATCH_ERROR_MESAGE,
							null, null));
					return syncResult;
				}
				// loginData is not null, going to fetch the user
				logger.info("Request has come for sync. Username : "
						+ loginDataModel.getUsername() + " passowrd string: "
						+ loginDataModel.getPassword());
				// encode password
				String encodedPassword = passwordEncoder.encodePassword(
						loginDataModel.getUsername(),
						loginDataModel.getPassword());

				//
				MSTUser mSTuser = mSTUserRepository.findByUsernameAndPassword(
						loginDataModel.getUsername(), encodedPassword);
				// checking for null
				if (mSTuser != null) {
					// send master data if any
					MasterDataModel masterDataModel = new MasterDataModel();
					// Getting user area mapping
					List<UserAreaMapping> userAreaMappings = userAreaMappingRepository
							.findByUserUserIdAndIsLiveTrue(mSTuser.getUserId()
									.intValue());
					if (userAreaMappings.size() > 0) {

						UserAreaMapping userAreaMapping = userAreaMappings
								.get(0);
						List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings = userAreaMapping
								.getUserRoleFeaturePermissionMappings();

						if (userRoleFeaturePermissionMappings.size() > 0) {
							UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping = userRoleFeaturePermissionMappings
									.get(0);
							RoleFeaturePermissionScheme roleFeaturePermissionScheme = userRoleFeaturePermissionMapping
									.getRoleFeaturePermissionScheme();
							Role role = roleFeaturePermissionScheme.getRole();
							Date lastSyncDate = sdfFull.parse(loginDataModel
									.getLastSyncDate());
							UserModel userModel = new UserModel();
							userModel.setName(mSTuser.getName());

							if (role.getRoleCode().equals(
									messages.getMessage(
											Constants.Mobile.DEO_ROLE_CODE,
											null, null))) {
								userModel.setIsDEO(true);
							} else if (role
									.getRoleCode()
									.equals(messages
											.getMessage(
													Constants.Mobile.AHI_ASSOCIATE_ROLE_CODE,
													null, null))) {
								userModel.setIsDEO(false);
							} else {
								// bad user type, only deo and AHI associate are
								// allowed to use mobile data entry
								logger.warn("Role is not for mobile data entry for Username : "
										+ loginDataModel.getUsername()
										+ " passowrd string: "
										+ loginDataModel.getPassword());
								syncResult.setErrorCode(1);
								syncResult
										.setErrorMessage(messages
												.getMessage(
														Constants.Mobile.INVALID_USER_TYPE_LOGIN,
														null, null));
								return syncResult;
							}
							// save transactional data
							// If deo is sending the data vo need to do
							// engagement score wor,
							// If AHI Associate is sending data no need to do
							// SNCU work
							// save sncu data
							TimePeriod timePeriod = timePeriodRepository
									.lastMonthTimeperiod();
							if (syncModel.getDeo() == 1) {
								List<TXNSNCUData> datas = txnsncuDataRepository
										.findByTimePeriodIdAndAreaId(
												timePeriod.getTimePeriodId(),
												userAreaMapping.getFacility()
														.getAreaId().intValue());

								// Rejection update work start
							
								int rejectionTypeDetailId = Integer
										.parseInt(messages
												.getMessage(
														Constants.Web.SUBMISSION_STATUS_REJECTED,
														null, null));
								int approveTypeDetailId = Integer
										.parseInt(messages
												.getMessage(
														Constants.Web.SUBMISSION_STATUS_APPROVED,
														null, null));
								int autoApproveTypeDetailId = Integer
										.parseInt(messages
												.getMessage(
														Constants.Web.SUBMISSION_STATUS_AUTO_APPROVED,
														null, null));

								// if(today.get(Calendar.DAY_OF_MONTH) <=
								// sup_deadline)
								{

									// Let's check there is data present for
									// this time period and area or not
									if (datas.size() > 0) {
										// There is data for this time period
										// and area
										TXNSNCUData data = datas.get(0);
										TXNSubmissionManagement submissionManagement = data
												.getTxnSubmissionManagement();

										// edited by harsh
										// ===============
										// checking if the submission management
										// data is latest or not
										// if not latest then we will bring
										// latest one from db using ref
										// submission id as txnsubmission id
										// will be
										// refsubmssion Id for other record
										// other submission
										if (submissionManagement != null
												&& !submissionManagement
														.getIsLatest()) {
											int refSubmissionId = submissionManagement
													.getTxnSubmissionId();
											submissionManagement = new TXNSubmissionManagement();
											submissionManagement = txnSubmissionManagementRepository
													.findByIsLatestTrueAndRefSubmissionId(refSubmissionId);
										}

										// ================
										// Submission management could be null
										if (submissionManagement != null) {
											// Wow it is not null
											TypeDetail detail = submissionManagement
													.getStatusSuperintendent();
											// type detail could be null
											if (detail != null) {
												// Wow it is not null
												int detailId = detail
														.getTypeDetailId()
														.intValue();
												syncResult
														.setRejectedDate(sdf
																.format(submissionManagement
																		.getCreatedDate()));
												if (detailId == rejectionTypeDetailId) {
													syncResult
															.setRejectedBySup(1);
													syncResult
															.setRemarkSup(submissionManagement
																	.getRemarkBySuperintendent());
												} else if (detailId == approveTypeDetailId) {
													syncResult
															.setAprovedBySup(1);
													// new subha
													syncResult
															.setApproveByMne(0);
													syncResult
															.setRemarkSup(submissionManagement
																	.getRemarkBySuperintendent());
												}

												else if (detailId == autoApproveTypeDetailId) {
													syncResult
															.setAutoApproved(1);
													syncResult.setRemarkSup("");
												}

											}
										}
									}
								}
								// Let's do it for MNE
								// int mne_deadline =
								// Integer.parseInt(messages.getMessage(
								// Constants.Mobile.REJECTION_SUBMISSION_DEADLINE_DATE_BY_MNE,
								// null, null));
								
								// if(today.get(Calendar.DAY_OF_MONTH) <=
								// mne_deadline)
								{

									// Let's check there is data present for
									// this time period and area or not
									if (datas.size() > 0) {
										// There is data for this time period
										// and area
										TXNSNCUData data = datas.get(0);
										TXNSubmissionManagement submissionManagement = data
												.getTxnSubmissionManagement();
										// Submission management could be null
										if (submissionManagement != null) {
											// Wow it is not null
											syncResult
													.setSubmittedDate(sdf
															.format(submissionManagement
																	.getCreatedDate()));
											// checking whether the submission
											// Management is latest or not
											if (!submissionManagement
													.getIsLatest()) {
												// oops its not latest.So we
												// will bring latest one
												int refSubmissionId = submissionManagement
														.getTxnSubmissionId();
												submissionManagement = new TXNSubmissionManagement();
												// we got latest one by taking
												// old submissionId as its
												// refSubmission Id
												// Yo we done it
												submissionManagement = txnSubmissionManagementRepository
														.findByIsLatestTrueAndRefSubmissionId(refSubmissionId);
											}
											TypeDetail detail = submissionManagement
													.getStatusMne();
											// type detail could be null
											if (detail != null) {
												// Wow it is not null
												int detailId = detail
														.getTypeDetailId()
														.intValue();
												syncResult
														.setRejectedDate(sdf
																.format(submissionManagement
																		.getCreatedDate()));
												if (detailId == rejectionTypeDetailId) {
													syncResult
															.setRejectedByMne(1);
													syncResult
															.setAprovedBySup(0);
													syncResult
															.setRemarkMne(submissionManagement
																	.getRemarkByMnE());

												} else if (detailId == approveTypeDetailId) {
													syncResult
															.setApproveByMne(1);
													// new subha
													syncResult
															.setAprovedBySup(0);
													if (submissionManagement
															.getRemarkByMnE() != null) {
														syncResult
																.setRemarkMne(submissionManagement
																		.getRemarkByMnE());
													}
												}

											}
										}
									}
								}
								// Rejection update work start

								List<TXNSNCUDataModel> txnsncuDataModels = syncModel
										.getTxnsncuDataModels();
								Map<Integer, IndicatorFacilityTimeperiodMapping> newIftmpMap = new HashMap<Integer, IndicatorFacilityTimeperiodMapping>();
								if (syncModel.getMappingModels() != null
										&& syncModel.getMappingModels().size() > 0) {
									List<IndicatorFacilityTimeperiodMapping> mappings = indicatorFacilityTimeperiodMappingRepository
											.findByAreaIdAndTimePeriodIsLiveTrue(
													userAreaMapping
															.getFacility()
															.getAreaId()
															.intValue(),
													timePeriod
															.getTimePeriodId());
									List<IndicatorFacilityTimeperiodMappingModel> list = syncModel
											.getMappingModels();
									List<IndicatorFacilityTimeperiodMappingModel> rejectList = new ArrayList<>();
									for (IndicatorFacilityTimeperiodMapping mapping : mappings) {
										for (int i = 0; i < list.size(); i++) {
											IndicatorFacilityTimeperiodMappingModel model = list
													.get(i);
											if (model.getIndicatorId() == mapping
													.getIndicator()
													.getIndicatorId()
													.intValue()) {
												txnsncuDataModels = checkAndUpdateTXNSNCU(
														txnsncuDataModels,
														model.getIndFacilityTpId(),
														mapping.getIndFacilityTpId());
												model.setRejectMessage(messages
														.getMessage(
																Constants.Mobile.IFTM_PRESENT,
																null, null));
												rejectList.add(model);
												list.remove(model);
												i--;
											}
										}
									}
									syncResult.setMappingModels(rejectList);

									mappings = new ArrayList<>();

									if (list.size() > 0) {
										for (IndicatorFacilityTimeperiodMappingModel model : list) {
											IndicatorFacilityTimeperiodMapping mapping = new IndicatorFacilityTimeperiodMapping();
											mapping.setFacility(userAreaMapping
													.getFacility());
											mapping.setIndicator(indicatorRepository
													.findByIndicatorId(model
															.getIndicatorId()));
											mapping.setTimePeriod(timePeriod);
											mapping.setIsLive(true);
											mapping.setCreatedDate(model
													.getCreatedDate() != null ? new Timestamp(
													sdfFull.parse(
															model.getCreatedDate())
															.getTime())
													: null);
											// mapping.setIndFacilityTpId(model.getIndFacilityTpId()
											// != null ?
											// model.getIndFacilityTpId() : null
											// );
											// harsh
											IndicatorFacilityTimeperiodMapping iftmp = indicatorFacilityTimeperiodMappingRepository
													.save(mapping);
											// putting iftmp against the iftmpId
											// generated at mobile end
											newIftmpMap.put(
													model.getIndFacilityTpId(),
													iftmp);

											// mappings.add(mapping);

										}
										// indicatorFacilityTimeperiodMappingRepository.save(mappings);
									}
								}
								if (txnsncuDataModels != null
										&& txnsncuDataModels.size() > 0) {
									List<TXNSNCUDataModel> rejectedDataModels = new ArrayList<>();
									List<TXNSNCUData> txnsncuDatas = new ArrayList<>();
									boolean hasError = false;
									for (TXNSNCUDataModel txnsncuDataModel : txnsncuDataModels) {
										IndicatorFacilityTimeperiodMapping iftm = indicatorFacilityTimeperiodMappingRepository
												.findByIndFacilityTpId(txnsncuDataModel
														.getIftid());
										if (newIftmpMap
												.containsKey(txnsncuDataModel
														.getIftid())) {
											iftm = new IndicatorFacilityTimeperiodMapping();
											iftm = newIftmpMap
													.get(txnsncuDataModel
															.getIftid());
										}
										String result = validate(
												txnsncuDataModel, iftm,
												timePeriod.getTimePeriodId(),
												datas, syncResult);
										if (result != null) {
											hasError = true;
										}
										if (result == null) {
											/*
											 * TXNSNCUData txnsncuData = new
											 * TXNSNCUData();
											 * txnsncuData.setNumeratorValue
											 * (txnsncuDataModel
											 * .getNumeratorValue() != null?
											 * txnsncuDataModel
											 * .getNumeratorValue
											 * ().intValue():null);
											 * txnsncuData.setDenominatorValue
											 * (txnsncuDataModel
											 * .getDenominatorValue() != null?
											 * txnsncuDataModel
											 * .getDenominatorValue
											 * ().intValue():null);
											 * txnsncuData.setPercentage
											 * (txnsncuDataModel
											 * .getPercentage());
											 * txnsncuData.setIsLive(true);
											 * txnsncuData
											 * .setCreatedDate(txnsncuDataModel
											 * .getCreatedDate() != null? new
											 * Timestamp
											 * (sdfFull.parse(txnsncuDataModel
											 * .getCreatedDate
											 * ()).getTime()):null);
											 * txnsncuData.
											 * setIndicatorFacilityTimeperiodMapping
											 * (iftm);
											 * txnsncuDatas.add(txnsncuData);
											 */
										} else {
											txnsncuDataModel
													.setErrorMessage(result);
											rejectedDataModels
													.add(txnsncuDataModel);
										}

									}

									if (!hasError) {
										List<TXNSNCUData> oldData = new ArrayList<>();
										for (TXNSNCUData txnsncuData : datas) {
											txnsncuData.setIsLive(false);
											oldData.add(txnsncuData);
										}
										txnsncuDataRepository.save(oldData);
										TXNSubmissionManagement oldDataofSubmission = txnSubmissionManagementRepository
												.findByFacilityAreaIdAndTimePeriodTimePeriodIdAndIsLatestTrue(
														userAreaMapping
																.getFacility()
																.getAreaId(),
														timePeriod
																.getTimePeriodId());
										if (oldDataofSubmission != null) {
											oldDataofSubmission
													.setIsLatest(false);
											txnSubmissionManagementRepository
													.save(oldDataofSubmission);
										}
										TXNSubmissionManagement txnSubmissionManagement = new TXNSubmissionManagement();
										txnSubmissionManagement
												.setCreatedBy(mSTuser
														.getUserName());
										txnSubmissionManagement
												.setCreatedDate(new Timestamp(
														new Date().getTime()));
										txnSubmissionManagement
												.setFacility(userAreaMapping
														.getFacility());
										txnSubmissionManagement
												.setIsLatest(true);
										txnSubmissionManagement
												.setTimePeriod(timePeriod);
										txnSubmissionManagement
												.setStatusSuperintendent(typeDetailRepository
														.findByTypeDetailId(Integer
																.parseInt(messages
																		.getMessage(
																				Constants.Web.SUBMISSION_STATUS_PENDING,
																				null,
																				null))));
										txnSubmissionManagement = txnSubmissionManagementRepository
												.save(txnSubmissionManagement);

										for (TXNSNCUDataModel txnsncuDataModel : txnsncuDataModels) {
											IndicatorFacilityTimeperiodMapping iftm = indicatorFacilityTimeperiodMappingRepository
													.findByIndFacilityTpId(txnsncuDataModel
															.getIftid());
											if (newIftmpMap
													.containsKey(txnsncuDataModel
															.getIftid())) {
												iftm = new IndicatorFacilityTimeperiodMapping();
												iftm = newIftmpMap
														.get(txnsncuDataModel
																.getIftid());
											}

											TXNSNCUData txnsncuData = new TXNSNCUData();
											txnsncuData
													.setNumeratorValue(txnsncuDataModel
															.getNumeratorValue() != null ? txnsncuDataModel
															.getNumeratorValue()
															.intValue()
															: null);
											txnsncuData
													.setDenominatorValue(txnsncuDataModel
															.getDenominatorValue() != null ? txnsncuDataModel
															.getDenominatorValue()
															.intValue()
															: null);
											txnsncuData
													.setPercentage(txnsncuDataModel
															.getPercentage());
											txnsncuData.setIsLive(true);
											txnsncuData
													.setCreatedDate(txnsncuDataModel
															.getCreatedDate() != null ? new Timestamp(
															sdfFull.parse(
																	txnsncuDataModel
																			.getCreatedDate())
																	.getTime())
															: null);
											txnsncuData
													.setIndicatorFacilityTimeperiodMapping(iftm);
											txnsncuData
													.setTxnSubmissionManagement(txnSubmissionManagement);
											if (txnsncuDataModel
													.getDescription() != null) {
												txnsncuData
														.setDescription(new TypeDetail(
																txnsncuDataModel
																		.getDescription()));
											}
											txnsncuDatas.add(txnsncuData);
											syncResult.setRejectedByMne(0);
											syncResult.setRejectedBySup(0);
											syncResult.setAutoApproved(0);
											syncResult
													.setSubmittedDate(sdf
															.format(txnSubmissionManagement
																	.getCreatedDate()));
										}
										txnsncuDataRepository
												.save(txnsncuDatas);
									}

									syncResult
											.setTxnsncuDataModels(rejectedDataModels);
								}
							} else {
								// save engagement score data
								if (syncModel.getTxnEngagementScoreModels() != null
										&& syncModel
												.getTxnEngagementScoreModels()
												.size() > 0) {
									List<TXNEngagementScoreModel> txnEngagementScoreModels = syncModel
											.getTxnEngagementScoreModels();
									List<TXNEngagementScoreModel> errorModels = new ArrayList<>();
									for (TXNEngagementScoreModel txnEngagementScoreModel : txnEngagementScoreModels) {
										TXNEngagementScore engagementScore = txnEngagementScoreRepository
												.findByAreaIdTimePeriod(
														txnEngagementScoreModel
																.getAreaId(),
														txnEngagementScoreModel
																.getTimePeriodId());
										if (engagementScore == null) {
											TXNEngagementScore txnEngagementScore = new TXNEngagementScore();
											txnEngagementScore
													.setEngagementScore(mSTEngagementScoreRepository
															.findByMstEngagementScoreId(txnEngagementScoreModel
																	.getEngagementScoreId()));
											txnEngagementScore
													.setCreatedBy(mSTuser
															.getUserName());
											txnEngagementScore
													.setCreatedDate(new Timestamp(
															sdfFull.parse(
																	txnEngagementScoreModel
																			.getCreatedDate())
																	.getTime()));
											txnEngagementScore
													.setFacility(areaRepository
															.findByAreaId(txnEngagementScoreModel
																	.getAreaId()));
											txnEngagementScore
													.setTimePeriod(timePeriodRepository
															.findByTimePeriodId(txnEngagementScoreModel
																	.getTimePeriodId()));
											txnEngagementScoreRepository
													.save(txnEngagementScore);
											txnEngagementScoreModel
													.setRejectionMessage("Success");
											errorModels
													.add(txnEngagementScoreModel);
										} else {
											txnEngagementScoreModel
													.setRejectionMessage("Already data present for "
															+ engagementScore
																	.getFacility()
																	.getAreaName()
															+ " of month "
															+ engagementScore
																	.getTimePeriod()
																	.getTimePeriod());
											errorModels
													.add(txnEngagementScoreModel);
										}
									}
									syncResult
											.setTxnEngagementScoreModels(errorModels);
								}
							}

							List<Integer> areaIds = new ArrayList<>();
							for (UserAreaMapping userAreaMappingLocal : userAreaMappings) {
								areaIds.add(userAreaMappingLocal.getFacility()
										.getAreaId().intValue());
							}
							userModel.setAreaIds(areaIds);
							masterDataModel.setUserModel(userModel);

							// Area list
							// We have to make sure that should we need to send
							// all the areas to mobile or some areas
							List<Area> areas = areaRepository
									.findByLastSyncDate(lastSyncDate);
							List<AreaModel> areaModels = new ArrayList<>();

							for (Area area : areas) {

								AreaModel areaModel = new AreaModel();
								areaModel
										.setAreaId(area.getAreaId() != null ? area
												.getAreaId().intValue() : 0);
								areaModel.setAreaName(area.getAreaName());
								areaModel.setFacilitySize(area
										.getFacilitySize() != null ? area
										.getFacilitySize().getTypeDetailId()
										: 0);
								areaModel.setFacilityType(area
										.getFacilityType() != null ? area
										.getFacilityType().getTypeDetailId()
										: 0);
								areaModel.setLevel(area.getLevel());
								areaModel.setParentAreaId(area
										.getParentAreaId());
								areaModel.setWave(area.getWave() != null ? area
										.getWave().intValue() : 0);
								areaModels.add(areaModel);
							}
							masterDataModel.setAreaModels(areaModels);

							// Indicators
							List<Indicator> indicators = indicatorRepository
									.findByLastSyncDate(lastSyncDate);
							List<IndicatorModel> indicatorModels = new ArrayList<>();
							for (Indicator indicator : indicators) {

								IndicatorModel indicatorModel = new IndicatorModel();
								indicatorModel.setCoreArea(indicator
										.getCoreArea() != null ? indicator
										.getCoreArea().getTypeDetailId()
										.intValue() : 0);
								indicatorModel.setDenominator(indicator
										.getDenominator());
								indicatorModel.setExceptionRule(indicator
										.getExceptionRule());
								indicatorModel.setIndicatorId(indicator
										.getIndicatorId());
								indicatorModel.setIndicatorName(indicator
										.getIndicatorName());
								indicatorModel.setIndicatorType(indicator
										.getIndicatorType().getTypeDetailId()
										.intValue());
								// indicatorModel.setIntermediateIndicatorId(indicator.getIntermediateIndicatorId()
								// != null ?
								// indicator.getIntermediateIndicatorId().intValue()
								// : 0);
								indicatorModel.setIsReqired(indicator
										.getIsReqired());
								indicatorModel.setNumerator(indicator
										.getNumerator());
								// indicatorModel.setOutcomeIndicatorId(indicator.getOutcomeIndicatorId()
								// != null ?
								// indicator.getOutcomeIndicatorId().intValue()
								// : 0);
								// indicatorModel.setProcessIndicatorId(indicator.getProcessIndicatorId()
								// != null ?
								// indicator.getProcessIndicatorId().intValue()
								// : 0);
								indicatorModel.setRelatedIndicatorIds(indicator
										.getRelatedIndicatorIds());

								indicatorModels.add(indicatorModel);
							}
							masterDataModel.setIndicatorModels(indicatorModels);

							// Types
							List<Type> types = typeRepository
									.findByLastSyncDate(lastSyncDate);
							List<TypeModel> typeModels = new ArrayList<>();

							for (Type type : types) {
								TypeModel typeModel = new TypeModel();

								typeModel
										.setTypeId(type.getTypeId().intValue());
								typeModel.setTypeName(type.getTypeName());
								typeModel.setDescription(type.getDescription());

								typeModels.add(typeModel);
							}

							masterDataModel.setTypeModels(typeModels);

							// Type details
							List<TypeDetail> typeDetails = typeDetailRepository
									.findByLastSyncDate(lastSyncDate);
							List<TypeDetailModel> typeDetailModels = new ArrayList<>();
							for (TypeDetail typeDetail : typeDetails) {
								TypeDetailModel typeDetailModel = new TypeDetailModel();
								typeDetailModel.setTypeDetailId(typeDetail
										.getTypeDetailId().intValue());
								typeDetailModel.setTypeDetail(typeDetail
										.getTypeDetail());
								typeDetailModel.setDescription(typeDetail
										.getDescription());
								typeDetailModel.setTypeId(typeDetail.getType()
										.getTypeId());
								typeDetailModels.add(typeDetailModel);
							}

							masterDataModel
									.setTypeDetailModels(typeDetailModels);

							// Time periods
							List<TimePeriod> timePeriods = timePeriodRepository
									.findByLastSyncDate(lastSyncDate);
							List<TimePeriodModel> timePeriodModels = new ArrayList<>();
							for (TimePeriod timePeriodLocal : timePeriods) {

								TimePeriodModel timePeriodModel = new TimePeriodModel();
								timePeriodModel.setTimePeriodId(timePeriodLocal
										.getTimePeriodId().intValue());
								timePeriodModel.setTimePeriod(timePeriodLocal
										.getTimePeriod());
								timePeriodModel.setWave(timePeriodLocal
										.getWave());
								timePeriodModel.setStartDate(timePeriodLocal
										.getStartDate() != null ? sdf
										.format(timePeriodLocal.getStartDate())
										: null);
								timePeriodModel.setEndDate(timePeriodLocal
										.getEndDate() != null ? sdf
										.format(timePeriodLocal.getEndDate())
										: null);
								timePeriodModel.setPeriodicity(timePeriodLocal
										.getPeriodicity());
								timePeriodModels.add(timePeriodModel);

							}

							masterDataModel
									.setTimePeriodModels(timePeriodModels);

							// Engagement score master data
							List<MSTEngagementScore> mSTEngagementScores = mSTEngagementScoreRepository
									.findByLastSyncDate(lastSyncDate);
							List<MSTEngagementScoreModel> mSTEngagementScoreModels = new ArrayList<>();
							for (MSTEngagementScore mSTEngagementScore : mSTEngagementScores) {

								MSTEngagementScoreModel mSTEngagementScoreModel = new MSTEngagementScoreModel();
								mSTEngagementScoreModel
										.setMstEngagementScoreId(mSTEngagementScore
												.getMstEngagementScoreId()
												.intValue());
								mSTEngagementScoreModel
										.setProgress(mSTEngagementScore
												.getProgress());
								mSTEngagementScoreModel
										.setDefinition(mSTEngagementScore
												.getDefinition());
								mSTEngagementScoreModel
										.setScore(mSTEngagementScore.getScore());
								mSTEngagementScoreModels
										.add(mSTEngagementScoreModel);

							}
							masterDataModel
									.setmSTEngagementScoreModels(mSTEngagementScoreModels);

							// IndicatorFacilityTimeperiodMapping
							List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings = indicatorFacilityTimeperiodMappingRepository
									.findByAreaIdAndTimePeriodIsLiveTrue(
											userAreaMapping.getFacility()
													.getAreaId().intValue(),
											timePeriod != null ? timePeriod
													.getTimePeriodId() : 0);
							// List<IndicatorFacilityTimeperiodMapping>
							// indicatorFacilityTimeperiodMappings =
							// indicatorFacilityTimeperiodMappingRepository.findByLastSyncDateAndAreaId(lastSyncDate,
							// userAreaMapping.getFacility().getAreaId().intValue())
							// ;
							List<IndicatorFacilityTimeperiodMappingModel> indicatorFacilityTimeperiodMappingModels = new ArrayList<>();
							for (IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping : indicatorFacilityTimeperiodMappings) {
								IndicatorFacilityTimeperiodMappingModel indicatorFacilityTimeperiodMappingModel = new IndicatorFacilityTimeperiodMappingModel();
								indicatorFacilityTimeperiodMappingModel
										.setIndFacilityTpId(indicatorFacilityTimeperiodMapping
												.getIndFacilityTpId()
												.intValue());
								indicatorFacilityTimeperiodMappingModel
										.setFacilityId(indicatorFacilityTimeperiodMapping
												.getFacility().getAreaId()
												.intValue());
								indicatorFacilityTimeperiodMappingModel
										.setIndicatorId(indicatorFacilityTimeperiodMapping
												.getIndicator()
												.getIndicatorId().intValue());
								indicatorFacilityTimeperiodMappingModel
										.setTimePeriodId(indicatorFacilityTimeperiodMapping
												.getTimePeriod()
												.getTimePeriodId().intValue());
								indicatorFacilityTimeperiodMappingModel
										.setCreatedDate(indicatorFacilityTimeperiodMapping
												.getCreatedDate() != null ? sdfFull
												.format(indicatorFacilityTimeperiodMapping
														.getCreatedDate())
												: null);
								indicatorFacilityTimeperiodMappingModels
										.add(indicatorFacilityTimeperiodMappingModel);
							}
							masterDataModel
									.setIndicatorFacilityTimeperiodMappingModels(indicatorFacilityTimeperiodMappingModels);
							masterDataModel
									.setDeoDeadLine(Integer.parseInt(messages
											.getMessage(
													Constants.Mobile.SUBMISSION_DEADLINE_DATE,
													null, null)));
							masterDataModel
									.setSubDeadLine(Integer.parseInt(messages
											.getMessage(
													Constants.Mobile.REJECTION_SUBMISSION_DEADLINE_DATE_BY_SUP,
													null, null)));
							// masterDataModel.setMneDeadLine(Integer.parseInt(messages.getMessage(Constants.Mobile.REJECTION_SUBMISSION_DEADLINE_DATE_BY_MNE,
							// null, null)));
							masterDataModel.setMneDeadLine(LocalDate.now()
									.with(TemporalAdjusters.lastDayOfMonth())
									.getDayOfMonth());
							masterDataModel.setLastSyncDate(sdfFull
									.format(new Date()));

						} else {
							// no role mapped to the user
							logger.warn("No role mapped for Username : "
									+ loginDataModel.getUsername()
									+ " passowrd string: "
									+ loginDataModel.getPassword());
							syncResult.setErrorCode(1);
							syncResult.setErrorMessage(messages.getMessage(
									Constants.Mobile.INVALID_USER_ROLE_MAPPING,
									null, null));
						}

					} else {
						// size 0, no area mapping to this user
						logger.warn("No area mapping found for Username : "
								+ loginDataModel.getUsername()
								+ " passowrd string: "
								+ loginDataModel.getPassword());
						syncResult.setErrorCode(1);
						syncResult.setErrorMessage(messages.getMessage(
								Constants.Mobile.INVALID_USER_AREA_MAPPING,
								null, null));
					}

					syncResult.setMasterDataModel(masterDataModel);
					return syncResult;
				} else {
					// No user found in given credentials

					logger.warn("No user found for Username : "
							+ loginDataModel.getUsername()
							+ " passowrd string: "
							+ loginDataModel.getPassword());
					syncResult.setErrorCode(1);
					syncResult.setErrorMessage(messages.getMessage(
							Constants.Mobile.INVALID_CREDENTIALS, null, null));
					return syncResult;
				}

			} else {
				// login data is null, going to send error invalid username and
				// password

				logger.warn("Request has come for sync. Null info has come");
				syncResult.setErrorCode(1);
				syncResult.setErrorMessage(messages.getMessage(
						Constants.Mobile.INVALID_DATA, null, null));
				return syncResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception : " + e.getMessage());
			syncResult.setErrorCode(1);
			syncResult.setErrorMessage("Server error : " + e.getMessage());
			return syncResult;
		}

	}

	/**
	 * This is important one, this method is going to update the txn sncu data
	 * if there would be any conflict in indicator facility time period mapping
	 * 
	 * @param txnsncuDataModels
	 *            sncu transactional data from mobile
	 * @param mobileId
	 *            Id from mobile
	 * @param webId
	 *            Id in web
	 * @return dataModels modified list to persist in db.
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 09-May-2017 3:25:16
	 *         pm
	 */
	private List<TXNSNCUDataModel> checkAndUpdateTXNSNCU(
			List<TXNSNCUDataModel> txnsncuDataModels, int mobileId, int webId) {
		List<TXNSNCUDataModel> dataModels = new ArrayList<>();
		for (TXNSNCUDataModel dataModel : txnsncuDataModels) {
			if (dataModel.getIftid() == mobileId) {
				dataModel.setIftid(webId);
			}
			dataModels.add(dataModel);
		}
		return dataModels;
	}

	/**
	 * This method will validate the data
	 * 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 08-May-2017 1:33:49
	 *         pm
	 */
	private String validate(TXNSNCUDataModel txnsncuDataModel,
			IndicatorFacilityTimeperiodMapping iftm, int lastTimePeriodId,
			List<TXNSNCUData> datas, SyncResult syncResult) {

		Calendar today = Calendar.getInstance();
		int submission_date = Integer.parseInt(messages.getMessage(
				Constants.Mobile.SUBMISSION_DEADLINE_DATE, null, null));
		int rejection_submission_date_by_sup = Integer
				.parseInt(messages
						.getMessage(
								Constants.Mobile.REJECTION_SUBMISSION_DEADLINE_DATE_BY_SUP,
								null, null));
		// int rejection_submission_date_by_mne =
		// Integer.parseInt(messages.getMessage(Constants.Mobile.REJECTION_SUBMISSION_DEADLINE_DATE_BY_MNE,
		// null, null));
		int rejection_submission_date_by_mne = LocalDate.now()
				.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

		if (syncResult.getRejectedByMne() == 1) {
			if (today.get(Calendar.DAY_OF_MONTH) > rejection_submission_date_by_mne) {
				return "Data sumission should be on or before "
						+ rejection_submission_date_by_mne;
			}
		}

		if (syncResult.getRejectedBySup() == 1) {
			if (today.get(Calendar.DAY_OF_MONTH) > rejection_submission_date_by_sup) {
				return "Data sumission should be on or before "
						+ rejection_submission_date_by_sup;
			}
		}

		if (today.get(Calendar.DAY_OF_MONTH) > submission_date
				&& !(txnsncuDataModel.getRejectedByMNE() == 1 || txnsncuDataModel
						.getRejectedBySup() == 1)) {
			return "Data sumission should be on or before " + submission_date;
		}

		if (iftm.getTimePeriod().getTimePeriodId().intValue() != lastTimePeriodId) {
			return messages.getMessage(Constants.Mobile.INVALID_MONTH_DATA,
					null, null);
		}

		if (datas.size() != 0
				&& !(syncResult.getRejectedByMne() == 1 || syncResult
						.getRejectedBySup() == 1)) {
			return messages.getMessage(
					Constants.Mobile.DATA_PRESENT_FOR_TIME_PERIOD, null, null);
		}
		return null;
	}

}
