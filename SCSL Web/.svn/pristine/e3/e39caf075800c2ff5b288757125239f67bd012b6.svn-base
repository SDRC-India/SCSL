package org.sdrc.scsl.repository;

import java.util.Date;
import java.util.List;

import org.sdrc.scsl.domain.Type;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:17:44 am
 */
public interface TypeRepository {

	List<Type> findAll();

	/**
	 * This method is going to return list of Type whose created date or updated date is greater than last sync date
	 * 
	 * @param lastSyncDate The last sync date that came from mobile device
	 * @return List of Type whose created date or updated date is greater than last sync date
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 28-Apr-2017 1:25:31 pm
	 */
	List<Type> findByLastSyncDate(Date lastSyncDate);

}
