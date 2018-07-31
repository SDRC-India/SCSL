function dashboardHomeController($scope, $rootScope, $http, $timeout, allServices){
	$scope.activeMenu = 'dashboardHome';
	$scope.pageName = 'Dashboard';
	$scope.lineChartData = undefined;
	
	/*
	 * Get  Json for indicators
	*/	
	allServices.getDashboardIndicators().then(function(data){
		$("#loader-mask").show();
		$scope.statictIndicators = data;
		allServices.getRestDashboardIndicators().then(function(data){
			$scope.indicators = $scope.statictIndicators.concat(data);
			$("#loader-mask").fadeOut();
			
			/*
			 * Get Json for line charts once indicators are loaded
			*/
			allServices.getDashboardChart().then(function(data){
				$("#loader-mask").show();
				$scope.lineChartData = data;
				$("#loader-mask").fadeOut();
			}, function(){
				
			});
			
			$timeout(function(){
				$('[data-toggle="tooltip"]').tooltip();
			});
		}, function(){
			
		});

	}, function(){
		
	});
	
	/*
	 * returns tooltip data when hover on indicator grid 
	*/
	$scope.getTooltipData = function(data){
		return "<b>Andhra Pradesh : </b>" + (data.andhraValue != null ? data.andhraValue: "N/A") + (data.source == ""? "<br><span style='color: #333a3b'>(" + data.andhraSource + ")</span>": "") +"<br>"+ "<b>Telangana : </b>" + (data.telanganaValue != null ? data.telanganaValue:"N/A") + (data.source == ""? "<br><span style='color: #333a3b'>(" + data.telanganaSource + ")</span>": "");
	};
	$scope.getArrayForGridRows = function(num) {
		if(num)
	    return new Array(num);   
		else
			return [];
	}
	//on click of download button
	$scope.getLandingPageExcelFile = function() {
		$("#loader-mask").show();
		d3.selectAll(".domain, .y.axis .tick line").style({"fill": "none", "stroke": "#000"});
		d3.selectAll("svg").attr("version", 1.1).attr("xmlns", "http://www.w3.org/2000/svg");
		if ($scope.indicators) {
			var url = 'getDashboardHomeExcelFilePath';
			var chartSvgs = [];
			var svgs = $(".trend-chart").map(function() {
				return "#" + this.id;
			}).get();
			for (var i = 0; i < svgs.length; i++) {
				$(svgs[i]).children().each(function(index, el) {
					var chartSvg = $(this).html();
					if (chartSvg != "")
						chartSvgs.push(chartSvg);
				});
			}
			;
			var gridChartJson = {
				boxCharts : $scope.indicators,
				chartModels : $scope.lineChartData,
				trendChartSvgs : chartSvgs
			}

			$http.post(url, gridChartJson).then(function(result) {

				var fileName = {
						"fileName" : result.data
					};
					$.download("downloadReport/", fileName, 'POST');
					$("#loader-mask").fadeOut();
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
			$(".loader").css("display", "none");
		}
		;
	};
}
