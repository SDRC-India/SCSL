//@author Devikrushna (devikrushna@sdrc.co.in)

function historicalDataController($scope, $http,$filter, allServices){
	$scope.activeMenu = "uploadHistoricalData";
	$scope.pageName = "Historical Data";
	$scope.endTimePeriods = [];
	$scope.errorMsgs=undefined
//	
	$("#loader-mask").show();
	$scope.convert = function(array) {
		var map = {};
		for (var i = 0; i < array.length; i++) {
			var obj = array[i];
			if (obj.parentAreaId == -1)
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
	
	allServices.getAllAreaForHistoricalView().then(
			function(data) {
				$scope.historicaldata = data;
				$scope.areaList = $scope.convert($scope.historicaldata).children;

				$("#loader-mask").fadeOut();
			}, function() {

			});
	$("#loader-mask").show();
	allServices.getTimePeriodForLegacy().then(function(data) {
		$scope.allTimeperiod = data;
		$scope.timePeriod = $scope.allTimeperiod;
		$("#loader-mask").fadeOut();
	}, function() {

	});
			
			$scope.selectState = function(state) {
				$("#loader-mask").show();
				$scope.selectedState = state;
				$scope.selectedDistrict = undefined;
				$scope.selectedFacility = undefined;
				$("#loader-mask").fadeOut();

			};
			$scope.selectDistrict = function(district) {
				$("#loader-mask").show();
				$scope.selectedDistrict = district;
				$scope.selectedFacility = undefined;
				$("#loader-mask").fadeOut();
			};
			$scope.selectFacility = function(facility) {
				$scope.selectedFacility = facility;
			};
			
			$scope.resetDiv = function(){
				$scope.selectedState = undefined;
				$scope.selectedDistrict = undefined;
				$scope.selectedFacility = undefined;
				$scope.selectedFromDate = undefined;
				$scope.selectedToDate = undefined
				$scope.historicaldata = [];
				
			};
			
			$scope.selectFromDate = function(fromdate) {
				$scope.selectedFromDate = fromdate;
				$scope.selectedToDate = undefined
				$scope.endTimePeriods = [];
				var count = 0;
				for (var i = $scope.allTimeperiod.length-1; i >= 0; i--) {
					if ($scope.allTimeperiod[i].timePeriodId >= $scope.selectedFromDate.timePeriodId) {
						count++;
						$scope.endTimePeriods.push($scope.allTimeperiod[i]);
						if(count>59){ //60 is number of columns in template with validation
							break;
						}
					}
				}
				$scope.endTimePeriods.reverse();
			};
			$scope.selectToDate = function(todate) {
				$scope.selectedToDate = todate;

			};
			
			
			$scope.validateHistoricalData = function() {
				if ($scope.selectedState == undefined) {
					$scope.errorMsg = "Please select State";
					$("#errorMessage").modal("show");
				} else if ($scope.selectedDistrict == undefined) {
					$scope.errorMsg = "Please select District";
					$("#errorMessage").modal("show");
				} else if ($scope.selectedFacility == undefined) {
					$scope.errorMsg = "Please select Facility";
					$("#errorMessage").modal("show");
				}else if ($scope.selectedFromDate == undefined) {
					$scope.errorMsg = "Please select From Timeperiod";
					$("#errorMessage").modal("show");
				}
//				else if ($scope.selectedToDate == undefined) {
//					$scope.errorMsg = "Please select To Timeperiod";
//					$("#errorMessage").modal("show");
//				}
				else {
					$("#loader-mask").show();
					$scope.downloadData();
				}
			};
			$scope.downloadData=function(){		
				var url = $scope.selectedToDate != undefined ? 'getHistoricalDataTemplateFile?facilityId='
				+ $scope.selectedFacility.areaId
				+ '&facilityName='
				+ $scope.selectedFacility.areaName
				+ '&startTimeperiod='
				+ $scope.selectedFromDate.timePeriodId
				+ '&endTimePeriod='
				+ $scope.selectedToDate.timePeriodId
				+ '&startTimeperiodName='
				+ $scope.selectedFromDate.timePeriod
				+ '&endTimePeriodName='
				+ $scope.selectedToDate.timePeriod
				+ '&hasLr='
				+ $scope.selectedFacility.hasLr
				: 'getHistoricalDataTemplateFile?facilityId='
						+ $scope.selectedFacility.areaId + '&facilityName='
						+ $scope.selectedFacility.areaName
						+ '&startTimeperiod='
						+ $scope.selectedFromDate.timePeriodId
						+ '&startTimeperiodName='
						+ $scope.selectedFromDate.timePeriod
						+ '&hasLr='
						+ $scope.selectedFacility.hasLr;
				
				$http.get(url).then(function(response){
					
					var result = {"fileName" :response.data};
					
					$.download = function(url, data, method){
						//url and data options required
							if( url && data ){ 
						//data can be string of parameters or array/object
							data = typeof data == 'string' ? data : jQuery.param(data);
					  //split params into form inputs
							var inputs = '';
							jQuery.each(data.split('&'), function(){ 
								var pair = this.split('=');
								inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />'; 
							});
					  //send request
							jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
							.appendTo('body').submit().remove();
							};
							$(".loader").css("display", "none");
					  };
					$.download("downloadHistoricalDataTemplateFile",result,'POST');
					$("#loader-mask").fadeOut();
				},function(error){
					window.location.href = "authException";
				});
				
				
			};
		
			$scope.getFileDetails = function(e) {

				$scope.files = [];
				$scope
						.$apply(function() {

							// STORE THE FILE OBJECT IN AN ARRAY.
							for (var i = 0; i < e.files.length; i++) {
								$scope.firstFileExt = e.files[i].name.split(".").pop();
								if ($scope.firstFileExt.toLowerCase() != "xls"
										) {
											$scope.clearFile();
											$scope.errorMsg = "Please upload the same file downloaded from this page";
											$("#errorMessage").modal("show");
								} else {
									$scope.files.push(e.files[i])
									$scope.uploadFileName = $scope.files[0].name;
									$scope.infoMsg="Are you sure to upload "+$scope.uploadFileName +"?";
									$("#infoMessage").modal("show");
								
								}
							}
//							$scope.uploadBtn = $scope.files[0].name;

						});
			};
			
			$scope.clearFile= function()
			{
				 angular.element("input[type='file']").val(null);
				 $scope.files = undefined;
				 $scope.dataload=undefined;
				 $("#infoMessage").modal("hide");
			};

			$scope.uploadFile = function(files){
				$("#infoMessage").modal("hide");
				$("#loader-mask").show();
				$http({
					url : 'uploadHistoricalDataTemplateFile',
					method : 'POST',
					headers : {
						'Content-Type' : undefined
					},
					transformRequest : function(data) {
						var formData = new FormData();
						formData.append("file", data.file[0]);
						return formData;
					},
					data : {
						file : files
					}
				}).then(function successCallback(response) {
					$scope.clearFile();
					$("#loader-mask").fadeOut();
					$scope.errorMsgs=response.data;
					 
					if($scope.errorMsgs.length > 0){
						$("#errorMessage1").modal("show");
					}
					else{
						$scope.msg="Data uploaded successfully";
						$("#pop").modal("show");
						
					}
					
				}, function errorCallback(response) {
				});
			}
			
			setTimeout(function() {
				
				$('#uploadBtn , #uploadicon').click(function() {
					$('#data-upload').click();
				});
				
			}, 500)
	
	
}