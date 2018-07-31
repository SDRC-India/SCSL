package org.sdrc.scsl.repository.springdatajpa;

import java.util.List;

import org.sdrc.scsl.domain.AggregateData;
import org.sdrc.scsl.repository.AggregateDataRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 * @author Debiprasad
 * @author Subrat
 */
public interface SpringDataAggregateDataRepository
		extends AggregateDataRepository, JpaRepository<AggregateData, Integer> {

	@Override
	@Query("SELECT agdt FROM AggregateData agdt,Indicator ind,Area ar,TimePeriod tim "
			+ "WHERE agdt.area.areaId = ar.areaId  AND agdt.indicator.indicatorId = ind.indicatorId "
			+ "AND agdt.timePeriod.timePeriodId = tim.timePeriodId AND agdt.area.areaId=:areaId AND tim.timePeriodId  BETWEEN :startDate AND :endDate "
			+ "AND tim.periodicity = :periodicity  AND agdt.aggreagteType.typeDetailId = :typeDetailId AND agdt.wave = :wave ")
	 List<AggregateData> findByAreaIdAndAggreagteType(@Param("startDate")Integer startDate, 
			 @Param("endDate")Integer endDate,@Param("areaId") Integer areaId, @Param("typeDetailId")Integer typeDetailId,
				@Param("periodicity") String periodicity, @Param("wave")Integer wave) ;
	
	@Override
	@Query(value= "SELECT d.area_id_fk, d.timeperiod_id_fk, d.countdone, t.totalcount "
			+ "FROM (select ad.area_id_fk, ad.timeperiod_id_fk,"
			+ "(COUNT(CASE WHEN numerator_value IS NOT NULL AND denominator_value IS NOT NULL then 1 END)) "
			+ "AS countdone FROM aggregate_data ad, area aa, indicator ind, timeperiod tp "
			+ "WHERE ad.area_id_fk = aa.id "
			+ "AND aa.id in(select a1.id from area a1) "
			+ "AND ad.indicator_id_fk = ind.id "
			+ "AND ad.timeperiod_id_fk = tp.id "
			+ "AND ad.area_id_fk IN (:facilityIds) "
			+ "AND tp.id between :startDate and :endDate "
			+ "AND tp.periodicity = :periodicity  "
			+ "AND ind.is_profile IS NULL "
			+ "GROUP BY ad.area_id_fk,ad.timeperiod_id_fk) d "
			+ "CROSS JOIN (SELECT iftm.facility_id_fk, iftm.timeperiod_id_fk, COUNT(iftm) as totalcount "
			+ "FROM indicator_facility_timeperiod_mapping iftm,  indicator indic "
			+ "WHERE iftm.facility_id_fk in (:facilityIds) "
			+ "AND iftm.timeperiod_id_fk BETWEEN :startDate and :endDate "
			+ "AND iftm.indicator_id_fk = indic.id "
			+ "AND indic.is_profile IS NULL "
			+ "GROUP BY iftm.facility_id_fk, iftm.timeperiod_id_fk) t "
			+ "WHERE d.area_id_fk = t.facility_id_fk "
			+ "AND d.timeperiod_id_fk = t.timeperiod_id_fk", nativeQuery=true)
	public List<Object[]> fetchByDataCompletion(@Param("startDate")Integer startDate, @Param("endDate")Integer endDate,
			@Param("facilityIds")List<Integer> facilityIds,
			@Param("periodicity") String periodicity);
	
	@Override
	@Query("SELECT agdt FROM AggregateData agdt,Indicator ind,Area ar,TimePeriod tim "
			+ "WHERE agdt.area.areaId = ar.areaId  AND agdt.indicator.indicatorId = ind.indicatorId "
			+ "AND agdt.timePeriod.timePeriodId= tim.timePeriodId AND agdt.area.areaId in"
			+ "(SELECT arr.areaId FROM Area arr WHERE arr.parentAreaId=:areaId OR arr.areaId=:areaId) "
			+ "AND tim.timePeriodId  BETWEEN :startDate AND :endDate AND tim.periodicity = :periodicity "
			+ "AND agdt.aggreagteType.typeDetailId = :aggreagteTypeId AND agdt.wave = :wave "
			+ "ORDER BY agdt.area.areaId ")
	List<AggregateData> fetchByParentAreaId(@Param("startDate") Integer startDate, @Param("endDate") Integer endDate,
			@Param("areaId") Integer areaId, @Param("periodicity") String periodicity, @Param("aggreagteTypeId") Integer aggreagteTypeId, @Param("wave")Integer wave);
	
	@Override
	@Query("SELECT Count(dt) as total, dt.indicator.indicatorId, dt.timePeriod.timePeriodId FROM AggregateData dt,Area ar "
			+ "WHERE dt.timePeriod.timePeriodId BETWEEN :startDate and :endDate "
			+ "AND dt.timePeriod.periodicity = :periodicity "
			+ "AND dt.area.areaId = ar.areaId AND ar.level = 4 "
			+ "AND dt.aggreagteType.typeDetailId = :aggreagteTypeId AND dt.wave = :wave "
			+ "GROUP BY dt.indicator.indicatorId , dt.timePeriod.timePeriodId "
			+ "ORDER BY dt.timePeriod.timePeriodId, dt.indicator.indicatorId")
    List<Object[]> fetchByCountryCount(@Param("startDate") Integer startDate, @Param("endDate") Integer endDate,
			@Param("periodicity") String periodicity, @Param("aggreagteTypeId") Integer aggreagteTypeId, @Param("wave")Integer wave);
    
    
    @Override
    @Query("SELECT Count(dt) as total,dt.indicator.indicatorId, dt.timePeriod.timePeriodId, state.areaName "
    		+ " FROM AggregateData dt , Area ar, Area parent, Area state WHERE  ar.parentAreaId = parent.areaId "
    		+ " AND parent.parentAreaId = state.areaId AND ar.areaId = dt.area.areaId AND state.level = 2 "
    		+ " AND dt.timePeriod.timePeriodId between :startDate and :endDate "
    		+ " AND dt.timePeriod.periodicity = :periodicity "
    		+ " AND dt.aggreagteType.typeDetailId = :aggreagteTypeId AND dt.wave = :wave "
    		+ " GROUP BY dt.indicator.indicatorId , state.areaId, dt.timePeriod.timePeriodId"
    		+ " ORDER BY dt.timePeriod.timePeriodId, dt.indicator.indicatorId, state.areaId")
     List<Object[]> fetchByStateCount(@Param("startDate") Integer startDate, @Param("endDate") Integer endDate,
				@Param("periodicity") String periodicity, @Param("aggreagteTypeId") Integer aggreagteTypeId, @Param("wave")Integer wave);
     

     @Override
     @Query("SELECT Count(dt) as total, dt.indicator.indicatorId, dt.timePeriod.timePeriodId, state.areaName "
     		+ "FROM AggregateData dt , Area ar, Area parent, Area state "
     		+ "WHERE  ar.parentAreaId = parent.areaId "
     		+ "AND parent.parentAreaId = state.areaId AND ar.areaId = dt.area.areaId "
     		+ "AND state.level = 2 AND dt.timePeriod.timePeriodId BETWEEN :startDate and :endDate "
     		+ "AND dt.timePeriod.periodicity = :periodicity "
     		+ "AND dt.aggreagteType.typeDetailId = :aggreagteTypeId AND dt.wave = :wave "
     		+ "AND state.areaId =:stateId GROUP BY dt.indicator.indicatorId , state.areaId, dt.timePeriod.timePeriodId "
     		+ "ORDER BY dt.timePeriod.timePeriodId, dt.indicator.indicatorId, state.areaId")
    List<Object[]> fetchBySelectedStateId(@Param("startDate")Integer startDate, @Param("endDate")Integer endDate,@Param("stateId") Integer stateId,
			@Param("periodicity") String periodicity, @Param("aggreagteTypeId") Integer aggreagteTypeId, @Param("wave")Integer wave);
    
    
    
    @Override
    @Query("SELECT Count(dt) as total, dt.indicator.indicatorId, dt.timePeriod.timePeriodId, parent.areaName "
    		+ "FROM AggregateData dt , Area ar, Area parent WHERE  ar.parentAreaId = parent.areaId "
    		+ "AND ar.areaId = dt.area.areaId AND parent.level = 3 and dt.timePeriod.timePeriodId BETWEEN :startDate AND :endDate "
    		+ "AND dt.timePeriod.periodicity = :periodicity "
    		+ "AND parent.parentAreaId =:stateId "
    		+ "AND dt.aggreagteType.typeDetailId = :aggreagteTypeId AND dt.wave = :wave "
    		+ "GROUP BY dt.indicator.indicatorId , parent.areaId, dt.timePeriod.timePeriodId "
    		+ "ORDER BY dt.timePeriod.timePeriodId, dt.indicator.indicatorId, parent.areaId")
    List<Object[]> fetchDistictBySelectedStateId(@Param("startDate")Integer startDate, @Param("endDate")Integer endDate,@Param("stateId") Integer stateId,
			@Param("periodicity") String periodicity, @Param("aggreagteTypeId") Integer aggreagteTypeId, @Param("wave")Integer wave);
    
    
    //new query
    @Override
	@Query("SELECT agdt,ind,ar,tim FROM AggregateData agdt, Indicator ind, Area ar, TimePeriod tim "
			+ "WHERE agdt.indicator.indicatorId = ind.indicatorId "
			+ "AND agdt.timePeriod.timePeriodId = tim.timePeriodId " 
			+ "AND agdt.area.areaId = ar.areaId "
			+ "AND ar.areaId IN (:facilityIds) " 
			+ "AND tim.timePeriodId BETWEEN :startDate AND :endDate "
			+ "AND tim.periodicity = :periodicity ORDER BY agdt.area.areaId")
	List<Object[]> findByFacilityTypeIdsAndFacility(@Param("facilityIds") List<Integer> facilityIds,
			@Param("startDate") Integer startDate, @Param("endDate") Integer endDate,
			@Param("periodicity") String periodicity);
	
	//top line box chart dynamic data in dashboard home for Andhra and Telangana
	@Override
	@Query("SELECT SUM(agdt.numeratorValue), agdt.area.areaId FROM AggregateData agdt WHERE agdt.indicator.indicatorId IN (:indicatorId) "
			+ "AND agdt.timePeriod.timePeriodId IN (:timePeriodId) "
			+ "AND agdt.area.areaId IN (:areaId) AND agdt.aggreagteType.typeDetailId = :aggreagteTypeId "
			+ "AND agdt.wave = :wave GROUP BY agdt.area.areaId ORDER BY agdt.area.areaId")
	List<Object[]> findByIndicatorIdTimePeriodIdAndAreaId(@Param("indicatorId") List<Integer> indicatorIds, 
			@Param("timePeriodId") List<Integer> timePeriodId, @Param("areaId") List<Integer> areaIds, @Param("aggreagteTypeId") Integer aggreagteTypeId, @Param("wave")Integer wave);
	
	//facility dashboard view
	//pAverage [0] , indicator_id_fk [1], indicator_name[2]
	@Override
	@Query("SELECT CASE WHEN CAST(SUM(agdt.denominatorValue) AS float) <> 0 THEN "
			+ "(CAST(SUM(agdt.numeratorValue) AS float) / CAST(SUM(agdt.denominatorValue) AS float)) ELSE 0 END, agdt.indicator.indicatorId "
			+ "FROM AggregateData agdt WHERE agdt.timePeriod.timePeriodId in (:timePeriodId) and agdt.area.areaId= :areaId "
			+ "AND agdt.indicator.isProfile IS NULL "
			+ "GROUP BY agdt.indicator.indicatorId "
			+ "ORDER BY agdt.indicator.indicatorId")
	List<Object[]> findPAverageGroupByIndicatorAndTimePeriodByFacilityId(@Param("timePeriodId") List<Integer> timePeriodIds,
			@Param("areaId") Integer areaId);
	
//	@Override
//	@Query(value="SELECT CAST(SUM(agdt.numeratorValue) AS float) / CAST(SUM(agdt.denominatorValue) AS float) "
//			+ "FROM AggregateData agdt WHERE agdt.timePeriod.timePeriodId in (:timePeriodId) and agdt.area.areaId= :areaId "
//			+ "AND agdt.indicator.indicatorId = :indicatorId "
//			+ "AND agdt.aggreagteType.typeDetailId = :typeDetailId AND agdt.wave = :wave "
//			+ "GROUP BY agdt.indicator.indicatorId "
//			+ "ORDER BY agdt.indicator.indicatorId")
//	List<Object[]> findPAverageByTimePeriodByAreaId(@Param("timePeriodId") List<Integer> timePeriodIds,
//			@Param("areaId") Integer areaId, @Param("indicatorId") Integer indicatorId);
	
//	@Override
//	@Query(value="SELECT CAST(SUM(agdt.numeratorValue) AS float) / CAST(SUM(agdt.denominatorValue) AS float), agdt.area.areaId "
//			+ "FROM AggregateData agdt WHERE agdt.timePeriod.timePeriodId in (:timePeriodId) AND agdt.area.areaId IN (:areaId) "
//			+ "AND CASE WHEN agdt.area.areaId = :aggregateAreaId THEN agdt.aggreagteType.typeDetailId = :aggreagteTypeId AND agdt.wave = :wave "
//			+ "ELSE agdt.aggreagteType.typeDetailId = :facilityAggreagteTypeId AND agdt.wave = :waveFacilty END "
//			+ "AND agdt.indicator.indicatorId = :indicatorId "
//			+ "GROUP BY agdt.area.areaId "
//			+ "ORDER BY agdt.area.areaId")
//	List<Object[]> findAllPAverageByTimePeriodIdAndIndicatorIdAndAreaId(@Param("timePeriodId") List<Integer> timePeriodIds,
//			@Param("areaId") List<Integer> areaIds, @Param("indicatorId") Integer indicatorId, @Param("aggregateAreaId") Integer aggregateAreaId,
//			@Param("aggreagteTypeId") Integer aggreagteTypeId, @Param("wave")Integer wave,
//			@Param("facilityAggreagteTypeId") Integer facilityAggreagteTypeId, @Param("waveFacilty")Integer waveFacilty);
	
	//the case is not working in HQL so we have used native query, we need to update the JPA version 
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.AggregateDataRepository#findAllPAverageByTimePeriodIdAndIndicatorIdAndAreaId(java.util.List, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query(value="SELECT CASE WHEN CAST(SUM(agdt.denominator_value) AS float) <> 0 THEN "
			+ "(CAST(SUM(agdt.numerator_value) AS float) / CAST(SUM(agdt.denominator_value) AS float)) ELSE NULL END, agdt.area_id_fk "
			+ "FROM aggregate_data agdt WHERE agdt.timeperiod_id_fk in (:timePeriodId) AND agdt.area_id_fk IN (:areaId) "
			+ "AND CASE WHEN agdt.area_id_fk = :aggregateAreaId THEN agdt.aggregate_type_fk = :aggreagteTypeId AND agdt.wave = :wave "
			+ "ELSE agdt.aggregate_type_fk = :facilityAggreagteTypeId AND agdt.wave = :waveFacilty END "
			+ "AND agdt.indicator_id_fk = :indicatorId "
			+ "GROUP BY agdt.area_id_fk "
			+ "ORDER BY agdt.area_id_fk", nativeQuery=true)
	List<Object[]> findAllPAverageByTimePeriodIdAndIndicatorIdAndAreaId(@Param("timePeriodId") List<Integer> timePeriodIds,
			@Param("areaId") List<Integer> areaIds, @Param("indicatorId") Integer indicatorId, @Param("aggregateAreaId") Integer aggregateAreaId,
			@Param("aggreagteTypeId") Integer aggreagteTypeId, @Param("wave")Integer wave,
			@Param("facilityAggreagteTypeId") Integer facilityAggreagteTypeId, @Param("waveFacilty")Integer waveFacilty);
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.AggregateDataRepository#findByTimePeriodIdAndIndicatorIdAndAreaId(java.util.List, java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query(value="SELECT agdt.numerator_value, agdt.denominator_value, agdt.area_id_fk, tp.time_period, ar.area_name, tp.id "
			+ "FROM aggregate_data agdt, area ar, timePeriod tp WHERE tp.id = agdt.timeperiod_id_fk AND agdt.area_id_fk = ar.id "
			+ "AND agdt.timeperiod_id_fk in (:timePeriodId) AND agdt.area_id_fk IN (:areaId) "
			+ "AND CASE WHEN agdt.area_id_fk = :aggregateAreaId THEN agdt.aggregate_type_fk = :aggreagteTypeId AND agdt.wave = :wave "
			+ "ELSE agdt.aggregate_type_fk = :facilityAggreagteTypeId AND agdt.wave = :waveFacilty END "
			+ "AND agdt.indicator_id_fk = :indicatorId "
			+ "ORDER BY ar.level, agdt.area_id_fk, agdt.timeperiod_id_fk", nativeQuery=true)
	List<Object[]> findByTimePeriodIdAndIndicatorIdAndAreaId(@Param("timePeriodId") List<Integer> timePeriodIds,
			@Param("areaId") List<Integer> areaIds, @Param("indicatorId") Integer indicatorId, @Param("aggregateAreaId") Integer aggregateAreaId,
			@Param("aggreagteTypeId") Integer aggreagteTypeId, @Param("wave")Integer wave,
			@Param("facilityAggreagteTypeId") Integer facilityAggreagteTypeId, @Param("waveFacilty")Integer waveFacilty);
}
