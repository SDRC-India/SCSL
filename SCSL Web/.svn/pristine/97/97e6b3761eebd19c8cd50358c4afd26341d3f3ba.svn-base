package org.sdrc.scsl.repository.springdatajpa;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.Area;
import org.sdrc.scsl.repository.AreaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:10:15 am
 * @author Sarita Panigrahi (sarita@sdrc.co.in) on 28-Apr-2017 12:10:15 pm
 */
@RepositoryDefinition(domainClass = Area.class, idClass = Integer.class)
public interface SpringDataAreaRepository extends AreaRepository {

	@Override
	@Query("SELECT ar.areaId FROM Area ar WHERE ar.parentAreaId = :parentAreaId")
	List<Integer> findByParentAreaId(@RequestParam("parentAreaId") Integer parentAreaId);
	
	@Override
	@Query("SELECT ar.areaId FROM Area ar WHERE ar.parentAreaId IN (SELECT a.areaId FROM Area a WHERE a.parentAreaId = :parentAreaId)")
	List<Integer> findByParentParentAreaId(@RequestParam("parentAreaId") Integer parentAreaId);
	
	@Override
	@Query("SELECT ar FROM Area ar WHERE ar.createdDate > :date or ar.updatedDate > :date")
	List<Area> findByLastSyncDate(@Param("date") Date lastSyncDate);
	
	@Override
	@Query("select arr from Area arr where arr.parentAreaId in "
			+ "(select are.areaId from Area are where are.parentAreaId=:parentParentAreaId) "
			+ "or arr.areaId in (select areaId from Area a where a.parentAreaId=:parentParentAreaId) ORDER BY arr.areaId")
	List<Area> findAllByParentParentAreaId(@Param("parentParentAreaId")Integer parentParentAreaId);
	
	@Override
	@Query("select arr from Area arr where arr.parentAreaId in (select ar.areaId from Area ar where ar.areaId=:parentId) ORDER BY arr.areaId")
	 List<Area> findAllByParentAreaId(@Param("parentId")Integer parentId); 
	
	@Override
	@Query("select arr from Area arr where arr.areaId=:areaId")
	List<Area> findbyAreaId(@Param("areaId")Integer areaId);
	
	@Override
	@Query("SELECT ar FROM Area ar WHERE ar.parentAreaId = :parentAreaId")
	List<Area> findAreaByParentAreaId(@Param("parentAreaId") Integer parentAreaId);
	
	@Override
	@Query("SELECT ar.areaId FROM Area ar, Area parent "
			+ "WHERE ar.parentAreaId = parent.areaId "
			+ "AND parent.parentAreaId = :stateId "
			+ "AND ar.wave in (:wave)"
			+ "AND ar.level = :level ORDER BY  ar.areaId ")
	List<Integer> fetchDataByState(@Param("stateId") Integer stateId, @Param("wave") List<Integer> wave, @Param("level") Integer level);
	
	@Override
	@Query("SELECT ar.areaId FROM Area ar, Area parent "
			+ "WHERE ar.parentAreaId = parent.areaId "
			+ "AND parent.parentAreaId = :stateId "
			+ "AND ar.level = :level AND ar.facilityType.typeDetailId =:facilityTypeId "
			+ "AND ar.wave in (:wave) ORDER BY ar.areaId ")
	List<Integer> fetchDataByStateAndFacilityType(@Param("stateId") Integer stateId, 
			@Param("facilityTypeId")Integer facilityTypeId, @Param("wave") List<Integer> wave, @Param("level") Integer level);
	
	@Override
	@Query("SELECT ar.areaId FROM Area ar, Area parent "
			+ "WHERE ar.parentAreaId = parent.areaId "
			+ "AND parent.parentAreaId = :stateId "
			+ "AND ar.level = :level AND ar.facilitySize.typeDetailId =:facilitySizeId "
			+ "AND ar.wave in (:wave) ORDER BY ar.areaId ")
	List<Integer> fetchDataByStateAndFacilitySize(@Param("stateId") Integer stateId, 
			@Param("facilitySizeId")Integer facilitySizeId, @Param("wave") List<Integer> wave, @Param("level") Integer level);
	
	@Override
	@Query("SELECT ar.areaId FROM Area ar WHERE ar.parentAreaId = :districtId AND ar.level = :level "
			+ "AND ar.wave in (:wave) ORDER BY  ar.areaId")
	List<Integer> fetchDataByDistrict(@Param("districtId") Integer districtI, @Param("wave") List<Integer> waved, @Param("level") Integer level);
	
	@Override
	@Query("SELECT  ar.areaId FROM Area ar WHERE ar.level = :level "
			+ "AND ar.wave in (:wave) ORDER BY ar.areaId")
	List<Integer> fetchAllData(@Param("wave") List<Integer> wave, @Param("level") Integer level);
	
	@Override
	@Query("SELECT ar.areaId from Area ar where ar.facilityType.typeDetailId =:facilityTypeId AND ar.parentAreaId=:districtId "
			+ "AND ar.wave in (:wave) ORDER BY ar.areaId")
	List<Integer> fetchDataByFacilityType(@Param("facilityTypeId") Integer facilityTypeId,
			@Param("districtId") Integer districtId, @Param("wave") List<Integer> wave);

	@Override
	@Query("SELECT ar.areaId from Area ar where ar.facilitySize.typeDetailId =:facilitySizeId AND ar.parentAreaId=:districtId "
			+ "AND ar.wave in (:wave) ORDER BY  ar.areaId")
	List<Integer> fetchDataByFacilitySize(@Param("facilitySizeId") Integer facilitySizeId,
			@Param("districtId") Integer districtId, @Param("wave") List<Integer> wave);
	
	@Override
	@Query("SELECT ar.areaId FROM Area ar WHERE ar.level = :level AND ar.facilityType.typeDetailId =:facilityTypeId "
			+ "AND ar.wave in (:wave) ORDER BY ar.areaId")
	List<Integer> fetchAllDataByFacilityType(@Param("facilityTypeId")Integer facilityTypeId, @Param("wave") List<Integer> wave, @Param("level") Integer level);
	
	@Override
	@Query("SELECT ar.areaId FROM Area ar WHERE ar.level = :level AND ar.facilitySize.typeDetailId =:facilitySizeId "
			+ "AND ar.wave in (:wave) ORDER BY ar.areaId")
	List<Integer> fetchAllDataByFacilitySize(@Param("facilitySizeId")Integer facilitySizeId, @Param("wave") List<Integer> wave, @Param("level") Integer level);
	
	@Override
	@Query("SELECT ar FROM Area ar, Area parent WHERE ar.level = :level AND "
			+ "ar.parentAreaId = parent.id AND parent.parentAreaId = :parentAreaId")
	List<Area> findByHasLrTrueAndLevel(@Param("level")Integer level, @Param("parentAreaId")Integer parentAreaId);
}
