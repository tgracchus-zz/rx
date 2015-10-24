// MODULE
var pagesApp = angular.module('pagesApp', ['ngCookies']);

// CONTROLLERS
pagesApp.controller('loginController',
 ['$scope','$http','$cookies','$location','$window', function ($scope,$http,$cookies,$location,$window) {

  $scope.session = {"sessionId" : null}

    $scope.isActive = function (viewLocation) {
         return viewLocation === $location.path();
     };

    $scope.isLogged = function() {
        $scope.session = $cookies.getObject("session")
        if($scope.session){
            return true;
        }else{
             return false
        }
    }


    $scope.login = function(form) {

        var loginContext = {
            "userId" : form.userId,
            "password" : form.password
        }

        $http({
            method: 'POST',
            url: 'http://localhost:8888/login',
            data: loginContext,
            headers: {}
        })
        .then(function successCallback(response) {
                $scope.session = response.data;
                $cookies.putObject("session",response.data)
                form.success = true;
                form.error =false;
              }, function errorCallback(response) {
                 form.success = false;
                 form.error =true;
              });
    }

    $scope.logout = function(form) {
            var session = $cookies.getObject("session")
            var logoutContext = {
                "sessionId" : session.sessionId,
            }

            $http({
                method: 'POST',
                url: 'http://localhost:8888/logout',
                data: logoutContext,
                headers: {}
            })
            .then(function successCallback(response) {
                    $cookies.putObject("session",null)
                     $scope.session = null
                  }, function errorCallback(response) {
                    $cookies.putObject("session",null)
                      $scope.session = null

            });

            $window.location.reload();
        }

}]);


