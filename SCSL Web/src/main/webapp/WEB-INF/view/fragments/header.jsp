<!-- 
@author Laxman (laxman@sdrc.co.in)
 -->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="org.sdrc.scsl.util.Constants"%>
<%@ page import="org.sdrc.scsl.model.web.UserModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page
	import="org.sdrc.scsl.model.web.UserRoleFeaturePermissionMappingModel"%>
<!--logo part end-->
<!-- spinner -->
<%
	Integer role = 0;
UserModel user = null; Boolean hasLR = false;
List<String> features = new ArrayList<String>();
List<String> permissions = new ArrayList<String>();

if (request.getSession().getAttribute(Constants.Web.USER_PRINCIPAL) != null) {
		user = (UserModel) request.getSession().getAttribute(Constants.Web.USER_PRINCIPAL);

		role = user.getUserAreaModels().get(0).getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole().getRoleId();
		hasLR = user.isHasLr();

		List<UserRoleFeaturePermissionMappingModel> ursMappings = new ArrayList<UserRoleFeaturePermissionMappingModel>();
		ursMappings = user != null ? user.getUserAreaModels().get(0).getUserRoleFeaturePermissionMappings()
				: null;
		if (ursMappings != null && !ursMappings.isEmpty()) {
			for (UserRoleFeaturePermissionMappingModel ursm : ursMappings) {

				features.add(ursm.getRoleFeaturePermissionSchemeModel().getFeaturePermissionMapping()
						.getFeature().getFeatureName());
				permissions.add(ursm.getRoleFeaturePermissionSchemeModel().getFeaturePermissionMapping()
						.getPermission().getPermissionName());
			}
		}
	}
%>
<script>
	var role = <%=role%>;
	var hasLR = <%=hasLR%>;
</script>

<!-- 
<div id="spinner" class="loader" style="display: none;"></div>
<div id="loader-mask" class="loader" style="display: none;"></div> -->
<!-- /spinner -->
<nav class="navbar nav-menu-container">
<!-- 	<div class="container-fluid header-section">
		<div class="col-md-12 navbar-header">
			<div class="logoresize">
				<div class="heading_partDesktop heading_part">
					<h4 class="headerinfo">
						<div class="logo-header">
							<a class="ah" href="http://accessh.org/" target="_blank"><img alt="access health international" style="width: 162px;" src="resources/images/ah-logo1.png"></a>
						</div>
						<div class="logo-header"> 
							<a href="home"><img alt="scsl" style="width: 63px;" src="resources/images/scsl_app_icon.png"><div class="logo-text">Safe Care,<br> Saving Lives</div></a>
						</div>
						<div class="logo-right">
							<a href="http://www.aarogyasri.telangana.gov.in" target="_blank"><img alt="Arogyasri" style="width: 70px;" src="resources/images/arogyasri.png"></a>
						</div>
					</h4>
				</div>
			</div>
		</div>
	</div> -->
	<button class="navbar-toggle custom-navbar-mobile" style="z-index: 777; background-color: #333A3B;"
		data-toggle="collapse" data-target=".navbar-menu-collapse">
		<span class="icon-bar" style="background-color:#fff";></span> <span class="icon-bar" style="background-color:#fff";></span> <span
			class="icon-bar" style="background-color:#fff";></span>
	</button>
	<div class="container-fluid nav-section">
		<div class="logo-header"> 
							<a href="<spring:url value="/" htmlEscape="true" />"><img alt="scsl" style="width: 63px;"
							 src="<spring:url value="/resources/images/scsl_app_icon.png" htmlEscape="true" />"><div class="logo-text">Safe Care,<br> Saving Lives</div></a>
						</div>
		<div
			class="col-md-7 navHeaderCollapse2 navbar-menu-collapse collapse navbar-collapse" data-hover="dropdown" style="padding-top:7px;"> 
			 <%if(user!=null){ %>
			<div class="welcome-user hidden-sm">
				<div style="color: #343a3b;">Welcome<p style="color: #ffcc00;"><%=user.getName()%></p></div>
			</div>
			<%} %> 
			<ul class="nav navbar-nav navbar-right nav-submenu nav-place-right">

				<li class="home"><a href="<spring:url value="/" htmlEscape="true" />"><div>&nbsp;Home</div></a></li>
				<li class="about"><a href="<spring:url value="/#aboutUs" htmlEscape="true" />"><div>&nbsp;About Us</div></a></li>
				<li class="resource"><a href="<spring:url value="/resource" htmlEscape="true" />"><div>&nbsp;Resources</div></a></li>
				<li class="stories"><a href="<spring:url value="/#successStories" htmlEscape="true" />"><div>&nbsp;Success Stories</div></a></li>
				<li class="partners"><a href="<spring:url value="/#partner-sec" htmlEscape="true" />"><div>&nbsp;Partners</div></a></li>
				<li class="contact"><a id="contactUs-ref" href="<spring:url value="/#contactUs-sec" htmlEscape="true" />"><div>&nbsp;Contact Us</div></a></li>
				<!-- 				<li ng-class="{'active' : activeMenu == 'report'}"><a href="login"><div>&nbsp;Login</div></a></li> -->
				
			</ul>
		</div>
	</div>
</nav>
<%if(user!=null){ %>
<div class="menuSlideBtn">
	<button>
		Menu <i class="fa fa-long-arrow-right" aria-hidden="true"></i>
	</button>
</div>
<%} %>
<div class="slideMenu" id="slideMenu">
	<div>
		<div class="slide-head-menu slide-head">Menu</div>
		<div class="slide-head slide-menu-icon">
			<i class="fa fa-long-arrow-left" aria-hidden="true"></i>
		</div>
	</div>
	<div class="link-container">
		<%
			if (user != null) {
		%>
		<div class="username"><%=user.getName()%></div>

		<ul class="facilityLevel">
					<%
			if (user.getStateName()!=null) {
		%>
			<li><div style="padding-left: 40px;"><span>State:</span> <%=user.getStateName()%></div></li>
			<%
			}
			if (user.getDistrictName()!=null) {
		%>
			<li><div style="padding-left: 40px;"><span>District:</span> <%=user.getDistrictName()%><div></li>
			<%
			} if (user.getFacilityName()!=null && user.getAreaLevel()==4 && (user.getRoleName().equals("DEO") 
					|| user.getRoleName().equals("Superintendent") || user.getRoleName().equals("Unit In charge") ))
			{
			%>
			<li><div style="padding-left: 40px;"><span>Facility:</span> <%=user.getFacilityName()%><div></li>
			<%} 
			 if (user.getCountry()!=null && user.getAreaLevel()==1)
				{
				%>
				<li><div style="padding-left: 40px;"><span>Area:</span> <%=user.getCountry()%><div></li>
				<%} %>
			
		</ul>
		<%
			}
		%>
		<ul class="pageLinks mainmenu">
		<% if(features.contains("pdsa")||features.contains("sncuDataEntry")) {%>
			<li><a href="#"><span>Data Entry</span>&nbsp;<i class="fa fa-chevron-down" aria-hidden="true"></i></a>
				<ul class="submenu">
				<% if(features.contains("sncuDataEntry")) {%>
					<li><a href=<spring:url value="/sncuDataEntry" htmlEscape="true" /> ng-class="{'active': activeMenu == 'sncu-data-entry'}">SNCU/NICU</a></li>
					<%} if(features.contains("pdsa")) {%>
					<li><a href=<spring:url value="/pdsa" htmlEscape="true" />  ng-class="{'active': activeMenu == 'pdsaDataEntry'}">PDSA</a></li>
					<%} %>
				</ul>
			</li>
			<%} %>
			<% if(features.contains("dashboardHome")||features.contains("pdsaSummary")||features.contains("dashboardSmallMultiple")||features.contains("dashboardFacilityView")){%>
			<li><a href="#"><span>Dashboard</span>&nbsp;<i class="fa fa-chevron-down" aria-hidden="true"></i></a>
				<ul class="submenu">
				<% if(features.contains("dashboardHome")) {%>
					<li><a href=<spring:url value="/dashboardHome" htmlEscape="true" />  ng-class="{'active': activeMenu == 'dashboardHome'}">Home</a></li>
					<li><a href=<spring:url value="/dashboardFacilityView" htmlEscape="true" />  ng-class="{'active': activeMenu == 'dashboardFacilityView'}">Facility View</a></li>
					<li><a href=<spring:url value= "/pdsaSummary" htmlEscape="true" /> ng-class="{'active': activeMenu == 'pdsaSummary'}">PDSA Summary</a></li>
					<li><a href=<spring:url value="/smallMultiple" htmlEscape="true" /> ng-class="{'active': activeMenu == 'dashboardChartView'}">Small Multiple</a></li>
					<%}  %>
				</ul>
			</li>
			<%} %>
			<% if(features.contains("engagementScore")) {%>
			<li><a href=<spring:url value= "/engagementScore"  htmlEscape="true" /> ng-class="{'active': activeMenu == 'engagementScore'}"><span>Engagement Score</span></a></li>
			<%}if(features.contains("report")) {%>
			<li><a href=<spring:url value="/report" htmlEscape="true" /> ng-class="{'active': activeMenu == 'report'}"><span>Report</span></a></li>
			<%} %>
			
			<% if(features.contains("submissionManagement")) {%>
			<li><a href=<spring:url value="/submissionManagement"  htmlEscape="true" /> ng-class="{'active': activeMenu == 'submissionManagement'}"><span>Submission Management</span></a></li>
			<%} %>
			<%if(features.contains("uploadHistoricalData")) {%>
			<li><a href=<spring:url value="/uploadHistoricalData" htmlEscape="true" /> ng-class="{'active': activeMenu == 'uploadHistoricalData'}"><span>Upload Historical Data</span></a></li>
			<%} %>
			<%if(features.contains("uploadHistoricalData")) {%>
			<li><a href=<spring:url value="/legacySncuDataEntry" htmlEscape="true" /> ng-class="{'active': activeMenu == 'legacySncuDataEntry'}"><span>Historical Data Entry</span></a></li>
			<%} %>
			<%if(features.contains("planning")) {%>
			<li><a href=<spring:url value="/planning" htmlEscape="true" /> ng-class="{'active': activeMenu == 'Plan'}"><span>Plan</span></a></li>
			<%} %>			
			
			<li><a href=<spring:url value="/changePassword"  htmlEscape="true" /> ng-class="{'active': activeMenu == 'changePwd'}">Change Password</a></li>
			
			<%
				if (request.getSession().getAttribute(Constants.Web.USER_PRINCIPAL) != null) {
			%>
				<li><a href=<spring:url value="/webLogout" htmlEscape="true" />>Logout</a></li>
				<%}%>	
		</ul>
	</div>

</div>
<div class="loaderMask" id="loader-mask" >
	<div class="windows8">
		<div class="wBall" id="wBall_1">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_2">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_3">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_4">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_5">
			<div class="wInnerBall"></div>
		</div>
	</div>
</div>
<div class="loaderMaskSubmission" id="loader-mask-submission" >
<div class="windows8header">Saving and sending an email notification...</div>
	<div class="windows8">
		<div class="wBall" id="wBall_1">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_2">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_3">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_4">
			<div class="wInnerBall"></div>
		</div>
		<div class="wBall" id="wBall_5">
			<div class="wInnerBall"></div>
		</div>
	</div>
</div>