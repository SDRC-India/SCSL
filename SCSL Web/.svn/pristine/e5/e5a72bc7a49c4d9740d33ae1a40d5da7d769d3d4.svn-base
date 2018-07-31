function dashboardChartViewController($scope, $http, $timeout, $filter,allServices){
	$scope.activeMenu = "dashboardChartView";
	$scope.pageName = "Small Multiple";
	$scope.sortReverse = true;
	$scope.showFacilityCharts = false;
	$scope.showIndicatorCharts = false;
	$scope.openedTab = "facility";
	$scope.selectedIndState = {};
	$scope.selectedIndDistrict = {};
	$scope.selectedIndFacilityType = {}; 
	$scope.selectedIndFacilitySize = {}; 
	$scope.selectedIndWaveParam = {};
	$scope.aggreagteAreaName = "";
	$scope.breadcrumb = "";
	$scope.resetIndicatorView = false;
	$scope.resetFacilityView = false;
	$scope.isEmpty = function(obj){
		   return Object.keys(obj).length === 0;
		}
 	$scope.periodicities = [ {
		id : '1',
		name : 'Monthly'
	}, {
		id : '3',
		name : 'Quarterly'
	},{
		id : '12',
		name : 'Yearly'
	}
	
	];
 	$scope.selectedPeriodicity = $scope.periodicities[0];
	$scope.allWaveParamList = [{
		id: '1',
		name: 'Wave 1'
	},{
		id: '2',
		name: 'Wave 2'
	},{
		id: '3',
		name: 'Wave 3'
	}];
	
	/*
	 * map districts according to their state 
	*/
	$scope.convert = function(array, level) {
		var map = {};
		for (var i = 0; i < array.length; i++) {
			var obj = array[i];
			if (obj.level == level)
				obj.parentAreaId = null;
			if (!(obj.areaId in map)) {
				map[obj.areaId] = obj;
				map[obj.areaId].children = [];
			}

			var parent = obj.parentAreaId || '-';
			if (!(parent in map)) {
				map[parent] = {};
				map[parent].children = [];
			}
			map[parent].children.push(map[obj.areaId]);
		}
		return map['-'];
	};
	
	$scope.filterIndFacilitySize = function(facilitytype){
		if(facilitytype.facilityType == 24){
			$scope.filteredIndFacilitySizes = $filter('filter')($scope.facilitySizes, function(size){
		        return size.facilitySize === 25 || size.facilitySize === 26;
			});
		}
		if(facilitytype.facilityType == 23){
			$scope.filteredIndFacilitySizes = $filter('filter')($scope.facilitySizes, function(size){
		        return size.facilitySize === 27 || size.facilitySize === 28;
			});
		}
	};
	/*
	 * gets state and district list puts in @areaList
	*/
	$scope.getArea = function(){
		allServices.getAreaForFacilityViewFilterData().then(
				function(data) {
					$scope.areaList = $scope.convert(data['areaList'], 1).children;
					$("#loader-mask").fadeOut();
				}, function() {

				});
	};
	$scope.getArea();
	
	$scope.getIndicatorDetails = function(){
		allServices.getAllIndicatorDetails().then(function(data) {
			$("#loader-mask").fadeOut();
			$scope.allIndicatorMap = data;
			$scope.indicatorTypeList = Object.keys(data);
		}, function() {

		});
	};
	$scope.getIndicatorDetails();
	
	/*
	 * Get Facility Size and type
	*/
	allServices.getFacilityTypeAndSize().then(function(data) {
		$scope.facilitytypes = data["type"];
		$scope.facilitySizes = data["size"];
		$scope.filteredIndFacilitySizes = $scope.facilitySizes;
		$("#loader-mask").fadeOut();
	}, function() {

	});
	
	/*
	 * get control charts for facility
	*/
	$scope.getFacilityChart = function(selectedFacility){
		$("#loader-mask").show();
		$scope.savedSelectedState = JSON.parse(JSON.stringify($scope.selectedState));
		$scope.savedSelectedDistrict = JSON.parse(JSON.stringify($scope.selectedDistrict));
		$scope.savedSelectedFacility = JSON.parse(JSON.stringify(selectedFacility));
		allServices.getFacilityChart(selectedFacility.areaId, selectedFacility.hasLR).then(
				function(data) {
					$("#loader-mask").fadeOut();
					if(data.length){
						$scope.showFacilityCharts = true;
						$scope.resetFacilityView = false;
					}
					else{
						$scope.showFacilityCharts = false;
						$scope.errorMsg = "No data available for this facility";
						$("#errorMessage").modal("show");
					}
					$timeout(function(){
						$scope.allFacilityCharts = data;
					});
					
				});
	};
	
	/*
	 * get control charts for Indicator
	*/
	$scope.getIndicatorChart = function(selectedIndicatorKey){
		$("#loader-mask").show();
		$scope.savedSelectedIndicatorType = JSON.parse(JSON.stringify($scope.selectedIndicatorType));
		if($scope.selectedFocusArea)
		$scope.savedSelectedFocusArea = JSON.parse(JSON.stringify($scope.selectedFocusArea));
		$scope.savedSelectedIndicator = JSON.parse(JSON.stringify($scope.selectedIndicator));
		$scope.savedSelectedIndState = JSON.parse(JSON.stringify($scope.selectedIndState));
		$scope.savedSelectedIndDistrict = JSON.parse(JSON.stringify($scope.selectedIndDistrict));
		$scope.savedSelectedIndFacilityType = JSON.parse(JSON.stringify($scope.selectedIndFacilityType));
		$scope.savedSelectedIndFacilitySize = JSON.parse(JSON.stringify($scope.selectedIndFacilitySize));
		$scope.savedSelectedIndWaveParam = JSON.parse(JSON.stringify($scope.selectedIndWaveParam));
		$scope.savedSelectedPeriodicity = JSON.parse(JSON.stringify($scope.selectedPeriodicity));
		allServices.getIndicatorChart(selectedIndicatorKey, $scope.selectedIndState.areaId, $scope.selectedIndDistrict.areaId, 
				$scope.selectedIndFacilityType.facilityType, $scope.selectedIndFacilitySize.facilitySize, $scope.selectedIndWaveParam.id, $scope.selectedPeriodicity.id,
				$scope.savedSelectedIndicatorType).then(
				function(data) {
					$("#loader-mask").fadeOut();
					if(data){
						$scope.showIndicatorCharts= true;
						$scope.resetIndicatorView = false;
					}
					else{
						$scope.clearAdvanceIndFilterSelections();
						$scope.showIndicatorCharts = false;
						$scope.errorMsg = "No data available for this indicator";
						$("#errorMessage").modal("show");
					}
					$timeout(function(){
						$scope.allIndicatorCharts = data;
						$scope.aggreagteAreaName = $scope.allIndicatorCharts.aggreagteArea[0][0].name;
						$scope.getRelatedIndicators(selectedIndicatorKey);
						$scope.breadcrumb = $scope.savedSelectedIndicatorType +" >> " + ($scope.savedSelectedIndicatorType != "Outcome" ?  $scope.savedSelectedFocusArea + " >> ": "")
							+ $scope.savedSelectedIndicator.value + " >> " +"<br/>" + ($scope.selectedIndState.areaName ?$scope.selectedIndState.areaName+ " >> "  : "")
							+ ($scope.selectedIndDistrict.areaName ? $scope.selectedIndDistrict.areaName+ " >> "  : "")
								+ ($scope.selectedIndFacilityType.facilityTypeName ?  $scope.selectedIndFacilityType.facilityTypeName+ " >> "  : "")
									+ ($scope.selectedIndFacilitySize.facilitySizeName ?  $scope.selectedIndFacilitySize.facilitySizeName+ " >> "  : "")
										+($scope.selectedIndWaveParam.name ?  $scope.selectedIndWaveParam.name+ " >> "  : "")
											 + $scope.selectedPeriodicity.name;
					});
					
				});
	};
	
	
	$scope.clearAdvanceIndFilterSelections = function(){
		$scope.selectedIndState = {};
		$scope.selectedIndDistrict = {};
		$scope.selectedIndFacilityType = {};
		$scope.selectedIndFacilitySize = {};
		$scope.selectedIndWaveParam = {};
		$scope.selectedPeriodicity = $scope.periodicities[0];
	};
	$scope.selectState = function(state) {
		$scope.selectedState = state;
		$scope.selectedDistrict = undefined;
		$scope.selectedFacility = undefined;
		$scope.resetFacilityView = true;
	};
	$scope.selectDistrict = function(district) {
		$scope.selectedDistrict = district;
		$scope.selectedFacility = undefined;
		$scope.resetFacilityView = true;
	};
	$scope.selectFacility = function(facility) {
		$scope.selectedFacility = facility;
		$scope.resetFacilityView = false;

	};
	$scope.selectIndicatorType = function(indicatorType){
		$scope.selectedIndicatorType = indicatorType;
		$scope.allFocusAreaList = Object.keys($scope.allIndicatorMap[$scope.selectedIndicatorType]);
		$scope.allIndicatorList = [];
		$scope.selectedFocusArea = undefined;
		$scope.selectedIndicator = undefined;
		$scope.resetIndicatorView = true;
		if($scope.selectedIndicatorType == "Outcome"){
			$scope.selectFocusArea($scope.allFocusAreaList[0]);
			$scope.hideFocusArea = true;
		}
		else{
			$scope.hideFocusArea = false;
		}
	};
	$scope.selectFocusArea = function(focusArea){
		$scope.selectedFocusArea = focusArea;
		$scope.allIndicatorList = $scope.allIndicatorMap[$scope.selectedIndicatorType][$scope.selectedFocusArea];
		$scope.selectedIndicator = undefined;
		$scope.resetIndicatorView = true;
	};
	$scope.selectIndicator = function(indicator) {
		$scope.selectedIndicator = indicator;
		$scope.resetIndicatorView = false;
	};
	$scope.selectWaveParam = function(wave){
		$scope.selectedIndWaveParam = wave;
	};
	$scope.selectIndState = function(state) {
		$scope.selectedIndState = state;
		$scope.selectedIndDistrict = {};
	};
	$scope.selectIndDistrict = function(district) {
		$scope.selectedIndDistrict = district;
	};
	$scope.selectIndFacilityType = function(facilitytype) {
		$scope.selectedIndFacilityType = facilitytype;
		$scope.selectedIndFacilitySize={};
		$scope.filteredIndFacilitySizes = [];
		$scope.filterIndFacilitySize(facilitytype);

	};
	$scope.selectIndFacilitySize = function(facilitysize) {
		$scope.selectedIndFacilitySize = facilitysize;
	};
	$scope.selectPeriodicity = function(periodicity) {
		$scope.selectedPeriodicity = periodicity;
	};
	$scope.resetFacilityDropdowns = function(){
		$scope.selectedState = undefined;
		$scope.selectedDistrict=undefined;
		$scope.selectedFacility = undefined;
		$scope.resetFacilityView = true;
	};
	
	$scope.resetIndicatorDropdowns = function(){
		$scope.selectedIndicator = undefined;
		$scope.selectedFocusArea=undefined;
		$scope.selectedIndicatorType = undefined;
		$scope.allFocusAreaList = [];
		$scope.allIndicatorList = [];
		$scope.resetIndicatorView = true;
		$scope.clearAdvanceIndFilterSelections();
	};
	$scope.backToFacilitySelection = function(){
		$scope.showFacilityCharts = false;
		$scope.allFacilityCharts = [];
	};
	$scope.backToIndicatorSelection = function(){
		$scope.clearAdvanceIndFilterSelections();
		$scope.showIndicatorCharts = false;
		$scope.allIndicatorCharts = {"all":[], "aggreagteArea":[]};
	};
	$scope.getOpenedTab = function(tab){
		$scope.openedTab = tab;
	};
	
	$scope.setRelatedIndicatorDetails = function(id){
		var indicatorTypeFormats = $scope.allIndicatorMap;
		var indicatorTypeFormatKeys = Object.keys(indicatorTypeFormats);
		for(var i=0; i<indicatorTypeFormatKeys.length; i++){
			var focusAreaFormats = $scope.allIndicatorMap[indicatorTypeFormatKeys[i]];
			var focusAreaKeys = Object.keys(focusAreaFormats);
			for(var j=0; j<focusAreaKeys.length; j++){
				var focusAreaIndicators = focusAreaFormats[focusAreaKeys[j]];
				for(var k=0; k<focusAreaIndicators.length; k++){
					if(focusAreaIndicators[k].key == id){
						var selectedIndicator = focusAreaIndicators[k];
						var selectedFocusArea = focusAreaKeys[j];
						var selectedIndicatorType = indicatorTypeFormatKeys[i];
						$scope.selectIndicatorType(selectedIndicatorType);
						$scope.selectFocusArea(selectedFocusArea);
						$scope.selectIndicator(selectedIndicator);
					}
				}
			}
		}
	};
	
	
	$scope.getRelatedIndicators = function(id){
		$("#loader-mask").show();
		allServices.getRelatedIndicators(id).then(function(data){
			$("#loader-mask").fadeOut();
			$scope.allRelatedIndicatorList = data;
		}, function(){
			
		});
	};
	 $scope.showRelatedIndicatorModal = function(type){
		 $scope.showingRelatedIndicatorType = type;
		 $scope.relatedIndicators = $scope.allRelatedIndicatorList[type];
		 $('#relatedIndicatorListModal').modal('show');
	 };
	 $scope.closeRelatedIndicatorModal = function(){
		 $('#relatedIndicatorListModal').modal('hide');
	 };
	 
	//sarita code
	//on click of download button
	$scope.getSmallMultipleExcelFile = function() {
			$("#loader-mask").show();
//			var url = 'smallMultipleExcelFilePath';
//			var chartSvg = $("#lineChart").html().replace('<image xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="resources/images/icons/svg_median_Selected.svg" width="32" height="30" key="UCL" x="345" y="-64" style="cursor: pointer;"></image>', '').replace('<image xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="resources/images/icons/svg_line_Selected.svg" width="32" height="30" key="UCL" x="395" y="-64" style="cursor: pointer;"></image>', '');
			var chartSvgs = [];
//			d3.selectAll(".domain, .y.axis .tick line").style({"fill": "none", "stroke": "#000"});
			d3.selectAll("svg").attr("version", 1.1).attr("xmlns", "http://www.w3.org/2000/svg");
			if($scope.openedTab == 'facility'){//for facility view the trend chart div id is different from indicator view
				var svgs = $(".trend-chart").map(function() {
					return "#" + this.id;
				}).get();
				for (var i = 0; i < svgs.length; i++) {
					$(svgs[i]).children().each(function(index, el) {
						$(this).find("#not_median_Selected_img1").remove();
						$(this).find("#not_line_Selected_img1").remove();
						$(this).find("#median_Selected_img1").remove();
						$(this).find("#line_Selected_img1").remove();
						var chartSvg = $(this).html().replace('36.5°C','36.5 C').replace('37.4°C','37.4 C');
						if (chartSvg != "")
							chartSvgs.push(chartSvg);
					});
				}
				;
			} else {// indicator view
			// aggregated chart

				$("#not_median_Selected_img1").remove();
				$("#not_line_Selected_img1").remove();
				$("#median_Selected_img1").remove();
				$("#line_Selected_img1").remove();

			var aggregatechartSvg = $("#aggregatelineChart").html().replace(
					'36.5°C', '36.5 C').replace('37.4°C', '37.4 C');
			chartSvgs.push(aggregatechartSvg)

			var svgs = $(".trend-ind-chart").map(function() {
				return "#" + this.id;
			}).get();
			for (var i = 0; i < svgs.length; i++) {
				$(svgs[i]).children().each(
						function(index, el) {
							$(this).find("#not_median_Selected_img1").remove();
							$(this).find("#not_line_Selected_img1").remove();
							$(this).find("#median_Selected_img1").remove();
							$(this).find("#line_Selected_img1").remove();
							var chartSvg = $(this).html().replace('36.5°C',
									'36.5 C').replace('37.4°C', '37.4 C');
							if (chartSvg != "")
								chartSvgs.push(chartSvg);
						});
			}
			;
		}
			
			$scope.allIndicatorChartsCombined = [];
			
			if($scope.openedTab == 'indicator'){
				[]
				for(var i=0; i < $scope.allIndicatorCharts.aggreagteArea.length; i++){
					$scope.allIndicatorChartsCombined.push($scope.allIndicatorCharts.aggreagteArea[i]);
				}
				
				for(var i=0; i < $scope.allIndicatorCharts.all.length; i++){
					$scope.allIndicatorChartsCombined.push($scope.allIndicatorCharts.all[i]);
				}
			}
			
			var smallMultiple = {
			
					chartSvgs : chartSvgs,
					dashboardChartModels : $scope.openedTab == 'facility' ? $scope.allFacilityCharts : $scope.allIndicatorChartsCombined,
					facilityName : $scope.openedTab == 'facility' && $scope.savedSelectedFacility ? $scope.savedSelectedFacility.areaName
							: null,
					state : $scope.openedTab == 'facility' ? $scope.savedSelectedState.areaName : $scope.savedSelectedState ? $scope.savedSelectedIndState.areaName : null,
					district :  $scope.openedTab == 'facility' ? $scope.savedSelectedDistrict.areaName : $scope.savedSelectedDistrict ? $scope.savedSelectedIndDistrict.areaName
							: null,
					indicatorName : $scope.openedTab == 'indicator' && $scope.savedSelectedIndicator ? $scope.savedSelectedIndicator.value
							: null,
					wave : $scope.openedTab == 'indicator' && $scope.savedSelectedIndWaveParam ? $scope.savedSelectedIndWaveParam.name
							: null,
					facilityType : $scope.openedTab == 'indicator' && $scope.savedSelectedIndFacilityType ? $scope.savedSelectedIndFacilityType.facilityTypeName
							: null,
					facilitySize : $scope.openedTab == 'indicator' && $scope.savedSelectedIndFacilitySize ? $scope.savedSelectedIndFacilitySize.facilitySizeName
							: null,
					indicatorType : $scope.openedTab == 'indicator' && $scope.savedSelectedIndicatorType ? $scope.savedSelectedIndicatorType
							: null,
					coreArea : $scope.openedTab == 'indicator' && $scope.savedSelectedFocusArea ? $scope.savedSelectedFocusArea
							: null,
					periodicity : $scope.openedTab == 'indicator' && $scope.savedSelectedPeriodicity ? $scope.savedSelectedPeriodicity.name
							: null

		};
			//call window resize to draw chart's ucl/lcl buttons
			$(window).resize();
			
		$http.post('smallMultipleExcelFilePath', smallMultiple).then(function(result) {

			var fileName = {
				"fileName" : result.data
			};
			$.download("downloadReport/", fileName, 'POST');

		}, function(error) {

		});

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
	
}