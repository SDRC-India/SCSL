package org.sdrc.scsl.repository;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.TypeDetail;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:21:14 am
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 */
public interface TypeDetailRepository {

	List<TypeDetail> findAll();

	/**
	 * 
	 * @param statusCodeIds
	 * @return
	 */
	List<TypeDetail> findByTypeDetailIdIsIn(List<Integer> statusCodeIds);

	List<TypeDetail> findByTypeTypeId(int typeId);
	
	/**
	 * This method is going to return list of TypeDetail whose created date or updated date is greater than last sync date
	 * 
	 * @param lastSyncDate The last sync date that came from mobile device
	 * @return List of TypeDetail whose created date or updated date is greater than last sync date
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 28-Apr-2017 1:25:31 pm
	 */
	List<TypeDetail> findByLastSyncDate(Date lastSyncDate);
	
	/**
	 * 
	 * 
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 23-May-2017 10:40:49 pm
	 */
	TypeDetail findByTypeDetailId(Integer id);

	/** 
	 * @author Sarita Panigrahi, created on: 09-Aug-2017
	 * @param typeIds
	 * @return
	 */
	List<TypeDetail> findByTypeTypeIdInOrderByTypeTypeIdAsc(List<Integer> typeIds);
}
