<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>

<head>
<base href="<%=basePath%>/" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title>车况检查</title>
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap-theme.min.css" />
<link rel="stylesheet" href="css/mobiscroll.css" />
<link rel="stylesheet" href="css/mobiscroll_002.css" />
<link rel="stylesheet" href="css/mobiscroll_003.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bottomnav.css" />
</head>

<body class="bg" >
	<!--title开始 -->
	<header>
		<div class="container top">
			<div ass="row">
				<div class="col-xs-2">
					<a href="/mtestdrive/driveRecodsAction.action?index" class="back ">
					 <span class="glyphicon glyphicon-chevron-left"></span>
					</a>

				</div>
				<div class="col-xs-8">
					<div class="change_password">车况检查</div>
				</div>
			</div>
		</div>
	</header>
	<!--title结束 -->
	<section>
		<div class="container">
			<!--检查开始  -->
			<div class="agreements">
			<input type="hidden" name="id" value="${carInfo.id }">
				<div class="check">
					<div class="vehicle_condition">车身清洁</div>
					<div class="checkbox pull-right">
						<input id="clean" type="checkbox">
					</div>
				</div>
				<div class="check">
					<div class="vehicle_condition">油量确认</div>
					<div class="checkbox pull-right">
						<input id="petrol" type="checkbox">
					</div>
				</div>
				<div class="check">
					<div class="vehicle_condition">车内准备(车内清洁、饮用水、音乐等)</div>
					<div class="checkbox pull-right">
						<input id="readey" type="checkbox">
					</div>
				</div>

			</div>
			<!--检查结束 -->
			<!--车辆位置开始 -->
			<!-- <div style="overflow: scroll; height: 100px"> -->
			<div class="Vehicle_position">
				<a href="#"> <span class="glyphicon glyphicon-map-marker"></span>
					车辆位置
				</a>
			</div>
			<div id="currMap" style="height: 120px;max-height: 200px"></div>
			<!--车辆位置结束 -->
			<!--完成按钮开始  -->
			<div style="margin-bottom: 60px">
			
			<a href="javascript:void(0);">
			 <button onclick="check()" type="button" class="btn btn-primary  btn-block complete">车况确认</button>
			</a>
			</div>
			<!--完成按钮结束  -->
		<!--</div>  -->
	</section>
	</div>
	<!--底部导航开始  -->
	<footer class="navbar-fixed-bottom text-center ">
		<a href="/mtestdrive/login.action?page">
			<div class="col-xs-3">
				<span class="glyphicon glyphicon-home"></span>
				<h6>首页</h6>
			</div>
		</a> <a href="/mtestdrive/driveRecodsAction.action?index">
			<div class="col-xs-3">
				<span class="glyphicon glyphicon-credit-card "></span>
				<h6>试驾管理</h6>
			</div>
		</a> <a href="/mtestdrive/customerInfoAction.action?index">
			<div class="col-xs-3">
				<span class="glyphicon glyphicon-user"></span>
				<h6>我的客户</h6>
			</div>
		</a> <a href="/mtestdrive/carInfoAction.action?index">
			<div class="col-xs-3 nenuActi">
				<span class="glyphicon glyphicon-list"></span>
				<h6>车辆管理</h6>
			</div>
		</a>
	</footer>
	<!--底部导航结束-->
</body>
<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=4lb48bMFrQh09NzCuuul4W2e7YpAGrNG"></script>
<script type="text/javascript" src="js/mobiscroll_002.js"></script>
<script type="text/javascript" src="js/mobiscroll.js"></script>
<script type="text/javascript" src="js/mobiscroll_003.js"></script>
<script type="text/javascript" src="js/customer.js"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
	$.ajax({
	    url:'obdInfoAction.action?getLocationById&id=${carInfo.obdId }',
	    type:'GET', //GET
	    timeout:5000,    //超时时间
	    dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
	    success:function(data){
	    	console.log(JSON.stringify(data));
	    	if(data.obj.lon != null || data.obj.lat != null){
				
				var map = new BMap.Map("currMap");
	 			var ggPoint = new BMap.Point(data.obj.lon, data.obj.lat);
	 			map.enableScrollWheelZoom(true);
	 		    //坐标转换完之后的回调函数
	 		    translateCallback = function (data){
	 		      if(data.status === 0) {
	 		    	    var new_point = new BMap.Point(data.points[0].lng,data.points[0].lat);  
	 		    	    map.centerAndZoom(new_point,11);
	 		 			map.enableScrollWheelZoom(true);
	 		 		    var marker = new BMap.Marker(new_point);  // 创建标注
	 		 		    map.addOverlay(marker);              // 将标注添加到地图中
	 		 		    map.panTo(new_point);    
	 		 		    map.addControl(new BMap.ScaleControl()); // 添加比例尺控件
	 		      }
	 		    }
	 			var convertor = new BMap.Convertor();
		        var pointArr = [];
		        pointArr.push(ggPoint);
		        convertor.translate(pointArr, 1, 5, translateCallback)
				
	    	}else{
	    		$("#currMap").html("暂无位置信息");
	    	}
	    },
	    error:function(xhr,textStatus){
	    }
	})	
})

function check(){
	var clean = $("input[id='clean']:checked").val();
	if(clean!="on"){
		layer.msg('请完成车况确认！');
		return false;
	}
	var petrol = $("input[id='petrol']:checked").val();
	if(petrol!="on"){
		layer.msg('请完成车况确认！');
		return false;
	}
	var readey = $("input[id='readey']:checked").val();
	if(readey!="on"){
		layer.msg('请完成车况确认！');
		return false;
	}
	window.location.href="/mtestdrive/carInfoAction.action?status&id="+'${driveRecods.id }';
}
</script>

</html>