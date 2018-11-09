app.controller('applyDetailsController', ['$scope', '$http', '$state', '$localStorage', '$cookies', function($scope, $http, $state, $localStorage, $cookies) {

  if($cookies.get('purchaseUser') == null || $cookies.get('purchaseUser') == ''){
         $state.go('access.signin');
  }
    $scope.resultMessage = '';
    var phoneNum = $cookies.get('purchaseUser');
    function getContractLog(condition){
        $http.get('/approval/'+condition+'/log').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.items = [];
                $scope.items = result.data.contractinfo.contractstatelist;
                if($scope.items.length == 0){
                    $scope.resultMessage = "您的订单已提交，请耐心等待审核......";
                }
                for(var j in $scope.items){
                    if($scope.items[j].BASQZT == '首付款扣款成功'){
                        $scope.resultMessage = "备注：扣款成功后等待店面提车信息，请保持手机畅通！";
                    }
                 }
                $scope.ApplicantsInfo = {name: result.data.contractinfo.BASQXM, num: result.data.contractinfo.BASQBH};
            }else{
                alert(result.error);
            }
        }).error(function () {
                alert('系统异常，请稍后再试');
        });
    }
    getContractLog(phoneNum);
    $scope.convertDate = function(date){
            return date.substring(0, 4) + '-' + date.substring(4, 6) + '-' + date.substring(6, 8)
    };
}])
;

