<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>/" />
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title>试驾后添加图片  </title>
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/maser.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bottomnav.css" />
</head>

<body>
	<section>
		<div class=" theme_color top_bom_pad text-center">
			<h4 class="font_color">生成报告  </h4>
		</div>
	</section>
	<section class="container imgpad">
		<div class="container">
			<div class="media">
				<div class="media-left media-middle">
					<span class="glyphicon glyphicon-user" style="font-size: 36px;"></span>
				</div>
				<div class="media-body">
					<h4>试驾信息</h4>
				</div>
			</div>
		</div>
	</section>

	<section>
		<div class="container shijiaInof">
			<div class="row">
				<div class="col-xs-4">
					<dt>姓名:${driveRecods.customer.name }</dt>
				</div>
				<div class="col-xs-8">
					<dt>${driveRecods.customer.mobile }</dt>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<dt>车型：${driveRecods.car.type }</dt>
					<dt>
						<fmt:formatDate value="${driveRecods.orderStartTime}" type="date" pattern="yyyy.MM.dd  HH:mm" />
						-<fmt:formatDate value="${driveRecods.orderEndTime}" type="date" pattern="HH:mm" />
					</dt>
				</div>
			</div>
			<div class="photo">
				<div class="pic">
					<input id="endPicPath" name="endPicPath" type="text" value="${driveRecods.endPicPath }">
					<input id="img" type="file" accept="image/*" capture="camera"  data-url="fileUpload.action?upImg"/>
					<span>试驾后合照</span>
				</div>
				<div id="photo">
					<div id="progress">
						<div class="bar" style="width: 0%;"></div>
					</div>
				</div>
			</div>
			<div class="container tijiao">
				<button type="button" class="btn btn-primary  btn-block theme_color" onclick="jumpToTestDriveReportPage()">生成报告</button>

			</div>
		</div>

	</section>
	<div class="modal fade bs-example-modal-lg" tabindex="0" role="dialog" aria-labelledby="myLargeModalLabel" id="myModal">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">查看试驾报告</h4>
				</div>
				<div class="modal-body">
					又有一位客户完成试驾了，您可以下载PDF形式的试驾报告或分享二维码。
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">下载</button>
					<button type="button" class="btn btn-primary">分享</button>
				</div>


			</div>
		</div>
	</div>
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
					$("span").hide();
				})
				
				$('#img').fileupload({
					autoUpload : "true",
					dataType:'JSON',
					done:function(e,data){
						$("#endPicPath").val(data.result[0].fileName);
					},
                    progress: function (e, data) {
                        var progress = parseInt(data.loaded / data.total * 100, 10);
                        $('#progress .bar').css(
                            'width',
                            progress + '%'
                        );
                    }
				})
			})
			function jumpToTestDriveReportPage(){
				var endPicPath = $("#endPicPath").val();
				
				/*if(endPicPath.length==0){
					layer.msg('请上传试驾合照照片！');
					return false;
				}*/

				location.href="/mtestdrive/driveRecodsAction.action?report&id=${driveRecods.id }&endPicPath="+$("#endPicPath").val();
			}
</script>
</html>