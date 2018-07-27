function engagementScoreController($scope, $http,$filter, allServices){
	$scope.activeMenu = "engagementScore";
	$scope.pageName = "Engagement Score";
	
	$(".loader").show();
	allServices.getMSTEngagementScoreData().then(function(data){
		$(".loader").fadeOut();
		$scope.engagementScoreData = data;
		$scope.facilityModel = data.facilityModel;
		$scope.mstEngagementScoreModel = data.mstEngagementScoreModel;
		$scope.columns = Object.keys($scope.mstEngagementScoreModel[0]);
		$scope.selectFacility($scope.facilityModel[0]);
		if($scope.facilityModel[0].timeperiods[0].mstEngagementScoreId){
			$scope.selectFacility($scope.facilityModel[0]);
			$scope.selectTimeperiod($scope.selectedFacility.timeperiods[0]);
//			$scope.selectEngagementScore($scope.selectedFacility.timeperiods[0].mstEngagementScoreId);
//			for(var i=0; i<$scope.mstEngagementScoreModel.length; i++){
//				if($scope.mstEngagementScoreModel[i].mstEngagementScoreId == $scope.selectedFacility.timeperiods[0].mstEngagementScoreId){
////					$scope.selectedEngagementScore = $scope.mstEngagementScoreModel[i];
////					$("input[type='radio'][value=" + $scope.selectedFacility.timeperiods[0].mstEngagementScoreId +"]").next().find("span").click();
//				}
//			}
		}
	}, function(){
		
	});
	
	
	$scope.selectFacility = function(facility){
		$scope.selectedFacility = facility;
		if($scope.selectedFacility.timePeriodId!=0)
			{
		
		$scope.selectTimeperiod($filter('filter')($scope.selectedFacility.timeperiods, {timePeriodId:$scope.selectedFacility.timePeriodId},true)[0]);
			}
		else
			{
			$scope.selectTimeperiod($scope.selectedFacility.timeperiods[$scope.selectedFacility.timeperiods.length-1]);
			}
//		$scope.selectTimeperiod($scope.selectedFacility.timeperiods[0]);
		$scope.getLineChartOfEngagementScore();
	};
	$scope.selectTimeperiod = function(timeperiod){
			{
			//if($scope.selectedTimeperiod!=timeperiod)
			{		
		$scope.selectedTimeperiod = timeperiod;				
		$scope.selectedEngagementScore = "";
		if($scope.selectedTimeperiod.mstEngagementScoreId==0)
			{
		$("input[type='radio']:checked").removeAttr("checked");		
			}
			}
			}
	};
	$scope.selectEngagementScore = function(engagementScore){
		$scope.selectedEngagementScore = engagementScore;
	};
	
	$scope.getLineChartOfEngagementScore = function(){
		$(".loader").show();
		allServices.getLineChartOfEngagementScore($scope.selectedFacility.facailityId).then(function(data){
			$(".loader").fadeOut();
			$scope.ldata = [];
			$scope.ldata.push(data);
		}, function(){
			
		});
	};
	
	$scope.submitForm = function(){
		$(".loader").show();
		allServices.saveTXNEngagementScore($scope.selectedEngagementScore.mstEngagementScoreId, $scope.selectedFacility.facailityId, $scope.selectedTimeperiod.timePeriodId).then(function(data){
			$(".loader").fadeOut();
			if(data==true){
				$scope.msg = "The engagement score was saved successfully";
				$("#pop").modal("show");
			}else{
				$scope.errorMsg = "The data has been already submitted for the current facility and timeperiod.";
				$("#errorMessage").modal("show");
			}
			
			
		}, function(){
			
		});
	};
	$scope.reloadPage = function(){
		$("#errorMessage").modal("hide");
		$("#pop").modal("hide");
		$(".loader").show();
		$scope.selectedEngagementScore=undefined;
		allServices.getMSTEngagementScoreData().then(function(data){
			$scope.engagementScoreData = data;
			$scope.facilityModel = data.facilityModel;
			$scope.mstEngagementScoreModel = data.mstEngagementScoreModel;
			$scope.columns = Object.keys($scope.mstEngagementScoreModel[0]);
			$scope.selectFacility($filter('filter')($scope.facilityModel, {facailityId:$scope.selectedFacility.facailityId},true)[0]);
			//angular.element('#pageLineChart').focus();
/*			   $location.hash('pageLineChart');

			    // call $anchorScroll()
			    $anchorScroll();*/
			$('html, body').animate({
		        scrollTop: $("#pageLineChart").offset().top
		    }, 2000);
			$(".loader").fadeOut();
		}, function(){
			
		});
	};
	
}