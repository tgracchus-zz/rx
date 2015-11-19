// MODULE
var pagesApp = angular.module('pagesApp', ['ngCookies']);

// CONTROLLERS
pagesApp.controller('loginController',
 ['$scope','$http','$location','$window', function ($scope,$http,$location,$window) {

    $scope.logged = false;

    $scope.isActive = function (viewLocation) {
         return viewLocation === $location.path();
     };

    $scope.isLogged = function() {
       return $scope.logged;
    }


    $scope.login = function(form) {

        var loginContext = {
            "userId" : form.userId,
            "password" : form.password
        }

        $http({
            method: 'POST',
            url: 'http://localhost:8080/login',
            data: loginContext,
            headers: {}
        })
        .then(function successCallback(response) {
                $scope.logged = true;
              }, function errorCallback(response) {
                 $scope.logged = false;
              });
    }

    $scope.logout = function(form) {

            $http({
                method: 'POST',
                url: 'http://localhost:8080/logout',
                headers: {}
            })
            .then(function successCallback(response) {
                    $scope.logged = false;
                  }, function errorCallback(response) {
                    $scope.logged = false;
            });

            $window.location.reload();
        }

}]);


