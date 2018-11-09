'use strict';

/**
 * Config for the router
 */
angular.module('app')
    .run(
    [          '$rootScope', '$state', '$stateParams',
        function ($rootScope,   $state,   $stateParams) {
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
        }
    ]
)
    .config(
    [          '$stateProvider', '$urlRouterProvider', 'JQ_CONFIG', 'MODULE_CONFIG',
        function ($stateProvider,   $urlRouterProvider, JQ_CONFIG, MODULE_CONFIG) {


            $urlRouterProvider.otherwise("/access/signin");
            $stateProvider.state("app", {
                "abstract": !0,
                url: "/app",
                templateUrl:  "tpl/app.html",
                resolve: load(["js/controllers/blocks/nav.js"])
            }).state("access", {
                url: "/access",
                template: '<div ui-view class="fade-in-right-big smooth"></div>'
            }).state("access.signin", {
                url: "/signin",
                templateUrl: "tpl/purchase/hpl_purchase_signin.html",
                resolve: load(["toaster", "js/controllers/hplPurchaseSigninController.js"])
            }).state("app.entryway", {
                url: "/entryway",
                templateUrl: "tpl/purchase/hpl_purchase_home.html",
                resolve: load(["toaster", "js/controllers/hplPurchaseHomeController.js"])
            })
            .state("app.approval", {
                url: "/approval",
                templateUrl: "tpl/purchase/hpl_approval.html",
                resolve: load(["toaster", "js/controllers/hplApprovalController.js"])
            })
            .state("app.applyDetails", {
                url: "/applyDetails",
                templateUrl: "tpl/purchase/hpl_order_details.html",
                resolve: load(["toaster", "js/controllers/hplOrderDetailsController.js"])
            }).state("app.vehicleAttachment", {
                url: "/vehicleAttachment",
                templateUrl: "tpl/attachment/hpl_vehicle_attachment.html",
                resolve: load(["toaster", "js/directives/fileModel.js", "js/controllers/attachment/hplVehicleAttachmentController.js"])
            }).state("app.approvalResult", {
                url: "/approvalResult",
                templateUrl: "tpl/approvalresult/hpl_approval_result.html",
                resolve: load(["toaster", "js/controllers/approvalresult/hplApprovalResultController.js"])
            }).state("app.selectVehicle", {
                url: "/selectVehicle",
                templateUrl: "tpl/order/hpl_select_vehicle.html",
                resolve: load(["toaster", "js/controllers/order/hplSelectVehicleController.js"])
            }).state("app.newOrderRecord", {
                url: "/newOrderRecord",
                templateUrl: "tpl/neworderrecord/hpl_new_order_record.html",
                resolve: load(["toaster", "js/controllers/neworderrecord/hplNewOrderRecordController.js"])
            });



            function load(srcs, callback) {
                return {
                    deps: ['$ocLazyLoad', '$q',
                        function( $ocLazyLoad, $q ){
                            var deferred = $q.defer();
                            var promise  = false;
                            srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
                            if(!promise){
                                promise = deferred.promise;
                            }
                            angular.forEach(srcs, function(src) {
                                promise = promise.then( function(){
                                    if(JQ_CONFIG[src]){
                                        return $ocLazyLoad.load(JQ_CONFIG[src]);
                                    }
                                    angular.forEach(MODULE_CONFIG, function(module) {
                                        if( module.name == src){
                                            name = module.name;
                                        }else{
                                            name = src;
                                        }
                                    });
                                    return $ocLazyLoad.load(name);
                                });
                            });
                            deferred.resolve();
                            return callback ? promise.then(function(){ return callback(); }) : promise;
                        }]
                }
            }
        }
    ]
)
    .config(['$httpProvider', function($httpProvider) {
        //Handle 401 Error
        $httpProvider.interceptors.push(function($q, $injector) {
            return {
                response: function(response){
                    return response || $q.when(response);
                },
                responseError: function(rejection){
                    if(rejection.status === 401){
                        var state = $injector.get('$state');
                        var location = $injector.get('$location');
                        var rootScope = $injector.get('$rootScope');
                        rootScope.currentUrl = location.url().substring(1).replace("/",".");
                        state.go("access.signin");
                    }
                    return $q.reject(rejection);
                }
            };
        });
    }]);
