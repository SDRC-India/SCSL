package org.sdrc.scsl.repository;

import org.sdrc.scsl.domain.HistoricalDataFileDetail;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sarita
 * {@link CreatedDate : 22-03-2018} 
 */
public interface HistoricalDataFileDetailRepository {

	@Transactional
	void save(HistoricalDataFileDetail historicalDataFileDetail);

}
