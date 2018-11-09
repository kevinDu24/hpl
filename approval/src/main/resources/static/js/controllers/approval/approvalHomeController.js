
app.controller('approvalHomeController', ['toaster', '$scope', '$http', '$state', '$rootScope', '$location', '$cookies', '$localStorage', function(toaster, $scope, $http, $state, $rootScope, $location, $cookies, $localStorage) {

    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.apply = function(){
        $state.go('app.approvalDetail');
    };

}]);