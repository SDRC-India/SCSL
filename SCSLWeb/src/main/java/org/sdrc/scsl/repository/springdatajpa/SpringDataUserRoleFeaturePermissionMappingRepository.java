package org.sdrc.scsl.repository.springdatajpa;

import org.sdrc.scsl.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.scsl.repository.UserRoleFeaturePermissionMappingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
*
*/
public interface SpringDataUserRoleFeaturePermissionMappingRepository
		extends UserRoleFeaturePermissionMappingRepository, JpaRepository<UserRoleFeaturePermissionMapping, Integer> {


}
