/**
 * Created by duyusheng on 16/11/8.
 */
'use strict';
app.controller('hplApprovalController', ['$scope', '$http', '$modal', 'toaster', '$window','$cookies', '$state', function ($scope, $http, $modal, toaster, $window, $cookies, $state) {

    if($cookies.get('purchaseUser') == null || $cookies.get('purchaseUser') == ''){
        $state.go('access.signin');
    }
    var userInfo = $cookies.get('purchaseUser');
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.apply = {contact1Relationship: '家人',contact2Relationship: '家人'};
    $scope.info1 = {isOpen: false}
    $scope.info2 = {isOpen: false}
    $scope.info3 = {isOpen: false}
    $scope.info4 = {isOpen: false}
    $scope.info5 = {isOpen: false}
    $scope.user = {};
    $scope.user.maritalStatus = '0';

    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    $scope.oneAtATime = true;

    $scope.submit = function(){
        if($scope.apply.idCardName == null || $scope.apply.idCardNum == null){
           alert("提交失败，请检查身份证信息是否填写完整！");
           return
        }
        if($scope.apply.driveLicenceName == null || $scope.apply.quasiDriveType == null || $scope.apply.driveLicenceEffectiveTerm == null
           || $scope.apply.driveLicenceFn == null || $scope.apply.driveLicenceNum == null){
           alert("提交失败，请检查驾照信息是否填写完整！");
           return
        }
        if($scope.apply.mobile == null){
           alert("提交失败，请检查联系方式是否填写完整！");
           return
        }
        if($scope.apply.contact1Name == null || $scope.apply.contact1Mobile == null || $scope.apply.contact1Relationship == null
           || $scope.apply.contact2Name == null || $scope.apply.contact2Mobile == null || $scope.apply.contact2Relationship == null){
           alert("提交失败，请检查紧急联系人是否填写完整！");
           return
        }

        if( $scope.user.maritalStatus == '0' && ($scope.apply.mateName == null || $scope.apply.mateIdty == null || $scope.apply.mateMobile == null)){
          alert("提交失败，请检查配偶信息是否填写完整！");
          return
        }
        if($scope.user.maritalStatus == '1'){
            $scope.apply.mateName = null;
            $scope.apply.mateIdty = null;
            $scope.apply.mateMobile = null;
        }
               $http.post('/approval/autoFinancingPreApplySubmit?phoneNum=' + userInfo, $scope.apply).success(function(result){
                   if(result.status == 'SUCCESS'){
                       alert("申请提交成功!");
                        $state.go('app.approvalResult');
                   }else{
                       alert(result.error);
                   }
               }).error(function () {
                       alert('系统异常，请稍后再试');
               });
    };

}])
;