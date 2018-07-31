/**
 * 
 */
package org.sdrc.scsl.repository.springdatajpa;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.scsl.domain.TXNPlanning;
import org.sdrc.scsl.repository.TXNPlanningRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@RepositoryDefinition(domainClass=TXNPlanning.class,idClass=Integer.class)
public interface SpringDataTXNPlanningRepository extends TXNPlanningRepository {
	
	

	@Override
	@Query("SELECT txnp FROM TXNPlanning txnp,Area ar WHERE ar.areaId = txnp.facility.areaId "
			+ "AND ar.level=4 And txnp.planDate BETWEEN :startDate AND :endDate AND "
			+ "txnp.isLive='true' order by txnp.planningId")
	List<TXNPlanning> findByCountryId(@Param("startDate")Timestamp startDate,@Param("endDate")Timestamp endDate);
	
	@Override
	@Query("SELECT txnp FROM TXNPlanning txnp,Area ar "
			+ "WHERE ar.areaId = txnp.facility.areaId AND ar.areaId in (select ari.areaId FROM Area ari "
			+ "where ari.parentAreaId in (SELECT arr.areaId from Area arr where arr.parentAreaId in "
			+ "(SELECT uam.facility.areaId FROM UserAreaMapping uam WHERE uam.user.userId=:userId))) "
			+ "And txnp.planDate BETWEEN :startDate AND :endDate AND "
			+ "txnp.isLive='true' order by txnp.planningId")
	 List<TXNPlanning> findByStateId(@Param("startDate")Timestamp startDate,@Param("endDate") Timestamp endDate,@Param("userId") Integer userId);
	
	
	@Override
	@Query("SELECT txnp FROM TXNPlanning txnp,Area ar "
			+ "WHERE ar.areaId = txnp.facility.areaId AND ar.areaId in (SELECT arr.areaId from Area arr where arr.parentAreaId in "
			+ "(SELECT uam.facility.areaId FROM UserAreaMapping uam WHERE uam.user.userId=:userId)) "
			+ "And txnp.planDate BETWEEN :startDate AND :endDate AND "
			+ "txnp.isLive='true' order by txnp.planningId")
	List<TXNPlanning> findByDistrictId(@Param("startDate")Timestamp startDate, @Param("endDate")Timestamp endDate, @Param("userId")Integer userId);
	
	
	@Override
	@Query("SELECT txnp FROM TXNPlanning txnp,Area ar "
			+ "WHERE ar.areaId = txnp.facility.areaId AND ar.areaId in "
			+ "(SELECT uam.facility.areaId FROM UserAreaMapping uam WHERE uam.user.userId=:userId) "
			+ "And txnp.planDate BETWEEN :startDate AND :endDate AND "
			+ "txnp.isLive='true' order by txnp.planningId")
	 List<TXNPlanning> findByFacilityId(@Param("startDate")Timestamp startDate, @Param("endDate")Timestamp endDate,@Param("userId")Integer userId);
		
	
	

}
