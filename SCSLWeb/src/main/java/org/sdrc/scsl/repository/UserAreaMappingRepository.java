package org.sdrc.scsl.repository;

import java.util.List;
import java.util.Set;

import org.sdrc.scsl.domain.UserAreaMapping;

/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 25-Apr-2017 10:29:15 am
 * 
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 */
public interface UserAreaMappingRepository {
		
/**
 * 
 * @param userId -Logged in userId
 * @return List of user area map for logged in user and and mapping is islive true
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 */

	List<UserAreaMapping> findByUserUserIdAndIsLiveTrue(int userId);

	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param areaId
	 * @return
	 */
	List<UserAreaMapping> findByFacilityAreaId(Integer areaId);
	
	/**
	 * @author Sarita Panigrahi(sarita@sdrc.co.in)
	 * @param areaId
	 * @param userId
	 * @return
	 */
	UserAreaMapping findByFacilityAreaIdAndUserUserIdLike(Integer areaId, Integer userId);

/**
 * This method will return the list of each user for each area and for a specific role
 * @param keySet
 * @param roleId
 * @return
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 * 
 */

	List<UserAreaMapping> findDistinctByFacilityAreaIdInAndUserRoleFeaturePermissionMappingsRoleFeaturePermissionSchemeRoleRoleId(
			Set<Integer> keySet, int roleId);
}
