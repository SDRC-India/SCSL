<!-- 
@author Laxman (laxman@sdrc.co.in)
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="SncuDataEntryApp">
<head>

<title>SCSL-Historical Data Entry</title>
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
<!--[if lte IE 12]>
  <link rel="stylesheet" type="text/css" media="screen, projection" href="resources/css/ie12-down.css" />
<![endif]-->

<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>
<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
	var="bootstrapjs" />
<script src="${bootstrapjs}"></script>
<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
	var="angularmin" />
<script src="${angularmin}" type="text/javascript"></script>
<style>

.legacy-label{
	text-align: justify;
}
.col-md-offset-2 {
    margin-left: 14.666667%;
}
.select-container {
	width: 450px;
}
@media (max-width: 1024px){	
	 .select-container {
         margin: 0px; 
	}	
}
@media (max-width: 768px){
	.select-container {
    	width: 200px;
	}
	.legacy-cont{
	 	text-align: center;	 
	}	
}

@media (max-width: 767px){
	.pageNameContainer {
	    right: 50%;     
	 }
	 .legacy-cont{
	 	text-align: center;
	 	margin-bottom: 75px;
	 }
	 .select-container {
    	width: 200px;
	}
}
@media (max-width: 540px){
.reset-button {
    width: 0px !important; 
}
</style>

</head>

<body ng-controller="sncuDataEntryController" class="xoverflowHidden" ng-cloak>
<style type="text/css">
section.bottomfooter {position: fixed; !important;}</style>
	<jsp:include page="fragments/header.jsp"></jsp:include>

	<div id ="mymain" class="container legacy-cont">
		<div class="pageNameContainer">
			<h4>{{pageName}}</h4>
		</div>
				
		<div class="col-md-12 historicalDataSection">

			<div class="select-container dist-list report-margin">
			<label class="col-md-4 legacy-label">State:</label>
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Select state *" id="state"
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
			<div class="select-container dist-list report-margin">
			<label class="col-md-4 legacy-label">District:</label>
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Select district *" id="district"
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
	
		</div>
		<div class="col-md-12 reportSection1">
		<div class="select-container dist-list report-margin">
			<label class="col-md-4 legacy-label">Facility:</label>
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Select facility *" id="facility" data-toggle="tooltip"
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
			
			<div class="select-container dist-list report-margin">
			<label class="col-md-4 legacy-label">Time Period:</label>
				<div class="input-group" style="margin: auto;">
					<input type="text" placeholder="Select time period *" id="date"
						class="form-control not-visible-input" name="date" readonly=""
						ng-model="selectedDate.timePeriod">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu corearea-dropdown1" role="menu">
							<li ng-repeat="date in timePeriod"
								ng-click="selectDate(date);"><a href="">{{date.timePeriod}}</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		
				<div class="col-md-12 col-sm-12 report-button-section text-center">
				  <div class="col-md-10 col-md-offset-1 legacy-btns">
				   <div class="col-md-7 col-sm-6 col-xs-6 text-right">
					<button class="downloadReport " ng-click="validateHistoricalDataSelection()">View</button>
					</div>
					
					<div class="col-md-4 col-sm-4 col-xs-4 text-left">
						<!-- <div class="input-group-btn" data-toggle="tooltip" data-original-title="Reset" 
						data-placement="top">
							<button data-toggle="dropdown" ng-click="resetDiv()" 
								class="btn btn-color dropdown-toggle" type="button">
								<i class="fa fa-undo"></i>
							</button>
						</div> -->
						<button class="downloadReport " ng-click="resetDiv()">Reset</button>
					</div>
				 </div>
				</div>
		
				
		<section ng-show="selectionValidated" class="profile-section col-md-12 col-sm-12 col-xs-12" style="margin-top:-20px">
		<div id="bandDiv">
			<div class="text-center input-group"
				style="margin: auto; font-size: 30px;">
				{{selectedFacilityDataEntry.areaName}} ({{selectedDateDataEntry.timePeriod}})</div>
			<div class="text-center alreadySubmitted"
				ng-class="allIndicatorsModel.cssClass"
				ng-show="allIndicatorsModel.statusMessage != null">{{alreadySubmitted}}</div>
			<div class="submit-remark" ng-if="superRemark">{{superRemark}}</div>
		</div>
		<div style="margin-top: 40px;"></div>
			<div class="profile-entry">
				<a href="#profileTable" class="accordion-trigger expanded" title="open/close profile entry form" data-toggle="tooltip"  data-accord-group="group1">Profile Entry</a>
				<div id="profileTable" class="accordion-content expanded">	
					<div class=" table-responsive">
						<table items="tableData" show-filter="true" cellpadding="0"
							cellspacing="0" border="0"
							class="dataTable table table-bordered sncu" id="dataTable">
							<thead>
								<tr>
									<th style="position: relative; text-align: center;">Sl.&nbsp;No.</th>
									<th style="position: relative;">Indicator Name</th>
									<th style="position: relative; text-align: center;">Value</th>
								</tr>
							</thead>
							<tbody >
								<tr>
									<td>1</td>
									<td style="text-align: left !important">Labor room available</td>
									<td class="inputBox"  >
										<input style="font-size: 18px;" hide-header-footer type='text' class='tableInput' ng-model="selectedFacilityHasDataEntry"
										ng-disabled="true">
									</td>
								</tr>
								<tr
									ng-repeat="indicator in profileTableRows">
									<td>{{$index+2}}</td>
									<td style="text-align: left !important">{{profileTableData[indicator][0].indicatorName}}</td>
									<td class="inputBox {{rowData.cssClass}}" ng-class="{'live-births':profileTableData[indicator][0].indicatorId == 43, 'total-deliveries':profileTableData[indicator][0].indicatorId == 40, 'warned': profileTableData[indicator][0].denominatorCss == 'warned'}">
										<input only-seven-digits hide-header-footer type='text' class='tableInput'
										ng-blur="profileEntryDataPopulation(profileTableData[indicator][0]);isNOLBgreaterTD(profileTableData[indicator][0])"
										ng-model='profileTableData[indicator][0].percentage' 
										ng-change="calculateTotalAndPercentage(profileTableData[indicator][0]); " 
										ng-disabled="!profileTableData[indicator][0].isEnable || profileFormDisabled || profileTableData[indicator][0].indicatorId == 36
										 || profileTableData[indicator][0].indicatorId == 37 || profileTableData[indicator][0].indicatorId == 44
										  || profileTableData[indicator][0].indicatorId == 41 || profileTableData[indicator][0].indicatorId == 42 || profileTableData[indicator][0].indicatorId == 40">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="text-center">
							<button class="saveProfile" ng-show="!profileDataSaved" 
							ng-disabled=" (!(profileTableData['35'][0].percentage 
							&& (profileTableData['39'] ? profileTableData['39'][0].percentage: true) && (profileTableData['40'] ? profileTableData['40'][0].percentage: true) 
							&& (profileTableData['44'] ? profileTableData['44'][0].percentage: true)))"
								ng-click="confirmSave()">PROCEED</button>
							<button class="resetProfile" 
							ng-disabled="(!(profileTableData['34'][0].percentage || profileTableData['35'][0].percentage || profileTableData['39'][0].percentage || profileTableData['40'][0].percentage || profileTableData['44'][0].percentage))"
								ng-click="confirmReset()">RESET</button>
						</div>	
				</div>		
		</div>	
		</section>
		<section ng-show="pageLoaded && (profileDataSaved)" class="formTable col-md-12 col-sm-12 col-xs-12">
			<div class='tabs col-md-12 col-sm-12 col-xs-12'>
				
				<input type='radio' id='r1' name='t' checked><label for='r1'>PROCESS</label>
				<div class='content'>
					<div class="addIndicatorContainer" ng-click="openAddIndicatorModal()">
						<div class="addIndicator">
							<h5>Add Indicator</h5>
						</div>
					</div>
					<div class=" table-responsive">
						<table items="tableData" show-filter="true" cellpadding="0"
							cellspacing="0" border="0"
							class="dataTable table sncu" id="dataTable">
							<thead>
								<tr>
									<th style="position: relative;">Sl.&nbsp;No.</th>
									<th style="position: relative;">Focus&nbsp;Area</th>
									<th style="position: relative;">Indicator Name</th>
									<th style="position: relative;border-width: 1px 0px 1px;">Numerator</th>
									<th style="position: relative; text-align: center;background-color: #428ead;border: 1px solid #428ead;">Value</th>
									<th style="position: relative;border-width: 1px 0px 1px;">Denominator</th>
									<th style="position: relative; text-align: center;background-color: #428ead;border: 1px solid #428ead;">Value</th>
									<th style="position: relative;border-bottom: 1px solid #214757;">Percentage</th>
								</tr>
							</thead>
							<tbody >
								<tr
									ng-repeat="rowData in formTableData.Process | orderBy:filterType:sortReverse">
									<td >{{$index+1}}</td>
									<td >{{rowData['coreAreaName']}}</td>
									<td >{{rowData['indicatorName']}}</td>
									<td style="border-width: 1px 0px 1px;">{{rowData['numeratorName']}}</td>
									<td style="border: 1px solid #a4cada;border-bottom: 1px solid #909798;" class="inputBox" ng-class="{'warned':rowData.numeratorCss == 'warned', 'noLrDIsabled': !rowData.isLr}" id="{{'numerator' + $index}}" ><input ng-blur="validateInput(rowData, 'numeratorValue', 'Process', 'numerator' + $index)" only-seven-digits hide-header-footer type='text' class='tableInput' ng-model='rowData.numeratorValue' 
									ng-disabled="!rowData.isEnable || rowData.profileBaseNumeratorDisable || !rowData.isLr" ng-change="calculatePercentage(rowData)"></td>
									<td style="border-width: 1px 0px 1px;">{{rowData['denominatorName']}}</td>
									<td style="border: 1px solid #a4cada;border-bottom: 1px solid #909798;" ng-class="{'warned':rowData.denominatorCss == 'warned', 'noLrDIsabled': !rowData.isLr}" class="inputBox" id="{{'denominator' + $index}}" ><input ng-blur="validateInput(rowData, 'denominatorValue', 'Process', 'denominator' + $index)" only-seven-digits hide-header-footer type='text' class='tableInput' ng-model='rowData.denominatorValue' 
									ng-disabled="!rowData.isEnable || rowData.profileBaseDenominatorDisable || !rowData.isLr" ng-change="calculatePercentage(rowData)"></td>
									<td class="inputBox" ng-class="{'noLrDIsabled': !rowData.isLr}" style="background-color: rgba(33, 71, 87, 0.65);"><input type='text' readonly class='tableInput' ng-model='rowData.percentage'></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<input type='radio' id='r2' name='t'><label for='r2'>INTERMEDIATE</label>
				<div class='content'>
					<div class=" table-responsive">
						<table items="tableData" show-filter="true" cellpadding="0"
							cellspacing="0" border="0"
							class="dataTable table table-bordered sncu" id="dataTable">
							<thead>
								<tr>
									<th style="position: relative;">Sl.&nbsp;No.</th>
									<th style="position: relative;">Focus&nbsp;Area</th>
									<th style="position: relative;">Indicator Name</th>
									<th style="position: relative;border-width: 1px 0px 1px;">Numerator</th>
									<th style="position: relative; text-align: center;background-color: #428ead;border: 1px solid #428ead;">Value</th>
									<th style="position: relative;border-width: 1px 0px 1px;">Denominator</th>
									<th style="position: relative; text-align: center;background-color: #428ead;border: 1px solid #428ead;">Value</th>
									<th style="position: relative;border-bottom: 1px solid #214757;">Percentage</th>
								</tr>
							</thead>
							<tbody >
								<tr
									ng-repeat="rowData in formTableData.Intermediate | orderBy:filterType:sortReverse">
									<td >{{$index+1}}</td>
									<td >{{rowData['coreAreaName']}}</td>
									<td >{{rowData['indicatorName']}}</td>
									<td style="border-width: 1px 0px 1px;">{{rowData['numeratorName']}}</td>
									<td style="border: 1px solid #a4cada;border-bottom: 1px solid #909798;" ng-class="{'warned':rowData.numeratorCss == 'warned', 'noLrDIsabled': !rowData.isLr}" class="inputBox" id="{{'numeratorInt' + $index}}" ><input ng-blur="validateInput(rowData, 'numeratorValue', 'Intermediate', 'numeratorInt' + $index)" only-seven-digits hide-header-footer type='text' class='tableInput' ng-model='rowData.numeratorValue' 
									ng-disabled="!rowData.isEnable || rowData.profileBaseNumeratorDisable || !rowData.isLr" ng-change="calculatePercentage(rowData, 'Intermediate')"></td>
									<td style="border-width: 1px 0px 1px;">{{rowData['denominatorName']}}</td>
									<td style="border: 1px solid #a4cada;border-bottom: 1px solid #909798;" ng-class="{'warned':rowData.denominatorCss == 'warned', 'noLrDIsabled': !rowData.isLr}" class="inputBox" id="{{'denominatorInt' + $index}}" ><input ng-blur="validateInput(rowData, 'denominatorValue', 'Intermediate', 'denominatorInt' + $index)" only-seven-digits hide-header-footer type='text' class='tableInput' ng-model='rowData.denominatorValue' 
									ng-disabled="!rowData.isEnable || rowData.profileBaseDenominatorDisable || !rowData.isLr" ng-change="calculatePercentage(rowData, 'Intermediate')"></td>
									<td class="inputBox" ng-class="{'noLrDIsabled': !rowData.isLr}" style="background-color: rgba(33, 71, 87, 0.65);" ><input type='text' ng-init="!rowData.numeratorValue || !rowData.denominatorValue ? rowData.percentage = 'N/A':''" readonly class='tableInput' ng-model='rowData.percentage'></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<input type='radio' id='r3' name='t'><label for='r3'>OUTCOME</label>
				<div class='content'>
					<div class=" table-responsive">
						<table items="tableData" show-filter="true" cellpadding="0"
							cellspacing="0" border="0"
							class="dataTable table table-bordered sncu" id="dataTable">
							<thead>
								<tr>
									<th style="position: relative;">Sl.&nbsp;No.</th>
<!-- 									<th style="position: relative;">Core Area</th> -->
									<th style="position: relative;">Indicator Name</th>
									<th style="position: relative;border-width: 1px 0px 1px;">Numerator</th>
									<th style="position: relative; text-align: center;background-color: #428ead;border: 1px solid #428ead;">Value</th>
									<th style="position: relative;border-width: 1px 0px 1px;">Denominator</th>
									<th style="position: relative; text-align: center;background-color: #428ead;border: 1px solid #428ead;">Value</th>
									<th style="position: relative;border-bottom: 1px solid #214757;">Percentage</th>
								</tr>
							</thead>
							<tbody >
								<tr
									ng-repeat="rowData in formTableData.Outcome | orderBy:filterType:sortReverse">
									<td >{{$index+1}}</td>
<!-- 									<td >{{rowData['coreAreaName']}}</td> -->
									<td >{{rowData['indicatorName']}}</td>
									<td style="border-width: 1px 0px 1px;">{{rowData['numeratorName']}}</td>
									<td style="border: 1px solid #a4cada;border-bottom: 1px solid #909798;" ng-class="{'warned':rowData.numeratorCss == 'warned', 'noLrDIsabled': !rowData.isLr}" class="inputBox" id="{{'numeratorOut' + $index}}"><input ng-blur="validateInput(rowData, 'numeratorValue', 'Outcome', 'numeratorOut' + $index)" type='text' only-seven-digits hide-header-footer class='tableInput' ng-model='rowData.numeratorValue' 
									ng-disabled="!rowData.isEnable || rowData.profileBaseNumeratorDisable || !rowData.isLr" ng-change="calculatePercentage(rowData)"></td>
									<td style="border-width: 1px 0px 1px;" >{{rowData['denominatorName']}}</td>
									<td style="border: 1px solid #a4cada;border-bottom: 1px solid #909798;" ng-class="{'warned':rowData.denominatorCss == 'warned', 'noLrDIsabled': !rowData.isLr}" class="inputBox" id="{{'denominatorOut' + $index}}" ><input ng-blur="validateInput(rowData, 'denominatorValue', 'Outcome', 'denominatorOut' + $index)" type='text' only-seven-digits hide-header-footer class='tableInput' ng-model='rowData.denominatorValue' 
									ng-disabled="!rowData.isEnable || rowData.profileBaseDenominatorDisable || !rowData.isLr" ng-change="calculatePercentage(rowData)"></td>
									<td class="inputBox" ng-class="{'noLrDIsabled': !rowData.isLr}" style="background-color: rgba(33, 71, 87, 0.65);" ><input type='text' readonly class='tableInput' ng-model='rowData.percentage'></td>
								</tr>
							</tbody>
						</table>
						<div class="text-center">
							<button class="submitSNCU" ng-click="submitForm()">SUBMIT</button>
						</div>
					</div>
				</div>
				<div id='slider'></div>
			</div>
		</section>
	</div>
		<!-- popup modal -->
	<div id="addIndicatorModal" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content" style="border-radius: 0;">
				<!-- <button type="button" class="custom-close close" data-dismiss="modal">&times;</button> -->
				<div class="modal-body text-center">
					<h4 class="text-center">Add Indicator</h4>
					<h5><b>Number of selected Indicators : </b><span style="color: #4d7dde;">Asphyxia( {{numberSelectedIndicators.Asphyxia}} ), Sepsis( {{numberSelectedIndicators.Sepsis}} ), Prematurity( {{numberSelectedIndicators.Prematurity}} )</span> </h5>
					<div class="select-container addIndicatorCustom dist-list text-center">
						<div class="input-group" style="margin: auto;">
							<input type="text" placeholder="Focus Area *" id="coreArea"
								class="form-control not-visible-input" name="coreArea" readonly=""
								ng-model="selectedIndicatorCoreArea">
							<div class="input-group-btn" style="position: relative;">
								<button data-toggle="dropdown" style="margin-top: 0;color: #FFF;background-color: #46803D;"
									class="btn btn-color dropdown-toggle" type="button">
									<i class="fa fa-list"></i>
								</button>
								<ul class="dropdown-menu" role="menu">
									<li ng-repeat="indicatorCoreArea in indicatorCoreAreas"
										ng-click="selectIndicatorCoreArea(indicatorCoreArea);"><a href="">{{indicatorCoreArea}}</a></li>
								</ul>
							</div>
						</div>
					</div>
					<div class="table-responsive" style="margin-top: 35px;">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th class="text-center">Sl.&nbsp;No.</th>
									<th>Indicator</th>
									<th>Select</th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="indicator in restIndicators[selectedIndicatorCoreArea]">
									<td class="text-center">{{$index+1}}</td>
									<td class="text-left">{{indicator.value}}</td>
									<td><input type="checkbox" ng-click="countSelectedIndicators(restIndicators[selectedIndicatorCoreArea][$index].isSelected, selectedIndicatorCoreArea)" ng-model="restIndicators[selectedIndicatorCoreArea][$index].isSelected">{{restIndicators[selectedIndicatorCoreArea][$index].isEnable}}</td>
								</tr>
							</tbody>
							
						</table>
					</div>
					<!-- <div class="text-center">
							<button class="submitSNCU" style="margin-bottom: 10px;"
								ng-click="confirmAddingIndicator()">SUBMIT</button>
						</div> -->
					<div class="text-center">
							<button type="button" class="btn errorOk" data-dismiss="modal">Cancel</button>
							<button type="button" class="btn errorOk" ng-click="confirmAddingIndicator()">Submit</button>
					</div>	
				</div>
			</div>

		</div>
	</div>
	<!-- Modal for confirm adding indicator -->
	<div id="confirmAddIndicator" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_info_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INDICATORS SELECTED</div>
					<ul>
						<li ng-repeat="indicator in allSelectedIndicators">{{indicator.value}} (&nbsp;<b>{{indicator.description}}</b>&nbsp;)</li>
					</ul>
					<div class="impNote">
						* Indicators once added can not be removed.<br>
						* New indicator addition will erase all prefetch data from Process, Intermediate and Outcome section.
					</div>
					<div class="text-center">
							<!-- <button class="submitSNCU" style="margin-top: 20px;margin-bottom: 10px;" ng-click="addIndicator()">Confirm</button>
							<button class="submitSNCU" style="margin-top: 20px;margin-bottom: 10px;" data-dismiss="modal">Cancel</button> -->
							<button type="button" class="btn errorOk" data-dismiss="modal">Cancel</button>
							<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="addIndicator()">Confirm</button>
						</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal for error message -->
	<div id="errorMessage" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody">{{errorMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<!--end of thematic and chklist  -->
	<!-- Modal for warning message -->
	<div id="warningMessage" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="warnhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; WARNING</div>
					<div class="warnbody">{{warningMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<div id="infoMessage" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_info_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody">{{infoMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal for warning message -->
	<div id="warningMessageSncuSubmit" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="warnhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; WARNING</div>
					<div class="warnbody">{{warningMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="proceedtoSubmitWithBlankIntermediateForm()">Proceed To Submit</button>
				</div>
			</div>
		</div>
	</div>
	<!--end of thematic and chklist  -->
	<!-- Modal for warning message -->
	<div id="confirmResetModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody">{{warningMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="resetForm()">Confirm</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal for warning message -->
	<div id="confirmSaveModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody">{{warningMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="saveProfileForm()">Confirm</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="confirmModifyProfileModal" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="infohead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; INFO</div>
					<div class="warnbody">{{warningMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Cancel</button>
					<button type="button" class="btn errorOk" data-dismiss="modal" ng-click="resetFormTable();saveProfileForm()">Confirm</button>
				</div>
			</div>
		</div>
	</div>
	<!-- popup modal -->
	<div id="pop" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="successhead"><img alt="" src="resources/images/icons/Messages_success_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; SUCCESS</div>
					<div class="successbody">{{msg}}</div>
					<a class="btn btn-default" ng-href="{{link}}">Ok</a>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="fragments/footer.jsp"></jsp:include>
	<script type="text/javascript"
		src="resources/js/quick-accord.js"></script>
	<script type="text/javascript"
		src="resources/js/angularController/legacySncuDataEntryController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
		var app = angular.module("SncuDataEntryApp", []);
		var myAppConstructor = angular.module("SncuDataEntryApp");
		myAppConstructor.controller("sncuDataEntryController",
				sncuDataEntryController);
		myAppConstructor.service('allServices', allServices); 
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directive.js"></script>
	<script type="text/javascript">
		$(".slideMenu").css("height", $(window).height()-100);
		if($(window).width() <= 1024){
			$(".slideMenu").css("height", "auto");
		}
		
		$("#msgBox").show().delay(2000).fadeOut(400);
		$(".accordion-trigger").QuickAccord();
	</script>
	<script type="text/javascript">
	$(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip(); 
	});
	</script>
</body>

</html>