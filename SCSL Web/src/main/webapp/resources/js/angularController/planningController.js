//@author Devikrushna , Harsh

function planningController($scope, $http, $filter, allServices) {
	$scope.activeMenu = "Plan";
	$scope.pageName = "Plan";
	$("#loader-mask").show();
	$scope.agenda = false;
	$scope.planCalender = false;
	$scope.next = false;
	$scope.submit = false;
	$scope.plan = true;
	$scope.release = false;
	$scope.facilityNameDiv = true;
	$scope.showAssessement = false;
	$scope.showStatus = false;
	$scope.datePickerOpen = true;

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

	allServices
			.getPlanningData()
			.then(
					function(plandata) {
						$("#loader-mask").fadeOut();
						$scope.allPlanDetails = plandata;
						if ($scope.allPlanDetails.areaModel.length > 0) {
							$scope.areaList = $scope.convert(
									$scope.allPlanDetails['areaModel'], 1).children;
						}
						$scope.facillityDetailsList = $scope.allPlanDetails['facilityPlanningModel'];
						setTimeout(function() {
							$('[data-toggle="tooltip"]').tooltip();

						}, 500)
						// $( "#datepicker" ).datepicker({dateFormat:
						// "dd-mm-yy"});
						$scope.filter();
					}, function() {

					});

	$scope.selectState = function(state) {
		$("#loader-mask").show();
		$scope.selectedState = state;
		$scope.selectedDistrict = undefined;
		$scope.selectedFacility = undefined;
		$scope.facilityNameDiv = true;
		$scope.showAssessement = false;
		$scope.planCalender = false;
		$scope.next = false;
		$scope.release = false;
		$scope.agenda = false;
		$scope.submit = false;
		$scope.datePickerOpen = false;
		$scope.filter();

		$("#loader-mask").fadeOut();

	};
	$scope.selectDistrict = function(district) {
		$("#loader-mask").show();
		$scope.selectedDistrict = district;
		$scope.selectedFacility = undefined;
		$scope.facilityNameDiv = true;
		$scope.showAssessement = false;
		$scope.planCalender = false;
		$scope.next = false;
		$scope.agenda = false;
		$scope.release = false;
		$scope.submit = false;
		$scope.datePickerOpen = false;
		$scope.filter();
		$("#loader-mask").fadeOut();
	};
	// show facility name in left side

	$scope.showFacility = function(facility) {
		$scope.selectedFacility = facility;

		$scope.assessementList = facility.assessmentHistory;
		$scope.planHistoryList = facility.plannedHistory;
		$scope.userName = Object.keys($scope.assessementList);
		$scope.showAssessement = true;
		$scope.showStatus = false;
		$scope.planCalender = true;
		$scope.plan = true;
		$scope.release = false;
		$scope.next = false;
		$scope.agenda = false;
		$scope.submit = false;
		$scope.datePickerOpen = true;

		$("#datepicker").datepicker({
			dateFormat : "dd-mm-yy",
			autoclose : false,
			beforeShow : function(input, inst) {
				setTimeout(function() {
					inst.dpDiv.css({
						top : $("#datepicker").offset().top + 35,
						left : $("#datepicker").offset().left
					});
				}, 0);
//				if ($(window).width() >= 768 || $(window).width() <= 1024 )
//					setTimeout(function() {
//						inst.dpDiv.css({
//							top : $("#datepicker").offset().top + 35,
//							left : $("#datepicker").offset().left
//						});
//					}, 500);
		},
			onSelect : function() {
				$(this).data('datepicker').inline = true;
			},
			onClose : function() {
				$(this).data('datepicker').inline = false;
			}
			
		});
		$('#datepicker').datepicker('setDate', null);
		if ($(window).width() >= 768)
			$('html,body').animate({
				scrollTop : $('#topView').offset().top - 250
			}, 'slow');
		else {
			setTimeout(function() {
				$('html, body').animate({
					scrollTop : $("#calendertavView").offset().top
				}, 1000);
			}, 200)
		}

		$scope.clearFile();
		$scope.planButtonClicked();
		setTimeout(function() {
			angular.element('#calendar-btn').triggerHandler('click');
			$("#calendar-btn").addClass('active');
			$("#release-btn").removeClass('active');
		}, 20)

		return false;
	};

	$scope.resetDiv = function() {
		$scope.selectedState = undefined;
		$scope.selectedDistrict = undefined;
		$scope.selectedFacility = undefined;
		$scope.showAssessement = false;
		$scope.showStatus = false;
		$scope.planCalender = false;
		$scope.release = false;
		$scope.next = false;
		$scope.agenda = false;
		$scope.submit = false;
		$scope.datePickerOpen = false;
		$scope.filter();
		setTimeout(function() {
			$('[data-toggle="tooltip"]').tooltip();

		}, 20)

	};
	$scope.filter = function() {
		if ($scope.selectedState == undefined) {
			$scope.facillityDetails = $scope.facillityDetailsList;
		} else if ($scope.selectedDistrict != undefined) {
			$scope.facillityDetails = $filter('filter')(
					$scope.facillityDetailsList, {
						parentId : $scope.selectedDistrict.areaId
					}, true);
		} else if ($scope.selectedState != undefined) {
			$scope.facillityDetails = $filter('filter')(
					$scope.facillityDetailsList, {
						stateId : $scope.selectedState.areaId
					}, true);
		}
		$("#loader-mask").fadeOut();
	};
	// show status and date in left side
	$scope.showStatusDate = function(assessmentHistory) {
		$("#loader-mask").show();
		$scope.selectedAssessmentHistory = assessmentHistory;
		$scope.planButtonClicked();
		
		$scope.showStatus = true;
		$scope.showAssessement = false;
		$scope.agenda = false;
		$scope.next = true;
		$scope.submit = false;
		// $scope.next = false;
		setTimeout(function() {
			angular.element('#calendar-btn').triggerHandler('click');
			$("#calendar-btn").addClass('active');
			$("#release-btn").removeClass('active');

		}, 20)
$("#loader-mask").fadeOut();
		return false;
		
		
		
	};
	// show planned history in tool tip
	$scope.showPlan = function(facility) {
		var dateList = facility.facilityName;
		for (var i = 0; i < facility.plannedHistory.length; i++) {
			dateList += '<p>' + facility.plannedHistory[i].key + " " + "-"
					+ " " + facility.plannedHistory[i].value + " " + '</p>';
		}
		return dateList;

	}

	// submit agenda in right side
	$scope.submitAgenda = function() {
//		setTimeout(function() {
//			$("#ui-datepicker-div").css(
//					"right",
//					parseInt($("#ui-datepicker-div").css("right").replace(
//							"px")) + 8)
//		}, 200)
//		
		//$("#datepicker").removeClass('active');
		var selectDate = $('#datepicker').datepicker({
			dateFormat : 'dd,MM,yyyy'
		}).val();
		
		if (selectDate == '') {
			$scope.errorMsg = "Please select a date";
			$("#errorMessage").modal("show");
			setTimeout(function() {
				$("#ui-datepicker-div").css(
						"left",
						parseInt($("#ui-datepicker-div").css("left").replace(
								"px")) - 8)
			}, 200)
			$scope.planButtonClicked();
			$scope.planCalender = true;
			$scope.next = true;
			$scope.agenda = false;
			$scope.submit = false;
			$scope.planButtonClicked();
			setTimeout(function() {
				angular.element('#calendar-btn').triggerHandler('click');
				$("#calendar-btn").addClass('active');
				$("#release-btn").removeClass('active');
			}, 20)
			

		} else {
			$scope.planCalender = false;
			$scope.next = false;
			$scope.agenda = true;
			$scope.submit = true;
			$scope.datePickerOpen = false;
//			setTimeout(function() {
//				$("#ui-datepicker-div").css(
//						"left",
//						parseInt($("#ui-datepicker-div").css("left").replace(
//								"px")) + 8)
//			}, 200)
		}
	};

	// plan Button Clicked
	$scope.planButtonClicked = function() {

		// $("#release-btn").css({"background-color":
		// "#f0bf7f","color":"#333a3b","border":"none"});
		// $("#release-font").css({"color":"#333a3b","border":"none"});
		$scope.next = true;
		$scope.release = false;
		$('#calendar-btn').click(function() {
			$('#datepicker').click();
		});
		$("#datepicker").css("color", "rgb(101, 90, 215)");
		
		$('#datepicker').datepicker('setDate', null);
		// $( "#datepicker" ).datepicker({dateFormat: "dd-mm-yy"});
		var eventDatepicker = angular.element(document).find('#datepicker');
		eventDatepicker.datepicker('refresh');
		eventDatepicker.datepicker("option", "minDate",
				$scope.allPlanDetails.startDate);
		eventDatepicker.datepicker("option", "maxDate",
				$scope.allPlanDetails.endDate);

		var dates = [];
		/*
		 * for(i=0;i<$scope.selectedFacility.plannedHistory.length;i++) {
		 * dates.push($scope.selectedFacility.plannedHistory[i].key) }
		 */
		if ($scope.selectedFacility.realeaseDate != null) {
			for (var i = 0; i < $scope.selectedFacility.realeaseDate.length; i++) {
				dates.push($scope.selectedFacility.realeaseDate[i].value)
			}
		}
		// disabling specific dates
		eventDatepicker.datepicker("option", "beforeShowDay", function(date) {
			var string = jQuery.datepicker.formatDate('dd-mm-yy', date);
			return [ dates.indexOf(string) == -1 ]
		});
		
	};
	
	// release Button Clicked
	$scope.releaseButtonClicked = function() {
		// $("#release-btn").css({"background-color":
		// "#333a3b","color":"#f0a997","border":"none"});
		// $("#release-font").css({"color":"#f0a997","border":"none"});
		// $("#calendar-btn").css({"background-color":
		// "#f0bf7f","color":"#f0a997","border":"none"});
		// $("#plan-font").css({"color":"#333a3b","border":"none"});
		$('#release-btn').click(function() {
			$('#datepicker').click();
		});
		$('#datepicker').datepicker('setDate', null);
		// $( "#datepicker" ).datepicker({dateFormat: "dd-mm-yy"});
		var eventDatepicker = angular.element(document).find('#datepicker');
		console.log(eventDatepicker);
		eventDatepicker.datepicker('refresh');
		eventDatepicker.datepicker("option", "minDate",
				$scope.allPlanDetails.startDate);
		eventDatepicker.datepicker("option", "maxDate",
				$scope.allPlanDetails.endDate);

		var dates = [];
		if ($scope.selectedFacility.realeaseDate != null) {
			for (i = 0; i < $scope.selectedFacility.realeaseDate.length; i++) {
				dates.push($scope.selectedFacility.realeaseDate[i].value)
			}
		}
		// disabling specific dates
		eventDatepicker.datepicker("option", "beforeShowDay", function(date) {
			var string = jQuery.datepicker.formatDate('dd-mm-yy', date);
			return [ dates.indexOf(string) >= 0 ]
		});

		$scope.release = true;
		$scope.plan = false;
		$scope.next = false;

	}
	// open upload report modal
	$scope.openUploadReport = function() {

//		 $( "#datepicker2" ).datepicker({dateFormat: "dd-mm-yy",minDate:
//		 $scope.selectedAssessmentHistory.plannedDate,maxDate:$scope.allPlanDetails.serverDate});
//	$("#datepicker3").datepicker({
//			dateFormat : "dd-mm-yy",
//			minDate : $scope.selectedAssessmentHistory.plannedDate,
//			maxDate : $scope.allPlanDetails.serverDate
//		});
		var eventDatepicker = angular.element(document).find('#datepicker2');
		$("#datepicker2").css("color", "rgb(101, 90, 215)");
		eventDatepicker.datepicker('refresh');
		eventDatepicker.datepicker("option", "minDate",
				$scope.selectedAssessmentHistory.plannedDate);
		eventDatepicker.datepicker("option", "maxDate",
				$scope.allPlanDetails.serverDate);
		
		setTimeout(function() {
			$('#datepicker3').click(function() {
				$('#datepicker2').datepicker("show");
		});
		}, 200)
		
		$('#uploadReport').modal('show');
		
		$scope.uplodReport = undefined;
		$scope.selectDate = undefined;
		$scope.reportDescription = undefined;
		$scope.datePickerOpen = false;

	};
	// plan agenda
	$scope.getAgendaDetails = function(e) {
		if (e.files.length > 0) {
			$scope.agenda = [];
			$scope.$apply(function() {

				// STORE THE FILE OBJECT IN AN ARRAY.
				for (var i = 0; i < e.files.length; i++) {
					$scope.agendaFileExt = e.files[i].name.split(".").pop();
					if ($scope.agendaFileExt.toLowerCase() != "pdf") {
						$scope.agenda = undefined
						$scope.agendaUpload = undefined
						$scope.agendaReport = undefined;
						$scope.clearFile();
						$scope.errorMsg = "File must be of pdf type";
						$("#errorMessage").modal("show");
						$scope.agenda = true;
					} else if (e.files[i].size > 500000) {
						$scope.agenda = undefined
						$scope.agendaUpload = undefined
						$scope.agendaReport = undefined;
						$scope.clearFile();
						$scope.errorMsg = "File must be less than 500kb";
						$("#errorMessage").modal("show");
						$scope.agenda = true;
					} else {

						$scope.agenda.push(e.files[i])
						$scope.agendaUpload = $scope.agenda[0].name;
						$scope.agendaReport = $scope.agenda[0].name;
					}
				}
				// $scope.reportUpload = $scope.files[0].name;

			});
		}
	};
	// upload report validation
	$scope.getFileDetails = function(e) {

		if (e.files.length > 0) {
			$scope.files = [];
			$scope.$apply(function() {

				// STORE THE FILE OBJECT IN AN ARRAY.
				for (var i = 0; i < e.files.length; i++) {
					$scope.firstFileExt = e.files[i].name.split(".").pop();
					if ($scope.firstFileExt.toLowerCase() != "pdf") {
						$scope.files = undefined
						$scope.planUpload = undefined
						$scope.uplodReport = undefined;
						$scope.clearFile();
						$scope.errorMsg = "File must be of pdf type";
						$("#errorMessage").modal("show");
					} else if (e.files[i].size > 500000) {
						$scope.files = undefined
						$scope.planUpload = undefined
						$scope.uplodReport = undefined;
						$scope.clearFile();
						$scope.errorMsg = "File must be less than 500kb";
						$("#errorMessage").modal("show");
					} else {
						$scope.files.push(e.files[i])
						$scope.planUpload = $scope.files[0].name;
						$scope.uplodReport = $scope.files[0].name;
					}
				}
				// $scope.reportUpload = $scope.files[0].name;

			});
		}
	};
	$scope.clearData = function() {
		$scope.clearFile();
		$('.menuSlideBtn').show();
		$scope.datePickerOpen = true;
		$("#datepicker").datepicker({
			dateFormat : "dd-mm-yy",
			autoclose : false,
			beforeShow : function(input, inst) {
				setTimeout(function() {
					inst.dpDiv.css({
						top : $("#datepicker").offset().top + 35,
						left : $("#datepicker").offset().left
					});
				}, 0);
			},
			onSelect : function() {
				$(this).data('datepicker').inline = true;
			},
			onClose : function() {
				$(this).data('datepicker').inline = false;
			}
		});
		if ($(window).width() >= 768)
			setTimeout(function() {
				$("#ui-datepicker-div").css(
						"left",
						parseInt($("#ui-datepicker-div").css("left").replace(
								"px")) - 8)
			}, 200)
		if ($(window).width() == 768 || $(window).width() == 1024 )
			setTimeout(function() {
				$("#ui-datepicker-div").css(
						"left",
						parseInt($("#ui-datepicker-div").css("left").replace(
								"px")) + 8)
			}, 200)	
			
		else
			setTimeout(function() {
				$("#ui-datepicker-div").css(
						"left",
						parseInt($("#ui-datepicker-div").css("left").replace(
								"px")))
			}, 200)
		$scope.planButtonClicked();
		setTimeout(function() {
			angular.element('#calendar-btn').triggerHandler('click');
			// $("#calendar-btn").css({"background-color":
			// "#333a3b","color":"#f0a997","border":"none"});
			$("#calendar-btn").addClass('active');
			$("#release-btn").removeClass('active');
			// $("#plan-font").css({"color":"#f0a997","border":"none"});

		}, 20)

	}
	// clear file for trip report and agenda report
	$scope.clearFile = function() {

		angular.element("input[type='file']").val(null);
		$scope.files = undefined;
		$scope.planUpload = undefined;
		//$scope.uplodReport = undefined;
		$scope.agenda = undefined
		$scope.agendaUpload = undefined
		$scope.agendaReport = undefined;
		$scope.planTag = undefined;
		//$scope.selectDate = undefined;
		//$scope.reportDescription = undefined;
		document.getElementById('invalidmsg').innerHTML = "";

	};

	$scope.validateEmail = function(name, errorId) {
		var mailArr = [];
		var splitA = [];
		var splitAflag = false;
		if (name != undefined) {
			if (name.includes("@")) {
				splitA = name.split("@");
				for (var i = 0; i < splitA.length; i++) {
					if (splitA[1] == "") {
						document.getElementById("invalidmsg").innerHTML = "Please enter valid email id";
					}
				}
			}
			if (!name.includes("@") || !name.includes(".")
					|| name.includes("!") || name.includes("#")
					|| name.includes("$") || name.includes("%")
					|| name.includes("^") || name.includes("&")
					|| name.includes("*") || name.includes("(")
					|| name.includes(")") || name.includes("_")
					|| name.includes("+")) {
				document.getElementById("invalidmsg").innerHTML = "Please enter valid email id";
			} else {
				mailArr = name.split(",");
				for (var i = 0; i < mailArr.length; i++) {
					var nextAsplit = mailArr[i].split("@");
					for(var j=0; j<nextAsplit.length;j++){
						if(nextAsplit[j] == "" || !nextAsplit[nextAsplit.length-1].includes("."))
							splitAflag = true;
						else
							splitAflag = false;
					}
				}
				for (var i = 0; i < mailArr.length; i++) {
					var aCount = 0;
					var dotCount = 0;
					if (mailArr[i] == "") {
						document.getElementById("invalidmsg").innerHTML = "Please enter valid email id";
					} else if (!mailArr[i].includes("@")
							|| !mailArr[i].includes(".")) {
						document.getElementById("invalidmsg").innerHTML = "Please enter valid email id";
					}
					for (var j = 0; j < mailArr[i].length; j++) {
						if (mailArr[i][j] == "@") {
							aCount++;
						}
						if (mailArr[i][j] == ".") {
							dotCount++;
						}
					}
				}
				if (aCount > 1) {
					document.getElementById("invalidmsg").innerHTML = "Please enter valid email id";
				}
				if (dotCount >= 1) {
					var flag = false;
					for (var i = 0; i < mailArr.length; i++) {
						var dotArr = mailArr[i].split(".");
						for (var j = 0; j < dotArr.length; j++) {
							if (dotArr.length == 1 || dotArr[j] == "") {
								document.getElementById("invalidmsg").innerHTML = "Please enter valid email id";
								flag = true;
							}
							if (dotArr[j] == "" || dotArr[j].length == 1
									|| flag || !dotArr[0].includes("@") || dotArr[dotArr.length-1] == "") {
								document.getElementById("invalidmsg").innerHTML = "Please enter valid email id";
							} else {
								document.getElementById("invalidmsg").innerHTML = "";
							}
							if(!dotArr[j].includes("@") || splitAflag){
								document.getElementById("invalidmsg").innerHTML = "";
							}else if(dotArr[j].includes("@") && splitA[splitA.length-1]==""){
								document.getElementById("invalidmsg").innerHTML = "Please enter valid email id";
							}
							if(dotArr[dotArr.length-1] == "" || splitAflag){
								document.getElementById("invalidmsg").innerHTML = "Please enter valid email id";
							}else {
								document.getElementById("invalidmsg").innerHTML = "";
							}
						}
					}
				}
				//
			}
		}
		if($scope.planTag=="" || $scope.planTag==undefined){
			document.getElementById("invalidmsg").innerHTML = "";
		}
	};
	$scope.clearId = function() {
		$scope.planTag = null;
		document.getElementById("invalidmsg").innerHTML = "";

	};
	// plan submit
	$scope.validatePlan = function() {
		if ($scope.agendaUpload == undefined) {
			$scope.errorMsg = "Please upload agenda";
			$("#errorMessage").modal("show");
		}
		else if (document.getElementById('invalidmsg').innerHTML != "") {
			$scope.errorMsg = "Please enter valid email id";
			$("#errorMessage").modal("show");

		} else {
			$("#loader-mask").show();
			$scope.submitPlan();
			
			
		}
	}

	$scope.submitPlan = function() {

		var selectDate = $('#datepicker').datepicker({
			dateFormat : 'dd,MM,yyyy'
		}).val();
		$scope.txnPlanningModel = {
			planningId : 0,
			date : selectDate,
			tagEmailIds : $scope.planTag,
			tripDescription : '',
			facilityId : $scope.selectedFacility.facilityId

		};

		$("#loader-mask").show();
		$http(
				{
					url : 'addPlannings',
					method : 'POST',
					headers : {
						'Content-Type' : undefined
					},
					transformRequest : function(data) {
						var formData = new FormData();

						formData.append('txnPlanningModel', new Blob(
								[ (data.txnPlanningModel) ], {
									type : "application/json"
								}));

						formData.append("agenda", data.agenda[0]);
						return formData;
					},
					data : {
						txnPlanningModel : angular
								.toJson($scope.txnPlanningModel),
						agenda : $scope.agenda
					}
				}).then(function successCallback(response) {
			$("#loader-mask").fadeOut();
			if (response.data.statusCode == '404') {
				$scope.clearFile();
				$scope.errorMsg = response.data.errorMessage;
				$("#errorMessage").modal("show");
				$scope.submit = false;
			} else {
				$scope.msg = response.data.errorMessage;
				$("#pop").modal("show");

			}
		}, function errorCallback(response) {
		});
		;

	}
	$scope.releasePlan = function() {
		$("#loader-mask").show();
		$scope.datePickerOpen = true;
		var selectDate = $('#datepicker').datepicker({
			dateFormat : 'dd,MM,yyyy'
		}).val();
		if (selectDate == null || selectDate == '') {
			$scope.errorMsg = 'Please select a date ';
			$("#errorMessage").modal("show");
			$("#loader-mask").fadeOut();
		} else {

			var planningId;
			for (i = 0; i < $scope.selectedFacility.realeaseDate.length; i++) {
				// dates.push($scope.selectedFacility.realeaseDate[i].value)
				if ($scope.selectedFacility.realeaseDate[i].value == selectDate) {
					planningId = $scope.selectedFacility.realeaseDate[i].key
					break;
				}
			}
			$("#loader-mask").show();
			$http({
				url : 'releasePlanning?planningId=' + planningId,
				method : 'POST',
			}).then(function successCallback(response) {
				$("#loader-mask").fadeOut();
				if (response.data.statusCode == '404') {
					$scope.errorMsg = response.data.errorMessage;
					$("#errorMessage").modal("show");
					$("#loader-mask").fadeOut();
				} else {
					$scope.msg = response.data.statusMessage;
					$("#pop").modal("show");

				}
			}, function errorCallback(response) {
			});
			;
		}
	}

	// save uploaded report
	$scope.saveReport = function() {
		if ($scope.selectDate == undefined) {
			$scope.errorMsg = "Please select a date";
			$("#errorMessage").modal("show");
		} else if ($scope.reportDescription == undefined) {
			$scope.errorMsg = "Please enter description";
			$("#errorMessage").modal("show");
		} else if ($scope.uplodReport == undefined) {
			$scope.errorMsg = "Please upload report";
			$("#errorMessage").modal("show");
		} else {
			$("#loader-mask").show();

			$scope.submitTripReport();
		}

	};
	$scope.submitTripReport = function() {
		$("#loader-mask").fadeOut();
		var selectDate = $('#datepicker2').datepicker({
			dateFormat : 'dd,MM,yyyy'
		}).val();
		$scope.txnPlanningModel = {
			planningId : $scope.selectedAssessmentHistory.planningId,
			date : selectDate,
			tagEmailIds : '',
			tripDescription : $scope.reportDescription,
			facilityId : 0

		};

		$("#loader-mask").show();
		$http(
				{
					url : 'closePlanning',
					method : 'POST',
					headers : {
						'Content-Type' : undefined
					},
					transformRequest : function(data) {
						var formData = new FormData();

						formData.append('txnPlanningModel', new Blob(
								[ (data.txnPlanningModel) ], {
									type : "application/json"
								}));

						formData.append("tripReport", data.tripReport[0]);
						return formData;
					},
					data : {
						txnPlanningModel : angular
								.toJson($scope.txnPlanningModel),
						tripReport : $scope.files
					}
				}).then(function successCallback(response) {
			$("#loader-mask").fadeOut();
			if (response.data.statusCode == '404') {
				$scope.clearFile();
				$scope.errorMsg = response.data.errorMessage;
				$("#errorMessage").modal("show");
			} else {
				$scope.msg = response.data.errorMessage;
				$("#pop").modal("show");

			}
		}, function errorCallback(response) {
		});
		;
	}
	$scope.downloadFile = function(fileName, $event) {
		$scope.showStatus = false;
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
			$("#loader-mask").fadeOut();
			// $("#pop").modal("show");
		};

		var result = {
			"fileName" : fileName
		};
		$.download("downloadPlanningFile/", result, 'POST');
		$event.stopPropagation();
	};

	$scope.planPage=function(){
		$("#pop").modal("hide");
		$("#pop1").modal("hide");
		$("#uploadReport").modal("hide");
		allServices
		.getPlanningData()
		.then(
				function(plandata) {
					$("#loader-mask").fadeOut();
					$scope.allPlanDetails = plandata;
					if ($scope.allPlanDetails.areaModel.length > 0) {
						$scope.areaList = $scope.convert(
								$scope.allPlanDetails['areaModel'], 1).children;
					}
					$scope.facillityDetailsList=undefined;
					$scope.facillityDetailsList = $scope.allPlanDetails['facilityPlanningModel'];
					setTimeout(function() {
						$('[data-toggle="tooltip"]').tooltip();

					}, 500)
					// $( "#datepicker" ).datepicker({dateFormat:
					// "dd-mm-yy"});
					$scope.filter();
					var facility=$filter('filter')(
							$scope.facillityDetailsList, {
								facilityId : $scope.selectedFacility.facilityId
							}, true);
					$scope.showFacility(facility[0]);
					setTimeout(function() {
						$("#ui-datepicker-div").css(
								"left",
								parseInt($("#ui-datepicker-div").css("left").replace(
										"px")) - 8)
					}, 200)
				}, function() {

				});
		
//		setTimeout(function() {
//			$("#ui-datepicker-div").css(
//					"left",
//					parseInt($("#ui-datepicker-div").css("left").replace(
//							"px")) - 8)
//		}, 200)
		
	};
	setTimeout(function() {

		$('#reportUpload,#trip_img').click(function() {
			$('#reportplan-upload').click();
		});
		$('#agenda,#agendaimg').click(function() {
			$('#agenda-upload').click();
		});

	}, 500)

};