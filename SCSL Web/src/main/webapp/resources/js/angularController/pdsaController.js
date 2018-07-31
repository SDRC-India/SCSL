//@author Devikrushna (devikrushna@sdrc.co.in)
function pdsaController($scope, $http, $filter, allServices) {
	$scope.activeMenu = "pdsa";
	$scope.pageName = "PDSA";
	$scope.firstFileName = "UPLOAD";
	$scope.activeMenu = "pdsaDataEntry";
	$scope.sortType = null;
	$scope.sortReverse = false;

	$("#loader-mask").show();
	allServices.getPDSAFilterOption().then(function(data) {
		$("#loader-mask").fadeOut();
		$scope.indicatordata = data.Indicator;
		$scope.coreAreaList = Object.keys($scope.indicatordata);
		$scope.IndicatorList = undefined;
		$scope.changeIdeaList = undefined;
		$scope.frequencyList = data.frequencies;
	}, function() {

	});
	allServices.getPDSAForFacility().then(function(data) {
		$("#loader-mask").fadeOut();
		$scope.totalpdsaList = data;
		$scope.pdsaList = $scope.totalpdsaList;
		$scope.columns = Object.keys($scope.pdsaList[0]);
	}, function() {

	});

	allServices.getClosingPDSAStatus().then(function(status) {
		$("#loader-mask").fadeOut();
		$scope.allstatusList = status;
		$scope.statusList = $scope.allstatusList;
		$scope.columns = Object.keys($scope.statusList[0]);
	}, function() {

	});

	$scope.selectStatus = function(status) {
		$scope.selectedStatus = status;

	};
	$scope.selectArea = function(area) {
		$scope.selectedArea = area;
		$scope.coreIndicatorList = $scope.indicatordata[area];
		$scope.selectedIndicator = undefined;
		$scope.selectedChangeIdea = undefined;
		$scope.changeIdeaList = undefined;
		$("#loader-mask").show();
		$scope.filter();
	};
	$scope.selectIndicator = function(indicator) {
		$scope.selectedIndicator = indicator;
		$scope.selectedChangeIdea = undefined;
		$scope.changeIdeaList = $scope.selectedIndicator.valueModelList;
		$("#loader-mask").show();
		$scope.filter();
		$scope.addIdea = true;

	};
	$scope.selectIdea = function(idea) {
		$scope.selectedChangeIdea = idea;
		$("#loader-mask").show();
		$scope.filter();

	};
	$scope.changeIdeaMore= function(event){
		$('#viewChangeIdeaMore').modal('show');
		return false;
		
	};
	$scope.resetDiv = function(){
		$scope.selectedArea = undefined;
		$scope.selectedIndicator =undefined;
		$scope.selectedChangeIdea = undefined;
		$scope.coreIndicatorList=[];
		$scope.changeIdeaList=[];
		$scope.filter();
	};
	
	
	$scope.openPdsaDataentry = function(selectedPdsaModal) {
		
		$scope.selectedPdsaModal = selectedPdsaModal;
		$("#loader-mask").show();
		allServices
				.getTXNPDSADetails($scope.selectedPdsaModal.pdsaId)
				.then(
						function(data) {
							$("#loader-mask").fadeOut();
							$scope.pdsaNumeratorDenominetor = data.pdsaModel;
							$scope.pdsaTXNData = data.txnPDSAModel;
							$scope.disableClosePDSA = false;
							for (var i = 0; i < $scope.pdsaTXNData.length; i++) {
								if ($scope.pdsaTXNData[i].percentage == null) {
									$scope.disableClosePDSA = true;
								}
							}
							setTimeout(function() {
								$('#uploadBtn').click(function() {
									$('#modal-upload').click();

									// var data = new FormData();
									//
									// for (var i in $scope.files) {
									// data.append("uploadedFile",
									// $scope.files[i]);
									// }
								});
								$('#lastdoc, #lasticon').click(function() {
									$('#lastdoc-upload').click();

									// var lastdata = new FormData();
									//
									// for (var i in $scope.lastfile) {
									// lastdata.append("uploadedFile",
									// $scope.lastfile[i]);
									// }
								});
								$('#otherdoc, #othericon').click(function() {
									$('#otherdoc-upload').click();
								});
							}, 500)

							if ($scope.pdsaTXNData.length > 0) {
								$scope.ldata = [];
								$scope.ldata.push(data.chartDataModel);
								$scope.lastfile = undefined;
								$scope.otherfile = undefined
								$scope.otherDoc = undefined
								$scope.selectedStatus = undefined;
								$scope.briefdescription = undefined;
								$scope.files = undefined
								$scope.firstFileName = "UPLOAD";
								$scope.lastfile = undefined
								$scope.lastDoc = undefined

								$('#viewDataEntryModal').modal('show');
								$scope.columns = Object
										.keys($scope.pdsaTXNData[0]);
							} else {
								$scope.infoMsg = "The selected pdsa has not yet been started";
								$('#infoMessage').modal('show');

							}

						}, function() {

						});
	};

	$scope.calculatePercentage = function(rowData) {
		if (rowData.numeratorValue && rowData.denominatorValue)
			{
			if(rowData.denominatorValue==0)
				{
				rowData.percentage=0;
				}
			else
				
			rowData.percentage = Math.round(rowData.numeratorValue
					/ rowData.denominatorValue * 100 * 10) / 10;
			}
		else
			rowData.percentage = "";
	};

	$scope.validatePdsaNumerator = function(rowData) {
		var validated = true;
		if ($scope.pdsaTXNData[0].enable) {
			if ($scope.files == undefined) {
				validated = false;
				$scope.errorMsg = "First Document is mandatory to upload";
				$("#errorMessage").modal("show");
				return;
			} else {
				validated = true;
				$scope.pdsaTXNData[0].document = $scope.files;
			}

			if ($scope.pdsaTXNData[0].numeratorValue == undefined
					|| $scope.pdsaTXNData[0].denominatorValue == undefined) {
				validated = false;
				$scope.errorMsg = "Please enter both numerator and denominator value for pdsa no 1";
				$("#errorMessage").modal("show");
				return;

			} else if ($scope.pdsaTXNData[0].numeratorValue == ''
					|| $scope.pdsaTXNData[0].denominatorValue == '') {
				validated = false;
				$scope.errorMsg = "Please enter both numerator and denominator value for pdsa no 1";
				$("#errorMessage").modal("show");
				return;

			} else {
				validated = true;
			}
		}

		for (i = 0; i < $scope.pdsaTXNData.length; i++) {
			if (($scope.pdsaTXNData[i].numeratorValue == undefined && $scope.pdsaTXNData[i].denominatorValue != undefined)
					|| ($scope.pdsaTXNData[i].numeratorValue != undefined && $scope.pdsaTXNData[i].denominatorValue == undefined)
					|| (($scope.pdsaTXNData[i].numeratorValue != undefined || $scope.pdsaTXNData[i].denominatorValue != undefined) && ($scope.pdsaTXNData[i].percentage == '' ))) {
				if (($scope.pdsaTXNData[i].numeratorValue == null && $scope.pdsaTXNData[i].denominatorValue == '')
						|| ($scope.pdsaTXNData[i].numeratorValue == '' && $scope.pdsaTXNData[i].denominatorValue == null)
						|| ($scope.pdsaTXNData[i].numeratorValue == '' && $scope.pdsaTXNData[i].denominatorValue == '')) {
					$scope.pdsaTXNData[i].percentage = null;
					$scope.pdsaTXNData[i].numeratorValue = null;
					$scope.pdsaTXNData[i].denominatorValue = null;
				}

				/*else if ($scope.pdsaTXNData[i].denominatorValue!=''&&$scope.pdsaTXNData[i].denominatorValue==0) {
					validated = false;
					$scope.errorMsg = "Denominator  value cannot be zero";
					$("#errorMessage").modal("show");
					return;

				}*/
				else if($scope.pdsaTXNData[i].percentage===0&&$scope.pdsaTXNData[i].denominatorValue==0)
					{
					validated=true;
					}
				
				else {
					validated = false;
					$scope.errorMsg = "Please enter both numerator and denominator value for pdsa no "
							+ (i + 1);
					$("#errorMessage").modal("show");
					return;
				}

			}
		}
		if (validated == true) {
			$("#loader-mask").show();
			$http({
				url : 'saveTXNPDSADetails',
				method : 'POST',
				headers : {
					'Content-Type' : undefined
				},
				transformRequest : function(data) {
					var formData = new FormData();

					formData.append('txnPdsa', new Blob([ (data.txnPdsa) ], {
						type : "application/json"
					}));
					if ($scope.pdsaTXNData[0].enable) {
						formData.append("file", data.file[0]);
					}
					return formData;
				},
				data : {
					txnPdsa : angular.toJson($scope.pdsaTXNData),
					file : $scope.files
				}
			}).then(function successCallback(response) {
				$("#loader-mask").fadeOut();
				if (response.data.statusCode == '404') {
					$scope.errorMsg = response.data.errorMessage;
					$("#errorMessage").modal("show");
				} else {
					$scope.msg = response.data.errorMessage;
					$scope.link = "pdsa";
					$("#pop").modal("show");

				}
			}, function errorCallback(response) {
			});
			;
		}
	};
	$scope.validateClosePdsa = function() {
		if ($scope.lastfile == undefined) {
			$scope.errorMsg = "Last Document is mandatory to upload";
			$("#errorMessage").modal("show");

		} else if ($scope.selectedStatus == undefined) {
			$scope.errorMsg = "Status is mandatory ";
			$("#errorMessage").modal("show");

		}else if ($scope.briefdescription == undefined
				|| $scope.briefdescription == '') {
			$scope.errorMsg = "Brief description is mandatory ";
			$("#errorMessage").modal("show");

		} else if($scope.briefdescription.length < 150){
			$scope.errorMsg="Brief description should be minimum 150 character length";
			$("#errorMessage").modal("show");
			
		}else {
			$("#loader-mask").show();
			$scope.allfiles = [];
			$scope.allfiles.push($scope.lastfile[0]);
			if ($scope.otherfile != undefined) {
				$scope.allfiles.push($scope.otherfile[0]);
			}
			var filesJson = []
			filesJson.push(($scope.allfiles[0]));
			if ($scope.otherfile != undefined) {
				filesJson.push(($scope.allfiles[1]));
			}
			var files = JSON.stringify(filesJson)
			var url = 'closePDSA?pdsaId=' + $scope.selectedPdsaModal.pdsaId
					+ '&statusId=' + $scope.selectedStatus.key
					+ '&description=' + $scope.briefdescription;

			var fd = new FormData();
			// fd.append('project',projects);
			fd.append('documents', filesJson[0]);
			fd.append('documents', filesJson[1]);
			// $http.post( url , fd)
			// .then(function(result){
			// if (request.readyState == 4) {
			// var jsonResponse = JSON.parse(request.responseText);
			// if(jsonResponse.statusCode==200)
			// {
			// $scope.msg =jsonResponse.errorMessage;
			// $("#pop").modal("show");
			// $scope.link="pdsa";
			// }
			// else if(jsonResponse.statusCode==404)
			// {
			// $scope.errorMsg = jsonResponse.errorMessage;
			// $("#errorMessage").modal("show");
			// }
			// // defensive check
			// if (typeof callback == "function") {
			// // apply() sets the meaning of "this" in the callback
			// callback.apply(request);
			// }
			// }
			// }, function(error){
			// console.log(error);
			// });
			var request = new XMLHttpRequest();
			request.open('POST', url, true);

			request.onreadystatechange = function() {
				if (request.readyState == 4) {
					$("#loader-mask").fadeOut();
					var jsonResponse = JSON.parse(request.responseText);
					if (jsonResponse.statusCode == 200) {
						$("#pop1").modal("show");
					} else if (jsonResponse.statusCode == 404) {
						// var msgErrorClosePDSA = jsonResponse.errorMessage;
						$("#errorMessage1").modal("show");
					}
					// defensive check
					if (typeof callback == "function") {
						// apply() sets the meaning of "this" in the callback
						callback.apply(request);
					}
				}
			}
			request.send(fd);
			// allServices.closePDSA($scope.selectedPdsaModal.pdsaId,$scope.allfiles,$scope.selectedStatus.key,$scope.briefdescription).then(function(data){
			//				
			// $scope.msg = "Saved Successfully";
			// $("#pop").modal("show");
			//				
			// }, function(){
			//				
			// });

		}

	};


	
	$scope.order = function (sortType) {  
        $scope.sortReverse = ($scope.sortType === sortType) ? !$scope.sortReverse : false;  
        $scope.sortType = sortType;  
      };
      $scope.filterType = function(val){
      	if(isNaN(parseInt(val[$scope.sortType])))
      		if(!$scope.sortType)
      			return true;
      		else
      			return val[$scope.sortType];
      	else
      		return parseInt(val[$scope.sortType]);
      };
      $scope.filter = function(){
  		if($scope.selectedArea==undefined)
  			{
  			$scope.pdsaList = $scope.totalpdsaList ;
  			}
  		else if ($scope.selectedChangeIdea!=undefined)
  		{
  			$scope.pdsaList = $filter('filter')($scope.totalpdsaList, {changeIdeaId:$scope.selectedChangeIdea.key},true);
  		}
  		else if($scope.selectedIndicator!=undefined)
  			{
  			$scope.pdsaList = $filter('filter')($scope.totalpdsaList, {indicatorId:$scope.selectedIndicator.key},true);
  			}
  		
  		else if($scope.selectedArea!=undefined)
			{
			$scope.pdsaList = $filter('filter')($scope.totalpdsaList, {coreAreaId:parseInt($scope.selectedArea.split('_')[1])},true);
			}
  		$("#loader-mask").fadeOut();
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
			$("#loader-mask").fadeOut();
		};

		var result = {
			"fileName" : fileName
		};
		$.download("downloadPDSADoc/", result, 'POST');

	};

	$scope.getFileDetails = function(e) {

		$scope.files = [];
		$scope
				.$apply(function() {

					// STORE THE FILE OBJECT IN AN ARRAY.
					for (var i = 0; i < e.files.length; i++) {
						$scope.firstFileExt = e.files[i].name.split(".").pop();
						if ($scope.firstFileExt.toLowerCase() != "pdf"
								&& $scope.firstFileExt.toLowerCase() != "docx"
								&& $scope.firstFileExt.toLowerCase() != "doc"
								&& $scope.firstFileExt.toLowerCase() != "jpg"
								&& $scope.firstFileExt.toLowerCase() != "jpeg"
								&& $scope.firstFileExt.toLowerCase() != "png") {
							$scope.files = undefined
							$scope.firstFileName = "UPLOAD";
							$scope.errorMsg = "File must be of either document or pdf or image (jpg,jpeg,png) type";
							$("#errorMessage").modal("show");
						} else if (e.files[i].size > 500000) {
							$scope.files = undefined
							$scope.firstFileName = "UPLOAD";
							$scope.errorMsg = "File must be less than 500kb";
							$("#errorMessage").modal("show");
						} else {
							$scope.files.push(e.files[i])
							$scope.firstFileName = $scope.files[0].name;
						}
					}
					$scope.uploadBtn = $scope.files[0].name;

				});
	};

	$scope.getLastFileDetails = function(e) {

		$scope.lastfile = [];
		$scope
				.$apply(function() {

					// STORE THE FILE OBJECT IN AN ARRAY.
					for (var i = 0; i < e.files.length; i++) {
						$scope.lastFileExt = e.files[i].name.split(".").pop()
						if ($scope.lastFileExt.toLowerCase() != "pdf"
								&& $scope.lastFileExt.toLowerCase() != "docx"
								&& $scope.lastFileExt.toLowerCase() != "doc"
								&& $scope.lastFileExt.toLowerCase() != "jpg"
								&& $scope.lastFileExt.toLowerCase() != "jpeg"
								&& $scope.lastFileExt.toLowerCase() != "png") {
							$scope.lastfile = undefined
							$scope.lastDoc = undefined
							$scope.errorMsg = "File must be of either document or pdf or image (jpg,jpeg,png) type";
							$("#errorMessage").modal("show");
						} else if (e.files[i].size > 500000) {
							$scope.lastfile = undefined
							$scope.lastDoc = undefined
							$scope.errorMsg = "File must be less than 500kb";
							$("#errorMessage").modal("show");
						} else {
							$scope.lastfile.push(e.files[i])
							$scope.lastDoc = $scope.lastfile[0].name;
						}
					}

				});
	};
	$scope.getOtherFileDetails = function(e) {

		$scope.otherfile = [];
		$scope
				.$apply(function() {

					// STORE THE FILE OBJECT IN AN ARRAY.
					for (var i = 0; i < e.files.length; i++) {
						$scope.otherFileExt = e.files[i].name.split(".").pop()
						if ($scope.otherFileExt.toLowerCase() != "pdf"
								&& $scope.otherFileExt.toLowerCase() != "docx"
								&& $scope.otherFileExt.toLowerCase() != "doc"
								&& $scope.otherFileExt.toLowerCase() != "jpg"
								&& $scope.otherFileExt.toLowerCase() != "jpeg"
								&& $scope.otherFileExt.toLowerCase() != "png") {
							$scope.otherfile = undefined
							$scope.otherDoc = undefined
							$scope.errorMsg = "File must be of either document or pdf or image (jpg,jpeg,png) type";
							$("#errorMessage").modal("show");
						} else if (e.files[i].size > 500000) {
							$scope.otherfile = undefined
							$scope.otherDoc = undefined
							$scope.errorMsg = "File must be less than 500kb";
							$("#errorMessage").modal("show");
						} else {
							$scope.otherfile.push(e.files[i])
							$scope.otherDoc = $scope.otherfile[0].name;
						}
					}

				});
	};
	
	$scope.viewReadMore =function ($event,selectedPdsaModal){
		$('#viewChangeIdeaMore').modal('show');
		$scope.selectedPdsaModal = selectedPdsaModal;
		  $event.stopPropagation();
	}
}
$(document).ready(
		function() {
			$("#viewDataEntryModal .modal-dialog").css("height",
					$(window).height() - 75 - 44);
			
		});

// $(function(){
// $('#uploadBtn').click(function(){
// $('#modal-upload').click();
//        
// var data = new FormData();
//
// for (var i in $scope.files) {
// data.append("uploadedFile", $scope.files[i]);
// }
// });
// });
