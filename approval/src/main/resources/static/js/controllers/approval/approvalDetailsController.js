/**
 * Created by yuanzhenxia on 2017/7/19
 */
'use strict';
app.controller('approvalDetailsController', ['$scope', '$http', '$modal', 'toaster', '$window','$cookies', '$state','$localStorage', function ($scope, $http, $modal, toaster, $window, $cookies, $state,$localStorage) {

    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.user = {};
    $scope.apply = {contact1Relationship: '家人',contact2Relationship: '家人'};
    $scope.info1 = {isOpen: false}
    $scope.info2 = {isOpen: false}
    $scope.info3 = {isOpen: false}
    $scope.user.maritalStatus = '0';

    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };
    $scope.oneAtATime = true;

    $scope.submit = function(){
        if($scope.apply.applicantName == null || $scope.apply.idCardNum == null
            || $scope.apply.applicantConmany == null || $scope.apply.yearlySalary == null || $scope.apply.homeAddress == null){
            alert("提交失败，请检查申请人信息是否填写完整！");
            return
        }
        if($scope.apply.idCardNum.length != 15 && $scope.apply.idCardNum.length !=18){
            alert("提交失败，请输入有效的申请人身份证信息！");
            return
        }
        if($scope.apply.applicantPhoneNum == undefined ){
            alert("提交失败，请输入有效的申请人电话号码！");
            return
        }
        if($scope.apply.contact1Name == null ||  $scope.apply.contact1Relationship == null
            || $scope.apply.contact2Name == null || $scope.apply.contact2Relationship == null){
            alert("提交失败，请检查紧急联系人是否填写完整！");
            return
        }
        if($scope.apply.contact1Mobile== undefined){
            alert("提交失败，请输入有效的联系人1的电话号码！");
            return
        }
        if($scope.apply.contact2Mobile== undefined){
            alert("提交失败，请输入有效的联系人2的电话号码！");
            return
        }
        if( $scope.user.maritalStatus == '0' && ($scope.apply.mateName == null || $scope.apply.mateIdty == null )){
            alert("提交失败，请检查配偶信息是否填写完整！");
            return
        }
        if($scope.user.maritalStatus == '0' && ($scope.apply.mateIdty.length!=15 && $scope.apply.mateIdty.length!=18)){
            alert("提交失败，请输入有效的配偶省份证信息！");
            return
        }
        if($scope.user.maritalStatus == '0' && $scope.apply.mateMobile== undefined){
            alert("提交失败，请输入有效的配偶的电话号码！");
            return
        }
        if($scope.user.maritalStatus == '1'){
            $scope.apply.mateName = null;
            $scope.apply.mateIdty = null;
            $scope.apply.mateMobile = null;
        }
        $scope.$emit("BUSY");
        $http.post('/approval/autoFinancingPreApplySubmit', $scope.apply).success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.$emit("NOTBUSY");
                alert("申请提交成功!");
                $localStorage.applyNum = result.error;
                $state.go('app.approvalResult');
            }else{
                $scope.$emit("NOTBUSY");
                alert(result.error);
            }
        }).error(function () {
            $scope.$emit("NOTBUSY");
            alert('系统异常，请稍后再试');
        });
    };

}])
;