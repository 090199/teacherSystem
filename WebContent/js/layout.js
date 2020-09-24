let app = angular.module('layoutApp', ['ui.router']);
app.config(function ($stateProvider, $urlRouterProvider) {
    // 配置路由：第一个参数是路由的名称，第二个参数是路由的详细信息
    $stateProvider.state("welcome", {
        url: '/login',//地址栏显示的信息
        templateUrl: 'view/login.html',//使用显示模板
        controller: 'loginCtrl' //该模板的控制器名称
    });
    // 默认人情况下使用的路由地址
    $urlRouterProvider.otherwise("/login");
});
app.controller('layoutCtrl', function ($scope) {
    $scope.menus = [
        {
            id: 1,
            name: '菜单1',
            children: [
                {id: 10, name:'子菜单1'},
                {id: 11, name:'子菜单2'},
                {id: 12, name:'子菜单3'},
            ]
        }, {
            id: 2,
            name: '菜单2',
            children: [
                {id: 13, name:'子菜单3'},
                {id: 14, name:'子菜单4'}
            ]
        },
    ];
    $scope.changeMenuState = function (menu) {
        for(let i=0; i<$scope.menus.length; i++){
            if($scope.menus[i] == menu){
                // 当前菜单显示和影藏进行切换
                menu.selected = !menu.selected;
            } else {
                // 控制其他菜单不显示
                $scope.menus[i].selected = false;
            }
        }
    }
});
