<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	    <base href="<%=basePath%>/" />
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<%@include file="/context/ico.jsp"%>
		<title>发送验证码</title>
		<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
		<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap-theme.min.css" />
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/bottomnav.css" />
	</head>

	<body class="bg">
		<!--title开始 -->
		<header>
			<div class="container top">
				<div ass="row">
					<div class="col-xs-2">
						<a href="login.action?page" class="back "><span class="glyphicon glyphicon-chevron-left"></span></a>
					</div>
					<div class="col-xs-8">
						<div class="change_password">找回密码</div>
					</div>
				</div>
			</div>
		</header>
		<!--title结束 -->
		<!--logo开始 -->
		<header>
			<div class="container text-center "  style="margin: 20px 0;">
				<div style="margin: 0 auto;   width:80%;height:90px; background-image: url(images/logo.jpg);background-position:center;background-size:contain ; background-repeat:no-repeat; ">
            </div>
			</div>
		</header>
		<!--logo结束 -->
		<!--用户名和密码开始 -->
		<section class="container">
			<form onsubmit="return false;">
				<div class="form-group" style=" overflow: hidden;">
					<input type="text" id="phone" class="form-control phone accountInfo" placeholder="请输入手机号">
					<input id="btn" value="获取验证码" onclick="sendAuthCode(this)" class="yanzhenma" readonly="readonly" />
				</div>
				<div class="form-group">
					<input type="text" class="form-control accountInfo" id="code"  placeholder="请输入验证码">
				</div>

			    <button  id="submitBtn" disabled="disabled" class="btn btn-primary  btn-block">下一步</button>
			</form>
		</section>
		<!--用户名和密码结束 -->
	</body>
	<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="plug-in/layer/layer.js"></script>
	<script type="text/javascript" src="js/login.js"></script>

</html>