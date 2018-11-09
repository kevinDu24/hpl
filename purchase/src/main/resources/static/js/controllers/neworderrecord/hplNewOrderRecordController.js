
app.controller('hplNewOrderRecordController', ['toaster', '$scope', '$http', '$state','$rootScope', '$location', '$cookies', '$localStorage', function(toaster, $scope, $http, $state, $rootScope, $location, $cookies, $localStorage) {
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.purchaseOrderInfo = {};
    $scope.purchaseOrderInfo.bankName = '';
    $scope.searchType = '';
    var flag = true;
    //var userInfo = '18055313782';
    var userInfo = $cookies.get('purchaseUser');
    $scope.apply = function(){
        $state.go('app.newOrderRecord');
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };

    function initNewOrderRecord() {
            $http.get('/order/getneworderrecord?phoneNum=' + userInfo).success(function(result){
                if(result.status == 'SUCCESS'){
                    $scope.purchaseOrderInfo = result.data;
                    $scope.purchaseOrderInfo.bankName = result.data.bankName;
                    flag = false;
                }
            }).error(function () {
                alert('系统异常，请稍后再试');
            });
    }
    function initBanks() {
        $http.get('/order/banks?phoneNum=' + userInfo).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.bankNameList = result.data;
                if(flag){
                    $scope.purchaseOrderInfo.bankName = $scope.bankNameList[0].BAHKYH;
                }
            }else{
                alert(result.error);
            }
        }).error(function () {
            alert('系统异常，请稍后再试');
        });
    }

    function init(){
        if($cookies.get('purchaseUser') == null || $cookies.get('purchaseUser') == ''){
            $state.go('access.signin');
        }
        initBanks();
        initNewOrderRecord();
    }
    init();

    $scope.onChangeRadio = function(){
        if ($scope.searchType == 0) {
            $scope.purchaseOrderInfo.localAdd = $scope.purchaseOrderInfo.liveAdd;
        } else {
            $scope.purchaseOrderInfo.localAdd = '';
        }
    }
    $scope.next = function () {
        var personInfo = false;
        var bankInfo = false;
        if($scope.purchaseOrderInfo.company == null ||$scope.purchaseOrderInfo.company == '' ||  $scope.purchaseOrderInfo.workAdd == null||  $scope.purchaseOrderInfo.workAdd == ''||
            $scope.purchaseOrderInfo.liveAdd == null||$scope.purchaseOrderInfo.liveAdd == ''|| $scope.purchaseOrderInfo.localAdd == null|| $scope.purchaseOrderInfo.localAdd == ''){
            personInfo = true;
        }
        if($scope.purchaseOrderInfo.bankUser == null ||$scope.purchaseOrderInfo.bankUser == '' || $scope.purchaseOrderInfo.bankCard == null || $scope.purchaseOrderInfo.bankCard == ''
            || $scope.purchaseOrderInfo.bankName == null || $scope.purchaseOrderInfo.bankName == ''|| $scope.purchaseOrderInfo.bankName == '借记卡开户行'){
            bankInfo = true;
        }
        if(bankInfo && personInfo){
            alert("请完善个人信息");
            return
        }
        if(!bankInfo && personInfo){
            alert("请检查个人信息是否填写完整！");
            return
        }
        if(bankInfo && !personInfo){
            alert("请检查银行卡信息是否填写完整！");
            return
        }
        $http.post('/order/neworderrecord?phoneNum=' + userInfo, $scope.purchaseOrderInfo).success(function(result){
            if(result.status == 'SUCCESS'){
                alert("订单信息提交成功!");
                $state.go('app.vehicleAttachment');
            }else{
                alert(result.error);
            }
        }).error(function () {
            alert('系统异常，请稍后再试');
        });
    };
}]);