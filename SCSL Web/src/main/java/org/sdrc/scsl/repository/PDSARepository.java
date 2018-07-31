package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.PDSA;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Mandakini Biswal
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface PDSARepository {
	@Transactional
	void save(PDSA pdsa);
	
	List<PDSA> findByIndicatorCoreAreaTypeDetailIdAndFacilityAreaId(Integer coreAreaId ,Integer facilityId);
	List<PDSA> findByIndicatorIndicatorIdAndFacilityAreaId(Integer indicatorId,Integer facilityId);
	//List<PDSA> findByChangeIdea(Integer changeIdeaId);
	List<PDSA> findByFacilityAreaId(Integer facilityId);
	List<PDSA> findByChangeIdeaChangeIdeaId(Integer changeIdeaId);
	
	/**
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @param facilityId
	 * @param statusListsId
	 * @return
	 */
	List<PDSA> findByFacilityAreaIdAndStatusTypeDetailIdIsIn(int facilityId,
			List<Integer> statusListsId);

	/**
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @param pdsaId
	 * @return
	 */
	PDSA findByPdsaId(int pdsaId);

	/**
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @param facilityId
	 * @return
	 */
	List<PDSA> findByFacilityAreaIdOrderByPdsaIdDesc(int facilityId);

	/**
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @param facilityId
	 * @return
	 */
	List<PDSA> findByFacilityAreaIdOrderByPdsaNumberDesc(int facilityId);

	/** 
	 * @author Sarita Panigrahi, created on: 28-Oct-2017
	 * @return
	 */
	List<Integer> findDistinctFacilityId();

}
