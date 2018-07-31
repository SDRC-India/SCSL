<!-- 
@author Devikrushna (devikrushna@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="addpdsaApp">
<head>

<title>SCSL-Add pdsa</title>
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
<spring:url value="/resources/css/bootstrap-datetimepicker.css" var="datetimepickerCss" />
<link href="${datetimepickerCss}" rel="stylesheet" />

<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>

<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
	var="angularmin" />
<script src="${angularmin}" type="text/javascript"></script>
<spring:url value="/webjars/jquery-ui/1.10.3/themes/base/jquery-ui.css"
	var="jQueryUiCss" />
<link href="${jQueryUiCss}" rel="stylesheet"></link>
<style type="text/css">
.ui-widget-content{
	padding: 10px 0;
    border: none;
    box-shadow: rgb(204, 204, 204) 0px 0px 3px 1px;
    color: rgb(85, 85, 85) !important;
    font-size: 15px !important;
    display: none;
    z-index: 99999 !important;
    /* width: auto !important; */
    width: 378px !important;
    font-family: Josefin Sans;
    cursor: pointer;
    background: none;
    background-color: rgb(243, 243, 243);
}
.ui-menu-item div:hover{
	border: none;
}
.ui-menu-item-wrapper{
	padding: 7px 10px;

}
.ui-widget-content:hover .ui-menu-item div, .ui-widget-content:not(:hover) .ui-menu-item div{
	border: none !important;
}
.ui-datepicker-calendar thead th {
    color: #555 !important;
}
#ui-datepicker-div{
	max-width: 250px;
	padding: 0;
}
</style>
</head>

<body ng-controller="addPdsaController" ng-cloak>

<jsp:include page="fragments/header.jsp"></jsp:include>

	<div id="mymain" class="container">
		<div class="pageNameContainer">
			<h4>{{pageName}}</h4>
		</div>
		<div class="addpdsaSelection text-center">
			<h5 class="addpdsaBorder">ADD PDSA</h5>
		</div>
		<div class="container">
			<div class="row">
				<div class="col-md-12 pdsa-main-margin">
					<div class="col-md-12 col-sm-6 col-xs-6 pdsa-margin ">
						<label class="col-md-4 pdsa-margin1" for="textinput"> Focus
							Area </label>
						<div class="col-md-4  select-container">
							<div class="input-group select-container3"
								ng-class="{'hideTooltip': !selectedArea.split('_')[0]}">
								<input type="text" placeholder="Focus Area *" id="area"
									data-toggle="tooltip"
									data-original-title="{{selectedArea.split('_')[0]? selectedArea.split('_')[0]:''}}"
									data-placement="top" class="form-control" name="area"
									readonly="" ng-model="selectedArea.split('_')[0]" required>
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu corearea-dropdown" role="menu">
										<li ng-repeat="area in coreAreaList"
											ng-click="selectArea(area);"><a href="">{{area.split('_')[0]}}</a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-12 col-sm-12 pdsa-margin ">
						<label class="col-md-4 col-sm-12 col-xs-12 pdsa-margin1"
							for="textinput"> Indicator </label>
						<div class="col-md-8 select-container indicator-width" style="width: 503px;">
							<div class="input-group select-container1"
								ng-class="{'hideTooltip': !selectedIndicator.value}">
								<input type="text" placeholder="Indicator *" id="indicator"
									data-toggle="tooltip"
									data-original-title="{{selectedIndicator.value? selectedIndicator.value:''}}"
									data-placement="top" class="form-control " name="indicator"
									readonly="" ng-model="selectedIndicator.value" required>
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu indicator-dropdown" role="menu">
										<li ng-repeat="indicator in coreIndicatorList"
											ng-click="selectIndicator(indicator);"><a href="">{{indicator.value}}</a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-12 col-sm-12 col-xs-12 pdsa-margin ">
						<label class="col-md-4 col-sm-12 col-xs-12 pdsa-margin1" for="textinput">
							Change Idea </label>
						<div class="col-md-8 select-container indicator-width" style="width: 503px;">
							<div class="input-group select-container1"
								ng-class="{'hideTooltip': !selectedChangeIdea.value}">
								<input type="text" placeholder="Change Idea *" id="idea"
									required class="form-control " name="idea" 
									data-toggle="tooltip" readonly
									data-original-title="{{selectedChangeIdea.value? selectedChangeIdea.value:''}}"
									data-placement="top" ng-model="selectedChangeIdea.value">
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu indicator-dropdown" role="menu">
										<li ng-repeat="idea in changeIdeaList"
											ng-click="selectIdea(idea);"><a href="">{{idea.value}}</a></li>
									</ul>
								</div>
							</div>
						
						</div>
						<!-- <div class="col-md-2 col-sm-12 col-xs-12"> -->
							<button class="pdsa-add" ng-show="addIdea"
								ng-click="openIdeamodal()">+ Add</button>
						<!-- </div> -->
					</div>
					<div class="col-md-12 col-sm-12 col-xs-12 pdsa-margin ">
						<label class="col-md-4 col-sm-12 col-xs-12 pdsa-margin1" for="textinput">
							PDSA Number </label>
						<div class="col-md-4 col-sm-12 col-xs-12">
							<div class="input-group pdsaNo" style="margin: auto;">
								<input class="pdsa-input1" type="text"
									placeholder="PDSA Number *" id="pdsano" name="pdsano" readonly=""
									style="" ng-model="pdsanumber" ng-disabled='disabled'>
							</div>
						</div>
					</div>
					<div class="col-md-12 col-sm-6 pdsa-margin ">
						<label class="col-md-4 pdsa-margin1" for="textinput"> PDSA
							Name </label>
						<div class="col-md-4">
							<div class="input-group pdsaname" style="margin: auto;">
								<input class="pdsa-input1 pdsa-name-mobile" max-three-hundred hide-header-footer 
									type="text" required placeholder="PDSA Name *" id="name"
									name="idea" ng-model="pdsaName">
							</div>
						</div>
					</div>
					<div class="col-md-12 col-sm-12 pdsa-margin ">
						<label class="col-md-4 col-sm-12 pdsa-margin1" for="textinput">
							Brief Summary </label>
						<div class="col-md-4">
							<div class="input-group pdsabrief" style="margin: auto;">
								<textarea class="pdsa-textarea1 textarea-length" max-three-hundred
									ng-change="calculateCharLeft(pdsaSummary);" required type="text"
									placeholder="Brief Summary *" id="summery" name="summery"
									hide-header-footer ng-model="pdsaSummary"
									style="resize: none;"></textarea>
								<div style="text-align: right;" class="charLeft">Characters
									left: {{300-pdsaSummary.length}}</div>
							</div>
						</div>
					</div>
					<div class=" col-md-12 col-sm-12 pdsa-margin frequency-data-collection">
						<label class="col-md-4 col-sm-12 col-xs-12 pdsa-margin1"
							for="textinput">Frequency of data collection</label>
						<div class="col-md-4 select-container">
							<div class="input-group select-container3">
								<input type="text" placeholder="Frequency of data collection *" only-digits
									id="idea" class="form-control" required name="idea" 
									onkeyup="this.value = maxHundred(this.value,1, 366)" maxlength="3"
									ng-model="selectedFrequency.value" ng-change="resetStartDate()">
								<div class="input-group-btn" style="position: relative;">
									<!-- <button data-toggle="dropdown"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-list"></i>
									</button>
									<ul class="dropdown-menu corearea-dropdown" role="menu">
										<li ng-repeat="frequency in frequencyList"
											ng-click="selectFrequency(frequency); "><a href="">{{frequency.value}}</a></li>
									</ul> -->
									days
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="input-group" style="margin: auto;">
								<input class="pdsa-frequency"
									ng-model="monthNumber"
									onkeyup="this.value = minmax(this.value,1, 99)" only-digits type="text"
									regex="^(\d?[1-9]|[1-9]0)$" maxlength="2"
									ng-change="calculateEndDate()" ng-change="resetStartDate()"
									placeholder="Number of PDSA entries *" id="name"
									name="idea">
							</div>
						</div>
					</div>
					<div class="col-md-12 col-sm-6 pdsa-margin">
						<label class="col-md-4 col-xs-12 pdsa-margin1" for="textinput">
							Start/End Date </label>
						<div class="col-md-4 select-container" style="margin-right: 40px;">
							<div class="input-group select-container3">
								<input type="text" placeholder="Start Date *" required
									id="datepicker" ng-model="startDate"
									class="form-control not-visible-input"
									ng-change="calculateEndDate()" name="idea" readonly>
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown" id="icondatepicker"
										class="btn btn-color dropdown-toggle" type="button">
										<i class="fa fa-calendar"></i>
									</button>
								</div>
							</div>
						</div>
						<div class="col-md-4 select-container">
							<div class="input-group select-container3">
								<input type="text" placeholder="End Date *" required
									id="datepicker1" ng-model="endDatePDSA" ng-disabled="true"
									class="form-control not-visible-input" name="idea"
									readonly>
								<div class="input-group-btn" style="position: relative;">
									<button data-toggle="dropdown" id="icondatepicker1"  ng-disabled="true"
										class="btn btn-color dropdown-toggle" type="button"
										>
										<i class="fa fa-calendar"></i>
									</button>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-12 col-sm-12 pdsa-margin text-center">
						<button class="submitEngagementScore pdsa-button"
							ng-click="validatePdsaForm()">SUBMIT</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- add idea modal -->

	<div id="addIdea" class="modal fade" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content idea-modal">
				<div class="modal-body">
					<div class="">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close" style="margin-top: -30px;">
							<span aria-hidden="true" style="font-size: 42px;">×</span>
						</button>
						<div>
							<h5 class="idea-modal-header">New Change Idea</h5>
							<div style="border-bottom: 1px solid gray;"></div>
							<h5 class="" style="margin-bottom: 45px;">
								<span style="font-weight: bold;">Indicator Name: </span>&nbsp;{{selectedIndicator.value}}
							</h5>
						</div>
						<div class="row">
							<div class="col-md-12 text-center">
								<div class="input-group col-md-8 col-md-offset-2 "
									style="margin: auto;">
									<textarea class="ideamodal-input modal-text-area" max-three-hundred hide-header-footer
										ng-change="calculateCharLeft(changeIdeaDescription); getAutoCompleteChangeIdea()" required
										placeholder="Change Idea *" id="changeidea" type="text"
										name="changeidea" ng-model="changeIdeaDescription"
										></textarea>

									<div style="text-align: right;" class="charLeft">Characters
										left: {{300-changeIdeaDescription.length}}</div>
								</div>
							</div>
							<div class="col-md-12 text-center">
								<button class="ideamodal-submit" ng-click="saveChangeIdealist()">Submit</button>
							</div>
						</div>
					</div>
				</div>
			</div>
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
	<!-- End of error message -->
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
	<!-- End of warning message -->
	<!-- infoMessage  Modal  -->
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
	<!--end of infoMessage   -->
	<!-- Modal for warning message -->
	<div id="warnLargeFrequency" class="modal confrirmation-modal fade" role="dialog"
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
					<button type="button" class="btn errorOk" ng-click="submitPdsa()" >Ok</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" >Cancel</button>
				</div>
			</div>
		</div>
	</div>
	<!-- End of warning message -->
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
					<a class="btn btn-default" ng-href="{{link}}">Ok</a>
				</div>
			</div>
		</div>
	</div>
	<!-- change idea success modal -->
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
					<div class="successbody">{{msg}}</div>
					<a class="btn btn-default" ng-click="successidea()">Ok</a>
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
		src="resources/js/angularController/addPdsaController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
		var app = angular.module("addpdsaApp", []);
		var myAppConstructor = angular.module("addpdsaApp");
		myAppConstructor.controller("addPdsaController", addPdsaController);
		myAppConstructor.service('allServices', allServices);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directive.js"></script>
	<script type="text/javascript">
		$("#msgBox").show().delay(5000).fadeOut(4000);
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#button3id").on('submit', function() {
				$('#ideamodal').modal('show');
			});

		});
		$("#icondatepicker").click(function() {
			$("#datepicker").datepicker("show");
		});
		$("#icondatepicker1").click(function() {
			$("#datepicker1").datepicker("show");
		})
	</script>
	<script type="text/javascript" src="resources/js/autosizeTextarea.js"></script>
	<script type="text/javascript">
		$(function() {
			$('.textarea-length').autosize({
				append : "\n"
			});
		});
	</script>
	<script>
		$(document).ready(function() {

			$('[data-toggle="tooltip"]').tooltip();
			
		});
	</script>
	
</body>

</html>