package org.sdrc.scsl.repository.springdatajpa;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.MSTEngagementScore;
import org.sdrc.scsl.repository.MSTEngagementScoreRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:25:17 am
 */
@RepositoryDefinition(domainClass=MSTEngagementScore.class,idClass=Integer.class)
public interface SpringDataMSTEngagementScoreRepository extends
		MSTEngagementScoreRepository {

	@Override
	@Query("SELECT m FROM MSTEngagementScore m WHERE m.createdDate > :date or m.updatedDate > :date")
	public List<MSTEngagementScore> findByLastSyncDate(@Param("date") Date lastSyncDate);
}
