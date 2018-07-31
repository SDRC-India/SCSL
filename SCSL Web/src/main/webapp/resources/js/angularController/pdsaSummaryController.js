function pdsaSummaryController($scope, $http, $filter,allServices){
	$scope.activeMenu = "pdsaSummary";
	$scope.pageName = "PDSA Summary";
	$scope.sortReverse = true;
	$scope.facilityChartView = false;
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

	       /* if(typeof map[obj.areaId].areaName == 'undefined'){
	            map[obj.areaId].areaId = String(obj.areaId);
	            map[obj.areaId].areaName = obj.areaName;
	            map[obj.areaId].parentAreaId= String(obj.parentAreaId);
	        }
*/
	        var parent = obj.parentAreaId || '-';
	        if(!(parent in map)){
	            map[parent] = {};
	            map[parent].children = [];
	        }
	        map[parent].children.push(map[obj.areaId]);
	    }
	    return map['-'];
	};
	
	
	allServices.getPDSAFilterOption().then(function(data) {
		$scope.indicatordata = data.Indicator;
		$scope.coreAreaList = Object.keys($scope.indicatordata);
		$scope.IndicatorList = undefined;
		$scope.changeIdeaList = undefined;
		$scope.frequencyList = data.frequencies;
	}, function() {

	});
	
	allServices.getAllPDSAs().then(function(data){
		$("#loader-mask").fadeOut();
		$scope.allPdsas = data;
		$scope.areaList = $scope.convert(data.areaList).children;
		$scope.totalfacilityPdsa=data.facilityPdsa;
		$scope.facilityPdsa = $scope.totalfacilityPdsa;
		$scope.columns = Object.keys($scope.facilityPdsa[0]);
	}, function(){
		
	});
	
	
	$scope.selectState = function(state){
		$scope.selectedState = state;
		$scope.selectedDistrict=undefined;
		$scope.filter();
	};
	$scope.selectDistrict = function(district){
		$scope.selectedDistrict = district;
		$scope.filter();
	};
	
	$scope.selectArea = function(area) {
		$scope.selectedArea = area;
		$scope.coreIndicatorList = $scope.indicatordata[area];
		$scope.selectedIndicator = undefined;
		$scope.selectedChangeIdea = undefined;
		$scope.changeIdeaList = undefined;
	};
	$scope.selectIndicator = function(indicator) {
		$scope.selectedIndicator = indicator;
		$scope.selectedChangeIdea = undefined;
		$scope.changeIdeaList = $scope.selectedIndicator.valueModelList;

	};
	$scope.selectIdea = function(idea) {
		$scope.selectedChangeIdea = idea;

	};
	$scope.resetFacilityDropdowns = function(){
		$scope.selectedState = undefined;
		$scope.selectedDistrict=undefined;
		$scope.filter();
	};
	
	$scope.filter = function(){
		if($scope.selectedState==undefined && $scope.selectedDistrict == undefined)
			{
				$scope.facilityPdsa = $scope.totalfacilityPdsa;
			}
		else if ($scope.selectedDistrict == undefined && $scope.selectedState!=undefined)
		{
			$scope.facilityPdsa = $filter('filter')($scope.totalfacilityPdsa, {State:$scope.selectedState.areaId.toString()}, true);
		}
		else if($scope.selectedDistrict != undefined && $scope.selectedState!=undefined)
			{
				$scope.facilityPdsa = $filter('filter')($scope.totalfacilityPdsa, {District:$scope.selectedDistrict.areaId.toString()}, true);
			}
		$(".loader").fadeOut();
	};
	
	$scope.filterPdsaList = function(focusArea, indicatorId, changeIdeaId){
	
		if (changeIdeaId){
			$scope.pdsaForFacility = $filter('filter')($scope.totalPdsaForFacility, {changeIdeaId: changeIdeaId, coreAreaName: focusArea, indicatorId: indicatorId});
		}
		else if (indicatorId){
			$scope.pdsaForFacility = $filter('filter')($scope.totalPdsaForFacility, {coreAreaName: focusArea, indicatorId: indicatorId});
		}
		else if (focusArea){
			$scope.pdsaForFacility = $filter('filter')($scope.totalPdsaForFacility, {coreAreaName: focusArea});
		}
		else {
			$scope.pdsaForFacility = JSON.parse(JSON.stringify($scope.totalPdsaForFacility));
		}
		$scope.selectedPdsa = undefined;
		 $("#advance-filter-section").animate({
	   			right: "-280px"
	   		}, 1000);
	};
	
	$scope.resetFilter = function(){
		$scope.selectedArea = undefined;
		$scope.selectedIndicator = undefined;
		$scope.selectedChangeIdea = undefined;
		$scope.selectedPdsa = undefined;
		$scope.filterPdsaList(false, false, false);
		 $("#advance-filter-section").animate({
	   			right: "-280px"
	   		}, 1000);
	}

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
    $scope.filterType = function(val){
    	if(val['District'] == "Chhattisgarh"){
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
    	}
    	if(isNaN(parseInt(val[$scope.sortType])))
    	return val[$scope.sortType];
    	else
    		return parseInt(val[$scope.sortType]);
    };
    $scope.removeRowId = function(item){
		 return item != 'District' && item != 'State' && item !='FacilityId';
	};
	
	$scope.backToPdsaList = function(){
		$scope.selectedFacilityForChart = undefined;
		$scope.facilityChartView = false;
		$scope.selectedPdsa = undefined;
	}
	
	$scope.getChartView = function(facility){
		$("#loader-mask").show();
		$scope.selectedFacilityForChart = facility;
		$scope.facilityChartView = true;
		allServices.getPDSAChartForFacility(facility.FacilityId).then(function(data){
			$("#loader-mask").fadeOut();
			$scope.pdsaForFacility = data;
			$scope.totalPdsaForFacility = data;
		}, function(){
			
		});
	};
	$scope.getPdsaChart = function(pdsa){
		$("#loader-mask").show();
		$scope.selectedPdsa = pdsa;
		allServices.getPDSASummaryChart(pdsa.pdsaId).then(function(data){
			$("#loader-mask").fadeOut();
			$scope.pdsaDetails = data;
			$scope.controlLinedata = data.charts;
		}, function(){
			
		});
	};
	
	//on click of download button
	$scope.getPDSASummaryExcelFile = function() {
		$("#loader-mask").show();
		d3.selectAll(".domain, .y.axis .tick line").style({"fill": "none", "stroke": "#000"});
		d3.selectAll("svg").attr("version", 1.1).attr("xmlns", "http://www.w3.org/2000/svg");
		
//		$("#not_median_Selected_img1").css("display", "none !important");
//		$("#not_line_Selected_img1").css("display", "none !important");
//		$("#median_Selected_img1").css("display", "none !important");
//		$("#line_Selected_img1").css("display", "none !important");
		
		$("#not_median_Selected_img1").remove();
		$("#not_line_Selected_img1").remove();
		$("#median_Selected_img1").remove();
		$("#line_Selected_img1").remove();
		
		if ($scope.facilityChartView) {
			var url = 'pdsaSummaryExcelFilePath';
			var chartSvg = $("#pageLineChart").html().replace('36.5°C','36.5 C').replace('37.4°C','37.4 C');
			
			var pdsaDetail = {
				facilityDetailMap : $scope.selectedFacilityForChart,
				pdsaList : $scope.pdsaForFacility,
				chartSvgs : [chartSvg],
				chartData: $scope.pdsaDetails,
				selectedPdsa: $scope.selectedPdsa
			}

			//add styles
			//call window resize to draw chart's ucl/lcl buttons
			$(window).resize();
			
			$http.post(url, pdsaDetail).then(function(result) {

				var fileName = {
						"fileName" : result.data
					};
					$.download("downloadReport/", fileName, 'POST');
					
			}, function(error) {

			});
			
		}

	}
	
	// download a file
	$.download = function(url, data, method) {
		// url and data options required
		if (url && data) {
			// data can be string of parameters or array/object
			data = typeof data == 'string' ? data : jQuery.param(data);
			// split params into form inputs
			var inputs = '';
			jQuery.each(data.split('&'), function() {
				var pair = this.split('=');
				inputs += '<input type="hidden" name="' + pair[0] + '" value="'	+ pair[1] + '" />';
			});
			// send request
			jQuery(
					'<form action="' + url + '" method="' + (method || 'post')
							+ '">' + inputs + '</form>').appendTo('body')
					.submit().remove();
			$("#loader-mask").fadeOut();
		}
		;
	};
	
	$scope.downloadFile = function (fileName) {
		$("#loader-mask").show();
		var result = {
			"fileName" : fileName
		};
		$.download("downloadPDSADoc/", result, 'POST');
		$("#loader-mask").fadeOut();
	};
	
}