package org.sdrc.scsl.repository;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.scsl.domain.UserLoginMeta;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in)
 *
 */
public interface UserLoginMetaRepository {
	

	UserLoginMeta save(UserLoginMeta userLoginMeta);

	void updateStatus(Timestamp loggedOutDateTime, long userLogInMetaId);
	
	UserLoginMeta findByMstUserUserIdAndIsLoggedInTrue(Integer userId);

	void updateStatusForAll(Timestamp loggedOutDateTime);
	
	UserLoginMeta findByMstUserUserIdAndUserLogInMetaId(Integer userId, long userLogInMetaId);
	
	List<UserLoginMeta> findByIsLoggedInTrue();
}
