<!-- 
@author Laxman (laxman@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="submissionApp">
<head>

<title>SCSL-Submission</title>
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
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular-sanitize.js"></script>
</head>

<body ng-controller="SubmissionController" ng-cloak>

	<jsp:include page="fragments/header.jsp"></jsp:include>
	<!-- <div class="pageNameContainer">
		<h4>{{pageName}}</h4>
	</div> -->
	<div id="mymain" class="container">
		<div class="pageNameContainer">
			<h4>{{pageName}}</h4>
		</div>

		<section class="submissionTableSection col-md-12">
			<div class="engagementSelection text-center" ng-if="role == 3">
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
						<input type="text" placeholder="Facility " id="facility"
							class="form-control not-visible-input" name="district"
							readonly="" ng-model="selectedFacility.areaName">
						<div class="input-group-btn" style="position: relative;">
							<button data-toggle="dropdown"
								class="btn btn-color dropdown-toggle" type="button">
								<i class="fa fa-list"></i>
							</button>
							<ul class="dropdown-menu" role="menu">
								<li ng-repeat="facility in selectedDistrict.children"
									ng-click="selectFacility(facility);"><a href="">{{facility.areaName}}</a></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="reset-button text-center">
					<div class="input-group-btn" style="position: relative;" data-toggle="tooltip" data-original-title="Reset" 
					data-placement="top">
						<button data-toggle="dropdown" ng-click="resetDiv()"  
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-undo"></i>
						</button>
					</div>
				</div>
				
			</div>
			<div class="horizontal-legends">

				<ul>
					<li class="legend_list ng-scope"><span
						class="yellow legnedblock"> </span><span class="legend_key ">Pending</span></li>
					<li class="legend_list "><span class="pink legnedblock">
					</span><span class="legend_key ng-binding">Rejected</span></li>
					<li class="legend_list "><span class="blue legnedblock">
					</span><span class="legend_key ng-binding">Approved</span></li>
					<li class="legend_list "><span class="auto-approve legnedblock">
					</span><span class="legend_key ng-binding">Auto-approved</span></li>
					<li class="legend_list "><span class="legacy legnedblock">
					</span><span class="legend_key ng-binding">Legacy</span></li>
				</ul>
			</div>
			<div class="table-responsive header-fixed-table submissionTableContainer" style="width: 100%" tableuniqueclass="'submissionTableContainer'" tabledata="indicatorList">
				<table items="tableData" show-filter="true" cellpadding="0"
					cellspacing="0" border="0"
					class="dataTable table table-striped submissionTable"
					id="dataTable">
					<thead>
						<tr>
							<!-- <th ng-repeat="column in columns | filter:removeRowId"
								ng-class="{'selectedColumn' : sortType == column}"
								ng-click="order(column)" style="position: relative;">{{column}}
								<div class="sorting1">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != column &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == column &&  sortReverse == false}"></i>
								</div>
							</th> -->
							<th style="position: relative;min-width: 85px;" nowrap>Sl.&nbsp;No.</th>
							<th ng-if="role==3" nowrap><div style="position: relative;">State
								<div class="sorting1" ng-click="order('stateName')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != 'stateName' &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'stateName' &&  sortReverse == false}"></i>
								</div>
								</div>
							</th>
							<th ng-if="role==3" nowrap><div style="position: relative;">District
								<div class="sorting1" ng-click="order('districtName')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != 'districtName' &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'districtName' &&  sortReverse == false}"></i>
								</div></div>
							</th>
							<th style="position: relative;min-width: 110px;" ng-if="role==3" nowrap>Facility
								<div class="sorting1" ng-click="order('facilityName')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != 'facilityName' &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'facilityName' &&  sortReverse == false}"></i>
								</div>
							</th>
							<th style="position: relative;min-width: 128px;" nowrap>Created Date
								<div class="sorting1" ng-click="order('uploadDate')" nowrap>
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != 'uploadDate' &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'uploadDate' &&  sortReverse == false}"></i>
								</div>
							</th>
							<th style="position: relative;min-width: 110px;" nowrap>Timeperiod
								<div class="sorting1" ng-click="order('timePeriodId')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != 'timePeriodId' &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'timePeriodId' &&  sortReverse == false}"></i>
								</div>
							</th>
							<th style="position: relative;min-width: 185px;" nowrap>Superintendent Status
								<div class="sorting1" ng-click="order('statusSuperintendent')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != 'statusSuperintendent' &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'statusSuperintendent' &&  sortReverse == false}"></i>
								</div>
							</th>
							<th style="position: relative;min-width: 170px;">M&E Status
								<div class="sorting1" ng-click="order('statusMnE')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != 'statusMnE' &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'statusMnE' &&  sortReverse == false}"></i>
								</div>
							</th>
							<th style="position: relative;min-width: 200px;" nowrap>Superintendent Remarks
								<div class="sorting1" ng-click="order('remarkBySuperintendent')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != 'remarkBySuperintendent' &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'remarkBySuperintendent' &&  sortReverse == false}"></i>
								</div>
							</th>
							<th style="position: relative;min-width: 185px;">M&E Remarks
								<div class="sorting1" ng-click="order('remarkByMnE')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != 'remarkByMnE' &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'remarkByMnE' &&  sortReverse == false}"></i>
								</div>
							</th>
							<th style="position: relative;">View</th>
							<th style="position: relative;">History</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-click="getChartView(rowData)"
							ng-repeat="rowData in indicatorList | orderBy:filterType:sortReverse">
							<td sortable="'{{rowData.column}}'">{{$index+1}}</td>
							<td sortable="'{{rowData.column}}'" ng-if="role==3">{{rowData.stateName}}</td>
							<td sortable="'{{rowData.column}}'" ng-if="role==3">{{rowData.districtName}}</td>
							<td sortable="'{{rowData.column}}'" ng-if="role==3">{{rowData.facilityName}}</td>
							<td sortable="'{{rowData.column}}'">{{rowData.uploadDate}}</td>
							<td sortable="'{{rowData.column}}'">{{rowData.timePeriod}}</td>
							<td sortable="'{{rowData.column}}'"><div
									ng-class="rowData.statusSuperintendent"
									style="width: 112px; margin: auto;">{{rowData.statusSuperintendent}}</div></td>
							<td sortable="'{{rowData.column}}'"><div
									ng-class="rowData.statusMnE"
									style="width: 112px; margin: auto;">{{rowData.statusMnE}}</div></td>
							<td sortable="'{{rowData.column}}'"><div
									style="background-color: #FFF; padding: 2px; min-height: 24px; text-align: left;">
									{{rowData.remarkBySuperintendent == 'null' ?
									'':rowData.remarkBySuperintendent | limitTo: 75}}
									<button
										style="border: none; font-weight: bold; background: none;"
										ng-click="viewReadMore($event,rowData)" class="readMore"
										ng-if="rowData.remarkBySuperintendent.length > 75">Read
										More</button>
								</div></td>
							<td sortable="'{{rowData.column}}'"><div
									style="background-color: #FFF; padding: 2px; min-height: 24px;  text-align: left;">
									
									{{rowData.remarkByMnE == 'null' ? '':rowData.remarkByMnE | limitTo: 75}}
									
									<button
										style="border: none; font-weight: bold; background: none; text-align: left;"
										ng-click="viewReadMoreMnE($event,rowData)" class="readMore"
										ng-if="rowData.remarkByMnE.length > 75">Read
										More</button>
									</div></td>
							<td sortable="'{{rowData.column}}'"><button
									class="btn submission-view"
									ng-class="{'latest' : rowData.latest}"
									ng-click="viewSubmission(rowData, true)">view</button></td>
							<td sortable="'{{rowData.column}}'"><button
									class="btn submission-view view-log"
									ng-class="{'latest' : rowData.latest}"
									ng-click="viewSubmissionLog(rowData, true)">view history</button></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div ng-show="!indicatorList.length"><h3 style="text-align: center;color: #ce8835;">No submission available</h3></div>
		</section>
		<div id="viewLogModal" class="modal fade" role="dialog"
			data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<!-- Modal content -->
				<div class="modal-content">
					<button type="button" class="custom-close close"
						data-dismiss="modal" style="margin-right: 15px; margin-top: 10px;">&times;</button>
					<div class="modal-body text-center">
						<div class="table-responsive"
							style="width: 100%; margin-top: 30px;"
							ng-if="previousIndicatorList.length">
							<table items="tableData" show-filter="true" cellpadding="0"
								cellspacing="0" border="0"
								class="dataTable table table-striped submissionTable"
								id="dataTable">
								<thead>
									<tr>
										<th style="position: relative;min-width: 85px;">Sl.&nbsp;No.
											<div class="sorting1" ng-click="order('slno')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'slno' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'slno' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;" ng-if="role==3">State
											<div class="sorting1" ng-click="order('state')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'state' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'state' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;" ng-if="role==3">District
											<div class="sorting1" ng-click="order('district')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'district' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'district' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;min-width: 110px;" ng-if="role==3">Facility
											<div class="sorting1" ng-click="order('facility')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'facility' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'facility' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;min-width: 128px;">Created Date
											<div class="sorting1" ng-click="order('date')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'date' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'date' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;min-width: 110px;">Timeperiod
											<div class="sorting1" ng-click="order('timePeriodId')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'timePeriodId' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'timePeriodId' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;min-width: 185px;">Superintendent Status
											<div class="sorting1"
												ng-click="order('superintendentStatus')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'superintendentStatus' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'superintendentStatus' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;min-width: 170px;">M&E Status
											<div class="sorting1" ng-click="order('mneStatus')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'mneStatus' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'mneStatus' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;min-width: 200px;">Superintendent Remarks
											<div class="sorting1" ng-click="order('remarkSuperitendent')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'remarkSuperitendent' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'remarkSuperitendent' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;min-width: 185px;">M&E Remarks
											<div class="sorting1" ng-click="order('remarkME')">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != 'remarkME' &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == 'remarkME' &&  sortReverse == false}"></i>
											</div>
										</th>
										<th style="position: relative;">View</th>
									</tr>
								</thead>
								<tbody>
									<tr ng-click="getChartView(rowData)"
										ng-repeat="rowData in previousIndicatorList | orderBy:filterIndicatorType:sortIndReverse">
										<td sortable="'{{rowData.column}}'">{{$index+1}}</td>
										<td sortable="'{{rowData.column}}'" ng-if="role==3">{{rowData.stateName}}</td>
										<td sortable="'{{rowData.column}}'" ng-if="role==3">{{rowData.districtName}}</td>
										<td sortable="'{{rowData.column}}'" ng-if="role==3">{{rowData.facilityName}}</td>
										<td sortable="'{{rowData.column}}'">{{rowData.uploadDate}}</td>
										<td sortable="'{{rowData.column}}'">{{rowData.timePeriod}}</td>
										<td sortable="'{{rowData.column}}'"><div
												ng-class="rowData.statusSuperintendent"
												style="width: 112px; margin: auto;">{{rowData.statusSuperintendent}}</div></td>
										<td sortable="'{{rowData.column}}'"><div
												ng-class="rowData.statusMnE"
												style="width: 112px; margin: auto;">{{rowData.statusMnE}}</div></td>
										<td sortable="'{{rowData.column}}'"><div
												style="background-color: #FFF; padding: 2px; min-height: 24px; text-align: left;">
												{{rowData.remarkBySuperintendent | limitTo: 75}}

												<button
													style="border: none; font-weight: bold; background: none;"
													ng-click="viewReadMore($event,rowData)" class="readMore"
													ng-if="rowData.remarkBySuperintendent.length > 75">Read
													More</button>
											</div></td>
										<td sortable="'{{rowData.column}}'"><div
												style="background-color: #FFF; padding: 2px; min-height: 24px; text-align: left;">
												{{rowData.remarkByMnE | limitTo: 75}}
												<button
													style="border: none; font-weight: bold; background: none;"
													ng-click="viewReadMoreMnE($event,rowData)" class="readMore"
													ng-if="rowData.remarkByMnE.length > 75">Read More</button>
											</div></td>
										<td sortable="'{{rowData.column}}'"><button
												class="btn submission-view"
												ng-class="{'latest' : rowData.latest}"
												ng-click="viewSubmission(rowData, false)">view</button></td>
									</tr>
								</tbody>
							</table>
						</div>
						
					</div>
				</div>
			</div>
		</div>
		<div id="viewSubmissionModal" class="modal fade" role="dialog"
			data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<!-- Modal content -->
				<div class="modal-content">
					<button type="button" class="custom-close close"
						data-dismiss="modal" style="margin-right: 15px; margin-top: 10px;">&times;</button>
					<div class="modal-body text-center">
						<section class="approvalSection"
							ng-show="submissionIndicatorList.length">
							<!-- <div class="text-right"><div class="monthTContainer"></div></div> -->
							<div class="submissionPath">{{selectedRow.stateName}} / {{selectedRow.districtName}} / {{selectedRow.facilityType}} / {{selectedRow.facilitySize}}</div>
							<div class="table-responsive" style="width: 100%">
								<table items="tableData" show-filter="true" cellpadding="0"
									cellspacing="0" border="0"
									class="dataTable table table-striped submissionTable"
									id="dataTable">
									<thead>
										<tr style="border-bottom: 6px solid #FFF;">
											<th colspan="4" style="visibility: hidden"></th>
											<th colspan="4" style="position: relative; background-color: #46803D;" nowrap>{{selectedRow.timePeriod}} ( T )</th>
										</tr>
										<tr class="second-row-head">
											<!-- <th ng-repeat="column in columns | filter:removeRowId"
								ng-class="{'selectedColumn' : sortType == column}"
								ng-click="order(column)" style="position: relative;">{{column}}
								<div class="sorting1">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortReverse == true || (sortType != column &&  sortReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == column &&  sortReverse == false}"></i>
								</div>
							</th> -->
											<th style="position: relative; text-align: left;" nowrap>Indicator&nbsp;Type
												<!-- <div class="sorting1" ng-click="orderSubmissionIndicators('indicatorType')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortIndReverse == true || (sortType != 'indicatorType' &&  sortIndReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'indicatorType' &&  sortIndReverse == false}"></i>
								</div> -->
											</th>
											<th style="position: relative; text-align: left;" nowrap>Indicator
												<!-- <div class="sorting1" ng-click="orderSubmissionIndicators('indicator')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortIndReverse == true || (sortType != 'indicator' &&  sortIndReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'indicator' &&  sortIndReverse == false}"></i>
								</div> -->
											</th>
											<th style="position: relative;" nowrap>T-2&nbsp;Month
												<!-- <div class="sorting1" ng-click="orderSubmissionIndicators('t2')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortIndReverse == true || (sortType != 't2' &&  sortIndReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 't2' &&  sortIndReverse == false}"></i>
								</div> -->
											</th>
											<th style="position: relative;" nowrap>T-1&nbsp;Month
												<!-- <div class="sorting1" ng-click="orderSubmissionIndicators('t1')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortIndReverse == true || (sortType != 't1' &&  sortIndReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 't1' &&  sortIndReverse == false}"></i>
								</div> -->
											</th>
											<th style="position: relative; background-color: #5fb255;" nowrap>Numerator <!-- <div class="sorting1" ng-click="orderSubmissionIndicators('numerator')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortIndReverse == true || (sortType != 'numerator' &&  sortIndReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'numerator' &&  sortIndReverse == false}"></i>
								</div> -->
											</th>
											<th style="position: relative; background-color: #5fb255;" nowrap>Denominator <!-- <div class="sorting1" ng-click="orderSubmissionIndicators('Denominator')">
									<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
										ng-class="{'hiding': sortIndReverse == true || (sortType != 'Denominator' &&  sortIndReverse == false)}"></i>
									<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
										ng-class="{'hiding': sortType == 'Denominator' &&  sortIndReverse == false}"></i>
								</div> -->
											</th>
											<th style="position: relative; background-color: #46803d;">Percentage/Value</th>
											<th style="position: relative; background-color: #46803d;">Remarks</th>
										</tr>
									</thead>
									<tbody>
										<tr ng-click="getChartView(rowData)"
											ng-repeat="rowData in submissionIndicatorList | orderBy:filterType:sortIndReverse">
											<td sortable="'{{rowData.column}}'" style="text-align: left;">{{rowData.indicatorType}}</td>
											<td sortable="'{{rowData.column}}'" style="text-align: left;">{{rowData.indicatorName}}</td>
											<td sortable="'{{rowData.column}}'">{{rowData.t2PercentValue}}</td>
											<td sortable="'{{rowData.column}}'">{{rowData.t1PercentValue}}</td>
											<td sortable="'{{rowData.column}}'">{{rowData.numeratorValue}}</td>
											<td sortable="'{{rowData.column}}'">{{rowData.denominatorValue}}</td>
											<td sortable="'{{rowData.column}}'">{{rowData.percentValue}}</td>
											<td sortable="'{{rowData.column}}'" style="text-align: left;" ng-class="rowData.cssClass">{{rowData.description}}</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="remarks text-center">
								<div style="position: relative;display: inline-block;">
								<textarea class="textarea-length"
									ng-disabled="role == 3 ? !selectedRow.enableMnE : role == 2 ? !selectedRow.enableSuperintendent : true "
									rows="5" cols="50" ng-model="remark" hide-header-footer 
									style="resize: none; min-height: 100px; background-color: rgba(95, 178, 85, 0.3); border: none; text-align: center; padding-top: 15px;padding-bottom: 15px;"
									placeholder="Remarks" maxlength="1000"></textarea>
									<div style="text-align: right;" class="charLeft">Characters left: {{1000-remark.length}}</div>
								</div>
							</div>
							<div class="approveOrReject text-center">
								<button class="submitSCSL" style="background-color: #428ead;"
									ng-disabled="role == 3 ? !selectedRow.enableMnE : role == 2 ? !selectedRow.enableSuperintendent : true "
									ng-click="approveOrRejectSubmission(true)">Approve</button>
								<button class="submitSCSL"
									ng-disabled="role == 3 ? !selectedRow.enableMnE : role == 2 ? !selectedRow.enableSuperintendent : true "
									ng-click="remark ? approveOrRejectSubmission(false):warningForRemark()">Reject</button>
							</div>
						</section>
					</div>
				</div>
			</div>
		</div>
	</div>
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
	<!-- popup modal -->
	<div id="pop" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="successhead"><img alt="" src="resources/images/icons/Messages_success_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; SUCCESS</div>
					<div class="successbody" ng-bind-html="msg"></div>
					<a class="btn btn-default" href="submissionManagement">Ok</a>
				</div>
			</div>

		</div>
	</div>
			<!-- remark read more modal superintendent -->
		<div id="viewRemarkMore" class="modal fade remarkModal standard-modal" role="dialog"
		data-backdrop="static" data-keyboard="false" style="z-index: 9999">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content idea-modal">
				<div class="modal-body">
						
						<div class="remarkhead"><img alt="" src="resources/images/icons/Messages_info_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; REMARKS</div>
						<div class="warnbody">{{selectedSubmission.remarkBySuperintendent}}</div>
						<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- remark read more modal mne -->
		<div id="viewReadMoreMnE" class="modal fade remarkModal" role="dialog"
		data-backdrop="static" data-keyboard="false" style="z-index: 9999">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content idea-modal">
				<div class="modal-body">
						<div class="remarkhead"><img alt="" src="resources/images/icons/Messages_info_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; REMARKS</div>
						<div class="warnbody">{{selectedSubmissionMnE.remarkByMnE}}</div>
						<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="fragments/footer.jsp"></jsp:include>

	<script type="text/javascript"
		src="resources/js/angularController/submissionController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
		var app = angular.module("submissionApp", ['ngSanitize']);
		var myAppConstructor = angular.module("submissionApp");
		myAppConstructor.controller("SubmissionController",
				submissionController);
		myAppConstructor.service('allServices', allServices);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directive.js"></script>
	<script type="text/javascript">
		$("#msgBox").show().delay(2000).fadeOut(400);
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
		$(document).ready(
				function() {
					$('[data-toggle="tooltip"]').tooltip();
				});
		</script>
</body>

</html>