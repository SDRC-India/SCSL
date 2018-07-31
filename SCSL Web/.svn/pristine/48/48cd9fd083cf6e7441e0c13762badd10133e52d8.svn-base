package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.RoleFeaturePermissionScheme;

public interface RoleFeaturePermissionSchemeRepository {

	List<RoleFeaturePermissionScheme> findByRoleRoleIdAndFeaturePermissionMappingFeatureFeatureIdInAndFeaturePermissionMappingPermissionPermissionIdIn(
			int roleId, List<Integer> featureIds, List<Integer> permissionIds);

	RoleFeaturePermissionScheme findByRoleFeaturePermissionSchemeId(Integer roleFeaturePermissionSchemeId);
}
