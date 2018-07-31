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
		<div style="margin-top: 150px"><h2 class="text-center" style="font-weight: bold;">UNDER CONSTRUCTION</h2>
			<h4 class="text-center">We are currently working on this page.</h4>
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