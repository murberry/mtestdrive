<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>/" />
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title>完成办理</title>
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/maser.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bottomnav.css" />

</head>
<body>
	<section style="margin-bottom: 10px;">
		<div class="container-fluid theme_color text-center">
			<div ass="row">
				<div class="col-xs-2" style="line-height: 39px;">
					<a href="javascript:history.back(-1);" class="back font_color">
					 <span class="glyphicon glyphicon-chevron-left"></span>
					</a>
				</div>
				<div class="col-xs-8">
					<h4 class="font_color">手续办理</h4>
				</div>
			</div>
		</div>
	</section>
	<div class="container-fluid" style="background: #FFF">
		<div id="waychose">
			<div id="myCarousel" class="carousel slide">
				<!-- Indicators -->
				<!-- <ol class="carousel-indicators">
					<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
					<li data-target="#myCarousel" data-slide-to="1"></li>
				</ol> -->

				<!-- Wrapper for slides -->
				<div class="carousel-inner" role="listbox" id="routeListDiv">
					<div class="item active">
						<div id="allmap" style="height: 235px"></div>
						<div class="carousel-caption">
							<p class="fontBlack" id="routeInfo">路线1</p>
						</div>
					</div>
				</div>

				<!-- Controls -->
				<a id="prevRoute" class="left carousel-control" href="#myCarousel"
					role="button" data-slide="prev"> <span
					class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
					<span class="sr-only">Previous</span>
				</a> <a id="nextRoute" class="right carousel-control" href="#myCarousel"
					role="button" data-slide="next"> <span
					class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					<span class="sr-only">Next</span>
				</a>
			</div>
		</div>
		<div class="container tijiao">
			<input type="hidden" name="id" id="id" value="${param.id }"/>
			<input type="hidden" name="routeId" id="routeId" />
			<input type="hidden" name="routeId" id="routeId" />
			<input type="hidden" name="routeId" id="routeId" />
			<button id="showdiv" type="button" class="btn btn-primary btn-block theme_color">完成办理</button>
		</div>
		<div class="container tijiao">
			<button style="display: none;" id="beginTestDrive" type="button" class="btn btn-primary btn-block theme_color">开始试驾</button>
		</div>

	</div>



	<!--
   描述： 菜单栏
    -->
	<footer class="navbar-fixed-bottom text-center ">
		<a href="/mtestdrive/login.action?page">
			<div class="col-xs-3">
				<span class="glyphicon glyphicon-home"></span>
				<h6>首页</h6>
			</div>
		</a> <a href="/mtestdrive/driveRecodsAction.action?index">
			<div class="col-xs-3 nenuActi">
				<span class="glyphicon glyphicon-credit-card "></span>
				<h6>试驾管理</h6>
			</div>
		</a> <a href="/mtestdrive/customerInfoAction.action?index">
			<div class="col-xs-3">
				<span class="glyphicon glyphicon-user"></span>
				<h6>我的客户</h6>
			</div>
		</a> <a href="/mtestdrive/carInfoAction.action?index">
			<div class="col-xs-3">
				<span class="glyphicon glyphicon-list"></span>
				<h6>车辆管理</h6>
			</div>
		</a>
	</footer>

</body>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=4lb48bMFrQh09NzCuuul4W2e7YpAGrNG"></script>
<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<script type="text/javascript">
	var currentIndex = 0;//当前选中的路线下标
	$(function() {
		//$("#yinchangdiv").hide();
		$("#showdiv").click(function() {
			var drive = {
				"id" : $("#id").val(),
				"routeId" : $("#routeId").val(),
				"testDriveContractPicPath" : "${param.picPath1 }"
			};
			$.ajax({
				url:"/mtestdrive/driveRecodsAction.action?management&drivingLicensePicPath=${param.picPath2 }",
				type:"post",
				data:JSON.stringify(drive),
				dataType:"json",
				contentType:'application/json',
				success:function(data){
					$("#beginTestDrive").show(1000);
					$("#showdiv").parent().hide();
				}
			});

            $(this).unbind("click");//防止重复点击

		});
		$(".carousel").carousel('pause');//停止自动轮播
		$("#beginTestDrive").on("click", function() {
			var id = $("#id").val();
			location.href = "/mtestdrive/carInfoAction.action?monitor&driveStart=1&id="+id;
		});
		$('#myCarousel').on('slid.bs.carousel', function() {
			$(".carousel").carousel('pause');//停止自动轮播
			$(".carousel-indicators>li").each(function(index, domEle) {
				if ($(domEle).hasClass("active")) {
					currentIndex = index;
				}
			});

		})
		$("#routeId").val(gathers[0].routeId);
	});
</script>
<script type="text/javascript" src="mjs/route.js"></script>
<script>
	var gathers = null;
	//获取所有路线
	$.ajax({
		url : 'routeInfoAction.action?getRoutes',
		type : 'GET',
		async : false,
		timeout : 5000, //超时时间
		dataType : 'json',
		success : function(data) {
			gathers = data.obj;
		},
		error : function(xhr, textStatus) {

		}
	})
	//页面加载完成展示第一条路线
	printRoute(0);
</script>

</html>
