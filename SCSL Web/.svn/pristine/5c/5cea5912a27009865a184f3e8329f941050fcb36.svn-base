package org.sdrc.scsl.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.sdrc.scsl.domain.AggregateData;
import org.sdrc.scsl.domain.ArchiveAggregateData;
import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.domain.Indicator;
import org.sdrc.scsl.domain.TXNSNCUData;
import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.domain.TypeDetail;
import org.sdrc.scsl.model.web.ReturnModel;
import org.sdrc.scsl.repository.AggregateDataRepository;
import org.sdrc.scsl.repository.ArchiveAggregateDataRepository;
import org.sdrc.scsl.repository.AreaRepository;
import org.sdrc.scsl.repository.TXNSNCUDataRepository;
import org.sdrc.scsl.repository.TXNSubmissionManagementRepository;
import org.sdrc.scsl.repository.TimePeriodRepository;
import org.sdrc.scsl.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in) Time wise aggregate sncu data
 */
@Service
public class AggregationServiceImpl implements AggregationService {

	@Autowired
	private ResourceBundleMessageSource messages;

	@Autowired
	private TimePeriodRepository timePeriodRepository;

	@Autowired
	private TXNSNCUDataRepository txnsncuDataRepository;

	@Autowired
	private AggregateDataRepository aggregateDataRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private ArchiveAggregateDataRepository archiveAggregateDataRepository;

	@Autowired
	private TXNSubmissionManagementRepository tXNSubmissionManagementRepository;

	private SimpleDateFormat simpleDateformat = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.MONTH_SMALL_YEAR_FORMATTER_NO_HYPHEN));
	private SimpleDateFormat simpleDateformater = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_FORMATTER_HYPHEN));
	private DateFormat formatter = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_FORMATTER_HYPHEN));
	private DecimalFormat df = new DecimalFormat("#.##");

	private static final Logger LOGGER=Logger.getLogger(AggregationServiceImpl.class);

	// rollback if any exception occurs
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sdrc.scsl.service.AggregationService#aggregateSNCUData(java.lang.
	 * String)
	 * 
	 * @author Sarita Panigrahi
	 * 
	 * while persisting aggregate data for country or state or district set
	 * AggregateData aggreagteType as "total" (typedetail id) while persisting
	 * aggregate data for facility type/ size ("public",
	 * "private","Small Public","Small Private","Large Public") set
	 * corresponding typedetail id AggregateData aggreagteType )
	 * For wavely aggregation, we will aggregate the data in various levels like total-wave, facility size- wave, type-wave.
	 * For all wave aggregation, set aggregation wave as "0"
	 * For all other the corresponding wave  
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void aggregateSNCUData(String aggregationFrequency, Integer tpId) {

		List<Integer> timeIds = new ArrayList<>();
		TimePeriod timePeriod = null;

		List<TimePeriod> timePeriods = new ArrayList<>();
		try {
			// if it is a monthly aggregation fetch the respected timenid
			if (aggregationFrequency
					.equals(messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null))) {
				if(tpId==null)
					timePeriod = createTimePeriodByPeriodicity(aggregationFrequency);
				else
					timePeriod = timePeriodRepository.findByTimePeriodId(tpId);
				timeIds.add(timePeriod.getTimePeriodId());
			} else {
				
				if(tpId==null)
					timePeriod = createTimePeriodByPeriodicity(aggregationFrequency);
				else
					timePeriod = timePeriodRepository.findByTimePeriodId(tpId);
				//calculate the timeperiods between this
				// for which sncu data needs to be fetch
				TimePeriod startTimePeriod = timePeriodRepository.findByTimePeriod(timePeriod.getTimePeriod().split("-")[0].trim());
				TimePeriod endTimePeriod = timePeriodRepository.findByTimePeriod(timePeriod.getTimePeriod().split("-")[1].trim());
				
				timePeriods = timePeriodRepository.findByTimePeriodIdBetweenAndPeriodicityOrderByTimePeriodIdAsc
						(startTimePeriod.getTimePeriodId(), endTimePeriod.getTimePeriodId(), messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null));
				
				for (TimePeriod allTimePeriod : timePeriods) {
					timeIds.add(allTimePeriod.getTimePeriodId());
				}
			}

			List<Integer> submissionIds = tXNSubmissionManagementRepository.findTXNSubmissionIdByTypeDetailIdForMnE(
					Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED, null, null)));

			List<Integer> legacySubmissions = tXNSubmissionManagementRepository
					.findTXNSubmissionIdByTypeDetailIdForLegacy(
							Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_LEGACY_STATUS, null, null)));

			if (!legacySubmissions.isEmpty())
				submissionIds.addAll(legacySubmissions);

			String[] areaLevels = { messages.getMessage(Constants.Web.FACILITY_LEVEL, null, null),
					messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null),
					messages.getMessage(Constants.Web.STATE_LEVEL, null, null),
					messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null) };

			// we will call the aggregate queries according to the area level
			for (String areaLevel : areaLevels) {
				Integer aggreagteType = Integer
						.parseInt(messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null)); // total
																										// type
																										// detail
																										// ID
				try {
					List<Object[]> aggregatedData = null;
					List<AggregateData> aggregateDataDomainList = new ArrayList<>();

					if (areaLevel.equals(messages.getMessage(Constants.Web.FACILITY_LEVEL, null, null))) {
						// in this level, if it is a monthly aggregation
						// directly
						// get the raw value from txn data
						// else get aggregated data
						if (aggregationFrequency
								.equals(messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null))) {

							List<TXNSNCUData> txnsncuDatasMonthly = txnsncuDataRepository
									.findByIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdAndIsLiveTrueAndTxnSubmissionManagementStatusMneTypeDetailId(
											timePeriod.getTimePeriodId(), submissionIds);
							// set in aggregate data
							AggregateData aggregateData = null;
							for (TXNSNCUData txnsncuData : txnsncuDatasMonthly) {

								// if both numerator and denominator is null,
								// then skip

								// if(txnsncuData.getNumeratorValue()==null &&
								// txnsncuData.getDenominatorValue()==null)
								// continue;
								aggregateData = setAggregateData(
										txnsncuData.getNumeratorValue() != null
												? txnsncuData.getNumeratorValue().longValue() : null,
										txnsncuData.getDenominatorValue() != null
												? txnsncuData.getDenominatorValue().longValue() : null,
										txnsncuData.getPercentage(),
										txnsncuData.getIndicatorFacilityTimeperiodMapping().getFacility().getAreaId(),
										timePeriod,
										txnsncuData.getIndicatorFacilityTimeperiodMapping().getIndicator()
												.getIndicatorId(),
										aggreagteType, txnsncuData.getIndicatorFacilityTimeperiodMapping()
												.getIndicator().getIsProfile(),
										0,areaLevel);

								if (null != aggregateData)
									aggregateDataDomainList.add(aggregateData);
							}
						} else { // quarterly--yearly
							// get sncu data
							// 0 index-data numerator value,1 index deno value,
							// 2
							// index-per value , 3 index-area id, 4
							// index-indicator
							// id

							aggregatedData = txnsncuDataRepository
									.findFacilityAggregatedDataByIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											timeIds, submissionIds);
							aggregateDataDomainList = setAggregateData(aggregatedData, timePeriod, aggreagteType, 0,areaLevel);

						}

						// district level
					} else if (areaLevel.equals(messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null))) {
						aggregatedData = txnsncuDataRepository
								.findDistrictAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, 0, areaLevel));

						// district wise wavely aggregation

						aggregatedData = txnsncuDataRepository
								.findDistrictWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList
								.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, null, areaLevel));

						// district wise ---- facility type and size aggregation

						aggregatedData = txnsncuDataRepository
								.findDistrictTypeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, areaLevel)); // set
																												// aggreagteType
																												// null
																												// while
																												// aggregating
																												// type
																												// /
																												// size
																												// wise
						aggregatedData = txnsncuDataRepository
								.findDistrictSizeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, areaLevel)); // set
																												// aggreagteType
																												// null
																												// while
																												// aggregating
																												// type
																												// /
																												// size
																												// wise

						// district wise TYPE wise---- wavely aggregation
						// aggregate the facility data wave wise

						aggregatedData = txnsncuDataRepository
								.findDistrictTypeWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, areaLevel)); // set
																													// aggreagteType
																													// null
																													// while
																													// aggregating
																													// type
																													// /
																													// size
																													// wise

						// district wise SIZE wise---- wavely aggregation
						// aggregate the facility data wave wise

						aggregatedData = txnsncuDataRepository
								.findDistrictSizeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, areaLevel)); // set
																													// aggreagteType
																													// null
																													// while
																													// aggregating
																													// type
																													// /
																													// size
																													// wise

						// state level
					} else if (areaLevel.equals(messages.getMessage(Constants.Web.STATE_LEVEL, null, null))) {
						aggregatedData = txnsncuDataRepository
								.findStateAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, 0, areaLevel));

						// state wise----Wave wise
						aggregatedData = txnsncuDataRepository
								.findStateWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList
								.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, null, areaLevel));

						// state wise ---- facility type and size aggregation
						aggregatedData = txnsncuDataRepository
								.findStateTypeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, areaLevel));

						// state wise ---- facility type and size aggregation
						aggregatedData = txnsncuDataRepository
								.findStateSizeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, areaLevel));

						// state wise ---- facility type
						// wise---wavely------aggregation
						aggregatedData = txnsncuDataRepository
								.findStateTypeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, areaLevel));

						// state wise ---- facility size wise----wavely--
						// aggregation
						aggregatedData = txnsncuDataRepository
								.findStateSizeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
										timeIds, submissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, areaLevel));

					} else {// COUNTRY_LEVEL
						// get sncu data
						// 0 index-data numerator value,1 index deno value, 2
						// index-per value , 3 index-indicator id
						AggregateData aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountryAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										submissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = null != objects[0] ? (Long) objects[0] : null;
							Long denominatorDataVal = null != objects[1] ? (Long) objects[1] : null;
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							Boolean isProfile = (Boolean) objects[4];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, 0, areaLevel); // india
																							// id
																							// =1
							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}

						// COUNTRY WISE WAVELY AGGREGATION
						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountryWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										submissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = null != objects[0] ? (Long) objects[0] : null;
							Long denominatorDataVal = null != objects[1] ? (Long) objects[1] : null;
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							Boolean isProfile = (Boolean) objects[4];
							Integer wave = (Integer) objects[5];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, wave, areaLevel); // India
																								// id
																								// =1
							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}

						// Aggregate by facility size and facility type--
						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountryTypeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										submissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = (Long) objects[0];
							Long denominatorDataVal = (Long) objects[1];
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							aggreagteType = (Integer) objects[4];
							Boolean isProfile = (Boolean) objects[5];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, 0, areaLevel); // india
																							// id
																							// =1
							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}

						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountrySizeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										submissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = (Long) objects[0];
							Long denominatorDataVal = (Long) objects[1];
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							aggreagteType = (Integer) objects[4];
							Boolean isProfile = (Boolean) objects[5];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, 0, areaLevel); // india
																							// id
																							// =1

							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}
						
						// Aggregate by facility type wise--wavely--
						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountryTypeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										submissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = (Long) objects[0];
							Long denominatorDataVal = (Long) objects[1];
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							aggreagteType = (Integer) objects[4];
							Boolean isProfile = (Boolean) objects[5];
							Integer wave = (Integer) objects[6];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, wave, areaLevel); // india
																							// id
																							// =1
							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}
						
						//country size---wavely
						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountrySizeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										submissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = (Long) objects[0];
							Long denominatorDataVal = (Long) objects[1];
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							aggreagteType = (Integer) objects[4];
							Boolean isProfile = (Boolean) objects[5];
							Integer wave = (Integer) objects[6];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, wave, areaLevel); // india
																							// id
																							// =1

							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}

					}

					aggregateDataRepository.save(aggregateDataDomainList);
				} catch (Throwable e) {
					LOGGER.info("area level : "+areaLevel+"aggregationFrequency : "+aggregationFrequency);
					LOGGER.error("message",e);
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}

			}

		} catch (Throwable e) {
			LOGGER.info("ERROR OCCURED IN AGGREGATION");
			LOGGER.error("message",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}

	}

	// set list of AggregateData
	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param aggregatedData
	 * @param timePeriod
	 * @param aggreagteType
	 * @param wave
	 * @param areaLevel
	 * @return
	 */
	private List<AggregateData> setAggregateData(List<Object[]> aggregatedData, TimePeriod timePeriod,
			Integer aggreagteType, Integer wave, String areaLevel) {
		List<AggregateData> aggregateDatas = new ArrayList<>();
		AggregateData aggregateData = null;
		for (Object[] objects : aggregatedData) {
			Long numeratorDataVal = (Long) objects[0];
			Long denominatorDataVal = (Long) objects[1];
			Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
			Integer facilityId = (Integer) objects[3];
			Integer indicatorId = (Integer) objects[4];
			Boolean isProfile = null;
			Integer aggtype = null;
			Integer waveType = null;
			if (aggreagteType == null) { // facility type/size wise aggregation
											// -- else total type
				aggtype = (Integer) objects[5];
				isProfile = (Boolean) objects[6];
				// set wave
				// wave wise aggregate if wave is present --
				// else wave 0
				if(wave == null)
					waveType = (Integer) objects[7];
				else
					waveType = wave;

			} else {
				aggtype = aggreagteType;
				isProfile = (Boolean) objects[5];
				// set wave
				// wave wise aggregate if wave is present --
				// else wave 0
				if(wave == null)
					waveType = (Integer) objects[6];
				else
					waveType = wave;

			}

			aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal, facilityId,
					timePeriod, indicatorId, aggtype, isProfile, waveType, areaLevel);

			if (null != aggregateData)
				aggregateDatas.add(aggregateData);
		}

		return aggregateDatas;
	}

	// set one by one AggregateData
	
	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param numeratorValue
	 * @param denominatorValue
	 * @param percentageVal
	 * @param facilityId
	 * @param timePeriod
	 * @param indicatorId
	 * @param aggreagteType
	 * @param isProfile
	 * @param waveType
	 * @param areaLevel
	 * @return
	 */
	private AggregateData setAggregateData(Long numeratorValue, Long denominatorValue, Double percentageVal,
			Integer facilityId, TimePeriod timePeriod, Integer indicatorId, Integer aggreagteType, Boolean isProfile,
			Integer waveType, String areaLevel) {

		// if the indicator is not a profile indicator then persist only if both
		// numeratorValue and denominatorValue is present and it is not facility level area
		// if it is a profile indicator then direct persist
		
		if ((null == isProfile && null != numeratorValue && null != denominatorValue) || null != isProfile
				|| areaLevel.equals(messages.getMessage(Constants.Web.FACILITY_LEVEL, null, null))) {
			
			AggregateData aggregateData = new AggregateData();
			aggregateData.setArea(new Area(facilityId));
			aggregateData.setIndicator(new Indicator(indicatorId));
			aggregateData.setNumeratorValue(numeratorValue);

			// for these profile indicators set numerator values same as percent
			// values
			// if(indicatorId == 34 || indicatorId == 35 || indicatorId == 36 ||
			// indicatorId == 38 || indicatorId == 39 || indicatorId == 40 ||
			// indicatorId == 43)
			// aggregateData.setNumeratorValue(percentageVal.longValue());

			aggregateData.setDenominatorValue(denominatorValue);
			aggregateData.setPercentage(null != percentageVal && !percentageVal.isNaN() && !percentageVal.isInfinite()
					? Double.parseDouble(df.format(percentageVal)) : null);
			aggregateData.setTimePeriod(timePeriod);
			aggregateData.setAggreagteType(new TypeDetail(aggreagteType));
			aggregateData.setWave(waveType);
			aggregateData.setCreatedDate(new Timestamp(new Date().getTime()));

			return aggregateData;
		} else
			return null;

	}

	// aggregate after historical data uploaded
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.service.AggregationService#
	 * aggregateAfterHistoricalDataUpload(java.util.List, java.lang.Integer,
	 * java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Transactional
	public ReturnModel aggregateAfterHistoricalDataUpload(List<Integer> timeIds, Integer areaId,
			List<TimePeriod> timePeriods) {

		//get all legacy submission ids
		List<Integer> legacySubmissionIds = tXNSubmissionManagementRepository.findTXNSubmissionIdByTypeDetailIdForLegacy(
				Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_LEGACY_STATUS, null, null)));

		List<Integer> mneSubmissionIds = tXNSubmissionManagementRepository.findTXNSubmissionIdByTypeDetailIdForMnE(
				Integer.parseInt(messages.getMessage(Constants.Web.SUBMISSION_STATUS_APPROVED, null, null)));
		mneSubmissionIds.addAll(legacySubmissionIds); // for facility level quarter
												// and yearly aggregation and
												// district, state, country
												// level aggregation we require
												// all the m&e approved
												// submissions including legacy
												// submissions

		List<Object[]> aggregatedData = null;
		// we will call the aggregate queries according to the arealevel

		Map<Integer, Area> areaIdNameMap = new HashMap<>();
		List<Area> areas = areaRepository.findAll();

		for (Area area : areas) {
			areaIdNameMap.put(area.getAreaId(), area);
		}

		List<Area> areaList = new ArrayList<>();
		areaList.add(areaIdNameMap.get(areaId)); // facility
		areaList.add(areaIdNameMap.get(areaIdNameMap.get(areaId).getParentAreaId())); // district
		areaList.add(
				areaIdNameMap.get(areaIdNameMap.get(areaIdNameMap.get(areaId).getParentAreaId()).getParentAreaId())); // state
		areaList.add(areaIdNameMap.get(Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)))); // india

		List<TimePeriod> quarterlyTimePeriods = new ArrayList<>();
		List<TimePeriod> yearlyTimePeriods = new ArrayList<>();

		ReturnModel model = new ReturnModel();

		for (TimePeriod timePeriod : timePeriods) {

			// we will add the quarterly tps and yearly tp here
			TimePeriod quarterlyTimePeriod = timePeriodRepository
					.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndPeriodicity(timePeriod.getEndDate(),
							timePeriod.getEndDate(),
							messages.getMessage(Constants.Web.TIMEPERIOD_QUARTER_PERIODICITY, null, null));
			TimePeriod yearlyTimePeriod = timePeriodRepository
					.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndPeriodicity(timePeriod.getEndDate(),
							timePeriod.getEndDate(),
							messages.getMessage(Constants.Web.TIMEPERIOD_YEAR_PERIODICITY, null, null));

			// as for a monthly tp we can get multiple duplicate quarterly tp
			if (null != quarterlyTimePeriod && !quarterlyTimePeriods.contains(quarterlyTimePeriod))
				quarterlyTimePeriods.add(quarterlyTimePeriod);
			if (null != yearlyTimePeriod && !yearlyTimePeriods.contains(yearlyTimePeriod))
				yearlyTimePeriods.add(yearlyTimePeriod);

			// iterate for all area--> facility/district/state/india
			for (Area area : areaList) {

				Integer aggreagteType = Integer
						.parseInt(messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null)); // total
																										// type
																										// detail
																										// ID

				try {
					// archive all aggregate data and then delete
					moveDataToArchiveAggregate(aggregateDataRepository
							.findByAreaAreaIdAndTimePeriodTimePeriodId(area.getAreaId(), timePeriod.getTimePeriodId()));
				} catch (Exception e) {
					LOGGER.error("message",e);
					model.setErrorMessage("Something went wrong");
				}

				try {

					// we will delete all the aggregated data from table
					// for the given timeperiod and area
					// then we will persist the new record

					aggregateDataRepository.deleteByAreaAreaIdAndTimePeriodTimePeriodId(area.getAreaId(),
							timePeriod.getTimePeriodId());

				} catch (Exception e) {
					LOGGER.error("message",e);
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					model.setErrorMessage("Something went wrong");
				}

				try {
					List<AggregateData> aggregateDataDomainList = new ArrayList<>();
					// set in aggregate data
					AggregateData aggregateData = null;
					aggregatedData = null;

					if (area.getLevel() == Integer
							.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null))) { // facility
																											// level
						// in this level, if it is a monthly aggregation
						// directly
						// get the raw value from txn data for the given
						// facility
						// else get aggregated data

						List<TXNSNCUData> txnsncuDatasMonthly = txnsncuDataRepository
								.findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodId(
										areaId, timePeriod.getTimePeriodId(), legacySubmissionIds);
						for (TXNSNCUData txnsncuData : txnsncuDatasMonthly) {
							aggregateData = setAggregateData(
									txnsncuData.getNumeratorValue() != null
											? txnsncuData.getNumeratorValue().longValue() : null,
									txnsncuData.getDenominatorValue() != null
											? txnsncuData.getDenominatorValue().longValue() : null,
									txnsncuData.getPercentage(),
									txnsncuData.getIndicatorFacilityTimeperiodMapping().getFacility().getAreaId(),
									timePeriod,
									txnsncuData.getIndicatorFacilityTimeperiodMapping().getIndicator().getIndicatorId(),
									aggreagteType,
									txnsncuData.getIndicatorFacilityTimeperiodMapping().getIndicator().getIsProfile(),
									0, messages.getMessage(Constants.Web.FACILITY_LEVEL, null, null));
							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);
						}

						// district level
					} else if (area.getLevel() == Integer
							.parseInt(messages.getMessage(Constants.Web.DISTRICT_LEVEL_ID, null, null))) {
						aggregatedData = txnsncuDataRepository
								.findDistrictAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, 0, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));
						
						//district --wavely
						
						aggregatedData = txnsncuDataRepository
								.findDistrictWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, null, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));

						// aggregate by district wise facility type and size
						aggregatedData = txnsncuDataRepository
								.findDistrictTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));

						aggregatedData = txnsncuDataRepository
								.findDistrictSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));
						
						// aggregate by district wise facility type and size-----WAVELY
						aggregatedData = txnsncuDataRepository
								.findDistrictTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));

						aggregatedData = txnsncuDataRepository
								.findDistrictSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));

						// state level
					} else if (area.getLevel() == Integer
							.parseInt(messages.getMessage(Constants.Web.STATE_LEVEL_ID, null, null))) {
						aggregatedData = txnsncuDataRepository
								.findStateAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, 0, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));
						
//						wavely
						aggregatedData = txnsncuDataRepository
								.findStateWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, null, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));

						// aggregate by state wise facility type and size
						aggregatedData = txnsncuDataRepository
								.findStateTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));

						aggregatedData = txnsncuDataRepository
								.findStateSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));
						
						// WAVELY aggregate by state wise facility type and size
						aggregatedData = txnsncuDataRepository
								.findStateTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));

						aggregatedData = txnsncuDataRepository
								.findStateSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
										mneSubmissionIds);
						aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));

					} else {// COUNTRY_LEVEL
						// get sncu data
						// 0 index-data numerator value,1 index deno value, 2
						// index-per value , 3 index-indicator id
						aggregatedData = txnsncuDataRepository
								.findCountryAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
										Arrays.asList(timePeriod.getTimePeriodId()), mneSubmissionIds);
						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = (Long) objects[0];
							Long denominatorDataVal = (Long) objects[1];
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							Boolean isProfile = (Boolean) objects[4];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, 0, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																							// id
																							// =1

							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);
						}
						
						// COUNTRY WISE WAVELY AGGREGATION
						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountryWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										legacySubmissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = null != objects[0] ? (Long) objects[0] : null;
							Long denominatorDataVal = null != objects[1] ? (Long) objects[1] : null;
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							Boolean isProfile = (Boolean) objects[4];
							Integer wave = (Integer) objects[5];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, wave, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // India
																								// id
																								// =1
							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}

						// Aggregate by facility size and facility type-
						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountryTypeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										legacySubmissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = (Long) objects[0];
							Long denominatorDataVal = (Long) objects[1];
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							aggreagteType = (Integer) objects[4];
							Boolean isProfile = (Boolean) objects[5];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, 0, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																							// id
																							// =1
							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}

						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountrySizeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										legacySubmissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = (Long) objects[0];
							Long denominatorDataVal = (Long) objects[1];
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							aggreagteType = (Integer) objects[4];
							Boolean isProfile = (Boolean) objects[5];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, 0, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																							// id
																							// =1
							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}
						
						// Aggregate by facility type wise--wavely--
						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountryTypeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										legacySubmissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = (Long) objects[0];
							Long denominatorDataVal = (Long) objects[1];
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							aggreagteType = (Integer) objects[4];
							Boolean isProfile = (Boolean) objects[5];
							Integer wave = (Integer) objects[6];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, wave, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																							// id
																							// =1
							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}
						
						//country size---wavely
						aggregateData = null;
						aggregatedData = txnsncuDataRepository
								.findCountrySizeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
										legacySubmissionIds);

						for (Object[] objects : aggregatedData) {
							Long numeratorDataVal = (Long) objects[0];
							Long denominatorDataVal = (Long) objects[1];
							Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
							Integer indicatorId = (Integer) objects[3];
							aggreagteType = (Integer) objects[4];
							Boolean isProfile = (Boolean) objects[5];
							Integer wave = (Integer) objects[6];

							aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
									Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
									timePeriod, indicatorId, aggreagteType, isProfile, wave, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																							// id
																							// =1

							if (null != aggregateData)
								aggregateDataDomainList.add(aggregateData);

						}
					}

					aggregateDataRepository.save(aggregateDataDomainList);
				} catch (Throwable e) {
					LOGGER.info("area level : " + area.getLevel());
					LOGGER.error("message",e);
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					model.setErrorMessage("Something went wrong");
				}

			}
		}
		// all quarter--year time periods
		yearlyTimePeriods.addAll(quarterlyTimePeriods);

		if (!yearlyTimePeriods.isEmpty()) {

			List<TimePeriod> monthlyYearlyTimePeriods;
			// start quarterly and yearly aggregation
			for (TimePeriod timePeriod : yearlyTimePeriods) {
				
				//calculate the timeperiods between this
				// for which sncu data needs to be fetch
				TimePeriod startTimePeriod = timePeriodRepository.findByTimePeriod(timePeriod.getTimePeriod().split("-")[0].trim());
				TimePeriod endTimePeriod = timePeriodRepository.findByTimePeriod(timePeriod.getTimePeriod().split("-")[1].trim());
				
				monthlyYearlyTimePeriods = timePeriodRepository.findByTimePeriodIdBetweenAndPeriodicityOrderByTimePeriodIdAsc
						(startTimePeriod.getTimePeriodId(), endTimePeriod.getTimePeriodId(), messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null));
				

				List<Integer> monthlyTimeIds = new ArrayList<>();

				for (TimePeriod allTimePeriod : monthlyYearlyTimePeriods) {
					monthlyTimeIds.add(allTimePeriod.getTimePeriodId());
				}
				
				// iterate for all area--> facility/district/state/country
				for (Area area : areaList) {

					Integer aggreagteType = Integer
							.parseInt(messages.getMessage(Constants.Web.TOTAL_TYPEDETAIL_ID, null, null)); // total
																											// type
																											// detail
																											// ID

					// archive all aggregate data and then delete
					moveDataToArchiveAggregate(aggregateDataRepository
							.findByAreaAreaIdAndTimePeriodTimePeriodId(area.getAreaId(), timePeriod.getTimePeriodId()));
					// first we will delete all the aggregated data from table
					// for the given timeperiod and area
					// then we will persist the new record
					aggregateDataRepository.deleteByAreaAreaIdAndTimePeriodTimePeriodId(area.getAreaId(),
							timePeriod.getTimePeriodId());

					try {
						List<AggregateData> aggregateDataDomainList = new ArrayList<>();
						aggregatedData = null;

						if (area.getLevel() == Integer
								.parseInt(messages.getMessage(Constants.Web.FACILITY_LEVEL_ID, null, null))) { // facility
																												// level
							// in this level, if it is a monthly aggregation
							// directly
							// get the raw value from txn data for the given
							// facility
							// else get aggregated data

							// quarterly--yearly

							aggregatedData = txnsncuDataRepository
									.findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndTimePeriodTimePeriodIdInForFacilityAggregate(
											areaId, monthlyTimeIds, mneSubmissionIds);
							aggregateDataDomainList = setAggregateData(aggregatedData, timePeriod, aggreagteType, 0, messages.getMessage(Constants.Web.FACILITY_LEVEL, null, null));

							// district level
						} else if (area.getLevel() == Integer
								.parseInt(messages.getMessage(Constants.Web.DISTRICT_LEVEL_ID, null, null))) {
							aggregatedData = txnsncuDataRepository
									.findDistrictAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											monthlyTimeIds, area.getAreaId(), mneSubmissionIds);
							aggregateDataDomainList
									.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, 0, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));
							
							//district --wavely
							
							aggregatedData = txnsncuDataRepository
									.findDistrictWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, null, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));

							// aggregate by district wise facility type and size
							aggregatedData = txnsncuDataRepository
									.findDistrictTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));

							aggregatedData = txnsncuDataRepository
									.findDistrictSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));
							
							// aggregate by district wise facility type and size-----WAVELY
							aggregatedData = txnsncuDataRepository
									.findDistrictTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));

							aggregatedData = txnsncuDataRepository
									.findDistrictSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, messages.getMessage(Constants.Web.DISTRICT_LEVEL, null, null)));

							// state level
						} else if (area.getLevel() == Integer
								.parseInt(messages.getMessage(Constants.Web.STATE_LEVEL_ID, null, null))) {
							aggregatedData = txnsncuDataRepository
									.findStateAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											monthlyTimeIds, area.getAreaId(), mneSubmissionIds);
							aggregateDataDomainList
									.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, 0, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));
							
//							wavely
							aggregatedData = txnsncuDataRepository
									.findStateWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, aggreagteType, null, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));

							// aggregate by state wise facility type and size
							aggregatedData = txnsncuDataRepository
									.findStateTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));

							aggregatedData = txnsncuDataRepository
									.findStateSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, 0, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));
							
							// WAVELY aggregate by state wise facility type and size
							aggregatedData = txnsncuDataRepository
									.findStateTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));

							aggregatedData = txnsncuDataRepository
									.findStateSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
											Arrays.asList(timePeriod.getTimePeriodId()), area.getAreaId(),
											mneSubmissionIds);
							aggregateDataDomainList.addAll(setAggregateData(aggregatedData, timePeriod, null, null, messages.getMessage(Constants.Web.STATE_LEVEL, null, null)));

						} else {// COUNTRY_LEVEL
							// get sncu data
							// 0 index-data numerator value,1 index deno value,
							// 2
							// index-per value , 3 index-indicator id
							AggregateData aggregateData = null;
							aggregatedData = txnsncuDataRepository
									.findCountryAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
											monthlyTimeIds, mneSubmissionIds);
							for (Object[] objects : aggregatedData) {
								Long numeratorDataVal = (Long) objects[0];
								Long denominatorDataVal = (Long) objects[1];
								Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
								Integer indicatorId = (Integer) objects[3];
								Boolean isProfile = (Boolean) objects[4];

								aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
										Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
										timePeriod, indicatorId, aggreagteType, isProfile, 0, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																								// id
																								// =1

								if (null != aggregateData)
									aggregateDataDomainList.add(aggregateData);
							}
							
							// COUNTRY WISE WAVELY AGGREGATION
							aggregateData = null;
							aggregatedData = txnsncuDataRepository
									.findCountryWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
											legacySubmissionIds);

							for (Object[] objects : aggregatedData) {
								Long numeratorDataVal = null != objects[0] ? (Long) objects[0] : null;
								Long denominatorDataVal = null != objects[1] ? (Long) objects[1] : null;
								Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
								Integer indicatorId = (Integer) objects[3];
								Boolean isProfile = (Boolean) objects[4];
								Integer wave = (Integer) objects[5];

								aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
										Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
										timePeriod, indicatorId, aggreagteType, isProfile, wave, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // India
																									// id
																									// =1
								if (null != aggregateData)
									aggregateDataDomainList.add(aggregateData);

							}

							// Aggregate by facility size and facility type-
							aggregateData = null;
							aggregatedData = txnsncuDataRepository
									.findCountryTypeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
											legacySubmissionIds);

							for (Object[] objects : aggregatedData) {
								Long numeratorDataVal = (Long) objects[0];
								Long denominatorDataVal = (Long) objects[1];
								Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
								Integer indicatorId = (Integer) objects[3];
								aggreagteType = (Integer) objects[4];
								Boolean isProfile = (Boolean) objects[5];

								aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
										Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
										timePeriod, indicatorId, aggreagteType, isProfile, 0, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																								// id
																								// =1
								if (null != aggregateData)
									aggregateDataDomainList.add(aggregateData);

							}

							aggregateData = null;
							aggregatedData = txnsncuDataRepository
									.findCountrySizeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
											legacySubmissionIds);

							for (Object[] objects : aggregatedData) {
								Long numeratorDataVal = (Long) objects[0];
								Long denominatorDataVal = (Long) objects[1];
								Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
								Integer indicatorId = (Integer) objects[3];
								aggreagteType = (Integer) objects[4];
								Boolean isProfile = (Boolean) objects[5];

								aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
										Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
										timePeriod, indicatorId, aggreagteType, isProfile, 0, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																								// id
																								// =1
								if (null != aggregateData)
									aggregateDataDomainList.add(aggregateData);

							}
							
							// Aggregate by facility type wise--wavely--
							aggregateData = null;
							aggregatedData = txnsncuDataRepository
									.findCountryTypeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
											legacySubmissionIds);

							for (Object[] objects : aggregatedData) {
								Long numeratorDataVal = (Long) objects[0];
								Long denominatorDataVal = (Long) objects[1];
								Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
								Integer indicatorId = (Integer) objects[3];
								aggreagteType = (Integer) objects[4];
								Boolean isProfile = (Boolean) objects[5];
								Integer wave = (Integer) objects[6];

								aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
										Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
										timePeriod, indicatorId, aggreagteType, isProfile, wave, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																								// id
																								// =1
								if (null != aggregateData)
									aggregateDataDomainList.add(aggregateData);

							}
							
							//country size---wavely
							aggregateData = null;
							aggregatedData = txnsncuDataRepository
									.findCountrySizeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(timeIds,
											legacySubmissionIds);

							for (Object[] objects : aggregatedData) {
								Long numeratorDataVal = (Long) objects[0];
								Long denominatorDataVal = (Long) objects[1];
								Double percentDataVal = null != objects[2] ? ((Float) objects[2]).doubleValue() : null;
								Integer indicatorId = (Integer) objects[3];
								aggreagteType = (Integer) objects[4];
								Boolean isProfile = (Boolean) objects[5];
								Integer wave = (Integer) objects[6];

								aggregateData = setAggregateData(numeratorDataVal, denominatorDataVal, percentDataVal,
										Integer.parseInt(messages.getMessage(Constants.Web.INDIA_AREA_ID, null, null)),
										timePeriod, indicatorId, aggreagteType, isProfile, wave, messages.getMessage(Constants.Web.COUNTRY_LEVEL, null, null)); // india
																								// id
																								// =1

								if (null != aggregateData)
									aggregateDataDomainList.add(aggregateData);

							}
						}

						aggregateDataRepository.save(aggregateDataDomainList);
					} catch (Throwable e) {
						LOGGER.info("area level : " + area.getLevel());
						LOGGER.error("message",e);
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						model.setErrorMessage("Something went wrong");
					}

				}
			}
		}
		return model;
	}

	// For aggregation of Aug, the data will be filled by Sep last day, and the
	// data will be aggregated on Oct 1st
	@Override
	public TimePeriod createTimePeriodByPeriodicity(String periodicity) throws Exception {
		Calendar startDateCalendar = Calendar.getInstance();

		if (periodicity.equals(messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null)))
			startDateCalendar.add(Calendar.MONTH, -2);
		else if (periodicity.equals(messages.getMessage(Constants.Web.TIMEPERIOD_QUARTER_PERIODICITY, null, null)))
			startDateCalendar.add(Calendar.MONTH, -4);
		else
			startDateCalendar.add(Calendar.MONTH, -13);

		startDateCalendar.set(Calendar.DATE, 1);
		Date sDate = startDateCalendar.getTime();
		String startDateStr = simpleDateformater.format(sDate);
		Date startDate =  formatter.parse(startDateStr + " 00:00:00.000");
		startDateCalendar.set(Calendar.DATE, startDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.add(Calendar.MONTH, -2);
		endDateCalendar.set(Calendar.DATE, endDateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date eDate = endDateCalendar.getTime();
		String endDateStr = simpleDateformater.format(eDate);
		Date endDate =  formatter.parse(endDateStr + " 00:00:00.000");
		TimePeriod utTime = timePeriodRepository.findByStartDateAndEndDate(startDate, endDate);

		if (utTime == null) {
			TimePeriod utTimePeriod = new TimePeriod();
			utTimePeriod.setStartDate(startDate);
			utTimePeriod.setEndDate(endDate);
			utTimePeriod.setPeriodicity(periodicity); // for monthly aggregation
														// periodicity is 1
														// //quarter- 3, year-
														// 12
			utTimePeriod.setCreatedDate(new Timestamp(new Date().getTime()));
			if (periodicity.equals(messages.getMessage(Constants.Web.TIMEPERIOD_PREODICITY_MONTHLY, null, null)))
				utTimePeriod.setTimePeriod(simpleDateformat.format(startDate));
			else
				utTimePeriod.setTimePeriod(simpleDateformat.format(startDate) + "-" + simpleDateformat.format(endDate));
			utTimePeriod.setWave(2); // change later
			utTimePeriod = timePeriodRepository.save(utTimePeriod);
			return utTimePeriod;
		}
		return utTime;
	}

	/**
	 * @param aggregateDatas
	 * moves the data from AggregateData to ArchiveAggregateData
	 */
	public void moveDataToArchiveAggregate(List<AggregateData> aggregateDatas) {

		if (!aggregateDatas.isEmpty()) {
			List<ArchiveAggregateData> archiveAggregateDatas = new ArrayList<>();

			for (AggregateData aggregateData : aggregateDatas) {
				ArchiveAggregateData archiveAggregateData = new ArchiveAggregateData();
				archiveAggregateData.setArcArea(aggregateData.getArea());
				archiveAggregateData.setArcCreatedDate(aggregateData.getCreatedDate());
				archiveAggregateData.setArcDenominatorValue(aggregateData.getDenominatorValue());
				archiveAggregateData.setArcNumeratorValue(aggregateData.getNumeratorValue());
				archiveAggregateData.setArcIndicator(aggregateData.getIndicator());
				archiveAggregateData.setArcPercentage(aggregateData.getPercentage());
				archiveAggregateData.setArcTimePeriod(aggregateData.getTimePeriod());
				archiveAggregateDatas.add(archiveAggregateData);
			}
			archiveAggregateDataRepository.save(archiveAggregateDatas);
		}

	}
	
	
	//comment later
	/*@Override
	@Transactional(rollbackFor = Exception.class)
	public String aggregateMonthly() {
		List<TimePeriod> timePeriods = timePeriodRepository.findAllByPeriodicityOrderByTimePeriodIdAsc("1");
		for (TimePeriod timePeriod : timePeriods) {
			if(timePeriod.getTimePeriodId() < 44){
				aggregateSNCUData("1", timePeriod.getTimePeriodId());
			}
		}
		return "aggregated";
	}*/
	//create quarterly time periods in prod db and call aggregations
	//comment later
	/*@Override
	@Transactional(rollbackFor = Exception.class)
	public String createAggregateQuartelyAndYearlyTps() {
		List<TimePeriod> timePeriods = timePeriodRepository.findAllByPeriodicityOrderByTimePeriodIdAsc("1");
		Date startDate = null;
		Date endDate= null;
		String tp="";
				
		for (int i = 0; i < 36; i++) { //untill Oct 2017 as Nov 2017-Jan 2018 tp is already created
			
			int endCount = i+2;
			startDate = timePeriods.get(i).getStartDate();
			tp = timePeriods.get(i).getTimePeriod() + "-" + timePeriods.get(endCount).getTimePeriod();
			endDate = timePeriods.get(endCount).getEndDate();

			TimePeriod newTp = new TimePeriod();
			newTp.setCreatedDate(new Timestamp(new Date().getTime()));
			newTp.setTimePeriod(tp);
			newTp.setStartDate(startDate);
			newTp.setEndDate(endDate);
			newTp.setPeriodicity("3");
			newTp.setWave(1);
			TimePeriod quarterTp = timePeriodRepository.save(newTp);
			
			aggregateSNCUData("3", quarterTp.getTimePeriodId());

			if (timePeriods.get(i).getTimePeriodId() == 12 || timePeriods.get(i).getTimePeriodId() == 24
					|| timePeriods.get(i).getTimePeriodId() == 37) {

				tp = timePeriods.get(i - 9).getTimePeriod() + "-" + timePeriods.get(endCount).getTimePeriod();
				startDate = timePeriods.get(i - 9).getStartDate();
				endDate = timePeriods.get(endCount).getEndDate();

				newTp = new TimePeriod();
				newTp.setCreatedDate(new Timestamp(new Date().getTime()));
				newTp.setTimePeriod(tp);
				newTp.setStartDate(startDate);
				newTp.setEndDate(endDate);
				newTp.setPeriodicity("12");
				newTp.setWave(1);
				TimePeriod yearlyTp = timePeriodRepository.save(newTp);
				
				aggregateSNCUData("12", yearlyTp.getTimePeriodId());
				
				
			}
			i = i + 2;
		}
		
		List<TimePeriod> timePeriods = timePeriodRepository.findAllByPeriodicityOrderByTimePeriodIdAsc("3");
		for (TimePeriod timePeriod : timePeriods) {
				aggregateSNCUData("3", timePeriod.getTimePeriodId());
		}
		
		List<TimePeriod> yearlyTimePeriods = timePeriodRepository.findAllByPeriodicityOrderByTimePeriodIdAsc("12");
		for (TimePeriod timePeriod : yearlyTimePeriods) {
				aggregateSNCUData("12", timePeriod.getTimePeriodId());
		}
		return "quarter and yearly aggregated";
		
	}*/
}
	