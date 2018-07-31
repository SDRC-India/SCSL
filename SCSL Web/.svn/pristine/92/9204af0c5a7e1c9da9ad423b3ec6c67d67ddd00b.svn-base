package org.sdrc.scsl.repository.springdatajpa;

import java.util.List;

import org.sdrc.scsl.domain.TXNSubmissionManagement;
import org.sdrc.scsl.repository.TXNSubmissionManagementRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
* @author Subrata(Subrata@sdrc.co.in)
*/
public interface SpringDataTXNSubmissionManagementRepository
		extends TXNSubmissionManagementRepository, JpaRepository<TXNSubmissionManagement, Integer> {
	
	@Override
	@Query("SELECT txnsm FROM TypeDetail td, TXNSubmissionManagement txnsm "
			+ "WHERE txnsm.isLatest = true "
			+ "AND txnsm.facility.areaId = :facilityId "
			+ "AND txnsm.timePeriod.timePeriodId = :timePeriod "
			+ "AND txnsm.statusSuperintendent.typeDetailId = td.typeDetailId")
	TXNSubmissionManagement findByStatus(@Param("facilityId")Integer facilityId,@Param("timePeriod")Integer timePeriod);
	
	//mne approved data
	@Override
	@Query("SELECT sub.refSubmissionId FROM TXNSubmissionManagement sub WHERE sub.statusMne.typeDetailId = :typeDetailId ")
	List<Integer> findTXNSubmissionIdByTypeDetailIdForMnE(@Param("typeDetailId")Integer typeDetailId);

	//legacy data upload
	@Override
	@Query("SELECT sub.txnSubmissionId FROM TXNSubmissionManagement sub WHERE sub.statusMne.typeDetailId = :typeDetailId ")
	List<Integer> findTXNSubmissionIdByTypeDetailIdForLegacy(@Param("typeDetailId")Integer typeDetailId);
}
