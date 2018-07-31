package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.ArchiveAggregateData;
import org.springframework.transaction.annotation.Transactional;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
*
*/

public interface ArchiveAggregateDataRepository {

	@Transactional
	<S extends ArchiveAggregateData> List<S> save(Iterable<S> entities);
}
