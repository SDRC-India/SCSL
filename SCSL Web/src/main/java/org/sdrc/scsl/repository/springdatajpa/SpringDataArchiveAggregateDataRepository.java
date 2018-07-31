package org.sdrc.scsl.repository.springdatajpa;

import org.sdrc.scsl.domain.ArchiveAggregateData;
import org.sdrc.scsl.repository.ArchiveAggregateDataRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataArchiveAggregateDataRepository extends ArchiveAggregateDataRepository, JpaRepository<ArchiveAggregateData, Integer> {
	/**
	* @author Sarita Panigrahi(sarita@sdrc.co.in)
	*
	*/

}
