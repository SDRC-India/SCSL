<!-- 
@author Laxman (laxman@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="pdsaSummaryApp">
<head>

<title>SCSL-PDSA Summary</title>
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
<style>
@media(min-width: 768px){
	.pageNameContainer{
		right: 66px;
	}
}
@media(max-width: 640px){
	.pageNameContainer.excel-available{
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

<body ng-controller="PdsaSummaryController" ng-cloak>
	<jsp:include page="fragments/header.jsp"></jsp:include>
	<div class="filter-btn" ng-show="facilityChartView">
		<i class="fa fa-filter fa-lg" aria-hidden="true"></i>
	</div>
	<div class="filter-section" id="advance-filter-section" ng-show="facilityChartView">
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;"
					ng-class="{'hideTooltip': !selectedArea.split('_')[0]}">
					<input type="text" placeholder="Focus Area" id="focus-area"
						data-toggle="tooltip"
						data-original-title="{{selectedArea.split('_')[0]? selectedArea.split('_')[0]:''}}"
						data-placement="bottom" class="form-control not-visible-input"
						name="state" readonly="" ng-model="selectedArea.split('_')[0]">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu corearea-dropdown" role="menu">
							<li ng-repeat="area in coreAreaList" ng-click="selectArea(area);"><a
								href="">{{area.split('_')[0]}}</a></li>
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
							<li ng-repeat="indicator in coreIndicatorList"
								ng-click="selectIndicator(indicator);"><a href="">{{indicator.value}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;"
					ng-class="{'hideTooltip': !selectedChangeIdea.value}">
					<input type="text" placeholder="Change Idea" id="district"
						data-toggle="tooltip"
						data-original-title="{{selectedChangeIdea.value? selectedChangeIdea.value:''}}"
						data-placement="bottom" class="form-control not-visible-input"
						name="district" readonly="" ng-model="selectedChangeIdea.value">
					<div class="input-group-btn" style="position: relative;">
						<button data-toggle="dropdown"
							class="btn btn-color dropdown-toggle" type="button">
							<i class="fa fa-list"></i>
						</button>
						<ul class="dropdown-menu change-dropdown" role="menu">
							<li ng-repeat="idea in changeIdeaList"
								ng-click="selectIdea(idea);"><a href="">{{idea.value}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			
			<button type="submit" class="filter-submit" ng-click="filterPdsaList(selectedArea.split('_')[0], selectedIndicator.key, selectedChangeIdea.key)">Filter</button>
			<button type="submit" class="form-control not-visible-input" class="filter-submit" style="margin-top: 8px;" ng-click="resetFilter()">Reset Filter</button>
	</div>
	<div id="mymain" class="container">
		<div class="container">
			<div style="position: relative" class="page-info">
			<div class="pageNameContainer" ng-class="{'excel-available': facilityChartView}">
				<h4>{{pageName}}</h4>
			</div>
			<div class="download-container dashboard-excel-btn" ng-show="facilityChartView" ng-class="{'not-active': !selectedPdsa}" ng-click="selectedPdsa?getPDSASummaryExcelFile():''" data-toggle="tooltip" title="Download Excel" data-placement="bottom" >
					<img alt="" style="width: 24px;height: 24px;" src="resources/images/icons/excel-icon.svg">
			</div>
			</div>
		</div>
		
		<section ng-show="!facilityChartView" class="content-sec">
			<div class="engagementSelection text-center">
				<div class="select-container dist-list text-center">
					<div class="input-group" style="margin: auto;">
						<input type="text" placeholder="State *" id="state"
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
						<input type="text" placeholder="District *" id="district"
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
							<button ng-click="resetFacilityDropdowns()"   data-toggle="tooltip" title="Reset" data-placement="bottom" 
								class="btn btn-color dropdown-toggle undo-selection" type="button">
								<i class="fa fa-undo"></i>
							</button>
			</div>
			<section class="pdsaSummaryTableSection" >
				<div class="table-responsive header-fixed-table facilitylisttablecontainer" sdrc-pda-table-header-fix tableuniqueclass="'facilitylisttablecontainer'" tabledata="facilityPdsa" style="width: 100%; max-height: 500px;">
					<table items="tableData" show-filter="true" cellpadding="0"
						cellspacing="0" border="0" class="dataTable table table-striped pdsaSummaryTable"
						id="dataTable">
						<thead>
							<tr>
								<th ng-repeat="column in columns | filter:removeRowId" ng-class="{'selectedColumn' : sortType == column}"
									ng-click="order(column)" style="position: relative;">{{column}}
									<div class="sorting1">
										<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
											ng-class="{'hiding': sortReverse == true || (sortType != column &&  sortReverse == false)}"></i>
										<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
											ng-class="{'hiding': sortType == column &&  sortReverse == false}"></i>
									</div>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr ng-click="rowData.Abandon != '0' || rowData.Adapt != '0' || rowData.Adopt != '0' || rowData['On-going'] != '0'|| rowData.Pending != '0' ? getChartView(rowData):''" 
							ng-class="{'rowClickable': rowData.Abandon != '0' || rowData.Adapt != '0' || rowData.Adopt != '0' || rowData['On-going'] != '0'|| rowData.Pending != '0'}"
								ng-repeat="rowData in facilityPdsa | orderBy:filterType:sortReverse">
								<td ng-repeat="column in columns | filter:removeRowId"
									sortable="'{{rowData.column}}'">{{filterFloat(rowData[column])}}</td>
							</tr>
						</tbody>
					</table>
				</div>
				
			</section>
		</section>
		
		<section id="chartViewSection" class="content-sec" ng-show="facilityChartView">
			<div class="row">
				<div class="col-md-4">
					<div class="facilityList">
						<div class="row">
							<div class="col-md-6">
								<button class="back-pdsa" ng-click="backToPdsaList()"><i class="fa fa-long-arrow-left" aria-hidden="true"></i> Facility List</button>
								<div class="legends legend-pdsa-summary" style="background: transparent;">

										<ul>
												<li class="legend_list ng-scope"><span
													class="legend_key ">On-going</span> <span
													class="yellow legnedblock"> </span> </li>
												<!-- end ngRepeat: legend in legends -->
												<li class="legend_list "><span class="legend_key ">Pending</span> <span class="red legnedblock"> </span> </li>
												<!-- end ngRepeat: legend in legends -->
												<li class="legend_list "><span
													class="legend_key ng-binding">Adopt</span> <span
													class="pink legnedblock"> </span> </li>
												<li class="legend_list "><span
													class="legend_key ng-binding">Adapt</span> <span
													class="blue legnedblock"> </span> </li>
												<li class="legend_list "><span
													class="legend_key ng-binding">Abandon</span> <span
													class="green legnedblock"> </span> </li>	
										</ul>
								</div>
							</div>
							<div class="col-md-6 facility-list-border">
								<div class="row">
								<div class="col-md-12 headFacilityName">
									<h5>{{selectedFacilityForChart.Facility}}</h5>
								</div>
								
								<div class="col-md-12 pdsaList">
									<div class="pdsaHead">
										<div
											ng-click="order(column)" style="position: relative;">PDSA
											<div class="sorting1">
												<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
													ng-class="{'hiding': sortReverse == true || (sortType != column &&  sortReverse == false)}"></i>
												<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
													ng-class="{'hiding': sortType == column &&  sortReverse == false}"></i>
											</div>
										</div>
									</div>
									<div class="pdsaBody">
										<div ng-repeat="pdsa in pdsaForFacility | orderBy:filterType:sortReverse" class="pdsaBtn" 
										ng-class="pdsa.cssClass"
										ng-click="getPdsaChart(pdsa)">
											{{pdsa.pdsaName}}
										</div>
										<div ng-if="!pdsaForFacility.length"><h5 class="text-center" style="color: #ce8835;">No PDSA Available</h5></div>
									</div>
								</div>
								</div>
							</div>
							
						</div>
					</div>
				</div>
				<div class="col-md-8"  style="margin-top: 30px;">
					<div ng-show="!selectedPdsa" >
						<h3 class="text-center" style="margin-top: 50px;color: #ce8835;">No PDSA Selected</h3>
					</div>
					<div class="row" ng-show="selectedPdsa">
						<div class="col-md-4">
							<b>Focus Area : </b><span class="color-blue">{{selectedPdsa.coreAreaName}}</span><br>
							<b>Indicator Type : </b><span class="color-blue">{{selectedPdsa.indicatorType}}</span><br>
							<b>Indicator Name : </b><p class="color-blue name-para">{{selectedPdsa.indicatorName}}</p><br>
							<b>Change Idea : </b><span class="color-blue">{{selectedPdsa.changeIdeaName}}</span><br>
							<b>Start Date : </b><span class="color-blue">{{selectedPdsa.startDate}}</span><br>
							<b>End Date : </b><span class="color-blue">{{selectedPdsa.endDate}}</span><br>
							<b>Frequency : </b><span class="color-blue">{{selectedPdsa.frequency}} day(s)
						</div>
						<div class="col-md-8 text-center" style="position: relative"
							ng-repeat="data in controlLinedata" id="lineChartLandingPage">
							<sdrc-multi-line-chart id="pageLineChart" dataprovider="data"></sdrc-multi-line-chart>
							<h4 ng-if="!controlLinedata[0].length" class="nochart">No Chart Available</h4>
							<div class="brief-desc-container">
								<textarea readonly ng-model="selectedPdsa.summary" class="textarea-length"></textarea>
							</div>
							<div class="pdsa-documents">
								<div ng-click="downloadFile(selectedPdsa.firstDocFilepath)" class="first-doc" ng-show="selectedPdsa.firstDocFilepath" style="cursor: pointer">
									<a target="_blank">
									<img alt="" style="width: 20px;height: 26px;" src="resources/images/icons/svg_first_last_doc.svg">
									<div>First Doc.</div>
									</a>
								</div>
								<div ng-click="downloadFile(selectedPdsa.lastDocFilepath)" class="last-doc" ng-show="selectedPdsa.lastDocFilepath" style="cursor: pointer">
								<a target="_blank">
									<img alt="" style="width: 20px;height: 26px;" src="resources/images/icons/svg_first_last_doc.svg">
									<div>Last Doc.</div>
									</a>
								</div>
								<div ng-click="downloadFile(selectedPdsa.otherDocFilePath)" class="any-doc" ng-show="selectedPdsa.otherDocFilePath" style="cursor: pointer">
								<a target="_blank">
									<img alt="" style="width: 20px;height: 26px;" src="resources/images/icons/svg_first_last_doc.svg">
									<div>Other Doc.</div>
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
		
	</div>
	<!-- Modal for error message -->
	<div id="errorMessage" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<h3>
						{{errorMsg}}</span>
					</h3>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<!--end of thematic and chklist  -->
	<!-- popup modal -->
	<div id="pop" class="modal fade" role="dialog">
		<div class="modal-dialog" style="top: 35%">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body text-center">
					<h3 class="text-center">Saved Successfully</h3>
					<a class="btn btn-default" href="engagementScore">Close</a>
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
		src="resources/js/angularController/pdsaSummaryController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
		var app = angular.module("pdsaSummaryApp", []);
		var myAppConstructor = angular.module("pdsaSummaryApp");
		myAppConstructor.controller("PdsaSummaryController",
				pdsaSummaryController);
		myAppConstructor.service('allServices', allServices);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directive.js"></script>
		<script type="text/javascript"
		src="resources/js/angularDirective/pdsaDirective.js"></script>
	<script type="text/javascript">
		$("#msgBox").show().delay(2000).fadeOut(400);
		$(document).ready(function() {

			$('[data-toggle="tooltip"]').tooltip();
			
		});
	</script>
</body>

</html>