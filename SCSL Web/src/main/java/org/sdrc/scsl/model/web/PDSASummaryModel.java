package org.sdrc.scsl.model.web;

import java.util.List;
import java.util.Map;

/**
 * @author Sarita Panigrahi, created on: 10-Oct-2017 Specialized class to get
 *         the json while downloading the excel in pdsa summary view
 */
public class PDSASummaryModel {

	private List<PDSAModel> pdsaList;
	private Map<String, String> facilityDetailMap;
	private Map<String, Object> chartData;
	private List<String> chartSvgs;
	private PDSAModel selectedPdsa;

	public List<PDSAModel> getPdsaList() {
		return pdsaList;
	}

	public void setPdsaList(List<PDSAModel> pdsaList) {
		this.pdsaList = pdsaList;
	}

	public Map<String, String> getFacilityDetailMap() {
		return facilityDetailMap;
	}

	public void setFacilityDetailMap(Map<String, String> facilityDetailMap) {
		this.facilityDetailMap = facilityDetailMap;
	}

	public Map<String, Object> getChartData() {
		return chartData;
	}

	public void setChartData(Map<String, Object> chartData) {
		this.chartData = chartData;
	}

	public List<String> getChartSvgs() {
		return chartSvgs;
	}

	public void setChartSvgs(List<String> chartSvgs) {
		this.chartSvgs = chartSvgs;
	}

	public PDSAModel getSelectedPdsa() {
		return selectedPdsa;
	}

	public void setSelectedPdsa(PDSAModel selectedPdsa) {
		this.selectedPdsa = selectedPdsa;
	}

	
}
