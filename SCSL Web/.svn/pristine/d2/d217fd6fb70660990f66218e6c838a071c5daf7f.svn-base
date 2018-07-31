//@author Devikrushna (devikrushna@sdrc.co.in) && Laxman (laxman@sdrc.co.in)


function addPdsaController($scope, $http,$filter, allServices){
	$scope.pageName = "PDSA";
	$scope.activeMenu = "pdsaDataEntry";
	$scope.addIdea=false;
	$scope.weekNumber=false;
	
	
	$("#loader-mask").show();
		allServices.getPDSAFilterOption().then(function(data){
			
			$scope.indicatordata = data.Indicator;
			$scope.coreAreaList = Object.keys($scope.indicatordata);
			$scope.IndicatorList = undefined;
			$scope.changeIdeaList=undefined;
			$scope.frequencyList=data.frequencies;
			$("#loader-mask").fadeOut();
		}, function(){
			
		});
		
		allServices.generatePDSANumberAndDate().then(function(data){
			$scope.pdsanumber = data.pdsaNumber;
			$scope.pdsatime=data.time;
//			$( "#datepicker" ).datepicker({dateFormat: "dd-mm-yy",minDate:$scope.pdsatime, onSelect: function (selected) {
//	            var dt = new Date(selected);
//	            dt.setDate(dt.getDate() + 1);
//	            $("#datepicker1").datepicker("option", "minDate", dt);}});
			$( "#datepicker" ).datepicker({dateFormat: "dd-mm-yy",minDate:$scope.pdsatime});
			$( "#datepicker1" ).datepicker({dateFormat: "dd-mm-yy",minDate:$scope.pdsatime});
		}, function(){
			
		});
		allServices.getBlankPDSAObject().then(function(data){
			$scope.pdsaModel=data;
		}, function(){
			
		});
		
		$scope.openIdeamodal=function(){
			$scope.changeIdeaDescription=undefined;
			$("#changeidea").css("height", "120px");
			$('#addIdea').modal('show');
		};
		
		$scope.selectArea = function(area){
			$("#loader-mask").show();
			$scope.selectedArea = area;
			$scope.coreIndicatorList=$scope.indicatordata[area];
			$scope.selectedIndicator=undefined;
			$scope.changeIdeaList=undefined;
			$scope.selectedChangeIdea=undefined;
			$scope.addIdea=false;
			$("#loader-mask").fadeOut();
		};
		$scope.selectIndicator = function(indicator){
			$("#loader-mask").show();
			$scope.selectedIndicator = indicator;
			$scope.selectedChangeIdea=undefined;
			$scope.changeIdeaList=$scope.selectedIndicator.valueModelList;
			$scope.addIdea=true;
			/*$("#name").autocomplete({
				source : $scope.changeIdeaList,
				appendTo : "#searchIdeaList",
				select: function(event, ui) {
//					$scope.selectIdea(ui.item);
					$scope.$apply();
			    }
			});*/
			$("#loader-mask").fadeOut();
			
		};
		$scope.selectIdea = function(idea){
			$scope.selectedChangeIdea = idea;
			$scope.addIdea=true;
			
		};
		$scope.getAutoCompleteChangeIdea = function(){
			$("#changeidea").autocomplete({
				source : $scope.changeIdeaList,
				appendTo : "#searchIdeaList",
				select: function(event, ui) {
					$scope.changeIdeaDescription = ui.item.value;
					$('#changeidea').click();
					$scope.$apply();
			    }
			});
			 $("#changeidea").autocomplete("option", "position", {
	                my: "left top",
	                at: "left bottom"
	            });
		}
		$scope.selectFrequency = function(frequency){
			$("#loader-mask").show();
			$scope.selectedFrequency = frequency;
			if($scope.selectedFrequency.key!=1){
				$scope.weekNumber=true;
				$scope.calculateEndDate();
				
			}
			else
				{
				$scope.weekNumber=false;
				$scope.calculateEndDate();
				}
			
		};
		
		$scope.saveChangeIdealist=function()
		{
			if($scope.changeIdeaDescription==undefined ){
				$scope.errorMsg="Please enter change idea";
				$("#errorMessage").modal("show");
			}
			else
				{
				$("#loader-mask").show();
				$scope.saveChangeIdea()
				}
		};
		

		$scope.saveChangeIdea=function(){
			allServices.saveChangeIdea($scope.selectedIndicator.key,$scope.changeIdeaDescription).then(function(data){
				$("#loader-mask").fadeOut();
				if(data.value!=null)
					{
				$scope.changeIdeaList.push(data);
				$scope.selectedChangeIdea=data;
				$scope.msg = "Saved Successfully";
				$("#pop1").modal("show");
				$scope.changeIdeaDescription=undefined;
					}
				else
					{
					$scope.errorMsg = "Duplicate Change Idea not allowed";
					$("#errorMessage").modal("show");
					$scope.changeIdeaDescription=undefined;
					}
				
			}, function(){
				
			});
			
			
		};
		$scope.successidea=function(){
			
			$("#pop1").modal("hide");
			$("#addIdea").modal("hide");
			
		};
		/*$scope.resetStartDate = function(){
			var endDate = $('#datepicker').datepicker ('refresh');
		}*/
			
		$scope.calculateEndDate=function(){
			$("#loader-mask").fadeOut();
			if($scope.monthNumber==0||$scope.monthNumber=='0'|| typeof $scope.monthNumber==String)
				{
				$scope.monthNumberCount=0;
				}
			else{
				$scope.monthNumberCount = $scope.monthNumber;
			}
				var endDate = $('#datepicker').datepicker ('getDate'); 
				if(endDate){
				endDate.setDate (endDate.getDate () + (parseInt($scope.selectedFrequency.value) * $scope.monthNumberCount - 1));
				}
			       var eventDatepicker = angular.element(document).find('#datepicker1');
//			        console.log(eventDatepicker);
			        eventDatepicker.datepicker('refresh');
			        eventDatepicker.datepicker("option", "minDate", endDate);
			        eventDatepicker.datepicker("option", "maxDate", endDate);
			        $( "#datepicker1" ).datepicker( "setDate", (endDate));
				
			
			
			
		};
		$scope.validatePdsaForm=function(){
			if($scope.selectedArea==undefined ){
				$scope.errorMsg="Please select focus area";
				$("#errorMessage").modal("show");
			}
			else if($scope.selectedIndicator==undefined){
				$scope.errorMsg="Please select indicator";
				$("#errorMessage").modal("show");
			}
			else if($scope.selectedChangeIdea==undefined){
				$scope.errorMsg="Please select change idea";
				$("#errorMessage").modal("show");
			}
			else if($scope.pdsaName==undefined){
				$scope.errorMsg="Please enter PDSA name";
				$("#errorMessage").modal("show");
			}
			else if($scope.pdsaSummary==undefined){
				$scope.errorMsg="Please enter PDSA summary";
				$("#errorMessage").modal("show");
			}
			else if($scope.pdsaSummary.length < 150){
				$scope.errorMsg="PDSA summary should be minimum 150 character length";
				$("#errorMessage").modal("show");
			}
			else if($scope.selectedFrequency==undefined){
				$scope.errorMsg="Please select frequency of data collection";
				$("#errorMessage").modal("show");
			}
			else if($scope.startDate==undefined){
				$scope.errorMsg="Please select start date";
				$("#errorMessage").modal("show");
			}
			else if($scope.selectedFrequency.key!=1&&($scope.monthNumber==undefined || $scope.monthNumber===''||$scope.monthNumber===0))
				{
				$scope.errorMsg="Please enter the number of PDSA entries";
				$("#errorMessage").modal("show");
				}
			else if($scope.selectedFrequency.key==1&&$scope.endDatePDSA==undefined)
				{
				$scope.errorMsg="Please enter end date";
				$("#errorMessage").modal("show");
				}
			else if(parseInt($scope.selectedFrequency.value) > 14){
				$scope.warningMsg="Frequency of data collection is more than 14 days";
				$("#warnLargeFrequency").modal("show");
			}
		else{
			$("#loader-mask").show();
			$scope.submitPdsa();
		}
		};
		

		$scope.submitPdsa = function(){
			$("#warnLargeFrequency").modal("hide");
			var endDate = $('#datepicker1').datepicker ({ dateFormat: 'dd,MM,yyyy' }).val();
			$scope.pdsaModel.pdsaName=$scope.pdsaName;
			$scope.pdsaModel.pdsaNumber=$scope.pdsanumber;
			$scope.pdsaModel.frequency=$scope.selectedFrequency.value;
			$scope.pdsaModel.indicatorId=$scope.selectedIndicator.key;
			$scope.pdsaModel.changeIdeaId=$scope.selectedChangeIdea.key;
			$scope.pdsaModel.summary=$scope.pdsaSummary;
			$scope.pdsaModel.startDate=$scope.startDate;
			$scope.pdsaModel.endDate=endDate;
//			if($scope.pdsaModel.frequencyId!=1)
			$scope.pdsaModel.pdsaFrequency=$scope.monthNumber;
		
		
		$http({
			url : 'savePDSADetails', 
			method : 'POST',
			data : JSON.stringify($scope.pdsaModel),
			contentType : 'application/json'
		}).then(function successCallback(response) {
			$("#loader-mask").fadeOut();
			if(response.data.statusCode=='404')
			{
			$scope.errorMsg = response.data.errorMessage;
			$("#errorMessage").modal("show");
				}
			else
				{
				$scope.msg =response.data.errorMessage;
				$("#pop").modal("show");
				$scope.link='pdsa'
				
				}
			}, function errorCallback(response) {
		  });;
		
		
		};

		$scope.resetStartDate = function(){
			$("#datepicker").datepicker("option", "minDate", $scope.pdsatime);
			$.datepicker._clearDate($("#datepicker"));
//			$.datepicker._clearDate($("#datepicker1"));
		}
	
	
	$scope.reloadPage = function(){
		window.loaction.href = '';
	};
	/*$scope.calculateCharLeft = function(textAreaModel){
		if(textAreaModel){
			$scope.pdsaSummary = textAreaModel + "(" + (1000-$scope.pdsaSummary.length) + ")";
		}
	};*/
	
}
