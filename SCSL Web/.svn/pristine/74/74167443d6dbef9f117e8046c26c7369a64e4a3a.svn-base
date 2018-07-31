/*
 * @author Laxman Paikaray(laxman@sdrc.co.in) 
 */

function sncuDataEntryController($scope, $http, $timeout){
	$scope.activeMenu = "sncu-data-entry";
	$scope.pageName = "SNCU/NICU Data Entry";
	$("#loader-mask").show();
	$scope.pageLoaded = false;
	$scope.tableData = [""];
	$scope.profileDataSaved = false;
	$scope.profileFormDisabled = false;
	$scope.numberSelectedIndicators = {Prematurity: 0, Sepsis: 0, Asphyxia: 0};
	$scope.demo = {
			value: 50
	};
	if(hasLR){$scope.hasLR = "YES"}else{$scope.hasLR = "No"};
	$scope.putInputBox = function(model){
		$timeout(function(){
			$(".inputBox").html("<input type='number' class='tableInput' ng-model='demo.value'>");
		}, 1000);
	};
	
	$scope.getTimeperiod = $http.get("getTimePeriod").then(function(response){
		$scope.timeperiod = response.data;
		if($scope.timeperiod){
			$scope.getRestIndicators();
		}
		$scope.getFormTable($scope.timeperiod.timePeriodId, false, false);
	},function(error){
		
	});
	
	/*
	 * get json for all remained indicators
	*/
	$scope.getRestIndicators = function(){
		$http.get('getRestIndicators?timeperiodId=' + $scope.timeperiod.timePeriodId).then(function(response){
			$scope.restIndicators = response.data;
			$scope.indicatorCoreAreas = Object.keys($scope.restIndicators);
			$scope.selectIndicatorCoreArea($scope.indicatorCoreAreas[0]);
		},function(error){
			
		});
	};
	
	/*
	 * gets json model for all data in the profile and data entry form
	 * @param getTimePeriod - selected time period
	 * @param noAccessProfileData - this flag used to identify whether profile data to be updated or not according to received json
	 * @param showSuccessfulAddIndicator - specially when after adding indicator this function is called, it shows the message indicator added succesfull
	 * 
	*/
	$scope.getFormTable = function(getTimePeriod, noAccessProfileData, showSuccessfulAddIndicator){
		$http.get("getIndicators?timeperiodId=" + getTimePeriod).then(function(response){
			$("#loader-mask").fadeOut();
			if(showSuccessfulAddIndicator){
				$scope.msg = "Indicator(s) added successfully";
				$("#addIndicatorSuccess").modal("show");
			}
			$scope.pageLoaded = true;
			$scope.alreadySubmitted = response.data.statusMessage;
			$scope.superRemark = response.data.remark;
			$scope.allIndicatorsModel = response.data;
			$scope.formTableData = response.data.indTypeIndicatorModelMap["dataEntry"];
			$timeout(function(){
				$("[type='radio'] + label + .content").each(function(){$(this).height($(this).find(".table-responsive").height()+200)})
			},200)
			
			//convert all form table json data to string
			
			$scope.formKeys = Object.keys($scope.formTableData);
			for(var j=0; j<$scope.formKeys.length; j++){
				for(var i=0; i<$scope.formTableData[$scope.formKeys[j]].length; i++){
					if($scope.formTableData[$scope.formKeys[j]][i].numeratorValue != null){
						$scope.formTableData[$scope.formKeys[j]][i].numeratorValue = $scope.formTableData[$scope.formKeys[j]][i].numeratorValue.toString();
					}
					if($scope.formTableData[$scope.formKeys[j]][i].denominatorValue != null){
						$scope.formTableData[$scope.formKeys[j]][i].denominatorValue = $scope.formTableData[$scope.formKeys[j]][i].denominatorValue.toString();
					}
					if($scope.formTableData[$scope.formKeys[j]][i].percentage != null){
						$scope.formTableData[$scope.formKeys[j]][i].percentage = $scope.formTableData[$scope.formKeys[j]][i].percentage.toString();
					}
				}
			}
			if(!noAccessProfileData){
				$scope.profileTableData = response.data.indTypeIndicatorModelMap["profileEntry"];
				
				//convert all profile table json data to string
				$scope.profileKeys=Object.keys($scope.profileTableData);
				for(var i=0; i<$scope.profileKeys.length; i++){
					if($scope.profileTableData[$scope.profileKeys[i]][0].numeratorValue != null){
						$scope.profileTableData[$scope.profileKeys[i]][0].numeratorValue = $scope.profileTableData[$scope.profileKeys[i]][0].numeratorValue.toString();
					}
					if($scope.profileTableData[$scope.profileKeys[i]][0].denominatorValue != null){
						$scope.profileTableData[$scope.profileKeys[i]][0].denominatorValue = $scope.profileTableData[$scope.profileKeys[i]][0].denominatorValue.toString();
					}
					if($scope.profileTableData[$scope.profileKeys[i]][0].percentage != null){
						$scope.profileTableData[$scope.profileKeys[i]][0].percentage = $scope.profileTableData[$scope.profileKeys[i]][0].percentage.toString();
					}
				}
				$scope.savedProfileTableData = JSON.parse(JSON.stringify(response.data.indTypeIndicatorModelMap["profileEntry"]));
				$timeout(function(){
					if($(window).width() > 768)
					$("#profileTable").css("height", $("#profileTable div.table-responsive").height()+85);
					else
						$("#profileTable").css("height", $("#profileTable div.table-responsive").height()+185);
				}, 200);
			}
			else{
				$scope.resetData();
				if(!hasLR)
					$scope.profileEntryDataPopulation($scope.profileTableData["35"][0]);
				$scope.profileEntryDataPopulation($scope.profileTableData["34"][0]);
				$scope.profileEntryDataPopulation($scope.profileTableData["41"][0]);
			}
			$scope.profileTableRows = Object.keys($scope.profileTableData);
			
			
			if($scope.formTableData.Process.length)
				$scope.processColumns = Object.keys($scope.formTableData.Process[0]);
			if($scope.formTableData.Intermediate.length)
				$scope.intermediateColumns = Object.keys($scope.formTableData.Intermediate[0]);
			if($scope.formTableData.Outcome.length)
				$scope.outcomeColumns = Object.keys($scope.formTableData.Outcome[0]);
			$scope.enabled=response.data.enabled;
			
		}, function(){
			
		});
	};
	
	/*
	 * Remove extra columns added in the dynamic form table
	*/
	$scope.removeUnnecessaryColumns = function(item){
		 return item != 'coreAreaId' && item != 'isRequired' && item != 'exceptionRule';
		};
	
	
	$scope.selectIndicatorCoreArea = function(indicatorCoreArea){
		$scope.selectedIndicatorCoreArea = indicatorCoreArea;
	};
	
	/*
	 * this function locks profile while proceeding to form table 
	 * @param profileDataSaved - this flag used to disable proceed button inside profile entry
	 * @param profileFormDisabled - this flag used to disable all
	*/
	$scope.saveProfileForm = function(){
		$scope.profileDataSaved = true;
		$scope.profileFormDisabled = true;
		$(".profile-entry .accordion-trigger, .profile-entry .accordion-content").removeClass("expanded").addClass("collapsed").attr("aria-expanded", "false");
		$(".profile-entry .accordion-content").animate({"height": "0px"}, 500);
		$("body").animate({scrollTop: 0}, 500);
		$("#r1").click();
		for(var i=0; i<Object.keys($scope.profileTableData).length; i++){
			$scope.profileEntryDataPopulation($scope.profileTableData[Object.keys($scope.profileTableData)[i]][0]);
		}
	};
	
	/*
	 * 
	*/
	$scope.confirmReset = function(){
		$scope.warningMsg = "This will erase all data in both profile entry and data entry.";
		$("#confirmResetModal").modal("show");
			
	}
	
	/*
	 * This function checks whether the profile data populated from backend json is changed or not while proceeding to data entry section
	*/
	$scope.confirmSave = function(){
		var profileDataChanged = false;
		for(var i=0; i<$scope.profileKeys.length; i++){
			if($scope.savedProfileTableData[$scope.profileKeys[i]][0].percentage){
				if($scope.savedProfileTableData[$scope.profileKeys[i]][0].percentage != $scope.profileTableData[$scope.profileKeys[i]][0].percentage){
					profileDataChanged = true;
					break;
				}
			}
		}
		if(!profileDataChanged){
				$scope.warningMsg = "Are you sure you want to continue with these data?";
				$("#confirmSaveModal").modal("show");
			}
		else{
				$scope.warningMsg = "Are you sure you want to continue with these data? It will erase all prefetch data from Process, Intermediate and Outcome section.";
				$("#confirmModifyProfileModal").modal("show");
			}
	}
	
	/*
	 * It resets all data in both profile entry and data entry form.
	*/
	$scope.resetForm = function(){
		$scope.profileFormDisabled = false;
		$scope.profileDataSaved = false;
		$(".total-deliveries").removeClass("warned");
		$(".live-births").removeClass("warned");
		var keys = Object.keys($scope.profileTableData)
		for(var i=0; i<keys.length; i++){
			$scope.profileTableData[keys[i]][0].percentage = null;
			$scope.profileTableData[keys[i]][0].numeratorValue = null;
			$scope.profileTableData[keys[i]][0].denominatorValue = null;
		}
		var formats = $scope.formKeys;
		for(var i=0; i<formats.length; i++){
			for(var j=0; j<$scope.formTableData[formats[i]].length; j++){
					if($scope.formTableData[formats[i]][j].numeratorValue)
						$scope.formTableData[formats[i]][j].numeratorValue = null;
					if($scope.formTableData[formats[i]][j].denominatorValue)
						$scope.formTableData[formats[i]][j].denominatorValue = null;
					if($scope.formTableData[formats[i]][j].percentage && formats[i] == "Intermediate")
						$scope.formTableData[formats[i]][j].percentage = "N/A";
					else
						$scope.formTableData[formats[i]][j].percentage = null;
			}
		}
		$('html, body').animate({
			scrollTop : 0
		}, 500);
		$(".warned").removeClass("warned");
		$(".errorFound").removeClass("errorFound");
	};
	
	$scope.resetFormTable = function(){
		$scope.profileFormDisabled = false;
		$scope.profileDataSaved = false;
		$scope.resetData();
		
	};
	
	/*
	 * while submit sncu data when some fields in intermediate section left blank
	 * 'alldata' - alldata stores all the data both from profile entry and data entry form
	*/
	$scope.proceedtoSubmitWithBlankIntermediateForm = function(){
		$("#loader-mask").show();
		for(var i=0; i<$scope.formTableData.Intermediate.length; i++){
			if($scope.formTableData.Intermediate[i].percentage == "N/A"){
				$scope.formTableData.Intermediate[i].percentage = null;
			}
		}
		$scope.allData = $scope.formTableData;
//		$scope.allData.profileEntry = $scope.profileTableData;
		
		
		var keys = Object.keys($scope.profileTableData);
		for(var i=0; i<keys.length; i++){
						if($scope.profileTableData[keys[i]][0].numeratorValue)
							$scope.profileTableData[keys[i]][0].numeratorValue = parseInt($scope.profileTableData[keys[i]][0].numeratorValue);
						if($scope.profileTableData[keys[i]][0].denominatorValue)
							$scope.profileTableData[keys[i]][0].denominatorValue = parseInt($scope.profileTableData[keys[i]][0].denominatorValue);
						if($scope.profileTableData[keys[i]][0].percentage)
							$scope.profileTableData[keys[i]][0].percentage = parseFloat($scope.profileTableData[keys[i]][0].percentage);
						
						$scope.allData[keys[i]] = $scope.profileTableData[keys[i]];
		}
		
		
//		$scope.allData.push($scope.profileTableData);
		var formats = $scope.formKeys;
		for(var i=0; i<formats.length; i++){
			for(var j=0; j<$scope.allData[formats[i]].length; j++){
					delete $scope.allData[formats[i]][j].profileBaseDenominatorDisable;
					delete $scope.allData[formats[i]][j].profileBasenumeratorDisable;
					if($scope.allData[formats[i]][j].numeratorValue)
						$scope.allData[formats[i]][j].numeratorValue = parseInt($scope.allData[formats[i]][j].numeratorValue);
					if($scope.allData[formats[i]][j].denominatorValue)
						$scope.allData[formats[i]][j].denominatorValue = parseInt($scope.allData[formats[i]][j].denominatorValue);
					if($scope.allData[formats[i]][j].percentage)
						$scope.allData[formats[i]][j].percentage = parseFloat($scope.allData[formats[i]][j].percentage);
					
			}
				
	
		}
//		var keys = Object.keys($scope.allData["profileEntry"]);
//		for(var i=0; i<keys.length; i++){
//						if($scope.allData["profileEntry"][keys[i]][0].numeratorValue)
//							$scope.allData["profileEntry"][keys[i]][0].numeratorValue = parseInt($scope.allData["profileEntry"][keys[i]][0].numeratorValue);
//						if($scope.allData["profileEntry"][keys[i]][0].denominatorValue)
//							$scope.allData["profileEntry"][keys[i]][0].denominatorValue = parseInt($scope.allData["profileEntry"][keys[i]][0].denominatorValue);
//						if($scope.allData["profileEntry"][keys[i]][0].percentage)
//							$scope.allData["profileEntry"][keys[i]][0].percentage = parseInt($scope.allData["profileEntry"][keys[i]][0].percentage);
//		}	
		$http.post('saveSNCUIndicator?timeperiodId=' + $scope.timeperiod.timePeriodId, $scope.allData)
		
		.then(function(result){
			console.log($scope.allData);
			$("#loader-mask").fadeOut()
			if(result.data.statusCode==200){
				$scope.msg = "Data successfully submitted for SNCU/NICU";
				$("#pop").modal("show");
			}else{
				$scope.errorMsg = result.data.errorMessage;
				$("#errorMessage").modal("show");
			}
			$scope.link = "sncuDataEntry";
		}, function(error){
			console.log(error);
		});
	};
	
	/*
	 * validates form table data
	 * if all fields are filled then it sends data neither shows warning modal
	*/
	$scope.submitForm = function(){
		
		var formats = $scope.formKeys;
		for(var x=0; x<formats.length; x++){
			for(var i=0; i<$scope.formTableData[formats[x]].length; i++){
				if($scope.formTableData[formats[x]][i].isRequired == true){
					if($scope.formTableData[formats[x]][i].numeratorValue == "" || $scope.formTableData[formats[x]][i].numeratorValue == null){
						$scope.errorMsg = "Numerator value can not be blank for " + $scope.formTableData[formats[x]][i].indicatorName + " in " + formats[x] + " indicators";
						$("#errorMessage").modal("show");
						return false;
					}
					if($scope.formTableData[formats[x]][i].denominatorValue == "" || $scope.formTableData[formats[x]][i].denominatorValue == null){
						$scope.errorMsg = "Denominator value can not be blank for " + $scope.formTableData[formats[x]][i].indicatorName + " in " + formats[x] + " indicators";
						$("#errorMessage").modal("show");
						return false;
					}
				}
			}
		}
//		if(hasLR){
			for(var x=0; x<formats.length; x++){
				for(var i=0; i<$scope.formTableData[formats[x]].length; i++){
					if($scope.formTableData[formats[x]][i].isRequired == null){
						if($scope.formTableData[formats[x]][i].isLr && ($scope.formTableData[formats[x]][i].numeratorValue == "" || $scope.formTableData[formats[x]][i].numeratorValue == null)){
							$scope.warningMsg = "Some fields are left blank in Intermediate section";
							$("#warningMessageSncuSubmit").modal("show");
							return false;
						}
						if($scope.formTableData[formats[x]][i].isLr && ($scope.formTableData[formats[x]][i].denominatorValue == "" || $scope.formTableData[formats[x]][i].denominatorValue == null)){
							$scope.warningMsg = "Some fields are left blank in Intermediate section";
							$("#warningMessageSncuSubmit").modal("show");
							return false;
						}
					}
				}
			}
//		}
		$("#loader-mask").show();
		$scope.allData = $scope.formTableData;
//		$scope.allData.profileEntry = $scope.profileTableData;
		
		var keys = Object.keys($scope.profileTableData);
		for(var i=0; i<keys.length; i++){
						if($scope.profileTableData[keys[i]][0].numeratorValue)
							$scope.profileTableData[keys[i]][0].numeratorValue = parseInt($scope.profileTableData[keys[i]][0].numeratorValue);
						if($scope.profileTableData[keys[i]][0].denominatorValue)
							$scope.profileTableData[keys[i]][0].denominatorValue = parseInt($scope.profileTableData[keys[i]][0].denominatorValue);
						if($scope.profileTableData[keys[i]][0].percentage)
							$scope.profileTableData[keys[i]][0].percentage = parseFloat($scope.profileTableData[keys[i]][0].percentage);
						
						$scope.allData[keys[i]] = $scope.profileTableData[keys[i]];
		}
		
//		$scope.allData.push($scope.profileTableData);
		var formats = $scope.formKeys;
		for(var i=0; i<formats.length; i++){
			for(var j=0; j<$scope.allData[formats[i]].length; j++){
					delete $scope.allData[formats[i]][j].profileBaseDenominatorDisable;
					delete $scope.allData[formats[i]][j].profileBasenumeratorDisable;
					if($scope.allData[formats[i]][j].numeratorValue)
						$scope.allData[formats[i]][j].numeratorValue = parseInt($scope.allData[formats[i]][j].numeratorValue);
					if($scope.allData[formats[i]][j].denominatorValue)
						$scope.allData[formats[i]][j].denominatorValue = parseInt($scope.allData[formats[i]][j].denominatorValue);
					if($scope.allData[formats[i]][j].percentage )
						$scope.allData[formats[i]][j].percentage = parseFloat($scope.allData[formats[i]][j].percentage);
					
			}
				
	
		}
//		var keys = Object.keys($scope.allData["profileEntry"]);
//		for(var i=0; i<keys.length; i++){
//						if($scope.allData["profileEntry"][keys[i]][0].numeratorValue)
//							$scope.allData["profileEntry"][keys[i]][0].numeratorValue = parseInt($scope.allData["profileEntry"][keys[i]][0].numeratorValue);
//						if($scope.allData["profileEntry"][keys[i]][0].denominatorValue)
//							$scope.allData["profileEntry"][keys[i]][0].denominatorValue = parseInt($scope.allData["profileEntry"][keys[i]][0].denominatorValue);
//						if($scope.allData["profileEntry"][keys[i]][0].percentage)
//							$scope.allData["profileEntry"][keys[i]][0].percentage = parseInt($scope.allData["profileEntry"][keys[i]][0].percentage);
//		}		
	
		$http.post('saveSNCUIndicator?timeperiodId=' + $scope.timeperiod.timePeriodId, $scope.allData)
		.then(function(result){
			console.log($scope.allData);
			$("#loader-mask").fadeOut()
			if(result.data.statusCode==200){
				$scope.msg = "Data successfully submitted for SNCU/NICU";
				$("#pop").modal("show");
			}else{
				$scope.errorMsg = result.data.errorMessage;
				$("#errorMessage").modal("show");
			}
			$scope.link = "sncuDataEntry";
			
		}, function(error){
			console.log(error);
		});
	};
	
	
	/*
	 * opens add indicator modal
	*/
	$scope.openAddIndicatorModal = function(){
		if($scope.indicatorCoreAreas.length){
			$scope.numberSelectedIndicators = {Prematurity: 0, Sepsis: 0, Asphyxia: 0};
			for(var i=0; i<$scope.indicatorCoreAreas.length; i++){
				for(var j=0; j<$scope.restIndicators[$scope.indicatorCoreAreas[i]].length; j++){
					if($scope.restIndicators[$scope.indicatorCoreAreas[i]][j].isSelected){
						$scope.restIndicators[$scope.indicatorCoreAreas[i]][j].isSelected = false;
					}
				}
			}
			$("#addIndicatorModal").modal("show");
		}
		else{
			$scope.infoMsg = "There are no more indicators to add";
			$("#infoMessage").modal("show");
		}
	};
	
	/*
	 * 
	*/
	$scope.confirmAddingIndicator = function(){
		$scope.allSelectedIndicators = [];
		
		for(var i=0; i<$scope.indicatorCoreAreas.length; i++){
			for(var j=0; j<$scope.restIndicators[$scope.indicatorCoreAreas[i]].length; j++){
				if($scope.restIndicators[$scope.indicatorCoreAreas[i]][j].isSelected){
					$scope.allSelectedIndicators.push($scope.restIndicators[$scope.indicatorCoreAreas[i]][j]);
				}
			}
		}
		if($scope.allSelectedIndicators.length){
			$("#addIndicatorModal").modal("hide");
			$("#confirmAddIndicator").modal("show");
		}
		else{
			$scope.warningMsg = "No indicator selected";
			$("#warningMessage").modal("show");
		}
	};
	
	/*
	 * counts number of selected indicators selected to add
	*/
	$scope.countSelectedIndicators = function(checked, coreArea){
		if(checked)
			$scope.numberSelectedIndicators[coreArea] += 1;
		else
			$scope.numberSelectedIndicators[coreArea] -= 1;
	};
	
	/*
	 * sends selected rest indicator list to add them to current indicator list 
	*/
	$scope.addIndicator = function(){
		$("#loader-mask").show()
		$("#confirmAddIndicator").modal("hide");
		$http.post('updateIndicators?timeperiodId=' + $scope.timeperiod.timePeriodId, $scope.allSelectedIndicators)
		.then(function(result){
			$scope.getFormTable($scope.timeperiod.timePeriodId, true, true);
			$timeout(function(){
				$("[type='radio'] + label + .content").each(function(){$(this).height($(this).find(".table-responsive").height()+200)})
			},200)
			$scope.getRestIndicators();
		}, function(error){
			alert("some error occured, please try again later");
		});
	};
	
	/*
	 * reset all data inside process, intermediate, outcome section
	*/
	$scope.resetData = function(){
		var formats = $scope.formKeys;
		for(var i=0; i<formats.length; i++){
			for(var j=0; j<$scope.formTableData[formats[i]].length; j++){
					if($scope.formTableData[formats[i]][j].numeratorValue)
						$scope.formTableData[formats[i]][j].numeratorValue = null;
					if($scope.formTableData[formats[i]][j].denominatorValue)
						$scope.formTableData[formats[i]][j].denominatorValue = null;
					if($scope.formTableData[formats[i]][j].percentage && formats[i] == "Intermediate")
						$scope.formTableData[formats[i]][j].percentage = "N/A";
					else
						$scope.formTableData[formats[i]][j].percentage = null;
			}
		}
	}
	
	
	
	/*****validations****/
	$scope.validateInput = function(model, key, format, id){
		for(var i=0; i<model.exceptionRule.length; i++){
			if(model.exceptionRule[i].split('#')[0] == "Numerator should not be greater than denominator" ){
				if(typeof $scope.isNumeratorLessThanDenominator(model, key, model.exceptionRule[i].split('#')[1], format, id) != 'undefined')
					return $scope.isNumeratorLessThanDenominator(model, key, model.exceptionRule[i].split('#')[1], format, id);
				else{
					$("#" + id).removeClass("errorFound");
					$("#" + id).removeClass("warned");
					$("#" + id).siblings(".warned").removeClass("warned");
				}
			}
			if(model.exceptionRule[i].split('#')[1] == "denominator cannot greater than denominator"){
				if(key == "denominatorValue"){
					if(typeof $scope.isGreaterThanDependency(model, key,  model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[2], format, id) != 'undefined')
						return $scope.isGreaterThanDependency(model, key,  model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[2], format, id);
					else{
						$("#" + id).removeClass("errorFound");
						$("#" + id).removeClass("warned");
						$("#" + id).siblings(".warned").removeClass("warned");
					}
				}
			}
			if(model.exceptionRule[i].split('#')[1] == "denominator equal"){
				if(key == "denominatorValue"){
					if(typeof $scope.isEqualToDependency(model, key,  model.exceptionRule[i].split('#')[0].split(","), model.exceptionRule[i].split('#')[2], format, id) != 'undefined')
						return $scope.isEqualToDependency(model, key,  model.exceptionRule[i].split('#')[0].split(","), model.exceptionRule[i].split('#')[2], format, id);
					else{
						$("#" + id).removeClass("errorFound");
						$("#" + id).removeClass("warned");
						$("#" + id).siblings(".warned").removeClass("warned");
					}	
				}
				
			}
			if(model.exceptionRule[i].split('#')[1] == "denominator cannot lesser than denominator"){
				if(key == "denominatorValue"){
					if(typeof $scope.isLesserThanDependency(model, key,  model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[2], format, id) != 'undefined')
						return $scope.isLesserThanDependency(model, key,  model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[2], format, id);
					else{
						$("#" + id).removeClass("errorFound");
						$("#" + id).removeClass("warned");
						$("#" + id).siblings(".warned").removeClass("warned");
					}
				}
			}
			if(model.exceptionRule[i].split('#')[1] == "numerator equal with other denominator" && key == "denominatorValue"){
				if(typeof $scope.isNumeratorEqualWithOtherDependencies(model, key, model.exceptionRule[i].split('#')[0].split("-")[0].split(","), model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[2], format, id) != 'undefined')
					return $scope.isNumeratorEqualWithOtherDependencies(model, key, model.exceptionRule[i].split('#')[0].split("-")[0].split(","), model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[2], format, id);
				else{
					$("#" + id).removeClass("errorFound");
					$("#" + id).removeClass("warned");
					$("#" + id).siblings(".warned").removeClass("warned");
				}
			}
			if(model.exceptionRule[i].split('#')[1] == "denominator equal with other numerator" && key == "numeratorValue"){
				if(typeof $scope.isNumeratorEqualWithOtherDependencies(model, key, model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[0].split("-")[0].split(","), model.exceptionRule[i].split('#')[2], format, id) != 'undefined')
					return $scope.isNumeratorEqualWithOtherDependencies(model, key, model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[0].split("-")[0].split(","), model.exceptionRule[i].split('#')[2], format, id);
				else{
					$("#" + id).removeClass("errorFound");
					$("#" + id).removeClass("warned");
					$("#" + id).siblings(".warned").removeClass("warned");
				}
			}
			
			if(model.exceptionRule[i].split('#')[1] == "numerator cannot greater than denominator"){
				if(key == "numeratorValue"){
					if(typeof $scope.isGreaterThanDependencyDenominator(model, key,  model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[2], format, id) != 'undefined')
						return $scope.isGreaterThanDependencyDenominator(model, key,  model.exceptionRule[i].split('#')[0].split("-")[1].split(","), model.exceptionRule[i].split('#')[2], format, id);
					else{
						$("#" + id).removeClass("errorFound");
						$("#" + id).removeClass("warned");
						$("#" + id).siblings(".warned").removeClass("warned");
					}
				}
			}
//			$scope.checkIsRequired(model, key);
		}
	};
	$scope.isGreaterThanDependencyDenominator = function(model, key, compareIds, option, format, id){
		var formats = $scope.formKeys;
		if(!(model[key] == "" || model[key] == null))
		for(var x=0; x<formats.length; x++){
			for(var i=0; i<compareIds.length; i++){
				for(var j=0; j<$scope.formTableData[formats[x]].length; j++){
					if(compareIds[i] == $scope.formTableData[formats[x]][j].indicatorId){
						if(!($scope.formTableData[formats[x]][j].denominatorValue == null || $scope.formTableData[formats[x]][j].denominatorValue == "") && parseInt(model[key]) > parseInt($scope.formTableData[formats[x]][j].denominatorValue) ){
							
							if(option == "Mandatory"){
								$scope.errorMsg = "'"+model.numeratorName + "' can not be greater than '" + $scope.formTableData[formats[x]][j].denominatorName+"' ("+$scope.formTableData[formats[x]][j].denominatorValue+")";
								$("#errorMessage").modal("show");
								model[key] = "";
								$("#" + id).addClass("errorFound");
								$("#" + id).removeClass("warned");
							}
							else{
								$scope.warningMsg = "'"+model.numeratorName + "' is greater than '" + $scope.formTableData[formats[x]][j].denominatorName+"' ("+$scope.formTableData[formats[x]][j].denominatorValue+")";;
								$("#warningMessage").modal("show");
								$("#" + id).addClass("warned");
							}
							
							$scope.calculatePercentage(model, format);
							return false;
						}
					}
				}
			}
		}
	};
	$scope.profileEntryDataPopulation = function(model){
		if(model.exceptionRule){
			for(var i=0; i<model.exceptionRule.length; i++){
				if(model.exceptionRule[i].split('#')[1] == "denominator equal with profile entry"){
					$scope.profileEntryToForm(model, "denominatorValue", model.exceptionRule[i])
				}
			}
			
		}
		var otherDependentIndicators = [];
		if(model.indicatorId == 34 || model.indicatorId == 35){
			if(hasLR)
				otherDependentIndicators = ["36", "37", "44"];
			else
				otherDependentIndicators = ["36", "44"];
			for(var i=0; i<otherDependentIndicators.length; i++){
				if($scope.profileTableData[otherDependentIndicators[i]][0].exceptionRule){
					for(var j=0; j<$scope.profileTableData[otherDependentIndicators[i]][0].exceptionRule.length; j++){
						if($scope.profileTableData[otherDependentIndicators[i]][0].exceptionRule[j].split('#')[1] == "denominator equal with profile entry"){
							$scope.profileEntryToForm($scope.profileTableData[otherDependentIndicators[i]][0], "denominatorValue", $scope.profileTableData[otherDependentIndicators[i]][0].exceptionRule[j])
						}
					}
				}
			}
		}
		if(model.indicatorId == 38 || model.indicatorId == 39){
			otherDependentIndicators = ["40", "41", "42"];
			for(var i=0; i<otherDependentIndicators.length; i++){
				if($scope.profileTableData[otherDependentIndicators[i]][0].exceptionRule){
					for(var j=0; j<$scope.profileTableData[otherDependentIndicators[i]][0].exceptionRule.length; j++){
						if($scope.profileTableData[otherDependentIndicators[i]][0].exceptionRule[j].split('#')[1] == "denominator equal with profile entry"){
							$scope.profileEntryToForm($scope.profileTableData[otherDependentIndicators[i]][0], "denominatorValue", $scope.profileTableData[otherDependentIndicators[i]][0].exceptionRule[j])
						}
					}
				}
			}
		}
	};
	$scope.profileEntryToForm = function(model, inputKey, exceptionRule){
		
				var formats = $scope.formKeys;
				for(var i=0; i<formats.length; i++){
					for(var j=0; j<$scope.formTableData[formats[i]].length; j++){
							for(var k=0; k<exceptionRule.split('#')[0].split("-")[1].split(",").length; k++){
								if($scope.formTableData[formats[i]][j].indicatorId == exceptionRule.split('#')[0].split("-")[1].split(",")[k]){
									$scope.formTableData[formats[i]][j][inputKey] = model.percentage.toString();
									/*if(i==0){
										if(inputKey == "denominatorValue"){
											$("#denominator"+j).find("input").
										}
											
									}*/
									if(inputKey == "denominatorValue")
										$scope.formTableData[formats[i]][j].profileBaseDenominatorDisable = true;
									if(inputKey == "numeratorValue")
										$scope.formTableData[formats[i]][j].profileBasenumeratorDisable = true;
								}
							}
						
					}
			
		}
	}
	$scope.calculatePercentage = function(rowData, section){
		if(rowData.numeratorValue && rowData.denominatorValue)
			//for new indicator Incidence of sepsis in neonates in the NICU/SNCU (%)
			//formula is (N/D) * 1000
			if(rowData.indicatorId == 17 && rowData.denominatorValue != 0){
				rowData.percentage = Math.round( rowData.numeratorValue / rowData.denominatorValue * 1000 * 10 ) / 10;
			}
			else if(rowData.denominatorValue != 0)
				rowData.percentage = Math.round( rowData.numeratorValue / rowData.denominatorValue * 100 * 10 ) / 10;
			else
				rowData.percentage = "NaN";
		else if(section == "Intermediate"){
			rowData.percentage = "N/A";
		}
		else
			rowData.percentage = "";
	};	
	$scope.isNOLBgreaterTD = function(indicator){
		if(indicator.indicatorId == 43 || indicator.indicatorId == 38 || indicator.indicatorId == 39){
			if(parseInt($scope.profileTableData["44"][0].percentage) > parseInt($scope.profileTableData["41"][0].percentage)){
				if(indicator.indicatorId == 43){
					$scope.warningMsg = "Number of live births should not be greater than number of total deliveries";
					$("#warningMessage").modal("show");
					$(".live-births").addClass("warned");
					$(".total-deliveries").addClass("warned");
				}
				if(indicator.indicatorId == 38 || indicator.indicatorId == 39){
					$scope.warningMsg = "Number of total deliveries can not be less than number of live births";
					$("#warningMessage").modal("show");
					$(".total-deliveries").addClass("warned");
					$(".live-births").addClass("warned");
				}
			}
			else{
				$(".total-deliveries").removeClass("warned");
				$(".live-births").removeClass("warned");
			}
		}
	};
	$scope.calculateTotalAndPercentage = function(indicator){
		if(indicator.indicatorId == 34 || indicator.indicatorId == 35){
			if($scope.profileTableData["34"] && $scope.profileTableData["34"][0].percentage && $scope.profileTableData["35"][0].percentage){
				$scope.profileTableData["34"][0].numeratorValue =  parseInt($scope.profileTableData["34"][0].percentage);
				$scope.profileTableData["35"][0].numeratorValue =  parseInt($scope.profileTableData["35"][0].percentage);
				$scope.profileTableData["36"][0].percentage = parseInt($scope.profileTableData["34"][0].percentage) + parseInt($scope.profileTableData["35"][0].percentage);
				$scope.profileTableData["37"][0].percentage = Math.round($scope.profileTableData["34"][0].percentage/$scope.profileTableData["36"][0].percentage*100*10)/10;
				if(isNaN($scope.profileTableData["37"][0].percentage)){
					$scope.profileTableData["37"][0].percentage = "NaN";
				}
				$scope.profileTableData["37"][0].numeratorValue = $scope.profileTableData["34"][0].percentage;
				$scope.profileTableData["37"][0].denominatorValue = $scope.profileTableData["36"][0].percentage;
				$scope.profileTableData["38"][0].percentage = Math.round($scope.profileTableData["35"][0].percentage/$scope.profileTableData["36"][0].percentage*100*10)/10;
				if(isNaN($scope.profileTableData["38"][0].percentage)){
					$scope.profileTableData["38"][0].percentage = "NaN";
				}
				$scope.profileTableData["38"][0].numeratorValue = $scope.profileTableData["35"][0].percentage;
				$scope.profileTableData["38"][0].denominatorValue = $scope.profileTableData["36"][0].percentage;
				$scope.profileTableData["36"][0].numeratorValue = $scope.profileTableData["36"][0].percentage ;
			}
			else if($scope.profileTableData["34"] && $scope.profileTableData["34"][0].percentage && !$scope.profileTableData["35"][0].percentage){
				$scope.profileTableData["34"][0].numeratorValue =  parseInt($scope.profileTableData["34"][0].percentage);
				$scope.profileTableData["35"][0].numeratorValue =  parseInt($scope.profileTableData["35"][0].percentage);
				$scope.profileTableData["36"][0].percentage = parseInt($scope.profileTableData["34"][0].percentage) + 0;
				$scope.profileTableData["37"][0].percentage = Math.round($scope.profileTableData["34"][0].percentage/$scope.profileTableData["36"][0].percentage*100*10)/10;
				if(isNaN($scope.profileTableData["37"][0].percentage)){
					$scope.profileTableData["37"][0].percentage = "NaN";
				}
				$scope.profileTableData["37"][0].numeratorValue = $scope.profileTableData["34"][0].percentage;
				$scope.profileTableData["37"][0].denominatorValue = $scope.profileTableData["36"][0].percentage;
				$scope.profileTableData["38"][0].percentage = "";
				$scope.profileTableData["38"][0].numeratorValue = "";
				$scope.profileTableData["38"][0].denominatorValue = $scope.profileTableData["36"][0].percentage;
				$scope.profileTableData["36"][0].numeratorValue = $scope.profileTableData["36"][0].percentage ;
			}else if (!$scope.profileTableData["34"] && $scope.profileTableData["35"][0].percentage){
				$scope.profileTableData["35"][0].numeratorValue =  parseInt($scope.profileTableData["35"][0].percentage);
				$scope.profileTableData["36"][0].percentage = 0 + parseInt($scope.profileTableData["35"][0].percentage);
				$scope.profileTableData["38"][0].percentage = Math.round($scope.profileTableData["35"][0].percentage/$scope.profileTableData["36"][0].percentage*100*10)/10;
				if(isNaN($scope.profileTableData["38"][0].percentage)){
					$scope.profileTableData["38"][0].percentage = "NaN";
				}
				$scope.profileTableData["38"][0].numeratorValue = $scope.profileTableData["35"][0].percentage;
				$scope.profileTableData["38"][0].denominatorValue = $scope.profileTableData["36"][0].percentage;
				$scope.profileTableData["36"][0].numeratorValue = $scope.profileTableData["36"][0].percentage ;
			}
			else if($scope.profileTableData["34"] && !$scope.profileTableData["34"][0].percentage && $scope.profileTableData["35"][0].percentage){
				$scope.profileTableData["34"][0].numeratorValue =  parseInt($scope.profileTableData["34"][0].percentage);
				$scope.profileTableData["35"][0].numeratorValue =  parseInt($scope.profileTableData["35"][0].percentage);
				$scope.profileTableData["36"][0].percentage = 0 + parseInt($scope.profileTableData["35"][0].percentage);
				$scope.profileTableData["37"][0].percentage = "";
				$scope.profileTableData["37"][0].numeratorValue = $scope.profileTableData["34"][0].percentage;
				$scope.profileTableData["37"][0].denominatorValue = $scope.profileTableData["36"][0].percentage;
				$scope.profileTableData["38"][0].percentage = Math.round($scope.profileTableData["35"][0].percentage/$scope.profileTableData["36"][0].percentage*100*10)/10;
				if(isNaN($scope.profileTableData["38"][0].percentage)){
					$scope.profileTableData["38"][0].percentage = "NaN";
				}
				$scope.profileTableData["38"][0].numeratorValue = $scope.profileTableData["35"][0].percentage;
				$scope.profileTableData["38"][0].denominatorValue = $scope.profileTableData["36"][0].percentage;
				$scope.profileTableData["36"][0].numeratorValue = $scope.profileTableData["36"][0].percentage ;
			}
			else{
				$scope.profileTableData["36"][0].percentage = null;
				$scope.profileTableData["36"][0].numeratorValue = null;
				if($scope.profileTableData["37"]){
					$scope.profileTableData["37"][0].percentage = null;
					$scope.profileTableData["37"][0].numeratorValue = null;
					$scope.profileTableData["37"][0].denominatorValue = null;
				}
				$scope.profileTableData["38"][0].percentage = null;
				$scope.profileTableData["38"][0].numeratorValue = null;
				$scope.profileTableData["38"][0].denominatorValue = null;
			}
		}
		if(indicator.indicatorId == 38 || indicator.indicatorId == 39){
			if($scope.profileTableData["39"][0].percentage && $scope.profileTableData["40"][0].percentage){
				$scope.profileTableData["39"][0].numeratorValue =  parseInt($scope.profileTableData["39"][0].percentage);
				$scope.profileTableData["40"][0].numeratorValue =  parseInt($scope.profileTableData["40"][0].percentage);
				$scope.profileTableData["41"][0].percentage = parseInt($scope.profileTableData["39"][0].percentage) + parseInt($scope.profileTableData["40"][0].percentage);
				$scope.profileTableData["41"][0].numeratorValue =$scope.profileTableData["41"][0].percentage ;
				$scope.profileTableData["42"][0].percentage = Math.round($scope.profileTableData["39"][0].percentage/$scope.profileTableData["41"][0].percentage*100*10)/10;
				if(isNaN($scope.profileTableData["42"][0].percentage)){
					$scope.profileTableData["42"][0].percentage = "NaN";
				}
				$scope.profileTableData["42"][0].numeratorValue = $scope.profileTableData["39"][0].percentage;
				$scope.profileTableData["42"][0].denominatorValue = $scope.profileTableData["41"][0].percentage;
				$scope.profileTableData["43"][0].percentage = Math.round($scope.profileTableData["40"][0].percentage/$scope.profileTableData["41"][0].percentage*100*10)/10;
				if(isNaN($scope.profileTableData["43"][0].percentage)){
					$scope.profileTableData["43"][0].percentage = "NaN";
				}
				$scope.profileTableData["43"][0].numeratorValue = $scope.profileTableData["40"][0].percentage;
				$scope.profileTableData["43"][0].denominatorValue = $scope.profileTableData["41"][0].percentage;
			}
			else if($scope.profileTableData["39"][0].percentage && !$scope.profileTableData["40"][0].percentage){
				$scope.profileTableData["39"][0].numeratorValue =  parseInt($scope.profileTableData["39"][0].percentage);
				$scope.profileTableData["40"][0].numeratorValue =  parseInt($scope.profileTableData["40"][0].percentage);
				$scope.profileTableData["41"][0].percentage = parseInt($scope.profileTableData["39"][0].percentage) + 0;
				$scope.profileTableData["41"][0].numeratorValue =$scope.profileTableData["41"][0].percentage ;
				$scope.profileTableData["42"][0].percentage = Math.round($scope.profileTableData["39"][0].percentage/$scope.profileTableData["41"][0].percentage*100*10)/10;
				if(isNaN($scope.profileTableData["42"][0].percentage)){
					$scope.profileTableData["42"][0].percentage = "NaN";
				}
				$scope.profileTableData["42"][0].numeratorValue = $scope.profileTableData["39"][0].percentage;
				$scope.profileTableData["42"][0].denominatorValue = $scope.profileTableData["41"][0].percentage;
				$scope.profileTableData["43"][0].percentage = "";
				$scope.profileTableData["43"][0].numeratorValue = $scope.profileTableData["40"][0].percentage;
				$scope.profileTableData["43"][0].denominatorValue = $scope.profileTableData["41"][0].percentage;
			}
			else if(!$scope.profileTableData["39"][0].percentage && $scope.profileTableData["40"][0].percentage){
				$scope.profileTableData["39"][0].numeratorValue =  parseInt($scope.profileTableData["39"][0].percentage);
				$scope.profileTableData["40"][0].numeratorValue =  parseInt($scope.profileTableData["40"][0].percentage);
				$scope.profileTableData["41"][0].percentage = 0 + parseInt($scope.profileTableData["40"][0].percentage);
				$scope.profileTableData["41"][0].numeratorValue =$scope.profileTableData["41"][0].percentage ;
				$scope.profileTableData["42"][0].percentage = "";
				$scope.profileTableData["42"][0].numeratorValue = $scope.profileTableData["39"][0].percentage;
				$scope.profileTableData["42"][0].denominatorValue = $scope.profileTableData["41"][0].percentage;
				$scope.profileTableData["43"][0].percentage = Math.round($scope.profileTableData["40"][0].percentage/$scope.profileTableData["41"][0].percentage*100*10)/10;
				if(isNaN($scope.profileTableData["43"][0].percentage)){
					$scope.profileTableData["43"][0].percentage = "NaN";
				}
				$scope.profileTableData["43"][0].numeratorValue = $scope.profileTableData["40"][0].percentage;
				$scope.profileTableData["43"][0].denominatorValue = $scope.profileTableData["41"][0].percentage;
			}
			else{
				$scope.profileTableData["41"][0].percentage = null;
				$scope.profileTableData["42"][0].percentage = null;
				$scope.profileTableData["43"][0].percentage = null;
				$scope.profileTableData["42"][0].numeratorValue = null;
				$scope.profileTableData["42"][0].denominatorValue = null;
				$scope.profileTableData["43"][0].numeratorValue = null;
				$scope.profileTableData["43"][0].denominatorValue = null;
			}
		}if(indicator.indicatorId == 43){
			if($scope.profileTableData["44"][0].percentage){
				$scope.profileTableData["44"][0].numeratorValue =  parseInt($scope.profileTableData["44"][0].percentage);
			}
		}
	}
	$scope.isNumeratorEqualWithOtherDependencies = function(model, key, compareDenominators, compareNumerators, option, format, id){
		var formats = $scope.formKeys;
		if(!(model[key] == "" || model[key] == null))
		for(var x=0; x<formats.length; x++){
			for(var i=0; i<compareNumerators.length; i++){
				for(var j=0; j<$scope.formTableData[formats[x]].length; j++){
					if(compareNumerators[i] == $scope.formTableData[formats[x]][j].indicatorId){
						if(!($scope.formTableData[formats[x]][j].numeratorValue == null || $scope.formTableData[formats[x]][j].numeratorValue == "") && parseInt(model[key]) != parseInt($scope.formTableData[formats[x]][j].numeratorValue)){
							
							if(option == "Mandatory"){
//								if(key == "numeratorValue")
									$scope.errorMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].numeratorName.trim() + "' (" + $scope.formTableData[formats[x]][j].numeratorValue + ")";
//								else
//									$scope.errorMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].numeratorName.trim() + "' (" + $scope.formTableData[formats[x]][j].numeratorValue + ")";
								$("#errorMessage").modal("show");
								model[key] = "";
								$("#" + id).addClass("errorFound");
								$("#" + id).removeClass("warned");
							}
							else{
//								if(key == "numeratorValue")
									$scope.warningMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].numeratorName.trim() + "' (" + $scope.formTableData[formats[x]][j].numeratorValue + ")";
//								else
//									$scope.warningMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].numeratorName.trim() + "' (" + $scope.formTableData[formats[x]][j].numeratorValue + ")";
								$("#warningMessage").modal("show");
								$("#" + id).addClass("warned");
							}
							
							$scope.calculatePercentage(model, format);
							return false;
						}
					}
				}
			}
			for(var i=0; i<compareDenominators.length; i++){
				for(var j=0; j<$scope.formTableData[formats[x]].length; j++){
					if(compareDenominators[i] == $scope.formTableData[formats[x]][j].indicatorId){
						if(!($scope.formTableData[formats[x]][j].denominatorValue == null || $scope.formTableData[formats[x]][j].denominatorValue == "") && parseInt(model[key]) != parseInt($scope.formTableData[formats[x]][j].denominatorValue)){
							
							if(option == "Mandatory"){
//								if(key == "numeratorValue")
									$scope.errorMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].denominatorName.trim() + "' (" + $scope.formTableData[formats[x]][j].denominatorValue + ")";
//								else
//									$scope.errorMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].numeratorName.trim() + "' (" + $scope.formTableData[formats[x]][j].denominatorValue + ")";
								$("#errorMessage").modal("show");
								model[key] = "";
								$("#" + id).addClass("errorFound");
								$("#" + id).removeClass("warned");
							}
							else{
//								if(key == "numeratorValue")
									$scope.warningMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].denominatorName.trim() + "' (" + $scope.formTableData[formats[x]][j].denominatorValue + ")";
//								else
//									$scope.warningMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].numeratorName.trim() + "' (" + $scope.formTableData[formats[x]][j].denominatorValue + ")";
								$("#warningMessage").modal("show");
								$("#" + id).addClass("warned");
							}
							
							$scope.calculatePercentage(model, format);
							return false;
						}
					}
				}
			}
		}
	}
	$scope.isNumeratorLessThanDenominator = function(model, key, option, format, id){
		if(model.numeratorValue && model.denominatorValue){
			if(parseInt(model.numeratorValue) > parseInt(model.denominatorValue)){
				if(key == 'numeratorValue' && option == "Mandatory"){
					$scope.errorMsg = "Numerator value can not be greater than denominator value";
					$("#errorMessage").modal("show");
				}
				else if(key == 'numeratorValue' && option == "Not mandatory"){
					$scope.warningMsg = "Numerator value is greater than denominator value";
					$("#warningMessage").modal("show");
					$("#" + id).addClass("warned");
				}
				else if(key == 'denominatorValue' && option == "Not mandatory"){
					$scope.warningMsg = "Denominator value is less than numerator value";
					$("#warningMessage").modal("show");
					$("#" + id).addClass("warned");
				}
				else{
					$scope.errorMsg = "Denominator value can not be less than numerator value";
					$("#errorMessage").modal("show");
				}
					
				
				if(option == "Mandatory"){
					model[key] = "";
					$("#" + id).addClass("errorFound");
					$("#" + id).removeClass("warned");
				}
					
				$scope.calculatePercentage(model, format);
				return false;
			}
		}
	};
	$scope.isGreaterThanDependency = function(model, key, compareIds, option, format, id){
		var formats = $scope.formKeys;
		if(!(model[key] == "" || model[key] == null))
		for(var x=0; x<formats.length; x++){
			for(var i=0; i<compareIds.length; i++){
				for(var j=0; j<$scope.formTableData[formats[x]].length; j++){
					if(compareIds[i] == $scope.formTableData[formats[x]][j].indicatorId){
						if(!($scope.formTableData[formats[x]][j][key] == null || $scope.formTableData[formats[x]][j][key] == "") && parseInt(model[key]) > parseInt($scope.formTableData[formats[x]][j][key]) ){
							
							if(option == "Mandatory"){
								$scope.errorMsg = "'" + model.denominatorName + "' can not be greater than '" + $scope.formTableData[formats[x]][j].denominatorName + "' ("+$scope.formTableData[formats[x]][j].denominatorValue+")";
								$("#errorMessage").modal("show");
								model[key] = "";
								$("#" + id).addClass("errorFound");
								$("#" + id).removeClass("warned");
							}
							else{
								$scope.warningMsg = "'"+model.denominatorName + "' is greater than '" + $scope.formTableData[formats[x]][j].denominatorName + + "' ("+$scope.formTableData[formats[x]][j].denominatorValue+")";
								$("#warningMessage").modal("show");
								$("#" + id).addClass("warned");
							}
							
							$scope.calculatePercentage(model, format);
							return false;
						}
					}
				}
			}
		}
	};
	$scope.isEqualToDependency = function(model, key, compareIds, option, format, id){
		var formats = $scope.formKeys;
		if(!(model[key] == "" || model[key] == null))
			for(var x=0; x<formats.length; x++){
				for(var i=0; i<compareIds.length; i++){
					for(var j=0; j<$scope.formTableData[formats[x]].length; j++){
						if(compareIds[i] == $scope.formTableData[formats[x]][j].indicatorId){
							if(!($scope.formTableData[formats[x]][j][key] == null || $scope.formTableData[formats[x]][j][key] == "") && parseInt(model[key]) != parseInt($scope.formTableData[formats[x]][j][key])){
								
								if(option == "Mandatory"){
									$scope.errorMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].denominatorName.trim() + "' (" + $scope.formTableData[formats[x]][j].denominatorValue + ")";
									$("#errorMessage").modal("show");
									model[key] = "";
									$("#" + id).addClass("errorFound");
									$("#" + id).removeClass("warned");
								}
								else{
									$scope.warningMsg = "Value mismatch with previous input for '" + $scope.formTableData[formats[x]][j].denominatorName.trim() + "' (" + $scope.formTableData[formats[x]][j].denominatorValue + ")";
									$("#warningMessage").modal("show");
									$("#" + id).addClass("warned");
								}
								
								$scope.calculatePercentage(model, format);
								return false;
							}
						}
					}
				}
			}
	};
	$scope.checkIsRequired = function(model, key){
		if(model.isRequired == null){
			if(model[key] == "" || model[key] == null){
				$scope.errorMsg = key + " of " + model.indicatorName + " can not be blank";
				$("#errorMessage").modal("show");
				return false;
			}
		}
		if(model.isRequired){
			if(model[key] == "" || model[key] == null){
				$scope.errorMsg = key + " of " + model.indicatorName + " can not be blank";
				$("#errorMessage").modal("show");
				return false;
			}
		}
	};
	
	$scope.isLesserThanDependency = function(model, key, compareIds, option, format, id){
		var formats = $scope.formKeys;
		if(!(model[key] == "" || model[key] == null))
		for(var x=0; x<formats.length; x++){
			for(var i=0; i<compareIds.length; i++){
				for(var j=0; j<$scope.formTableData[formats[x]].length; j++){
					if(compareIds[i] == $scope.formTableData[formats[x]][j].indicatorId){
						if(!($scope.formTableData[formats[x]][j][key] == null || $scope.formTableData[formats[x]][j][key] == "") && parseInt(model[key]) < parseInt($scope.formTableData[formats[x]][j][key]) ){
							
							if(option == "Mandatory"){
								$scope.errorMsg = model.denominatorName + " can not be less than " + $scope.formTableData[formats[x]][j].denominatorName;
								$("#errorMessage").modal("show");
								model[key] = "";
								$("#" + id).addClass("errorFound");
								$("#" + id).removeClass("warned");
							}
							else{
								$scope.warningMsg = model.denominatorName + " is less than " + $scope.formTableData[formats[x]][j].denominatorName;
								$("#warningMessage").modal("show");
								$("#" + id).addClass("warned");
								
							}
							
							$scope.calculatePercentage(model, format);
							return false;
						}
					}
				}
			}
		}
	};
}
$(document).ready(function(){
	$("#addIndicatorModal .modal-dialog").css("max-height", $(window).height() - 75 -44);
	if($(window).height()>992)
	$("#addIndicatorModal .modal-dialog .modal-body .table-responsive").css("max-height", $(window).height() - 75 -44 -200);
	else{
		$("#addIndicatorModal .modal-dialog .modal-body .table-responsive").css("max-height", 'none');
	}
	$("#confirmAddIndicator .modal-dialog").css("max-height", $(window).height() - 75 -44);
	$("#confirmAddIndicator .modal-dialog .modal-body ul").css("max-height", $(window).height() - 75 -44-180);
	$("#r1, #r2, #r3").click(function(){
		$("[type='radio'] + label + .content").each(function(){$(this).height($(this).find(".table-responsive").height()+300)})
	})
});