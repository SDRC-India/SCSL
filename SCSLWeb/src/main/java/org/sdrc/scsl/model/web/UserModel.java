package org.sdrc.scsl.model.web;

import java.util.List;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 28-04-2017 Contains
 *         all the user specific info
 */
public class UserModel {

	private Integer userId;
	private String name;
	private String emailId;
	private String username;
	private String password;
	private boolean isLive;
	private Integer lead;
	private Integer facilityId;
	private String facilityName;
	private String districtName;
	private String stateName;
	private long userLoginMetaId;
	private int areaLevel;
	private String country;
	private String roleName;
	private Boolean hasLr;
	private List<UserAreaModel> userAreaModels;
	
	public String getRoleName() {
		return roleName;
	}

	public Boolean isHasLr() {
		return hasLr;
	}

	public void setHasLr(Boolean hasLr) {
		this.hasLr = hasLr;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public long getUserLoginMetaId() {
		return userLoginMetaId;
	}

	public void setUserLoginMetaId(long userLoginMetaId) {
		this.userLoginMetaId = userLoginMetaId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<UserAreaModel> getUserAreaModels() {
		return userAreaModels;
	}

	public void setUserAreaModels(List<UserAreaModel> userAreaModels) {
		this.userAreaModels = userAreaModels;
	}

	public Integer getFacilityId() {
		return facilityId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isLive() {
		return isLive;
	}

	public Integer getLead() {
		return lead;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public void setLead(Integer lead) {
		this.lead = lead;
	}

	public int getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(int areaLevel) {
		this.areaLevel = areaLevel;
	}

}
