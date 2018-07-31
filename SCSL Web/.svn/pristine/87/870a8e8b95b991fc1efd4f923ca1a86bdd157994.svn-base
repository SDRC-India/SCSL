package org.sdrc.scsl.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Sarita
 * Holds all the meta data of a logged in user
 *
 */
@Entity
@Table(name="user_login_meta")
public class UserLoginMeta implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="User_Log_In_Meta_Id")
	private long userLogInMetaId;
	
	@Column(name="Logged_In_DateTime")
	private Timestamp loggedInDateTime;
	
	@Column(name="Logged_Out_DateTime")
	private Timestamp loggedOutDateTime;
	
	@Column(name="User_Ip_Address")
	private String userIpAddress;
	
	@Column(name="user_agent")
	private String userAgent;
	
	@Column(name="is_logged_in")
	private boolean isLoggedIn;
	
	@ManyToOne
	@JoinColumn(name="user_id_fk")
	private MSTUser mstUser;
	
	public long getUserLogInMetaId() {
		return userLogInMetaId;
	}

	public Timestamp getLoggedInDateTime() {
		return loggedInDateTime;
	}

	public Timestamp getLoggedOutDateTime() {
		return loggedOutDateTime;
	}

	public String getUserIpAddress() {
		return userIpAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserLogInMetaId(long userLogInMetaId) {
		this.userLogInMetaId = userLogInMetaId;
	}

	public void setLoggedInDateTime(Timestamp loggedInDateTime) {
		this.loggedInDateTime = loggedInDateTime;
	}

	public void setLoggedOutDateTime(Timestamp loggedOutDateTime) {
		this.loggedOutDateTime = loggedOutDateTime;
	}

	public void setUserIpAddress(String userIpAddress) {
		this.userIpAddress = userIpAddress;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public UserLoginMeta(long userLogInMetaId) {
		super();
		this.userLogInMetaId = userLogInMetaId;
	}

	public UserLoginMeta() {
		super();
	}

	public MSTUser getMstUser() {
		return mstUser;
	}

	public void setMstUser(MSTUser mstUser) {
		this.mstUser = mstUser;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

}
