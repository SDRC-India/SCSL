<!-- 
@author Laxman (laxman@sdrc.co.in)
 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>
<!DOCTYPE html>

<html ng-app="homeApp">
<head>

<title>SCSL-Home</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport">
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapjs" />
<link href="${bootstrapjs}" rel="stylesheet" />
<spring:url value="/resources/css/font-awesome.min.css" var="fontawesomeCss" />
<link href="${fontawesomeCss}" rel="stylesheet" />
<spring:url value="/resources/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet" />
<spring:url value="/resources/css/customLoader.css" var="customLoaderCss" />
<link href="${customLoaderCss}" rel="stylesheet" />
<spring:url value="/resources/css/jquery-ui.css" var="jqueryUiCss" />
<link href="${jqueryUiCss}" rel="stylesheet" />
<%@taglib prefix="serror" uri="/WEB-INF/ErrorDescripter.tld"%>
<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>
<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
	var="bootstrapjs" />
<script src="${bootstrapjs}"></script>
<!-- <script src="resources/js/angular.min.js"></script> -->
<style type="text/css">
/* @media (max-width: 767px){
		div#mymain{
			margin-bottom: 0px;
		}
	} */
</style>
</head>

<body ng-controller="HomeController">
	<style type="text/css">
div#mymain {
	margin-bottom: 0px !important;
}
</style>
	<jsp:include page="fragments/header.jsp"></jsp:include>
	<div id="mymain">
		<div class="container" style="position: relative">
			<div class="pageNameContainer">
				<h4>Contact Us</h4>
			</div>
		</div>
		<div class="container">
			<div class="col-md-12">
				<div class="row contact-detail">
					<div class="col-md-4 location">
						<b>Aarogyasri Health Care Trust</b><br> D.No.
						8-2-293/82a/ahct, <br>Road No: 46, Jubilee Hills, <br>Hyderabad
						- 500033 <br>Contact : 040-23547107 <br>Fax No :
						040-23555657 <br>E-mail : ceots@aarogyasri.gov.in
					</div>
					<div class="col-md-8 contact-form-section">
						<div class="col-md-12">
							<div class="row">
								<div class="col-md-6">
									<input type="text" placeholder="Name" name="name"
										class="input-contact">
									<textarea rows="" cols="" class="textarea-contact"
										name="message" placeholder="Message"></textarea>
								</div>
								<div class="col-md-6">
									<input type="email" placeholder="Email Id" name="email"
										class="input-contact">
									<div class="">
										<!-- BEGIN: ReCAPTCHA implementation example. -->
										<div id="recaptcha-demo" class="g-recaptcha"
											data-sitekey="6Le-wvkSAAAAAPBMRTvw0Q4Muexq9bi0DJwx_mJ-"
											data-callback="onSuccess"></div>
										<script>
											var onSuccess = function(response) {
												var errorDivs = document
														.getElementsByClassName("recaptcha-error");
												if (errorDivs.length) {
													errorDivs[0].className = "";
												}
												var errorMsgs = document
														.getElementsByClassName("recaptcha-error-message");
												if (errorMsgs.length) {
													errorMsgs[0].parentNode
															.removeChild(errorMsgs[0]);
												}
											};
										</script>
										<!-- Optional noscript fallback. -->
										<noscript>
											<div style="width: 302px; height: 462px;">
												<div
													style="width: 302px; height: 422px; position: relative;">
													<div
														style="width: 302px; height: 422px; position: absolute;">
														<iframe
															src="https://www.google.com/recaptcha/api/fallback?k=6Le-wvkSAAAAAPBMRTvw0Q4Muexq9bi0DJwx_mJ-"
															frameborder="0" scrolling="no"
															style="width: 302px; height: 422px; border-style: none;"></iframe>
													</div>
												</div>
												<div
													style="border-style: none; bottom: 12px; left: 25px; margin: 0px; padding: 0px; right: 25px; background: #f9f9f9; border: 1px solid #c1c1c1; border-radius: 3px; height: 60px; width: 300px;">
													<textarea id="g-recaptcha-response"
														name="g-recaptcha-response" class="g-recaptcha-response"
														style="width: 250px; height: 40px; border: 1px solid #c1c1c1; margin: 10px 25px; padding: 0px; resize: none;"></textarea>
												</div>
											</div>
											<br>
										</noscript>
										<!-- END: ReCAPTCHA implementation example. -->
									</div>
									<input type="submit" value="Submit" style="margin-top: 9px;"
										class="submit-contact input-contact">
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
		<div class="mapSection">
			<iframe
				src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3806.620059545569!2d78.39134331443947!3d17.430011888053134!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3bcb9151bd248219%3A0xa976fe1a43d92dfe!2sAarogyasri+Health+Care+Trust!5e0!3m2!1sen!2sin!4v1497251123874"
				width="100%" height="300" frameborder="0" style="border: 0"
				allowfullscreen></iframe>
		</div>

	</div>

	<!--end of thematic and checklist  -->
	<jsp:include page="fragments/footer.jsp"></jsp:include>
	<!-- <script src="resources/js/angularController/loginController.js"></script> -->
	<!-- <script type="text/javascript">
		var app = angular.module("homeApp", []);
		var myAppConstructor = angular.module("homeApp");
		myAppConstructor.controller("HomeController",
				engagementScoreController);
		myAppConstructor.service('allServices', allServices);
	</script>
	<script type="text/javascript" src="resources/js/angularDirective/directive.js"></script> -->
	<script type="text/ecmascript">
		
		$(document).ready(function() {
			$(".loginPopBtn button").click(function(e) {
				$(".loginPopForm").animate({
					right : 0
				}, 500);
				e.stopPropagation();
			});
			$('body').click(function(evt) {
				if (evt.target.id == "loginPopForm")
					return;
				//For descendants of menu_content being clicked, remove this check if you do not want to put constraint on descendants.
				else if ($(evt.target).closest('#loginPopForm').length)
					return;

				//Do processing of click event here for every element except with id menu_content
				else {
					$(".loginPopForm").animate({
						right : "-251px"
					}, 500);
				}

			});
			$(".slideMenu").css("height", $(window).height() - 75);
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
	<script type="text/javascript">
		$(document).ready(
				function() {
					$("ul.submenu")
							.each(
									function() {
										if ($(this).find("a.active").length) {
											$(this).css("display", "block");
											$(this).prev().find(
													"i.fa-chevron-down").css(
													"transform",
													"rotate(-180deg)");
										}
									});
					$("ul.pageLinks.mainmenu > li a[href='#']")
							.click(
									function() {
										if ($(this).next(".submenu").css(
												"display") == 'none') {
											$(this).next(".submenu").slideDown(
													"slow");
											$(this).addClass("opened");
											$(this).find("i.fa-chevron-down")
													.css("transform",
															"rotate(-180deg)");
										} else {
											$(this).next(".submenu").slideUp(
													"slow");
											$(this).removeClass("opened");
											$(this).find("i.fa-chevron-down")
													.css("transform",
															"rotate(0deg)");
										}

									});
					$("div#loginPopForm input").focus(
							function() {
								if ($(window).height() <= 665
										&& $(window).width() <= 1024) {
									$("div#loginPopForm").css({
										"z-index" : "9999",
										"top" : "0",
										"transform" : "translateY(0)"
									});
									$(".loginPopBtn1").hide();
								}
							});
					$("div#loginPopForm input").blur(
							function() {
								if ($(window).height() <= 665
										&& $(window).width() <= 1024) {
									if ($(window).height() <= 330) {
										$("div#loginPopForm").css({
											"z-index" : "9999",
											"top" : "51px",
											"transform" : "translateY(0)"
										})
									} else {
										$("div#loginPopForm").css({
											"z-index" : "9999",
											"top" : "75px",
											"transform" : "translateY(0)"
										})
									}

									$(".loginPopBtn1").show();
								}
							});
				});
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			setTimeout(function() {
				if (role == 0) {
					$(".loginPopBtn").show();
				}
				$("#recaptcha-demo").css("transform", "scale(" + $("#recaptcha-demo").width()/304 + ")");
			}, 500);
			
		})
	</script>
	<script type="text/javascript">
		$("#msgBox").show().delay(2000).fadeOut(400);
	</script>
	<script src='resources/js/captchaApi.js' async defer></script>
</body>

</html>