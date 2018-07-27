package org.sdrc.scsl.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017
* This entity class holds the master user list
*
*/
@Entity
@Table(name = "mst_user")
public class MSTUser implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private Integer userId;
	
	@Column(length=100)
	private String name;
	
	@Column(name = "user_name", unique=true)
	private String userName;
	
	private String password;
	
	private String email;
	
	@Column(name = "is_live")
	private Boolean isLive;
	
	@Column(name="created_date")
	private Timestamp createdDate;
	
	private Integer lead;
	
//	******** bi-directional one-to-many association to UserAreaMapping *******
	
	@OneToMany(mappedBy="user")
	private List<UserAreaMapping> userAreaMappings;
	
//	******** bi-directional one-to-many association to TXNPlanning *******
	@OneToMany(mappedBy="mstUser")
	private List<TXNPlanning> txnPlanning;
	
	//GETTER SETTER **********************
	
	public Integer getUserId() {
		return userId;
	}

	public List<UserAreaMapping> getUserAreaMappings() {
		return userAreaMappings;
	}

	public void setUserAreaMappings(List<UserAreaMapping> userAreaMappings) {
		this.userAreaMappings = userAreaMappings;
	}

	public String getName() {
		return name;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public Boolean getIsLive() {
		return isLive;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
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

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIsLive(Boolean isLive) {
		this.isLive = isLive;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setLead(Integer lead) {
		this.lead = lead;
	}

	public MSTUser(Integer userId) {
		super();
		this.userId = userId;
	}

	public MSTUser() {
		super();
	}

	public List<TXNPlanning> getTxnPlanning() {
		return txnPlanning;
	}

	public void setTxnPlanning(List<TXNPlanning> txnPlanning) {
		this.txnPlanning = txnPlanning;
	}
	
	
}
