/**
 * 
 */
package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.TXNPDSA;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in) At 27-04-2017 12:31
 * @since 1.0.0
 * @author Sarita Panigrahi (sarita@sdrc.co.in)
 *
 */
public interface TXNPDSARepository {

	@Transactional
	<S extends TXNPDSA> List<S> save(Iterable<S> txnPDSA);

	List<TXNPDSA> findByPdsaFacilityAreaIdAndPercentageIsNotNullOrderByDueDateDesc(
			int facilityId);

	List<TXNPDSA> findByPdsaPdsaIdOrderByDueDateAsc(int pdsaId);

	List<TXNPDSA> findByPdsaPdsaIdAndPercentageIsNull(int pdsaId);
	
	@Transactional
	void deleteByPdsaPdsaIdAndPercentageIsNull(int pdsaId);

	List<Object []> findCountByPercentageIsNotNullAndPdsaFacilityAreaIdGroupByPdsaPdsaId(
			int facilityId);

	/** 
	 * @author Sarita Panigrahi, created on: 05-Oct-2017
	 * @param pdsaId
	 * @param pageable
	 * @return 
	 * get average P/ median txn of a pdsa limit to 12 (for last 12 transaction)
	 */
	Object findAveragePByFacilityAreaIdGroupByPdsaPdsaId(Integer pdsaId);

	/** 
	 * @author Sarita Panigrahi, created on: 05-Oct-2017
	 * @param pdsaId
	 * @param pageable
	 * @return
	 * get fractional index of a pdsa limit to 12 (for last 12 transaction)
	 */
//	List<Object[]> findFractionalIndexByFacilityAreaIdGroupByPdsaPdsaId(Integer pdsaId, Pageable pageable);
	
	/** 
	 * @author Sarita Panigrahi, created on: 05-Oct-2017
	 * @param pdsaId
	 * @param pageable
	 * @return
	 */
	List<TXNPDSA> findByPdsaPdsaIdOrderByTxnPDSAIdAsc(Integer pdsaId, Pageable pageable);
//	List<TXNPDSA> findByPercentageIsNotNullAndPdsaPdsaIdOrderByTxnPDSAIdAsc(Integer pdsaId, Pageable pageable);
}
