/**
 * 
 */
package org.sdrc.scsl.model.web;

import java.util.List;
import java.util.Map;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class SNCUIndicatorDataModel {

	private Map<String, Map<String, List<IndicatorModel>>> indTypeIndicatorModelMap;

	private boolean isEnabled;

	private String mneStatus;

	private String supritendentStatus;

	private String remark;
	
	private String cssClass;
	
	private String statusMessage;

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getSupritendentStatus() {
		return supritendentStatus;
	}

	public void setSupritendentStatus(String supritendentStatus) {
		this.supritendentStatus = supritendentStatus;
	}

	public Map<String, Map<String, List<IndicatorModel>>> getIndTypeIndicatorModelMap() {
		return indTypeIndicatorModelMap;
	}

	public void setIndTypeIndicatorModelMap(Map<String, Map<String, List<IndicatorModel>>> indTypeIndicatorModelMap) {
		this.indTypeIndicatorModelMap = indTypeIndicatorModelMap;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getMneStatus() {
		return mneStatus;
	}

	public void setMneStatus(String status) {
		this.mneStatus = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

}
