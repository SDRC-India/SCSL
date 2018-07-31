package org.sdrc.scsl.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import org.sdrc.scsl.domain.TimePeriod;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:31:14 am
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 */
public interface TimePeriodRepository {

	List<TimePeriod> findAll();

	/**
	 * 
	 * @param timeperiodId
	 * @return
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 */
	TimePeriod findByTimePeriodId(Integer timeperiodId);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param timePeriodIds
	 * @return last two time periods except viewing timeperiod
	 */
	List<TimePeriod> findTop2ByTimePeriodIdLessThanOrderByTimePeriodIdDesc(
			Integer timePeriodId);

	List<TimePeriod> findTop2ByTimePeriodIdLessThanAndPeriodicityOrderByTimePeriodIdDesc(
			Integer timePeriodId, String periodicity);

	List<TimePeriod> findTop2ByPeriodicityOrderByTimePeriodIdDesc(String periodicity);

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 */
	TimePeriod findByStartDateAndPeriodicity(Date startDate,
			String preodictiy);

	TimePeriod findTop1ByPeriodicityOrderByTimePeriodIdDesc(String periodicity);

	@Transactional
	TimePeriod save(TimePeriod timePeriod);
	
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param date
	 * @param date1
	 * @param periodicity
	 * @return
	 */
	TimePeriod findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndPeriodicity(Date date,Date date1,String periodicity);
	
	/**
	 * This method is going to return list of TimePeriod whose created date or updated date is greater than last sync date
	 * 
	 * @param lastSyncDate The last sync date that came from mobile device
	 * @return List of TimePeriod whose created date or updated date is greater than last sync date
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 28-Apr-2017 1:25:31 pm
	 */
	List<TimePeriod> findByLastSyncDate(Date lastSyncDate);

	/**
	 *  @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	List<TimePeriod> findByPeriodicityOrderByStartDateAsc(String periodicity);
	
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @return
	 */
	List<TimePeriod> findTopByPeriodicityOrderByTimePeriodIdDesc(String periodicity, Pageable pageable);
	
	TimePeriod findByStartDateAndEndDate(Date startDate ,Date endDate);

	TimePeriod lastMonthTimeperiod();
	
	TimePeriod findTop1ByPeriodicityAndIndicatorFacilityTimeperiodMappingsIndFacilityTpIdIsNotNullOrderByStartDateDesc(
			String string);
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param timePeriodIds
	 * @return
	 */
	List<TimePeriod> findByTimePeriodIdInOrderByTimePeriodIdAsc(List<Integer> timePeriodIds);
	
	List<TimePeriod> findByTimePeriodIdBetweenAndPeriodicityOrderByTimePeriodIdAsc(Integer startDate, Integer endDate,String periodicity);

	/**
	 *  @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	List<TimePeriod> findByOrderByStartDateDesc();
	
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param timePeriodId
	 * @param periodicity
	 * @return all time periods comes under the quarter
	 */
	List<TimePeriod> findTop3ByTimePeriodIdLessThanAndPeriodicityOrderByTimePeriodIdDesc(
			Integer timePeriodId, String periodicity);
	
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param timePeriodId
	 * @param periodicity
	 * @return all time periods comes under the year
	 */
	List<TimePeriod> findTop12ByTimePeriodIdLessThanAndPeriodicityOrderByTimePeriodIdDesc(
			Integer timePeriodId, String periodicity);

	List<TimePeriod> findTopByPeriodicityNotInMaxOrderByTimePeriodIdDesc(String periodicity, Pageable pageable);

	List<TimePeriod> findTopNthByPeriodicityOrderByTimePeriodIdDesc(String periodicity, Pageable pageable);
	
	List<TimePeriod> findAllByPeriodicityOrderByTimePeriodIdDesc(String periodicity);
	
	/** 
	 * @author Sarita Panigrahi, created on: 27-Sep-2017
	 * @param periodicity
	 * @return
	 */
	TimePeriod findMaxTimePeriodIdByPeriodicity(String periodicity);

	List<Integer> findByTimePeriodIdBetweenAndPeriodicity(Integer startDate, Integer endDate, String periodicity);
	
	/** 
	 * @author Sarita Panigrahi, created on: 10-Oct-2017
	 * @param timePeriod
	 * @return
	 */
	TimePeriod findByTimePeriod(String timePeriod);
	
	/** 
	 * @author Sarita Panigrahi, created on: 04-Dec-2017
	 * @param timePeriodId
	 * @param periodicity
	 * @param pageable
	 * @return
	 */
	List<TimePeriod> findTopByTimePeriodIdLessThanAndPeriodicityOrderByTimePeriodIdDesc(Integer timePeriodId, String periodicity, Pageable pageable);

	List<TimePeriod> findAllByPeriodicityOrderByTimePeriodIdAsc(String string);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Transactional
	//find timeperiods greater than the given tpid
	List<TimePeriod> findByTimePeriodIdGreaterThanEqualAndPeriodicityAndTimePeriodIdLessThanOrderByTimePeriodIdDesc(Integer timePeriodId, String periodicity, Integer latestTimePeriodId);
	
	List<TimePeriod> findByTimePeriodIdGreaterThanAndPeriodicityAndTimePeriodIdLessThanOrderByTimePeriodIdDesc(Integer timePeriodId, String periodicity, Integer latestTimePeriodId);
	
	List<TimePeriod> findByTimePeriodIdGreaterThanEqualAndPeriodicityOrderByTimePeriodIdAsc(Integer timePeriodId, String periodicity);
}
