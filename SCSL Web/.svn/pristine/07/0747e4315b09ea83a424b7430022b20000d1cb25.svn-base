<!-- 
@author Laxman (laxman@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="engagementScoreApp">
<head>

<title>SCSL-Engagement Score</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport">
<link rel="icon" href="resources/images/scsl_app_icon.png" type="image/png"
	sizes="16x16">	
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
</head>

<body ng-controller="EngagementScoreController" ng-cloak>
	<jsp:include page="fragments/header.jsp"></jsp:include>
	
	<div id="mymain" class="container">
		<div class="pageNameContainer">
			<h4>{{pageName}}</h4>
		</div>
		<div class="engagementSelection text-center">
			<div class="select-container dist-list1 text-center">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Facility *" id="facility"
						class="form-control not-visible-input" name="facility" readonly=""
						ng-model="selectedFacility.facilityName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li ng-repeat="facility in facilityModel"
								ng-click="selectFacility(facility);"><a href="">{{facility.facilityName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Month *" id="month"
						class="form-control not-visible-input" name="month" readonly=""
						ng-model="selectedTimeperiod.timePeriod">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li ng-repeat="timeperiod in selectedFacility.timeperiods"
								ng-click="selectTimeperiod(timeperiod);"><a href="">{{timeperiod.timePeriod}}</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<section class="formEngagementTable col-md-12">
			<div class=" table-responsive" style="width: 100%;">
				<table items="tableData" show-filter="true" cellpadding="0"
					cellspacing="0" border="0"
					class="dataTable table table-bordered engagementScoreTable"
					id="dataTable">
					<thead>
						<tr>
							<th style="position: relative;">Sl.&nbsp;No.</th>
							<th style="position: relative;">Progress</th>
							<th style="position: relative;">Question</th>
							<th style="position: relative; min-width: 100px;">Select</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="rowData in mstEngagementScoreModel">
							<td class="text-center">{{rowData.mstEngagementScoreId}}</td>
							<td>{{rowData.progress}}</td>
							<td>{{rowData.definition}}</td>
							<td class="text-center"><input type="radio" id='{{"radio" + $index}}'
								ng-disabled="selectedTimeperiod.mstEngagementScoreId || selectedTimeperiod.cssClass=='disabled'"
								ng-checked="rowData.mstEngagementScoreId == selectedTimeperiod.mstEngagementScoreId"
								ng-model="mstEngagementScoreId"
								ng-value="rowData.mstEngagementScoreId" name="score" /> <label
								ng-click="selectEngagementScore(rowData)"
								for='{{"radio" + $index}}'><span></span></label></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="text-center">
				<button class="submitEngagementScore"
					ng-disabled="!selectedFacility || !selectedTimeperiod || selectedTimeperiod.mstEngagementScoreId || !selectedEngagementScore || selectedTimeperiod.cssClass=='disabled'"
					ng-click="submitForm()">SUBMIT</button>
			</div>
		</section>
		<section class="linechartSection" style="margin-bottom: 50px;"
			ng-if="ldata[0].length">
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-8 col-md-offset-2 text-center"
						ng-repeat="data in ldata" id="lineChartLandingPage">
						<h5 class="district-heading">Quality Improvement Engagement Score for 
							{{selectedFacility.facilityName + " for " + data[data.length-1].axis
							+ " is "}} <span>{{data[data.length-1].value}}</span>
							<!-- <div ng-if="selectedDistrictId" ng-click="showStateLineChart()" class="reset-dist">
								<i class="fa fa-undo" aria-hidden="true"></i>
							</div> -->
						</h5>
						<samiksha-line id="pageLineChart" dataprovider="data"></samiksha-line>
					</div>
				</div>
			</div>
		</section>
	</div>
	<!-- Modal for error message -->
	<div id="errorMessage" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody">{{errorMsg}}</div>
					<button type="button" class="btn errorOk" ng-click="reloadPage()">Close</button>
				</div>
			</div>
		</div>
	</div>
	<!--end of thematic and chklist  -->
	<!-- popup modal -->
	<div id="pop" class="modal fade" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="successhead"><img alt="" src="resources/images/icons/Messages_success_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; SUCCESS</div>
					<div class="successbody">{{msg}}</div>
					<a class="btn btn-default" ng-click="reloadPage()">Ok</a>
				</div>
			</div>

		</div>
	</div>
	<jsp:include page="fragments/footer.jsp"></jsp:include>

	<spring:url value="/webjars/d3js/3.4.6/d3.min.js" var="d3" />
	<script src="${d3}"></script>
	<script type="text/javascript"
		src="resources/js/angularController/engagementScoreController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
		var app = angular.module("engagementScoreApp", []);
		var myAppConstructor = angular.module("engagementScoreApp");
		myAppConstructor.controller("EngagementScoreController",
				engagementScoreController);
		myAppConstructor.service('allServices', allServices);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directive.js"></script>
	<script type="text/javascript">
		$("#msgBox").show().delay(5000).fadeOut(4000);
	</script>
</body>

</html>