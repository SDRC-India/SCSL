var  app = angular.module('passwordChangeApp', []);
app.controller('passwordChangeController', function($scope, $http, $filter, $timeout, $window, $location) {

					$scope.activeMenu = "changePwd";
					$scope.pageName = "Change Password";
					$scope.user = {};
					
					$scope.removeErr=function(){
						if($scope.user.newPassword != undefined || $scope.user.newPassword != "")
							document.getElementById('newPasserror').innerHTML  = "";
					};
					
					$scope.changeUserPassword = function() {

						if($scope.user.oldPassword == undefined || $scope.user.oldPassword == ""){
							document.getElementById('oldPasserror').innerHTML  = "Please enter current password."; 
						} else if ($scope.user.newPassword == undefined	|| $scope.user.newPassword == "") {
							document.getElementById('newPasserror').innerHTML  = "Please enter new password.";
						} else if($scope.user.oldPassword == $scope.user.newPassword){
							document.getElementById('newPasserror').innerHTML  = "New password should not be same as currrent password.";
						} else if ($scope.user.newPassword != $scope.user.confirmPassword) {
							document.getElementById('confirmPasserror').innerHTML  = "Password did not match.";
						}else {
							document.getElementById('oldPasserror').innerHTML  = "";
							document.getElementById('newPasserror').innerHTML  = "";
							document.getElementById('confirmPasserror').innerHTML  = "";
						  							
							$http.post('checkCurrentPassword?currentPassword='+ $scope.user.oldPassword).then(function(result) {
								//console.log(result.data);
								$scope.changeData = result.data;
							   if($scope.changeData == true){
								   $("#loader-mask").show();
								 $http.post('updateALoggedInUserPassword?newPassword='+ $scope.user.newPassword).then(function(res) {
									 $("#loader-mask").fadeOut();
									 $scope.msg = "Password changed.";
									 $('#pop').modal('show');
					   			  });
					        	}else if($scope.changeData == false){
					        		$scope.validationMsg = "Current password is incorrect";
					        		$('#errorMessage').modal('show');
					        		$scope.user.oldPassword  = "";
					        		//$scope.user.newPassword = "";
					        		//$scope.user.confirmPassword = "";
					        	}
							}, function(error) {
								console.log(error);
							});
						}

					};
					
					
					$scope.oldPassError = function()
					{
						if($scope.user.oldPassword != undefined || $scope.user.oldPassword != "")
						document.getElementById('oldPasserror').innerHTML  = "";
					};
					$scope.newPassError = function()
					{
						if($scope.user.newPassword != undefined || $scope.user.newPassword != "")
						document.getElementById('newPasserror').innerHTML  = "";
					};
					$scope.confirmPassError = function()
					{
						if($scope.user.newPassword == $scope.user.confirmPassword)
						document.getElementById('confirmPasserror').innerHTML  = "";
					};
					
					$scope.function1 = function() {
						$(".minChar").show();
						$("#span_error").hide();
					};
					$scope.redirectToLogout = function(){
						 $window.location.href = "/SCSL/webLogout";
					};
				});




app.directive('disallowSpaces', function() {
	  return {
	    restrict: 'A',

	    link: function($scope, $element) {
	      $element.bind('input', function() {
	        $(this).val($(this).val().replace(/ /g, ''));
	      });
	    }
	  };
	});

/******************** code for change password****************************/
