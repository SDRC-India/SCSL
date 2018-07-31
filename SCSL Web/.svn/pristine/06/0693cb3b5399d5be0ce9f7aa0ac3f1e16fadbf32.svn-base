package org.sdrc.scsl.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import org.sdrc.scsl.domain.MSTEngagementScore;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:24:35 am
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 */
public interface MSTEngagementScoreRepository {

	List<MSTEngagementScore> findAll();

	/**
	 * This method will return the Object of MSTEngagementScore for a given engagementScoreId
	 * @param engagementScoreId
	 * @return
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Transactional
	MSTEngagementScore findByMstEngagementScoreId(int engagementScoreId);

	/**
	 * This method is going to return list of MSTEngagementScore whose created date or updated date is greater than last sync date
	 * 
	 * @param lastSyncDate The last sync date that came from mobile device
	 * @return List of MSTEngagementScore whose created date or updated date is greater than last sync date
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 28-Apr-2017 1:25:31 pm
	 */
	List<MSTEngagementScore> findByLastSyncDate(Date lastSyncDate);

}
