<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" uri="/easyui-tags"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>

<head>
<base href="<%=basePath%>/" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title>车辆管理</title>
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap-theme.min.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bottomnav.css" />
<style type="text/css">
.form-control {
	height: 30px;
}

.query .query_btn {
	height: 30px;
	line-height: 30px;
}
</style>
</head>


<body>
	<!--头部查询开始 -->
	<header class="navbar-fixed-top">
		<div class="container query">
			<div ass="row">
				<div class="col-xs-3" style="padding-left: 0px; padding-right: 0px">
					<select id="carType" class="form-control" style="padding: 0px">
						<option value="">选择车型</option>
						<c:forEach items="${types }" var="row">
						<option value="${row[0] }" <c:if test="${carType eq row[0] }">selected="selected"</c:if> >${row[1] }</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-xs-7">
					<input type="text" class="form-control" placeholder="请输车牌号" value="${plateNo }"
						id="plateNo">
				</div>
				<div onclick="submit()" class="col-xs-2 query_btn" id="queryBtn">查询</div>
			</div>
		</div>
	</header>
	<!--头部查询结束 -->
	<!--客户列表开始 -->

	<div class="container details_list" style="margin-top:40px ">
		<div>车辆信息</div>
		<!--客户列表内容开始 -->
	
		<c:forEach items="${carInfoList }" var="row">
			<div class="tab">
			<div class="row details">
			<input type="hidden" value="${row.id }" name="id"/>
				<div class="col-xs-7 details_left">
					<p>车型：${row.type }</p>
					<p>配置：高端</p>
					<p>试驾次数：
						<c:if test="${row.driveTotal == null }">0</c:if>
						<c:if test="${row.driveTotal != null }">${row.driveTotal}</c:if>
					次</p>
				</div>
				<div class="col-xs-5 details_right">
					<p>车牌：<c:if test="${row.plateNo == null}"></c:if><c:if test="${row.plateNo != null}">${row.plateNo}</c:if></p>
					<p>使用日期：${row.saleYearTOString }</p>
					<p>车辆状态：
					<t:dictSelect field="carStatus"  id="carStatus" defaultVal="${row.status }" typeGroupCode="car_status" 	hasLabel="false"></t:dictSelect>
					<span style="margin-left: -5px;" id="carStatusSpan"></span>
					</p>
				</div>
			</div>
			</div>
		
		</c:forEach>
		
		
			<!--客户列表内容结束 -->
			<!--客户列表内容开始 -->

			
				<!--客户列表内容结束 -->
			
		
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
	</div>
</body>
<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
<br>
<script type="text/javascript">
		$("[name='carStatus']").hide();
		$(function(){
			$("[name='carStatus']").each(function(){
				$(this).next().html($(this).find("option:selected").text());
			});
		})
		function submit(){
			var carType = $("#carType").val();
			var plateNo = $("#plateNo").val();
			var url = "/mtestdrive/carInfoAction.action?index";
			url += "&plateNo="+plateNo;
			url += "&carType="+carType;
			location.href = url;
		}

			$(".details").click(function(){
				
			var id=	$(this).children("input[type='hidden']").val();
			window.location.href="/mtestdrive/carInfoAction.action?detail&id="+id;
			});
			
			
	</script>
</html>