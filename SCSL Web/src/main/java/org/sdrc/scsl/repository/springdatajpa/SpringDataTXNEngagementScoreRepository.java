/**
 * 
 */
package org.sdrc.scsl.repository.springdatajpa;

import java.util.List;

import org.sdrc.scsl.domain.TXNEngagementScore;
import org.sdrc.scsl.repository.TXNEngagementScoreRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @author Sarita Panigrahi (sarita@sdrc.co.in)
 */

@RepositoryDefinition(domainClass=TXNEngagementScore.class,idClass=Integer.class)
public interface SpringDataTXNEngagementScoreRepository extends
		TXNEngagementScoreRepository {
	
	@Override
	@Query("select t from TXNEngagementScore t where t.facility.areaId = :areaId and t.timePeriod.timePeriodId = :timePeriodId")
	TXNEngagementScore findByAreaIdTimePeriod(@Param("areaId")int areaId, @Param("timePeriodId")
			int timePeriodId);
	
	@Override
	@Query("SELECT area.areaId FROM "
			+ " TXNEngagementScore txn, Area area "
			+ " WHERE area.areaId = txn.facility.areaId  "
			+ " AND txn.timePeriod.timePeriodId=(SELECT MAX(time.timePeriodId) FROM  TimePeriod time) "
			+ " AND area.level= 4 "
			+ " GROUP BY area.areaId")
	List<Integer> findFacilityWithCurrentMonthSubmission();
	
	@Override
	@Query(value="SELECT * FROM ( SELECT *, row_number() OVER (PARTITION BY facility_id_fk "
			+ "ORDER BY timeperiod_id_fk DESC) RN FROM txn_engagement_score) v WHERE RN = 1", nativeQuery=true)
	List<TXNEngagementScore> findLatestFacilityData();
}
