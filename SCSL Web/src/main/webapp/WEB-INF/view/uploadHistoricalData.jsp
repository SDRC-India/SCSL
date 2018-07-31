<!-- 
@author Devikrushna (devikrushna@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="historicalDataApp">
<head>

<title>SCSL-Historical Data</title>
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
<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
	var="bootstrapjs" />
<script src="${bootstrapjs}"></script>
<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
	var="angularmin" />
<script src="${angularmin}" type="text/javascript"></script>
</head>

<body ng-controller="historicalDataController" ng-cloak>
	<jsp:include page="fragments/header.jsp"></jsp:include>
	<div id="mymain" class="container">
		<div class="pageNameContainer">
			<h4>{{pageName}}</h4>
		</div>


		<div class="col-md-12 historicalDataSection text-center ">

			<div class="select-container dist-list text-center report-margin">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="State *" id="state"
						class="form-control not-visible-input" name="state" readonly=""
						ng-model="selectedState.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu corearea-dropdown1" role="menu">
							<li ng-repeat="state in areaList[0].children"
								ng-click="selectState(state);"><a href="">{{state.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center report-margin">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="District *" id="district"
						class="form-control not-visible-input" name="district" readonly=""
						ng-model="selectedDistrict.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu corearea-dropdown" role="menu">
							<li ng-repeat="district in selectedState.children"
								ng-click="selectDistrict(district);"><a href="">{{district.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center report-margin">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Facility *" id="facility"
					
					 data-toggle="tooltip"
						data-original-title="{{selectedFacility.areaName? selectedFacility.areaName:''}}"
						data-placement="top" ng-class="{'hideTooltip': !selectedFacility.areaName}"
						class="form-control not-visible-input" name="facility" readonly=""
						ng-model="selectedFacility.areaName">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu report-dropdown" role="menu">
							<li ng-repeat="facility in selectedDistrict.children"
								ng-click="selectFacility(facility);"><a href="">{{facility.areaName}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="reset-button text-center report-margin">
					<div class="input-group-btn" style="position: relative;" data-toggle="tooltip" data-original-title="Reset" 
					data-placement="top">
						<button data-toggle="dropdown" ng-click="resetDiv()" 
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-undo"></i>
						</button>
					</div>
				</div>
			
		</div>
		<div class="col-md-12 reportSection1 text-center ">
			<div class="select-container dist-list text-center report-margin">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="From *" id="fromdate"
						class="form-control not-visible-input" name="fromdate" readonly=""
						ng-model="selectedFromDate.timePeriod">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu corearea-dropdown1" role="menu">
							<li ng-repeat="fromdate in timePeriod"
								ng-click="selectFromDate(fromdate);"><a href="">{{fromdate.timePeriod}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center report-margin">
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="To" id="todate"
						class="form-control not-visible-input" name="todate" readonly=""
						ng-model="selectedToDate.timePeriod">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu corearea-dropdown" role="menu">
							<li ng-repeat="todate in endTimePeriods"
								ng-click="selectToDate(todate);"><a href="">{{todate.timePeriod}}</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>


		<div class="col-md-12 col-sm-12 col-xs-12  text-center " >
			<img ng-click=validateHistoricalData() alt="" class="down-up-icon"
				style="width: 70px; cursor: pointer; margin-top: 40px;"
				src="resources/images/icons/svg_download_doc.svg">
			<h4 style="text-align:center;"><span style="cursor: pointer;" ng-click=validateHistoricalData()>DOWNLOAD</span></h4>
			<div style="border-bottom: 1px solid gray; margin-top: 40px;"></div>
		</div>
		<div class="col-md-12 col-sm-12 col-xs-12  text-center upload-div">
			<img id="uploadicon" class="down-up-icon" alt=""
				style="width: 70px; cursor: pointer; margin-top: 40px;"
				src="resources/images/icons/svg_upload_doc_data.svg">

			<h4 id="uploadBtn" style="cursor: pointer;">UPLOAD</h4>
		</div>
		<span>
		<input type="file" ng-class="" id="data-upload" onchange="angular.element('body').scope().getFileDetails(this)"
									ng-model="dataload" name="file"></span>
	</div>


	<!-- Modal for error message modal -->
	<div id="errorMessage1" class="modal fade errorMessage-modal" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-dialog1">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead">
						<img alt=""
							src="resources/images/icons/Messages_warning_caution_icon.svg"
							style="width: 25px; margin-top: -5px;">&nbsp; ERROR
					</div>
					<ul style="max-height: 300px;padding-left: 35px;">
						<li ng-repeat="errors in errorMsgs">{{errors.exceptionType}} : {{errors.exceptionMessage}}</li>
					</ul>
					<button type="button" class="btn errorOk" data-dismiss="modal" >Close</button>
				</div>
			</div>
		</div>
	</div>
	<!--end of error message  -->


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
	<!--end of error message  -->
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
					<div class="warnbody" ng-repeat="errors in errorMsgs">{{errors.exceptionType}} : {{errors.exceptionMessage}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Ok</button>
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
					<button type="button" class="btn errorOk" ng-click="clearFile()" >Cancel</button>
					<span><button type="button" class="btn errorOk" id="uploadModal" ng-click="uploadFile(files)" >Upload</button></span>
					
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
					<div class="successbody" > sucessful</div>
					<a class="btn btn-default" >Ok</a>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="fragments/footer.jsp"></jsp:include>

		<spring:url value="/webjars/d3js/3.4.6/d3.min.js" var="d3" />
		<script src="${d3}"></script>
		<script type="text/javascript"
			src="resources/js/angularController/historicalDataController.js"></script>
		<script type="text/javascript"
			src="resources/js/angularService/services.js"></script>
		<script type="text/javascript">
			var app = angular.module("historicalDataApp", []);
			var myAppConstructor = angular.module("historicalDataApp");
			myAppConstructor.controller("historicalDataController", historicalDataController);
			myAppConstructor.service('allServices', allServices);
		</script>
		<script type="text/javascript"
			src="resources/js/angularDirective/directive.js"></script>
		<script type="text/javascript">
			$("#msgBox").show().delay(2000).fadeOut(400);
		</script>
		<script>
		$(document).ready(
				function() {
					$('[data-toggle="tooltip"]').tooltip();
				});
		</script>
</body>

</html>