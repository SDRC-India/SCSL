package org.sdrc.scsl.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.MSTEngagementScore;
import org.sdrc.scsl.domain.MSTUser;
import org.sdrc.scsl.domain.TXNEngagementScore;
import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.domain.UserAreaMapping;
import org.sdrc.scsl.model.web.EngagementAreaModel;
import org.sdrc.scsl.model.web.FacilityModel;
import org.sdrc.scsl.model.web.LineChartDataModel;
import org.sdrc.scsl.model.web.MSTEngagementScoreAndFacilityCollectionModel;
import org.sdrc.scsl.model.web.MSTEngagementScoreModel;
import org.sdrc.scsl.model.web.MailModel;
import org.sdrc.scsl.model.web.TimePeriodModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.MSTEngagementScoreRepository;
import org.sdrc.scsl.repository.MSTUserRepository;
import org.sdrc.scsl.repository.TXNEngagementScoreRepository;
import org.sdrc.scsl.repository.TimePeriodRepository;
import org.sdrc.scsl.repository.UserAreaMappingRepository;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * This service will hold all the methods required in engagement score data entry
 */
@Service
public class EngagementScoreServiceImpl implements EngagementScoreService {

	@Autowired
	private MSTEngagementScoreRepository mstEngagementScoreRepository;

	@Autowired
	private UserAreaMappingRepository userAreaMappingRepository;

	@Autowired
	private TXNEngagementScoreRepository txnEngagementScoreRepository;

	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private StateManager stateManager;

	@Autowired
	private ResourceBundleMessageSource messages;

	@Autowired
	private MailService mailService;

	@Autowired
	private MSTUserRepository mSTUserRepository;

	private SimpleDateFormat simpleDateformater = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_FORMATTER_HYPHEN));

	private static final Logger LOGGER=Logger.getLogger(EngagementScoreServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.EngagementScoreService#getMSTEngagementScoreData()
	 * This method will return the Master Engagement Score and Facility area and
	 * Timeperiod Facility Area will be according to facility assigned to that
	 * user TimePeriod List will be within the Facility Object ,as each Facility
	 * all the Timeperiod
	 */
	@Override
	@Transactional
	public MSTEngagementScoreAndFacilityCollectionModel getMSTEngagementScoreData() {

		// finding all the engagement Score
		List<MSTEngagementScore> mstEngagementScores = mstEngagementScoreRepository
				.findAll();

		UserModel userModel = (UserModel) stateManager
				.getValue(Constants.Web.USER_PRINCIPAL);
		// Fetching the facilities assigned to the logged in user
		List<UserAreaMapping> userAreaMappings =new ArrayList<>();
		List<Area> areas=new ArrayList<Area>();
		if(userModel.getAreaLevel()==4)
		{
			userAreaMappings = userAreaMappingRepository
					.findByUserUserIdAndIsLiveTrue(userModel.getUserId());
		}
		else
		{
			if(userModel.getAreaLevel()==1)
			{
				areas=areaRepository.findByLevel(Integer.parseInt(messages
				.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
			}
			else if(userModel.getAreaLevel()==2)
			{
				areas=areaRepository.findAllByParentAreaId(userModel.getFacilityId());
			}
			else if(userModel.getAreaLevel()==3)
			{
				areas=areaRepository.findAreaByParentAreaId(userModel.getFacilityId());
			}
			
		}
		if(userModel.getAreaLevel()!=4)
		{
			userAreaMappings =new ArrayList<>();
			for(Area area:areas)
			{
				UserAreaMapping areaMapping=new UserAreaMapping();
				areaMapping.setFacility(area);
				userAreaMappings.add(areaMapping);
			}
			
		}

		// fetching all the engagement score filled up
		List<TXNEngagementScore> txnEngagementScores = txnEngagementScoreRepository
				.findAll();

		// fetching all the timeperiod
		List<TimePeriod> timePeriods = timePeriodRepository
				.findByTimePeriodIdGreaterThanEqualAndPeriodicityOrderByTimePeriodIdAsc(Integer.parseInt(
						messages.getMessage(Constants.Web.ENGAGEMENT_SCORE_STARTING_TIMEPERIOD_ID, null, null)),
						messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null));
		
		Map<String, TXNEngagementScore> txnEngagementScoreMap = new HashMap<>();

		// putting engagement score filled up in a map against key where key
		// will be combination of timeperiodId and FacilityId as one facility
		// can have only one score for a timeperiod
		if (!txnEngagementScores.isEmpty())
			for (TXNEngagementScore txnEngagementScore : txnEngagementScores) {
				txnEngagementScoreMap.put(txnEngagementScore.getFacility()
						.getAreaId()
						+ "_"
						+ txnEngagementScore.getTimePeriod().getTimePeriodId(),
						txnEngagementScore);
			}

		List<MSTEngagementScoreModel> engagementScoreModels = new ArrayList<>();
		List<FacilityModel> facilityModels = new ArrayList<>();

		for (UserAreaMapping userAreaMapping : userAreaMappings) {

			Area facility = userAreaMapping.getFacility();
			FacilityModel facilityModel = new FacilityModel();
			facilityModel.setFacailityId(facility.getAreaId());
			facilityModel.setFacilityName(facility.getAreaName());
			List<TimePeriodModel> timePeriodModels = new ArrayList<>();
			int timePeriodId = 0;

			// putting timeperiod within the Facility Model

			boolean flag = true;
			
			for (TimePeriod timePeriod : filterArea(timePeriods, waveWiseTimePeriod(facility.getWave()))) {

				TimePeriodModel timePeriodModel = new TimePeriodModel();

				timePeriodModel.setTimePeriod(timePeriod.getTimePeriod());
				timePeriodModel.setTimePeriodId(timePeriod.getTimePeriodId());
				timePeriodModel.setEndDate(simpleDateformater.format(timePeriod
						.getEndDate()));
				timePeriodModel.setStartDate(simpleDateformater
						.format(timePeriod.getStartDate()));
				timePeriodModel.setWave(timePeriod.getWave());
				timePeriodModel.setPeriodicity(timePeriod.getPeriodicity());

				// Checking whether the current facility and timperiod has any
				// score in txnEngamentScore table or not
				// if yes score is present then css class will be set to
				// disabled
				// and respective score will be sent

				if (txnEngagementScoreMap.containsKey(facilityModel
						.getFacailityId() + "_" + timePeriod.getTimePeriodId())
						|| !flag) {
					timePeriodModel.setCssClass("disabled");
					if (txnEngagementScoreMap.containsKey(facilityModel
							.getFacailityId()
							+ "_"
							+ timePeriod.getTimePeriodId())) {
						timePeriodModel
								.setMstEngagementScoreId(txnEngagementScoreMap
										.get(facilityModel.getFacailityId()
												+ "_"
												+ timePeriod.getTimePeriodId())
										.getEngagementScore()
										.getMstEngagementScoreId());
						timePeriodId = timePeriod.getTimePeriodId();
					}
				} else {
					flag = false;
					timePeriodModel.setCssClass("enabled");
				}

				timePeriodModels.add(timePeriodModel);

			}
			Collections.reverse(timePeriodModels);
			facilityModel.setTimeperiods(timePeriodModels);
			facilityModel.setTimePeriodId(timePeriodId);
			facilityModels.add(facilityModel);

		}

		// Checking if above logged in user has any facility
		// if he don't have any facility then this block of code will not be
		// executed
		if (!facilityModels.isEmpty()) {
			for (MSTEngagementScore mstEngagementScore : mstEngagementScores) {
				MSTEngagementScoreModel engagementScoreModel = new MSTEngagementScoreModel();

				engagementScoreModel.setDefinition(mstEngagementScore
						.getDefinition());
				engagementScoreModel.setMstEngagementScoreId(mstEngagementScore
						.getMstEngagementScoreId());
				engagementScoreModel.setProgress(mstEngagementScore
						.getProgress());
				engagementScoreModel.setScore(mstEngagementScore.getScore());

				engagementScoreModels.add(engagementScoreModel);
			}
		}

		MSTEngagementScoreAndFacilityCollectionModel mstEngagementScoreAndFacilityCollectionModel = new MSTEngagementScoreAndFacilityCollectionModel();

		mstEngagementScoreAndFacilityCollectionModel
				.setFacilityModel(facilityModels);
		mstEngagementScoreAndFacilityCollectionModel
				.setMstEngagementScoreModel(engagementScoreModels);

		return mstEngagementScoreAndFacilityCollectionModel;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.EngagementScoreService#persistTXNEngagementScore(int, int, int)
	 * This method will persist the engagement score for a selected Facility and
	 * selected Timperiod and selected score from the score option
	 */
	@Override
	@Transactional
	public boolean persistTXNEngagementScore(int engagementScoreId,
			int facilityId, int timeperiodId) {

		UserModel userModel = (UserModel) stateManager
				.getValue(Constants.Web.USER_PRINCIPAL);
		
		TXNEngagementScore txnEngagementScore = new TXNEngagementScore();
		MSTEngagementScore mstEngagementScore = mstEngagementScoreRepository
				.findByMstEngagementScoreId(engagementScoreId);
		
		//sarita code
		//check if already exist for the particular time and facility (this scenario may occur while entering simultaneous data in app and web)
		TXNEngagementScore alreadyTxnEng = txnEngagementScoreRepository.findByAreaIdTimePeriod(facilityId, timeperiodId);
		
		if(null!= alreadyTxnEng){
			return false;
		}
		//sarita code end
		
		Area facility = areaRepository.findByAreaId(facilityId);
		TimePeriod timePeriod = timePeriodRepository
				.findByTimePeriodId(timeperiodId);

		if (mstEngagementScore != null && facility != null
				&& timePeriod != null) {
			txnEngagementScore.setEngagementScore(mstEngagementScore);
			txnEngagementScore.setFacility(facility);
			txnEngagementScore.setTimePeriod(timePeriod);
			txnEngagementScore.setCreatedDate(new Timestamp(new Date()
					.getTime()));
			txnEngagementScore.setCreatedBy(userModel.getUsername());
			txnEngagementScore.setUser(new MSTUser(userModel.getUserId()));
			txnEngagementScore.setIsWeb(true);
			txnEngagementScoreRepository.save(txnEngagementScore);

			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.EngagementScoreService#getLineChartEngagementScoreOfAFacility(int)
	 * This method will return the last 12 submission of engagement score for a
	 * selected Facility(Area)
	 */
	@Override
	public List<LineChartDataModel> getLineChartEngagementScoreOfAFacility(
			int facilityId) {
		List<TXNEngagementScore> txnEnagemeEngagementScores = txnEngagementScoreRepository
				.findTop12ByFacilityAreaIdOrderByTimePeriodTimePeriodIdDesc(facilityId);

		List<LineChartDataModel> lineChartDataModels = new ArrayList<>();

		for (TXNEngagementScore txnEngagementScore : txnEnagemeEngagementScores) {
			LineChartDataModel lineChartDataModel = new LineChartDataModel();
			lineChartDataModel.setAxis(txnEngagementScore.getTimePeriod()
					.getTimePeriod());
			lineChartDataModel.setValue(String.valueOf(txnEngagementScore
					.getEngagementScore().getScore()));

			lineChartDataModels.add(lineChartDataModel);

		}

		Collections.reverse(lineChartDataModels);
		return lineChartDataModels;
	}


	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.EngagementScoreService#getEngagementAreaModel()
	 * @author Sarita Panigrahi
	 * This method will provide all google map facility view geographic data of the engagement score for the particular facility for the last reported month.
	 */
	@Override
	@Transactional
	public List<EngagementAreaModel> getEngagementAreaModel() {
		List<EngagementAreaModel> listOfEngagementModel = new ArrayList<>();
		Map<Integer, Area> areaIdNameMap = new HashMap<>();
		List<Area> areas = areaRepository.findAll();
		for (Area area : areas) {
			areaIdNameMap.put(area.getAreaId(), area);
		}
		String bellowPushPin = "resources/images/allPins/EVM_googlemap_icon_red.png";
		String mediumPushPin = "resources/images/allPins/EVM_googlemap_icon_orange.png";
		String abovePushPin = "resources/images/allPins/EVM_googlemap_icon_green.png";
		for (TXNEngagementScore txnEngagementScore : txnEngagementScoreRepository.findLatestFacilityData()) {
			
			String pushPin = "";
			EngagementAreaModel engagementAreaModel = new EngagementAreaModel();

			engagementAreaModel.setDefinition(txnEngagementScore
					.getEngagementScore().getDefinition());
			engagementAreaModel.setDisrictId(txnEngagementScore.getFacility()
					.getParentAreaId());
			engagementAreaModel.setDistrictName(areaIdNameMap
					.get(txnEngagementScore.getFacility().getParentAreaId()).getAreaName());
			
			engagementAreaModel.setSateId(areaIdNameMap
					.get(areaIdNameMap.get(txnEngagementScore.getFacility().getParentAreaId()).getParentAreaId())
					.getAreaId());
			engagementAreaModel.setStateName(areaIdNameMap
					.get(areaIdNameMap.get(txnEngagementScore.getFacility().getParentAreaId()).getParentAreaId())
					.getAreaName());
			engagementAreaModel.setFacilityIypeId(txnEngagementScore
					.getFacility().getFacilityType().getTypeDetailId());
			engagementAreaModel.setFacilityType(txnEngagementScore
					.getFacility().getFacilityType().getTypeDetail());
			engagementAreaModel.setFacilitySizeId(txnEngagementScore
					.getFacility().getFacilitySize() == null ? 0
					: txnEngagementScore.getFacility().getFacilitySize()
							.getTypeDetailId());
			engagementAreaModel.setFacilitySizeName(txnEngagementScore
					.getFacility().getFacilitySize() == null ? ""
					: txnEngagementScore.getFacility().getFacilitySize()
							.getTypeDetail());
			engagementAreaModel.setId(txnEngagementScore
					.getTxnEngagementScoreId());
			engagementAreaModel.setProgress(txnEngagementScore
					.getEngagementScore().getProgress());
			engagementAreaModel.setScore((double) txnEngagementScore
					.getEngagementScore().getScore());
			engagementAreaModel.setTimePeriod(txnEngagementScore
					.getTimePeriod().getTimePeriodId());
			engagementAreaModel.setTimePeriodStr(txnEngagementScore
					.getTimePeriod().getTimePeriod());
			engagementAreaModel.setFacilityId(txnEngagementScore.getFacility()
					.getAreaId());
			engagementAreaModel.setTitle(txnEngagementScore
					.getFacility().getAreaName());
			String gps = txnEngagementScore.getFacility().getGps();
			engagementAreaModel.setLongitude(gps!=null ? gps.split(",")[1] : null);
			engagementAreaModel.setLatitude(gps!=null ? gps.split(",")[0] : null);
			engagementAreaModel.setDateOfVisit(simpleDateformater.format(txnEngagementScore.getCreatedDate()));
			
			double score = (double) txnEngagementScore
					.getEngagementScore().getScore();
			if (score >= 4) {
				pushPin = abovePushPin;
			} else if (score >= 2) {
				pushPin = mediumPushPin;
			} else if(score < 2) {
				pushPin = bellowPushPin;
			}
			engagementAreaModel.setIcon(pushPin);
			listOfEngagementModel.add(engagementAreaModel);
		}
		return listOfEngagementModel;

	}

	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.EngagementScoreService#pendingEngagementScoreMailService()
	 * This method will send the mail for the pending 
	 */
	@Override
	public boolean pendingEngagementScoreMailService() {
		
		//bringing all the facilities
		List<Area> areas = areaRepository.findByLevel(Integer.parseInt(messages
				.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
		// fetching the current timeperiod
		TimePeriod timePeriod = timePeriodRepository
				.findTop1ByPeriodicityOrderByTimePeriodIdDesc(messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null));

		// finding all the users
		List<MSTUser> mstUsers = mSTUserRepository.findByIsLiveTrueOrderByUserIdAsc();

		Map<Integer, MSTUser> mstUsersMap = new HashMap<>();
		for (MSTUser mstUser : mstUsers) {

			mstUsersMap.put(mstUser.getUserId(), mstUser);
		}
		
		Map<Integer, Area> facilityAreaMap = new HashMap<>();
		for (Area area : areas) {
			facilityAreaMap.put(area.getAreaId(), area);
		}
		
		// bringing all the facility which have submission in latest timeperiod
		List<Integer> facilityIdWithSubmissionLatestSubmision = txnEngagementScoreRepository
				.findFacilityWithCurrentMonthSubmission();
		
		// checking if any facility has latest submission or not
		if (facilityIdWithSubmissionLatestSubmision != null
				&& !facilityIdWithSubmissionLatestSubmision.isEmpty()) {
			
			// now we will remove these timeperiod from the facilityMap.We don't need them
			for (int facilityId : facilityIdWithSubmissionLatestSubmision) {
				facilityAreaMap.remove(facilityId);
			}

		}
		// bringing roleID of the ahi associate
		int roleID = Integer.parseInt(messages.getMessage(
				Constants.Web.AHI_ASSOCIATE_ROLE_ID, null, null));
		
		// finding all the ahi associate users who are assigned to facilities who has pending submission  
		List<UserAreaMapping> userAreaMappings = userAreaMappingRepository
				.findDistinctByFacilityAreaIdInAndUserRoleFeaturePermissionMappingsRoleFeaturePermissionSchemeRoleRoleId(
						facilityAreaMap.keySet(), roleID);
		
		List<String> facilityName = new ArrayList<>();
		
		// Sending mail to each user
		for (UserAreaMapping userAreaMapping : userAreaMappings) {
			String subject = "SCSL: Pending Engagement Score";

			String message = "The Engagement Score for "
					+ userAreaMapping.getFacility().getAreaName()
					+ " of the month " + timePeriod.getTimePeriod()
					+ " is pending .Kindly submit as soon as possible";

			facilityName.add(userAreaMapping.getFacility().getAreaName());
			MailModel mailModel = new MailModel();

			List<String> ccEmailIds = new ArrayList<>();
			List<String> emails = new ArrayList<>();
			emails.add(userAreaMapping.getUser().getEmail());

			mailModel.setToEmailIds(emails);
			if (userAreaMapping.getUser().getLead() != null) {
				ccEmailIds.add(mstUsersMap.get(
						userAreaMapping.getUser().getLead()).getEmail());
			}
			mailModel.setCcEmailIds(ccEmailIds);

			mailModel.setFromUserName("System");
			mailModel.setMessage(message);
			mailModel.setSubject(subject);
			mailModel.setToUserName(userAreaMapping.getUser().getName());

			// send the mail

			try {
				mailService.sendMail(mailModel);
			} catch (Exception e) {
				LOGGER.error("exception while sending mail",e);
			}
		}
		// bringing mail of ahi project manager and operation lead
		List<MSTUser> users = mSTUserRepository
				.findDistinctByUserAreaMappingsUserRoleFeaturePermissionMappingsRoleFeaturePermissionSchemeRoleRoleIdIn(
						Arrays.asList(
								Integer.parseInt(messages.getMessage(Constants.Web.AHI_PROJECT_MANAGER, null, null)),
								Integer.parseInt(
										messages.getMessage(Constants.Web.AHI_OPERATION_LEAD_ROLE_ID, null, null)),
								Integer.parseInt(messages.getMessage(Constants.Web.AHI_MNE_LEAD, null, null))));
		
		// checking if any user we have in our data base who is assigned role of project manager
		if(users!=null && !users.isEmpty())
		{
			List<String> emails = new ArrayList<>();
			List<String> ccEmailIds = new ArrayList<>();
			
			emails.add(users.get(0).getEmail());
			
			for (int i=1; i< users.size(); i++) {
				ccEmailIds.add(users.get(i).getEmail());
			}
			MailModel mailModel = new MailModel();
			String subject = "SCSL: Pending Engagement Score";
		
			String message = "The Engagement Score of following facility is pending of the month "
					+ timePeriod.getTimePeriod() + "<br>";
			int i = 1;
			// attaching name of each facility with pending submssion
			for (Map.Entry<Integer, Area> entry : facilityAreaMap.entrySet()) {
				message += ("<br>" + i + "." + entry.getValue().getAreaName());
				i++;
			}
			mailModel.setToEmailIds(emails);
			mailModel.setCcEmailIds(ccEmailIds);

			mailModel.setFromUserName("System");
			mailModel.setMessage(message);
			mailModel.setSubject(subject);
			mailModel.setToUserName(users.get(0).getName());

			try {
				mailService.sendMail(mailModel);
			} catch (Exception e) {
				LOGGER.error("exception while sending mail",e);
			}
		}
		return false;
	}
	
	// filter timeperiod
	public static List<TimePeriod> filterArea(List<TimePeriod> timePeriods, Predicate<TimePeriod> predicate) {
		return timePeriods.stream().filter(predicate).collect(Collectors.<TimePeriod>toList());
	}

	// predicates
	// predict for wave wise facility
	//wave 1 facility will carry forward for wave 2 as well as wave 2 and wave 3
	//wave 2 for 2 and 3
	// wave 3 for only 3
	public static Predicate<TimePeriod> waveWiseTimePeriod(int wave) {
		return t -> wave == 1 ? t.getWave() == wave || t.getWave() == 2 || t.getWave() == 3
				: wave == 2 ? t.getWave() == wave || t.getWave() == 3 : t.getWave() == wave;
	}
}
