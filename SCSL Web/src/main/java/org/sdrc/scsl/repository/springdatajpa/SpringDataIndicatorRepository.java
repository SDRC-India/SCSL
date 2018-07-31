package org.sdrc.scsl.repository.springdatajpa;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.Indicator;
import org.sdrc.scsl.repository.IndicatorRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:15:53 am
 */
@RepositoryDefinition(domainClass=Indicator.class,idClass=Integer.class)
 interface SpringDataIndicatorRepository extends IndicatorRepository{
	
	@Override
	@Query("SELECT ind, iftm "
			+ "FROM IndicatorFacilityTimeperiodMapping iftm, Indicator ind "
			+ "WHERE iftm.timePeriod.timePeriodId = :timeperiodId "
			+ "AND iftm.facility.areaId = :facilityId "
			+ "AND iftm.indicator.indicatorId = ind.indicatorId "
			+ "ORDER BY ind.indicatorOrder ")
	 List<Object[]> getIndicatorFacilityTimeperiodMappingByFacilityIdAndTimePeriodIdOrderByIndicatorOrder(@Param("facilityId") Integer facilityId, @Param("timeperiodId") Integer timeperiodId);
	
	@Override
	@Query("SELECT ind, iftm, txndata "
			+ "FROM IndicatorFacilityTimeperiodMapping iftm, Indicator ind, TXNSNCUData txndata "
			+ "WHERE iftm.timePeriod.timePeriodId = :timeperiodId  "
			+ "AND iftm.facility.areaId = :facilityId "
			+ "AND iftm.indicator.indicatorId = ind.indicatorId "
			+ "AND txndata.txnSubmissionManagement.txnSubmissionId = :txnSubmissionId "
			+ "AND txndata.indicatorFacilityTimeperiodMapping.indFacilityTpId = iftm.indFacilityTpId "
			+ "ORDER BY ind.indicatorOrder")
	 List<Object[]> fetchIndicatorsName(@Param("facilityId") Integer facilityId, @Param("timeperiodId") Integer timeperiodId,
			@Param("txnSubmissionId") Integer txnSubmissionId);
	
	@Override
	@Query("SELECT ind FROM Indicator ind "
			+ "WHERE ind.indicatorType.typeDetailId = :indType "
			+ "AND ind.indicatorId NOT IN "
			+ "(SELECT iftm.indicator.indicatorId FROM IndicatorFacilityTimeperiodMapping iftm, UserAreaMapping uam "
			+ "WHERE iftm.facility.areaId = uam.facility.areaId AND uam.facility.areaId = :facilityId "
			+ "AND iftm.timePeriod.timePeriodId = :timePeriodId) ORDER BY ind.indicatorOrder")
	 List<Indicator> fetchIndicatorsNameByFacilityIdAndTimePeriodId(@Param("facilityId") Integer facilityId,
			@Param("timePeriodId") Integer timeperiodId, @Param("indType") Integer indType);
	
	@Override
	@Query("SELECT i FROM Indicator i WHERE i.createdDate > :date or i.updatedDate > :date")
	 List<Indicator> findByLastSyncDate(@Param("date") Date lastSyncDate);
	
	//get the mappings added later--not present in txn sncu data
	@Override
	@Query("SELECT ind, iftm FROM IndicatorFacilityTimeperiodMapping iftm, Indicator ind "
			+ "WHERE iftm.indicator.indicatorId = ind.indicatorId "
			+ "AND iftm.indFacilityTpId NOT IN (SELECT sncu.indicatorFacilityTimeperiodMapping.indFacilityTpId FROM TXNSNCUData sncu  "
			+ "WHERE sncu.indicatorFacilityTimeperiodMapping.indFacilityTpId IN "
			+ "(SELECT if.indFacilityTpId FROM IndicatorFacilityTimeperiodMapping if WHERE if.facility.areaId = :facilityId "
			+ "AND if.timePeriod.timePeriodId = :timeperiodId ) "
			+ "AND sncu.isLive = TRUE) AND iftm.facility.areaId = :facilityId AND iftm.timePeriod.timePeriodId = :timeperiodId ORDER BY ind.indicatorOrder")
	 List<Object[]> fetchIndicatorsNameNotInTxnSncu(@Param("facilityId") Integer facilityId, @Param("timeperiodId") Integer timeperiodId);
	
	/** 
	 * @author Sarita Panigrahi, created on: 05-Aug-2017
	 * @param facilityId
	 * @param timeperiodId
	 * @return
	 * when lr is not present in the facility
	 */
	@Override
	@Query("SELECT ind FROM Indicator ind "
			+ "WHERE ind.indicatorType.typeDetailId = :indType "
			+ "AND ind.isLr IS NULL "
			+ "AND ind.indicatorId NOT IN "
			+ "(SELECT iftm.indicator.indicatorId FROM IndicatorFacilityTimeperiodMapping iftm, UserAreaMapping uam "
			+ "WHERE iftm.facility.areaId = uam.facility.areaId AND uam.facility.areaId = :facilityId "
			+ "AND iftm.timePeriod.timePeriodId = :timePeriodId) ORDER BY ind.indicatorOrder")
	 List<Indicator> fetchIndicatorsNameByFacilityIdAndTimePeriodIdAndIsLrIsNull(@Param("facilityId")Integer facilityId, 
			 @Param("timePeriodId") Integer timeperiodId, @Param("indType") Integer indType);
}
