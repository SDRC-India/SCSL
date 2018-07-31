<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html ng-app="passwordChangeApp">
<head>

<title>SCSL-Change password</title>
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
<style>
.bottomfooter {
	position: fixed !important;
	bottom: 0 !important;
}
</style>

</head>

<body ng-controller="passwordChangeController" ng-cloak>

<jsp:include page="fragments/header.jsp"></jsp:include>

	<div id="mymain" class="container">
		<div class="pageNameContainer">
			<h4>{{pageName}}</h4>
		</div>
	</div> 
	
  	<div class="container">
			<div class="row">			
			<form class="form-horizontal form-reg" name="myForm1">
			   <div class="col-md-9 col-md-offset-2 change-pass">
				<div class="form-group">
				  <div class="col-md-3 col-sm-4">	  
					<label class="control-label label-text"
						for="pwd">Current password<span style="color: red">*</span>
					</label>
					</div>
					<div class="col-md-6 col-sm-6">
						<input type="password" id="oldpassword" class="form-control"
							ng-keyup="function1()" name="oldpass" ng-model="user.oldPassword" htmlEscape="true" 
							ng-change="oldPassError()" placeholder="Enter current password" disallow-spaces>
						<span id="oldPasserror" style="color: red;"></span>
					</div>
						<div class="clearfix"></div>
				</div>
			
				 <div class="form-group">
				  <div class="col-md-3 col-sm-4">	  
					<label class="control-label label-text" for="pwd">New password<span style="color: red">*</span>
					</label>
					</div>
					<div class="col-md-6 col-sm-6">
						<input type="password" class="form-control" id="txtPassword"
							minlength="8" maxlength="16" ng-keyup="newpass()" htmlEscape="true" 
							ng-model="user.newPassword" placeholder="Enter new password"
							ng-change="newPassError()" name="newpass" disallow-spaces>
						    <span class="minChar" ng-show="myForm1.newpass.$error.minlength && myForm1.newpass.$dirty"  
							> The password must have minimum 8 characters. </span> 
						<span id="newPasserror" ng-hide="myForm1.newpass.$error.minlength && myForm1.newpass.$dirty" style="color: red;"></span>
					</div>
					<div class="clearfix"></div>
				</div>
				
				<div class="form-group">
				  <div class="col-md-3 col-sm-4">	  
					<label class="control-label label-text"
						for="pwd">Confirm password<span style="color: red">*</span>
					</label>
					</div>
					<div class="col-md-6 col-sm-6">
						<input type="password" class="form-control"
							id="txtConfirmPassword" ng-keyup="hideerrormessage()" htmlEscape="true" 
							ng-model="user.confirmPassword" minlength="8" maxlength="16"
							placeholder="Confirm new password" name="confirmpass"
							ng-change="confirmPassError()" disallow-spaces> <span
							id="confirmPasserror" style="color: red;"
							></span>
					</div>
					<div class="clearfix"></div>
				</div>
				
				<div class="form-group">
				   <div class="col-md-4 col-sm-4">	  
					<label class="control-label"
						for="mname"></label>
					</div>					
					<div class="col-sm-5 col-md-4 text-center">
						<button type="submit" id="btnSubmit"
							ng-click="changeUserPassword()"
							class="button submit-selection" value="Submit">Submit</button>
					</div>					
				</div>
				<span style="font-size: 12px;">Note:- <span style="color: red;">*</span>(Changing your password will sign you out from your device. You will need to enter your new password to sign in again)</span>
				<div class="clearfix"></div>
			</div>
			</form>
	    </div>
<!-- 	  </form> -->
	</div>
	
	<div id="pop" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="successhead"><img alt="" src="resources/images/icons/Messages_success_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; SUCCESS</div>
					<div class="successbody">{{msg}}</div>
					<button type="button" class="btn" data-dismiss="modal"  ng-click="redirectToLogout();">OK</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="errorMessage" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-body text-center">
					<div class="errorhead"><img alt="" src="resources/images/icons/Messages_warning_caution_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; ERROR</div>
					<div class="errorbody padding-errorbody">{{validationMsg}}</div>
					<button type="button" class="btn errorOk" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
		
<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>
<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
	var="bootstrapjs" />
<script src="${bootstrapjs}"></script>
<spring:url value="/webjars/angularjs/1.5.5/angular.min.js"
	var="angularmin" />
<script src="${angularmin}" type="text/javascript"></script>
<jsp:include page="fragments/footer.jsp"></jsp:include>	
<script type="text/javascript" src="resources/js/angularController/changePasswordController.js"></script>

	<!-- <script type="text/javascript">
		var app = angular.module("passwordChangeApp", []);
		var myAppConstructor = angular.module("passwordChangeApp");
	 	myAppConstructor.controller("changePasswordController",
				changePasswordController);	
	</script> -->
		
<script type="text/javascript">
$(document).ready(function(){
	$("ul.submenu").each(function() {
		if ($(this).find("a.active").length) {
			$(this).css("display", "block");
			$(this).prev().find("i.fa-chevron-down").css("transform", "rotate(-180deg)");
		}
	});
	$("ul.pageLinks.mainmenu > li a[href='#']").click(function() {
		if ($(this).next(".submenu").css("display") == 'none'){
			$(this).next(".submenu").slideDown("slow");
			$(this).addClass("opened");
			$(this).find("i.fa-chevron-down").css("transform", "rotate(-180deg)");
		}
		else{
			$(this).next(".submenu").slideUp("slow");
			$(this).removeClass("opened");
			$(this).find("i.fa-chevron-down").css("transform", "rotate(0deg)");
		}
			
	});
	$(".slideMenu").css("height", $(window).height()-100);
	if($(window).width() <= 1024){
		$(".slideMenu").css("height", "auto");
	}
	$(".menuSlideBtn button").click(function(e) {
		$(".slideMenu").animate({
			left : 0
		}, 500);
		e.stopPropagation();
	});
	$('body').click(function(evt){    
	    if(evt.target.id == "slideMenu")
	       return;
	    //For descendants of menu_content being clicked, remove this check if you do not want to put constraint on descendants.
	    else if($(evt.target).closest('#slideMenu').length)
	       return;             
	
	   //Do processing of click event here for every element except with id menu_content
	    else{
	 	   $("#slideMenu").animate({
		   			left: "-250px"
		   		}, 500);
	    }
	});
	$(".slide-menu-icon").click(function(){
		$(".slideMenu").animate({
			left: "-250px"
		}, 500);
	});
});
</script>

</body>

</html>