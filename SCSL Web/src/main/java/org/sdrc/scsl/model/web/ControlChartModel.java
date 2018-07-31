package org.sdrc.scsl.model.web;

/** 
* @author Sarita Panigrahi, created on: 11-Oct-2017
* This model class holds control chart keys
*/
public class ControlChartModel {
	
	private Double fractionalIndex;
	private Double standardDeviation;
	private Double ucl;
	private Double lcl;
	
	public Double getFractionalIndex() {
		return fractionalIndex;
	}
	public void setFractionalIndex(Double fractionalIndex) {
		this.fractionalIndex = fractionalIndex;
	}
	public Double getStandardDeviation() {
		return standardDeviation;
	}
	public void setStandardDeviation(Double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
	public Double getUcl() {
		return ucl;
	}
	public void setUcl(Double ucl) {
		this.ucl = ucl;
	}
	public Double getLcl() {
		return lcl;
	}
	public void setLcl(Double lcl) {
		this.lcl = lcl;
	}

	
}
