<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>

<head>
<base href="<%=basePath%>/" />
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title>手续办理</title>
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/maser.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bottomnav.css" />
</head>

<body>
	<section>
		<div class="container-fluid theme_color  text-center">
			<h4 class="font_color">手续办理</h4>
		</div>
	</section>
	<section class="container imgpad">
		<div class="media">
			<div class="media-left media-middle">
				<span class="glyphicon glyphicon-user"></span>
			</div>
			<div class="media-body">
				<h4>试驾信息</h4>
			</div>
		</div>
	</section>
	<section>
		<div class="container" style="padding-top: 10px;">
			<div class="row">
				<div class="col-xs-7">
					<p>${driveRecodsVo.customer.name }</p>
					<p>车型：${driveRecodsVo.car.type}</p>
					<p>
						<fmt:formatDate value="${driveRecodsVo.orderStartTime}" type="date" pattern="yyyy.MM.dd  HH:mm" />
						-<fmt:formatDate value="${driveRecodsVo.orderEndTime}" type="date" pattern="HH:mm" />
					</p>
				</div>

				<div class="col-xs-5">
					<p>${driveRecodsVo.customer.mobile }</p>
				</div>
			</div>
			<input type="hidden" id="id" name="id" value="${driveRecodsVo.id }"/>
			<div class="photo">
				<div class="pic">
					<input id="path1" name="path1" type="hidden">
					<input id="img" type="file" name="img[]" data-url="fileUpload.action?upImg" accept="image/*" capture="camera" style="border: 1px solid red" />
					 <span style="text-align: center;">添加试驾合同照片</span>
				</div>
				<div id="photo"></div>

				<div class="pic">
				<input id="path2" name="path2" type="hidden" value="${licensePicPath }">
					<input id="img1" type="file" name="img[]" data-url="fileUpload.action?upImg" accept="image/*" capture="camera" />
					<c:if test="${empty licensePicPath}">
						<span style="text-align: center;" id="tipSpan">添加驾照照片</span>
					</c:if>
				</div>
				<div id="photo1">
					<c:if test="${not empty licensePicPath}">
						<img src="fileUpload.action?view&fileName=${licensePicPath}" alt="" 
						onerror="this.src='images/onError.jpg'" />
					</c:if>
				</div>
			</div>
			<div class="container tijiao">
				<a href="javascript:void(0);">
					<button type="button" onclick="management()" class="btn btn-primary btn-block theme_color">下一步</button>
				</a>
			</div>
		</div>

	</section>

	<footer class="navbar-fixed-bottom text-center" style="background: #FFF;">
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
				<h6>车辆管理</h6>
			</div>
		</a>
	</footer>
</body>
<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>

<script src="plug-in/jquery-plugs/fileupload/js/jquery.1.9.1.min.js"></script>
<script src="plug-in/jquery-plugs/fileupload/js/vendor/jquery.ui.widget.js"></script>
<script src="plug-in/jquery-plugs/fileupload/js/jquery.iframe-transport.js"></script>
<script src="plug-in/jquery-plugs/fileupload/js/jquery.fileupload.js"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<script type="text/javascript">
			$(function() {
				$('#img').change(function() {
					var file = this.files[0];
					var r = new FileReader();
					r.readAsDataURL(file);
					$(r).load(function() {
						$('#photo').html('<img src="' + this.result + '" alt="" />');
					})
					$(this).siblings().hide();

				})
				$('#img1').change(function() {
					var file = this.files[0];
					var r = new FileReader();
					r.readAsDataURL(file);
					$(r).load(function() {
						$('#photo1').html('<img src="' + this.result + '" alt="" />');
					})
					$(this).siblings().hide();

				})
				$('#img').fileupload({
					autoUpload : "ture",
					dataType:'JSON',
					done:function(e,data){
						$("#path1").val(data.result[0].fileName);
					}
				})
				$('#img1').fileupload({
					autoUpload : "ture",
					dataType:'JSON',
					done:function(e,data){
						$("#path2").val(data.result[0].fileName);
					}
				})
			})
			function management(){
				var path1 = $("#path1").val();
				if(path1.length == 0){
					layer.msg('请上传试驾合同！');
					return false;
				}
				var path2 = $("#path2").val();
				if(path2.length == 0){
					layer.msg('请上传试驾照片！');
					return false;
				}
				
				window.location.href="/mtestdrive/driveRecodsAction.action?management&id="+"${driveRecodsVo.id}&picPath1="+path1+"&picPath2="+path2;
				
			}
			
			
		</script>

</html>