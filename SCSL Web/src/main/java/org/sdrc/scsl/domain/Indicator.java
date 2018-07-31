package org.sdrc.scsl.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Sarita Panigrahi(sarita@sdrc.co.in) Created on 22-04-2017 this entity will hold all the
 *         indicator details and its relationships among themselves
 *         (process-intermediate-outcome)
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)        
 */
@Entity
@Table(name="indicator")
public class Indicator implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer indicatorId;
	
	@Column(name="indicator_name", length=1000)
	private String indicatorName;
	
	//profile indicators are also present in numerator for simplified aggregation
	@Column(length=1000)
	private String numerator;
	
	@Column(length=1000)
	private String denominator;
	
	@Column(name="is_required")
	private Boolean isReqired;
	
	//updated data type, integer to string and to a single column to hold multiple related indicators
	
	@Column(name="related_indicator_id")
	private String relatedIndicatorIds;
	
	@Column(name="exception_rule", length=200)
	private String exceptionRule;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "updated_date")
	private Timestamp updatedDate;
	
	//post UAT
	@Column(name="Indicator_Order")
	private Integer indicatorOrder;
	
	//post UAT
	@Column(name="is_LR")
	private Boolean isLr;
	
	//post UAT
	@Column(name="is_profile")
	private Boolean isProfile;
	
//	********  bi-directional many-to-one association to TypeDetail *******
	
//	profile and outcome indicator has no coreArea
	@ManyToOne
	@JoinColumn(name="core_area_fk")
	private TypeDetail coreArea;
	
//	profile indicator has no indicator type
	@ManyToOne
	@JoinColumn(name="indicator_type_fk")
	private TypeDetail indicatorType;
	
//	******** bi-directional one-to-many association to ChangeIdea *******
	
	@OneToMany(mappedBy="indicator")
	private List<ChangeIdea> changeIdeas;
	
//	******** bi-directional one-to-many association to PDSA *******
	
	@OneToMany(mappedBy="indicator")
	private List<PDSA> pdsas;
	
//	******** bi-directional one-to-many association to ArchivePDSA *******
	
//	@OneToMany(mappedBy="arcIndicator")
//	private List<ArchivePDSA> archivePDSAs;
	
//	******** bi-directional one-to-many association to IndicatorFacilityTimeperiodMapping *******
	
	@OneToMany(mappedBy="indicator")
	private List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings;

	
//	******** bi-directional one-to-many association to AggregateData *******
	
//	@OneToMany(mappedBy="indicator")
//	private List<AggregateData> aggregateDatas;
	
//	******** bi-directional one-to-many association to ArchiveAggregateData *******
	
//	@OneToMany(mappedBy="arcIndicator")
//	private List<ArchiveAggregateData> archiveAggregateDatas;
	
	
	public Indicator(Integer indicatorMapId) {
		this.indicatorId=indicatorMapId;
		
	}

	
	//Default constructor
	public Indicator(){
		
	}
	

	//GETTER SETTER **********************
	

	public Integer getIndicatorId() {
		return indicatorId;
	}

	public Integer getIndicatorOrder() {
		return indicatorOrder;
	}


	public void setIndicatorOrder(Integer indicatorOrder) {
		this.indicatorOrder = indicatorOrder;
	}


	public Boolean getIsLr() {
		return isLr;
	}


	public void setIsLr(Boolean isLr) {
		this.isLr = isLr;
	}


	public Boolean getIsProfile() {
		return isProfile;
	}


	public void setIsProfile(Boolean isProfile) {
		this.isProfile = isProfile;
	}


	public String getIndicatorName() {
		return indicatorName;
	}

	public String getNumerator() {
		return numerator;
	}

	public String getDenominator() {
		return denominator;
	}

	public Boolean getIsReqired() {
		return isReqired;
	}

	public String getExceptionRule() {
		return exceptionRule;
	}

	public TypeDetail getCoreArea() {
		return coreArea;
	}

	public TypeDetail getIndicatorType() {
		return indicatorType;
	}

	public List<ChangeIdea> getChangeIdeas() {
		return changeIdeas;
	}

	public List<PDSA> getPdsas() {
		return pdsas;
	}

//	public List<ArchivePDSA> getArchivePDSAs() {
//		return archivePDSAs;
//	}

	public List<IndicatorFacilityTimeperiodMapping> getIndicatorFacilityTimeperiodMappings() {
		return indicatorFacilityTimeperiodMappings;
	}

//	public List<AggregateData> getAggregateDatas() {
//		return aggregateDatas;
//	}
//
//	public List<ArchiveAggregateData> getArchiveAggregateDatas() {
//		return archiveAggregateDatas;
//	}

	public void setIndicatorId(Integer indicatorId) {
		this.indicatorId = indicatorId;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public void setNumerator(String numerator) {
		this.numerator = numerator;
	}

	public void setDenominator(String denominator) {
		this.denominator = denominator;
	}

	public void setIsReqired(Boolean isReqired) {
		this.isReqired = isReqired;
	}

	public void setExceptionRule(String exceptionRule) {
		this.exceptionRule = exceptionRule;
	}

	public void setCoreArea(TypeDetail coreArea) {
		this.coreArea = coreArea;
	}

	public void setIndicatorType(TypeDetail indicatorType) {
		this.indicatorType = indicatorType;
	}

	public void setChangeIdeas(List<ChangeIdea> changeIdeas) {
		this.changeIdeas = changeIdeas;
	}

	public void setPdsas(List<PDSA> pdsas) {
		this.pdsas = pdsas;
	}

//	public void setArchivePDSAs(List<ArchivePDSA> archivePDSAs) {
//		this.archivePDSAs = archivePDSAs;
//	}

	public void setIndicatorFacilityTimeperiodMappings(
			List<IndicatorFacilityTimeperiodMapping> indicatorFacilityTimeperiodMappings) {
		this.indicatorFacilityTimeperiodMappings = indicatorFacilityTimeperiodMappings;
	}

//	public void setAggregateDatas(List<AggregateData> aggregateDatas) {
//		this.aggregateDatas = aggregateDatas;
//	}
//
//	public void setArchiveAggregateDatas(List<ArchiveAggregateData> archiveAggregateDatas) {
//		this.archiveAggregateDatas = archiveAggregateDatas;
//	}


	public Timestamp getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}


	public Timestamp getUpdatedDate() {
		return updatedDate;
	}


	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}


	public String getRelatedIndicatorIds() {
		return relatedIndicatorIds;
	}


	public void setRelatedIndicatorIds(String relatedIndicatorIds) {
		this.relatedIndicatorIds = relatedIndicatorIds;
	}

}
