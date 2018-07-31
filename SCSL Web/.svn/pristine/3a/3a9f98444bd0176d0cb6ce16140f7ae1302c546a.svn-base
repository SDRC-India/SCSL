<!-- 
@author Devikrushna (devikrushna@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="pdsaApp">
<head>

<title>SCSL-PDSA</title>
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

<body ng-controller="PdsaController" ng-cloak>
	<jsp:include page="fragments/header.jsp"></jsp:include>
	
	<div id="mymain" class="container">
		<div class="pageNameContainer">
			<h4>{{pageName}}</h4>
		</div>
		<div class="row">
			<div class="col-md-12">
			<div class="col-md-5"></div>
					<div class="col-md-4 addpdsa-mobile">
					<div class="addpdsabutton">
					<a ng-href="addPdsa"><h4>+ Add PDSA</h4></a>
				</div>
				</div>
				<div class="col-md-3"></div>
			</div>
		</div>

		<div class="col-md-12 engagementSelection text-center ">
			<div class="select-container dist-list text-center">
				<div class="input-group" style="margin: auto;"
					ng-class="{'hideTooltip': !selectedArea.split('_')[0]}">
					<input type="text" placeholder="Focus Area" id="state"
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
			<div class="reset-button text-center">
				<div class="input-group-btn" style="position: relative;"
					data-toggle="tooltip" data-original-title="Reset"
					data-placement="bottom">
					<button data-toggle="dropdown" ng-click="resetDiv()"
						class="btn btn-color dropdown-toggle" type="button">
						<i class="fa fa-undo"></i>
					</button>
				</div>
			</div>

		</div>
		<div class=" col-md-12 horizontal-legends">

			<ul>
				<li class="legend_list ng-scope"><span
					class="yellow legnedblock"> </span><span class="legend_key ">On-going</span></li>
				<li class="legend_list "><span class="pitch legnedblock">
				</span><span class="legend_key ng-binding">Adopt</span></li>
				<li class="legend_list "><span class="blue legnedblock">
				</span><span class="legend_key ng-binding">Adapt</span></li>
				<li class="legend_list "><span class="red legnedblock">
				</span><span class="legend_key ng-binding">Pending</span></li>
				<li class="legend_list "><span class="green legnedblock">
				</span><span class="legend_key ng-binding">Abandon</span></li>
			</ul>
		</div>

		<div class="table-responsive header-fixed-table pdsalisttablecontainer" style="width: 100%" 
		sdrc-table-header-fix tableuniqueclass="'pdsalisttablecontainer'" tabledata="pdsaList">
			<table items="tableData" show-filter="true" cellpadding="0"
				cellspacing="0" border="0"
				class="dataTable table table-striped submissionTable" id="dataTable">
				<thead>
					
					<th style="position: relative; min-width: 100px;" nowrap>Focus
						Area
						<div class="sorting1" ng-click="order('coreAreaName')">
							<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
								ng-class="{'hiding': sortReverse == true || (sortType != 'coreAreaName' &&  sortReverse == false)}"></i>
							<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
								ng-class="{'hiding': sortType == 'coreAreaName' &&  sortReverse == false}"></i>
						</div>
					</th>

					<th style="position: relative;min-width: 200px"  nowrap>Indicator
						<div class="sorting1" ng-click="order('indicatorName')">
							<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
								ng-class="{'hiding': sortReverse == true || (sortType != 'indicatorName' &&  sortReverse == false)}"></i>
							<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
								ng-class="{'hiding': sortType == 'indicatorName' &&  sortReverse == false}"></i>
						</div>
					</th>
					<th style="position: relative; min-width: 120px;" nowrap>Change
						Idea
						<div class="sorting1" ng-click="order('changeIdeaName')">
							<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
								ng-class="{'hiding': sortReverse == true || (sortType != 'changeIdeaName' &&  sortReverse == false)}"></i>
							<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
								ng-class="{'hiding': sortType == 'changeIdeaName' &&  sortReverse == false}"></i>
						</div>
					</th>
					<th style="position: relative; min-width: 112px" nowrap>PDSA
						Name
						<div class="sorting1" ng-click="order('pdsaName')">
							<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
								ng-class="{'hiding': sortReverse == true || (sortType != 'pdsaName' &&  sortReverse == false)}"></i>
							<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
								ng-class="{'hiding': sortType == 'pdsaName' &&  sortReverse == false}"></i>
						</div>
					</th>
					<th style="position: relative; min-width: 175px;" nowrap>Start
						Date - End Date
						<div class="sorting1" ng-click="order('timePeriod')" nowrap>
							<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
								ng-class="{'hiding': sortReverse == true || (sortType != 'timePeriod' &&  sortReverse == false)}"></i>
							<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
								ng-class="{'hiding': sortType == 'timePeriod' &&  sortReverse == false}"></i>
						</div>
					</th>
					<th style="position: relative; min-width: 112px;" nowrap>Frequency
						<div class="sorting1" ng-click="order('frequency')">
							<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
								ng-class="{'hiding': sortReverse == true || (sortType != 'frequency' &&  sortReverse == false)}"></i>
							<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
								ng-class="{'hiding': sortType == 'frequency' &&  sortReverse == false}"></i>
						</div>
					</th>
					<th style="position: relative;" nowrap>Status
						<div class="sorting1" ng-click="order('statusName')">
							<i class="fa fa-caret-up fa-lg asc" aria-hidden="true"
								ng-class="{'hiding': sortReverse == true || (sortType != 'statusName' &&  sortReverse == false)}"></i>
							<i class="fa fa-caret-down fa-lg dsc" aria-hidden="true"
								ng-class="{'hiding': sortType == 'statusName' &&  sortReverse == false}"></i>
						</div>
					</th>
					<th style="position: relative; min-width: 100px;" nowrap>First
						Doc.</th>

					<th style="position: relative; min-width: 100px;" nowrap>Last
						Doc.</th>

				</thead>
				<tbody>
					<tr
						ng-repeat="rowData in pdsaList | orderBy:filterType:sortReverse">
						
						<td ng-click="openPdsaDataentry(rowData)" style="cursor: pointer;" class="pdsa-table" sortable="'{{rowData.column}}'">{{rowData.coreAreaName}}</td>
						<td ng-click="openPdsaDataentry(rowData)" style="cursor: pointer;" class="pdsa-table" sortable="'{{rowData.column}}'">{{rowData.indicatorName}}</td>
						<td ng-click="openPdsaDataentry(rowData)" style="cursor: pointer;" class="pdsa-table" sortable="'{{rowData.column}}'">
						{{rowData.changeIdeaName | limitTo: 100}}
						<button style="border: none;font-weight: bold;background: none;" ng-click="viewReadMore($event,rowData)" class="readMore" ng-if="rowData.changeIdeaName.length > 100"
						>Read More</button>
						</td>
						<td ng-click="openPdsaDataentry(rowData)" style="cursor: pointer;" class="pdsa-table" sortable="'{{rowData.column}}'" >{{rowData.pdsaName}}</td>
						<td ng-click="openPdsaDataentry(rowData)" style="cursor: pointer;" class="pdsa-table" sortable="'{{rowData.column}}'">{{rowData.timePeriod}}</td>
						<td ng-click="openPdsaDataentry(rowData)" style="cursor: pointer;" class="pdsa-table" sortable="'{{rowData.column}}'">{{rowData.frequency}}</td>
						<td ng-click="openPdsaDataentry(rowData)" style="cursor: pointer;" class="pdsa-table" sortable="'{{rowData.column}}'"><div
								ng-class="rowData.cssClass"
								style="width: 112px; margin: auto; text-align: center;">{{rowData.statusName}}&nbsp({{rowData.noOfPdsaCompleted}}/{{rowData.pdsaFrequency}})</div></td>
						<td class="pdsa-table" sortable="'{{rowData.column}}'"><img
							ng-if="rowData.firstDocFilepath!=null" alt=""
							ng-click=downloadFile(rowData.firstDocFilepath)
							style="width: 20px; cursor: pointer;"
							src="resources/images/icons/svg_first_last_doc.svg"> <img
							ng-if="rowData.firstDocFilepath==null" alt=""
							style="width: 20px; cursor: pointer;"
							src="resources/images/icons/svg_upload_doc.svg"></td>
						<td class="pdsa-table" sortable="'{{rowData.column}}'"><img
							ng-if="rowData.lastDocFilepath==null" alt=""
							style="width: 20px; cursor: pointer;"
							src="resources/images/icons/svg_upload_doc.svg"> <img
							ng-if="rowData.lastDocFilepath!=null"
							ng-click=downloadFile(rowData.lastDocFilepath) alt=""
							style="width: 20px; cursor: pointer;"
							src="resources/images/icons/svg_first_last_doc.svg"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div ng-show="!pdsaList.length"><h3 style="text-align: center;color: #ce8835;">No PDSA available</h3></div>


		<!-- PDSA Data Entry Modal -->

		<div id="viewDataEntryModal" class="modal fade" role="dialog"
			data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog"
				style="width: 100%; background-color: #FFF;">

				<!-- Modal content -->
				<div class="modal-content modal-content1">
					<button type="button" class="custom-close close close-btn-pdsa"
						data-dismiss="modal" >&times;</button>
					<div class="modal-body text-center">
						<div class="pageNameContainer pagename-modal">
							<h4>PDSA Data Entry</h4>
						</div>

						<section class="approvalSection">
							<div class="pdsaname-modal">
								<h4 style="font-weight: bold;">{{selectedPdsaModal.pdsaName}}</h4>
							</div>

							<div class="table-responsive" style="width: 100%">
								<table items="tableData" show-filter="true" cellpadding="0"
									cellspacing="0" border="0"
									class="dataTable table  table-bordered pdsa-modal-table"
									id="dataTable">
									<tbody>
										<tr>
											<td style="text-align: center;">{{selectedPdsaModal.coreAreaName}}</td>
											<td colspan="2" style="text-align: center;">{{selectedPdsaModal.indicatorName}}</td>
											<td style="text-align: center;">{{selectedPdsaModal.changeIdeaName}}</td>
										</tr>
										<tr>
											<td style="text-align: center;">{{selectedPdsaModal.pdsaNumber}}</td>
											<td style="text-align: center;">{{selectedPdsaModal.startDate}}</td>
											<td style="text-align: center;">{{selectedPdsaModal.endDate}}</td>
											<td style="text-align: center;">{{selectedPdsaModal.frequency}}</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="row">
								<div class="col-md-7">
									<div class="table-responsive" style="width: 100%;max-height: 400px;margin-bottom: 0px !important;">
										<input type="file" ng-class="pdsaTXNData[0].cssClass"
											id="modal-upload" ng-model="pdsaDocument"
											onchange="angular.element(this).scope().getFileDetails(this)"
											name="file">
										<table items="tableData" show-filter="true" cellpadding="0"
											cellspacing="0" border="0"
											class="dataTable table table-bordered scroll pdsa-modal-table1 "
											id="dataTable">
											<thead>
												<tr>
													<th
														style="position: relative; min-width: 20px; background-color: #214757;"
														nowrap>Sl.&nbsp;No.</th>
													<th style="position: relative; background-color: #214757; text-align: center">Numerator</th>
													<th style="position: relative; background-color: #214757; text-align: center">Denominator</th>
													<th style="position: relative; background-color: #214757; text-align: center">Percentage</th>


												</tr>
											</thead>
											<tbody>
												<tr ng-repeat="rowData in pdsaTXNData">
													<td style="text-align: center;" ng-class="rowData.cssClass">
														<div class="modal-upload-div" ng-if="$index == 0">
															<input class="modal-upload"
																ng-class="rowData.cssClass" type="button"
																id="uploadBtn" value="{{firstFileName}}" ng-show="rowData.enable" style="border-right: 1px solid #9da0a0;border-left: none;border-bottom: none;border-top: none;" />
														</div> {{$index+1}}</br> <!-- {{rowData.dueDate}}</br> -->
													</td>
													<td class=""><input
														ng-blur="validateInput(rowData, 'numeratorValue', 'Intermediate')"
														type='text' only-seven-digits hide-header-footer class='tableInput1'
														ng-model='rowData.numeratorValue'
														ng-disabled='!rowData.enable'
														ng-change="calculatePercentage(rowData)"></td>

													<td class=""><input
														ng-blur="validateInput(rowData, 'denominatorValue', 'Intermediate')"
														type='text' only-seven-digits hide-header-footer class='tableInput1'
														ng-model='rowData.denominatorValue'
														ng-disabled='!rowData.enable'
														ng-change="calculatePercentage(rowData)"></td>
													<td class=""><input type='text' readonly
														class='tableInput1' ng-model='rowData.percentage'></td>
												</tr>
											</tbody>
										</table>
									</div>
									<div class="col-md-12 text-center">
										<div class="submit-table-modal">
											<button ng-disabled="!disableClosePDSA"
												class="submitEngagementScore pdsa-button1"
												ng-click="validatePdsaNumerator(rowData)">SUBMIT</button>
										</div>
										<div class="col-md-1"></div>
									</div>
								</div>
								<div class="col-md-5" ng-if="ldata[0].length">
									<section class="linechartSection" style="margin-bottom: 50px;">
										<div class="row">
											<div class="col-md-12">
												<div class="text-center" ng-repeat="data in ldata"
													id="lineChartLandingPage">
													<scsl-line-pdsa id="pageLineChart" dataprovider="data"></scsl-line-pdsa>
												</div>
											</div>
										</div>
									</section>
								</div>
							</div>

						</section>

						<div class="row" id="closePdsaForm"
							ng-show="pdsaTXNData.length>0&&(!pdsaTXNData[0].enable) && selectedPdsaModal.lastDocFilepath==null">

							<div class="col-md-12">
								<div class="col-md-10 col-md-offset-1">
									<h5 class="close-pdsa-border">CLOSE PDSA</h5>
								</div>
								<input type="file" ng-class="" id="lastdoc-upload"
									ng-model="lastdocupload" name="lastfile"
									onchange="angular.element(this).scope().getLastFileDetails(this)">
								<input type="file" ng-class="" id="otherdoc-upload"
									onchange="angular.element(this).scope().getOtherFileDetails(this)"
									ng-model="otherdocupload" name="otherfile">

								<div class="col-md-12 close-pdsa-div">
									<div class="col-md-10 col-sm-12 col-xs-12 col-md-offset-1">
										<label class="col-md-5 col-sm-6 col-xs-12 closepdsa-last text-right"
											for="textinput"> Last Document </label>
										<div class="col-md-5 col-sm-6 col-xs-12 text-left closeLablelastdoc">
											<div class="input-group" style="">
												<input class="close-input" type="text" required readonly
													placeholder="Last Document *" id="lastdoc" name="lastdoc"
													ng-model="lastDoc"> <img alt="" id="lasticon"
													class="close-upload"
													src="resources/images/icons/svg_upload_doc.svg">
											</div>
										</div>
									</div>
								</div>

								<div class="col-md-12 close-pdsa-div">
									<div class="col-md-10 col-sm-12 col-xs-12 col-md-offset-1">
										<label class="col-md-5 col-sm-6 col-xs-12 closepdsa-other  text-right"
											for="textinput"> Any Other Document </label>
										<div class="col-md-5 col-sm-6 col-xs-12 text-left closeLableotherdoc">
											<div class="input-group" style="">
												<input class="close-input" type="text" required readonly
													placeholder="Upload Other Document" id="otherdoc"
													name="otherdoc" ng-model="otherDoc"> <img alt=""
													id="othericon" class="close-upload"
													src="resources/images/icons/svg_upload_doc.svg">
											</div>
										</div>
									</div>
								</div>


								<div class="col-md-12 close-pdsa-div">
									<div class="col-md-10 col-sm-12 col-xs-12 col-md-offset-1">
										<label class="col-md-5 col-sm-6 col-xs-12 closepdsa-status text-right"
											for="textinput"> Status </label>
										<div class="col-md-5 col-sm-6 col-xs-12 select-container select-container2 closeLablelstatus">
											<div class="input-group ">
												<input type="text" placeholder="Select Status *" id="idea"
													class="form-control not-visible-input" required name="idea"
													readonly="" ng-model="selectedStatus.value">
												<div class="input-group-btn" style="position: relative;">
													<button data-toggle="dropdown"
														class="btn btn-color dropdown-toggle" type="button">
														<i class="fa fa-list"></i>
													</button>
													<ul class="dropdown-menu corearea-dropdown" role="menu">
														<li ng-repeat="status in statusList"
															ng-click="selectStatus(status); "><a href="">{{status.value}}</a></li>
													</ul>
												</div>
											</div>
										</div>
									</div>
								</div>


								<div class="col-md-12 col-sm-12 col-xs-12 close-pdsa-div ">
									<div class="col-md-6 col-sm-12 col-md-offset-3">
										<div class="col-md-6 col-sm-6 col-xs-12">
											<div class="input-group desc-margin" style="margin: auto;">
												<textarea maxlength="300" class="pdsa-textarea"
													ng-change="calculateCharLeft(briefdescription)"
													placeholder="Brief Description *" id="description"
													name="description" ng-model="briefdescription" hide-header-footer
													style="resize: none; overflow: hidden; word-wrap: break-word; height: 100px;"></textarea>
												<div style="text-align: right;" class="charLeft">Characters
													left: {{300-briefdescription.length}}</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-12 col-sm-12 col-xs-12  text-center close-pdsa-submit">
								<button class="submitEngagementScore pdsa-button2"
									ng-click="validateClosePdsa()">SUBMIT</button>
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
					<a class="btn btn-default" ng-href="{{link}}" >Ok</a>
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
					<div class="successbody">PDSA closed successfully</div>
					<a class="btn btn-default" ng-href="pdsa" >Ok</a>
				</div>
			</div>
		</div>
		</div>
		<!-- idea read more modal -->
	<div id="viewChangeIdeaMore" class="modal fade" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content idea-modal">
				<div class="modal-body">
					<div class="">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close" style="margin-top: -20px;">
							<span aria-hidden="true">×</span>
						</button>
						<div class="row">
							<div class="col-md-12 text-center">
								<span>{{selectedPdsaModal.changeIdeaName}}</span>
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
		<script type="text/javascript"
			src="resources/js/angularController/pdsaController.js"></script>
		<script type="text/javascript"
			src="resources/js/angularService/services.js"></script>
		<script type="text/javascript">
			var app = angular.module("pdsaApp", []);
			var myAppConstructor = angular.module("pdsaApp");
			myAppConstructor.controller("PdsaController", pdsaController);
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