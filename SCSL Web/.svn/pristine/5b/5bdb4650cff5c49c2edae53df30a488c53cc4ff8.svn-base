package org.sdrc.scsl.repository;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.Area;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:09:06 am
 * @author Harsh Pratyush(harsh@sdrc.co.in) on 26-Apr-2017 12:25am
 * @author Sarita Panigrahi (sarita@sdrc.co.in) on 28-Apr-2017 11.025am
 */
public interface AreaRepository {

	List<Area> findAll();

	/**
	 * This method will return the Area object for a given areaId
	 * 
	 * @param areaId
	 * @return
	 */
	Area findByAreaId(int areaId);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param parentAreaId
	 * @return List<Area>
	 */
	List<Integer> findByParentAreaId(Integer parentAreaId);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param parentAreaId
	 * @return
	 */
	List<Integer> findByParentParentAreaId(Integer parentAreaId);

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	List<Area> findAllByOrderByLevelAsc();
	
	/**
	 * This method is going to return list of areas whose created date or updated date is greater than last sync date
	 * 
	 * @param lastSyncDate The last sync date that came from mobile device
	 * @return List of areas whose created date or updated date is greater than last sync date
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 28-Apr-2017 1:25:31 pm
	 */
	List<Area> findByLastSyncDate(Date lastSyncDate);

	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	List<Area> findAllByOrderByLevelAscAreaNameAsc();
	
	List<Area> findByLevel(Integer level);
	
	
	/**
	 * @author Debiprasad Parida(debiprasad@sdrc.co.in)
	 * @return
	 */
	List<Area> findAllByParentParentAreaId(Integer parentParentAreaId);
	
	List<Area> findAllByParentAreaId(Integer parentId);
	
	List<Area> findbyAreaId(Integer areaId);

	
	/**This method will return the area under a district
	 * @author Harsh Pratyush(hasrh@sdrc.co.in)
	 * @param parentAreaId
	 * @return
	 */
	List<Area> findAreaByParentAreaId(Integer parentAreaId);


	List<Integer> fetchAllData(List<Integer> wave, Integer level);

	List<Integer> fetchDataByDistrict(Integer districtId, List<Integer> wave, Integer level);

	List<Integer> fetchDataByState(Integer stateId, List<Integer> wave, Integer level);
	
	List<Integer> fetchDataByFacilityType(Integer facilityTypeId,Integer districtId, List<Integer> wave);
	
	List<Integer> fetchDataByFacilitySize(Integer facilitySizeId,Integer districtId, List<Integer> wave);

	List<Integer> fetchDataByStateAndFacilityType(Integer stateId, Integer facilityTypeId, List<Integer> wave, Integer level);

	List<Integer> fetchDataByStateAndFacilitySize(Integer stateId, Integer facilitySizeId, List<Integer> wave, Integer level);

	List<Integer> fetchAllDataByFacilityType(Integer facilityTypeId, List<Integer> wave, Integer level);

	List<Integer> fetchAllDataByFacilitySize(Integer facilitySizeId, List<Integer> wave, Integer level);

//	List<Integer> fetchDataByStateAndType(Integer stateId, int selectCriteria, List<Integer> facilityTypeSizeIds);
	/** 
	 * @author Sarita Panigrahi, created on: 25-Sep-2017
	 * @param level
	 * @param parentAreaId
	 * @return
	 */
	List<Area> findByHasLrTrueAndLevel(Integer level, Integer parentAreaId);
	
	/** 
	 * @author Sarita Panigrahi, created on: 25-Sep-2017
	 * @return
	 */
	List<Area> findAllByOrderByAreaIdAsc();
	
	/** 
	 * @author Sarita Panigrahi, created on: 06-Nov-2017
	 * @param areaIds
	 * @return
	 */
	List<Area> findByAreaIdIn(List<Integer> areaIds);
	
	/** 
	 * @author Sarita Panigrahi, created on: 07-Nov-2017
	 * @param areaIds
	 * @return
	 */
	List<Area> findByAreaIdInAndHasLrTrue(List<Integer> areaIds);
}
