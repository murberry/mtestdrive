<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	    <base href="<%=basePath%>/" />
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<%@include file="/context/ico.jsp"%>
		<title></title>
		<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/bottomnav.css" />
	</head>

	<body class="bg">
		<!--顶部标题开始 -->
		<header>
			<div class="container top">
				<div ass="row">
					<div class="col-xs-2">
						<a href="Verification_code.html" class="back"><span class="glyphicon glyphicon-chevron-left"></span></a>

					</div>
					<div class="col-xs-8">
						<div class="change_password">重置密码</div>
					</div>
				</div>
			</div>
		</header>
		<!--顶部标题结束 -->
		<!--头部logo开始 -->
		<!--<div class="container login" >
				<img src="images/logo.jpg" >
		</div>-->
		<div class="container login ">
			<img src="images/logo.jpg" class="img-responsive">
		</div>
		<!--头部logo结束 -->
		<!--用户名和密码开始 -->
		<section class="container">
			<form onsubmit="return false;" id="usrInfoForm">
				<div class="form-group">
					<input type="hidden" value="${param.mobile }" name="phone"/>
					<input type="password" class="form-control" name="password" placeholder="请输入新密码">
				</div>
				<div class="form-group">
					<input type="password" class="form-control" name="repPassword" placeholder="请再次输入新密码">
				</div>

				<a href="javascript:void(0);">
					<div type="submit" id="submitBtn" class="btn btn-primary  btn-block confirm">确定</div>
				</a>
			</form>
		</section>
		<!--用户名和密码结束 -->
	</body>
	<script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="plug-in/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="plug-in/layer/layer.js"></script>
	<script type="text/javascript" src="mjs/setNewPassword.js"></script>
	<script type="text/javascript">
		var mobile = "${param.mobile }";
	</script>
</html>