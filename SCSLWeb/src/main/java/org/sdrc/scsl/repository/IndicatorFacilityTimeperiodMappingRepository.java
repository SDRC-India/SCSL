package org.sdrc.scsl.repository;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.IndicatorFacilityTimeperiodMapping;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:42:34 am
 */
public interface IndicatorFacilityTimeperiodMappingRepository {
	

//	List<IndicatorFacilityTimeperiodMapping> findAll();
	
	@Transactional
	<S extends IndicatorFacilityTimeperiodMapping> List<S> save(Iterable<S> entities);
	
	IndicatorFacilityTimeperiodMapping findByIndFacilityTpId(int id);
	

	/**
	 * This method is going to get all records according to the area id
	 * 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 09-May-2017 11:29:23 am
	 */
	List<IndicatorFacilityTimeperiodMapping> findByAreaId(int areaId);

	/**
	 * This method is going to return list of IndicatorFacilityTimeperiodMapping whose created date or updated date is greater than last sync date
	 * And Area id would be user's area id
	 * 
	 * @param lastSyncDate The last sync date that came from mobile device
	 * @return List of IndicatorFacilityTimeperiodMapping whose created date or updated date is greater than last sync date
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 28-Apr-2017 1:25:31 pm
	 */
	List<IndicatorFacilityTimeperiodMapping> findByLastSyncDateAndAreaId(
			Date lastSyncDate, int areaId);

	/**
	 * @author Abhisheka Mishra
	 * @param indicatorFacilityTimeperiodMapping
	 * @return
	 */
	@Transactional
	IndicatorFacilityTimeperiodMapping save(IndicatorFacilityTimeperiodMapping indicatorFacilityTimeperiodMapping);

	List<IndicatorFacilityTimeperiodMapping> findAll();

	/**
	 * This following method is going to get records whose area id is given area id,
	 * time period id is given time period id and is live should be 1
	 * 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 18-May-2017 5:20:34 pm
	 */
	List<IndicatorFacilityTimeperiodMapping> findByAreaIdAndTimePeriodIsLiveTrue(
			Integer areaId, Integer timePeriodId);
	
	List<IndicatorFacilityTimeperiodMapping> findByFacilityAreaIdAndTimePeriodTimePeriodIdInAndIsLiveTrue(Integer areaId, List<Integer> timePeriodIds);

	List<IndicatorFacilityTimeperiodMapping> findByTimePeriodTimePeriodIdAndIsLiveTrue(Integer previousTimeperiodId);
	
	List<IndicatorFacilityTimeperiodMapping> findByIndicatorIndicatorTypeTypeDetailIdAndFacilityAreaIdAndTimePeriodTimePeriodIdIn(Integer indType, Integer areaId, List<Integer> timePeriodIds);
	
	List<IndicatorFacilityTimeperiodMapping> findByIndicatorIndicatorIdAndTimePeriodTimePeriodIdInAndFacilityAreaIdIn(Integer indicatorId, 
			List<Integer> timePeriodIds,  List<Integer> facilityIds );
	
	List<IndicatorFacilityTimeperiodMapping> findByTimePeriodTimePeriodIdInAndIsLiveTrue( List<Integer> timePeriodIds);
	
	//get only process mappings
	List<IndicatorFacilityTimeperiodMapping> findByFacilityAreaIdAndTimePeriodTimePeriodIdInAndIsLiveTrueAndIndicatorIndicatorTypeTypeDetailId(
			Integer areaId, List<Integer> timePeriodIds, Integer typeDetailId);
}
