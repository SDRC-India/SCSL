<!-- 
@author Laxman(laxman@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="dashboardHomeApp">
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
#rext0 {
    display: block;
}
.tooltip.bottom{
	opacity: 1 !important;
}
.tooltip.bottom .tooltip-arrow{
	border-bottom-color: #5fb255;
}
.tooltip-inner {
    border-radius: 0;
    background-color: #5fb255 !important;
    box-shadow: 0px 2px 5px 1px #CCC;
}

@media(min-width: 768px){
	.pageNameContainer{
		right: 66px;
	}
}
@media(max-width: 640px){
	.pageNameContainer{
		right: auto;
		left: 0;
		width: 65%;
		transform: none;
	}
}
@media(max-width: 480px){
	.dashboard-excel-btn{
		    top: 5px;
	}
}
</style>
</head>

<body ng-controller="DashboardHomeController" ng-cloak>

<jsp:include page="fragments/header.jsp"></jsp:include>

	<div id="mymain" window-size>
		<div class="container">
			<div style="position: relative" class="page-info">
			<div class="pageNameContainer">
				<h4>{{pageName}}</h4>
			</div>
			<div class="download-container dashboard-excel-btn" ng-click="getLandingPageExcelFile()" data-toggle="tooltip" title="Download Excel" data-placement="bottom" >
					<img alt="" style="width: 24px;height: 24px;" src="resources/images/icons/excel-icon.svg">
			</div>
			</div>
		</div>
		<div class="container-fluid content-area">
			<section class="indicator-grid-section">
					<div class="indicatorGridRow" ng-repeat="noUse in getArrayForGridRows(12/oneRowGrid) track by $index">
						<div class="indicatorGrid" ng-repeat="indicator in indicators.slice($index * oneRowGrid, ($index+1) * oneRowGrid)">
							<div class="indicator-grid-content"  data-toggle="tooltip"
						title="{{getTooltipData(indicator)}}"  data-html="true"
						data-placement="bottom">
								<div class="value-container"><b class="ind-value">{{indicator.unit == "Number" ? indicator.andhraValue + indicator.telanganaValue:(indicator.andhraValue + indicator.telanganaValue)/2 + "%"}}</b></div>
								<div class="name-container"><b>{{indicator.name}} <br/> <span style="color: #9e9e9e;"> {{indicator.source ? "(" + indicator.source + ")": "" }} </span></b></div>
							</div>
						</div>
					</div>
					<!-- <div class="indicatorGridRow">
						<div class="indicatorGrid" ng-repeat="indicator in indicators.slice(oneRowGrid, 2 * oneRowGrid)">
							<div class="indicator-grid-content" data-toggle="tooltip"
						title="{{getTooltipData(indicator)}}"  data-html="true"
						data-placement="bottom">
								<div class="value-container"><b class="ind-value">{{indicator.unit == "Number" ? indicator.andhraValue + indicator.telanganaValue:(indicator.andhraValue + indicator.telanganaValue)/2 + "%"}}</b></div>
								<div class="name-container"><b>{{indicator.name}} <br/><span style="color: #9e9e9e;">{{indicator.source ? "(" + indicator.source + ")": "" }}</span></b></div>
							</div>
						</div>
					</div>
					<div class="indicatorGridRow">
						<div class="indicatorGrid" ng-repeat="indicator in indicators.slice(2 * oneRowGrid, 4 * oneRowGrid)">
							<div class="indicator-grid-content" data-toggle="tooltip"
						title="{{getTooltipData(indicator)}}"  data-html="true"
						data-placement="bottom">
								<div class="value-container"><b class="ind-value">{{indicator.unit == "Number" ? indicator.andhraValue + indicator.telanganaValue:(indicator.andhraValue + indicator.telanganaValue)/2 + "%"}}</b></div>
								<div class="name-container"><b>{{indicator.name}} <br/><span style="color: #9e9e9e;">{{indicator.source ? "(" + indicator.source + ")": "" }}</span></b></div>
							</div>
						</div>
					</div> -->
			</section>
			<section class="chart-section">
				<div style="margin-top: 30px" class="no-padding-mobile col-md-12">
					<div class="row">
						<div class="col-md-4 no-padding-mobile" ng-repeat="data in lineChartData">
							<div class="trend-chart" id="trend-chart-{{$index}}">
								<sdrc-multi-line-chart dataprovider="data" id="lineChart"></sdrc-multi-line-chart>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
	</div>
	
	<jsp:include page="fragments/footer.jsp"></jsp:include>
	<spring:url value="/webjars/d3js/3.4.6/d3.min.js" var="d3" />
	<script src="${d3}"></script>
	<script src="resources/js/jquery-ui.js"></script>
	<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
		var="bootstrapjs" />
	<script src="${bootstrapjs}"></script>
	<script type="text/javascript"
		src="resources/js/angularController/dashboardHomeController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
		var app = angular.module("dashboardHomeApp", []);
		var myAppConstructor = angular.module("dashboardHomeApp");
		myAppConstructor.controller("DashboardHomeController", dashboardHomeController);
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