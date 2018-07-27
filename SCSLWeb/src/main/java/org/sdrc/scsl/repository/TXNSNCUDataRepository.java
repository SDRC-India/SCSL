package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.TXNSNCUData;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @author Sarita Panigrahi
 * @author Subrat
 *
 */
public interface TXNSNCUDataRepository {

	@Transactional
	<S extends TXNSNCUData> List<S> save(Iterable<S> entities);

	List<TXNSNCUData> findByTxnSubmissionManagementTxnSubmissionId(Integer txnSubmissionId);

	List<TXNSNCUData> findByTxnSubmissionManagementTxnSubmissionIdAndIsLiveTrue(Integer txnSubmissionId);

	List<Object[]> findCountryAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(List<Integer> timePeriodIds, List<Integer> submissionIds);

	List<Object[]> findDistrictAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	List<Object[]> findStateAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	List<TXNSNCUData> findByIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdAndIsLiveTrueAndTxnSubmissionManagementStatusMneTypeDetailId(
			Integer timePeriodId, List<Integer> submissionIds);

	List<TXNSNCUData> findAll();

	List<TXNSNCUData> findByTxnSubmissionManagementTxnSubmissionIdOrderByIndicatorFacilityTimeperiodMappingIndicatorIndicatorOrderAsc(
			Integer txnSubmissionId);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param timePeriodId
	 * @return
	 */
	List<TXNSNCUData> findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodId(
			Integer facilityId, Integer timePeriodId, List<Integer> submissionIds);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param timePeriodIds
	 * @param areaId
	 * @return aggregation of historical data // district
	 */
	List<Object[]> findDistrictAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param timePeriodIds
	 * @param areaId
	 * @return aggregation of historical data //state
	 */
	List<Object[]> findStateAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param timePeriodIds
	 * @param areaId
	 * @return aggregation of historical data // facility
	 */
	List<Object[]> findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndTimePeriodTimePeriodIdInForFacilityAggregate(
			Integer areaId, List<Integer> timePeriodIds, List<Integer> submissionIds);

	/**
	 * 
	 * Extractive values by timeperiod
	 * 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 08-May-2017 3:05:10
	 *         pm
	 */
	List<TXNSNCUData> findByTimePeriodId(int timePeriodId);

	List<TXNSNCUData> findByTimePeriodIdAndAreaId(int timePeriodId, int areaId);

	List<TXNSNCUData> findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdInAndIsLiveTrue(
			Integer areaId, List<Integer> timePeriodIds);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param areaId
	 * @param timePeriodId
	 */
	@Transactional
	void updateByTXNSubmissionManagementTxnSubmissionId(Integer txnSubmissionId);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findFacilityAggregatedDataByIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

//	List<Object[]> findCountryTypeSizeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
//			List<Integer> timePeriodIds, List<Integer> submissionIds, Integer aggregateTypeId);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findCountryTypeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(List<Integer> timePeriodIds,
			List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findCountrySizeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(List<Integer> timePeriodIds,
			List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 * state wise ---- facility type aggregation
	 */
	List<Object[]> findStateTypeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 * state wise ---- facility size aggregation
	 */
	List<Object[]> findStateSizeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictTypeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictSizeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findStateTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findStateSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 30-Sep-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictTypeWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 30-Sep-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findCountryWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictSizeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findStateWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findStateTypeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findStateSizeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findCountryTypeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findCountrySizeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findDistrictSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findStateWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findStateTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);

	/** 
	 * @author Sarita Panigrahi, created on: 01-Oct-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @param submissionIds
	 * @return
	 */
	List<Object[]> findStateSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			List<Integer> timePeriodIds, Integer areaId, List<Integer> submissionIds);
}
