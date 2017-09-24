<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>

<head>
<base href="<%=basePath%>/" />
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title>试驾监控</title>
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/maser.css" />
<link rel="stylesheet" href="css/bottomnav.css" />

</head>

<body style="overflow: scroll;">
	<section>
	<input type="hidden" id="driveId" name="driveId" value="${driveRecods.id }">
		<div class="container-fluid theme_color top_bom_pad text-center">
			<div class="col-xs-2">
				<a href="/mtestdrive/driveRecodsAction.action?index" class="back ">
				 <span class="glyphicon glyphicon-chevron-left" style="color: #FFFFFF; line-height: 2.5em;"></span>
				</a>
			</div>
			<h4 class="font_color">试驾车位置</h4>
		</div>
	</section>
	<!--
        	作者：offline
        	时间：2017-03-17
        	描述：这里是现实当前的位置
       -->
        
	<div class="container-fluid" id="currMap" style="height: 235px;"></div>
  
	<div class="container tijiao" style="margin-bottom: 60px;">
		<button type="button" class="btn btn-primary btn-block theme_color" id="ok">完成</button>
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
</body>
<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=4lb48bMFrQh09NzCuuul4W2e7YpAGrNG"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<script type="text/javascript">
	$("#ok").click(function(){
		var driveId = $("#driveId").val();
		layer.confirm('您确定当前试驾已完成吗？',{
			btn: ['确定','取消']
		},function(){
			window.location.href="/mtestdrive/carInfoAction.action?status5&id="+driveId;
		},function(){
			
		});
		
	});
	var map = null;
	$(function(){
		$("#currMap").width($(window).width());
		showCurrentLocation();
		setInterval(showCurrentLocation, 10000);
		map = new BMap.Map("currMap");
	})
	//显示车辆当前位置
	function showCurrentLocation(){
		$.ajax({
		    url:'obdInfoAction.action?getLocationById&id=${carInfo.obdId }',
		    type:'GET', //GET
		    timeout:5000,    //超时时间
		    dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
		    success:function(data){
		    	if(data.obj.lon != null || data.obj.lat != null){
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
	}		
</script>
</html>