var app = angular.module('JobFinder', ['ngRoute', 'ngMaterial', 'ngMessages'])
app.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider
        .when('/login', {
            templateUrl: 'view/login.html',
            controller: 'loginController'
        }).when('/register', {
            templateUrl: 'view/register.html',
            controller: 'registerController'
        }).when('/main', {
            templateUrl: 'view/main.html',
            controller: 'mainController'
        }).otherwise({
            redirectTo: '/login'
        });
    }]);