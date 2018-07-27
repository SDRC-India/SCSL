package org.sdrc.scsl.model.web;

/**
 * This model will contain the line chart data i.e. a timeperiod name and its
 * respective score
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public class LineChartDataModel {

	private String axis;

	private String value;

	public String getAxis() {
		return axis;
	}

	public LineChartDataModel(String axis, String value) {
		super();
		this.axis = axis;
		this.value = value;
	}

	public LineChartDataModel() {
		// TODO Auto-generated constructor stub
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
