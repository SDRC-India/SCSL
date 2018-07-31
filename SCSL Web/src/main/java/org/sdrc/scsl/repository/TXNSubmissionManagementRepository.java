package org.sdrc.scsl.repository;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.LockModeType;

import org.sdrc.scsl.domain.TXNSubmissionManagement;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 * @author Subrata(Subrata@sdrc.co.in)
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 */
public interface TXNSubmissionManagementRepository {

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param startDate
	 * @param endDate
	 * @param facilityId
	 * @return List<TXNSubmissionManagement>
	 */
	List<TXNSubmissionManagement> findByCreatedDateBetweenAndFacilityAreaId(
			Timestamp startDate, Timestamp endDate, Integer facilityId);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param facilityId
	 * @param timePeriodIds
	 * @return List<TXNSubmissionManagement>
	 */
	List<TXNSubmissionManagement> findByFacilityAreaIdAndTimePeriodTimePeriodIdInAndIsLatestTrue(
			Integer facilityId, List<Integer> timePeriodIds);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param txnSubmissionId
	 * @return TXNSubmissionManagement
	 */
	TXNSubmissionManagement findByTxnSubmissionId(Integer txnSubmissionId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Transactional
	TXNSubmissionManagement findByFacilityAreaIdAndTimePeriodTimePeriodIdAndIsLatestTrue(
			Integer facilityId, Integer timerPeriod);

	@Transactional
	TXNSubmissionManagement save(TXNSubmissionManagement txnSubmissionManagement);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param startDate
	 * @param endDate
	 * @param facilityId
	 * @return List<TXNSubmissionManagement>
	 */
	List<TXNSubmissionManagement> findByCreatedDateBetweenAndFacilityAreaIdIn(
			Timestamp startDate, Timestamp endDate, List<Integer> facilityId);

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param timePeriodId
	 * @param statusPendingSupritendent
	 * @return List<TXNSubmissionManagement> 
	 */
	List<TXNSubmissionManagement> findByIsLatestTrueAndTimePeriodTimePeriodIdAndStatusSuperintendentTypeDetailId(
			Integer timePeriodId, int statusPendingSupritendent);

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param txnSubmissionManagement
	 * @return List<TXNSubmissionManagement> 
	 */
	@Transactional
	public <S extends TXNSubmissionManagement> List<S> save(
			Iterable<S> txnSubmissionManagement);

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param timePeriodId
	 * @param statusMnE
	 * @return
	 */
	List<TXNSubmissionManagement> findByIsLatestTrueAndTimePeriodTimePeriodIdAndStatusMneTypeDetailId(
			Integer timePeriodId, int statusMnE);

	TXNSubmissionManagement findByStatus(Integer facilityId, Integer timePeriod);
	
	List<TXNSubmissionManagement> findByCreatedDateBetweenAndStatusSuperintendentTypeDetailIdIn(Timestamp startDate, Timestamp endDate, List<Integer> typeDetailId);

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param startDate
	 * @param endDate
	 * @param facilityId
	 * @return
	 */
	List<TXNSubmissionManagement> findByCreatedDateBetweenAndFacilityAreaIdOrderByCreatedDateDesc(
			Timestamp startDate, Timestamp endDate, Integer facilityId);

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param startDate
	 * @param endDate
	 * @param asList
	 * @return
	 */
	List<TXNSubmissionManagement> findByCreatedDateBetweenAndStatusSuperintendentTypeDetailIdInOrderByCreatedDateDesc(
			Timestamp startDate, Timestamp endDate, List<Integer> asList);

	

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param typeDetailId
	 * @return
	 */
	List<Integer> findTXNSubmissionIdByTypeDetailIdForMnE(Integer typeDetailId);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param typeDetailId
	 * @return
	 */
	List<Integer> findTXNSubmissionIdByTypeDetailIdForLegacy(Integer typeDetailId);
	/**
	 *  @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param refSubmissionId
	 * @return
	 */
	TXNSubmissionManagement findByIsLatestTrueAndRefSubmissionId(int refSubmissionId);
	
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param facilityId
	 * @param statusId
	 * @param timePeriodId
	 * @return
	 */
	TXNSubmissionManagement findByIsLatestTrueAndFacilityAreaIdAndStatusSuperintendentTypeDetailIdAndTimePeriodTimePeriodId(Integer facilityId, Integer statusId, Integer timePeriodId);

}
