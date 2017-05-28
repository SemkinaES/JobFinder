var app = angular.module('JobFinder')

app.controller('indexController', function ($scope, $http, $rootScope, $location) {

     $scope.logout = function() {
        $rootScope.client = null;
        $rootScope.isAuthorizated = false;
        $location.path('/login');
     };

     $rootScope.signIn = function(login, password) {
         var auth = {
                    login: login,
                    password: password
                  };
         $http.post('/api/client/auth', auth)
                    .then(function (response) {
                            $rootScope.client = response.data;
                            $rootScope.isAuthorizated = true;
                            $location.path('/main');
                        },
                        function (response) {
                            $rootScope.isAuthorizated = false;
                        });
     };
});

app.controller('loginController',  function ($http, $scope, $rootScope, $location) {
    if ($rootScope.isAuthorizated) {
        $location.path('/main');
    };

    $scope.signIn = function() {
        $rootScope.signIn($scope.username, $scope.password);
    };

    $scope.signUp = function () {
        $location.path('/register');
    };
});

app.controller('registerController',  function ($http, $scope, $rootScope, $location) {
    if ($rootScope.isAuthorizated) {
        $location.path('/main');
    };

    $scope.signUp =function() {
        var client = {
                        "name": $scope.name,
                        "city": $scope.city,
                        "email": $scope.email,
                        "login": $scope.username,
                        "password": $scope.password,
                        "queries":[]
                     };
        $http.post('/api/client', client)
            .then(function (response) {
                   if (response.data.status == 'OK') {
                        $rootScope.signIn($scope.username, $scope.password);
                   }
                },
                function (response) {
                });
    };
});

app.controller('mainController',  function ($http, $scope, $rootScope, $location) {
    if (!$rootScope.isAuthorizated) {
        $location.path('/login');
    };

    $scope.queries = [];
    if ($rootScope.client != null) {
        $scope.name = $rootScope.client.name;
        $scope.city = $rootScope.client.city;
        $scope.email = $rootScope.client.email;
        $scope.username = $rootScope.client.login;
        $scope.password = $rootScope.client.password;
        if ($rootScope.client.queries != null) {
            $scope.queries = $rootScope.client.queries;
        }
    }

    $scope.save = function () {
        var client = {
                         	"name": $scope.name,
                         	"city": $scope.city,
                         	"email": $scope.email,
                         	"login": $scope.username,
                         	"password": $scope.password,
                         	"queries":  $scope.queries
                         };
            $http.put('/api/client', client)
                .then(function (response) {
                       $rootScope.client = response.data;
                    },
                    function (response) {
                    });
    };
});