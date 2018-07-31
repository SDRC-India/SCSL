package org.sdrc.scsl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sdrc.scsl.domain.Feature;
import org.sdrc.scsl.domain.FeaturePermissionMapping;
import org.sdrc.scsl.domain.MSTUser;
import org.sdrc.scsl.domain.Permission;
import org.sdrc.scsl.domain.Role;
import org.sdrc.scsl.domain.RoleFeaturePermissionScheme;
import org.sdrc.scsl.domain.UserAreaMapping;
import org.sdrc.scsl.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.scsl.model.web.FeatureModel;
import org.sdrc.scsl.model.web.FeaturePermissionMappingModel;
import org.sdrc.scsl.model.web.PermissionModel;
import org.sdrc.scsl.model.web.RoleFeaturePermissionSchemeModel;
import org.sdrc.scsl.model.web.RoleModel;
import org.sdrc.scsl.model.web.UserAreaModel;
import org.sdrc.scsl.model.web.UserModel;
import org.sdrc.scsl.model.web.UserRoleFeaturePermissionMappingModel;
import org.sdrc.scsl.model.web.ValueObject;
import org.springframework.stereotype.Component;

@Component
public class DomainToModelConverter {
	
	public static List<ValueObject> toUserRoleFeaturePermissionMappingValueObjs (
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings ){
		List<ValueObject> valueObjects = new ArrayList<>();
		for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappings) {
			ValueObject valueObject = new ValueObject(Integer.toString(userRoleFeaturePermissionMapping.getUserRoleFeaturePermissionId()),
					userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme().getSchemeName());
			valueObjects.add(valueObject);
		}
		return valueObjects;
	}
	
	public static List<UserModel> toUserDetailModels(List<UserAreaMapping> userDetails){
		List<UserModel> userDetailModels= new ArrayList<>();
		for (UserAreaMapping userDetail : userDetails) {
			UserModel userDetailModel = new UserModel();
			userDetailModel.setEmailId(userDetail.getUser().getEmail());
			userDetailModel.setLive(userDetail.getUser().getIsLive());
			userDetailModel.setPassword(userDetail.getUser().getPassword());
			userDetailModel.setUserId(userDetail.getUser().getUserId());
			userDetailModel.setUsername(userDetail.getUser().getUserName());
			userDetailModel.setName(userDetail.getUser().getName());
			userDetailModels.add(userDetailModel);
		}
		return userDetailModels;
	}
	
	public static List<FeaturePermissionMappingModel> toFeaturePermissionMappings(List<FeaturePermissionMapping> featurePermissionMappings){
		List<FeaturePermissionMappingModel> featurePermissionMappingModels = new ArrayList<>();
		for (FeaturePermissionMapping featurePermissionMapping : featurePermissionMappings) {
			FeaturePermissionMappingModel featurePermissionMappingModel = new FeaturePermissionMappingModel();
			featurePermissionMappingModel.setFeaturePermissionId(featurePermissionMapping.getFeaturePermissionId());
			featurePermissionMappingModel.setUpdatedBy(featurePermissionMapping.getUpdatedBy());
			featurePermissionMappingModel.setPermission(toPermissionModel(featurePermissionMapping.getPermission()));
			featurePermissionMappingModel.setFeature(toFeatureModels(Arrays.asList(featurePermissionMapping.getFeature())).get(0));
			featurePermissionMappingModel.setRoleFeaturePermissionSchemeModels(
					toRoleFeaturePermissionSchemeModelValueObjs(featurePermissionMapping.getRoleFeaturePermissionSchemes()));
			featurePermissionMappingModels.add(featurePermissionMappingModel);
		}
		return featurePermissionMappingModels;
	}
	
	public static List<ValueObject> toRoleFeaturePermissionSchemeModelValueObjs(List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes){
		List<ValueObject> valueObjects = new ArrayList<>();
		for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : roleFeaturePermissionSchemes) {
			ValueObject valueObject = new ValueObject(roleFeaturePermissionScheme.getRoleFeaturePermissionSchemeId(),
					roleFeaturePermissionScheme.getSchemeName());
			valueObjects.add(valueObject);
		}
		return valueObjects;
	}
	
	public static List<FeatureModel> toFeatureModels(List<Feature> features){
		List<FeatureModel> featureModels =new ArrayList<>();
		for (Feature feature : features) {
			FeatureModel featureModel = new FeatureModel();
			featureModel.setFeatureId(feature.getFeatureId());
			featureModel.setFeatureCode(feature.getFeatureCode());
			featureModel.setDescription(feature.getDescription());
			featureModel.setFeatureName(feature.getFeatureName());
			featureModel.setFeaturePermissionMappings(toFeaturePermissionMappingModelValueObjs(feature.getFeaturePermissionMappings()));
			featureModel.setUpdatedBy(feature.getUpdatedBy());
//			featureModel.setUpdatedDate(feature.getUpdatedDate());
			featureModels.add(featureModel);
		}
		return featureModels;
	}
	
	public static List<ValueObject> toFeaturePermissionMappingModelValueObjs(List<FeaturePermissionMapping> featurePermissionMappings){
		List<ValueObject> valueObjects = new ArrayList<>();
		for (FeaturePermissionMapping featurePermissionMapping : featurePermissionMappings) {
			ValueObject valueObject = new ValueObject(Integer.toString(featurePermissionMapping.getFeaturePermissionId()),
					featurePermissionMapping.getPermission().getPermissionName());
			valueObjects.add(valueObject);
		}
		return valueObjects;
	}
	
	public static PermissionModel toPermissionModel(Permission permission){
		PermissionModel permissionModel = new PermissionModel();
		permissionModel.setPermissionId(permission.getPermissionId());
		permissionModel.setPermissionCode(permission.getPermissionCode());
		permissionModel.setDescription(permission.getDescription());
		permissionModel.setUpdatedBy(permission.getUpdatedBy());
		permissionModel.setPermissionName(permission.getPermissionName());
		permissionModel.setFeaturePermissionMappings(toFeaturePermissionMappingModelValueObjs(permission.getFeaturePermissionMappings()));
		
		return permissionModel;
	}
	
	public static RoleModel toRoleModel(Role role){
		RoleModel roleModel = new RoleModel();
		roleModel.setDescription(role.getDescription());
		roleModel.setRoleCode(role.getRoleCode());
		roleModel.setRoleFeaturePermissionSchemes(toRoleFeaturePermissionSchemeModelValueObjs(role.getRoleFeaturePermissionSchemes()));
		roleModel.setRoleId(role.getRoleId());
		roleModel.setRoleName(role.getRoleName());
		roleModel.setUpdatedBy(role.getUpdatedBy());
		return roleModel;
	}
	
	
	public static List<RoleFeaturePermissionSchemeModel> toRoleFeaturePermissionSchemeModels(
			List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes){
		List<RoleFeaturePermissionSchemeModel> roleFeaturePermissionSchemeModels = new ArrayList<>();
		for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : roleFeaturePermissionSchemes) {
			RoleFeaturePermissionSchemeModel roleFeaturePermissionSchemeModel = new RoleFeaturePermissionSchemeModel();
			roleFeaturePermissionSchemeModel.setFeaturePermissionMapping(
					toFeaturePermissionMappings(Arrays.asList(roleFeaturePermissionScheme.getFeaturePermissionMapping())).get(0));
			roleFeaturePermissionSchemeModel.setRoleFeaturePermissionSchemeId(roleFeaturePermissionScheme.getRoleFeaturePermissionSchemeId());
			roleFeaturePermissionSchemeModel.setSchemeName(roleFeaturePermissionScheme.getSchemeName());
			roleFeaturePermissionSchemeModel.setUpdatedBy(roleFeaturePermissionScheme.getUpdatedBy());
			roleFeaturePermissionSchemeModel.setRole(toRoleModel(roleFeaturePermissionScheme.getRole()));
			roleFeaturePermissionSchemeModel.setUserRoleFeaturePermissionMappings(toUserRoleFeaturePermissionMappingModelValueObjs(
					roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings()));
			roleFeaturePermissionSchemeModels.add(roleFeaturePermissionSchemeModel);
		}
		return roleFeaturePermissionSchemeModels;
	}
	
	public static List<ValueObject> toUserRoleFeaturePermissionMappingModelValueObjs(
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings){
		List<ValueObject> valueObjects = new ArrayList<>();
		for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappings) {
			ValueObject valueObject = new ValueObject(Integer.toString(userRoleFeaturePermissionMapping.getUserRoleFeaturePermissionId()),
					userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme().getRole().getRoleName());
			valueObjects.add(valueObject);
		}
		return valueObjects;
	}
	
	public static UserRoleFeaturePermissionMappingModel toUserRoleFeaturePermissionMappingModel(
			UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping){
		UserRoleFeaturePermissionMappingModel userRoleFeaturePermissionMappingModel = new UserRoleFeaturePermissionMappingModel();
		userRoleFeaturePermissionMappingModel.setRoleFeaturePermissionSchemeModel(toRoleFeaturePermissionSchemeModels(
				Arrays.asList(userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme())).get(0));
		userRoleFeaturePermissionMappingModel.setUpdatedBy(userRoleFeaturePermissionMapping.getUpdatedBy());
		userRoleFeaturePermissionMappingModel.setUserDetailModel(toUserDetailModelValueObjs(
				userRoleFeaturePermissionMapping.getUserAreaMapping().getUser()));
		userRoleFeaturePermissionMappingModel.setUserRoleFeaturePermissionId(userRoleFeaturePermissionMapping.getUserRoleFeaturePermissionId());
		userRoleFeaturePermissionMappingModel.setAreaId(userRoleFeaturePermissionMapping.getUserAreaMapping().getFacility().getAreaId());
		return userRoleFeaturePermissionMappingModel;
	}

	public static List<UserRoleFeaturePermissionMappingModel> toUserRoleFeaturePermissionMappingModels(
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
		List<UserRoleFeaturePermissionMappingModel> userRoleFeaturePermissionMappingModels = new ArrayList<>();
		for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappings) {
			UserRoleFeaturePermissionMappingModel userRoleFeaturePermissionMappingModel = toUserRoleFeaturePermissionMappingModel(
					userRoleFeaturePermissionMapping);
			userRoleFeaturePermissionMappingModels.add(userRoleFeaturePermissionMappingModel);
		}
		return userRoleFeaturePermissionMappingModels;
	}
	
	public static List<UserAreaModel> toUserAreaMappingModel(List<UserAreaMapping> userAreaMappings) {
		List<UserAreaModel> userAreaModels = new ArrayList<>();
		for (UserAreaMapping userAreaMapping : userAreaMappings) {
			UserAreaModel userAreaModel = new UserAreaModel();
			userAreaModel.setAreaModel(new ValueObject(userAreaMapping.getFacility().getAreaId(),
					userAreaMapping.getFacility().getAreaName(), userAreaMapping.getFacility().getLevel()));
			userAreaModel.setUserId(userAreaMapping.getUser().getUserId());
			userAreaModel.setUserAreaMappingId(userAreaMapping.getUserAreaMappingId());
			userAreaModel.setUserRoleFeaturePermissionMappings(
					toUserRoleFeaturePermissionMappingModels(userAreaMapping.getUserRoleFeaturePermissionMappings()));
			userAreaModels.add(userAreaModel);
		}

		return userAreaModels;
	}

	public static ValueObject toUserDetailModelValueObjs(MSTUser userDetail){
		ValueObject valueObject = new ValueObject(Integer.toString(userDetail.getUserId()),
				userDetail.getUserName());
	return valueObject;
}

			
}
