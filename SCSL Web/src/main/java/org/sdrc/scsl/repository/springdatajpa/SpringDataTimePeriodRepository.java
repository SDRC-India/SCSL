package org.sdrc.scsl.repository.springdatajpa;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.TimePeriod;
import org.sdrc.scsl.repository.TimePeriodRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:40:51 am
 * @author Sarita Panigrahi (sarita@sdrc.co.in)
 */
@RepositoryDefinition(domainClass=TimePeriod.class,idClass=Integer.class)
public interface SpringDataTimePeriodRepository extends TimePeriodRepository {

	@Override
	@Query("SELECT t FROM TimePeriod t WHERE t.createdDate > :date ")
	List<TimePeriod> findByLastSyncDate(@Param("date") Date lastSyncDate);

	//periodicity added
	@Override
	@Query("select t from TimePeriod t where t.startDate = (SELECT MAX(t.startDate) FROM t WHERE t.startDate NOT IN ( SELECT Max(t.startDate) FROM t)) "
			+ "AND t.periodicity = '1'")
	TimePeriod lastMonthTimeperiod();

	
	@Override
	@Query("SELECT t FROM TimePeriod t WHERE t.periodicity = :periodicity "
			+ "AND t.timePeriodId < (SELECT Max(t.timePeriodId) FROM t WHERE t.periodicity = :periodicity) "
			+ "ORDER BY t.timePeriodId DESC ")
	List<TimePeriod> findTopByPeriodicityOrderByTimePeriodIdDesc(@Param ("periodicity") String periodicity, Pageable pageable);
	
	@Override
	@Query("SELECT t FROM TimePeriod t WHERE t.periodicity = :periodicity "
			+ "AND t.timePeriodId < (SELECT Max(t.timePeriodId) "
			+ "FROM t WHERE t.periodicity = :periodicity AND t.timePeriodId < (SELECT Max(t.timePeriodId) FROM t WHERE t.periodicity = :periodicity) ) "
			+ "ORDER BY t.timePeriodId DESC ")
	List<TimePeriod> findTopByPeriodicityNotInMaxOrderByTimePeriodIdDesc(@Param ("periodicity") String periodicity, Pageable pageable);
	
	@Override
	@Query("SELECT t FROM TimePeriod t WHERE t.periodicity = :periodicity "
			+ "ORDER BY t.timePeriodId DESC ")
	List<TimePeriod> findTopNthByPeriodicityOrderByTimePeriodIdDesc(@Param ("periodicity") String periodicity, Pageable pageable);
	
	@Override
	@Query("SELECT t FROM TimePeriod t WHERE t.periodicity = :periodicity "
			+ "AND t.timePeriodId = (SELECT Max(t.timePeriodId) FROM t WHERE t.periodicity = :periodicity) ")
	TimePeriod findMaxTimePeriodIdByPeriodicity(@Param ("periodicity") String periodicity);
	
	@Override
	@Query("SELECT t.timePeriodId FROM TimePeriod t WHERE t.timePeriodId BETWEEN :startDate AND :endDate AND t.periodicity = :periodicity ")
	List<Integer> findByTimePeriodIdBetweenAndPeriodicity(@Param("startDate") Integer startDate, @Param("endDate") Integer endDate, @Param ("periodicity") String periodicity);
}
