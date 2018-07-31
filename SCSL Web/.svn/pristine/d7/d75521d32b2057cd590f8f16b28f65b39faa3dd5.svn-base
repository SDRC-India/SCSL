function dashboardFacilityViewController($scope, $http, $timeout, $window, $filter, allServices){
	$scope.activeMenu = 'dashboardFacilityView';
	$scope.pageName = 'Facility View';
	$scope.lineChartData = undefined;
	
	
	function ValueObject(key, value) {
		this.key = key;
		this.value = value;
	};
	


		var w = angular.element($window);
		$scope.getWindowDimensions = function() {
			return {
				"h" : w.height(),
				"w" : (w.width() * 90 / 100)
			};
		};
		// this is to make sure that scope gets changes as window get resized.
		w.on("resize", function() {
			if(!$scope.$$phase)
			$scope.$apply();
		});
		$(".loader").show();
		$scope.pixelOffset = {

			pixelOffset : new google.maps.Size(0, -28)
		};

		$scope.map = {
			center : {
				latitude : 15.1124372,
				longitude : 79.633147
			},
			bounds : {},
			clickMarkers : [],
			zoom : 6,
			events : {
				"mouseover" : function(mapModel, eventName, originalEventArgs) {
					for (var i = 0; i < $scope.map.markers.length; i++) {
						if ($scope.map.markers[i].id == originalEventArgs.id) {
							$scope.map.markers[i].showWindow = true;
							break;
						}
					}
					if(!$scope.$$phase)
					$scope.$apply();
				},
				"mouseout" : function(mapModel, eventName, originalEventArgs) {
					for (var i = 0; i < $scope.map.markers.length; i++) {
						if ($scope.map.markers[i].id == originalEventArgs.id) {
							$scope.map.markers[i].showWindow = false;
							break;
						}
					}
					if(!$scope.$$phase)
					$scope.$apply();
				},
				"click" : function(mapModel, eventName, originalEventArgs) {
					$(".loader").show();
					$scope.selectedPushpin = originalEventArgs;
					$scope.getTrendData($scope.selectedPushpin.facilityId);
					if(!$scope.$$phase)
					$scope.$apply();

				}
			}
		};
		
		/*Creates polygon inside map
		*/
		$scope.polygons = [ {
			id : 1,
			stroke : {
				color : '#f75b46',
				weight : 2,
			},
			path: apPolygon,
			editable : true,
			draggable : false,
			geodesic : false,
			visible : true,
			fill : {
				color : 'rgb(242,230,223)',
				opacity : 0.7
			}
		}
		
		,
		{
			id : 2,
			stroke : {
				color : '#f75b46',
				weight : 2,
			},
			path: telanganaRP,
			editable : true,
			draggable : false,
			geodesic : false,
			visible : true,
			fill : {
				color : 'rgb(242,230,223)',
				opacity : 0.7
			}
		}
		];
		$scope.map.markers = [];
		
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
		
		/*
		 * This function counts number green/orange/red pushpins
		*/
		$scope.countPushpins = function(){
			$scope.greenMarkers = 0;
			$scope.redMarkers = 0;
			$scope.orangeMarkers = 0;
			for (var i = 0; i < $scope.map.markers.length; i++) {
				if (parseFloat($scope.map.markers[i].score) < 2)
					$scope.redMarkers++;
				else if (parseFloat($scope.map.markers[i].score) < 4)
					$scope.orangeMarkers++;
				else
					$scope.greenMarkers++;
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
		
		/*
		 * gets Facility Type list and Facility size list for dropdown
		*/
		$scope.getFacilitySizeAndType = function(){
			allServices.getFacilityTypeAndSize().then(function(data) {
				$scope.facilitytypes = data["type"];
				$scope.facilitySizes = data["size"];
				$scope.filteredFacilitySizes = $scope.facilitySizes;
				$("#loader-mask").fadeOut();
			}, function() {

			});
		};
		
		/*
		 * Gets map pushpin list
		*/
		$scope.getPushpinData = function(selectedParentSectorFormId,
				selectedSectorFormXpathScoreId, areaId) {
			$scope.map.markers = [];
			$scope.markersArray = [];
			$scope.totalMarkers = [];
			$(".loader").show();
			$http.get('getEngagementScorePushPins').success(function(data) {
				$(".loader").fadeOut();
				$scope.allMarkers = data;	//All pushpins stored
				$scope.map.markers = data;	// Pushpins to be shown on load 
				$scope.markersArray = $scope.map.markers;	// used while searching for pushpins
				$scope.countPushpins();
				if(!$scope.$$phase)
				$scope.$apply();
			});
		};
		$scope.getArea();                                           //State and District list for dropdown
		$scope.getFacilitySizeAndType();                            //Facility size and type for dropdown
		$scope.getPushpinData();									//Map pushpins 
		
		/*
		 * Get Trend chart when click on pushpins 
		*/
		
		$scope.getTrendData = function(facilityId){
			$("#loader-mask").show();
			$scope.lineChartData = [];
			allServices.getTrendData(facilityId).then(function(data) {
				$scope.isTrendVisible = true;
				$("#loader-mask").fadeOut();
				$timeout(function(){
					$scope.lineChartData.push(data);
				})
				
				if(!$scope.$$phase)
					$scope.$apply();
			}, function() {

			});
		};
		/*
		 * Closes trend chart
		*/
		$scope.closeViz = function() {
			$scope.isTrendVisible = false;
			$scope.lineChartData = [];
		};
		
		
		/*
		 * Filters facility size according to selected facilityType(public/private)
		*/
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
		
		
		$scope.selectState = function(state) {
			$scope.closeViz();
			$scope.selectedState = state;
			$scope.selectedDistrict = undefined;
			$scope.selectedFacilityType = undefined;
			$scope.filterPushpins($scope.selectedState.areaId, false, false, false);
		};
		$scope.selectDistrict = function(district) {
			$scope.closeViz();
			$scope.selectedDistrict = district;
			$('[data-toggle="tooltip"]').tooltip();
			$scope.filterPushpins(false, $scope.selectedDistrict.areaId, false, false);
		};
		$scope.selectFacilityType = function(facilitytype) {
			$scope.closeViz();
			$scope.selectedFacilityType = facilitytype;
			$scope.selectedFacilitySize=undefined;
			$scope.filteredFacilitySizes = [];
			$scope.filterFacilitySize(facilitytype);
			$scope.filterPushpins(false, false, $scope.selectedFacilityType.facilityType, false);
		};
		$scope.selectFacilitySize = function(facilitysize) {
			$scope.closeViz();
			$scope.selectedFacilitySize = facilitysize;
			$scope.selectedFacility=undefined;
			$scope.filterPushpins(false, false, false, $scope.selectedFacilitySize.facilitySize);
		};
		
		$scope.resetSelection = function(){
			$scope.closeViz();
			$scope.selectedState = undefined;
			$scope.selectedDistrict = undefined;
			$scope.selectedFacilityType = undefined;
			$scope.selectedFacilitySize = undefined;
			$scope.selectedFacility = undefined;
			$scope.filterPushpins();
		};

		$("#searchDashboard").autocomplete({
			source : $scope.optArrayDashboard,
			appendTo : "#searchDivDashboard",
			select: function(event, ui) {
				document.getElementById('searchDashboard').value = ui.item.value;
				$scope.searchNodeDashboard();
				$scope.$apply();
		    }
		});
		$scope.searchNodeDashboard= function() {
			$scope.optArrayDashboard = [];
			$scope.nooptArrayDashboard = [];
			var selectedVal = document.getElementById('searchDashboard').value;
			var node = [];
			var colorChange = [];
				$scope.optArrayDashboard = [];
				for(var i = 0; i<$scope.markersArray.length; i++){
					node.push($scope.markersArray[i].title);
					colorChange.push($scope.markersArray[i].id);
				}
				
				if (selectedVal == "") {
					for (var i = 0; i < $scope.map.markers.length; i++) {
//						var iconPath = $scope.map.markers[i].path;
						var icon = $scope.map.markers[i].icon;
						var replacedIcon = icon.replace(".png", "").replace("_op", "");
						$scope.map.markers[i].icon = replacedIcon+".png";
					}
				} else {
					for(var i = 0; i<$scope.markersArray.length; i++){
						if (node[i].toUpperCase().match(selectedVal.toUpperCase())) {
							$scope.optArrayDashboard.push(node[i]);
						}else{
							$scope.nooptArrayDashboard.push(node[i]);
						}
					}
					
					$("#searchDashboard").autocomplete({
						source : $scope.optArrayDashboard,
						appendTo : "#searchDivDashboard",
						select: function(event, ui) {
							document.getElementById('searchDashboard').value = ui.item.value;
							$scope.searchNodeDashboard();
							$scope.$apply();
					    }
					});
					
					for(var j=0;j<$scope.optArrayDashboard.length;j++){
						for (var i = 0; i < $scope.map.markers.length; i++) {
							if ($scope.map.markers[i].title == $scope.optArrayDashboard[j]) {
//								var iconPath = $scope.map.markers[i].path;
								var icon = $scope.map.markers[i].icon;
								var replacedIcon = icon.replace(".png", "").replace("_op", "");
								$scope.map.markers[i].icon = replacedIcon+".png";
							}
						}
					}
					for(var j=0;j<$scope.nooptArrayDashboard.length;j++){
						for (var i = 0; i < $scope.map.markers.length; i++) {
							if ($scope.map.markers[i].title == $scope.nooptArrayDashboard[j]) {
//								var iconPath = $scope.map.markers[i].path;
								var icon = $scope.map.markers[i].icon;
								var replacedIcon = icon.replace(".png", "").replace("_op", "");
								$scope.map.markers[i].icon = replacedIcon+"_op.png";
							}
						}
					}
				}
			};
			
			/*
			 * Filter pushpin data according to dropdown selection
			*/
			$scope.filterPushpins = function(stateId, distId, facilityTypeId, facilitySizeId){
				var allPushpins = [];
				for(var i=0; i<$scope.allMarkers.length; i++){
					if(($scope.selectedState == undefined || $scope.allMarkers[i].sateId == $scope.selectedState.areaId) && ($scope.selectedDistrict == undefined || $scope.allMarkers[i].disrictId == $scope.selectedDistrict.areaId)
							&& ( $scope.selectedFacilityType == undefined || $scope.allMarkers[i].facilityIypeId == $scope.selectedFacilityType.facilityType) && ($scope.selectedFacilitySize == undefined || $scope.allMarkers[i].facilitySizeId == $scope.selectedFacilitySize.facilitySize)){
						allPushpins.push($scope.allMarkers[i]);
					}
				}
				$scope.map.markers = allPushpins;
				$scope.countPushpins();
			};
			
			//trend on click of download button
			$scope.downloadGoogleMapTrend = function() {
				
				$("#loader-mask").show();
				d3.selectAll(".domain, .y.axis .tick line").style({"fill": "none", "stroke": "#000"});
				d3.selectAll(".domain, .x.axis .tick line").style({"fill": "none", "stroke": "#000"});
				d3.selectAll("text").style({"fill":"#333a3b", "font-weight": "bold"});;
				d3.selectAll("svg").attr("version", 1.1).attr("xmlns", "http://www.w3.org/2000/svg");
				
				var url = 'getGoogleMapTrendPath?areaName='+$scope.selectedPushpin.title;
				
				var chartSvg = $("#trendLineChart").html();
				
				$http.post(url, [chartSvg]).then(function(result) {

					var fileName = {
							"fileName" : result.data
						};
						$.download("downloadReport/", fileName, 'POST');
						d3.selectAll("text").style({"fill":"#f2f2f2", "font-weight": "bold"});;
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
	};


	$(document).ready(function() {
		$(".dist-list ul.dropdown-menu input").click(function(e) {
			e.preventDefault();
		});
	});
	
	
