package org.sdrc.scsl.repository.springdatajpa;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.IndicatorFacilityTimeperiodMapping;
import org.sdrc.scsl.repository.IndicatorFacilityTimeperiodMappingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:43:40 am
 */
@RepositoryDefinition(domainClass=IndicatorFacilityTimeperiodMapping.class,idClass=Integer.class)
public interface SpringDataIndicatorFacilityTimeperiodMappingRepository extends
		IndicatorFacilityTimeperiodMappingRepository {

	@Override
	@Query("SELECT i FROM IndicatorFacilityTimeperiodMapping i WHERE i.isLive = true and i.facility.areaId = :areaId and i.createdDate > :date or i.updatedDate > :date")
	public List<IndicatorFacilityTimeperiodMapping> findByLastSyncDateAndAreaId  (@Param("date") Date lastSyncDate, @Param("areaId") int areaId);
	
	
	@Override
	@Query("SELECT i FROM IndicatorFacilityTimeperiodMapping i WHERE i.isLive = true and i.facility.areaId = :areaId")
	public List<IndicatorFacilityTimeperiodMapping> findByAreaId(@Param("areaId") int areaId);
	
	@Override
	@Query("SELECT i FROM IndicatorFacilityTimeperiodMapping i WHERE i.isLive = true and i.facility.areaId = :areaId and i.timePeriod.timePeriodId = :timePeriodId")
	public List<IndicatorFacilityTimeperiodMapping> findByAreaIdAndTimePeriodIsLiveTrue(
			@Param("areaId") Integer areaId, @Param("timePeriodId") Integer timePeriodId);
	
}
