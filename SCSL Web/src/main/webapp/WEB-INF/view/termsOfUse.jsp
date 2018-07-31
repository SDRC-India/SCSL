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
<style>
	.bottomfooter{
		position: fixed !important;
		bottom: 0 !important;
	}
</style>

</head>

<body>

<jsp:include page="fragments/header.jsp"></jsp:include>

	<div id="mymain" class="container">
		<!-- <div style="position: relative">
				<div class="pageNameContainer">
					<h4>Terms Of Use</h4>
				</div>
		</div> -->
		
		<div class="col-md-12 col-sm-12 col-xs-12" style="margin-top: 40px;">
				<div class="terms-margin"><h3>Terms of Use</h3></div>
					<p class="termsdata">Welcome to our website. If you continue to browse and use this
					 website ,you are agreeing to comply with and be bound by the following
					  terms and conditions of use: </p>
					  <ul class="termsdata">
						<li>The content of the pages of this website is for your
							general information and use only. It is subject to change without
							notice.</li>
						<li>This website does not provide any warranty or guarantee
							as to the accuracy, timeliness, performance, completeness or
							suitability of the information and materials found or offered on
							this website for any particular purpose. You acknowledge that
							such information and materials may contain inaccuracies or errors
							and we expressly exclude liability for any such inaccuracies or
							errors to the fullest extent permitted by law.</li>
						<li>Your use of any information or materials on this website
							is entirely at your own risk, for which we shall not be liable.</li>
						<li>This website contains material which is owned by or
							licensed to us. This material includes, but is not limited to,
							the design, layout, look, appearance and graphics. Reproduction
							is prohibited other than in accordance with the copyright notice,
							which forms part of these terms and conditions</li>
						<li>All trademarks reproduced in this website, which are not
							the property of, or licensed to the operator, are acknowledged on
							the website</li>
						<li>Unauthorized use of this website may give rise to a claim
							for damages and/or be a criminal offence.</li>
						<li>From time to time, this website may also include links to
							other website(s). These links are provided for your convenience to
							provide further information. They do not signify that we endorse
							the website(s). We have no responsibility for the content of the
							linked website(s).</li>


					</ul>
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
})
</script>
</body>

</html>