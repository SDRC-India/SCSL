<!-- 
@author Laxman (laxman@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="dashboardChartViewApp">
<head>

<title>SCSL-Small Multiple</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport">
<spring:url value="/resources/images/scsl_app_icon.png"
	var="appicon" />
<link rel="icon" type="image/png"   href="${appicon}">
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapjs" />
<link href="${bootstrapjs}" rel="stylesheet" />
<spring:url value="/resources/css/font-awesome.min.css" var="fontawesomeCss" />
<link href="${fontawesomeCss}" rel="stylesheet" />
<spring:url value="/resources/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet" />
<spring:url value="/resources/css/customLoader.css" var="customLoaderCss" />
<link href="${customLoaderCss}" rel="stylesheet" />
<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>
<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
	var="bootstrapjs" />
<script src="${bootstrapjs}"></script>
<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
	var="angularmin" />
<script src="${angularmin}" type="text/javascript"></script>
<script src="resources/js/angular-sanitize.js"></script>
<style>
@media(min-width: 992px){
	#mymain{
		margin-bottom: 0 !important;
	}
}
@media ( min-width : 768px) {
	.pageNameContainer {
		right: 66px;
	}
}

@media ( max-width : 640px) {
	.pageNameContainer.excel-available {
		right: auto;
		left: 0;
		width: 65%;
		transform: none;
	}
}

@media ( max-width : 480px) {
	.dashboard-excel-btn {
		top: 14px;
	}
}
</style>

</head>

<body ng-controller="DashboardChartViewController" ng-cloak>
	<jsp:include page="fragments/header.jsp"></jsp:include>
	<div class="filter-btn"
		ng-show="showFacilityCharts && openedTab == 'facility' || showIndicatorCharts && openedTab == 'indicator'">
		<i class="fa fa-filter fa-lg" aria-hidden="true"></i>
	</div>

	<button class="back-facility"
		ng-show="showFacilityCharts && openedTab == 'facility'"
		ng-click="backToFacilitySelection()">
		<i class="fa fa-long-arrow-left fa-lg" aria-hidden="true"></i>
	</button>
	<button class="back-facility backIndicator"
		ng-show="showIndicatorCharts && openedTab == 'indicator'"
		ng-click="backToIndicatorSelection()">
		<i class="fa fa-long-arrow-left fa-lg" aria-hidden="true"></i>
	</button>
	<div class="filter-section" id="advance-filter-section"
		ng-show="showFacilityCharts && openedTab == 'facility' || showIndicatorCharts && openedTab == 'indicator'">
		<div class="filter-buttons filter-btn-facility"
			ng-show="showFacilityCharts && openedTab == 'facility'">
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="State" id="state"
						class="form-control not-visible-input" name="state" readonly=""
						ng-model="selectedState.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li ng-repeat="state in areaList[0].children"
								ng-click="selectState(state);"><a href="">{{state.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="District " id="district"
						class="form-control not-visible-input" name="district" readonly=""
						ng-model="selectedDistrict.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li ng-repeat="district in selectedState.children"
								ng-click="selectDistrict(district);"><a href="">{{district.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Facility" id="facility"
						data-toggle="tooltip"
						data-original-title="{{selectedFacility.areaName? selectedFacility.areaName:''}}"
						data-placement="top"
						ng-class="{'hideTooltip': !selectedFacility.areaName}"
						class="form-control not-visible-input" name="facility" readonly=""
						ng-model="selectedFacility.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4? false : true"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu"
							style="max-height: 200px !important">
							<li
								ng-repeat="facility in selectedDistrict.children | filter:  { facilityType:selectedFacilityType.facilityType ,facilitySize: selectedFacilitySize.facilitySize  }"
								ng-click="selectFacility(facility);"><a href="">{{facility.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>

			<button type="submit" class="filter-submit"
				ng-disabled="!selectedFacility"
				ng-click="getFacilityChart(selectedFacility)">Filter</button>
			<button type="submit" class="filter-submit"
				ng-disabled="!selectedFacility && !selectedDistrict && !selectedState"
				class="filter-submit" style="margin-top: 8px;"
				ng-click="resetFacilityDropdowns()">Reset Filter</button>
		</div>
		<div class="filter-buttons filter-btn-indicator"
			ng-show="showIndicatorCharts && openedTab == 'indicator'">
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Wave" id="wave"
						class="form-control not-visible-input" name="wave" readonly=""
						ng-model="selectedIndWaveParam.name">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li ng-repeat="wave in allWaveParamList"
								ng-click="selectWaveParam(wave);"><a href="">{{wave.name}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="State" id="state"
						class="form-control not-visible-input" name="state" readonly=""
						ng-model="selectedIndState.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li ng-repeat="state in areaList[0].children"
								ng-click="selectIndState(state);"><a href="">{{state.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="District " id="district"
						class="form-control not-visible-input" name="district" readonly=""
						ng-model="selectedIndDistrict.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li ng-repeat="district in selectedIndState.children"
								ng-click="selectIndDistrict(district);"><a href="">{{district.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center report-margin">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Facility Type" id="facilitytype"
						class="form-control not-visible-input" name="facilitytype" readonly=""
						ng-model="selectedIndFacilityType.facilityTypeName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="facilitytype in facilitytypes"
								ng-click="selectIndFacilityType(facilitytype);"><a href="">{{facilitytype.facilityTypeName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center report-margin" >
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Facility Size" id="facilitySize"
						class="form-control not-visible-input" name="state" readonly=""
						ng-model="selectedIndFacilitySize.facilitySizeName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="facilitysize in filteredIndFacilitySizes"
								ng-click="selectIndFacilitySize(facilitysize);"><a href="">{{facilitysize.facilitySizeName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Indicator Type *"
						id="Indicator Type" class="form-control not-visible-input"
						name="district" readonly="" ng-model="selectedIndicatorType">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li ng-repeat="indicatorType in indicatorTypeList"
								ng-click="selectIndicatorType(indicatorType);"><a href="">{{indicatorType}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center"
				ng-hide="hideFocusArea">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Focus Area" id="focusArea"
						class="form-control not-visible-input" name="state" readonly=""
						ng-model="selectedFocusArea">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu corearea-dropdown" role="menu">
							<li ng-repeat="area in allFocusAreaList"
								ng-click="selectFocusArea(area);"><a href="">{{area}}</a></li>
						</ul>
					</div>
				</div>
			</div>

			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;"
					ng-class="{'hideTooltip': !selectedIndicator.value}">
					<input type="text" placeholder="Indicator" id="indicator"
						data-toggle="tooltip"
						data-original-title="{{selectedIndicator.value? selectedIndicator.value:''}}"
						data-placement="bottom" class="form-control not-visible-input"
						name="district" readonly="" ng-model="selectedIndicator.value">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu corearea-dropdown1" role="menu">
							<li ng-repeat="indicator in allIndicatorList"
								ng-click="selectIndicator(indicator);"><a href="">{{indicator.value}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center report-margin">
				<div class="input-group"  style="margin: auto;">
					<input type="text" placeholder="Periodicity *" id="periodicity"
						class="form-control not-visible-input" name="periodicity" readonly=""
						ng-model="selectedPeriodicity.name">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="periodicity in periodicities"
								ng-click="selectPeriodicity(periodicity);"><a href="">{{periodicity.name}}</a></li>
						</ul>
					</div>
				</div>
			</div>

			<button type="submit" class="filter-submit"
				ng-disabled="!selectedIndicator || isEmpty(selectedPeriodicity)"
				ng-click="getIndicatorChart(selectedIndicator.key)">Filter</button>
			<button type="submit" class="filter-submit"
				ng-disabled="!selectedIndicator && !selectedIndicatorType && !selectedFocusArea && isEmpty(selectedIndState) && isEmpty(selectedIndDistrict)
				&& isEmpty(selectedIndFacilityType) && isEmpty(selectedIndFacilitySize)  && isEmpty(selectedIndWaveParam)  && isEmpty(selectedPeriodicity)"
				class="filter-submit" style="margin-top: 8px;"
				ng-click="resetIndicatorDropdowns()">Reset Filter</button>

		</div>
	</div>
	<div id="mymain" class="container-fluid">
		<div class="container">
			<div style="position: relative" class="page-info">
				<div class="pageNameContainer"
					ng-class="{'excel-available': facilityChartView}">
					<h4>{{pageName}}</h4>
				</div>
				<div class="download-container dashboard-excel-btn"
					ng-show="(showFacilityCharts && openedTab == 'facility' )|| ( showIndicatorCharts && openedTab == 'indicator' )"
					ng-click="( openedTab == 'indicator' ) || ( openedTab == 'facility' ) ? getSmallMultipleExcelFile():''"
					data-toggle="tooltip" title="Download Excel"
					data-placement="bottom">
					<img alt="" style="width: 24px; height: 24px;" 
						src="resources/images/icons/excel-icon.svg">
				</div>
			</div>
		</div>
		<section class="chart-view col-md-12 col-sm-12 col-xs-12">

			<ul class="nav nav-pills text-center heading-tab-chart">
				<li class="active" id="facility-tab"
					ng-click="getOpenedTab('facility')"><a data-toggle="pill"
					href="#facilitySelection">Facility</a></li>
				<li id="indicator-tab" ng-click="getOpenedTab('indicator')"><a
					data-toggle="pill" href="#indicatorSelection">Indicator</a></li>
				<div id="border-bottom-slider"></div>
			</ul>

			<div class="tab-content">
				<div id="facilitySelection"
					class="tab-pane text-center fade in active">
					<section class="selection-sec" ng-show="!showFacilityCharts">
						<div class="select-container dist-list text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" placeholder="State" id="state"
									class="form-control not-visible-input" name="state" readonly=""
									ng-model="selectedState.areaName">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu" role="menu">
										<li ng-repeat="state in areaList[0].children"
											ng-click="selectState(state);"><a href="">{{state.areaName}}</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="select-container dist-list text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" placeholder="District " id="district"
									class="form-control not-visible-input" name="district"
									readonly="" ng-model="selectedDistrict.areaName">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu" role="menu">
										<li ng-repeat="district in selectedState.children"
											ng-click="selectDistrict(district);"><a href="">{{district.areaName}}</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="select-container dist-list text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" placeholder="Facility" id="facility"
									data-toggle="tooltip"
									data-original-title="{{selectedFacility.areaName? selectedFacility.areaName:''}}"
									data-placement="top"
									ng-class="{'hideTooltip': !selectedFacility.areaName}"
									class="form-control not-visible-input" name="facility"
									readonly="" ng-model="selectedFacility.areaName">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4? false : true"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu report-dropdown" role="menu"
										style="max-height: 200px !important">
										<li
											ng-repeat="facility in selectedDistrict.children | filter:  { facilityType:selectedFacilityType.facilityType ,facilitySize: selectedFacilitySize.facilitySize  }"
											ng-click="selectFacility(facility);"><a href="">{{facility.areaName}}</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="submission-div">
							<button class="submit-selection" ng-disabled="!selectedFacility"
								ng-click="getFacilityChart(selectedFacility)">Submit</button>
						</div>
					</section>
					<section class="chart-section" ng-show="showFacilityCharts">
						<h4>
							<span class="blue-text">{{savedSelectedState.areaName}}</span> >>
							<span class="blue-text">{{savedSelectedDistrict.areaName}}</span>
							>> <span class="blue-text">{{savedSelectedFacility.areaName}}</span>
						</h4>
						<div style="margin-top: 30px" class="no-padding-mobile col-md-12">
							<div class="row">
								<div class="col-md-4 no-padding-mobile"
									ng-repeat="data in allFacilityCharts">
									<div class="trend-chart" id="trend-chart-{{$index}}">
										<sdrc-multi-line-chart dataprovider="data" id="lineChart"></sdrc-multi-line-chart>
									</div>
								</div>
							</div>
						</div>
					</section>
				</div>
				<div id="indicatorSelection" class="tab-pane text-center fade">
					<section class="selection-sec" ng-show="!showIndicatorCharts">
						<div class="select-container dist-list text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" placeholder="Indicator Type"
									id="Indicator Type" class="form-control not-visible-input"
									name="district" readonly="" ng-model="selectedIndicatorType">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu" role="menu">
										<li ng-repeat="indicatorType in indicatorTypeList"
											ng-click="selectIndicatorType(indicatorType);"><a
											href="">{{indicatorType}}</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="select-container dist-list text-center"
							ng-hide="hideFocusArea">
							<div class="input-group" style="margin: auto;">
								<input type="text" placeholder="Focus Area" id="state"
									class="form-control not-visible-input" name="state" readonly=""
									ng-model="selectedFocusArea">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu corearea-dropdown" role="menu">
										<li ng-repeat="area in allFocusAreaList"
											ng-click="selectFocusArea(area);"><a href="">{{area}}</a></li>
									</ul>
								</div>
							</div>
						</div>

						<div class="select-container dist-list text-center">
							<div class="input-group" style="margin: auto;"
								ng-class="{'hideTooltip': !selectedIndicator.value}">
								<input type="text" placeholder="Indicator" id="district"
									data-toggle="tooltip"
									data-original-title="{{selectedIndicator.value? selectedIndicator.value:''}}"
									data-placement="bottom" class="form-control not-visible-input"
									name="district" readonly="" ng-model="selectedIndicator.value">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu corearea-dropdown1" role="menu">
										<li ng-repeat="indicator in allIndicatorList"
											ng-click="selectIndicator(indicator);"><a href="">{{indicator.value}}</a></li>
									</ul>
								</div>
							</div>
						</div>
						<div class="submission-div">
							<button class="submit-selection" ng-disabled="!selectedIndicator"
								ng-click="getIndicatorChart(selectedIndicator.key)">Submit</button>
						</div>
					</section>
					<section class="chart-section" ng-show="showIndicatorCharts">
						<h4>
							<span class="blue-text" ng-bind-html="breadcrumb"></span>
						</h4>
						<div style="margin-top: 30px" class="no-padding-mobile col-md-12">
							<div class="row">
								<div class="col-md-5 no-padding-mobile fixedChart"
									ng-repeat="data in allIndicatorCharts.aggreagteArea">
									<div class="trend-ind-aggre-chart" id="trend-ind-aggre-chart-{{$index}}">
										<sdrc-multi-line-chart dataprovider="data" id="aggregatelineChart"></sdrc-multi-line-chart>
									</div>
									<div class="linkOtherChartSection">
										<h5 ng-show="savedSelectedIndicatorType == 'Process'"><span>Process Indicator ({{aggreagteAreaName}})</span></h5>
										<h5 ng-show="savedSelectedIndicatorType == 'Intermediate'"><span>Intermediate Indicator ({{aggreagteAreaName}})</span></h5>
										<h5 ng-show="savedSelectedIndicatorType == 'Outcome'"><span>Outcome Indicator ({{aggreagteAreaName}})</span></h5>
										<a href="javascript:void(0)" ng-show="allRelatedIndicatorList.Process.length" ng-click="showRelatedIndicatorModal('Process')"><i class="fa fa-angle-right" aria-hidden="true"></i> Go to related process indicator</a>
										<a href="javascript:void(0)" ng-show="allRelatedIndicatorList.Intermediate.length" ng-click="showRelatedIndicatorModal('Intermediate')"><i class="fa fa-angle-right" aria-hidden="true"></i> Go to related intermediate indicator</a>
										<a href="javascript:void(0)" ng-show="allRelatedIndicatorList.Outcome.length" ng-click="showRelatedIndicatorModal('Outcome')"><i class="fa fa-angle-right" aria-hidden="true"></i> Go to related outcome indicator</a>
									</div>
								</div>
								<div class="col-md-offset-5 col-md-7">
									<div class="row">
										<div class="col-md-6  no-padding-mobile"
											ng-repeat="data in allIndicatorCharts.all">
											<div class="trend-ind-chart" id="trend-ind-chart-{{$index}}">
												<sdrc-multi-line-chart dataprovider="data" id="lineChart"></sdrc-multi-line-chart>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div>
		</section>
	</div>
	<!-- Modal for error message -->
	<div id="errorMessage" class="modal fade" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead">
						<img alt=""
							src="resources/images/icons/Messages_warning_caution_icon.svg"
							style="width: 25px; margin-top: -5px;">&nbsp; ERROR
					</div>
					<div class="errorbody">{{errorMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<div id="relatedIndicatorListModal" class="modal fade" role="dialog"
			data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<!-- Modal content -->
				<div class="modal-content">
					<button type="button" class="custom-close close"
						data-dismiss="modal" style="margin-right: 15px;cursor: pointer; margin-top: 10px;">&times;</button>
					<div class="modal-body text-center">
						<div><h3 class="text-center heading-related-ind">Related {{showingRelatedIndicatorType}} Indicator List</h3></div>
						<section class="indicator-list-section">
							<div class="indicator-list">
								<div class="relatedIndicator" ng-click="closeRelatedIndicatorModal();setRelatedIndicatorDetails(relatedIndicator.indicatorId);getIndicatorChart(relatedIndicator.indicatorId);" ng-repeat="relatedIndicator in relatedIndicators">{{relatedIndicator.indicatorName}}</div>
							</div>
						</section>
					</div>
				</div>
			</div>
		</div>
	<jsp:include page="fragments/footer.jsp"></jsp:include>

	<spring:url value="/webjars/d3js/3.4.6/d3.min.js" var="d3" />
	<script src="${d3}"></script>
	<script type="text/javascript" src="resources/js/autosizeTextarea.js"></script>
	<script type="text/javascript">
		$(function() {
			$('.textarea-length').autosize({
				append : "\n"
			});
		});
	</script>
	<script type="text/javascript"
		src="resources/js/angularController/dashboardChartViewController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
		var app = angular.module("dashboardChartViewApp", ['ngSanitize']);
		var myAppConstructor = angular.module("dashboardChartViewApp");
		myAppConstructor.controller("DashboardChartViewController",
				dashboardChartViewController);
		myAppConstructor.service('allServices', allServices);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directive.js"></script>
	<script type="text/javascript"
		src="resources/js/angularDirective/dashboardChartViewDirective.js"></script>
	<script type="text/javascript">
		$("#msgBox").show().delay(2000).fadeOut(400);
		$(document).ready(function() {

			$('[data-toggle="tooltip"]').tooltip();

		});
	</script>
</body>

</html>