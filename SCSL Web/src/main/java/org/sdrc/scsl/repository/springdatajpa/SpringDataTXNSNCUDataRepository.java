package org.sdrc.scsl.repository.springdatajpa;

import java.util.List;

import org.sdrc.scsl.domain.TXNSNCUData;
import org.sdrc.scsl.repository.TXNSNCUDataRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sarita Panigrahi
 * @author Subrat
 */
@RepositoryDefinition(domainClass = TXNSNCUData.class, idClass = Integer.class)
public interface SpringDataTXNSNCUDataRepository extends TXNSNCUDataRepository {

	@Override
	@Query("select t from TXNSNCUData t where t.isLive = true and t.indicatorFacilityTimeperiodMapping.timePeriod.timePeriodId = :timePeriodId")
	public List<TXNSNCUData> findByTimePeriodId(@Param("timePeriodId") int timePeriodId);

	@Override
	@Query("select t from TXNSNCUData t where t.isLive = true and t.indicatorFacilityTimeperiodMapping.timePeriod.timePeriodId = :timePeriodId and "
			+ "t.indicatorFacilityTimeperiodMapping.facility.areaId = :areaId")
	public List<TXNSNCUData> findByTimePeriodIdAndAreaId(@Param("timePeriodId") int timePeriodId,
			@Param("areaId") int areaId);

	// AGGREGATE for each facility for time periods more than a month//facility
	// level aggregation //CAPITAL FLOAT Will not work, gives exception
	// CastFunction.render
	// with null check of numerator and denominator
	/*
	 * (non-Javadoc)
	 * 
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findFacilityAggregatedDataByIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List)
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "iftm.facility.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm "
			+ "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) " + "AND sncu.numeratorValue IS NOT NULL "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) AND sncu.isLive = TRUE "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.facility.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile")
	List<Object[]> findFacilityAggregatedDataByIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// AGGREGATE for district for different time periods //district level
	// aggregation //CAPITAL FLOAT Will not work, gives exception
	// CastFunction.render
	// with null check of numerator and denominator
	/*
	 * (non-Javadoc)
	 * 
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findDistrictAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List)
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND iftm.facility.areaId = ar.areaId "
			+ "AND parent.level = 3 AND sncu.isLive = TRUE " + "AND sncu.txnSubmissionManagement.txnSubmissionId IN "
			+ "(:submissionIds) " + "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId "
			+ "ORDER BY parent.areaId, iftm.indicator.indicatorId")
	List<Object[]> findDistrictAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// AGGREGATE for state for different time periods //state level
	// aggregation //CAPITAL FLOAT Will not work, gives exception
	// CastFunction.render
	// with null check of numerator and denominator
	/*
	 * (non-Javadoc)
	 * 
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findStateAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List)
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId")
	List<Object[]> findStateAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// AGGREGATE for Country for different time periods //india level
	// aggregation //CAPITAL FLOAT Will not work, gives exception
	// CastFunction.render
	// with null check of numerator and denominator
	/*
	 * (non-Javadoc)
	 * 
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findCountryAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(java
	 * .util.List)
	 */

	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE " + "AND CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ " AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "iftm.indicator.indicatorId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm "
			+ "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) AND sncu.isLive = TRUE "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile")
	List<Object[]> findCountryAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// AGGREGATE for a particular district for different time periods //district
	// level
	// aggregation //CAPITAL FLOAT Will not work, gives exception
	// CastFunction.render
	// with null check of numerator and denominator
	/*
	 * (non-Javadoc)
	 * 
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findDistrictAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List)
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND parent.areaId = :areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND parent.level = 3 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId")
	List<Object[]> findDistrictAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);

	// AGGREGATE for a particular state for different time periods //state level
	// aggregation //CAPITAL FLOAT Will not work, gives exception
	// CastFunction.render
	// with null check of numerator and denominator
	// (non-Javadoc)
	// * @author Sarita Panigrahi(sarita@sdrc.co.in)
	// * @see
	// org.sdrc.scsl.repository.TXNSNCUDataRepository#findStateAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List)

	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND iftm.facility.areaId = ar.areaId "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND state.areaId = :areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN (:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId")
	List<Object[]> findStateAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);

	// AGGREGATE for a particular facility for time periods more than a
	// month//facility
	// level aggregation //CAPITAL FLOAT Will not work, gives exception
	// CastFunction.render
	// with null check of numerator and denominator
	/*
	 * (non-Javadoc)
	 * 
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndTimePeriodTimePeriodIdInForFacilityAggregate
	 * (java.util.List)
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "iftm.facility.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm "
			+ "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN "
			+ "(:submissionIds ) AND sncu.txnSubmissionManagement.facility.areaId = :areaId "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) AND sncu.isLive = TRUE "
			+ "AND iftm.facility.areaId = :areaId "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.facility.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile")
	List<Object[]> findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndTimePeriodTimePeriodIdInForFacilityAggregate(
			@Param("areaId") Integer areaId, @Param("timePeriodIds") List<Integer> timePeriodIds,
			@Param("submissionIds") List<Integer> submissionIds);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * updateByTXNSubmissionManagementTxnSubmissionId(java.lang.Integer)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Transactional
	@Modifying
	@Query("UPDATE TXNSNCUData sncu SET sncu.isLive = FALSE WHERE sncu.txnSubmissionManagement.txnSubmissionId = :txnSubmissionId")
	void updateByTXNSubmissionManagementTxnSubmissionId(@Param("txnSubmissionId") Integer txnSubmissionId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findByIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdAndIsLiveTrueAndTxnSubmissionManagementStatusMneTypeDetailId
	 * (java.lang.Integer, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT sncu FROM TXNSNCUData sncu WHERE sncu.txnSubmissionManagement.txnSubmissionId IN "
			+ "(:submissionIds) " + "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId = :timePeriodId "
			+ "AND sncu.indicatorFacilityTimeperiodMapping.timePeriod.timePeriodId = :timePeriodId "
			+ "AND sncu.isLive = TRUE")
	List<TXNSNCUData> findByIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdAndIsLiveTrueAndTxnSubmissionManagementStatusMneTypeDetailId(
			@Param("timePeriodId") Integer timePeriodId, @Param("submissionIds") List<Integer> submissionIds);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodId
	 * (java.lang.Integer, java.lang.Integer, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT sncu FROM TXNSNCUData sncu WHERE sncu.txnSubmissionManagement.txnSubmissionId IN "
			+ "(:submissionIds) AND sncu.txnSubmissionManagement.facility.areaId = :facilityId "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId = :timePeriodId "
			+ "AND sncu.indicatorFacilityTimeperiodMapping.timePeriod.timePeriodId = :timePeriodId "
			+ "AND sncu.indicatorFacilityTimeperiodMapping.facility.areaId = :facilityId " + "AND sncu.isLive = TRUE")
	List<TXNSNCUData> findByIndicatorFacilityTimeperiodMappingFacilityAreaIdAndIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodId(
			@Param("facilityId") Integer facilityId, @Param("timePeriodId") Integer timePeriodId,
			@Param("submissionIds") List<Integer> submissionIds);

	// country level facility type/ size wise aggregation
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findCountryTypeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
	 * java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm "
			+ "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) AND sncu.isLive = TRUE "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.indicatorId, iftm.indicator.isProfile, iftm.facility.facilityType.typeDetailId ")
	List<Object[]> findCountryTypeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// country level facility type/ size wise aggregation
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findCountrySizeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
	 * java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm "
			+ "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) AND sncu.isLive = TRUE "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.indicatorId, iftm.indicator.isProfile, iftm.facility.facilitySize.typeDetailId")
	List<Object[]> findCountrySizeAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// state wise ---- facility type and size aggregation
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findStateTypeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, iftm.facility.facilityType.typeDetailId ")
	List<Object[]> findStateTypeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findStateSizeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, iftm.facility.facilitySize.typeDetailId ")
	List<Object[]> findStateSizeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// district wise ---- facility type and size aggregation
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findDistrictTypeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND iftm.facility.areaId = ar.areaId "
			+ "AND parent.level = 3 AND sncu.isLive = TRUE " + "AND sncu.txnSubmissionManagement.txnSubmissionId IN "
			+ "(:submissionIds) " + "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, iftm.facility.facilityType.typeDetailId")
	List<Object[]> findDistrictTypeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findDistrictSizeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND iftm.facility.areaId = ar.areaId "
			+ "AND parent.level = 3 AND sncu.isLive = TRUE " + "AND sncu.txnSubmissionManagement.txnSubmissionId IN "
			+ "(:submissionIds) " + "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, iftm.facility.facilitySize.typeDetailId")
	List<Object[]> findDistrictSizeAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// aggregate post historical data upload
	// aggregate by district wise facility type and size
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findDistrictTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.lang.Integer, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND parent.areaId = :areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND parent.level = 3 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, iftm.facility.facilityType.typeDetailId")
	List<Object[]> findDistrictTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findDistrictSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.lang.Integer, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND parent.areaId = :areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND parent.level = 3 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, iftm.facility.facilitySize.typeDetailId")
	List<Object[]> findDistrictSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);

	// aggregate by state wise facility type and size
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findStateTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.lang.Integer, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND iftm.facility.areaId = ar.areaId "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND state.areaId = :areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, iftm.facility.facilityType.typeDetailId")
	List<Object[]> findStateTypeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findStateSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.lang.Integer, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND iftm.facility.areaId = ar.areaId "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND state.areaId = :areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, iftm.facility.facilitySize.typeDetailId")
	List<Object[]> findStateSizeAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);

	/****************************************************************
	 * WAVELY AGGREGATION STARTED
	 ****************************************************************/

	// district wise wavely aggregation

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findDistrictWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND iftm.facility.areaId = ar.areaId "
			+ "AND parent.level = 3 AND sncu.isLive = TRUE " + "AND sncu.txnSubmissionManagement.txnSubmissionId IN "
			+ "(:submissionIds) " + "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, ar.wave "
			+ "ORDER BY parent.areaId, iftm.indicator.indicatorId ")
	List<Object[]> findDistrictWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// district---facility Type-- wise ---- WAVELY aggregation

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findDistrictTypeWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND iftm.facility.areaId = ar.areaId "
			+ "AND parent.level = 3 AND sncu.isLive = TRUE " + "AND sncu.txnSubmissionManagement.txnSubmissionId IN "
			+ "(:submissionIds) " + "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, iftm.facility.facilityType.typeDetailId, ar.wave")
	List<Object[]> findDistrictTypeWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	//// district---facility size-- wise ---- WAVELY aggregation
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findDistrictSizeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND iftm.facility.areaId = ar.areaId "
			+ "AND parent.level = 3 AND sncu.isLive = TRUE " + "AND sncu.txnSubmissionManagement.txnSubmissionId IN "
			+ "(:submissionIds) " + "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, iftm.facility.facilitySize.typeDetailId, ar.wave")
	List<Object[]> findDistrictSizeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// state wise wavely aggregation
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findStateWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, ar.wave")
	List<Object[]> findStateWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// state wise ---- facility type wise---wavely
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findStateTypeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, iftm.facility.facilityType.typeDetailId, ar.wave ")
	List<Object[]> findStateTypeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// state wise ---- facility size wise----wavely-- aggregation

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#
	 * findStateSizeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn
	 * (java.util.List, java.util.List)
	 * 
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, iftm.facility.facilitySize.typeDetailId, ar.wave ")
	List<Object[]> findStateSizeWiseWavelyAggregatedDataByAreaparentAreaIdTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);

	// country wise--wavely aggregation
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#findCountryWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List, java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE " + "AND CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "iftm.indicator.indicatorId, iftm.indicator.isProfile, iftm.facility.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm "
			+ "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) AND sncu.isLive = TRUE "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, iftm.facility.wave")
	List<Object[]> findCountryWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);
	
	// country wise--wavely--wavely aggregation
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#findCountryTypeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List, java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile, iftm.facility.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm "
			+ "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) AND sncu.isLive = TRUE "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.indicatorId, iftm.indicator.isProfile, iftm.facility.facilityType.typeDetailId, iftm.facility.wave ")
	List<Object[]> findCountryTypeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);
	
	//country size---wavely
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#findCountrySizeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List, java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile, iftm.facility.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm "
			+ "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) AND sncu.isLive = TRUE "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.indicatorId, iftm.indicator.isProfile, iftm.facility.facilitySize.typeDetailId, iftm.facility.wave")
	List<Object[]> findCountrySizeWiseWavelyAggregatedDataByTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("submissionIds") List<Integer> submissionIds);
	
	
	/********************************************HISTORICAL DATA WAVELY AGGREGATION******************************************/
	//district -- wavely
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#findDistrictWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List, java.lang.Integer, java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND parent.areaId = :areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND parent.level = 3 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, ar.wave")
	List<Object[]> findDistrictWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);
	
	// aggregate by district wise facility type-----WAVELY
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#findDistrictTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List, java.lang.Integer, java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND parent.areaId = :areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND parent.level = 3 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, iftm.facility.facilityType.typeDetailId, ar.wave")
	List<Object[]> findDistrictTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);
	
	// aggregate by district wise facility size-----WAVELY
	
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#findDistrictSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List, java.lang.Integer, java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "parent.areaId, iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND ar.parentAreaId = parent.areaId " + "AND parent.areaId = :areaId "
			+ "AND iftm.facility.areaId = ar.areaId " + "AND parent.level = 3 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, parent.areaId, iftm.facility.facilitySize.typeDetailId, ar.wave")
	List<Object[]> findDistrictSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);
	
	//state wise --- wavely
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#findStateWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List, java.lang.Integer, java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND iftm.facility.areaId = ar.areaId "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND state.areaId = :areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN (:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, ar.wave")
	List<Object[]> findStateWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);
	
	// WAVELY aggregate by state wise facility type
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#findStateTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List, java.lang.Integer, java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.facility.facilityType.typeDetailId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND iftm.facility.areaId = ar.areaId "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND state.areaId = :areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, iftm.facility.facilityType.typeDetailId, ar.wave")
	List<Object[]> findStateTypeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);
	
	// WAVELY aggregate by state wise facility size
	/* (non-Javadoc)
	 * @see org.sdrc.scsl.repository.TXNSNCUDataRepository#findStateSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(java.util.List, java.lang.Integer, java.util.List)
	 * @author Sarita Panigrahi
	 */
	@Override
	@Query("SELECT SUM(sncu.numeratorValue), SUM(sncu.denominatorValue), "
			+ "CASE WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 THEN "
			+ "((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN iftm.indicator.isProfile IS NULL AND " + "CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 "
			+ "WHEN iftm.indicator.isProfile = TRUE AND " + "CAST(SUM(sncu.denominatorValue) AS float) <> 0 "
			+ "THEN ((CAST(SUM(sncu.numeratorValue) AS float) / CAST(SUM(sncu.denominatorValue) AS float)) * 100 ) "
			+ "WHEN  iftm.indicator.isProfile = TRUE "
			+ "AND CAST(SUM(sncu.denominatorValue) AS float) = 0 THEN 0 END, "
			+ "state.areaId, iftm.indicator.indicatorId, iftm.facility.facilitySize.typeDetailId, iftm.indicator.isProfile, ar.wave "
			+ "FROM TXNSNCUData sncu INNER JOIN sncu.indicatorFacilityTimeperiodMapping iftm, "
			+ "Area ar, Area parent, Area state " + "WHERE iftm.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND iftm.facility.areaId = ar.areaId "
			+ "AND ar.parentAreaId = parent.areaId AND parent.parentAreaId = state.areaId "
			+ "AND state.areaId = :areaId " + "AND state.level = 2 AND sncu.isLive = TRUE "
			+ "AND sncu.txnSubmissionManagement.txnSubmissionId IN " + "(:submissionIds) "
			+ "AND sncu.txnSubmissionManagement.timePeriod.timePeriodId IN (:timePeriodIds) "
			+ "AND sncu.percentage IS NOT NULL "
			+ "GROUP BY iftm.indicator.indicatorId, iftm.indicator.isProfile, state.areaId, iftm.facility.facilitySize.typeDetailId, ar.wave")
	List<Object[]> findStateSizeWiseWavelyAggregatedDataByAreaparentAreaIdAndAreaIdIndicatorFacilityTimeperiodMappingTimePeriodTimePeriodIdIn(
			@Param("timePeriodIds") List<Integer> timePeriodIds, @Param("areaId") Integer areaId,
			@Param("submissionIds") List<Integer> submissionIds);
}