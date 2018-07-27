

function submissionController($scope, $http, $filter,allServices){
	$scope.activeMenu = "submissionManagement";
	$scope.pageName = "Submission Management";
	$scope.sortReverse = false;
	$scope.sortIndReverse = false;
	$scope.role = role;
	
	$("#loader-mask").show();
	
	$scope.convert = function(array){
	    var map = {};
	    for(var i = 0; i < array.length; i++){
	        var obj = array[i];
	        if(obj.parentAreaId == -1)
	        	obj.parentAreaId =  null;
	        if(!(obj.areaId in map)){
	            map[obj.areaId] = obj;
	            map[obj.areaId].children = [];
	        }

	        var parent = obj.parentAreaId || '-';
	        if(!(parent in map)){
	            map[parent] = {};
	            map[parent].children = [];
	        }
	        map[parent].children.push(map[obj.areaId]);
	    }
	    return map['-'];
	};
	
	allServices.getAllPDSAs().then(function(data){
		$(".loader").fadeOut();
		$scope.areaList = $scope.convert(data.areaList).children;
	}, function(){

	});
	$scope.selectState = function(state){
		$scope.selectedState = state;
		$scope.selectedDistrict=undefined;
		$scope.selectedFacility = undefined;
		$scope.filter();
	};
	$scope.selectDistrict = function(district){
		$scope.selectedDistrict = district;
		$scope.selectedFacility = undefined;
		$scope.filter();
	};
	$scope.selectFacility = function(facility){
		$scope.selectedFacility = facility;
		$scope.filter();
	};
	
	

	allServices.getSuperitendentMnE().then(function(data){
		$("#loader-mask").fadeOut();
		$scope.totalIndicatorList = data;
		$scope.indicatorList = $scope.totalIndicatorList;
		$scope.columns = Object.keys($scope.indicatorList[0]);
		
	}, function(){
		
	});
	
	$scope.resetDiv = function(){
		$scope.selectedState = undefined;
		$scope.selectedDistrict =undefined;
		$scope.selectedFacility = undefined;
		$scope.indicatorList=[];
		$scope.filter();
	};
	
	
	$scope.filter = function(){
		if($scope.selectedState==undefined && $scope.selectedDistrict == undefined && $scope.selectedFacility == undefined)
			{
			$scope.indicatorList = $scope.totalIndicatorList;
			}
		else if ($scope.selectedDistrict == undefined && $scope.selectedState!=undefined)
		{
			$scope.indicatorList = $filter('filter')($scope.totalIndicatorList, {stateId:$scope.selectedState.areaId}, true);
		}
		else if($scope.selectedDistrict != undefined && $scope.selectedFacility==undefined)
			{
			$scope.indicatorList = $filter('filter')($scope.totalIndicatorList, {districtId:$scope.selectedDistrict.areaId}, true);
			}
		else if($scope.selectedDistrict != undefined && $scope.selectedFacility!=undefined)
		{
		$scope.indicatorList = $filter('filter')($scope.totalIndicatorList, {facilityId:$scope.selectedFacility.areaId}, true);
		}
		$(".loader").fadeOut();
	};
	

	$scope.reloadPage = function(){
		window.loaction.href = '';
	};
	$scope.filterFloat = function(value) {
	        if (/^(\-|\+)?([0-9]+(\.[0-9]+)?|Infinity)$/
	        	      .test(value))
	        	      return Number(value);
	        	  return value;
	  }
	$scope.order = function (sortType) {  
        $scope.sortReverse = ($scope.sortType === sortType) ? !$scope.sortReverse : false;  
        $scope.sortType = sortType;  
      };
      $scope.orderSubmissionIndicators = function (sortType) {  
          $scope.sortIndReverse = ($scope.sortType === sortType) ? !$scope.sortIndReverse : false;  
          $scope.sortType = sortType;  
        };
    $scope.filterType = function(val){
    	/*if(val['District'] == "Chhattisgarh"){
    		if(isNaN(parseInt(val[$scope.sortType]))){
	    		if($scope.sortReverse == true)
	    			return "";
	    		else
	    			return "zzz";
    		}	
    		else{
    			if($scope.sortReverse == true)
	    			return -1;
	    		else
	    			return 9e12;
    		}
    	}*/
    	if(isNaN(parseInt(val[$scope.sortType])))
    		if(!$scope.sortType)
    			return true;
    		else
    			return val[$scope.sortType];
    	else
    		return parseInt(val[$scope.sortType]);
    };
    $scope.filterIndicatorType = function(val){
    	if(isNaN(parseInt(val[$scope.sortType])))
    		if(!$scope.sortType)
    			return true;
    		else
    			return val[$scope.sortType];
    	else
    		return parseInt(val[$scope.sortType]);
    };
    
    $scope.removeRowId = function(item){
		 return item != 'District' && item != 'State' && item !='FacilityId';
	};
	
	$scope.viewSubmission = function(data, isLatest){
		$scope.sortType = false;
		if(data.refSubmissionId == null){
			data.refSubmissionId = "";
		}
		
		$("#loader-mask").show();
		$scope.selectedTxnSubmissionId = data.txnSubmissionId;
		$scope.selectedRow = data;
		if($scope.selectedRow.remarkByMnE && $scope.selectedRow.remarkByMnE != 'null'){
			$scope.remark=$scope.selectedRow.remarkByMnE;
		}
		else if(!$scope.selectedRow.remarkByMnE && $scope.selectedRow.remarkBySuperintendent && $scope.selectedRow.remarkBySuperintendent != 'null' && $scope.role == 2){
			$scope.remark=$scope.selectedRow.remarkBySuperintendent;
		}
		else{
			$scope.remark = null;
		}
		if(isLatest)
		$scope.previousIndicatorList = data.submittedDataModelLogList;
		allServices.getSubmissionInicators(data.txnSubmissionId, data.refSubmissionId, data.facilityId, data.timePeriodId).then(function(data){
			$("#loader-mask").fadeOut();
			$scope.submissionIndicatorList = data;
			$scope.submissionIndicatorColumns = Object.keys($scope.submissionIndicatorList[0]);
			$("#viewSubmissionModal").modal("show");
		}, function(){
			
		});
	};
	
	$scope.viewSubmissionLog = function(data, isLatest){
		$scope.sortType = false;
		$scope.sortReverse = false;
		if(data.refSubmissionId == null){
			data.refSubmissionId = "";
		}
		
		$scope.selectedTxnSubmissionId = data.txnSubmissionId;
		$scope.selectedRow = data;
		if(isLatest)
			$scope.previousIndicatorList = data.submittedDataModelLogList;
		if($scope.previousIndicatorList != null)
			$('#viewLogModal').modal("show");
		else{
			$scope.infoMsg = "No history available for this submission";
			$('#infoMessage').modal("show");
		}
	};
	
	$scope.approveOrRejectSubmission = function(isApprove){
		$("#loader-mask-submission").show();
		$("#viewSubmissionModal").modal("hide");
		allServices.approveOrRejectSubmission($scope.selectedTxnSubmissionId, $scope.remark, isApprove).then(function(data){
			$("#loader-mask-submission").fadeOut();
			
			if(data.statusCode==200)
				{
			$scope.msg = data.statusMessage;
//			if(isApprove)
//				$scope.msg = "Submission approved";
//			else
//				$scope.msg = "Submission rejected";
			$("#pop").modal("show");
				}
		else
			{
			$scope.errorMsg = data.statusMessage;
			$("#errorMessage").modal("show");
			}
		}, function(){
			
		});
	};
	
	$scope.warningForRemark = function(){
		$scope.errorMsg = "Please fill remark with the cause of rejection";
		$("#errorMessage").modal("show");
	};
	
	
	$scope.viewReadMore =function ($event,selectedSubmission){
		$('#viewRemarkMore').modal('show');
		$scope.selectedSubmission = selectedSubmission;
	}
	
	$scope.viewReadMoreMnE =function ($event,selectedSubmissionMnE){
		$('#viewReadMoreMnE').modal('show');
		$scope.selectedSubmissionMnE = selectedSubmissionMnE;
	}
}
$(document).ready(function(){
	$("#viewLogModal .modal-dialog").css("height", $(window).height() );
	$("#viewSubmissionModal .modal-dialog").css("height", $(window).height() );
});