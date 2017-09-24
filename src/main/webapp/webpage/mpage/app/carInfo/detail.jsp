<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>

<head>
<base href="<%=basePath%>/" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title>车辆安排</title>
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/maser.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bottomnav.css" />
</head>
<body>
	<header style="margin-bottom: 15px;">
		<div class="container top">
			<div ass="row">
				<div class="col-xs-2">
					<a href="/mtestdrive/carInfoAction.action?index" class="back "><span
						class="glyphicon glyphicon-chevron-left"></span></a>

				</div>
				<div class="col-xs-8">
					<h4>车辆安排</h4>
				</div>
			</div>
		</div>
	</header>

	<div class="container"
		style="padding-top: 10px; background-color: #FFFFFF; border: 1px solid #F1F1F1;">
		<div class="col-xs-6 container">
			<p>车型：${carInfo.type }</p>
		</div>
		<div class="col-xs-6">
			<p>车牌：${carInfo.plateNo }</p>
		</div>
	</div>
	<div class="container-fluid" style="background-color: #FFFFFF;">
		<c:forEach items="${carArrangeVoList }" var="row">
			<div class="container"
			style="border-bottom: 4px solid #F1F1F1; padding-top: 8px;">
			<p>时间：<fmt:formatDate value="${row.startTime}" type="both" pattern="yyyy.MM.dd HH:mm"/><fmt:formatDate value="${row.endTime}" type="both" pattern=" -HH:mm"/>  </p>
			<p>安排：<c:if test="${row.status==1 }">预约</c:if>
				   <c:if test="${row.status==2 }">报备</c:if></p>
		</div>
		</c:forEach>
		
	</div>

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
</html>
