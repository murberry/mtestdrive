<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>/" />
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<title></title>
<link rel="stylesheet"
	href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<style type="text/css">
		html,body{
			width:100%;
			height:100%;
			margin:0;
			font-family:"微软雅黑";
		}
		
		.thumbnail>img{
			width:100% ;
			height:150px ;
		}
		.towSide .col-xs-6{
			padding:0 10px ;
		}
		a:active{ text-decoration: none;}
a:hover{ text-decoration: none;}
.Distance_img{margin-top:20px ;}
.Distance{margin-bottom:20px ;}
.Speed_title{border-bottom:1px solid #cccccc ;}
.Speed{float: right; margin-right:15PX ; margin-top:10px ;}
.speeds{margin-right:15px ;}
.speeds_color{color:red ;}
.footers{background:#0f1d3a ; text-align: center;}
.footers_nav{padding-top:20px; padding-bottom: 10px ;}
.footers_nav p{color:#FFF; font-size:12px ; line-height:0.7;}
table th,table td{
	font-size: 12px;
	
}
.wrap_share{
	position: fixed;
	z-index: 99999;
	right: 25px;
	top: 25px;
}
	
</style>
</head>
<body style="background: white;">

	<div class="container top">
		<div ass="row">
			<div class="col-xs-2">
				<a href="/mtestdrive/login.action?page" class="back">
					<span class="glyphicon glyphicon-chevron-left" style="color: #FFFFFF; line-height: 2.5em;"></span>
				</a>
			</div>
			<div class="col-xs-8">
				<h4>试驾报告</h4>
			</div>
		</div>
	</div>
	<header>
		<div class="container" style="padding-top: 10px;">
			<div class="wrap_share pull-right"
				style="position: fixed; top: 25px; right: 25px; z-index: 9999;">
				<div id="ckepop" class="share">
					<span class="jiathis_txt">分享到：</span> <a
						class="jiathis_button_weixin">微信</a>

					<script type="text/javascript"
						src="http://v3.jiathis.com/code/jia.js?uid=1" charset="utf-8"></script>
				</div>
			</div>
			<img src="images/heard_logo.png" width="100%" class="" />
		</div>

	</header>
	<div class=" container" style="margin-top: 3px;">

		<div class="row">
			<div class="col-xs-12">
				<p id="titleLabel" class="title_P"></p>
				<p id="subheadLabel" class="title_P"></p>
				<img id="carImg" src="" width="50%"
				onerror="this.src='images/onError.jpg'"
				/>
			</div>
			<%--div id="photo" class="col-xs-6">
				<img src="fileUpload.action?view&fileName=${driveRecodsVo.endPicPath }" width="100%"
					style="margin-bottom: 10px;"
					onerror="this.src='images/onError.jpg'"
					/>
					<!-- <img src="images/hezhao.JPG" width="100%"
					style="margin-bottom: 10px;" /> -->

			</div>
			--%>
		</div>

		<div class="row">
			<div class="col-xs-6">
				<p>客户姓名：${driveRecodsVo.customer.name }</p>

				<p>车&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型：<span id="carTypeSpan">${driveRecodsVo.car.type }</span></p>
				<p>
					试驾时间：
					<fmt:formatDate value="${driveRecodsVo.driveStartTime }" type="date" pattern="yyyy.MM.dd" />
				</p>
				<%--<p>试驾里程（km）：<c:if test="${driveRecodsVo.mileage == null}">0.00</c:if><c:if test="${driveRecodsVo.mileage != null}">${driveRecodsVo.mileage }</c:if></p>
				--%>
				<p>排&nbsp;&nbsp;量(L)：<span id="displacementSpan"></span></p>
			</div>
			<div class="col-xs-6" style="text-align: left; white-space: nowrap;">
				<p>销售顾问：${driveRecodsVo.salesman.name }</p>
				<p>车&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌：${driveRecodsVo.car.plateNo }</p>
				<input type="hidden" id="startTime" value="   <fmt:formatDate value="${driveRecodsVo.driveStartTime}" pattern="yyyy-MM-dd  HH:mm:ss" />"/>
				<input type="hidden" id="endTime" value="   <fmt:formatDate value="${driveRecodsVo.driveEndTime}" pattern="yyyy-MM-dd  HH:mm:ss" />"/>
				<p>持续时间：<span id="continueTime"></span></p>
				<p>RPM：<span id="rpmSpan"></span></p>
				<p>最高时速(km/h)：<span id="maxSpeed"></span></p>
			</div>
		</div>
	</div>
			<input type="hidden" name="routeId" id="routeId" value="${driveRecodsVo.routeId }"/>
			<input type="hidden" name="recodsId" id="recodsId" value="${driveRecodsVo.id }"/>
	<div class="container" style="height: 130px" id="container">
		<div id="allmap" style="height: 130px; width: 100%"></div>
	</div>
	<div class="container">
		<div class="Speed_title">
			<h6>你的表现</h6>
		</div>
		<div class="row Distance">
			<div  id="charts" style="width: 100%;height: 100px;"></div>
		</div>
	</div>
	
	<div class="footers container">
		<div class="footers_nav">
			<p>www.maserati.it</p>
			<p>联系电话：${driveRecodsVo.agency.telephone }</p>
			<p>${driveRecodsVo.agency.address }，${driveRecodsVo.agency.name }</p>
		</div>
	</div>
</body>
<script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="plug-in/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=4lb48bMFrQh09NzCuuul4W2e7YpAGrNG"></script>
<script type="text/javascript" src="plug-in/echarts/echarts.min.js"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<script type="text/javascript" src="mjs/resourceUtil.js"></script>
<script type="text/javascript" src="mjs/report.js"></script>
<script type="text/javascript">
	/* var startTime = new Date($("#startTime").val());
	var endTime = new Date($("#endTime").val());
	alert($("#startTime").val()); */

	//持续时间
	var timeDifference = "${timeDifference}";
	var data = timeDifference/1000/60;
	var minute = parseInt(data);
	var second = parseInt((timeDifference-minute*60*1000)/1000)
	$("#continueTime").html(minute+"'"+second+"'");


</script>
</html>
