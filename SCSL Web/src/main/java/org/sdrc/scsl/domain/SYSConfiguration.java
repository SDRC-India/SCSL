/**
 * 
 */
package org.sdrc.scsl.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * Used to create unique key of PDSA number
 *
 */
@Entity
@Table(name="SYS_Configuration ")
public class SYSConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4592697910163393457L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Sys_ID")
	private short sysId;
	
	@Column(name="last_update_date")
	private Timestamp lastUpdateDate;
	
	@Column(name = "Max_PDSA_Number")
	private String maxPDSANumber;
	
	//from v2.0.1 to restrict old apk sync
	@Column(name="api_version", length=10)
	private String apiVersion;
	
	public short getSysId() {
		return sysId;
	}

	public void setSysId(short sysId) {
		this.sysId = sysId;
	}

	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getMaxPDSANumber() {
		return maxPDSANumber;
	}

	public void setMaxPDSANumber(String maxPDSANumber) {
		this.maxPDSANumber = maxPDSANumber;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	
}
