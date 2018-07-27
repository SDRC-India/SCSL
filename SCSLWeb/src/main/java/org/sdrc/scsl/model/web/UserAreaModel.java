package org.sdrc.scsl.model.web;

import java.util.List;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
*
*/
public class UserAreaModel {

	private Integer userAreaMappingId;
	private Integer userId;
	private ValueObject areaModel;
	
	private List<UserRoleFeaturePermissionMappingModel> userRoleFeaturePermissionMappings;
	
	public Integer getUserAreaMappingId() {
		return userAreaMappingId;
	}
	public ValueObject getAreaModel() {
		return areaModel;
	}
	public void setUserAreaMappingId(Integer userAreaMappingId) {
		this.userAreaMappingId = userAreaMappingId;
	}

	public void setAreaModel(ValueObject areaModel) {
		this.areaModel = areaModel;
	}
	public List<UserRoleFeaturePermissionMappingModel> getUserRoleFeaturePermissionMappings() {
		return userRoleFeaturePermissionMappings;
	}
	public void setUserRoleFeaturePermissionMappings(
			List<UserRoleFeaturePermissionMappingModel> userRoleFeaturePermissionMappings) {
		this.userRoleFeaturePermissionMappings = userRoleFeaturePermissionMappings;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
