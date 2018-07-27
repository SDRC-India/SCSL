package org.sdrc.scsl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.scsl.domain.AggregateData;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.Indicator;
import org.sdrc.scsl.domain.IndicatorFacilityTimeperiodMapping;
import org.sdrc.scsl.domain.PDSA;
import org.sdrc.scsl.domain.TXNPDSA;
import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.model.web.BoxChart;
import org.sdrc.scsl.model.web.ControlChartModel;
import org.sdrc.scsl.model.web.DashboardChartModel;
import org.sdrc.scsl.model.web.DashboardLandingModel;
import org.sdrc.scsl.model.web.IndicatorModel;
import org.sdrc.scsl.model.web.PDSAModel;
import org.sdrc.scsl.model.web.PDSASummaryModel;
import org.sdrc.scsl.model.web.SmallMultipleExcelModel;
import org.sdrc.scsl.model.web.ValueModel;
import org.sdrc.scsl.repository.AggregateDataRepository;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.IndicatorFacilityTimeperiodMappingRepository;
import org.sdrc.scsl.repository.IndicatorRepository;
import org.sdrc.scsl.repository.PDSARepository;
import org.sdrc.scsl.repository.TXNPDSARepository;
import org.sdrc.scsl.repository.TimePeriodRepository;
import org.sdrc.scsl.util.Constants;
import org.sdrc.scsl.util.ControlChartDataProvider;
import org.sdrc.scsl.util.ImageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in) This service holds all the
 *         methods to be called in dashboard home, facility view, small multiple
 *         and pdsa summary created on: 21-09-2017
 *
 */
@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private ResourceBundleMessageSource dashboardmessages;

	@Autowired
	private ResourceBundleMessageSource messages;

	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private AggregateDataRepository aggregateDataRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private ResourceBundleMessageSource applicationMessageSource;
	
	@Autowired
	private TXNPDSARepository txnPDSARepository;
	
	@Autowired
	private PDSARepository pdsaRepository;
	
	@Autowired
	private PDSAService pdsaService;
	
	@Autowired
	private IndicatorRepository indicatorRepository;
	
	@Autowired
	private IndicatorFacilityTimeperiodMappingRepository indicatorFacilityTimeperiodMappingRepository;
	
	private static DecimalFormat df = new DecimalFormat("#.##");
	
	private SimpleDateFormat sdf = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.DATE_MONTH_YEAR_FORMATTER));
	private SimpleDateFormat format = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.DATE_MONTH_YEAR_FORMATTER_NO_HYPHEN));
	private SimpleDateFormat monthYearSdf = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.MONTH_YEAR_FORMATTER_NO_HYPHEN));
	private static final Logger LOGGER=Logger.getLogger(DashboardServiceImpl.class);
	private SimpleDateFormat timeFormatter = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_TIME_FORMATTER_NO_HYPHEN));
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.service.DashboardService#getAllLandingBoxValue()
	 * 
	 * @author Sarita Panigrahi For top line grid/box view key performing
	 * indicator values in Dashboard Home All static kpi values are provided by
	 * "landingdashboardgrid.json", and the dynamic ones derived from system are
	 * being provided by this method Get the kpi indicators from
	 * "dashboardmessages.properties" and then get respective latest time
	 * period's aggregated data values for Andhra and Telangana
	 */
	@Override
	public List<BoxChart> getAllLandingBoxValue() {

			try{
				
			//2016-jan, 2016-dec (sum)//17,18
			List<Integer> listOf12TimePeriod = timePeriodRepository.findByTimePeriodIdBetweenAndPeriodicity(
					Integer.parseInt(messages.getMessage(Constants.Web.JANUARY_2016_TIMEPERIOD_ID, null, null)),
					Integer.parseInt(messages.getMessage(Constants.Web.DECEMBER_2016_TIMEPERIOD_ID, null, null)),
					messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null));
	
			List<Area> areas = areaRepository.findAllByOrderByAreaIdAsc();
			// get the state level area ids
			List<Area> states = filterArea(areas,
					getByAreaLevel(Integer.parseInt(messages.getMessage(Constants.Web.STATE_LEVEL_ID, null, null))));
	
			List<Integer> stateIds = new ArrayList<>();
			states.forEach(area -> stateIds.add(area.getAreaId()));
	
			// get the required aggregated data
			// we will get Telangana 1st then Andhra Pradesh for order by areaid
			List<Object[]> aggregateDatas = aggregateDataRepository.findByIndicatorIdTimePeriodIdAndAreaId(
					Arrays.asList(Integer.parseInt(dashboardmessages.getMessage("number.of.live.births", null, null))),
					listOf12TimePeriod, stateIds,
					Integer.parseInt(messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null)), 0); // wave
																												// =
																												// 0
																												// for
																												// all
																												// wave
	
			List<BoxChart> boxCharts = new ArrayList<>();
			// 1st we will put the telangana data then andhra
	
			Long liveBirthAndhraValue = aggregateDatas.size() > 1 ? (Long)aggregateDatas.get(1)[0] : null;
			Long liveBirthTelanganaValue = !aggregateDatas.isEmpty() ? (Long)aggregateDatas.get(0)[0] : null;
			String programName = messages.getMessage(Constants.Web.SCSL_PROGRAM_NAME, null, null);
			// indicator name remains same in both index
			boxCharts.add(
					setBoxChart(liveBirthAndhraValue, liveBirthTelanganaValue,
							"Number of live births in the SNCUs", programName, "Number"));
	
			aggregateDatas = aggregateDataRepository.findByIndicatorIdTimePeriodIdAndAreaId(
					Arrays.asList(Integer.parseInt(dashboardmessages.getMessage("numberof.neonates.admitted", null, null))),
					listOf12TimePeriod, stateIds,
					Integer.parseInt(messages.getMessage(Constants.Web.PUBLIC_TYPEDETAIL_ID, null, null)), 0); // wave
																												// =
																												// 0
																												// for
																												// all
																												// wave
			Long neonatesAndhraValue = aggregateDatas.size() > 1 ? (Long)aggregateDatas.get(1)[0] : null;
			Long neonatesTelanganaValue = !aggregateDatas.isEmpty() ? (Long)aggregateDatas.get(0)[0] : null;
			// 2nd one
			boxCharts.add(
					setBoxChart(neonatesAndhraValue, neonatesTelanganaValue,
							"Number of neonates admitted in the SNCUs (public)",programName, "Number"));
	
			// get facility count under andhra and telangana where there is labor
			// room (hasLr=true)
			List<Area> telanganaLrFacilities = areaRepository.findByHasLrTrueAndLevel(
					Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)), stateIds.get(0)); // stateIds.get(0)
																															// =
																															// 2(id
																															// of
																															// telangana)
			List<Area> andhraLrFacilities = areaRepository.findByHasLrTrueAndLevel(
					Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)), stateIds.get(1));// stateIds.get(1)
																															// =
																															// 2(id
																															// of
																															// andhra)
	
			// Number of public, private Special New Born Care Unit
			
			//get all public facilities (andhra / telangana)
			boxCharts
					.add(setBoxChart(
							Integer.valueOf(filterArea(andhraLrFacilities,
									typeWiseFacility(Integer
											.parseInt(messages.getMessage(Constants.Web.PUBLIC_TYPEDETAIL_ID, null, null))))
													.size())
									.longValue(),
							Integer.valueOf(filterArea(telanganaLrFacilities,
									typeWiseFacility(Integer
											.parseInt(messages.getMessage(Constants.Web.PUBLIC_TYPEDETAIL_ID, null, null))))
													.size())
									.longValue(),
							"Number of Public Special New Born Care Unit",programName, "Number"));
	
			//get all private facilities (andhra / telangana)
			boxCharts
					.add(setBoxChart(
							Integer.valueOf(
									filterArea(andhraLrFacilities,
											typeWiseFacility(Integer.parseInt(
													messages.getMessage(Constants.Web.PRIVATE_TYPEDETAIL_ID, null, null))))
															.size())
									.longValue(),
							Integer.valueOf(filterArea(telanganaLrFacilities,
									typeWiseFacility(Integer.parseInt(
											messages.getMessage(Constants.Web.PRIVATE_TYPEDETAIL_ID, null, null)))).size())
									.longValue(),
							"Number of Private Special New Born Care Units",programName, "Number"));
	
		
			//get all public facilities with LR (andhra / telangana)
			// Number of delivery points under Safe Care, Saving Lives
			boxCharts
					.add(setBoxChart(
							Integer.valueOf(filterArea(andhraLrFacilities,
									hasLrAndTypeWiseFacility(true,
											Integer.parseInt(
													messages.getMessage(Constants.Web.PUBLIC_TYPEDETAIL_ID, null, null))))
															.size())
									.longValue(),
							Integer.valueOf(filterArea(telanganaLrFacilities,
									hasLrAndTypeWiseFacility(true,
											Integer.parseInt(
													messages.getMessage(Constants.Web.PUBLIC_TYPEDETAIL_ID, null, null))))
															.size())
									.longValue(),
							"Number of public delivery points",programName, "Number"));
	
			// PERCENT OF PROJECT COVERAGE
	
			Long staticBirthTelangana = Long.valueOf(dashboardmessages.getMessage("number.of.births.static.telanganaValue", null, null));
			Long staticBirthAndhra = Long.valueOf(dashboardmessages.getMessage("number.of.births.static.andhraValue", null, null));
			Long staticNeonatesTelangana = Long.valueOf(dashboardmessages.getMessage("numberof.neonates.admitted.static.telanganaValue", null, null));
			Long staticNeonatesAndhra = Long.valueOf(dashboardmessages.getMessage("numberof.neonates.admitted.static.andhraValue", null, null));
			
			boxCharts.add(setBoxChart(
					liveBirthAndhraValue != null
							? Math.round(liveBirthAndhraValue.doubleValue() * 100/ staticBirthAndhra.doubleValue()) : null,
					liveBirthTelanganaValue != null
							? Math.round (liveBirthTelanganaValue.doubleValue() * 100 / staticBirthTelangana.doubleValue())
							: null,
					"Coverage of live births in the SNCUs",programName, "Percent"));
	
			boxCharts.add(setBoxChart(neonatesAndhraValue != null
					? Math.round (neonatesAndhraValue.doubleValue() *100/ staticNeonatesAndhra.doubleValue()) : null,
					neonatesTelanganaValue != null ? Math.round (neonatesTelanganaValue.doubleValue()  * 100
							/ staticNeonatesTelangana.doubleValue()) : null,
					"Coverage of neonates admitted in the SNCUs",programName, "Percent"));
	
			return boxCharts;
		}catch (Exception e) {
			LOGGER.error(e);
			return null;
		}

	}

	/** 
	 * @author Sarita Panigrahi, created on: 03-Oct-2017
	 * @param andhraValue
	 * @param telanganaValue
	 * @param name
	 * @param source
	 * @return
	 */
	private BoxChart setBoxChart(Long andhraValue, Long telanganaValue, String name, String source, String unit) {
		BoxChart boxChart = new BoxChart();
		boxChart.setName(name);
		boxChart.setAndhraValue(andhraValue);
		boxChart.setTelanganaValue(telanganaValue);
		boxChart.setSource(source);
		boxChart.setUnit(unit);
		return boxChart;
	}

	// filter area
	/** 
	 * @author Sarita Panigrahi, created on: 03-Oct-2017
	 * @param areas
	 * @param predicate
	 * @return
	 */
	public static List<Area> filterArea(List<Area> areas, Predicate<Area> predicate) {
		return areas.stream().filter(predicate).collect(Collectors.<Area>toList());
	}

	// predicates
	// predict for LR facilities
	/** 
	 * @author Sarita Panigrahi, created on: 03-Oct-2017
	 * @param hasLr
	 * @param facilityTypeId
	 * @return
	 */
	public static Predicate<Area> hasLrAndTypeWiseFacility(Boolean hasLr, Integer facilityTypeId) {
		return a -> a.getHasLr() == hasLr && a.getFacilityType().getTypeDetailId() == facilityTypeId;
	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 04-Oct-2017
	 * @param facilityTypeId
	 * @return
	 */
	public static Predicate<Area> typeWiseFacility(Integer facilityTypeId) {
		return a -> a.getFacilityType().getTypeDetailId() == facilityTypeId;
	}

	// predict for state and facility by level
	/** 
	 * @author Sarita Panigrahi, created on: 03-Oct-2017
	 * @param level
	 * @return
	 */
	public static Predicate<Area> getByAreaLevel(Integer level) {
		return a -> a.getLevel() == level;
	}

	// predict for facility by level and parent
	/** 
	 * @author Sarita Panigrahi, created on: 03-Oct-2017
	 * @param level
	 * @return
	 */
	public static Predicate<Area> getByAreaLevelAndParent(Integer level) {
		return a -> a.getLevel() == level;
	}

	/** 
	 * @author Sarita Panigrahi, created on: 07-Nov-2017
	 * @param hasLr
	 * @return
	 */
	public static Predicate<Area> hasLr(Boolean hasLr) {
		return a -> a.getHasLr() == hasLr ;
	}
	
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#getPDSAControlChart(java.lang.Integer)
	 * @author Sarita Panigrahi
	 * This method will get called upon click of each PDSA in summary page.
	 * Returns chart data
	 */
	@Override
	public Map<String, Object> getPDSAControlChart(Integer pdsaId) {
		
		try {
			Map<String, Object> lineChartMap = new LinkedHashMap<>();
	
			//get average / median of last 12 records
			Object txnPdsaAvgValObj = txnPDSARepository.findAveragePByFacilityAreaIdGroupByPdsaPdsaId(pdsaId);
			Double txnPdsaAverageVal = txnPdsaAvgValObj!=null ? Double
					.valueOf(txnPdsaAvgValObj.toString()) : null;
	
			//get last 12 transactions
			List<TXNPDSA> txnPDsas = txnPDSARepository.findByPdsaPdsaIdOrderByTxnPDSAIdAsc(pdsaId, new PageRequest(0, 12));
	
			List<DashboardChartModel> clDashboardChartModels = new ArrayList<>();
			List<List<DashboardChartModel>> allDashboardChartModels = new ArrayList<>();
	
			//iterate through last 12 txn pdsa
			for (TXNPDSA txnpdsa : txnPDsas) {
	
				// as per the formula (=SQRT((PAverage*(1-PAverage))/Denominator))
				
				ControlChartModel controlChartModel = ControlChartDataProvider
						.getControlChartData(txnpdsa.getNumeratorValue()!=null ?
								txnpdsa.getNumeratorValue().longValue() : null, txnpdsa.getDenominatorValue()!=null ?
										txnpdsa.getDenominatorValue().longValue() : null, txnPdsaAverageVal);
				
				String d = format.format(txnpdsa.getDueDate());
				
				clDashboardChartModels = getDashboardChartModels(controlChartModel, d, null, clDashboardChartModels, txnpdsa.getPdsa().getName(), "", null, null, null);
			}
	
			allDashboardChartModels.add(clDashboardChartModels);
			
			//all charts in one key
			lineChartMap.put("charts", allDashboardChartModels);
			
			//put median for chart
			lineChartMap.put("median", txnPdsaAverageVal!=null ? df.format(txnPdsaAverageVal) : null);
			return lineChartMap;
			
			} catch (Exception e) {
				LOGGER.error(e);
				return null;
		}

	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 04-Oct-2017
	 * @param key
	 * @param date
	 * @param name
	 * @param value
	 * @param pdsaStr
	 * @return
	 * set DashboardChartModel
	 */
	private DashboardChartModel setDashboardChartModel(String key, String date, 
				String name, Double value, String source, String pdsaStr, Integer timeNid, Integer indicatorOrder){
		DashboardChartModel dashboardChartModel = new DashboardChartModel();
		dashboardChartModel.setKey(key); //trend chart
		dashboardChartModel.setDate(date);
		dashboardChartModel.setName(name);
		dashboardChartModel.setValue(value);
		dashboardChartModel.setSource(source);
		dashboardChartModel.setPdsas(pdsaStr);
		dashboardChartModel.setTimeNid(timeNid);
		dashboardChartModel.setIndicatorOrder(indicatorOrder);
		return dashboardChartModel;
	}
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#getAllPDSAWithinAFacility(int)
	 * @author Harsh Pratysh
	 * @author Sarita Panigrahi
	 * this method will return all the PDSA information for a particular facility
	 */
	@Override
	@Transactional
	public List<PDSAModel> getAllPDSAWithinAFacility(int facilityId) {

		try {
			// getting al the pdsa within a facility
			List<PDSA> pdsas = pdsaRepository.findByFacilityAreaIdOrderByPdsaIdDesc(facilityId);
	
			List<Object[]> pdsaCompletions = txnPDSARepository
					.findCountByPercentageIsNotNullAndPdsaFacilityAreaIdGroupByPdsaPdsaId(facilityId);
	
			Map<Integer, Integer> pdsaCompletionMap = new HashMap<Integer, Integer>();
			for (Object[] pdsaCompletion : pdsaCompletions) {
				pdsaCompletionMap.put(Integer.parseInt(pdsaCompletion[1].toString()),
						Integer.parseInt(pdsaCompletion[0].toString()));
			}
	
			List<PDSAModel> pdsaModels = new ArrayList<PDSAModel>();
			Date date = Calendar.getInstance().getTime();
			for (PDSA pdsa : pdsas) {
				PDSAModel pdsaModel = new PDSAModel();
	
				pdsaModel.setChangeIdeaId(pdsa.getChangeIdea().getChangeIdeaId());
				pdsaModel.setChangeIdeaName(pdsa.getChangeIdea().getDescription());
				pdsaModel.setFrequency(pdsa.getFrequency());
				pdsaModel.setIndicatorId(pdsa.getIndicator().getIndicatorId());
				pdsaModel.setIndicatorName(pdsa.getIndicator().getIndicatorName());
				pdsaModel.setIndicatorType(pdsa.getIndicator().getIndicatorType().getTypeDetail());
				if (pdsa.getFirstDocFilepath() != null)
					pdsaModel.setFirstDocFilepath(pdsa.getFirstDocFilepath());
				pdsaModel.setPdsaId(pdsa.getPdsaId());
				pdsaModel.setPdsaName(pdsa.getName());
				pdsaModel.setPdsaNumber(pdsa.getPdsaNumber());
				pdsaModel.setSummary(pdsa.getSummary());
				pdsaModel.setCoreAreaName(pdsa.getIndicator().getCoreArea().getTypeDetail());
				pdsaModel.setCoreAreaId(pdsa.getIndicator().getCoreArea().getTypeDetailId());
				pdsaModel.setTimePeriod(format.format(pdsa.getStartDate()) + "-" + format.format(pdsa.getEndDate()));
				pdsaModel.setPdsaFrequency(pdsa.getPdsaFrequency());
				if (pdsaCompletionMap.containsKey(pdsa.getPdsaId()))
					pdsaModel.setNoOfPdsaCompleted(pdsaCompletionMap.get(pdsaModel.getPdsaId()));
				else
					pdsaModel.setNoOfPdsaCompleted(0);
				// checking wether pdsa is closed or not
				if (Arrays.asList(messages.getMessage(Constants.Web.STATUS_CODE_PDSA_CLOSED, null, null).split(","))
						.contains(String.valueOf(pdsa.getStatus().getTypeDetailId()))) {
					pdsaModel.setClosingRemarks(pdsa.getClossingRemarks());
					pdsaModel.setStatusId(pdsa.getStatus().getTypeDetailId());
					pdsaModel.setStatusName(pdsa.getStatus().getTypeDetail());
					pdsaModel.setLastDocFilepath(pdsa.getLastDocFilepath());
					if (pdsa.getOtherDocFilepath() != null)
						pdsaModel.setOtherDocFilePath(pdsa.getOtherDocFilepath());
					// if closed we will get its respective css class from property
					// file
					if (pdsa.getStatus().getTypeDetailId() == Integer
							.parseInt(messages.getMessage(Constants.Web.ABANDON_STATUS, null, null))) {
						pdsaModel.setCssClass(messages.getMessage(Constants.Web.ABANDON_STATUS_CSS, null, null));
					} else if (pdsa.getStatus().getTypeDetailId() == Integer
							.parseInt(messages.getMessage(Constants.Web.ADAPT_STATUS, null, null))) {
						pdsaModel.setCssClass(messages.getMessage(Constants.Web.ADAPT_STATUS_CSS, null, null));
					} else if (pdsa.getStatus().getTypeDetailId() == Integer
							.parseInt(messages.getMessage(Constants.Web.ADOPT_STATUS, null, null))) {
						pdsaModel.setCssClass(messages.getMessage(Constants.Web.ADOPT_STATUS_CSS, null, null));
					}
				} else {
					// if pdsa started
					if (!pdsa.getTxnpdsas().isEmpty()) {
						int i = 0;
						for (TXNPDSA txnPDSA : pdsa.getTxnpdsas()) {
							// if any of the txnpdsa is null and due date has
							// crossed then PDSA will be counted as the Pending
							if (txnPDSA.getDenominatorValue() == null && !DateUtils.isSameDay(date, txnPDSA.getDueDate())) {
								pdsaModel.setCssClass(messages.getMessage(Constants.Web.PENDING_STATUS_CSS, null, null));
								pdsaModel.setStatusName("Pending");
								pdsaModel.setStatusId(
										Integer.parseInt(messages.getMessage(Constants.Web.PENDING_STATUS, null, null)));
								break;
							}
							i++;
							// if PDSA is started buta ll the data is filled or the
							// blank data is to be submitted today then it will be
							// pending
							if (i == pdsa.getTxnpdsas().size()) {
								pdsaModel.setCssClass(messages.getMessage(Constants.Web.ONGOING_STATUS_CSS, null, null));
								pdsaModel.setStatusName("Ongoing");
								pdsaModel.setStatusId(
										Integer.parseInt(messages.getMessage(Constants.Web.ONGOING_STATUS, null, null)));
							}
	
						}
					}
					// if pdsa not yet started then it will be ongoing
					else {
						pdsaModel.setCssClass(messages.getMessage(Constants.Web.ONGOING_STATUS_CSS, null, null));
						pdsaModel.setStatusName("Ongoing");
						pdsaModel.setStatusId(
								Integer.parseInt(messages.getMessage(Constants.Web.ONGOING_STATUS, null, null)));
					}
	
				}
				pdsaModel.setStartDate(format.format(pdsa.getStartDate()));
				pdsaModel.setEndDate(format.format(pdsa.getEndDate()));
				pdsaModels.add(pdsaModel);
			}
			return pdsaModels;
			
		} catch (Exception e) {
			LOGGER.error(e);
			return null;
		}
	}
	
	
	//update all deo pdsa txn table
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#updateAllDeoPDSATxn()
	 * @author Sarita Panigrahi
	 */
//	@Override every night at 3.30am
	@Scheduled(cron = "0 30 3 * * ?") //second, minute, hour, day of month, month, day(s) of week
	public void updateAllDeoPDSATxn(){
		try {
			List<Integer> facilityIds = pdsaRepository.findDistinctFacilityId();
			for (Integer facilityId : facilityIds) {
				pdsaService.updateTXNPdsa(facilityId);
			}
		} catch (Exception e) {
			LOGGER.error("Error in updateAllDeoPDSATxn", e);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#getDashboardFacilityCharts(java.lang.Integer)
	 * @author Sarita Panigrahi
	 * Get Dashboard facility view charts for a selected facility
	 */
	@Override
	@Transactional
	public List<List<DashboardChartModel>> getDashboardFacilityCharts(Integer facilityId, Boolean hasLr){
		
		try {
			List<TimePeriod> timePeriods = timePeriodRepository.findTopByPeriodicityNotInMaxOrderByTimePeriodIdDesc(
					messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null),new PageRequest(0, 12));
			
			//get last 12 time period ids
			List<Integer> timePeriodIds = new ArrayList<>();
			timePeriods.forEach(timePeriod -> timePeriodIds.add(timePeriod.getTimePeriodId()));
			
			Map<Integer, Double> indicatorIdValueNameMap =  new HashMap<>();
			
			//pAverage [0] , indicator_id_fk [1]
			List<Object[]> pAverages = aggregateDataRepository.findPAverageGroupByIndicatorAndTimePeriodByFacilityId(timePeriodIds, facilityId);
			//put in a map to get it easily later
			
			//here we have distinct indicator id as it is group by indicator id
			for (Object[] objects : pAverages) {
				if(null!=objects[0]) //skip lr related intermediate where lr is not applicable for the facility
					indicatorIdValueNameMap.put((Integer) objects[1],Double.parseDouble(objects[0].toString()));
			}
			List<AggregateData> aggregateDatas = aggregateDataRepository
					.findByIndicatorIsProfileIsNullAndAreaAreaIdAndTimePeriodTimePeriodIdInOrderByTimePeriodTimePeriodIdAsc(
							facilityId, timePeriodIds);
			
			//we will put pdsa list for indicator key and date key (only start date month will be considered here)
			Map<Integer, Map<String,List<PDSA>>> indicatorIdDatePDSAsMap = new HashMap<>();
			Map<String,List<PDSA>> datePDSAsMap;
			//get the PDSAs for this facility--
			
			//GET ALL indicatorfacilitytimeperiodmapping for the last 12 tp.
			//through historical data all the process mappings will be added
			//as we know intermediate and outcomes are mandatory across the facilities
			
			List<Indicator> indicators;
			
			//if the facility has lr then pick all intermediate and outcome
			//else pick those where the indicator does not depend on lr
			if(!hasLr)
				indicators = indicatorRepository.findByIndicatorTypeTypeDetailIdInAndIsLrIsNullOrderByIndicatorOrderAsc(
						Arrays.asList(Integer.parseInt(messages.getMessage(
						Constants.Web.INDICATOR_TYPE_INTERMEDIATE, null, null)),Integer.parseInt(messages.getMessage(
								Constants.Web.INDICATOR_TYPE_OUTCOME, null, null))));
			else
				indicators = indicatorRepository.findByIndicatorTypeTypeDetailIdInOrderByIndicatorOrderAsc(
						Arrays.asList(Integer.parseInt(messages.getMessage(
						Constants.Web.INDICATOR_TYPE_INTERMEDIATE, null, null)),Integer.parseInt(messages.getMessage(
								Constants.Web.INDICATOR_TYPE_OUTCOME, null, null))));
			
			List<String> indIdNameList = new ArrayList<>();
			indicators.forEach(ind -> indIdNameList.add(ind.getIndicatorId().toString()+"_"
					+ind.getIndicatorName()+"_"+ind.getIndicatorOrder()
			));
			
			//first put all intermediate and outcome inds
			Map<String, List<String>> timePeriodIndListMap = new LinkedHashMap<>();
			for(TimePeriod timePeriod:timePeriods){
				timePeriodIndListMap.put(timePeriod.getTimePeriodId().toString()+"_"+timePeriod.getTimePeriod(), new ArrayList<>(indIdNameList) ); //create a new object of list everytime
			}
			
			List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings = indicatorFacilityTimeperiodMappingRepository
					.findByIndicatorIndicatorTypeTypeDetailIdAndFacilityAreaIdAndTimePeriodTimePeriodIdIn(Integer.parseInt(messages.getMessage(
							Constants.Web.INDICATOR_TYPE_PROCESS, null, null)), facilityId, timePeriodIds);
			
			//then put mapped process inds
			for (IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping : indicatorFacilityTimeperiodMappings) {
				
				Integer timeId = indicatorFacilityTimeperiodMapping.getTimePeriod().getTimePeriodId();
				String timePeriodStr = indicatorFacilityTimeperiodMapping.getTimePeriod().getTimePeriod();
				
				//if the area doesn't have lr but it is a lr related intermediate indicator, then skip it
				//as we have kept the mapping to show in data entry view
				if(!hasLr && null!=indicatorFacilityTimeperiodMapping.getIndicator().getIsLr() && indicatorFacilityTimeperiodMapping.getIndicator().getIsLr())
					continue;
				
				if (timePeriodIndListMap.containsKey(timeId + "_" + timePeriodStr)) {

					// if the mapping is already present then skip
					if (!timePeriodIndListMap.get(timeId + "_" + timePeriodStr)
							.contains(indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId() + "_"
									+ indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorName() + "_"
									+ indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorOrder()))
						timePeriodIndListMap.get(timeId + "_" + timePeriodStr)
								.add(indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId() + "_"
										+ indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorName() + "_"
										+ indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorOrder());
				} else {
					List<String> indList = new ArrayList<>();
					indList.add(indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorId() + "_"
							+ indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorName() + "_"
							+ indicatorFacilityTimeperiodMapping.getIndicator().getIndicatorOrder());
					timePeriodIndListMap.put(timeId + "_" + timePeriodStr, indList);
				}
			}

			List<PDSA> pdsas = pdsaRepository.findByFacilityAreaId(facilityId);
			
			//we will put the pdsa list against indicator id and month year to get it easily while iterating aggregate data loop
			for (PDSA pdsa : pdsas) {
				
				String startMonth = monthYearSdf.format(pdsa.getStartDate());
				
				if (indicatorIdDatePDSAsMap.containsKey(pdsa.getIndicator().getIndicatorId())) {
					
					if(indicatorIdDatePDSAsMap.get(pdsa.getIndicator().getIndicatorId()).containsKey(startMonth)){
						
						indicatorIdDatePDSAsMap.get(pdsa.getIndicator().getIndicatorId()).get(startMonth).add(pdsa);
					}else{
						List<PDSA> pdsaList = new ArrayList<>();
						pdsaList.add(pdsa);
						indicatorIdDatePDSAsMap.get(pdsa.getIndicator().getIndicatorId()).put(startMonth, pdsaList);
					}
				} else {
					List<PDSA> pdsaList = new ArrayList<>();
					pdsaList.add(pdsa);
					datePDSAsMap = new HashMap<>();
					datePDSAsMap.put(startMonth, pdsaList);
					indicatorIdDatePDSAsMap.put(pdsa.getIndicator().getIndicatorId(), datePDSAsMap);
				}
			}
			
			//the graphs should be annotated for the PDSAs happened with respect to its start date
			List<DashboardChartModel> dashboardChartModels = new ArrayList<>();
			List<List<DashboardChartModel>> allDashboardChartModels = new ArrayList<>();
			
			String indicatoName = "";
			Integer indicatorId= null;
			for (AggregateData aggregateData : aggregateDatas) {
	
				//if the area doesn't have lr but it is a lr related intermediate indicator, then skip it
				if(!hasLr && null!=aggregateData.getIndicator().getIsLr() && aggregateData.getIndicator().getIsLr())
					continue;
				
				indicatoName = aggregateData.getIndicator().getIndicatorName();
				indicatorId = aggregateData.getIndicator().getIndicatorId();
				Map<String, List<PDSA>> aggregateDataDatePDSAMap = indicatorIdDatePDSAsMap.get(indicatorId);
				
				ControlChartModel controlChartModel = ControlChartDataProvider.getControlChartData(
						aggregateData.getNumeratorValue(), aggregateData.getDenominatorValue(),
						indicatorIdValueNameMap.get(indicatorId));
	
				dashboardChartModels = getDashboardChartModels(controlChartModel,
						aggregateData.getTimePeriod().getTimePeriod(), indicatorIdValueNameMap.get(indicatorId),
						dashboardChartModels, indicatoName, "",
						null != aggregateDataDatePDSAMap
								? aggregateDataDatePDSAMap.get(aggregateData.getTimePeriod().getTimePeriod()) : null,
						aggregateData.getTimePeriod().getTimePeriodId(), aggregateData.getIndicator().getIndicatorOrder());
				
//				 map that holds time wise indicators timeIDIndListMap
				//remove those inds which are already present
				timePeriodIndListMap.get(aggregateData.getTimePeriod().getTimePeriodId().toString()+"_"
						+ aggregateData.getTimePeriod().getTimePeriod()).remove(indicatorId.toString() + "_"+indicatoName+"_"+aggregateData.getIndicator().getIndicatorOrder() );
			}
			
			//key - time name, value - indicator id _ name
			for (Map.Entry<String, List<String>> entry : timePeriodIndListMap.entrySet()) {

				if(null!=entry.getValue() && !entry.getValue().isEmpty()){
					for (String indIdName : entry.getValue()) {

						Map<String, List<PDSA>> aggregateDataDatePDSAMap = indicatorIdDatePDSAsMap
								.get(indIdName.split("_")[0]);

						ControlChartModel controlChartModel = ControlChartDataProvider.getControlChartData(null, null,
								indicatorIdValueNameMap.get(Integer.parseInt(indIdName.split("_")[0])));

						dashboardChartModels = getDashboardChartModels(controlChartModel, entry.getKey().split("_")[1],
								indicatorIdValueNameMap.get(Integer.parseInt(indIdName.split("_")[0])),
								dashboardChartModels, indIdName.split("_")[1], "",
								null != aggregateDataDatePDSAMap ? aggregateDataDatePDSAMap.get(entry.getKey()) : null,
								Integer.parseInt(entry.getKey().split("_")[0]),
								Integer.parseInt(indIdName.split("_")[2]));
					}
				}
			

			}
			Collections.sort(dashboardChartModels, (t1,t2) -> t1.getTimeNid().compareTo(t2.getTimeNid()));
			Collections.sort(dashboardChartModels, (t1,t2) -> t1.getIndicatorOrder().compareTo(t2.getIndicatorOrder()));
			
			allDashboardChartModels = getGroupedDashboardList(dashboardChartModels, allDashboardChartModels);
			
			return allDashboardChartModels;
		} catch (Exception e) {
			LOGGER.error(e);
			return null;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#getDashboardIndicatorViewCharts
	 * (java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 * @author Sarita Panigrahi
	 * Get small multiple dashboard indicator view charts for a selected indicator
	 */
	@Override
	public Map<String, List<List<DashboardChartModel>> > getDashboardIndicatorViewCharts(Integer indicatorId, Integer stateId, Integer districtId,
			Integer facilityTypeId, Integer facilitySizeId, Integer waveParam, String periodicity, String indType){

		try {
			
			List<Integer> facilityIds;
			List<Integer> wave = null;
			
			if(waveParam==null)
				wave = Arrays.asList(1,2,3); //if there is no wave is selected, then we are considering all wave
			else
				wave = Arrays.asList(waveParam);
			
			//get the child area ids
			if(stateId == null && districtId == null && facilitySizeId != null){
				facilityIds = areaRepository.fetchAllDataByFacilitySize(facilitySizeId, wave, Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
			}else if(stateId == null && districtId == null && facilityTypeId != null){
				facilityIds = areaRepository.fetchAllDataByFacilityType(facilityTypeId, wave, Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
			}else if(stateId != null && facilitySizeId != null && districtId == null ){
				facilityIds = areaRepository.fetchDataByStateAndFacilitySize(stateId,facilitySizeId, wave, Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
			}else if(stateId != null && facilityTypeId != null && districtId == null ){
				facilityIds = areaRepository.fetchDataByStateAndFacilityType(stateId,facilityTypeId, wave, Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
			}else if (facilitySizeId != null && districtId != null) {
				facilityIds = areaRepository.fetchDataByFacilitySize(facilitySizeId, districtId, wave);
			} else if (facilityTypeId != null && districtId != null) {
				facilityIds = areaRepository.fetchDataByFacilityType(facilityTypeId, districtId, wave);
			} else if (stateId != null && districtId == null) {
				facilityIds = areaRepository.fetchDataByState(stateId, wave, Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
			}else if (districtId != null) {
				facilityIds = areaRepository.fetchDataByDistrict(districtId, wave, Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
			} else {
				facilityIds = areaRepository.fetchAllData(wave, Integer.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null)));
			}
			
			int facilityTypeSizeId = 0;
			if(facilitySizeId == null && facilityTypeId != null){ //only type selected
				facilityTypeSizeId = facilityTypeId;
			}else if(facilitySizeId != null){
				facilityTypeSizeId = facilitySizeId;
			}else{ //when nothing is selected
				facilityTypeSizeId = Integer.parseInt(
						messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null));
			}
			
			List<Area> facilities;
			facilities = areaRepository.findByAreaIdIn(facilityIds);
			
			if(indicatorId == 23 || indicatorId == 24 || indicatorId == 25){ //intermediate lr dependent inds--filter facilities those have lr
				facilities = filterArea(facilities,hasLr(true));
				facilityIds.clear();
				for(Area area : facilities){
					facilityIds.add(area.getAreaId());
				}
			}
			List<String> facilityIdName = new ArrayList<>();
			for (Area area : facilities) {
				facilityIdName.add(area.getAreaId()+"_"+area.getAreaName());
			}
			
			Integer aggregateAreaId = null;
			if (districtId != null) {
				aggregateAreaId = districtId;
			} else if (stateId != null) {
				aggregateAreaId = stateId;
			}else
				aggregateAreaId = Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)); //country id
			
			facilityIds.add(aggregateAreaId); //include aggregate area also
			List<TimePeriod> timePeriods = null;
			
			if(!periodicity.equals(messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null))){
				timePeriods = timePeriodRepository.findTopNthByPeriodicityOrderByTimePeriodIdDesc(periodicity,new PageRequest(0, 12));
			}else
				timePeriods = timePeriodRepository.findTopByPeriodicityNotInMaxOrderByTimePeriodIdDesc(periodicity,new PageRequest(0, 12));
		
			Map<String, List<String>> timePeriodFacilityListMap = new HashMap<>();
			//get last 12 time period ids
			List<Integer> timePeriodIds = new ArrayList<>();
			List<String> timePeriodIdNames = new ArrayList<>(); //optimize later
			String processIndType = messages.getMessage(Constants.Web.INDICATOR_TYPE_PROCESS_NAME, null, null);
			
			for(TimePeriod timePeriod:timePeriods){
				if(!indType.equalsIgnoreCase(processIndType)){
					timePeriodFacilityListMap.put(timePeriod.getTimePeriodId().toString()+"_"+timePeriod.getTimePeriod(),
							new ArrayList<>(facilityIdName) ); //create a new object of list everytime
					timePeriodIdNames.add(timePeriod.getTimePeriodId()+"_"+timePeriod.getTimePeriod());
				}
				
				timePeriodIds.add(timePeriod.getTimePeriodId());
			}
			
			List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings = new ArrayList<>();
			
			if(indType.equalsIgnoreCase(processIndType)){
				indicatorFacilityTimeperiodMappings = indicatorFacilityTimeperiodMappingRepository.
						findByIndicatorIndicatorIdAndTimePeriodTimePeriodIdInAndFacilityAreaIdIn(indicatorId, timePeriodIds, facilityIds);
			}
			
			for (IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping : indicatorFacilityTimeperiodMappings) {

				Integer timeId = indicatorFacilityTimeperiodMapping.getTimePeriod().getTimePeriodId();
				String timePeriodStr = indicatorFacilityTimeperiodMapping.getTimePeriod().getTimePeriod();

				if (indType.equalsIgnoreCase(processIndType) && !timePeriodIdNames.contains(timeId + "_" + timePeriodStr))
					timePeriodIdNames.add(timeId + "_" + timePeriodStr);

				if (indType.equalsIgnoreCase(processIndType)) {
					if (timePeriodFacilityListMap.containsKey(timeId + "_" + timePeriodStr)) {

						// if the mapping is already present then skip
						if (!timePeriodFacilityListMap.get(timeId + "_" + timePeriodStr)
								.contains(indicatorFacilityTimeperiodMapping.getFacility().getAreaId() + "_"
										+ indicatorFacilityTimeperiodMapping.getFacility().getAreaName()))
							timePeriodFacilityListMap.get(timeId + "_" + timePeriodStr)
									.add(indicatorFacilityTimeperiodMapping.getFacility().getAreaId() + "_"
											+ indicatorFacilityTimeperiodMapping.getFacility().getAreaName());
					} else {
						List<String> facilityList = new ArrayList<>();
						facilityList.add(indicatorFacilityTimeperiodMapping.getFacility().getAreaId() + "_"
								+ indicatorFacilityTimeperiodMapping.getFacility().getAreaName());
						timePeriodFacilityListMap.put(timeId + "_" + timePeriodStr, facilityList);
					}
				}

			}
			
			Map<Integer, Double> areaIdValueNameMap =  new HashMap<>();
			List<DashboardChartModel> dashboardChartModels = new ArrayList<>();
			List<List<DashboardChartModel>> allDashboardChartModels = new ArrayList<>();
			
			List<Object[]> pAverages = aggregateDataRepository.findAllPAverageByTimePeriodIdAndIndicatorIdAndAreaId(timePeriodIds,
					facilityIds, indicatorId, aggregateAreaId, facilityTypeSizeId, waveParam == null ? 0 : waveParam, Integer
					.parseInt(messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null)), 0); //for facility data send 72 as aggregate type and 0 as wave
			
			// here we have distinct area id as it is group by area id
			for (Object[] objects : pAverages) {
				areaIdValueNameMap.put((Integer) objects[1], null!=objects[0] ? Double.parseDouble(objects[0].toString()) : null);
			}
					
	//		[0] numerator_value, [1] denominator_value, [2] area_id_fk, [3] time_period, [4] area_name
			List<Object[]> aggregatedDatas = aggregateDataRepository.findByTimePeriodIdAndIndicatorIdAndAreaId(timePeriodIds,
					facilityIds, indicatorId, aggregateAreaId, facilityTypeSizeId, waveParam == null ? 0 : waveParam, Integer
					.parseInt(messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null)), 0);
			
			//we will iterate through aggregatedDatas to calculate the ucl and lcl values
			
			Map<String, List<List<DashboardChartModel>> > keyDashboardChartModelsMap = null;
			String aggregateAreaName = "Project Aggregate";
			String aggregateDataPresent = null;
			
			for (Object[] objects : aggregatedDatas) {
				
				Long numeratorValue = null!=objects[0] ? ((Integer) objects[0]).longValue() : null;
				Long denominatorValue = null!= objects[1] ? ((Integer)  objects[1]).longValue() : null;
				Integer areaId = (Integer) objects[2];
				String timePeriod = objects[3].toString();
				String areaName = objects[4].toString();
				Integer timePeriodId = (Integer) objects[5];
				
				//once aggregateDataPresent is assigned do not reassign
				if(aggregateDataPresent==null)
					aggregateDataPresent = areaId == aggregateAreaId ? "aggreagteArea" : "";
				//dynamic project aggreagte
				/*if(areaId==aggregateAreaId )
					aggregateAreaName = areaName;*/
				
				ControlChartModel controlChartModel = ControlChartDataProvider.getControlChartData(numeratorValue,
						denominatorValue, areaIdValueNameMap.get(areaId));
				
				
				//we will set source as a key for the aggregate area to recognize explicitly in dashboard
				dashboardChartModels = getDashboardChartModels(controlChartModel, timePeriod, 
						areaIdValueNameMap.get(areaId), dashboardChartModels, areaId==aggregateAreaId ? aggregateAreaName : areaName, 
								areaId==aggregateAreaId ? "aggreagteArea" : "", null, timePeriodId, indicatorId);
				
				if(timePeriodFacilityListMap!=null && timePeriodFacilityListMap.containsKey(timePeriodId+"_" + timePeriod))
					timePeriodFacilityListMap.get(timePeriodId+"_"
							+ timePeriod).remove(areaId + "_"+areaName );
				
				if(timePeriodIdNames!=null)
					timePeriodIdNames.remove(timePeriodId+"_"+ timePeriod);
			}
			//if no data available
			/*if(aggregatedDatas.isEmpty()){
				aggregateAreaName = areaRepository.findByAreaId(aggregateAreaId).getAreaName();	
			}*/
			
			if(timePeriodFacilityListMap!=null){
				for (Map.Entry<String, List<String>> entry : timePeriodFacilityListMap.entrySet()) {

					if(null!=entry.getValue() && !entry.getValue().isEmpty()){
						for (String areaIdName : entry.getValue()) {
							
							Integer areaId = Integer.parseInt(areaIdName.split("_")[0]);
						

							ControlChartModel controlChartModel = ControlChartDataProvider.getControlChartData(null, null,
									areaIdValueNameMap.get(areaId));

							dashboardChartModels = getDashboardChartModels(controlChartModel,
									entry.getKey().split("_")[1], areaIdValueNameMap.get(areaId), dashboardChartModels,
									areaId == aggregateAreaId ? aggregateAreaName : areaIdName.split("_")[1],
									aggregateDataPresent, null,
									Integer.parseInt(entry.getKey().split("_")[0]), indicatorId);
						}
					}
				}
			}
			
			if (aggregateDataPresent.equals("")) {
				for (Map.Entry<String, List<String>> entry : timePeriodFacilityListMap.entrySet()) {

					ControlChartModel controlChartModel = ControlChartDataProvider.getControlChartData(null, null,
							areaIdValueNameMap.get(aggregateAreaId));

					dashboardChartModels = getDashboardChartModels(controlChartModel, entry.getKey().split("_")[1],
							areaIdValueNameMap.get(aggregateAreaId),
							dashboardChartModels, aggregateAreaName, "aggreagteArea", null,
							Integer.parseInt(entry.getKey().split("_")[0]), indicatorId);
				}
			}
			
			if(timePeriodIdNames!=null ){
				for (String timeIdName : timePeriodIdNames){
					
					ControlChartModel controlChartModel = ControlChartDataProvider.getControlChartData(null, null,
							areaIdValueNameMap.get(aggregateAreaId));

					dashboardChartModels = getDashboardChartModels(controlChartModel, timeIdName.split("_")[1],
							areaIdValueNameMap.get(aggregateAreaId), dashboardChartModels, aggregateAreaName,
							"aggreagteArea", null, Integer.parseInt(timeIdName.split("_")[0]), indicatorId);
				}
				
			}
			
			allDashboardChartModels = getGroupedDashboardList(dashboardChartModels, allDashboardChartModels);
			
			if(null!=allDashboardChartModels && !allDashboardChartModels.isEmpty()){
				
				keyDashboardChartModelsMap = new HashMap<>();
				//we have aggregated data in 0th index //if there is no data in aggregated data then it is present in the last index
				keyDashboardChartModelsMap.put("aggreagteArea", aggregatedDatas.isEmpty() || aggregateDataPresent.equals("") ? 
						Arrays.asList(allDashboardChartModels.get(allDashboardChartModels.size()-1)) : Arrays.asList(allDashboardChartModels.get(0)));
				
				
				if(aggregatedDatas.isEmpty() || aggregateDataPresent.equals(""))
					allDashboardChartModels.remove(allDashboardChartModels.size()-1);
				else //remove the 0th element in case data is present
					allDashboardChartModels.remove(0);
				keyDashboardChartModelsMap.put("all", allDashboardChartModels);
			}
			if(null!=keyDashboardChartModelsMap){
				for(Map.Entry<String, List<List<DashboardChartModel>>> entry : keyDashboardChartModelsMap.entrySet() ){
					
					for(List<DashboardChartModel> facilityDashboardChartModels : entry.getValue()){
						Collections.sort(facilityDashboardChartModels, (t1,t2) -> t1.getTimeNid().compareTo(t2.getTimeNid()));
					}
				}
			}
			
			return keyDashboardChartModelsMap;
		
		} catch (Exception e) {
			LOGGER.error("exception in getDashboardIndicatorViewCharts",e);
			return null;
		}
	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param controlChartModel
	 * @param timePerid
	 * @param pAverage
	 * @param dashboardChartModels
	 * @param areaName
	 * @param pdsaList
	 * @return
	 * 
	 * set DashboardChartModel for UCL, LCL , P-Average, Fractional Index/CL
	 */
	private List<DashboardChartModel> getDashboardChartModels(ControlChartModel controlChartModel, String timePerid,
			Double pAverage, List<DashboardChartModel> dashboardChartModels, String areaName, String source,
			List<PDSA> pdsaList, Integer timeNid, Integer indicatorOrder) {
		try {
			//if pdsa is present then set in this pdsa string
			
			String pdsaStr = "";
			if(null!=pdsaList){
				for (PDSA pdsa : pdsaList) {
					
					pdsaStr = pdsaStr+ (!pdsaStr.equals("") ? ", <br/>":"") + pdsa.getName() + " ("+sdf.format(pdsa.getStartDate())+")";
					
				}
			}
			//For fractional index set key as CL
			DashboardChartModel dashboardChartModel = setDashboardChartModel("CL", timePerid,
					areaName, null!=controlChartModel ? controlChartModel.getFractionalIndex() : null, source, pdsaStr, timeNid, indicatorOrder);
	
			dashboardChartModels.add(dashboardChartModel);
			
			//For upper control limit set key as UCL
			dashboardChartModel = setDashboardChartModel("UCL", timePerid, areaName,
					null!=controlChartModel ? controlChartModel.getUcl() : null, source, null, timeNid, indicatorOrder);
			dashboardChartModels.add(dashboardChartModel);
	
			//For Lower control limit set key as LCL
			dashboardChartModel = setDashboardChartModel("LCL",  timePerid,
					areaName, null!=controlChartModel.getLcl() ? controlChartModel.getLcl() 
							< 0 ? 0.0 : controlChartModel.getLcl() : null, source, null, timeNid, indicatorOrder); //Restrict negative LCL value
			dashboardChartModels.add(dashboardChartModel);
			
			//For P-Average
			if(null!=pAverage){ //exception for PDSA summary chart
				dashboardChartModel = setDashboardChartModel("P-Average",  timePerid,
						areaName, pAverage!=null ? Double.parseDouble(df.format(pAverage)) : null, source, null, timeNid, indicatorOrder); //Restrict negative LCL value
				
				dashboardChartModels.add(dashboardChartModel);
			}
			return dashboardChartModels;
		} catch (Exception e) {
			LOGGER.error("exception while setting DashboardChartModels",e);
			return null;
		}
	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param dashboardChartModels
	 * @param allDashboardChartModels
	 * @return
	 * Set grouped chart by name (indicator/facility)
	 */
	private List<List<DashboardChartModel>> getGroupedDashboardList(List<DashboardChartModel> dashboardChartModels,
			List<List<DashboardChartModel>> allDashboardChartModels) {
		try {
			Map<String, List<DashboardChartModel>> occurance = new LinkedHashMap<>();
			for (DashboardChartModel dashboardChartModel : dashboardChartModels) {
				if (occurance.containsKey(dashboardChartModel.getName())) {
					occurance.get(dashboardChartModel.getName()).add(dashboardChartModel);
	
				} else {
					List<DashboardChartModel> list = new ArrayList<>();
					list.add(dashboardChartModel);
					occurance.put(dashboardChartModel.getName(), list);
				}
			}
	
			// group column chart according to indicator
			for(Map.Entry<String, List<DashboardChartModel>> entry : occurance.entrySet() ){
				List<DashboardChartModel> chartData = new ArrayList<>();
				for (DashboardChartModel chartModel : entry.getValue()) {
					DashboardChartModel controlChart = setDashboardChartModel(chartModel.getKey(), chartModel.getDate(),
							chartModel.getName(), chartModel.getValue(), chartModel.getSource(), chartModel.getPdsas(),
							chartModel.getTimeNid(), chartModel.getIndicatorOrder());
					chartData.add(controlChart);
				}
				allDashboardChartModels.add(chartData);
			}
	
			return allDashboardChartModels;
		} catch (Exception e) {
			LOGGER.error("exception while setting group",e);
			return null;
		}
	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param indicatorId
	 * @return
	 * get the list of related indicator for a particular indicator
	 */
	@Override
	public Map<String, List<IndicatorModel>> getRelatedIndicators(Integer indicatorId){
		
		//we DO NOT have profile indicators as related indicator
		List<Indicator> indicators = indicatorRepository.findByIsProfileIsNullOrderByIndicatorTypeTypeDetailIdAscIndicatorOrderAsc();
		
		Map<String, Indicator> indicatorIdDomainMap = new HashMap<>();
		for (Indicator indicator : indicators) {
			indicatorIdDomainMap.put(indicator.getIndicatorId().toString(), indicator);
		}
		Indicator selectedIndicator = indicatorIdDomainMap.get(indicatorId.toString());
		
		String indicatorIds =selectedIndicator.getRelatedIndicatorIds();
		return setIndicatorTypeModelMap(indicatorIds, indicatorIdDomainMap);
		
	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 10-Oct-2017
	 * @param indicator
	 * @return
	 */
	private IndicatorModel setIndicatorModel(Indicator indicator){
		
		IndicatorModel indicatorModel = new IndicatorModel();
		indicatorModel.setIndicatorId(indicator.getIndicatorId());
		indicatorModel.setIndicatorName(indicator.getIndicatorName());
		
		return indicatorModel;
	}
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#getAllIndicator()
	 * get list of indicators
	 */
	@Override
	public Map<String, Map<String, List<ValueModel>>> getAllIndicator() {
		Map<String, Map<String, List<ValueModel>>> indTypeCoreAreaMap = new LinkedHashMap<>();
		Map<String, List<ValueModel>> coreAreaIndicatorMap;
		// getting all the process indicator
		List<Indicator> indicators = indicatorRepository.findByIsProfileIsNullOrderByIndicatorTypeTypeDetailIdAscIndicatorOrderAsc();
		List<ValueModel> listOfValueModel = null;
		for (Indicator indicator : indicators) {

			String coreArea= indicator.getCoreArea()!=null ? indicator.getCoreArea().getTypeDetail() : "no";
			
			if (indTypeCoreAreaMap.containsKey(indicator.getIndicatorType().getTypeDetail())) {
				coreAreaIndicatorMap = indTypeCoreAreaMap.get(indicator.getIndicatorType().getTypeDetail());

				// outcome indicator has no core area
					if (!coreAreaIndicatorMap.containsKey(coreArea)) {
						listOfValueModel = new ArrayList<>();
						ValueModel valueModel = new ValueModel();
						valueModel.setKey(indicator.getIndicatorId());
						valueModel.setValue(indicator.getIndicatorName());
						listOfValueModel.add(valueModel);
						coreAreaIndicatorMap.put(coreArea, listOfValueModel);

					} else {

						coreAreaIndicatorMap.get(coreArea)
								.add(new ValueModel(indicator.getIndicatorId(), indicator.getIndicatorName()));
					}
					indTypeCoreAreaMap.put(indicator.getIndicatorType().getTypeDetail(), coreAreaIndicatorMap);
			} else {
				coreAreaIndicatorMap = new LinkedHashMap<>();
				
				listOfValueModel = new ArrayList<>();
				ValueModel valueModel = new ValueModel();
				valueModel.setKey(indicator.getIndicatorId());
				valueModel.setValue(indicator.getIndicatorName());
				listOfValueModel.add(valueModel);
				coreAreaIndicatorMap.put(coreArea, listOfValueModel);
				
				indTypeCoreAreaMap.put(indicator.getIndicatorType().getTypeDetail(), coreAreaIndicatorMap);
			}
		}

		return indTypeCoreAreaMap;

	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 10-Oct-2017
	 * @param indicatorIds
	 * @param indicatorIdDomainMap
	 * @return
	 */
	private Map<String, List<IndicatorModel>> setIndicatorTypeModelMap(String indicatorIds,
			Map<String, Indicator> indicatorIdDomainMap) {

		Map<String, List<IndicatorModel>> indicatorTypeModelMap = new HashMap<>();
		
//		split by (,) and iterate through all the indicators and put in a map --- key as inidcator type, value indicator model
		for (String indicatorId : indicatorIds.split(",")) {

			Indicator indicator = indicatorIdDomainMap.get(indicatorId);
			
			if (!indicatorTypeModelMap.containsKey((indicator.getIndicatorType().getTypeDetail()))) {

				List<IndicatorModel> indicatorModels = new ArrayList<>();
				indicatorModels.add(setIndicatorModel(indicator));
				indicatorTypeModelMap.put(indicator.getIndicatorType().getTypeDetail(), indicatorModels);
			} else {
				indicatorTypeModelMap.get(indicator.getIndicatorType().getTypeDetail())
						.add(setIndicatorModel(indicator));
			}

		}

		return indicatorTypeModelMap;
	}
	

	
	//Download Excels
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#getDashboardHomeExcel(org.sdrc.scsl.model.web.DashboardLandingModel)
	 * @author Sarita Panigrahi
	 * Excel for landing page (includes data values in tabular format and inline chart images) in XLSX
	 */
	@Override
	public String getDashboardHomeExcel(DashboardLandingModel dashboardLandingModel) {
		
		try {
				
			String filePath = applicationMessageSource.getMessage(
					Constants.Web.FILE_PATH_PDSA, null, null);
			
			File filePathDirect = new File(filePath);
			if (!filePathDirect.exists())
				filePathDirect.mkdir();
			XSSFWorkbook workbook = new XSSFWorkbook();
	
			String fileName = filePath+"/Dashboard home "+sdf.format(new Date())+".xlsx";
			
			// cell styles
	
			Font fileHeadFont = workbook.createFont();
			fileHeadFont.setFontName("Calibri");
			fileHeadFont.setBold(true);
			fileHeadFont.setFontHeightInPoints((short) 18);
	
			// header
			XSSFCellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
			headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			headerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(95, 178, 85)));
			headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			headerStyle.setFont(fileHeadFont);
			headerStyle.setWrapText(true);
	
			XSSFSheet sheet = workbook.createSheet("home");
			Row row0 = sheet.createRow(0);
			Cell cell = row0.createCell(0);
			cell.setCellValue("SCSL Landing Page");
			row0.setHeightInPoints(55); // set row height
			cell.setCellStyle(headerStyle);
	
			Font tableHeadFont = workbook.createFont();
			tableHeadFont.setFontName("Calibri");
			tableHeadFont.setBold(true);
			tableHeadFont.setFontHeightInPoints((short) 12);
	
			XSSFCellStyle tableHeaderStyle = setXSSFCellStyle(workbook, CellStyle.ALIGN_CENTER, new XSSFColor(new java.awt.Color(204, 204, 204)), tableHeadFont, true);
	
			// value cell style
			XSSFCellStyle valueCellStyle = setXSSFCellStyle(workbook, CellStyle.ALIGN_CENTER, null, null, true);
	
			// no center align
			XSSFCellStyle nameCellStyle = setXSSFCellStyle(workbook, (short)0, null, null, true);
	
			int upperCellNum = 0;
	
			// top line grid data
			for (BoxChart boxChart : dashboardLandingModel.getBoxCharts()) {
				int gridRowStartNum = 1;
				Row gridRow = sheet.getRow(gridRowStartNum);
				if (null == gridRow)
					gridRow = sheet.createRow(gridRowStartNum);
				Cell headerCell = gridRow.createCell(upperCellNum);
				headerCell
						.setCellValue(boxChart.getName() + ""
								+ (boxChart.getSource().equals("")
										? "\n Telangana Source: " + boxChart.getTelanganaSource()
												+ "\n Andhra Pradesh Source: " + boxChart.getAndhraSource()
										: "\n Source: "+ boxChart.getSource()));
				headerCell.setCellStyle(tableHeaderStyle);
				Cell blankCell = gridRow.createCell(upperCellNum + 1);
				blankCell.setCellStyle(tableHeaderStyle);
	
				gridRow.setHeightInPoints(110); // set row height
	
				// merge header cell
				sheet.addMergedRegion(
						new CellRangeAddress(gridRowStartNum, gridRowStartNum, upperCellNum, upperCellNum + 1));
				sheet.autoSizeColumn(upperCellNum, true);
	
				// 3rd row for telangana
				gridRowStartNum++;
				Row gridRowTelangana = sheet.getRow(gridRowStartNum);
				if (null == gridRowTelangana)
					gridRowTelangana = sheet.createRow(gridRowStartNum);
				Cell nameCellTelangana = gridRowTelangana.createCell(upperCellNum);
				nameCellTelangana.setCellValue("Telangana");
				nameCellTelangana.setCellStyle(nameCellStyle);
				sheet.autoSizeColumn(upperCellNum);
				upperCellNum++;
				Cell valueCellTelangana = gridRowTelangana.createCell(upperCellNum);
				//append % for coverage indicator where unit is percent
				valueCellTelangana.setCellValue(null!= boxChart.getTelanganaValue() ? boxChart.getUnit().equalsIgnoreCase("number") ? 
						boxChart.getTelanganaValue().toString() :boxChart.getTelanganaValue().toString()+" %" : "N/A");
				valueCellTelangana.setCellStyle(valueCellStyle);
				sheet.autoSizeColumn(upperCellNum);
				upperCellNum--;
	
				// 4th row for andhra
				gridRowStartNum++;
				Row gridRowAndhra = sheet.getRow(gridRowStartNum);
				if (null == gridRowAndhra)
					gridRowAndhra = sheet.createRow(gridRowStartNum);
				Cell nameCellAndhra = gridRowAndhra.createCell(upperCellNum);
				nameCellAndhra.setCellValue("Andhra Pradesh");
				nameCellAndhra.setCellStyle(nameCellStyle);
				sheet.autoSizeColumn(upperCellNum);
				upperCellNum++;
				Cell valueCellAndhra = gridRowAndhra.createCell(upperCellNum);
				//append % for coverage indicator where unit is percent
				valueCellAndhra.setCellValue(null!= boxChart.getAndhraValue() ? boxChart.getUnit().equalsIgnoreCase("number") ? 
						boxChart.getAndhraValue().toString() : boxChart.getAndhraValue().toString() +" %": "N/A");
				valueCellAndhra.setCellStyle(valueCellStyle);
				sheet.autoSizeColumn(upperCellNum);
				upperCellNum++;
	
			}
	
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, upperCellNum - 1)); // merge
																					// the
																					// top
																					// row
																					// till
																					// box
																					// chart
																					// last
																					// cell
	
			// svg chart data
			List<String> imagePathsColumn = new ArrayList<>();
	
			try {
				Map<String, List<String>> filePathImageMap = makeSvgToImage(dashboardLandingModel.getTrendChartSvgs(), null);
				
				for (Map.Entry<String, List<String>> entry : filePathImageMap.entrySet()) {
					imagePathsColumn.addAll(entry.getValue());
				}
			} catch (Exception e1) {
				LOGGER.error("exeption while putting image",e1);
			}
	
			int rowNum = 6;
			int i=0;
			XSSFRow row = null;
			XSSFCell chartCell = null;
			
			//iterate over chart models
			for (List<DashboardChartModel> dashboardChartModels : dashboardLandingModel.getChartModels()) {
				
				upperCellNum=0;
				row = sheet.createRow(rowNum);
				chartCell = row.createCell(upperCellNum);
				sheet.setColumnWidth(upperCellNum, 4000);
				chartCell.setCellValue(dashboardChartModels.get(0).getName() + "\n Source: " + dashboardChartModels.get(0).getSource());
				chartCell.setCellStyle(tableHeaderStyle);
				
				//merge 1st cell
				sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+3, upperCellNum, upperCellNum));
				row.setHeightInPoints(40);
				
				rowNum++;
				row = sheet.createRow(rowNum);
				if(null==row)
					row = sheet.createRow(rowNum);
				//set only cell style
				chartCell = row.createCell(upperCellNum);
				chartCell.setCellStyle(tableHeaderStyle);
				
				rowNum++;
				row = sheet.createRow(rowNum);
				if(null==row)
					row = sheet.createRow(rowNum);
				//set only cell style
				chartCell = row.createCell(upperCellNum);
				chartCell.setCellStyle(tableHeaderStyle);
				
				rowNum++;
				row = sheet.createRow(rowNum);
				if(null==row)
					row = sheet.createRow(rowNum);
				//set only cell style
				chartCell = row.createCell(upperCellNum);
				chartCell.setCellStyle(tableHeaderStyle);
				row.setHeightInPoints(80);
				
				upperCellNum++;
				for (DashboardChartModel chart : dashboardChartModels) {
					//set value
					rowNum= rowNum-3;
					row = sheet.getRow(rowNum);
					if(null==row)
						row = sheet.createRow(rowNum);
					chartCell = row.createCell(upperCellNum);
					chartCell.setCellValue(chart.getDate()); 
					chartCell.setCellStyle(tableHeaderStyle);
					chartCell = row.createCell(upperCellNum + 1);
					chartCell.setCellStyle(tableHeaderStyle);
	
					// merge header cell
					sheet.addMergedRegion(
							new CellRangeAddress(rowNum, rowNum, upperCellNum, upperCellNum + 1));
					
					//India row
					rowNum++;
					row = sheet.getRow(rowNum);
					if(null==row)
						row = sheet.createRow(rowNum);
					chartCell = row.createCell(upperCellNum);
					chartCell.setCellValue("India");
					chartCell.setCellStyle(nameCellStyle);
					sheet.autoSizeColumn(upperCellNum);
					upperCellNum++;
					chartCell = row.createCell(upperCellNum);
					chartCell.setCellValue(chart.getValue()== null? "N/A" : chart.getValue().toString());
					chartCell.setCellStyle(valueCellStyle);
					upperCellNum--; //Initialize to first column of that timeperiod
					
					//Telangana row
					rowNum++;
					row = sheet.getRow(rowNum);
					if(null==row)
						row = sheet.createRow(rowNum);
					chartCell = row.createCell(upperCellNum);
					chartCell.setCellValue("Telangana");
					chartCell.setCellStyle(nameCellStyle);
					sheet.autoSizeColumn(upperCellNum);
					upperCellNum++;
					chartCell = row.createCell(upperCellNum);
					chartCell.setCellValue(chart.getTelanganaValue() == null? "N/A" : chart.getTelanganaValue().toString());
					chartCell.setCellStyle(valueCellStyle);
					upperCellNum--; //Initialize to first column of that timeperiod
					
					//Andhra
					rowNum++;
					row = sheet.getRow(rowNum);
					if(null==row)
						row = sheet.createRow(rowNum);
					chartCell = row.createCell(upperCellNum);
					chartCell.setCellValue("Andhra Pradesh");
					chartCell.setCellStyle(nameCellStyle);
					sheet.autoSizeColumn(upperCellNum);
					upperCellNum++;
					chartCell = row.createCell(upperCellNum);
					chartCell.setCellValue(chart.getAndhraValue() == null? "N/A" : chart.getAndhraValue().toString());
					chartCell.setCellStyle(valueCellStyle);
					upperCellNum++;
				}
				rowNum= rowNum+23;
				//insert image after the value table
				insertimage(rowNum, imagePathsColumn.get(i), workbook, sheet,"home");
				new File(imagePathsColumn.get(i)).delete();
				i++;
			}
	
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
				workbook.write(fileOutputStream);
				workbook.close();
				fileOutputStream.close();
			} catch (IOException e) {
				LOGGER.error("exeption while putting image",e);
			}
	
			return fileName;
		} catch (Exception e) {
			LOGGER.error("exeption while putting image",e);
			return null;
		}
	}

	// SVG to Image
	/** 
	 * @author Sarita Panigrahi, created on: 23-Oct-2017
	 * @param chartSvgs
	 * @param areaName
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<String>> makeSvgToImage(List<String> chartSvgs, String areaName) throws Exception {
		ImageEncoder encoder = new ImageEncoder();
		
		try{
			String filePath = applicationMessageSource.getMessage(
					Constants.Web.FILE_PATH_PDSA, null, null)+"//scsl chart";
			
			File filePathDirect = new File(filePath);
			if (!filePathDirect.exists())
				filePathDirect.mkdirs();
			
			String rbpath = filePath+"/chart1_" + timeFormatter.format(new Date()) + ".svg";
			Map<String, List<String>> pieColumnIndexWiseList = new HashMap<>();
			for (String svg : chartSvgs) {
				File file = new File(rbpath);
				FileOutputStream fop = new FileOutputStream(file);
				byte[] contentbytes = svg.getBytes();
				fop.write(contentbytes);
	
				if (svg.contains("trendsvg")) { // trend chart
					if (pieColumnIndexWiseList.containsKey("trendsvg")) {
						pieColumnIndexWiseList.get("trendsvg").add(encoder.createImgFromFile(rbpath, areaName));
					} else {
						List<String> imgPath = new ArrayList<>();
						imgPath.add(encoder.createImgFromFile(rbpath, areaName));
						pieColumnIndexWiseList.put("trendsvg", imgPath);
					}
				}
				if(file.exists())
					file.delete();
				fop.flush();
				fop.close();
			}
			return pieColumnIndexWiseList;
		} catch (Exception e) {
			LOGGER.error("exeption while making svg to image",e);
			return null;
		}
	}

	// insert image in excel file
	/** 
	 * @author Sarita Panigrahi, created on: 03-Oct-2017
	 * @param rowNum
	 * @param imagePath
	 * @param xssfWorkbook
	 * @param sheet
	 */
	private void insertimage(int rowNum, String imagePath, XSSFWorkbook xssfWorkbook, XSSFSheet sheet, String view) {

		try {
			InputStream inputStream = new FileInputStream(imagePath);
			byte[] imageBytes = IOUtils.toByteArray(inputStream);
			int pictureIdx = xssfWorkbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
			inputStream.close();
			CreationHelper helper = xssfWorkbook.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = helper.createClientAnchor();
		
			if(view.equals("pdsaSummary")){
				anchor.setCol1(5);
				anchor.setRow1(rowNum);
				Picture pict = drawing.createPicture(anchor, pictureIdx);
				pict.resize(7,7);
			}	else if(view.equals("smallmultiple")){
				anchor.setCol1(0);
				anchor.setRow1(rowNum - 20);
				Picture pict = drawing.createPicture(anchor, pictureIdx);
				pict.resize(4,20);
			}
			else{
				anchor.setCol1(1);
				anchor.setRow1(rowNum - 21);
				Picture pict = drawing.createPicture(anchor, pictureIdx);
				pict.resize(5,19);
			}
			
		} catch (Exception e) {
			LOGGER.error("exeption while inserting image",e);
		}
	}

	/** 
	 * @author Sarita Panigrahi, created on: 10-Oct-2017
	 * @param workbook
	 * @param horizontalAlign
	 * @param color
	 * @param font
	 * @param wrapped
	 * @return
	 * 
	 * Set different cell styles
	 */
	private XSSFCellStyle setXSSFCellStyle(XSSFWorkbook workbook, short horizontalAlign, XSSFColor color, Font font, boolean wrapped){
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		
		try{
			if(horizontalAlign!=0)
				cellStyle.setAlignment(horizontalAlign);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			
			if(null!=color){
				cellStyle.setFillForegroundColor(color);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			}
			
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			if(font!=null)
				cellStyle.setFont(font);
			
			cellStyle.setWrapText(wrapped);
			
			return cellStyle;
		} catch (Exception e) {
			LOGGER.error("exeption in cell styling",e);
			return cellStyle;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#getPDSASummaryExcelFilePath(org.sdrc.scsl.model.web.PDSASummaryModel)
	 * @author Sarita Panigrahi
	 * Get PDSA summary chart for the selected PDSA
	 */
	@Override
	public String getPDSASummaryExcelFilePath(PDSASummaryModel pdsaSummaryModel ) {
		
		try{
			String filePath = applicationMessageSource.getMessage(
					Constants.Web.FILE_PATH_PDSA, null, null);
			
			File filePathDirect = new File(filePath);
			if (!filePathDirect.exists())
				filePathDirect.mkdir();
			XSSFWorkbook workbook = new XSSFWorkbook();
	
			String facility = pdsaSummaryModel.getFacilityDetailMap().get("Facility");
			String fileName = filePath + "/PDSA Summary" + facility
					+ pdsaSummaryModel.getSelectedPdsa().getPdsaName().substring(0, 10).replaceAll("\\\\", " ").replaceAll("/", " ")
							.replaceAll("$", " ").replaceAll("%", " ").replaceAll("^", " ")
							.replaceAll("`", " ").replaceAll("~", " ").replaceAll("\\*", " ").replaceAll("<", " ")
							.replaceAll(">", " ").replaceAll("\\|", " ")
					 + sdf.format(new Date()) + ".xlsx";
			
			// cell styles
	
			Font fileHeadFont = workbook.createFont();
			fileHeadFont.setFontName("Calibri");
			fileHeadFont.setBold(true);
			fileHeadFont.setFontHeightInPoints((short) 18);
	
			// header
			XSSFCellStyle headerStyle = setXSSFCellStyle(workbook, CellStyle.ALIGN_CENTER, new XSSFColor(new java.awt.Color(95, 178, 85)), fileHeadFont, true);
	
			XSSFSheet sheet = workbook.createSheet("PDSA Summary");
	
			Font tableHeadFont = workbook.createFont();
			tableHeadFont.setFontName("Calibri");
			tableHeadFont.setBold(true);
			tableHeadFont.setFontHeightInPoints((short) 12);
	
			// value cell style
	
			XSSFCellStyle valueCellStyle = setXSSFCellStyle(workbook, CellStyle.ALIGN_CENTER, null, null, true);
	
			// no center align
			XSSFCellStyle nameCellStyle =setXSSFCellStyle(workbook, (short)0, null, tableHeadFont, true);
			
			XSSFCellStyle tableheadCellStyle = setXSSFCellStyle(workbook, CellStyle.ALIGN_CENTER, new XSSFColor(new java.awt.Color(182, 180, 178)), tableHeadFont, true);
			
			//header row
			int rowNum = 0;
			Row row0 = sheet.createRow(rowNum);
			Cell cell = row0.createCell(0);
			cell.setCellValue("Facility : "+facility);
			row0.setHeightInPoints(55); // set row height
			cell.setCellStyle(headerStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
			
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 10000);
			
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "PDSA Number", 1, pdsaSummaryModel.getSelectedPdsa().getPdsaNumber(), nameCellStyle, valueCellStyle);
			
			// next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "PDSA Name", 1, pdsaSummaryModel.getSelectedPdsa().getPdsaName(),
					nameCellStyle, valueCellStyle);
			
			//next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "Focus Area", 1, pdsaSummaryModel.getSelectedPdsa().getCoreAreaName(), nameCellStyle, valueCellStyle);
			
			//next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "Indicator Type", 1, pdsaSummaryModel.getSelectedPdsa().getIndicatorType(), nameCellStyle, valueCellStyle);
			
			//next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "Indicator Name", 1, pdsaSummaryModel.getSelectedPdsa().getIndicatorName(), nameCellStyle, valueCellStyle);
			Row pdsaRow = sheet.getRow(rowNum);
			pdsaRow.setHeightInPoints(150);
			// next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "Change Idea", 1, pdsaSummaryModel.getSelectedPdsa().getChangeIdeaName(), nameCellStyle, valueCellStyle);
			
			// next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "Start Date", 1, pdsaSummaryModel.getSelectedPdsa().getStartDate(), nameCellStyle, valueCellStyle);
			
			// next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "End Date", 1, pdsaSummaryModel.getSelectedPdsa().getEndDate(), nameCellStyle, valueCellStyle);
			
			// next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "Frequency", 1, Integer.toString(pdsaSummaryModel.getSelectedPdsa().getFrequency()), nameCellStyle, valueCellStyle);
			
			// next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "Brief Description", 1, pdsaSummaryModel.getSelectedPdsa().getSummary(), nameCellStyle, valueCellStyle);
			
			// next row
			rowNum++;
			setPDSACell(sheet, rowNum, 0, "Status", 1, pdsaSummaryModel.getSelectedPdsa().getStatusName(), nameCellStyle, valueCellStyle);
			
			//if the PDSA is closed then only
			if(pdsaSummaryModel.getSelectedPdsa().getClosingRemarks()!=null){
				// next row
				rowNum++;
				setPDSACell(sheet, rowNum, 0,"Closing Remarks", 1, pdsaSummaryModel.getSelectedPdsa().getClosingRemarks(), nameCellStyle, valueCellStyle);
			}
			
			//set facilityDetailMap, which consists of all pdsa status count for that facility
			rowNum = rowNum+2;
			row0 = sheet.createRow(rowNum);
			
			setCellValue(0, "Status" , headerStyle, row0);
			
			row0.setHeightInPoints(30); // set row height
			
			setCellValue(1, "Count" , headerStyle, row0);
			
			for (Entry<String, String> entryMap :  pdsaSummaryModel.getFacilityDetailMap().entrySet()){
				//DO NOT PUT THESE INFORMATION IN EXCEL--ONLY PUT STATUS
				if(entryMap.getKey().equals("District") || 
						entryMap.getKey().equals("Facility") || entryMap.getKey().equals("FacilityId") || entryMap.getKey().equals("State"))
					continue;
				rowNum++;
				setPDSACell(sheet, rowNum, 0, entryMap.getKey(), 1, entryMap.getValue(), nameCellStyle, valueCellStyle);
			}
			
			// set PDSAModel, which consists of all pdsa detail for that facility
			rowNum = rowNum + 2;
			row0 = sheet.createRow(rowNum);
			
			setCellValue(0, "Name" , headerStyle, row0);
			row0.setHeightInPoints(30); // set row height
			
			setCellValue(1, "Status" , headerStyle, row0);
			
			//Iterate through all pdsa of that facility
			for(PDSAModel pdsaModel : pdsaSummaryModel.getPdsaList()){
				rowNum++;
				setPDSACell(sheet, rowNum, 0, pdsaModel.getPdsaName(), 1, pdsaModel.getStatusName(), 
						getColorWiseCellStyle(pdsaModel.getCssClass(), workbook, false), getColorWiseCellStyle(pdsaModel.getCssClass(), workbook, true));
			}
			
			
			//Control chart data
			rowNum = rowNum + 2;
			row0 = sheet.createRow(rowNum);
			
			setCellValue(0, "PDSA : "+pdsaSummaryModel.getSelectedPdsa().getPdsaName() , headerStyle, row0);
			
			row0.setHeightInPoints(30); // set row height
			// set only style
			
			setCellValue(1, "" , headerStyle, row0);
			setCellValue(2, "" , headerStyle, row0);
			setCellValue(3, "" , headerStyle, row0);
			
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 3));// till
																				// LCL
																				// Column
					
			//insert image after the value table
			
			// svg chart data
			List<String> imagePathsColumn = new ArrayList<>();
	
			try {
				Map<String, List<String>> filePathImageMap = makeSvgToImage(pdsaSummaryModel.getChartSvgs(), null);
				
				for (Map.Entry<String, List<String>> entry : filePathImageMap.entrySet()) {
					imagePathsColumn.addAll(entry.getValue());
				}
			} catch (Exception e1) {
				LOGGER.error("exeption while putting image",e1);
			}
	
			if(pdsaSummaryModel.getChartData().get("median") != null){
				insertimage(rowNum, imagePathsColumn.get(0), workbook, sheet, "pdsaSummary");
				new File(imagePathsColumn.get(0)).delete();
			}
			
			rowNum++;
			row0 = sheet.createRow(rowNum);
			
			setCellValue(0, "Date" , tableheadCellStyle, row0);
			setCellValue(1, "Fractional Index" , tableheadCellStyle, row0);
			setCellValue(2, "Upper Control Limit (UCL)" , tableheadCellStyle, row0);
			setCellValue(3, "Lower Control Limit (LCL)" , tableheadCellStyle, row0);
			
			row0.setHeightInPoints(70); // set row height
			int chartCellNum = 0 ;
			rowNum++;
			
			if(pdsaSummaryModel.getChartData().get("median") != null){
				//Iterate through Chart data
				for (List<LinkedHashMap<String, Object>> chartData : (List<List<LinkedHashMap<String, Object>>>) pdsaSummaryModel.getChartData()
						.get("charts")) { // charts key consist of chart model
											// DashboardChartModel
					//Only one chart is present
					for(LinkedHashMap<String, Object> dashboardChartModel : chartData){
						
						row0 = sheet.getRow(rowNum);
						if(row0 == null)
							row0 = sheet.createRow(rowNum);
						//put date in 0th column
						setCellValue(0, dashboardChartModel.get("date").toString() , valueCellStyle, row0);
						
						//check for key "CL"/ "LCL" / "UCL"
						if(dashboardChartModel.get("key").toString().equals("CL")){
							chartCellNum = 1;
						}else if(dashboardChartModel.get("key").toString().equals("UCL"))
							chartCellNum = 2;
						else //LCL
							chartCellNum = 3;
						
						setCellValue(chartCellNum,dashboardChartModel.get("value")!=null? df.format(dashboardChartModel.get("value")) : "N/A", valueCellStyle, row0);
						
						if(chartCellNum ==3)//once all data is there increase the row number
							rowNum++;
					}
				}
				//P-Average/ Median
				rowNum++;
				row0 = sheet.getRow(rowNum);
				if(row0 == null)
					row0 = sheet.createRow(rowNum);
				
				setCellValue(0, "P-Average" , nameCellStyle, row0);
				setCellValue(1, pdsaSummaryModel.getChartData().get("median").toString() , valueCellStyle, row0);
				//set only style
				setCellValue(2, "" , valueCellStyle, row0);
				setCellValue(3, "" , valueCellStyle, row0);
				sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 3));// till LCL Column
			}else{//no data available
				row0 = sheet.getRow(rowNum);
				if(row0 == null)
					row0 = sheet.createRow(rowNum);
				//put date in 0th column
				setCellValue(0, "No data available." , valueCellStyle, row0);
				//set style only
				setCellValue(1, "" , valueCellStyle, row0);
				setCellValue(2, "" , valueCellStyle, row0);
				setCellValue(3, "" , valueCellStyle, row0);
				sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 3));// till LCL Column
			}
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
				workbook.write(fileOutputStream);
				workbook.close();
				fileOutputStream.close();
			} catch (IOException e) {
				LOGGER.error("exeption while writing excel to outputstream",e);
			}
			return fileName;
		} catch (Exception e) {
			LOGGER.error("exeption while generating excel",e);
			return null;
		}
	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 10-Oct-2017
	 * @param sheet
	 * @param rowNum
	 * @param cellNum1
	 * @param label
	 * @param cellNum2
	 * @param value
	 * @param labelCellStyle
	 * @param valueCellStyle
	 * Set cell
	 */
	private void setPDSACell(XSSFSheet sheet,int rowNum, int cellNum1, String label, 
			int cellNum2, String value, XSSFCellStyle labelCellStyle, XSSFCellStyle valueCellStyle){
		Row pdsaRow = sheet.getRow(rowNum);
		if(pdsaRow == null)
			pdsaRow = sheet.createRow(rowNum);
		Cell pdsaCell = pdsaRow.createCell(cellNum1);
		pdsaCell.setCellValue(label);
		pdsaCell.setCellStyle(labelCellStyle); //align left
		pdsaCell = pdsaRow.createCell(cellNum2); //same row, next cell
		pdsaCell.setCellValue(value);
		pdsaCell.setCellStyle(valueCellStyle); //align center middle
	}
	
	//Set pdsa status wise color
	/** 
	 * @author Sarita Panigrahi, created on: 10-Oct-2017
	 * @param color
	 * @param workbook
	 * @param key
	 * @return
	 */
	private XSSFCellStyle getColorWiseCellStyle(String color, XSSFWorkbook workbook, boolean key){ //key will decide the vertical alignment
		
		Font tableHeadFont = workbook.createFont();
		tableHeadFont.setFontName("Calibri");
		tableHeadFont.setBold(true);
		tableHeadFont.setFontHeightInPoints((short) 12);
		
		XSSFCellStyle valueCellStyle = workbook.createCellStyle();
		if(key)
			valueCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		else //set bold in pdsa name column
			valueCellStyle.setFont(tableHeadFont);
		valueCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		valueCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		valueCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		valueCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		valueCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		
		XSSFColor xssfColor = null;
		
		switch(color){ //no relation between color and awt rgb
		case "red":{//pending
			xssfColor = new XSSFColor(new java.awt.Color(153, 101, 95));
			break;
		}
		case "yellow":{//on-going
			xssfColor = new XSSFColor(new java.awt.Color(206, 136, 53));
			break;
		}
		case "pitch":{//Adopt
			xssfColor = new XSSFColor(new java.awt.Color(70, 128, 61));
			break;
		}
		case "blue":{//Adapt
			xssfColor = new XSSFColor(new java.awt.Color(95, 178, 85));
			break;
		}
		default:{ //Abandon 
			xssfColor = new XSSFColor(new java.awt.Color(239, 91, 74));
		}
		
		}
		valueCellStyle.setFillForegroundColor(xssfColor);
		valueCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		valueCellStyle.setWrapText(true);
		
		return valueCellStyle;
	}
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#getSmallMultipleExcelFilePath(org.sdrc.scsl.model.web.SmallMultipleExcelModel)
	 * @author Sarita Panigrahi
	 * get small multiple dwonload excel file
	 */
	@Override
	public String getSmallMultipleExcelFilePath(SmallMultipleExcelModel smallMultipleExcelModel) {
		
		try{
			String filePath = applicationMessageSource.getMessage(
					Constants.Web.FILE_PATH_PDSA, null, null);
			
			File filePathDirect = new File(filePath);
			if (!filePathDirect.exists())
				filePathDirect.mkdir();
			XSSFWorkbook workbook = new XSSFWorkbook();
	
			String fileName = filePath+"/small multiple "+sdf.format(new Date())+".xlsx";
			
			// cell styles
	
			Font fileHeadFont = workbook.createFont();
			fileHeadFont.setFontName("Calibri");
			fileHeadFont.setBold(true);
			fileHeadFont.setFontHeightInPoints((short) 18);
	
			// header
			XSSFCellStyle headerStyle = setXSSFCellStyle(workbook, CellStyle.ALIGN_CENTER, new XSSFColor(new java.awt.Color(95, 178, 85)), fileHeadFont, true);
	
			XSSFSheet sheet = workbook.createSheet("Small Multiple");
	
			Font tableHeadFont = workbook.createFont();
			tableHeadFont.setFontName("Calibri");
			tableHeadFont.setBold(true);
			tableHeadFont.setFontHeightInPoints((short) 12);
	
			// value cell style
			XSSFCellStyle valueCellStyle = setXSSFCellStyle(workbook, CellStyle.ALIGN_CENTER, null, null, true);
	
			// no center align
			XSSFCellStyle nameCellStyle =setXSSFCellStyle(workbook, (short)0, null, tableHeadFont, true);
			
			XSSFCellStyle tableheadCellStyle = setXSSFCellStyle(workbook, CellStyle.ALIGN_CENTER, new XSSFColor(new java.awt.Color(182, 180, 178)), tableHeadFont, true);
			
			//header row
			int rowNum = 0;
			int cellNum = 0;
			Row row0 = sheet.createRow(rowNum);
			Cell cell = row0.createCell(cellNum);
			row0.setHeightInPoints(100); // set row height
			
			//for indicator view set other filters 
			if(smallMultipleExcelModel.getIndicatorName() !=null ){
				
				cell.setCellValue("Indicator Type : "+smallMultipleExcelModel.getIndicatorType());
				cell.setCellStyle(headerStyle);
				
				//skip focus area for outcome indicator
				if(!smallMultipleExcelModel.getIndicatorType().equals("Outcome")){
					cellNum++;
					setCellValue(cellNum, "Focus Area : "+smallMultipleExcelModel.getCoreArea() , headerStyle, row0);
				}
				
				cellNum++;
				setCellValue(cellNum, "Indicator Name : "+smallMultipleExcelModel.getIndicatorName(), headerStyle, row0);
				
				//these filters are not mandatory to select
				if(null!=smallMultipleExcelModel.getState()){
					cellNum++;
					setCellValue(cellNum, "State : "+smallMultipleExcelModel.getState(), headerStyle, row0);
				}
				if(null!=smallMultipleExcelModel.getDistrict()){
					cellNum++;
					setCellValue(cellNum, "District : "+smallMultipleExcelModel.getDistrict(), headerStyle, row0);
				}
				if(null!=smallMultipleExcelModel.getFacilityType()){
					cellNum++;
					setCellValue(cellNum, "Facility Type : "+smallMultipleExcelModel.getFacilityType(), headerStyle, row0);
				}
				if(null!=smallMultipleExcelModel.getFacilitySize()){
					cellNum++;
					setCellValue(cellNum, "Facility Size : "+smallMultipleExcelModel.getFacilitySize(), headerStyle, row0);
				}
				if(null!=smallMultipleExcelModel.getPeriodicity()){
					cellNum++;
					setCellValue(cellNum, "Periodicity : "+smallMultipleExcelModel.getPeriodicity(), headerStyle, row0);
				}
				if(null!=smallMultipleExcelModel.getWave()){
					cellNum++;
					setCellValue(cellNum, smallMultipleExcelModel.getWave() , headerStyle, row0);
				}
				
				
			}else{//for facility view set facility properties
				
				cell.setCellValue("State : "+smallMultipleExcelModel.getState());
				cell.setCellStyle(headerStyle);
				
				setCellValue(1, "District : "+smallMultipleExcelModel.getDistrict(), headerStyle, row0);
				
				setCellValue(2, "Facility : "+smallMultipleExcelModel.getFacilityName() , headerStyle, row0);
			}
			
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 10000);
			sheet.setColumnWidth(3, 7000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 5000);
			sheet.setColumnWidth(8, 5000);
			sheet.setColumnWidth(9, 5000);
			// svg chart data
			List<String> imagePathsColumn = new ArrayList<>();
	
			try {
				
				Map<String, List<String>> filePathImageMap = makeSvgToImage(smallMultipleExcelModel.getChartSvgs(), null);
				
				for (Map.Entry<String, List<String>> entry : filePathImageMap.entrySet()) {
					imagePathsColumn.addAll(entry.getValue());
				}
			} catch (Exception e1) {
				LOGGER.error("exeption while putting image",e1);
			}
			
			int i =0;
			for(List<DashboardChartModel> dashboardChartModels : smallMultipleExcelModel.getDashboardChartModels()){
				
				rowNum = rowNum+2;
				row0 = sheet.createRow(rowNum);
				setCellValue(0, dashboardChartModels.get(0).getName() , tableheadCellStyle, row0);
				row0.setHeightInPoints(60); // set row height
				
				//set only styles
				setCellValue(1, "", tableheadCellStyle, row0);
				setCellValue(2, "", tableheadCellStyle, row0);
				setCellValue(3, "", tableheadCellStyle, row0);
				
				sheet.addMergedRegionUnsafe(new CellRangeAddress(rowNum, rowNum, 0, 3));
				
				
				rowNum= rowNum+23;
				//insert image after the value table
				insertimage(rowNum, imagePathsColumn.get(i), workbook, sheet ,"smallmultiple");
				new File(imagePathsColumn.get(i)).delete();
				i++;
				
				rowNum++;
				row0 = sheet.createRow(rowNum);
				
				setCellValue(0, "Date", tableheadCellStyle, row0);
				
				setCellValue(1, "Fractional Index", tableheadCellStyle, row0);
				
				setCellValue(2, "Upper Control Limit (UCL)", tableheadCellStyle, row0);
				
				setCellValue(3, "Lower Control Limit (LCL)", tableheadCellStyle, row0);
				row0.setHeightInPoints(70); // set row height
				
				int chartCellNum = 0 ;
				rowNum++;
				
				Double pAverage = null;
				for (DashboardChartModel dashboardChartModel : dashboardChartModels) {
					
					if(dashboardChartModel.getKey().equals("P-Average"))
						pAverage = dashboardChartModel.getValue();
					else {
						row0 = sheet.getRow(rowNum);
						if(row0 == null)
							row0 = sheet.createRow(rowNum);
						//put date in 0th column
						setCellValue(0, dashboardChartModel.getDate().toString(), valueCellStyle, row0);
						
						//check for key "CL"/ "LCL" / "UCL"
						if(dashboardChartModel.getKey().equals("CL")){
							chartCellNum = 1;
						}else if(dashboardChartModel.getKey().equals("UCL"))
							chartCellNum = 2;
						else if(dashboardChartModel.getKey().equals("LCL"))//LCL
							chartCellNum = 3;
						
						setCellValue(chartCellNum, null!=dashboardChartModel.getValue() ? df.format(dashboardChartModel.getValue()) : "N/A", valueCellStyle, row0);
						
						if(chartCellNum ==3)//once all data is there increase the row number
							rowNum++;
					}
					
				}
				//add p-Average at the end
				row0 = sheet.getRow(rowNum);
				if(row0 == null)
					row0 = sheet.createRow(rowNum);
				
				setCellValue(0, "P-Average", nameCellStyle, row0);
				setCellValue(1, null!=pAverage ? pAverage.toString() : "N/A", valueCellStyle, row0);
				//set only style
				setCellValue(2, "", valueCellStyle, row0);
				setCellValue(3, "", valueCellStyle, row0);
				sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 3));// till LCL Column
			}
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
				workbook.write(fileOutputStream);
				workbook.close();
				fileOutputStream.close();
			} catch (IOException e) {
				LOGGER.error("exeption while writing excel",e);
			}
			return fileName;
		}catch (Exception e2) {
			LOGGER.error("exeption while generating excel",e2);
			return null;
		}
	}
	
	/** 
	 * @author Sarita Panigrahi, created on: 21-Oct-2017
	 * @param cellNum
	 * @param cellValue
	 * @param cellStyle
	 * @param row0
	 * set cell value
	 */
	private void setCellValue(int cellNum, String cellValue, CellStyle cellStyle, Row row0 ){
		Cell cell = row0.createCell(cellNum);
		cell.setCellValue(cellValue);
		cell.setCellStyle(cellStyle);
	}
	
	//download google map view engagement score chart as an image file
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.service.DashboardService#downloadTrendImage(java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	public String downloadTrendImage(List<String> chartSvgs, String areaName){
		
		// svg chart data
		List<String> imagePathsColumn = new ArrayList<>();

		try {
			Map<String, List<String>> filePathImageMap = makeSvgToImage(chartSvgs, areaName);
			
			for (Map.Entry<String, List<String>> entry : filePathImageMap.entrySet()) {
				imagePathsColumn.addAll(entry.getValue());
			}
		} catch (Exception e1) {
			LOGGER.error("exeption while putting image",e1);
		}
		//as there is only one file exists
		return imagePathsColumn.get(0);
	}
}
