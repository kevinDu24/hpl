
app.controller('purchaseHomeController', ['toaster', '$scope', '$http', '$state', '$rootScope', '$location', '$cookies', '$localStorage', function(toaster, $scope, $http, $state, $rootScope, $location, $cookies, $localStorage) {

    $scope.showMsg = '获取验证码';
    $scope.authError = null;
    $scope.user = {};
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.apply = function(){
        $state.go('access.signin');
    };

}]);