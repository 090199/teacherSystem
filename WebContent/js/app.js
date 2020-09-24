let app = angular.module('app', ['ui.router']);
// 配置路由
app.config(function ($stateProvider, $urlRouterProvider) {
	// 登陆页面，欢迎页面
    $stateProvider.state("welcome",{
        url: '/',
        templateUrl: 'view/login.html',
        controller: 'loginCtrl'
    });
    // 注册页面
    $stateProvider.state("register",{
        url: '/',
        templateUrl: 'view/login.html',
        controller: 'registerCtrl'
    });

    
    // 管理首页
    $stateProvider.state("manage",{
        url: '/manage',
        templateUrl: 'view/manage.html',
        controller: 'manageCtrl'
    });
    
    
    // 二级路由：在路由manage下再有一个子路由，这个子路由被称之为二级路由
    
    // 登录成功之后的首页
    $stateProvider.state("manage.home",{
        url: '/home',
        templateUrl: 'view/manage1.html',
        controller: 'manage1Ctrl'
    });    
    // 添加
    $stateProvider.state("manage.addScore",{
        url: '/addScore',
        templateUrl: 'view/addScore.html',
        controller: 'addScoreCtrl'
    });
    // 查询
    $stateProvider.state("manage.score",{
        url: '/score',
        templateUrl: 'view/score.html',
        controller: 'scoreCtrl'
    });
    // 修改
    $stateProvider.state("manage.updateScore",{
        url: '/updateScore',
        templateUrl: 'view/updateScore.html',
        controller: 'updateScoreCtrl',
        // 定义路由跳转过来时，需要接收的参数
        params:{
        	score: null
        }
    });
    
    // 班级管理：添加学生
    $stateProvider.state("manage.addStudent",{
        url: '/addStudent',
        templateUrl: 'view/addStudent.html',
        controller: 'addStudentCtrl',
    });
    
    // 班级管理：查询学生
    $stateProvider.state("manage.searchStu",{
        url: '/searchStu',
        templateUrl: 'view/searchStu.html',
        controller: 'searchStuCtrl',
    });
    
    

    
    
//////////////////////////////////////////////////////////////////////////////
   
    //添加课程
    $stateProvider.state("manage.addCourse",{
        url: '/addScore',
        templateUrl: 'view/addCourse.html',
        controller: 'addCourseCtrl'
    });
    // 查询课程
    $stateProvider.state("manage.manageCourse",{
        url: '/manageCourse',
        templateUrl: 'view/manageCourse.html',
        controller: 'manageCourseCtrl'
    });
    // 修改课程
    $stateProvider.state("manage.updateCourse",{
        url: '/updateCourse',
        templateUrl: 'view/updateCourse.html',
        controller: 'updateCourseCtrl',
        params: {
            course: null
        }
    });
    $urlRouterProvider.otherwise('/');
});

// 跳转路由需要一个状态服务$state，只需要在function的参数列表中添加即可使用
app.controller("loginCtrl", function ($scope, $state,$http) {
	$scope.isLogin = true,
    $scope.user = {
        username : '',
        password: '',
        confirm: ''
    };
    $scope.login = function () {
        $http({
            url: 'login', //发送至后端的处理地址
            method: 'post', //发送POST请求
            params: $scope.user //发送的数据：账号和密码
        }).then(function (response) {//响应结果存储在response对象的data属性中
            if("登录成功" == response.data){
                // 跳转至显示主页面的路由
                $state.go("manage.home");
//            	$state.go("manage")
            } else {
                alert(response.data);
            }
        });
    }


        $scope.clsList = [{
            id: '',
            name: '请选择'
        }];
        $scope.register = function () {
        	console.log("ok")
            if($scope.user.username == ''){
                alert("请输入用户名！")
                return;
            } else if($scope.user.password == ''){
            	alert("请输入密码！")
            	return
            }else if($scope.user.confirm != $scope.user.password){
            	alert("两次密码输入不一致！")
            	return
            }
            
        	
            //删除无用属性
//            delete $scope.user.confirm;
            $http({
                url: 'createUser',
                method: 'post',
                params: $scope.user
            }).then(function (resp) {
            	
                if(resp.data == "注册成功"){
                    //注册成功
                    $state.go("login");
                    alert("注册成功！")
                } else if(resp.data == "账号已被注册"){
                    alert('该用户名已被注册');
                }else if(resp.data == "两次密码输入不一致"){
                	alert("两次密码输入不一致")
                }
            })
        }

    
    // 修改登录或者注册状态
    $scope.changeType = function(){
    	$scope.isLogin = !$scope.isLogin
    }
});
app.controller("registerCtrl", function ($scope, $http) {
    $scope.userReg = {
        username: '',
        password: '',
        confirm: '',
    };

    $scope.clsList = [{
        id: '',
        name: '请选择'
    }];
    $scope.register = function () {
    	if($scope.userReg.password == ''){
            alert("请输入密码")
            return;
        }else if($scope.userReg.confirm != $scope.userReg.password){
            alert('两次密码输入不一致');
            return;
        }
        //删除无用属性
        delete $scope.userReg.confirm;
        $http({
            url: 'user',
            method: 'post',
            params: $scope.user
        }).then(function (resp) {
            if(resp.data == 1){
                //注册成功
                $state.go("login");
            } else if(resp.data == -1){
            	alert("该用户已被注册！")
            }
        })
    }

    $scope.init = function () {//初始化页面后获取班级信息
        $http({
            url: 'cls',
            method: 'get'
        }).then(resp=>{
            $scope.clsList = $scope.clsList.concat(resp.data);
        });
    }
    $scope.init();
});
app.controller("manageCtrl", function ($scope, $http) {
    $scope.menus = [];
    $scope.changeMenuState = function (menu) {
        for(let i=0; i<$scope.menus.length; i++){
            if($scope.menus[i] == menu){
                // 当前菜单显示和影藏进行切换
                menu.selected = !menu.selected;
            }
        }
    }

    $scope.getMenus = function () {
        $http({
            url: 'getUserMenu',
            method: 'get'
        }).then(function (response) {
        	console.log(response)
            // 从传回的JSON格式数据中将menus取出来
            $scope.menus = response.data.menus;
        });
    }
    // 调用初始化函数获取菜单信息
    $scope.getMenus();
    $scope.pageInfo = "主页面";
    $scope.changeMenuState = function (menu) {
    	console.log(menu.name)
        for(let i=0; i<$scope.menus.length; i++){
            if($scope.menus[i] == menu){
                // 当前菜单显示和影藏进行切换
                menu.selected = !menu.selected;
            }
        }
    }
    $scope.selectedChildMenu = function (parent, child) {
        parent.children.forEach(m=>m.selected = false);
        child.selected = true;
        $scope.pageInfo = parent.name + "   >>    " + child.name;
    }
});



// 添加课程
app.controller("addCourseCtrl", function ($scope,$http) {

    $scope.course = {
        number: '',
        name: '',
        times: '',
        score: ''
    };

    $scope.addCourse = function () {
    	console.log($scope.course)
        $http({
            url: 'course',
            method: 'post', //POST GET PUT DELETE
            params: $scope.course
        }).then(function (response) {
        	console.log(response)
            if(response.data == 1){
                alert("添加成功");
            } else {
                alert("添加失败");
            }
        	$scope.course = {
                    number: '',
                    name: '',
                    times: '',
                    score: ''
                };
        });
    }

    $scope.reset = function () {
        $scope.course = {
            number: '',
            name: '',
            times: '',
            score: ''
        };
    }
});

// 查询课程
app.controller("manageCourseCtrl", function ($scope, $http) {
	   
    $scope.search = {
        number: '',
        name: '',
        score: ''
    }
    $scope.courses = [];
    $scope.searchCourses = function () {
    	console.log($scope.search)
        $http({
        	url: 'course',
        	method: 'get',
        	params: $scope.search
        }).then(function(response){
        	console.log(response)
        	$scope.courses = response.data.courses;
        })
    }
    $scope.searchCourses();
    
    $scope.deleteCourse = function(number){
    	let result = confirm("确定删除该条成绩信息吗？")
    	if(!result) return
    	$http({
    		url: 'course',
    		method: 'delete',
    		params: {
    			number: number
    		}
    	}).then(function(response){
    		if(response.data == 1){
    			alert("删除成功！")
    			$scope.searchCourses()// 数据刷新
    		}else{
    			alert("删除失败！")
    		}
    	})
    }
});




// 修改课程
app.controller("updateCourseCtrl", function ($scope, $stateParams,$http) {
    $scope.course = $stateParams.course == null ? { } : $stateParams.course;

    $scope.unusable = (Object.keys($scope.course).length == 0);

    $scope.updateCourse = function () {
		$http({
			url: 'course',
			method: 'put',
			params: $scope.course
		}).then(function(response){
			console.log(response)
			if(response.data >= 1){
				alert("修改成功！")
			}else{
				alert("修改失败！")
			}
		})
    }

    $scope.goBack = function () {
        window.history.back();
    }
});
//app.controller("addClassCtrl", function ($scope) {
//
//});
//app.controller("manageClassCtrl", function ($scope) {
//
//});
//app.controller("updateClassCtrl", function ($scope) {
//
//});




app.controller("addScoreCtrl", function ($scope, $http) {
    $scope.score = {
        username: '',
        course: '',
        score: ''
    };
    $scope.addScore = function () {
        $http({
            url: 'score',
            method: 'post', //POST GET PUT DELETE
            params: $scope.score
        }).then(function (response) {
            if(response.data == 1){
                alert("添加成功");
            } else {
                alert("添加失败");
            }
        });
    }
});

app.controller("scoreCtrl", function ($scope, $http) {
    $scope.search = {
        username: '',
        from: '',
        to: ''
    };
    $scope.scores = []; //默认情况下查询的成绩为空数组

    $scope.searchScore = function () {
    	console.log($scope.search)
        $http({
            url: 'score',
            method: 'get',
            params: $scope.search
        }).then(function (response) {
            $scope.scores = response.data.scores;
        });
    }
    
    $scope.deleteScore = function(id){
    	let result = confirm("确定删除该条成绩信息吗？")
    	if(!result) return
    	$http({
    		url: 'score',
    		method: 'delete',
    		params: {
    			id: id
    		}
    	}).then(function(response){
    		if(response.data == 1){
    			alert("删除成功！")
    			$scope.searchScore()// 数据刷新
    		}else{
    			alert("删除失败！")
    		}
    	})
    }
});

app.controller("updateScoreCtrl",function($scope,$stateParams,$http){
	$scope.score = $stateParams.score == null ? {} : $stateParams.score;
	$scope.updateScore = function(){
		console.log($scope.score)
		$http({
			url: 'score',
			method: 'put',
			params: $scope.score
		}).then(function(response){
			console.log(response)
			if(response.data == 1){
				alert("修改成功！")
			}else{
				alert("修改失败！")
			}
		})
	}
})

app.controller("addStudentCtrl",function($scope,$stateParams,$http){
	$scope.stu = {
			id:'',
			name:'',
			className:''
	}
	$scope.addStudent = function(){
	    $http({
	        url: 'student',
	        method: 'post', //POST GET PUT DELETE
	        params: $scope.stu
	    }).then(function (response) {
	        if(response.data == 1){
	            alert("添加成功");
	        } else {
	            alert("添加失败");
	        }
	    });
	}

})

app.controller("searchStuCtrl",function($scope,$stateParams,$http){
	$scope.myclass = {
			className: ''
	}
	$scope.classes = []; //默认情况下查询的成绩为空数组
	$scope.searchStudent = function(){
		 $http({
	            url: 'student',
	            method: 'get',
	            params: $scope.myClass
	        }).then(function (response) {
	            $scope.classes = response.data.classes;
	        });
	}
})

app.controller("manage1Ctrl",function(){
	
})