//@author Devikrushna (devikrushna@sdrc.co.in)

function reportController($scope, $http, $filter, allServices) {
	$scope.activeMenu = "report";
	$scope.pageName = "Report";
	$scope.endTimePeriods = [];
	$scope.selectedState = {};
	$scope.selectedState.areaId = '';
	$scope.selectedDistrict = {};
	$scope.selectedDistrict.areaId = '';
	$scope.selectedFacilityType = {};
	$scope.selectedFacilityType.facilityType = '';
	$scope.selectedFacilitySize = {};
	$scope.selectedFacilitySize.facilitySize = '';
	$scope.selectedFacility = {};
	$scope.selectedFacility.areaId = '';
	$scope.planCalender=false;
	$scope.fromTodate=true;
	$scope.selectedPeriodicity={};

	$scope.reportType = [ {
		typeID : 1,
		typeName : 'Raw Data'
	}, {
		typeID : 2,
		typeName : 'Summary Data'
	},{
		typeID : 4,
		typeName : 'Planning Report'
	}
	
	];
	
	$scope.periodicities = [];
	
	$("#loader-mask").show();
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
	
	$("#loader-mask").show();
	allServices.getFacilityTypeAndSize().then(function(data) {
		$scope.facilitytypes = data["type"];
		$scope.facilitySizes = data["size"];
		$scope.filteredFacilitySizes = $scope.facilitySizes;
		$("#loader-mask").fadeOut();
	}, function() {

	});
	$("#loader-mask").show();
	
	$( "#datepicker" ).datepicker({dateFormat:"dd-mm-yy"});
	$( "#datepicker1" ).datepicker({dateFormat:"dd-mm-yy"});
	
	allServices.getAreaForReportFilterData().then(
			function(reportdata) {
				$scope.userlevel = reportdata.areaLevel;
				$scope.userlevelId = reportdata.arealevelId;
				$scope.allreportdetails = reportdata;
				$scope.aggregateDataArr={typeID : 3,
				typeName : 'Aggregate Data' };
				if($scope.userlevel==4){
					$scope.selectedFacilityType = $scope.allreportdetails['areaList'][0];
					$scope.selectedFacilitySize = $scope.allreportdetails['areaList'][0];
					$scope.selectedFacility = $scope.allreportdetails['areaList'][0];
						
				}
				if($scope.userlevel==3){
					$scope.selectedDistrict = $scope.convert($scope.allreportdetails['areaList'], 4);
					$scope.selectedDistrict.areaId = '';
				}
				if($scope.userlevel==2){
					$scope.reportType.push($scope.aggregateDataArr);
					$scope.selectedState = $scope.convert($scope.allreportdetails['areaList'], 3);
					$scope.selectedState.areaId = '';
					$scope.selectedFacilityType.facilityType = '';
					$scope.selectedFacilitySize.facilitySize = '';
					$scope.selectedFacility.areaId = '';
						
				}
				if($scope.userlevel==1){
					$scope.reportType.push($scope.aggregateDataArr);
					$scope.areaList = $scope.convert($scope.allreportdetails['areaList'], 1).children;
				}

				$("#loader-mask").fadeOut();
			}, function() {

			});

	$scope.selectPeriodicity = function(periodicity) {
		$scope.selectedPeriodicity = periodicity;
		$scope.selectedStartDate = undefined;
		$scope.selectedEndDate = undefined
		
		//make it synchronous
		if($scope.selectedPeriodicity){
			
			$("#loader-mask").show();
			allServices.getTimePeriodForReport($scope.selectedPeriodicity.id).then(function(data) {
				$scope.allTimeperiod = data;
				$scope.timePeriod = $scope.allTimeperiod;
				$("#loader-mask").fadeOut();
			}, function() {

			});
		}
	};
	
	$scope.selectStartDate = function(startdate) {
		$("#loader-mask").show();
		$scope.selectedStartDate = startdate;
		$scope.selectedEndDate = undefined
		$scope.endTimePeriods = [];
		for (i = 0; i < $scope.allTimeperiod.length; i++) {
			if ($scope.allTimeperiod[i].timePeriodId >= $scope.selectedStartDate.timePeriodId) {
				$scope.endTimePeriods.push($scope.allTimeperiod[i]);
			}
		}
		$("#loader-mask").fadeOut();
	};
	$scope.selectEndDate = function(enddate) {
		$scope.selectedEndDate = enddate;

	};
	$scope.selectReport = function(report) {
		$scope.selectedReportType = report;
		$scope.selectedPeriodicity={};
		if(report.typeID!=2){
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
		}else{
			$scope.periodicities = [ {
				id : '1',
				name : 'Monthly'
			}];
		}
		
		$scope.planCalender=false;
		$scope.fromTodate=true;
		$scope.selectedStartDate = undefined;
		$scope.selectedEndDate = undefined;
		if($scope.selectedReportType.typeID==3 || $scope.selectedReportType.typeID==4)
			{
			$scope.selectedState = {};
			$scope.selectedState.areaId = '';
			$scope.selectedDistrict = undefined;
			$scope.selectedFacilityType = undefined;
			$scope.selectedFacilitySize = undefined;
			$scope.selectedFacility = undefined;
			}
		if($scope.selectedReportType.typeID==4){
			$scope.planCalender=true;
			$scope.fromTodate=false;
			$scope.startplanDate=undefined;
			 $scope.endplanDate=undefined;
			
		}
	};
	$scope.calculateEndDate = function() {

		var endDate = $('#datepicker').datepicker('getDate');

		endDate.setDate(endDate.getDate());
		var eventDatepicker = angular.element(document).find('#datepicker1');
		console.log(eventDatepicker);
		eventDatepicker.datepicker('refresh');
		eventDatepicker.datepicker("option", "minDate", endDate);
		endDate.setDate(endDate.getDate());
	}
	
	
	
	$scope.selectState = function(state) {
		$("#loader-mask").show();
		$scope.selectedState = state;
		$scope.selectedDistrict = undefined;
		$scope.selectedFacilityType = undefined;
		$scope.selectedFacilitySize = undefined;
		$scope.selectedFacility = undefined;
		$("#loader-mask").fadeOut();

	};
	$scope.selectDistrict = function(district) {
		$("#loader-mask").show();
		$scope.selectedDistrict = district;
		$scope.selectedFacility = undefined;
		$('[data-toggle="tooltip"]').tooltip();
		$("#loader-mask").fadeOut();

	};
	$scope.filterFacilitySize = function(facilitytype){
  			if(facilitytype.facilityType == 24){
  				$scope.filteredFacilitySizes = $filter('filter')($scope.facilitySizes, function(size){
  			        return size.facilitySize === 25 || size.facilitySize === 26;
  				});
  			}
  			if(facilitytype.facilityType == 23){
  				$scope.filteredFacilitySizes = $filter('filter')($scope.facilitySizes, function(size){
  			        return size.facilitySize === 27 || size.facilitySize === 28;
  				});
  			}
  	};
	$scope.selectFacilityType = function(facilitytype) {
		$scope.selectedFacilityType = facilitytype;
		$scope.selectedFacility=undefined;
		$scope.selectedFacilitySize=undefined;
		$scope.filteredFacilitySizes = [];
		$scope.filterFacilitySize(facilitytype);

	};
	$scope.resetReportForm = function(){
		$scope.periodicities = [];
		$scope.selectedReportType = undefined;
		$scope.selectedPeriodicity = undefined;
		$scope.selectedStartDate = undefined;
		$scope.selectedEndDate = undefined;
		$scope.selectedState = {};
		$scope.selectedDistrict = {};
		$scope.selectedDistrict.areaId='';
		$scope.selectedFacilityType = undefined;
		$scope.selectedFacilitySize = undefined;
		$scope.selectedFacility = {};
		$scope.selectedFacility.areaId = '';
		$scope.timePeriod = [];
		$scope.endTimePeriods = [];
		$scope.startplanDate = undefined;
		$scope.endplanDate = undefined;
		$scope.filteredFacilitySizes = [];
		$scope.filteredFacilitySizes = $scope.facilitySizes;
		$scope.selectedState.areaId = '';
		//only in case of district level populate all facility by default
		if($scope.userlevel==3)
			$scope.selectedDistrict = $scope.convert($scope.allreportdetails['areaList'], 4);
		else if($scope.userlevel==2){
			$scope.selectedState = $scope.convert($scope.allreportdetails['areaList'], 3);
			$scope.selectedState.areaId = '';
			$scope.selectedFacilityType.facilityType = '';
			$scope.selectedFacilitySize.facilitySize = '';
			$scope.selectedFacility.areaId = '';
		}
			
	}
	$scope.selectFacilitySize = function(facilitysize) {
		$scope.selectedFacilitySize = facilitysize;
		$scope.selectedFacility=undefined;
	};
	$scope.selectFacility = function(facility) {
		$scope.selectedFacility = facility;

	};

	$scope.validateReport = function() {

		if ($scope.selectedReportType == undefined) {
			$scope.errorMsg = "Please select Report Type";
			$("#errorMessage").modal("show");
		}
		
		if ($scope.selectedReportType.typeID == 4) {

			if ($scope.startplanDate == undefined) {
				$scope.errorMsg = "Please select Start Date";
				$("#errorMessage").modal("show");
			} else {
				$("#loader-mask").show();
				$scope.downloadReport();
			}

		}
		else if ($scope.selectedPeriodicity.id == undefined) {
			$scope.errorMsg = "Please select Periodicity";
			$("#errorMessage").modal("show");
		} 
		else {
			if ($scope.selectedStartDate == undefined) {
				$scope.errorMsg = "Please select From Date";
				$("#errorMessage").modal("show");
			} else {
				$("#loader-mask").show();
				$scope.downloadReport();

			}
		}

	};
	$scope.downloadReport = function() {
		if($scope.selectedDistrict==undefined)
		{
			var districtId='';
		}
		else
		{
			var districtId=$scope.selectedDistrict.areaId;
		}
		if($scope.selectedFacilityType==undefined)
			{
			var facilityTypeId='';
			}
		else
			{
			var facilityTypeId=$scope.selectedFacilityType.facilityType;
			}
		if($scope.selectedFacilitySize==undefined){
			
			var facilitysizeId='';
		}
		else
		{
			var facilitysizeId=$scope.selectedFacilitySize.facilitySize;
		}
		if($scope.selectedFacility==undefined){
			
			var facilityId='';
		}
		else
		{
			var facilityId=$scope.selectedFacility.areaId;
		}
		
		var startDate = '';
		var endDate = '';
		var startDateStr = '';
		var endDateStr = '';
		
		if($scope.selectedReportType.typeID==4){
			if($scope.endplanDate==undefined){
				startDate = $scope.startplanDate;
				endDate = $scope.startplanDate;
				startDateStr = $scope.startplanDate;
				endDateStr = $scope.startplanDate;
			}
			else{
				startDate=$scope.startplanDate;
				endDate=$scope.endplanDate;
				startDateStr = $scope.startplanDate;
				endDateStr = $scope.endplanDate;
			}
			
		}
		else{
			if ($scope.selectedEndDate == undefined) {
				startDate=$scope.selectedStartDate.timePeriodId;
				endDate=$scope.selectedStartDate.timePeriodId;
				startDateStr = $scope.selectedStartDate.timePeriod;
				endDateStr = $scope.selectedStartDate.timePeriod;
			} 
			else
			{
				startDate=$scope.selectedStartDate.timePeriodId;
				endDate=$scope.selectedEndDate.timePeriodId;
				startDateStr = $scope.selectedStartDate.timePeriod;
				endDateStr = $scope.selectedEndDate.timePeriod;
			} 
		}
		
		
		$http.post(
				'report?reporttypeId=' + $scope.selectedReportType.typeID
						+ '&stateId=' + $scope.selectedState.areaId
						+ '&districtId=' + districtId
						+ '&facilitypeId='
						+ facilityTypeId
						+ '&facilitysizeId='
						+ facilitysizeId
						+ '&facilityId=' + facilityId
						+ '&startDate=' + startDate
						+ '&endDate=' + endDate
						+ '&periodicity=' +$scope.selectedPeriodicity.id
						+ '&startDateStr='+startDateStr
						+ '&endDateStr='+endDateStr)
				.then(function(result) {
					if(result.data!="")
						{
							$scope.downloadFile(result.data)
						}
					else
						{
							$scope.infoMsg = "There is no data available for the above selection.";
							$("#infoMessage").modal("show");
							$("#loader-mask").fadeOut();
						}
				});

	};
	
	$scope.downloadFile = function (fileName) {
		$("#loader-mask").show();
		$.download = function(url, data, method) {
			// url and data options required
			if (url && data) {
				// data can be string of parameters or array/object
				data = typeof data == 'string' ? data : jQuery.param(data);
				// split params into form inputs
				var inputs = '';
				jQuery.each(data.split('&'), function() {
					var pair = this.split('=');
					inputs += '<input type="hidden" name="' + pair[0]
							+ '" value="' + pair[1] + '" />';
				});
				// send request
				jQuery(
						'<form action="' + url + '" method="'
								+ (method || 'post') + '">' + inputs
								+ '</form>').appendTo('body').submit().remove();
			}
			;
//			$("#loader-mask").fadeOut();
//			$("#pop").modal("show");
		};

		var result = {
			"fileName" : fileName
		};
		$.download("downloadReport/", result, 'POST');
		$("#loader-mask").fadeOut();
		$scope.msg = "Download successful";
		$("#pop").modal("show");
	};

}