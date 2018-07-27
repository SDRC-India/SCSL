package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.MSTUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 6:35:04 pm
 */
public interface MSTUserRepository {

	/**
	 * This method will fetch record according to the following crieria
	 * Given username string should match with database record
	 * Given password string should match with database record
	 * The record should be live, is_live value should be 1/true
	 * @param username The username of the user
	 * @param password md5 representation of user string
	 * @since 1.0.0
	 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 7:12:27 pm
	 */
	MSTUser findByUsernameAndPassword(String username, String password);

	/**
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @return
	 */
	List<MSTUser> findByIsLiveTrueOrderByUserIdAsc();

	/**
	 * This method will return the the user list for a related role
	 * @author Harsh Pratyush (harsh@sdrc.co.in)
	 * @param roleID
	 * @return 
	 */
	List<MSTUser> findDistinctByUserAreaMappingsUserRoleFeaturePermissionMappingsRoleFeaturePermissionSchemeRoleRoleIdIn(
			List<Integer> roleIDs);
	
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param users
	 * @return
	 */
	@Transactional
	<S extends MSTUser> List<S> save(Iterable<S> users);
	
	MSTUser findByUserIdAndIsLiveTrue(Integer userId);

	/** 
	 * @author Sarita Panigrahi, created on: 23-Oct-2017
	 * @param userId
	 * @param password
	 */
	@Transactional
	void updateByUserId(Integer userId, String password);
	
	List<MSTUser> findDistinctByUserAreaMappingsUserRoleFeaturePermissionMappingsRoleFeaturePermissionSchemeRoleRoleIdInAndUserAreaMappingsFacilityAreaIdIn(
			List<Integer> roleIDs, List<Integer> facilityIds);
	
	/**
	 * @param userId
	 * @return
	 */
	MSTUser findByUserId(Integer userId);
	
	/**
	 * @return
	 */
	Integer findMaxUserId();
}
