<!-- 
@author Devikrushna (devikrushna@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="reportApp">
<head>

<title>SCSL-Report</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport">
<spring:url value="/resources/images/scsl_app_icon.png"
	var="appicon" />
<link rel="icon" type="image/png"   href="${appicon}">
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<spring:url value="/resources/css/font-awesome.min.css" var="fontawesomeCss" />
<link href="${fontawesomeCss}" rel="stylesheet" />
<spring:url value="/resources/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet" />
<spring:url value="/resources/css/customLoader.css" var="customLoaderCss" />
<link href="${customLoaderCss}" rel="stylesheet" />
<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>
<%-- <spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js" --%>
<%-- 	var="bootstrapjs" /> --%>
<%-- <script src="${bootstrapjs}"></script> --%>
<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
	var="angularmin" />
<script src="${angularmin}" type="text/javascript"></script>
<spring:url value="/webjars/jquery-ui/1.10.3/themes/base/jquery-ui.css"
	var="jQueryUiCss" />
<link href="${jQueryUiCss}" rel="stylesheet"></link>
</head>

<body ng-controller="reportController" ng-cloak class="reportplandatebody">
<style type="text/css">
.reportplandatebody .ui-datepicker table {
	font-size: 0.6em !important;
}
.reportplandatebody > div#ui-datepicker-div {
    width: 204px;
}
.reportplandatebody span.ui-datepicker-month ,.reportplandatebody span.ui-datepicker-year{
    font-size: 0.8em;
}
</style>
	<jsp:include page="fragments/header.jsp"></jsp:include>
	<div id="mymain" class="container">
		<div class="pageNameContainer">
			<h4>{{pageName}}</h4>
		</div>

		<div class="col-md-12 reportSection text-center ">
			<div class="select-container dist-list text-center report-margin">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Report Type *" id="reporttype"
						class="form-control not-visible-input" name="reporttype" readonly=""
						ng-model="selectedReportType.typeName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="report in reportType"
								ng-click="selectReport(report);"><a href="">{{report.typeName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			
			<div class="select-container dist-list text-center report-margin" ng-disabled="selectedReportType.typeID ==4 ? true : false">
				<div class="input-group"  style="margin: auto;">
					<input type="text" placeholder="Periodicity *" id="periodicity"
						class="form-control not-visible-input" name="periodicity" readonly=""
						ng-model="selectedPeriodicity.name">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown" ng-disabled="selectedReportType.typeID ==4 ? true : false"
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
			
			<div ng-show="fromTodate">
			<div class="select-container dist-list text-center report-margin">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="From *" id="startdate"
						class="form-control not-visible-input" name="startdate" readonly=""
						ng-model="selectedStartDate.timePeriod">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="startdate in timePeriod"
								ng-click="selectStartDate(startdate);"><a href="">{{startdate.timePeriod}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center report-margin">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="To" id="enddate"
						class="form-control not-visible-input" name="enddate" readonly=""
						ng-model="selectedEndDate.timePeriod">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="enddate in endTimePeriods"
								ng-click="selectEndDate(enddate);"><a href="">{{enddate.timePeriod}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			</div>

			<div class="row" ng-show="planCalender">
				<div class="select-container report-margin">
					<div class="input-group" style="margin: auto;">
						<input type="text" placeholder="Start Date *" required
							id="datepicker" ng-model="startplanDate"
							ng-change="calculateEndDate()"
							class="form-control not-visible-input" readonly>
						<div class="input-group-btn" style="position: relative;">
							<button data-toggle="dropdown" id="icondatepicker"
								class="btn btn-color dropdown-toggle" type="button">
								<i class="fa fa-calendar"></i>
							</button>
						</div>
					</div>
				</div>

				<div class="select-container report-margin">
					<div class="input-group" style="margin: auto;">
						<input type="text" placeholder="End Date" required
							id="datepicker1" ng-model="endplanDate"
							class="form-control not-visible-input" readonly
							ng-disabled="selectedStartplanDate!=startplanDate ? false : true">
						<div class="input-group-btn" style="position: relative;">
							<button data-toggle="dropdown" id="icondatepicker1"
								class="btn btn-color dropdown-toggle" type="button"
								ng-disabled="selectedStartplanDate!=startplanDate ? false : true">
								<i class="fa fa-calendar"></i>
							</button>
						</div>
					</div>
				</div>
			</div>

		</div>
		<div class="col-md-12 reportSection1 text-center ">
			
			<div class="select-container dist-list text-center report-margin" ng-if="userlevel == 1" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4 ? true : false">
				<div class="input-group" style="margin: auto;" >
					<input type="text" placeholder="State" id="state"
						class="form-control not-visible-input" name="state" readonly=""
						ng-model="selectedState.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4 ? false : true"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="state in areaList[0].children"
								ng-click="selectState(state);"><a href="">{{state.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center report-margin" ng-if="userlevel == 1 || userlevel == 2" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4  ? true : false" >
				<div class="input-group" style="margin: auto;" >
					<input type="text" placeholder="District" id="district"
						class="form-control not-visible-input" name="district" readonly=""
						ng-model="selectedDistrict.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4 ? false : true"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="district in selectedState.children"
								ng-click="selectDistrict(district);"><a href="">{{district.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-12 reportSection1 text-center" ng-if="userlevel == 1 || userlevel == 2 || userlevel == 3" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4 ? true : false">
			<div class="select-container dist-list text-center report-margin">
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
			<div class="select-container dist-list text-center report-margin" ng-if="userlevel == 1 || userlevel == 2 || userlevel == 3" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4 ? true : false">
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
			<div class="select-container dist-list text-center report-margin" ng-if="userlevel == 1 || userlevel == 2 || userlevel == 3" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4 ? true : false">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Facility" id="facility" data-toggle="tooltip"
						data-original-title="{{selectedFacility.areaName? selectedFacility.areaName:''}}"
						data-placement="top" ng-class="{'hideTooltip': !selectedFacility.areaName}"
						class="form-control not-visible-input" name="facility" readonly=""
						ng-model="selectedFacility.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown" ng-disabled="selectedReportType.typeID!=3 && selectedReportType.typeID!=4? false : true"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu" style="max-height: 200px !important">
							<li ng-repeat="facility in selectedDistrict.children | filter:  { facilityType:selectedFacilityType.facilityType ,facilitySize: selectedFacilitySize.facilitySize  }"
								ng-click="selectFacility(facility);"><a href="">{{facility.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<div class="col-md-12 col-sm-12 report-button-section  text-center">
			<button class="downloadReport " ng-click="validateReport()">Download</button>
			<button class="downloadReport " ng-click="resetReportForm()">Reset</button>
		</div>

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
	<!--end of thematic and chklist  -->
	<!-- Modal for warning message -->
	<div id="warningMessage" class="modal fade" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="warnhead">
						<img alt=""
							src="resources/images/icons/Messages_warning_caution_icon.svg"
							style="width: 25px; margin-top: -5px;">&nbsp; WARNING
					</div>
					<div class="warnbody">{{warningMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<div id="infoMessage" class="modal fade" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead">
						<img alt="" src="resources/images/icons/Messages_info_icon.svg"
							style="width: 25px; margin-top: -5px;">&nbsp; INFO
					</div>
					<div class="warnbody">{{infoMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<!-- popup modal -->
	<div id="pop" class="modal fade" role="dialog" data-backdrop="static"
		data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="successhead">
						<img alt="" src="resources/images/icons/Messages_success_icon.svg"
							style="width: 25px; margin-top: -5px;">&nbsp; SUCCESS
					</div>
					<div class="successbody">{{msg}}</div>
					<a class="btn btn-default" data-dismiss="modal">Ok</a>
				</div>
			</div>
		</div>
	</div>

	<div id="pop1" class="modal fade" role="dialog" data-backdrop="static"
		data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="successhead">
						<img alt="" src="resources/images/icons/Messages_success_icon.svg"
							style="width: 25px; margin-top: -5px;">&nbsp; SUCCESS
					</div>
					<div class="successbody">Closed successfully</div>
					<a class="btn btn-default" data-dismiss="modal">Ok</a>
				</div>
			</div>
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
		src="resources/js/angularController/reportController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
			var app = angular.module("reportApp", []);
			var myAppConstructor = angular.module("reportApp");
			myAppConstructor.controller("reportController", reportController);
			myAppConstructor.service('allServices', allServices);
		</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directive.js"></script>
	<script type="text/javascript">
			$("#msgBox").show().delay(2000).fadeOut(400);
		</script>
		<script type="text/javascript">
		$(document).ready(function() {
			$("#button3id").on('submit', function() {
				$('#ideamodal').modal('show');
			});

		});
		
		$( "#datepicker" ).datepicker({dateFormat:"dd-mm-yy"});
		$( "#datepicker1" ).datepicker({dateFormat:"dd-mm-yy"});
		$("#icondatepicker").click(function() {
			$("#datepicker").datepicker("show");
		});
		$("#icondatepicker1").click(function() {
			$("#datepicker1").datepicker("show");
		})
	</script>
	<script>
		$(document).ready(
				function() {
					$('[data-toggle="tooltip"]').tooltip();
				});
		</script>
</body>

</html>