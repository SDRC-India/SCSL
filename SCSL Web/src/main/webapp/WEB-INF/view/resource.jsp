<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html>
<head>

<title>SCSL-Resources</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport">
<spring:url value="/resources/images/scsl_app_icon.png" var="appicon" />
<link rel="icon" type="image/png" href="${appicon}">
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<spring:url value="/resources/css/font-awesome.min.css"
	var="fontawesomeCss" />
<link href="${fontawesomeCss}" rel="stylesheet" />
<spring:url value="/resources/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet" />
<style>
.bottomfooter {
	position: fixed !important;
	bottom: 0 !important;
}
</style>

</head>

<body>

	<jsp:include page="fragments/header.jsp"></jsp:include>

	<div id="mymain" class="container">
		<div class="pageNameContainer">
			<h4>Resources</h4>
		</div>
	</div>

	<div class="container">
		<div class="row">
			<div class="col-md-12" style="margin-bottom: 45px;">
				<div class="inner-page-title resource-table-container">
					<h2></h2>

					<table
						class="table table-responsive table-striped factsheet bg_grey">
						<thead>
							<tr>
								<th style="font-weight: bold; color: #3a3a3a;">User Manuals</th>
								<th class="algn_right">Download</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>SCSL User Manual</td>
								<td class="algn_right"><a
									href="resources/files/SCSL User Manual.pdf" target="_blank">
										<i class="fa fa-2x fa-file-pdf-o"></i>
								</a></td>

							</tr>
						</tbody>

					</table>

				</div>

				<div class="inner-page-title resource-table-container">

					<table
						class="table table-responsive table-striped factsheet bg_grey">
						<thead>
							<tr>
								<th style="font-weight: bold; color: #3a3a3a;">User Guide</th>
								<th class="algn_right"></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>SCSL User Guide</td>
								<td class="algn_right"><a href="#"> <i
										class="fa fa-2x fa-file-pdf-o"></i>
								</a></td>
							</tr>
						</tbody>

					</table>

				</div>

				<!-- <div class="inner-page-title resource-table-container">

					<table
						class="table table-responsive table-striped factsheet bg_grey">
						<thead>
							<tr>
								<th style="font-weight: bold; color: #3a3a3a;">Quality
									Improvement Tool Kit</th>
								<th class="algn_right"></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>SCSL Quality Improvement Tool Kit</td>
								<td class="algn_right"><a
									href="resources/files/Quality Improvement Tool Kit.pdf"
									target="_blank"> <i class="fa fa-2x fa-file-pdf-o"></i>
								</a></td>
							</tr>

						</tbody>

					</table>

				</div> -->
				<!-- <div class="inner-page-title resource-table-container">				
						
						<table class="table table-responsive table-striped factsheet bg_grey">							
							<thead>
								<tr>
									<th style="font-weight:bold ;color:#3a3a3a;">Mobile APK</th>
									<th class="algn_right">Download</th>
								</tr>
							</thead>
							<tbody>							
								<tr>
									<td> SCSL Mobile Application </td>
									<td class="algn_right"><a href="resources/files/SCSL UAT v2.0.0.apk">
											<i class="fa fa-2x fa-file-pdf-o"></i>
									</a></td>
								</tr>							
								
							</tbody>

						</table>

					</div> -->
				<!-- <div class="inner-page-title resource-table-container">

					<table
						class="table table-responsive table-striped factsheet bg_grey">
						<thead>
							<tr>
								<th style="font-weight: bold; color: #3a3a3a;">Overview</th>
								<th class="algn_right"></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>Overview of the Safe Care Saving Lives Program</td>
								<td class="algn_right"><a
									href="resources/files/Overview of the Safe Care Saving Lives Program.pdf"
									target="_blank"> <i class="fa fa-2x fa-file-pdf-o"></i>
								</a></td>
							</tr>
						</tbody>

					</table>

				</div> -->
				<!-- 					new docs added bu Dr.Ajit -->
				<!-- <div class="inner-page-title resource-table-container">

					<table
						class="table table-responsive table-striped factsheet bg_grey">
						<thead>
							<tr>
								<th style="font-weight: bold; color: #3a3a3a;">Estimates of
									Mortality Indicators</th>
								<th class="algn_right">Download</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>Estimates of Mortality Indicators</td>
								<td class="algn_right"><a
									href="resources/files/8.Chap 4-Mortality Indicators-2016.pdf"
									target="_blank"> <i class="fa fa-2x fa-file-pdf-o"></i>
								</a></td>
							</tr>
						</tbody>

					</table>

				</div> -->
				<div class="inner-page-title resource-table-container">

					<table
						class="table table-responsive table-striped factsheet bg_grey">
						<thead>
							<tr>
								<th style="font-weight: bold; color: #3a3a3a;">Sample
									Registration System, 2017</th>
								<th class="algn_right"></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>SRS Bulletin Sample Registration System Registrar
									General, India</td>
								<td class="algn_right"><a
									href="resources/files/SRS Bulletin -Sep_2017-Rate-2016.pdf"
									target="_blank"> <i class="fa fa-2x fa-file-pdf-o"></i>
								</a></td>
							</tr>
						</tbody>
						<tbody>
							<tr>
								<td>Estimates of Mortality Indicators</td>
								<td class="algn_right"><a
									href="resources/files/8.Chap 4-Mortality Indicators-2016.pdf"
									target="_blank"> <i class="fa fa-2x fa-file-pdf-o"></i>
								</a></td>
							</tr>
						</tbody>
					</table>

				</div>
			</div>
		</div>
	</div>

	<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jQuery" />
	<script src="${jQuery}"></script>
	<spring:url value="/webjars/bootstrap/3.1.1/js/bootstrap.min.js"
		var="bootstrapjs" />
	<script src="${bootstrapjs}"></script>
	<jsp:include page="fragments/footer.jsp"></jsp:include>
	<script type="text/javascript">
		$(document).ready(function() {
			$("ul.submenu").each(function() {
				if ($(this).find("a.active").length) {
					$(this).css("display", "block");
					$(this).prev().find("i.fa-chevron-down").css("transform", "rotate(-180deg)");
				}
			});
			$("ul.pageLinks.mainmenu > li a[href='#']").click(function() {
				if ($(this).next(".submenu").css("display") == 'none') {
					$(this).next(".submenu").slideDown("slow");
					$(this).addClass("opened");
					$(this).find("i.fa-chevron-down").css("transform", "rotate(-180deg)");
				} else {
					$(this).next(".submenu").slideUp("slow");
					$(this).removeClass("opened");
					$(this).find("i.fa-chevron-down").css("transform", "rotate(0deg)");
				}
	
			});
			$(".slideMenu").css("height", $(window).height() - 100);
			if ($(window).width() <= 1024) {
				$(".slideMenu").css("height", "auto");
			}
			$(".menuSlideBtn button").click(function(e) {
				$(".slideMenu").animate({
					left : 0
				}, 500);
				e.stopPropagation();
			});
			$('body').click(function(evt) {
				if (evt.target.id == "slideMenu")
					return;
				//For descendants of menu_content being clicked, remove this check if you do not want to put constraint on descendants.
				else if ($(evt.target).closest('#slideMenu').length)
					return;
	
				//Do processing of click event here for every element except with id menu_content
				else {
					$("#slideMenu").animate({
						left : "-250px"
					}, 500);
				}
			});
			$(".slide-menu-icon").click(function() {
				$(".slideMenu").animate({
					left : "-250px"
				}, 500);
			});
		})
	</script>

</body>

</html>