app.controller('hplVehicleAttachmentController', ['toaster', '$scope', '$http', '$state', '$rootScope', '$location', '$cookies', '$localStorage', function (toaster, $scope, $http, $state, $rootScope, $location, $cookies, $localStorage) {

    $scope.uploaded1 = false;
    $scope.notUpload1 = true;
    $scope.uploaded2 = false;
    $scope.notUpload2 = true;
    $scope.uploaded3 = false;
    $scope.notUpload3 = true;
    $scope.uploaded4 = false;
    $scope.notUpload4 = true;
    $scope.uploaded5 = false;
    $scope.notUpload5 = true;
    $scope.uploaded6 = false;
    $scope.notUpload6 = true;
    $scope.count = 1;
    $scope.attachmentDto = {};
    $scope.idCardFaceUrl = '';
    $scope.idCardBackUrl = '';
    $scope.creditLetterUrl = '';
    $scope.drivingLicenseFaceUrl = '';
    $scope.drivingLicenseBackUrl = '';
    $scope.jobCertificateUrl = '';
    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.apply = function () {
        $state.go('app.vehicleAttachment');
    };
    //var userInfo = '18055313782';
    var userInfo = $cookies.get('purchaseUser');
    if($cookies.get('purchaseUser') == null || $cookies.get('purchaseUser') == ''){
        $state.go('access.signin');
    }
    $scope.pop = function (type, title, text) {
        toaster.pop(type, '', text);
    };

    $scope.submit = function () {
        if($scope.count == 1){
            alert('请上传附件！');
            return;
        }else if($scope.count <= 6){
            alert('请完善附件信息后提交！');
            return;
        }
        $scope.attachmentDto.idCardFaceUrl = $scope.idCardFaceUrl;
        $scope.attachmentDto.idCardBackUrl = $scope.idCardBackUrl;
        $scope.attachmentDto.creditLetterUrl = $scope.creditLetterUrl;
        $scope.attachmentDto.drivingLicenseFaceUrl = $scope.drivingLicenseFaceUrl;
        $scope.attachmentDto.drivingLicenseBackUrl = $scope.drivingLicenseBackUrl;
        $scope.attachmentDto.jobCertificateUrl = $scope.jobCertificateUrl;
        var url = '/order/ordersubmit?phoneNum=' + userInfo;
        $http.post(url, $scope.attachmentDto).success(function (result) {
            if (result.status == 'SUCCESS') {
                alert('提交成功，请耐心等待审核结果');
                $state.go('app.applyDetails');
            } else {
                alert('提交失败！');
            }
        }).error(function () {
            alert('系统异常，请稍后再试');
        });
    };


    $scope.openFile = function (sort) {
        if (sort == 1) {
            document.getElementById("idCardFace").click();
        } else if (sort == 2) {
            document.getElementById("idCardBack").click();
        } else if (sort == 3) {
            document.getElementById("creditLetter").click();
        } else if (sort == 4) {
            document.getElementById("drivingLicenseFace").click();
        } else if (sort == 5) {
            document.getElementById("drivingLicenseBack").click();
        } else if (sort == 6) {
            document.getElementById("jobCertificate").click();
        }
    };

    $scope.uploadFile = function (sort) {
        var file = new FormData();
        var files = null;
        if(sort == 1){
            files = document.getElementById('idCardFace').files;
        }else if(sort == 2){
            files = document.getElementById('idCardBack').files;
        }else if(sort == 3){
            files = document.getElementById('creditLetter').files;
        }else if(sort == 4){
            files = document.getElementById('drivingLicenseFace').files;
        }else if(sort == 5){
            files = document.getElementById('drivingLicenseBack').files;
        }else if(sort == 6){
            files = document.getElementById('jobCertificate').files;
        }
        file.append("file", files[0]);
        $http.post('/files?type=attachment', file, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function (result) {
            if (result.status == 'SUCCESS') {
                if(sort == 1){
                    $scope.uploaded1 = true;
                    $scope.notUpload1 = false;
                    $scope.idCardFaceUrl = result.data.url;
                }else if(sort == 2){
                    $scope.uploaded2 = true;
                    $scope.notUpload2 = false;
                    $scope.idCardBackUrl = result.data.url;
                }else if(sort == 3){
                    $scope.uploaded3 = true;
                    $scope.notUpload3 = false;
                    $scope.creditLetterUrl = result.data.url;
                }else if(sort == 4){
                    $scope.uploaded4 = true;
                    $scope.notUpload4 = false;
                    $scope.drivingLicenseFaceUrl = result.data.url;
                }else if(sort == 5){
                    $scope.uploaded5 = true;
                    $scope.notUpload5 = false;
                    $scope.drivingLicenseBackUrl = result.data.url;
                }else if(sort == 6){
                    $scope.uploaded6 = true;
                    $scope.notUpload6 = false;
                    $scope.jobCertificateUrl = result.data.url;
                }
                $scope.count = $scope.count + 1;
            }
        }).error(function () {
            alert("上传文件异常，请稍后再试");
        });
    };

}]);