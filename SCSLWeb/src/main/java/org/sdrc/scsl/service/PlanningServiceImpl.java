/**
 * 
 */
package org.sdrc.scsl.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.MSTUser;
import org.sdrc.scsl.domain.TXNPlanning;
import org.sdrc.scsl.domain.UserAreaMapping;
import org.sdrc.scsl.model.web.AreaWebModel;
import org.sdrc.scsl.model.web.AssessmentHistory;
import org.sdrc.scsl.model.web.FacilityPlanningModel;
import org.sdrc.scsl.model.web.MailModel;
import org.sdrc.scsl.model.web.PlanningModel;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.model.web.TxnPlanningModel;
import org.sdrc.scsl.model.web.UserAreaModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.ValueObject;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.MSTUserRepository;
import org.sdrc.scsl.repository.TXNPlanningRepository;
import org.sdrc.scsl.repository.UserAreaMappingRepository;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @author Sarita Panigrahi (sarita@sdrc.co.in)
 *This class is responsible for planning module functionalities
 */
@Service
public class PlanningServiceImpl implements PlanningService {

	@Autowired
	private TXNPlanningRepository txnPlanningRepository;

	@Autowired
	private StateManager stateManager;

	@Autowired
	private ResourceBundleMessageSource messages;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private UserAreaMappingRepository userAreaMappingRepository;

	@Autowired
	private ResourceBundleMessageSource errorMessageSource;

	@Autowired
	private ResourceBundleMessageSource applicationMessageSource;

	@Autowired
	private MailService mailService;

	@Autowired
	private MSTUserRepository mstUserRepository;

	private SimpleDateFormat sdf = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.DATE_MONTH_YEAR_SIMPLEDATE_FORMATTER));

	private static final Logger LOGGER=Logger.getLogger(PlanningServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PlanningService#getPlanningData()
	 * This method will return the data on load of planning page
	 */
	@Override
	@Transactional
	// assessment history to be added
	public PlanningModel getPlanningData() {

		UserModel loggedInUser = (UserModel) stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		List<Area> areas = areaRepository.findAllByOrderByLevelAscAreaNameAsc();

		List<AreaWebModel> areaWebModels = new ArrayList<>();
		PlanningModel planningModel = new PlanningModel();
		List<FacilityPlanningModel> facilityPlanningModels = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);

		if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			// setting calendar to next week
			calendar.add(Calendar.DATE, 7);
		}
		// setting start date to Monday
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		// getting date of Monday of next week
		Date startDate = calendar.getTime();

		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date);
		calendar1.add(Calendar.DATE, 1);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		// setting calendar to Sunday by adding 6
		calendar.add(Calendar.DATE, 6);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		// getting last date of next week
		Date endDate = calendar.getTime();

		// the next week start and end date
		planningModel.setServerDate(sdf.format(date));
		planningModel.setStartDate(sdf.format(startDate));
		planningModel.setEndDate(sdf.format(endDate));

		List<TXNPlanning> txnPlannings ;
		List<TXNPlanning> allPlannings ;

		// Map<>

		// Getting roleId of of AHI associate
		int roleID = Integer.parseInt(messages.getMessage(Constants.Web.AHI_ASSOCIATE_ROLE_ID, null, null));

		List<Integer> pendingFacilities = new ArrayList<>();

		// if ahi associate has logged in then we will bring only planning of
		// those facilities assigned to the user of next week
		if (loggedInUser.getUserAreaModels().get(0).getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole().getRoleId() == roleID) {
			List<Integer> ahiFacilityArea = new ArrayList<>();

			for (UserAreaModel areaModel : loggedInUser.getUserAreaModels()) {
				ahiFacilityArea.add(Integer.parseInt(areaModel.getAreaModel().getKey()));
			}
			txnPlannings = txnPlanningRepository
					.findByPlanDateBetweenAndIsLiveTrueAndFacilityAreaIdIsInOrderByFacilityAreaNameAsc(
							new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), ahiFacilityArea);
			// Bringing all the planning done till today for the facility
			// assigned to AHI associate
			allPlannings = txnPlanningRepository
					.findByIsLiveTrueAndFacilityAreaIdIsInOrderByFacilityAreaNameAsc(ahiFacilityArea);
		}

		// else will all the planning of every facilities of next week
		else {
			txnPlannings = txnPlanningRepository.findByPlanDateBetweenAndIsLiveTrueOrderByFacilityAreaNameAsc(
					new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));
			// Bringing all the planning done till today
			allPlannings = txnPlanningRepository.findByIsLiveTrue();
		}

		// this map will contain the all the planing of next week
		Map<Integer, List<TXNPlanning>> txnPlanningMap = new HashMap<>();

		for (TXNPlanning txnPlanning : txnPlannings) {
			if (txnPlanningMap.containsKey(txnPlanning.getFacility().getAreaId())) {
				txnPlanningMap.get(txnPlanning.getFacility().getAreaId()).add(txnPlanning);
			} else {
				List<TXNPlanning> txnPlanningList = new ArrayList<>();
				txnPlanningList.add(txnPlanning);
				txnPlanningMap.put(txnPlanning.getFacility().getAreaId(), txnPlanningList);
			}
		}

		// this map will contain list of all the planning with key as facility
		// Id
		Map<Integer, List<AssessmentHistory>> allAssessmentHistoryMap = new HashMap<>();
		for (TXNPlanning txnPlanning : allPlannings) {
			if (!allAssessmentHistoryMap.containsKey(txnPlanning.getFacility().getAreaId())) {
				List<AssessmentHistory> asessementHistories = new ArrayList<>();
				AssessmentHistory assessmentHistory = new AssessmentHistory();
				assessmentHistory.setAgendaPath(txnPlanning.getAgendaReportFilepath());
				assessmentHistory.setPlannedDate(sdf.format(txnPlanning.getPlanDate()));
				assessmentHistory.setPlanningId(txnPlanning.getPlanningId());
				assessmentHistory.setUserId(txnPlanning.getMstUser().getUserId());
				assessmentHistory.setUserName(txnPlanning.getMstUser().getName());
				// if visit date is not null
				if (txnPlanning.getVisitedDate() != null) {
					// visited
					assessmentHistory.setVisitedDate(sdf.format(txnPlanning.getVisitedDate()));
					assessmentHistory.setReportPath(txnPlanning.getTripReportFilepath());
					assessmentHistory.setReportUpload(false);
				} else {
					// not visited
					// if logged user is and the user id of planning is same and
					// plan date is before or equal to today's date
					if (loggedInUser.getUserId() == txnPlanning.getMstUser().getUserId().intValue()
							&& (txnPlanning.getPlanDate().before(date)
									|| DateUtils.isSameDay(txnPlanning.getPlanDate(), date))) {
						// this attribute will be used to used determine the
						// whether uploading the trip report is allowed or not
						assessmentHistory.setReportUpload(true);
						if (!pendingFacilities.contains(txnPlanning.getFacility().getAreaId())) {
							pendingFacilities.add(txnPlanning.getFacility().getAreaId());
						}
					}
				}
				asessementHistories.add(assessmentHistory);
				allAssessmentHistoryMap.put(txnPlanning.getFacility().getAreaId(), asessementHistories);
			} else {
				AssessmentHistory assessmentHistory = new AssessmentHistory();
				assessmentHistory.setAgendaPath(txnPlanning.getAgendaReportFilepath());
				assessmentHistory.setPlannedDate(sdf.format(txnPlanning.getPlanDate()));
				assessmentHistory.setPlanningId(txnPlanning.getPlanningId());
				assessmentHistory.setUserId(txnPlanning.getMstUser().getUserId());
				assessmentHistory.setUserName(txnPlanning.getMstUser().getName());
				if (txnPlanning.getVisitedDate() != null) {
					assessmentHistory.setVisitedDate(sdf.format(txnPlanning.getVisitedDate()));
					assessmentHistory.setReportPath(txnPlanning.getTripReportFilepath());
					assessmentHistory.setReportUpload(false);
				} else {
					if (loggedInUser.getUserId() == txnPlanning.getMstUser().getUserId().intValue()
							&& (txnPlanning.getPlanDate().before(date)
									|| DateUtils.isSameDay(txnPlanning.getPlanDate(), date))) {
						assessmentHistory.setReportUpload(true);
						if (!pendingFacilities.contains(txnPlanning.getFacility().getAreaId())) {
							pendingFacilities.add(txnPlanning.getFacility().getAreaId());
						}
					}
				}
				allAssessmentHistoryMap.get(txnPlanning.getFacility().getAreaId()).add(assessmentHistory);
			}

		}

		/*
		 * find All Area & set into an map for StateID set in
		 * facilityPlanningModel
		 * 
		 * @author Debiprasad
		 */
		List<Area> listOfAreas = areaRepository.findAll();
		Map<Integer, Area> areaMap = new HashMap<>();
		for (Area area : listOfAreas) {
			areaMap.put(area.getAreaId(), area);
		}
		// if logged in user is not AHI_ASSOCIATE
		if (loggedInUser.getUserAreaModels().get(0).getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole().getRoleId() != roleID) {
			for (Area area : areas) {
				// if area is not facility will be added for filter
				if (area.getLevel() != 4) {
					AreaWebModel areaWebModel = new AreaWebModel();
					areaWebModel.setAreaId(area.getAreaId());
					areaWebModel.setAreaName(area.getAreaName());
					areaWebModel.setParentAreaId(area.getParentAreaId());
					areaWebModel.setLevel(area.getLevel());
					areaWebModels.add(areaWebModel);
				}

				// facility type area
				else {
					FacilityPlanningModel facilityPlanningModel = new FacilityPlanningModel();
					List<ValueObject> valueObjects = new ArrayList<>();
					List<ValueObject> releaseDates = new ArrayList<>();
					facilityPlanningModel.setFacilityId(area.getAreaId());
					facilityPlanningModel.setFacilityName(area.getAreaName());
					facilityPlanningModel.setParentId(area.getParentAreaId());
					facilityPlanningModel.setStateId(areaMap.get(area.getParentAreaId()).getParentAreaId());
					if (pendingFacilities.contains(area.getAreaId())) {
						facilityPlanningModel.setPending(true);
					}
					// if at least one planning is available for the facility
					if (txnPlanningMap.containsKey(area.getAreaId())) {
						// facility is planned
						facilityPlanningModel.setPlanned(true);

						for (TXNPlanning txnPlanning : txnPlanningMap.get(area.getAreaId())) {
							ValueObject valueObject = new ValueObject();
							// key will contain the planning date
							valueObject.setKey(sdf.format(txnPlanning.getPlanDate()));
							// value will contain the name of user planned the
							// facility
							valueObject.setValue(txnPlanning.getMstUser().getName());
							valueObjects.add(valueObject);

							// if facility is not visited and the logged in user
							// is same as user planned the facility
							// releaseDates will contain the date for which
							// datepicker will be available to release for
							// selected facility
							if (loggedInUser.getUserId() == txnPlanning.getMstUser().getUserId().intValue()
									&& txnPlanning.getVisitedDate() == null) {
								ValueObject realeaseDate = new ValueObject(txnPlanning.getPlanningId(),
										sdf.format(txnPlanning.getPlanDate()));
								releaseDates.add(realeaseDate);
							}
						}
					} else {
						facilityPlanningModel.setPlanned(false);
					}
					facilityPlanningModel.setPlannedHistory(valueObjects);

					// if atleast one facility planned then only we will set
					// releaseDates
					if (!releaseDates.isEmpty()) {
						facilityPlanningModel.setRealeaseDate(releaseDates);
					}

					Map<String, List<AssessmentHistory>> assessmentHistoryMap = new HashMap<>();

					if (allAssessmentHistoryMap.containsKey(area.getAreaId())) {
						List<AssessmentHistory> assessmentHistories = allAssessmentHistoryMap.get(area.getAreaId());

						for (AssessmentHistory assessmentHistory : assessmentHistories) {
							if (assessmentHistoryMap.containsKey(
									assessmentHistory.getUserId() + "_" + assessmentHistory.getUserName())) {
								assessmentHistoryMap
										.get(assessmentHistory.getUserId() + "_" + assessmentHistory.getUserName())
										.add(assessmentHistory);
							} else {
								List<AssessmentHistory> tempAssessmentList = new ArrayList<>();
								tempAssessmentList.add(assessmentHistory);

								assessmentHistoryMap.put(
										assessmentHistory.getUserId() + "_" + assessmentHistory.getUserName(),
										tempAssessmentList);
							}
						}
					}

					facilityPlanningModel.setAssessmentHistory(assessmentHistoryMap);
					facilityPlanningModels.add(facilityPlanningModel);
				}
			}
		}
		// if logged in user is AHI associate
		else {
			List<UserAreaMapping> userAreaMappings = userAreaMappingRepository
					.findByUserUserIdAndIsLiveTrue(loggedInUser.getUserId());
			for (UserAreaMapping userAreaMapping : userAreaMappings) {
				Area area = userAreaMapping.getFacility();
				FacilityPlanningModel facilityPlanningModel = new FacilityPlanningModel();
				List<ValueObject> valueObjects = new ArrayList<>();
				List<ValueObject> releaseDates = new ArrayList<>();
				facilityPlanningModel.setFacilityId(area.getAreaId());
				facilityPlanningModel.setFacilityName(area.getAreaName());
				facilityPlanningModel.setParentId(area.getParentAreaId());
				facilityPlanningModel.setStateId(areaMap.get(area.getParentAreaId()).getParentAreaId());
				if (pendingFacilities.contains(area.getAreaId())) {
					facilityPlanningModel.setPending(true);
				}
				if (txnPlanningMap.containsKey(area.getAreaId())) {
					// facility is planned
					facilityPlanningModel.setPlanned(true);

					for (TXNPlanning txnPlanning : txnPlanningMap.get(area.getAreaId())) {
						ValueObject valueObject = new ValueObject();
						// key will contain the planning date
						valueObject.setKey(sdf.format(txnPlanning.getPlanDate()));
						// value will contain the name of user planned the
						// facility
						valueObject.setValue(txnPlanning.getMstUser().getName());
						valueObjects.add(valueObject);

						// if facility is not visited and the logged in user
						// is same as user planned the facility
						// releaseDates will contain the date for which
						// datepicker will be available to release for
						// selected facility
						if (loggedInUser.getUserId() == txnPlanning.getMstUser().getUserId().intValue()
								&& txnPlanning.getVisitedDate() == null) {
							ValueObject realeaseDate = new ValueObject(txnPlanning.getPlanningId(),
									sdf.format(txnPlanning.getPlanDate()));
							releaseDates.add(realeaseDate);
						}
					}
				} else {
					facilityPlanningModel.setPlanned(false);
				}
				facilityPlanningModel.setPlannedHistory(valueObjects);
				if (!releaseDates.isEmpty()) {
					facilityPlanningModel.setRealeaseDate(releaseDates);
				}
				Map<String, List<AssessmentHistory>> assessmentHistoryMap = new HashMap<>();

				if (allAssessmentHistoryMap.containsKey(area.getAreaId())) {
					List<AssessmentHistory> assessmentHistories = allAssessmentHistoryMap.get(area.getAreaId());

					for (AssessmentHistory assessmentHistory : assessmentHistories) {
						if (assessmentHistoryMap
								.containsKey(assessmentHistory.getUserId() + "_" + assessmentHistory.getUserName())) {
							assessmentHistoryMap
									.get(assessmentHistory.getUserId() + "_" + assessmentHistory.getUserName())
									.add(assessmentHistory);
						} else {
							List<AssessmentHistory> tempAssessmentList = new ArrayList<>();
							tempAssessmentList.add(assessmentHistory);

							assessmentHistoryMap.put(
									assessmentHistory.getUserId() + "_" + assessmentHistory.getUserName(),
									tempAssessmentList);
						}
					}
				}

				facilityPlanningModel.setAssessmentHistory(assessmentHistoryMap);
				facilityPlanningModels.add(facilityPlanningModel);

			}
		}
		planningModel.setFacilityPlanningModel(facilityPlanningModels);
		planningModel.setAreaModel(areaWebModels);
		return planningModel;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PlanningService#releasePlanning(int)
	 * This method will be responsible for releasing a plan for a facility 
	 */
	@Override
	@Transactional
	public ReturnModel releasePlanning(int planningId) throws Exception {
		
		ReturnModel returnModel = new ReturnModel();
		try {
			TXNPlanning txnPlanning = txnPlanningRepository.findByPlanningId(planningId);
	
			if (txnPlanning != null) {
				Calendar calendar = Calendar.getInstance();
				Date date = new Date();
				calendar.setTime(date);
	
				if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
					// setting calendar to next week
					calendar.add(Calendar.DATE, 7);
				}
				// setting start date to Monday
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	
				// getting date of Monday of next week
				Date startDate = calendar.getTime();
	
				// setting calendar to Sunday by adding 6
				calendar.add(Calendar.DATE, 6);
	
				// getting last date of next week
				Date endDate = calendar.getTime();
	
				if (DateUtils.isSameDay(txnPlanning.getPlanDate(), startDate)
						|| DateUtils.isSameDay(txnPlanning.getPlanDate(), endDate)
						|| (txnPlanning.getPlanDate().after(startDate) && txnPlanning.getPlanDate().before(endDate))) {
					txnPlanning.setIsLive(false);
					TXNPlanning releasedata = txnPlanningRepository.save(txnPlanning);
					/* ============e-mail send start============ */
					try {
						UserModel userModel = (UserModel) stateManager.getValue(Constants.Web.USER_PRINCIPAL);
						MSTUser user = mstUserRepository.findByUserIdAndIsLiveTrue(userModel.getUserId());
	
						String facilityName = areaRepository.findByAreaId(txnPlanning.getFacility().getAreaId())
								.getAreaName();
	
						String messageBody = "A visit to " + facilityName + " planned by " + userModel.getName() + ""
								+ " on " + sdf.format(releasedata.getPlanDate()) + " has been cancelled  on "
								+ sdf.format(new Date())
								+ ". Should you require any additional information regarding the cancellation, please get in touch with "
								+ "" + userModel.getName() + ".";
	
						String subjectOfMail = "Visit Cancelled For Facility ";
	
						emailForFacilityPlanAndRelease(userModel, user, releasedata, facilityName, messageBody,
								subjectOfMail,null);
					} catch (Exception e) {
						LOGGER.error(e);
						returnModel.setStatusCode(
								errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
						returnModel.setErrorMessage(
								errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_RELEASE_MAILLING, null, null));
						return returnModel;
					}
					/* ============e-mail send end============ */
	
					returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.SUCCESS_STATUS_CODE, null, null));
					returnModel.setStatusMessage(
							errorMessageSource.getMessage(Constants.Web.PLANNING_RELEASE_SUCCESS, null, null));
					return returnModel;
				}
	
				else {
					returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
					returnModel.setStatusMessage(
							errorMessageSource.getMessage(Constants.Web.PLANNING_RELEASE_FAILURE, null, null));
					return returnModel;
				}
			}
	
			else {
				returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
				returnModel.setStatusMessage(
						errorMessageSource.getMessage(Constants.Web.PLANNING_RELEASE_FAILURE, null, null));
				return returnModel;
			}
		} catch (Exception e) {
			LOGGER.error(e);
			returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
			returnModel.setStatusMessage(
					errorMessageSource.getMessage(Constants.Web.PLANNING_RELEASE_FAILURE, null, null));
			return returnModel;
		}

	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.PlanningService#planFacility(org.sdrc.scsl.model.web.TxnPlanningModel)
	 * This method will be used for the planning
	 */
	@Override
	@Transactional
	public ReturnModel planFacility(TxnPlanningModel txnPlanningModel) {
		ReturnModel returnModel = new ReturnModel();
		TXNPlanning txnPlanning = new TXNPlanning();

		UserModel userModel = (UserModel) stateManager.getValue(Constants.Web.USER_PRINCIPAL);
		MSTUser user = mstUserRepository.findByUserIdAndIsLiveTrue(userModel.getUserId());
		txnPlanning.setCreatedDate(new Timestamp(new Date().getTime()));
		txnPlanning.setFacility(new Area(txnPlanningModel.getFacilityId()));
		txnPlanning.setMstUser(user);

		if (txnPlanningModel.getTagEmailIds() != null
				&& !txnPlanningModel.getTagEmailIds().trim().equalsIgnoreCase("")) {
			txnPlanning.setTagEmail(txnPlanningModel.getTagEmailIds());
		}

		try {
			Calendar calendar = Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);

			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				calendar.add(Calendar.DATE, -7);

			// setting start date to Monday
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

			// setting calendar to Friday by adding 4
			calendar.add(Calendar.DATE, 4);
//			Date fridayDate = calendar.getTime();
			txnPlanning.setPlanDate(new Timestamp(sdf.parse(txnPlanningModel.getDate()).getTime()));
			txnPlanning.setIsLive(true);

			String path = applicationMessageSource.getMessage(Constants.Web.FILE_PATH_PDSA, null, null);
			File filePath = new File(path);
			if (!filePath.exists())
				filePath.mkdir();
			// creating directory within planning folder with name planning
			File directory = new File(
					path + applicationMessageSource.getMessage(Constants.Web.FILEPATH_PLANNING_FOLDER, null, null));
			if (!directory.exists())
				directory.mkdirs();

			File agendaDirectory = new File(directory.getAbsolutePath()
					+ applicationMessageSource.getMessage(Constants.Web.FILEPATH_PLANNING_AGENDA_FOLDER, null, null));
			if (!agendaDirectory.exists())
				agendaDirectory.mkdirs();

			String filePathString = agendaDirectory.getAbsolutePath() + File.separator
					+ sdf.format(txnPlanning.getPlanDate()) + "_"
					+ txnPlanningModel.getDocument().getOriginalFilename();
			File file = new File(filePathString);
			BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(file));

			txnPlanning.setAgendaReportFilepath(filePathString);
			buffStream.write(txnPlanningModel.getDocument().getBytes());
			buffStream.close();
			TXNPlanning txnplanmail = txnPlanningRepository.save(txnPlanning);

			/* ============e-mail send start============ */
			try {

				String facilityName = areaRepository.findByAreaId(txnPlanning.getFacility().getAreaId()).getAreaName();

				String messageBody = "A visit to " + facilityName + " has been planned by " + userModel.getName() + ""
						+ " on " + sdf.format(txnplanmail.getPlanDate())
						+ ". Should you require any additional information regarding the visit, please get in touch with "
						+ "" + userModel.getName()
						+ ".<br/><br/></p>Please find the attached visit agenda, which is for your reference.";

				String subjectOfMail = "Visit Planned For Facility ";

				Map<String, String> agendareportMap = new HashMap<>();
				String attachedFileName = sdf.format(txnPlanning.getPlanDate()) + "_"
						+ txnPlanningModel.getDocument().getOriginalFilename();
				agendareportMap.put(attachedFileName, agendaDirectory.getAbsolutePath() + File.separator);

				emailForFacilityPlanAndRelease(userModel, user, txnPlanning, facilityName, messageBody, subjectOfMail,
						agendareportMap);

			} catch (Exception e) {
				LOGGER.error(e);
				returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
				returnModel.setErrorMessage(
						errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_PLANNING_MAILLING, null, null));
				return returnModel;

			}

			/* ============e-mail send end============ */
			returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.SUCCESS_STATUS_CODE, null, null));

			returnModel
					.setErrorMessage(errorMessageSource.getMessage(Constants.Web.SUCCESS_STATUS_PLANNING, null, null));

			return returnModel;
		}catch (Exception e) {
			LOGGER.error(e);
			returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
			returnModel
					.setErrorMessage(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_PLANNING, null, null));
			return returnModel;
		}
	}

	/**
	 * @param userModel
	 * @param user
	 * @param txnPlanning
	 * @param facilityName
	 * @param messageBody
	 * @param subjectOfMail
	 * @param agendareportMap
	 * @throws Exception
	 * sends email while releasing a plan
	 */
	private void emailForFacilityPlanAndRelease(UserModel userModel, MSTUser user, TXNPlanning txnPlanning,
			String facilityName, String messageBody, String subjectOfMail, Map<String, String> agendareportMap)
			throws Exception {

		Integer loginRoleId = userModel.getUserAreaModels().get(0).getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole().getRoleId();
		List<String> ccEmailLists = new ArrayList<>();
		List<String> toEmailLists = new ArrayList<>();

		List<MSTUser> projectManager = mstUserRepository
				.findDistinctByUserAreaMappingsUserRoleFeaturePermissionMappingsRoleFeaturePermissionSchemeRoleRoleIdIn(
						Arrays.asList(Integer.parseInt(messages.getMessage(Constants.Web.AHI_PROJECT_MANAGER, null, null))));

		for (MSTUser mstUser : projectManager) {
			if (mstUser.getEmail() != null && !mstUser.getEmail().equals("")) {
				toEmailLists.add(mstUser.getEmail());
			}
		}
		// split comma separated tag mail id & add into a List
		if (txnPlanning.getTagEmail() != null && !txnPlanning.getTagEmail().trim().equalsIgnoreCase("")) {
			String[] emailArray = txnPlanning.getTagEmail().split(",");
			for (int i = 0; i < emailArray.length; i++) {
				ccEmailLists.add(emailArray[i]);
			}
		}

		// Assumption:-Project Manager or MnE Lead e-mail Id or operation lead email is
		// compulsory for Sending mail
		if (loginRoleId == 5) {
			List<MSTUser> mneOperationLead = mstUserRepository
					.findDistinctByUserAreaMappingsUserRoleFeaturePermissionMappingsRoleFeaturePermissionSchemeRoleRoleIdIn(
							Arrays.asList(Integer.parseInt(messages.getMessage(Constants.Web.AHI_MNE_LEAD, null, null)),
									Integer.parseInt(messages.getMessage(Constants.Web.AHI_OPERATION_LEAD_ROLE_ID, null,
											null))));
			for (MSTUser mstUser : mneOperationLead) {
				if (mstUser.getEmail() != null && !mstUser.getEmail().equals("")) {
					toEmailLists.add(mstUser.getEmail());
				}
			}

			if (user.getLead() != null) {
				String leadName = mstUserRepository.findByUserIdAndIsLiveTrue(user.getLead()).getEmail();
				if (leadName != null && !leadName.equals("")) {
					ccEmailLists.add(leadName);
				}
			}

			if (user.getEmail() != null && !user.getEmail().equals("")) {
				ccEmailLists.add(user.getEmail());
			}
			//
			if (!toEmailLists.isEmpty()) {
				sendingMailToLeadPMAndTag(toEmailLists, subjectOfMail + facilityName, messageBody, "Admin", "Sir/Madam",
						agendareportMap, ccEmailLists);// mail
			}

		}

		else if (loginRoleId == 3) {

			if (user.getEmail() != null && !user.getEmail().equals("")) {
				ccEmailLists.add(user.getEmail());
			}

			if (!toEmailLists.isEmpty()) {
				sendingMailToLeadPMAndTag(toEmailLists, subjectOfMail + facilityName, messageBody, "Admin", "Sir/Madam",
						agendareportMap, ccEmailLists);// mail
			}
		}

		else if (loginRoleId == 15) {

			if (user.getEmail() != null && !user.getEmail().equals("")) {
				ccEmailLists.add(user.getEmail());
			}

			sendingMailToLeadPMAndTag(ccEmailLists, subjectOfMail + facilityName, messageBody, "Admin", "Sir/Madam",
					agendareportMap, null);

		}

	}

	private void sendingMailToLeadPMAndTag(List<String> emailLists, String subject, String message, String fromUserName,
			String toUserName, Map<String, String> agendareportMap, List<String> ccleadPMList) throws Exception {
		MailModel mailModel = new MailModel();
		mailModel.setAttachments(agendareportMap);
		mailModel.setToEmailIds(emailLists);
		mailModel.setCcEmailIds(ccleadPMList);
		mailModel.setSubject(subject);
		mailModel.setMessage(message);
		mailModel.setFromUserName(fromUserName);
		mailModel.setToUserName(toUserName);

		mailService.sendMail(mailModel);

	}

	@Override
	@Transactional
	public ReturnModel uploadPlanningReport(TxnPlanningModel txnPlanningModel) {
		ReturnModel returnModel = new ReturnModel();

		TXNPlanning txnPlanning = txnPlanningRepository.findByPlanningIdAndIsLiveTrue(txnPlanningModel.getPlanningId());
		if (txnPlanning != null) {
			String path = applicationMessageSource.getMessage(Constants.Web.FILE_PATH_PDSA, null, null);
			File filePath = new File(path);
			try {
				txnPlanning.setVisitedDate(new Timestamp(sdf.parse(txnPlanningModel.getDate()).getTime()));
				txnPlanning.setDescription(txnPlanningModel.getTripDescription());

				if (!filePath.exists())
					filePath.mkdir();
				// creating directory within planning folder with name planning
				File directory = new File(
						path + applicationMessageSource.getMessage(Constants.Web.FILEPATH_PLANNING_FOLDER, null, null));
				if (!directory.exists())
					directory.mkdirs();

				File tripDirectory = new File(directory.getAbsolutePath()
						+ applicationMessageSource.getMessage(Constants.Web.FILEPATH_PLANNING_TRIP_FOLDER, null, null));
				if (!tripDirectory.exists())
					tripDirectory.mkdirs();

				String filePathString = tripDirectory.getAbsolutePath() + File.separator
						+ txnPlanning.getFacility().getAreaName() + "_" + sdf.format(txnPlanning.getPlanDate()) + "_"
						+ txnPlanningModel.getDocument().getOriginalFilename();
				File file = new File(filePathString);
				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(file));

				buffStream.write(txnPlanningModel.getDocument().getBytes());
				buffStream.close();
				txnPlanning.setTripReportFilepath(filePathString);
				txnPlanningRepository.save(txnPlanning);
				returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.SUCCESS_STATUS_CODE, null, null));
				returnModel.setErrorMessage(
						errorMessageSource.getMessage(Constants.Web.SUCCESS_STATUS_PLANNING_CLOSE, null, null));
				return returnModel;
			}

			catch (Exception e) {
				LOGGER.error(e);
				returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));

				returnModel.setErrorMessage(
						errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_PLANNING_CLOSE, null, null));
				return returnModel;
			}
		} else {
			returnModel.setStatusCode(errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_CODE, null, null));
			returnModel.setErrorMessage(
					errorMessageSource.getMessage(Constants.Web.FAILURE_STATUS_PLANNING_CLOSE, null, null));
		}

		return returnModel;
	}

}
