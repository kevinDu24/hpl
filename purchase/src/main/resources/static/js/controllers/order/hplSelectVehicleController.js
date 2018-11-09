app.controller('hplSelectVehicleController',['$scope', '$state', '$http','toaster','$rootScope', '$cookies', function($scope,$state,$http,toaster,$rootScope,$cookies) {
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };

    // 登录验证
    if($cookies.get('purchaseUser') == null || $cookies.get('purchaseUser') == ''){
        $state.go('access.signin');
    }
    var userInfo = $cookies.get('purchaseUser');

    // 五种车型的各种属性的数据
    $scope.dataList = {};
    // 车型下拉框
    $scope.vehicleTypes = {};
    // 三个选择框选择的值
    $scope.order = {};
    // 当前选定的某款车型的车辆信息
    $scope.vehicle = null;

    init();
    function init() {
        var url = '/order/init?phoneNum='+ userInfo;
        $http.get(url).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.vehicleTypes = result.data.cars;
                $scope.dataList = result.data.carInfos;
                var vehicleId = result.data.vehicleId;
                // 该手机号下是否已经保存过车型页面相关信息
                if(vehicleId == null || vehicleId == ''){
                    // 否：默认第一款车型
                    $scope.vehicle = result.data.carInfos[0];
                    $scope.order.vehicleType = $scope.vehicle.vehicleId;
                    $scope.order.product = '0';
                    $scope.order.color = $scope.vehicle.colors[0];
                }else {
                    // 是：缓存用户之前选择过的值
                    $scope.order.vehicleType = vehicleId;
                    $scope.onChangeType();
                    $scope.order.product = result.data.product;
                    $scope.order.color = result.data.color;
                }
            }else{
                alert(result.error);
            }
        });
    }

    // 车型选择onchange事件
    $scope.onChangeType = function () {
        var type = $scope.order.vehicleType;
        for (var i = 0; i < $scope.dataList.length; i++) {
            if(type == $scope.dataList[i].vehicleId){
                $scope.vehicle = $scope.dataList[i];
                $scope.order.color = $scope.vehicle.colors[0];
                $scope.order.product = '0'
                break;
            }
        }
    }

    // 提交事件
    $scope.submit = function () {
        $http.post('/order/submit?phoneNum=' + userInfo, $scope.order).success(function(result){
            if(result.status == 'SUCCESS'){
                alert("提交成功!");
                $state.go('app.approval');
            }else{
                alert(result.error);
            }
        }).error(function () {
            alert('系统异常，请稍后再试');
        });
    }


}]);