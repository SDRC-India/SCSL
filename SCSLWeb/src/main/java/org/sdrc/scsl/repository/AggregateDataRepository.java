package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.AggregateData;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 * @author Debiprasad
 * @author Subrat
 * This repository belongs to AggregateData table
 */
public interface AggregateDataRepository {

	/** 
	 * Saves a list of AggregateData
	 * @author Sarita Panigrahi
	 * @param entities
	 * @return
	 */
	@Transactional
	<S extends AggregateData> List<S> save(Iterable<S> entities);
	
	/** 
	 * 
	 * @author Subrat Pradhan
	 * @param startDate
	 * @param endDate
	 * @param facilityTypeIds
	 * @param periodicity
	 * @return count of indicator reported vs total no. of mapped indicators by facility, time id
	 */
	List<Object[]> fetchByDataCompletion(Integer startDate, Integer endDate, List<Integer> facilityTypeIds, String periodicity);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param facilityId
	 * @param timePeriodId
	 * delete by AreaId And TimePeriodid while aggregating 
	 */
	@Transactional
	void deleteByAreaAreaIdAndTimePeriodTimePeriodId(Integer facilityId, Integer timePeriodId);
	
	/** 
	 * @author Sarita Panigrahi, created on: 22-Sep-2017
	 * @param areaId
	 * @param timePeriodId
	 * @return List<AggregateData> by area id and timeid
	 */
	List<AggregateData> findByAreaAreaIdAndTimePeriodTimePeriodId(Integer areaId, Integer timePeriodId);
	
	/** 
	 *@author Debiprasad Parida, @author Sarita Panigrahi, created on: 03-Oct-2017
	 * @param startDate
	 * @param endDate
	 * @param areaId
	 * @param periodicity
	 * @param aggreagteTypeId
	 * @param wave
	 * @return List<AggregateData> by parent id
	 */
	List<AggregateData> fetchByParentAreaId(Integer startDate, Integer endDate,Integer areaId, String periodicity, Integer aggreagteTypeId, Integer wave);
	
	/** 
	 * @author Debiprasad Parida, Sarita Panigrahi
	 * @param startDate
	 * @param endDate
	 * @param periodicity
	 * @param aggreagteTypeId
	 * @param wave
	 * @return list of child facilities of state
	 */
	List<Object[]> fetchByStateCount(Integer startDate, Integer endDate, String periodicity, Integer aggreagteTypeId, Integer wave);
	
	/** 
	 * @author Debiprasad Parida, Sarita Panigrahi
	 * @param startDate
	 * @param endDate
	 * @param stateId
	 * @param periodicity
	 * @param aggreagteTypeId
	 * @param wave
	 * @return list of child facilities of a selected state
	 */
	List<Object[]> fetchBySelectedStateId(Integer startDate, Integer endDate,Integer stateId, String periodicity, Integer aggreagteTypeId, Integer wave);
	
	/** 
	 * @author Debiprasad Parida, Sarita Panigrahi
	 * @param startDate
	 * @param endDate
	 * @param stateId
	 * @param periodicity
	 * @param aggreagteTypeId
	 * @param wave
	 * @return list of child districts of a selected state
	 */
	List<Object[]> fetchDistictBySelectedStateId(Integer startDate, Integer endDate,Integer stateId, String periodicity, Integer aggreagteTypeId, Integer wave);

	/** 
	 *@author Debiprasad Parida,  @author Sarita Panigrahi, created on: 03-Oct-2017
	 * @param startDate
	 * @param endDate
	 * @param areaId
	 * @param typeDetailId
	 * @param periodicity
	 * @param wave
	 * @return
	 */
	List<AggregateData> findByAreaIdAndAggreagteType(Integer startDate, Integer endDate, Integer areaId,
			Integer typeDetailId, String periodicity, Integer wave);

	/** 
	 * @author  Sarita Panigrahi
	 * @param facilityIds
	 * @param startDate
	 * @param endDate
	 * @param periodicity
	 * @return
	 */
	List<Object[]> findByFacilityTypeIdsAndFacility(List<Integer> facilityIds, Integer startDate, Integer endDate,
			String periodicity);

	/** 
	 * @author Debiprasad Parida, Sarita Panigrahi
	 * @param startDate
	 * @param endDate
	 * @param periodicity
	 * @param aggreagteTypeId
	 * @param wave
	 * @return
	 */
	List<Object[]> fetchByCountryCount(Integer startDate, Integer endDate, String periodicity, Integer aggreagteTypeId, Integer wave);



	/** 
	 * @author Sarita Panigrahi, created on: 04-Oct-2017
	 * @param indicatorIds
	 * @param timePeriodIds
	 * @param areaIds
	 * @param aggreagteTypeId
	 * @param wave
	 * @return
	 */
	List<Object[]> findByIndicatorIdTimePeriodIdAndAreaId(List<Integer> indicatorIds, List<Integer> timePeriodIds,
			List<Integer> areaIds, Integer aggreagteTypeId, Integer wave);

	/** 
	 * @author Sarita Panigrahi, created on: 11-Oct-2017
	 * @param timePeriodIds
	 * @param areaId
	 * @return
	 */
	List<Object[]> findPAverageGroupByIndicatorAndTimePeriodByFacilityId(List<Integer> timePeriodIds, Integer areaId);

	/** 
	 * @author Sarita Panigrahi, created on: 11-Oct-2017
	 * @param timePeriodId
	 * @param areaId
	 * @return
	 */
//	List<AggregateData> findByAreaIdAndTimePeriodIdIn(List<Integer> timePeriodId, Integer areaId);
	
	/** 
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param areaId
	 * @param timePeriodIds
	 * @return
	 */
	List<AggregateData> findByIndicatorIsProfileIsNullAndAreaAreaIdAndTimePeriodTimePeriodIdInOrderByTimePeriodTimePeriodIdAsc(Integer areaId, List<Integer> timePeriodIds);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param timePeriodIds
	 * @param areaIds
	 * @param indicatorId
	 * @param aggregateAreaId
	 * @param aggreagteTypeId
	 * @param wave
	 * @param facilityAggreagteTypeId
	 * @param waveFacilty
	 * @return
	 * Find P-average of aggregated facility and child facility for a selected indicator in a time limit
	 */
	List<Object[]> findAllPAverageByTimePeriodIdAndIndicatorIdAndAreaId(List<Integer> timePeriodIds,
			List<Integer> areaIds, Integer indicatorId, Integer aggregateAreaId, Integer aggreagteTypeId, Integer wave,
			Integer facilityAggreagteTypeId, Integer waveFacilty);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Oct-2017
	 * @param timePeriodIds
	 * @param areaIds
	 * @param indicatorId
	 * @param aggregateAreaId
	 * @param aggreagteTypeId
	 * @param wave
	 * @param facilityAggreagteTypeId
	 * @param waveFacilty
	 * @return
	 * Find all of aggregated facility and child facility for a selected indicator in a time limit
	 */
	List<Object[]> findByTimePeriodIdAndIndicatorIdAndAreaId(List<Integer> timePeriodIds, List<Integer> areaIds,
			Integer indicatorId, Integer aggregateAreaId, Integer aggreagteTypeId, Integer wave,
			Integer facilityAggreagteTypeId, Integer waveFacilty);

}
