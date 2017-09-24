<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>

<head>
<base href="<%=basePath%>/" />
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title>客户列表</title>
<link rel="stylesheet"
	href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="plug-in/bootstrap.min/css/bootstrap-theme.min.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bottomnav.css" />
<!-- <link rel="stylesheet" href="plug-in/dropload-gh-pages/dist/dropload.css" /> -->

<!--
        	作者：offline
        	时间：2017-03-23
        	描述：<script type="text/javascript" src="js/customer.js" ></script>
       -->
<style type="text/css">
.form-control {
	width: 100%;
	height: 30px;
}

.query .query_btn {
	height: 30px;
	line-height: 30px;
}

.table {
	margin: 0;
}
</style>
</head>

<body>
	<!--头部查询开始 -->
	<header class="navbar-fixed-top">
		<div class="container query">
			<div ass="row">
				<div class="col-xs-2 ico text-right" id="addBtn"
					style="padding: 0px; margin-right: 0px;">
					<a href="/mtestdrive/customerInfoAction.action?add"><img
						src="images/guanzhu.png" height="30px" width="30px"></a>
				</div>
				<div class="col-xs-8">
					<input type="text" class="form-control" placeholder="请输入客户的手机号或姓名"
						id="cusInfo">
				</div>
				<div onclick="searchData()" class="col-xs-2 query_btn" id="queryBtn">查询</div>
			</div>
		</div>
	</header>
	<!--头部查询结束 -->
	<!--客户列表开始 -->
	<div class="container details_list" style="margin-top: 40px">
		
		<!--客户列表内容开始 -->
	   <div id="customerListDiv">
			
		</div> 

		<!--客户列表内容结束 -->
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
			<div class="col-xs-3 nenuActi">
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
<script type="text/javascript"
	src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript"
	src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<!-- <script type="text/javascript" src="plug-in/dropload-gh-pages/dist/dropload.min.js"></script> -->
<br>
<script type="text/javascript">
	var curPage = 1;
	$(function() {
		refreshData();
		$(window).scroll(function() {
			var scrollTop = $(this).scrollTop();
			var scrollHeight = $(document).height();
			var windowHeight = $(this).height();
			if (scrollTop + windowHeight == scrollHeight) {
				refreshData();
			}
		});
	})
	
	function searchData(){
		curPage = 1;
		$("#customerListDiv").html("");
		refreshData();
	}
	
	function refreshData(){
		$.ajax({
			type : 'GET',
			url : 'customerInfoAction.action?jsonList&parameter='
					+ $("#cusInfo").val()
					+ '&curPage=' + curPage,
			dataType : 'json',
			success : function(data) {
				if(data.obj != null && data.obj.length != 0)
					curPage++;
				else{
					/* $("#customerListDiv").append('<div style="margin: 10px;text-align: center;">暂无数据</div>'); */
					return false;
				}
					
				var obj = null;
				for (var i = 0; i < data.obj.length; i++) {
					obj = data.obj[i];
					$("#customerListDiv").append(
							'<div class="row" id="lists"> '+
							'<div class="container">'+
							'	<table class="table">'+
							'		<thead>'+
							'			<tr>'+
							'				<td style="border: none;">姓名：'+obj.name+'</td>'+
							'				<td style="border: none;">电话： '+obj.mobile+'</td>'+
							'			</tr>'+
							'			<tr>'+
							'				<td style="border: none;">渠道：'+
											getSourceName(obj.source)+
							'				</td>'+
							'				<td style="border: none;">性别：'+
											getSexName(obj.gender)+
							'				</td>'+
							'			</tr>'+
							'			<tr>'+
							'				<td colspan="2" style="border: none;">最近预约：'+
							getOrderTime(obj.driveRecodses)+
							'				</td>'+
							'			</tr>'+
							'		</thead>'+
							'	</table>'+
							'</div>'+
							'<div class="text-center">'+
							'	<div class="col-xs-6 bnt_right ">'+
									getOpreation(obj.driveRecodses, obj.id)+
							'	</div>'+
							'	<div class="col-xs-6 bnt_left">'+
							'		<a href="/mtestdrive/customerInfoAction.action?add&id='+obj.id+'">'+
							'			<button type="button" class="btn btn-primary">修改资料</button>'+
							'		</a>'+
							'	</div>'+
							'</div>'+
						'</div>');
				}
			},
			error : function(xhr, type) {
			}
		});
	}
	
	
	function getOpreation(list, id){
		var normalStr = '<a href="/mtestdrive/driveRecodsAction.action?add&customerId='+id+'">'+
		'<button type="button" class="btn btn-primary">试驾预约</button></a>';
		
		var tipStr = '<span class="glyphicon glyphicon-heart" style="font-size: 32px; color: #0f1d3a;" '+
		'onclick="javascript:layer.msg(\'该客户已经正在预约试驾，请关注客户意向。\');"> </span>';
		
		if(list.length == 0 || list[0].status == 5 || list[0].status == 6
					|| list[0].status == 7 || list[0].status == 8 || list[0].status==9){
			return normalStr;
		}else{
			var obj = list[0];
			if(obj.status != 5 && obj.status != 6 && obj.status != 7 && obj.status != 8 && obj.status != 9)
				return tipStr;
		}
	}
	
	//获取性别名称
	function getSexName(sex){
		var sexName = null;
		switch(sex){
			case 1:
				sexName = '男';
				break;
			case 2:
				sexName = '女';
				break;
			default:
				sexName = '未知';
		}
		return sexName;
	}
	
	//获取预约时间
	function getOrderTime(obj){
		if(obj && obj.length > 0){
			var orderDate = obj[0].orderEndTime.substring(0,11);
			var startTime = obj[0].orderStartTime.substring(11,16);
			var endTime = obj[0].orderEndTime.substring(11,16);
			return orderDate+'  '+startTime+'-'+endTime;
		}else
			return '无';
	}
	
	//获取渠道名称
	function getSourceName(source){
		var sourceName = null;
		switch(source){
			case 1:
				sourceName = 'saleforce推送而来';
				break;
			case 2:
				sourceName = '4S店添加';
				break;
			default:
				sourceName = '未知';
		}
		return sourceName;
	}
</script>

</html>