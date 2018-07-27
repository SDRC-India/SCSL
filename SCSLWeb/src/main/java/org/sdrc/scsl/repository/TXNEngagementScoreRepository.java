/**
 * 
 */
package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.TXNEngagementScore;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface TXNEngagementScoreRepository {

	public List<TXNEngagementScore> findAll();

	@Transactional
	public TXNEngagementScore save(TXNEngagementScore txnEngagementScore);
	
	/**
	 * This method will return the latest 12 submission of given faciliity
	 * @param facilityId
	 * @return
	 */
	public List<TXNEngagementScore> findTop12ByFacilityAreaIdOrderByTimePeriodTimePeriodIdDesc(int facilityId);

	/**
	 * 
	 * 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 09-May-2017 6:18:21 pm
	 */
	public TXNEngagementScore findByAreaIdTimePeriod(int areaId,
			int timePeriodId);
	
	public List<Integer> findFacilityWithCurrentMonthSubmission();

	/** 
	 * @author Sarita Panigrahi, created on: 25-Sep-2017
	 * @return
	 * Select each row for a facility but only of the newest Timeperiod
	 */
	List<TXNEngagementScore> findLatestFacilityData();

}
