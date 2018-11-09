
app.controller('purchaseSigninController', ['toaster', '$scope', '$http', '$state', '$rootScope', '$location', '$cookies', '$localStorage', function(toaster, $scope, $http, $state, $rootScope, $location, $cookies, $localStorage) {

    $scope.showMsg = '获取验证码';
    $scope.authError = null;
    $scope.user = {};
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    $scope.login = function(){
        if($scope.code == null || $scope.code == ''){
           $scope.authError = '请填写验证码';
            return;
        }
        if($scope.phoneNum == null ||  $scope.phoneNum == null){
            $scope.authError = '请填写手机号';
            return;
        }
        $http.post('/system/verifyCode?phoneNum=' + $scope.phoneNum + '&code=' + $scope.code).success(function(data){
            if(data.status == 'SUCCESS'){

                var expireDate = new Date();
                expireDate.setDate(expireDate.getDate() + 1);
                $cookies.put("purchaseUser", $scope.phoneNum, {'expires': expireDate});
                //首次注册，车型页
                if(data.data == '0'){
                    $state.go('app.selectVehicle');
                }
                if(data.data == '1'){
                    $state.go('app.selectVehicle');
                }
                if(data.data == '2'){
                    $state.go('app.approvalResult');
                }
                if(data.data == '3'){
                    $state.go('app.newOrderRecord');
                }
                if(data.data == '4'){
                    $state.go('app.applyDetails');
                }

            }else{
                $scope.authError = data.error;
            }
        }).error(function () {
                $scope.authError = "系统异常，请稍后再试";
         });
    };

    $scope.getSmsCode = function(){
        if($scope.startCountdown){
            return;
        }
        if($scope.phoneNum == null){
            $scope.authError = '请填写手机号';
            return;
        }

        $http.post('/system/sendCode?phoneNum=' + $scope.phoneNum).success(function(data){
            if(data.status == 'SUCCESS'){
                $scope.$broadcast('timer-start');//开始倒计时
                $scope.showMsg = 's 后重新获取';
                $scope.startCountdown = true;
            }else{
               $scope.authError = data.error;
            }
        }).error(function (){
              $scope.authError = "系统异常，请稍后再试";
       });

    };

    $scope.$on('timer-stopped', function (event, data){
        $scope.startCountdown = false;
        $scope.showMsg = "获取验证码";
        $scope.$digest();//通知视图模型的变化
    });
}]);