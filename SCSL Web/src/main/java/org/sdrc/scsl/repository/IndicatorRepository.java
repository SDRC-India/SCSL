package org.sdrc.scsl.repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.sdrc.scsl.domain.Indicator;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:15:10 am
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 */
public interface IndicatorRepository {

	List<Indicator> findAll();
	
	/**
	 * @Harsh Pratyush
	 * @return
	 */
	List<Indicator> findByCoreAreaTypeDetailIdIsNotNullAndIndicatorTypeTypeDetailId(int indicatortType);
	
	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @return
	 */
	List<Indicator> findByCoreAreaTypeDetailIdIsNotNullOrderByCoreAreaTypeDetailIdAscIndicatorTypeTypeDetailIdAsc();

	/**
	 * This method is going to return list of Indicator whose created date or updated date is greater than last sync date
	 * 
	 * @param lastSyncDate The last sync date that came from mobile device
	 * @return List of Indicator whose created date or updated date is greater than last sync date
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 28-Apr-2017 1:25:31 pm
	 */
	List<Indicator> findByLastSyncDate(Date lastSyncDate);

	List<Object[]> getIndicatorFacilityTimeperiodMappingByFacilityIdAndTimePeriodIdOrderByIndicatorOrder(Integer facilityId, Integer timeperiodId);

	List<Object[]> fetchIndicatorsName(Integer facilityId, Integer timeperiodId, Integer txnSubmissionId);

	/**
	 * 
	 * This meothod will get the indicator by it's id
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 08-May-2017 8:34:08 pm
	 */
	Indicator findByIndicatorId(int indicatorId);
	

	/**
	 * @author Abhisheka Mishra
	 * @return
	 */
	LinkedList<Indicator> findByOrderByIndicatorOrderAsc();
	/**
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @param indicatorTypeId
	 * @return
	 */
	List<Indicator> findByCoreAreaTypeDetailIdIsNotNullAndIndicatorTypeTypeDetailIdOrderByCoreAreaTypeDetailIdAscIndicatorTypeTypeDetailIdAsc(
			int indicatorTypeId);

	
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param indTypes
	 * @return
	 */
	List<Indicator> findByIndicatorTypeTypeDetailIdInAndIsLrIsNullOrderByIndicatorOrderAsc(List<Integer> indTypes);
	
	/** 
	 * @author Sarita Panigrahi, created on: 07-Aug-2017
	 * @param indTypes
	 * @return
	 */
	List<Indicator> findByIndicatorTypeTypeDetailIdInOrderByIndicatorOrderAsc(List<Integer> indTypes);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param facilityId
	 * @param timeperiodId
	 * @return
	 */
	List<Object[]> fetchIndicatorsNameNotInTxnSncu(Integer facilityId, Integer timeperiodId);

	/** 
	 * @author Sarita Panigrahi, created on: 07-Aug-2017
	 * @return
	 */
	List<Indicator> findByIsProfileTrueAndIsLrIsNull();
	
	/** 
	 * @author Sarita Panigrahi, created on: 07-Aug-2017
	 * @return
	 */
	List<Indicator> findByIsProfileTrueAndIsLrTrue();

	/** 
	 * @author Sarita Panigrahi, created on: 14-Oct-2017
	 * @param facilityId
	 * @param timeperiodId
	 * @param indType
	 * @return
	 */
	List<Indicator> fetchIndicatorsNameByFacilityIdAndTimePeriodId(Integer facilityId, Integer timeperiodId,
			Integer indType);

	/** 
	 * @author Sarita Panigrahi, created on: 14-Oct-2017
	 * @param facilityId
	 * @param timeperiodId
	 * @param indType
	 * @return
	 */
	List<Indicator> fetchIndicatorsNameByFacilityIdAndTimePeriodIdAndIsLrIsNull(Integer facilityId,
			Integer timeperiodId, Integer indType);
	
	/** 
	 * @author Sarita Panigrahi, created on: 10-Oct-2017
	 * @return
	 */
	List<Indicator> findByIsProfileIsNullOrderByIndicatorTypeTypeDetailIdAscIndicatorOrderAsc();
	
}
