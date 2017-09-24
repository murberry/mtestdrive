<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	    <base href="<%=basePath%>/" />
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<%@include file="/context/ico.jsp"%>
		<title>登录</title>
		<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
		<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap-theme.min.css" />
		<link rel="stylesheet" href="css/style.css" />
		<link rel="stylesheet" href="css/bottomnav.css" />
	</head>

	<body class="bg">
		<!--头部logo开始 -->

		<header>
			<div class="container text-center "  style="margin: 30px 0;">
				<div style="margin: 0 auto;   width:100%;height:100px; background-image: url(images/logo.jpg);background-position:center;background-size:contain ; background-repeat:no-repeat; ">
            </div>
			</div>
		</header>
		<!--头部logo结束 -->
		<!--用户名和密码开始 -->
		<section class="container">
			<form>
				<div class="form-group">
					<input type="text" id="mobile" class="form-control" placeholder="请输入用户手机号" value="${param.mobile }">
				</div>
				<div class="form-group">
					<input type="password" id="password" class="form-control" placeholder="请输入密码">
				</div>
				<div class="agreement">
					<div class="password">
						<a href="login.action?jumpToRetrievePasswordPage">忘记密码</a>
					</div>
				</div>

				<a href="javascript:check();">
					<div type="submit" class="btn btn-primary  btn-block  confirm">登录</div>
				</a>
			</form>
		</section>
		<!--用户名和密码结束 -->
	</body>
	<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<script type="text/javascript">
	function check(){
		var mobile = $("#mobile").val();
		var password = $("#password").val();
		var url = "/mtestdrive/login.action?check";
		var req ={"mobile": mobile,"password":password};
		$.ajax({
		url : url,
		type : "post",
		data :JSON.stringify(req),
		dataType : "json",
		contentType:"text/html",  
		success : function(data){
			if(data=="1"){
				window.location.href="/mtestdrive/login.action?page";
			}
			if(data=="2"){
				//错误提示
				layer.msg("用户名密码错误");
			}
		}
		}
		
		);
	}
	</script>
</html>