/**
 * 
 */
package org.sdrc.scsl.repository;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.scsl.domain.TXNPlanning;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface TXNPlanningRepository {


	/**
	 * This method will return the all the planning between two dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<TXNPlanning> findByPlanDateBetweenAndIsLiveTrueOrderByFacilityAreaNameAsc(
			Timestamp startDate, Timestamp endDate);

	/**
	 * This method will return the all the planning between two dates of given facility list
	 * @param startDate
	 * @param endDate
	 * @param ahiFacilityArea
	 * @return
	 */
	List<TXNPlanning> findByPlanDateBetweenAndIsLiveTrueAndFacilityAreaIdIsInOrderByFacilityAreaNameAsc(
			Timestamp startDate, Timestamp endDate,
			List<Integer> ahiFacilityArea);

/*	*//**
	 * This method will return all the pending planning of the logged in user
	 * @param userId
	 * @return
	 *//*
	List<TXNPlanning> findByIsVisitedFalseAndIsLiveTrueAndMstUserUserId(Integer userId);*/

	/**
	 * This method will return all the pending planning of the logged in user after a given date
	 * @param userId
	 * @return
	 */
/*	List<TXNPlanning> findByIsVisitedFalseAndIsLiveTrueAndMstUserUserIdAndPlanDateLessThanEqualTo(
			Integer userId, Timestamp timestamp);*/

	/**
	 * This method will return planning data for a given planningID
	 * @param planningId
	 * @return
	 */
	TXNPlanning findByPlanningId(int planningId);

	@Transactional
	TXNPlanning save(TXNPlanning txnPlanning);

	/**
	 * 
	 * @param planningId
	 * @return
	 */
	TXNPlanning findByPlanningIdAndIsLiveTrue(int planningId);

	/**
	 * 
	 * @param ahiFacilityArea
	 * @return
	 */
	List<TXNPlanning> findByIsLiveTrueAndFacilityAreaIdIsInOrderByFacilityAreaNameAsc(
			List<Integer> ahiFacilityArea);

	/**
	 * 
	 * @return
	 */
	List<TXNPlanning> findByIsLiveTrue();

	/**
	 * 
	 * @param ahiFacilityArea
	 * @param nextDay
	 * @return
	 */
	List<TXNPlanning> findByIsLiveTrueAndFacilityAreaIdIsInAndPlanDateLessThanOrderByFacilityAreaNameAsc(
			List<Integer> ahiFacilityArea, Timestamp nextDay);

	/**
	 * 
	 * @param nextDay
	 * @return
	 */
	List<TXNPlanning> findByIsLiveTrueAndPlanDateLessThan(Timestamp nextDay);
	
    public List<TXNPlanning> findByCountryId(Timestamp startDate,Timestamp endDate);
	
	public List<TXNPlanning> findByStateId(Timestamp startDate,Timestamp endDate,Integer userId);
	
	public List<TXNPlanning> findByDistrictId(Timestamp startDate,Timestamp endDate,Integer userId);
	
	public List<TXNPlanning> findByFacilityId(Timestamp startDate,Timestamp endDate,Integer userId);

}
