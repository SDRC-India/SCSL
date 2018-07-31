<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html>
<head>

<title>SCSL</title>
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
</head>

<body>

<jsp:include page="fragments/header.jsp"></jsp:include>

	<div id="mymain" class="container">
		<!-- <div style="position: relative">
				<div class="pageNameContainer">
					<h4>Privacy Policy</h4>
				</div>
		</div> -->
		
		<div class="col-md-12 col-sm-12 col-xs-12 " style="margin-top: 40px;">
					<div class="terms-margin"><h3>Privacy Policy</h3></div>
					<p class="termsdata">This privacy policy sets out how SCSL
						uses and protects any information that you give while you use this
						website.</p>
					<p class="termsdata">SCSL is committed to ensuring that your
						privacy is protected. When we ask you to provide certain
						information by which you can be identified when using this
						website, then you can be assured that it will only be used in
						accordance with this privacy statement.</p>
					<p class="termsdata">SCSL may change this policy from time to
						time by updating this page. You should check this page from time
						to time to ensure that you are happy with any changes.</p>
					<h3 class="privacyinfo">What we collect</h3>
					<p class="termsdata">While using our site, we may ask you to
						provide us with certain Personal Information (information that can
						be used to contact or identify you) and Non-Personal Information.
					</p>
					<h3 class="privacyinfo">What we do with the information we gather</h3>
					<p class="termsdata">Except as otherwise stated in this privacy
						policy, we do not sell, trade, rent or otherwise share for
						marketing purposes your personal information with third parties
						without your consent. In general, the Personal Information you
						provide to us is used to help us communicate with you. For
						example, we use Personal Information to contact users in response
						to questions, solicit feedback from users, provide technical
						support, and inform users about promotional offers.</p>
					<h3 class="privacyinfo">Security</h3>
					<p class="termsdata">We are committed to ensuring that your
						information is secure. In order to prevent unauthorized access or
						disclosure, we have put in place suitable physical, electronic and
						managerial procedures to safeguard and secure the information we
						collect online.</p>
					<h3 class="privacyinfo">Why we use cookies</h3>
					<p class="termsdata">The site may use cookies to enhance users'
						experience. Cookies help us provide you with a better website, by
						enabling us to monitor which pages you find useful and which you
						do not. A cookie in no way gives us access to your computer or any
						information about you, other than the data you choose to share
						with us. The user may choose to set their web browser to refuse
						Cookies or alert the user when the Cookies are being sent.
						However, this may prevent you from taking full advantage of the
						website.</p>
					<h3 class="privacyinfo">Links to other websites</h3>
					<p class="termsdata">At many places in this website, you will
						find links to other websites/ portals. These links have been
						placed for your convenience. SCSL has no control over the
						nature, content and availability of those sites. The inclusion of
						any links does not necessarily imply a recommendation or endorse
						the views expressed within them.</p>
				<!-- 	<h3 class="privacyinfo">Copyright Policy</h3>
					<p class="termsdata">This website and its content is copyright
						of SCSL - © Arogyasri 2017. All rights reserved. Any
						redistribution or reproduction of part or all of the contents in
						any form is prohibited other than the following:</p>
					<ul class="termsdata">
						<li>You may reproduce the content partially or fully, with
							duly & prominently acknowledging the source.</li>
					</ul>
					<p class="termsdata">However, the permission to reproduce any
						material that is copyright of any third party has to be obtained
						from the copyright holders concerned. The contents of this website
						cannot be used in any misleading or objectionable context or
						derogatory manner.</p> -->
					<!-- <h3 class="privacyinfo">Contact Us</h3>	
						<p class="termsdata">If the users have any question, concerns or consent, they would write to us at:<br><br>
					 National Institute of Health & Family Welfare Campus,<br>
					  Baba Gangnath Marg, Munrika, New Delhi, Delhi 110067<br>
					Contact No. 011-26108982/83/84/92/93<br>
					Fax No. 011-26108994<br>
					Email Id: nhsrc.india@gmail.com
					
					</p> -->
						<br><br><br>
				</div>
	
	</div>
<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>
<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
	var="bootstrapjs" />
<script src="${bootstrapjs}"></script>
<jsp:include page="fragments/footer.jsp"></jsp:include>	
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
	$(".slideMenu").css("height", $(window).height()-167);
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
})
</script>
</body>

</html>