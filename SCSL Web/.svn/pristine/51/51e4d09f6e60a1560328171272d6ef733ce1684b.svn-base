<!-- 
@author Laxman(laxman@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="dashboardFacilityViewApp">
<head>

<title>SCSL-Dashboard</title>
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
<!--[if lte IE 8]>
  <link rel="stylesheet" type="text/css" media="screen, projection" href="ie8-down.css" />
<![endif]-->
<link rel="stylesheet" href="resources/css/bootstrap-datetimepicker.css">

<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>

<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
	var="angularmin" />
<script src="${angularmin}" type="text/javascript"></script>
<spring:url value="/webjars/jquery-ui/1.10.3/themes/base/jquery-ui.css"
	var="jQueryUiCss" />
<link href="${jQueryUiCss}" rel="stylesheet"></link>
<style>
.tooltip.bottom{
	opacity: 1 !important;
}
.popover.top{
	z-index: 9999;
}
.tooltip.bottom .tooltip-arrow{
	border-bottom-color: #5fb255;
}
.tooltip-inner {
    border-radius: 0;
    background-color: #5fb255 !important;
    box-shadow: 0px 2px 5px 1px #CCC;
}
div#mymain{
min-height: 600px !important;
}
@media (max-width: 991px){
	.content-area {
    	margin-top: 60px;
	}
}
ul#ui-id-1 {
max-height: 250px;
}
</style>

</head>

<body ng-controller="DashboardHomeController" ng-cloak>

<jsp:include page="fragments/header.jsp"></jsp:include>

	<div id="mymain">
		<div class="container">
			<div style="position: relative">
				<div class="pageNameContainer">
					<h4>{{pageName}}</h4>
				</div>
			</div>
		</div>
		<div class="container-fluid content-area">
			<div class="selection-section">
				<div class="facilityViewSelection text-center">
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
							<input type="text" placeholder="Facility Type" id="facilitytype"
						class="form-control not-visible-input" name="facilitytype" readonly=""
						ng-model="selectedFacilityType.facilityTypeName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4 ? false : true"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="facilitytype in facilitytypes"
								ng-click="selectFacilityType(facilitytype);"><a href="">{{facilitytype.facilityTypeName}}</a></li>
						</ul>
					</div>
						</div>
					</div>
					<div class="select-container dist-list text-center">
						<div class="input-group" style="margin: auto;">
							<input type="text" placeholder="Facility Size" id="facilitySize"
						class="form-control not-visible-input" name="state" readonly=""
						ng-model="selectedFacilitySize.facilitySizeName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4 ? false : true"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="facilitysize in filteredFacilitySizes"
								ng-click="selectFacilitySize(facilitysize);"><a href="">{{facilitysize.facilitySizeName}}</a></li>
						</ul>
					</div>
						</div>
					</div>
					<div class="reset-button text-center">
						<div class="input-group-btn" style="position: relative;" data-toggle="tooltip" data-original-title="Reset" 
						data-placement="top">
							<button data-toggle="dropdown" ng-click="resetSelection()"  
								class="btn btn-color dropdown-toggle" type="button">
								<i class="fa fa-undo"></i>
							</button>
						</div>
					</div>
					
				</div>
			</div>
			<div class="map-section">
				<div style="position: relative;">
					<section class="map-legends"
						style="min-width: 210px; max-width: 300px;">

						<ul style="padding-left: 10px; padding-right: 20px;">
							<li class="legend_list"
								style="margin-bottom: 2px; margin-top: 15px; text-align: center;">
								<h4>Overall Score</h4>
							</li>
							<li class="legend_list ng-scope">
								<div class="legend-ind">
									<span class="legend_key ">below 2</span> (<span
										style="color: red;">{{redMarkers}}</span>)
								</div>
								<div class="firstslices legnedblock"></div>
							</li>
							<!-- end ngRepeat: legend in legends -->
							<li class="legend_list "><div class="legend-ind">
									<span class="legend_key ">2-4</span> (<span
										style="color: orange;">{{orangeMarkers}}</span>)
								</div>
								<div class="secondslices legnedblock"></div></li>
							<!-- end ngRepeat: legend in legends -->
							<li class="legend_list "><div class="legend-ind">
									<span class="legend_key ng-binding">4 and above</span> (<span
										style="color: green;">{{greenMarkers}}</span>)
								</div>
								<div class="fourthslices legnedblock"></div></li>
						</ul>
					</section>
					<section class="searchFacility">
						<div class="select-container text-center">
							<div class="input-group" style="margin: auto;">
								<input type="text" placeholder="Search Facility"
									id="searchDashboard" class="form-control not-visible-input"
									name="searchFacility" ng-model="pushpinFilterWord"
									ng-keyup="searchNodeDashboard()">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown" class="btn btn-color"
										type="button">
										<i class="fa fa-search" style="color: #FFF;"></i>
									</button>
								</div>
							</div>
						</div>
					</section>

					<google-map center="map.center" zoom="map.zoom" draggable="true">
					<markers class="pushpin" models="map.markers" coords="'self'"
						icon="'icon'" events="map.events"> <windows
						show="'showWindow'" closeClick="'closeClick'"
						options='pixelOffset'>
					<p ng-non-bindable
						style="width: 130px; height: 30px; font-size: 15px; color: #313e4d; display: inline;">
						<strong style="color: #5FB255">{{title}}</strong> <br> <strong>Time
							Period: <span style="color: #5FB255">{{timePeriodStr}}</span>
						</strong> <br> <strong>Score: <span style="color: #5FB255">{{score}}</span></strong>
					</p>
					</windows> </markers> <polygon static="true" ng-repeat="p in polygons track by p.id"
						path="p.path" stroke="p.stroke" visible="p.visible"
						geodesic="p.geodesic" fill="p.fill" fit="false"
						editable="p.editable" draggable="p.draggable"></polygon> </google-map>

				</div>
				<div class="trend-viz animate-show" ng-animate=" 'animate' "
					ng-show="isTrendVisible">
					<button class="close" aria-hidden="true" type="button"
						ng-click="closeViz()"
						style="color: #f2f2f2; text-shadow: 0 1px 0 #656363;">
						<span class="glyphicon glyphicon-remove-circle"></span>
					</button>

<!-- 					<div class="container-fluid"> -->
<!-- 						<div class="row show-grid"> -->
<!-- 							<div class="col-xs-11 col-md-11 text-center left"> -->
<!-- 								<h4 style="margin-top: 0; margin-bottom: 0">{{selectedPushpin.title}}</h4> -->
<!-- 							</div> -->


<!-- 						</div> -->
<!-- 					</div> -->
					<div class="downloadTrend"
						style="margin-right: 15px; position: fixed; right: 26px; z-index: 1000;">
						<a style="cursor: pointer;"> <span
							style="font-size: x-large; margin-right: -10px;"
							data-toggle="tooltip" title="Download"
							id="lineChartDownloadBtnDashboard"
							class="btn viz-download excelDownloadBtn glyphicon glyphicon-download-alt"
							ng-click="downloadGoogleMapTrend();"></span>
						</a>
					</div>
					<div class="row">
						<div class="col-md-12">

							<div class="line-separator"></div>

							<div class="row">
								<div class="col-md-12">
									<div class="text-center"
										ng-repeat="data in lineChartData track by $index">
										<trend-line  id="trendLineChart" dataprovider="data"></trend-line>
									</div>
							</div>
									<div class="col-md-2"></div>
								

							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
		
	</div>
	
	<jsp:include page="fragments/footer.jsp"></jsp:include>
	<spring:url value="/webjars/d3js/3.4.6/d3.min.js" var="d3" />
	<script src="${d3}"></script>
	<script src="resources/js/jquery-ui.js"></script>
	<script src="resources/js/polygons.js"></script>
	<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
		var="bootstrapjs" />
	<script src="${bootstrapjs}"></script>
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBedTd_YjXAiOM8I34K2MRUzqso2wu0wlA"
		type="text/javascript"></script>
	<script type="text/javascript" src="resources/js/lodash.min.js"></script>		
	<script type="text/javascript" src="resources/js/angular-google-maps.min.js"></script>	
	<script type="text/javascript"
		src="resources/js/angularController/dashboardFacilityViewController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
		var app = angular.module("dashboardFacilityViewApp", ['google-maps']);
		var myAppConstructor = angular.module("dashboardFacilityViewApp");
		myAppConstructor.controller("DashboardHomeController", dashboardFacilityViewController);
		myAppConstructor.service('allServices', allServices);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directive.js"></script>
	<script type="text/javascript"
		src="resources/js/angularDirective/dashboardDirective.js"></script>
	<script>
		$(document).ready(function() {

			$('[data-toggle="tooltip"]').tooltip();
			
		});
	</script>
	
</body>

</html>