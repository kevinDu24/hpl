
app.controller('approvalResultController', ['toaster', '$scope', '$http', '$state','$rootScope', '$location', '$cookies', '$localStorage', function(toaster, $scope, $http, $state, $rootScope, $location, $cookies, $localStorage) {
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.isShow11 = true;
    $scope.isShow12 = false;
    $scope.isShow21 = true;
    $scope.isShow22 = false;
    $scope. isShow3 = false;
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    init();
    function init() {
        $http.get('/approval/queryFinancePreApplyResult').success(function(result){})

        var url = '/approval/getApprovalStatus?applyNum='+ $localStorage.applyNum;
        $http.post(url).success(function(result){
            if(result.status == 'SUCCESS'){
                //alert("成功");
                var p = "";
                $scope.resultStatus = result.data.status;
                if(result.data.status == '100'){
                    $scope.resultmessage = "请您稍后，我们在拼命审核中......";
                    $scope.isShow11 = false;
                    $scope.isShow12 = true;
                    setTimeout(function () {
                        init();
                    },5000)
                }else if(result.data.status == '1000'){
                    $scope.resultmessage = "恭喜您, 通过审核，可以去申请了奥";
                    $scope.isShow11 = false;
                    $scope.isShow12 = true;
                    $scope.isShow21 = false;
                    $scope.isShow22 = true;
                    $scope. isShow3 = true;
                }else if(result.data.status == '1100'){
                    $scope.resultmessage = "很遗憾，您未通过审核" ;
                    $scope.resultmessagereason = "拒绝原因："+ result.data.reason;
                    $scope.isShow11 = false;
                    $scope.isShow12 = true;
                    $scope.isShow21 = false;
                    $scope.isShow22 = true;
                    $scope. isShow3 = true;
                }
            }else{
                alert(result.error);
            }
        }).error(function () {
            alert('系统异常，请稍后再试');
        });
    }
}]);