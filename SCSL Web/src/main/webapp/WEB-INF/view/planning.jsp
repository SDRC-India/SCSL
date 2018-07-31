<!-- 
@author Devikrushna (devikrushna@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="planningApp">
<head>

<title>SCSL-Planning</title>
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

</head>

<body ng-controller="planningController" ng-cloak
	ng-class="{'datepickeropen': datePickerOpen}" class="planningbody">

	<style type="text/css">
.planningbody.datepickeropen>div#ui-datepicker-div[style] {
	height: 170px;
	width: 224px;
	margin-left: 3px;
	margin-top: 3px;
	margin-bottom: 3px;
	display: block !important;
	
}
div#ui-datepicker-div[style]{
	z-index: 99999 !important;
}
.planningbody .ui-datepicker table {
	font-size: 0.6em !important;
}

.planningbody .ui-datepicker .ui-datepicker-title {
	line-height: 1.1em !important;
}

.planningbody .ui-datepicker .ui-datepicker-prev span, .ui-datepicker .ui-datepicker-next span
	{
	margin-left: -8px !important;
	margin-top: -13px !important;
}

.planningbody .ui-state-default, .ui-widget-content .ui-state-default,
	.ui-widget-header .ui-state-default {
	border: none !important;
	background: none !important;
}
.planningbody .ui-state-hover {
	border: none !important;
	background: none !important;
}
.planningbody a.ui-state-active { 
 	color: rgb(101, 90, 215) !important; 
 	font-weight: bold; 
	font-size: small; 
 } 
 .planningbody.datepickeropen > div#ui-datepicker-div { 
     z-index: 555 !important; 
 } 
 @media (min-width: 676px) and (max-width:768px ){
 section.bottomfooter {
  width: 110% !important;
 }
}
</style>

	<jsp:include page="fragments/header.jsp"></jsp:include>

	<div id="mymain" class="container">
		<div class="pageNameContainer">
			<h4>{{pageName}}</h4>
		</div>

		<section class="planSection col-md-12" id="topView">
			<div class="engagementSelection text-center"
				ng-if="allPlanDetails.areaModel.length>0">
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
		</section>
		
		<div class="container div_details" style="margin-top: 70px;">
			<div class="row">
				<div class="col-md-12 col-sm-12 col-lg-12">
					<div class="row">
						<div class="col-md-9 col-sm-9">
							<div class="row">
								<div class="col-md-4 col-sm-4">
									<!-- facility data  -->
									<div ng-show="showAssessement">
										<div class="three-col1" style="padding-margin: 0px;">
											<div class="three-col-content">
												<div class="three-col-img">
													<h4 class="col3header" style="text-align: center">Facility</h4>
												</div>
											</div>
										</div>
										<div class="three-col2" style="padding-margin: 0px;">
											<div class="three-col-content">
												<div class="three-col-img">
													<h4 class="statusdate-font">{{selectedFacility.facilityName}}</h4>
												</div>
											</div>
										</div>
										<div class="three-col1" style="padding-margin: 0px;">
											<div class="three-col-content">
												<div class="three-col-img">
													<h5 class="col3header" style="text-align: center">Visit
														History</h5>

												</div>
											</div>
										</div>
										<div class="three-col4">

											<div class="three-cl4" ng-repeat="assessement in userName"
												ng-show="userName.length>=1">
												<h4 class="status-name-font">{{assessement.split('_')[1]}}</h4>

												<!-- Nested media object -->
												<div class="media-body">
													<!-- Nested media object -->
													<div class="media">
														<div class="media-left"></div>
														<div class="media-body">
															<a>
																<p class="statusdate-font" ng-repeat="assessmentHistory in assessementList[assessement]"
																	ng-click="showStatusDate(assessmentHistory)" style="cursor: pointer;">
																	{{assessmentHistory.plannedDate ?
																	assessmentHistory.plannedDate+' &nbsp; (Agenda
																	Report)':''}}
															</a>&nbsp;&nbsp;&nbsp;&nbsp; <img
																ng-click=downloadFile(assessmentHistory.agendaPath,$event)
																style="width: 14px; height: 14px; cursor: pointer;"
																src="resources/images/icons/svg_download_plan_doc.svg">


															<br> <a>{{assessmentHistory.visitedDate?assessmentHistory.visitedDate+'
																&nbsp; (Trip Report)':""}}</a>&nbsp;&nbsp;&nbsp;&nbsp; <img
																ng-if="assessmentHistory.visitedDate!=null"
																ng-click=downloadFile(assessmentHistory.reportPath,$event)
																style="width: 14px; height: 14px; cursor: pointer;"
																src="resources/images/icons/svg_download_plan_doc.svg">

															</p>
														</div>
													</div>
												</div>
											</div>
											<p class="statusdate-font" ng-show="userName.length==0">No
												history available</p>
										</div>
									</div>

									<!-- / start status and date planned -->
									<div ng-show="showStatus">
										<div class="three-status" style="padding-margin: 0px;">
											<div class="three-col-content">
												<div class="three-col-img">
													<h4 class="col3header"
														style="text-align: center">Facility</h4>
												</div>
											</div>
										</div>
										<div class="three-col2" style="padding-margin: 0px;">
											<div class="three-col-content">
												<div class="three-col-img">
													<h4 class="statusdate-font">{{selectedFacility.facilityName}}</h4>
												</div>
											</div>
										</div>
										<div class="three-status" style="padding-margin: 0px;">
											<div class="three-col-content">
												<div class="three-col-img">
													<h4 class="col3header"
														style="text-align: center">Date
														Planned</h4>
												</div>
											</div>
										</div>
										<div class="three-col2" style="padding-margin: 0px;">
											<div class="three-col-content">
												<div class="three-col-img">
													<h4 class="statusdate-font">
														{{selectedAssessmentHistory.plannedDate}}</h4>
												</div>
											</div>
										</div>
										<div class="three-status" style="padding-margin: 0px;">
											<div class="three-col-content">
												<div class="three-col-img">
													<h4 class="col3header"
														style="text-align: center">Status</h4>
												</div>
											</div>
										</div>
										<div class="three-col2" style="padding-margin: 0px;">
											<div class="three-col-content">
												<div class="three-col-img">
													<h4 class="visited-font"
														ng-if="selectedAssessmentHistory.visitedDate!=null">
														Visited On &nbsp {{selectedAssessmentHistory.visitedDate}}
														&nbsp;&nbsp;&nbsp; <img
															ng-click=downloadFile(selectedAssessmentHistory.reportPath)
															style="width: 14px; height: 14px; cursor: pointer; margin-right: 5px;"
															src="resources/images/icons/svg_download_plan_doc.svg">
													</h4>
													<h4 class="visited-font"
														ng-if="selectedAssessmentHistory.visitedDate==null">Not
														Visited</h4>
													<h4 class="uploadstatus"
														ng-if="selectedAssessmentHistory.reportUpload"
														ng-click="openUploadReport()">Upload Report</h4>
												</div>
											</div>
										</div>
									</div>

									<!-- / end status and date planned -->
								</div>
								<!--end facility data left -->
								<div class="col-md-8 col-sm-8 div-dis-details"
									ng-show="facilityNameDiv">
									<div class="row three-colr1">
										<div class="col-md-12 col-sm-12 col-lg-12">
											<div class="col-md-4 col-sm-6 col-xs-6"
												ng-click="showFacility(facility,$event);clearFile()"
												id="calenderopen"
												ng-class="facility.facilityName==selectedFacility.facilityName?'selectedDiv':''"
												ng-repeat="facility in facillityDetails" data-toggle="tooltip" data-html="true"
													title="{{showPlan(facility)}}" data-placement='bottom'>
												<div 
													ng-class="facility.planned?'three-colrPlanned':'three-colr11'">
													<img ng-if="facility.pending" class="warn-img-plan"
														src="resources/images/icons/Messages_warning-plan_caution_icon.svg">
													<div class="three-col-content">
														<div class="three-col-img">
															<h4 class="col3header1"
																style="text-align: center; height: 24px;">
																{{facility.facilityName}}
																<div ng-repeat="plan in facility.plannedHistory | limitTo : 1" style="margin-top: 4px">
																	{{plan.key}}&nbsp;-&nbsp;{{plan.value}}</div>
															</h4>
														</div>
													</div>
												</div>
											</div>
											<h3 class="text-center" style="margin-top: 40px;color: #99655f;" ng-show="!facillityDetails.length">No facility available</h3>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-3 col-sm-3">
							<div>
								<ul class="available">
									<li>
										<div id="circle-avail">
											<h5 class="col3-avail">Available</h5>
										</div>
									</li>
									<li>
										<div id="circle-plan">
											<h5 class="col3-avail">Planned</h5>
										</div>
									</li>
									<li>
										<div id="circle-warn-img">
											<img class="" style="width: 33px; margin-top: 10px;"
												src="resources/images/icons/Messages_warning-plan_caution_icon.svg">
											<h5 class="col3-avail" style="margin-top: -36px;">Trip
												Report Pending</h5>
										</div>
									</li>

								</ul>
							</div>
							<div class="three-col1-right" style="padding-margin: 0px;"
								ng-show="planCalender" id="calendertavView">
								<div class="three-col-content-cal-rel">
									<ul class="nav nav-tabs cal-rel">
										<li id="calendar-btn" style="margin-bottom: 0px;"><a
											data-toggle="tab" id="plan-font" href="#calender"
											ng-click="planButtonClicked()">Plan</a></li>

										<li id="release-btn" style="float: right; margin-bottom: 0px;"
											ng-show="selectedFacility.realeaseDate!=null"><a
											data-toggle="tab" id="release-font" href="#calender"
											ng-click="releaseButtonClicked()">Release</a></li>
									</ul>
									<div class="tab-content">
										<div id="calender" class="tab-pane fade in active">
											<div class="input-group" id="plan-calender-position">
												<input type="text" id="datepicker" class="form-control"
													placeholder="Select date" readonly ng-disabled="true">
												<span class="p-field-cb"></span> <span
													class="input-group-addon"><span
													class="p-select-arrow"><i class="fa fa-calendar"
														id="datepicker1"></i></span></span>
											</div>
											<!-- 											<div class="clndrdata_save"> -->
											<!-- 												<button ng-click="confirmToPlan()" type="button" -->
											<!-- 													class="btn btn-primary clnd_save"  ng-show="plan">Plan</button> -->



											<!-- 											</div> -->
										</div>
									</div>
								</div>
							</div>

							<!------------------- Agenda  ---------------------->

							<div class="three-col1-right" style="padding-margin: 0px;"
								ng-show="agenda">
								<div class="three-col-content-cal-rel">
									<div class="input-group" style="">
										<input class="agenda-upload" type="text" required readonly
											placeholder="Agenda * " id="agenda" name="agenda"
											ng-model="agendaUpload" style="cursor: pointer"> <img
											alt="" id="agendaimg" class="agenda-tag-upload"
											src="resources/images/icons/svg_upload_doc.svg">
									</div>
									<div class="input-group" style="margin: auto;">

										<input class="agenda-tag"
											ng-change="calculateCharLeft(reportDescription)" required
											placeholder="Tag an email id" id="tagAgenda" type="text"
											ng-model="planTag"
											ng-keyUp="validateEmail(planTag,'invalidmsg')" name="agenda"
											maxlength="300">
										<div id="invalidmsg"></div>

									</div>
									<span> <input type="file" ng-class="" id="agenda-upload"
										ng-model="agendaUpload" name="file"
										onchange="angular.element('body').scope().getAgendaDetails(this)"></span>
								</div>

							</div>
							<!-- 							<button type="button" ng-show="release" -->
							<!-- 													class="btn btn-primary clnd_save" ng-click="releasePlan()">Release</button> -->
							<div class="" ng-show="release">
								<button class="plan-next" ng-click="releasePlan()">Release</button>
							</div>
							<div class="" ng-show="next">
								<button class="plan-next" ng-click="submitAgenda()">Next</button>
							</div>
							<div class="" ng-show="submit">
								<button class="plan-next" ng-click="validatePlan()">SUBMIT</button>
							</div>
							<!---------------- end  Agenda  ------------->

						</div>
					</div>
				</div>
			</div>
		</div>
	<div style="" class="top-img" ><a href="#" class="back-top"><img class="scroll-top-img"
			style="width: 40px; margin-top: 10px;"
			src="resources/images/icons/scroll top.svg"> </a></div>
	</div>



	<!-- Modal for upload report  -->
	<div id="uploadReport" class="modal fade" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content upload-report-modal"
				style="background-color: #BA9691; margin-left: 63px;">
				<div class="modal-body"
					style="background-color: #BA9691; padding: 0px !important;">
					<div class="col-md-12"
						style="background-color: #333a3b; padding: 10px;">
						<h5 class="idea-modal-header" style="color: #BA9691;">Upload
							Report</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close" style="margin-top: -42px;"
							ng-click="clearData()">
							<span aria-hidden="true" style="font-size: 30px; color: #f0bf7f;">×</span>
						</button>
					</div>

					<div class="">
						<div class="row trip-mobile">
							<div class="col-md-12 text-center" style="">
								<div
									class="col-md-6 col-sm-6 col-xs-6 col-md-offset-3 text-center trip-calender"
									style="margin-top: 10px;">
									<div class="input-group">
										<input type="text" id="datepicker2" ng-model="selectDate"
											class="form-control" placeholder="Visit date * " readonly>
										<span class="p-field-cb"></span> <span
											class="input-group-addon"><span class="p-select-arrow"><i
												class="fa fa-calendar trip-calender-click" id="datepicker3"></i></span></span>
									</div>
								</div>
								<div
									class="input-group col-md-10 col-sm-10 col-xs-10 col-md-offset-1"
									style="margin: auto;">
									<textarea class="report-desc"
										ng-change="calculateCharLeft(reportDescription)" required
										placeholder="Brief Description * " id="reportDesc" type="text"
										ng-change="calculateCharLeft(reportDescription)" hide-header-footer
										name="changeidea" ng-model="reportDescription" maxlength="300"></textarea>
									<div style="text-align: right; margin-right: 32px;" class="charLeft">Characters
										left: {{300-reportDescription.length}}</div>
								</div>
								<div
									class="input-group col-md-10 col-sm-6 col-xs-6 col-md-offset-1"
									style="margin: auto;">
									<div class="input-group" style="">
										<input class="report-input" type="text" required readonly
											placeholder="Upload Trip Report * " id="reportUpload"
											name="reportUpload" ng-model="uplodReport"><img
											class="trip-report-upload" style="cursor: pointer;"
											id="trip_img"
											src="resources/images/icons/svg_upload_doc_plan_data.svg">
									</div>
									<span> <input type="file" ng-class=""
										id="reportplan-upload" ng-model="planUpload" name="file"
										onchange="angular.element('body').scope().getFileDetails(this)"></span>
								</div>

							</div>
						</div>
					</div>
					<div class="col-md-12 text-center">
						<button class="report-submit" ng-click="saveReport()">SUBMIT</button>
					</div>

				</div>
			</div>
		</div>
	</div>
	<!-- End Modal for upload report  -->

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
	<!--End of Modal for error message -->
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
	<!-- Modal for info message -->
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
	<!--end of info message  -->
	<!-- pop up (success) modal -->
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
					<a class="btn btn-default" ng-click="planPage()">Ok</a>
				</div>
			</div>
		</div>
	</div>
	<!-- change release success modal -->
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
					<a class="btn btn-default"  ng-click="planPage()">Ok</a>
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
		src="resources/js/angularController/planningController.js"></script>
	<script type="text/javascript"
		src="resources/js/angularService/services.js"></script>
	<script type="text/javascript">
		var app = angular.module("planningApp", []);
		var myAppConstructor = angular.module("planningApp");
		myAppConstructor.controller("planningController", planningController);
		myAppConstructor.service('allServices', allServices);
	</script>
	<script type="text/javascript"
		src="resources/js/angularDirective/directive.js"></script>
	<script type="text/javascript">
		$("#msgBox").show().delay(2000).fadeOut(400);
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			$(".back-top").click(function(e){
				e.preventDefault();
				$("html, body").animate({
					scrollTop : 0
				}, 500)
			})
			setTimeout(function() {
			$(window).scroll(function(){
				if($(window).scrollTop()>200)
					$(".top-img").css({"display":"block"});
				else
					$(".top-img").css({"display":"none"});
				});
			},200)
			//$('[data-toggle="tooltip"]').tooltip();
			$("#datepicker2").datepicker({
				dateFormat : "dd-mm-yy"
			});
			$("#datepicker3").datepicker({
				dateFormat : "dd-mm-yy"
			});
			$('[data-toggle="tooltip"]').tooltip();
			
// 			$('.back-top').fadeOut();
// 			var offset = 250;
// 			var duration = 500;
// 			$(window).scroll(function(){
// 				if (jQuery(this).scrollTop() > offset) {
// 					jQuery('.back-top').fadeIn(duration);
// 				}else{
// 					jQuery('.back-top').fadeOut(duration);
// 				}
// 			});
// 		$('.back-top').click(function(event) {
// 				event.preventDefault();
// 				jQuery('html, body').animate({scrollTop: 0}, duration);
// 				return false;
// 			});
		});
		/* 			$( "#datepicker" ).datepicker({dateFormat: "dd-mm-yy"});
		 $( "#datepicker1" ).datepicker({dateFormat: "dd-mm-yy"}); */

		$("#datepicker").click(function() {
			$("#datepicker").datepicker("show");
		});
		$('.datepicker').addClass('plan-calender-div');
	</script>
	<script type="text/javascript" src="resources/js/autosizeTextarea.js"></script>
	<script type="text/javascript">
	
		$(function() {
			$('.textarea-length').autosize({
				append : "\n"
			});
		});
	</script>

</body>

</html>