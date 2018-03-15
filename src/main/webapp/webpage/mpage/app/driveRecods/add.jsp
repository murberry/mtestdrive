<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>/" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title><c:if test="${driveRecodsVo ==null || driveRecodsVo.id==null }">添加预约</c:if><c:if test="${driveRecodsVo !=null && driveRecodsVo.id!=null }">修改预约</c:if></title>
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/maser.css" />
<link href="css/mobiscroll_002.css" rel="stylesheet" type="text/css">
<link href="css/mobiscroll.css" rel="stylesheet" type="text/css">
<link href="css/mobiscroll_003.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/bottomnav.css" />
</head>
<body>
	<section>
		<div class="container-fluid theme_color  text-center" style="line-height: 39px;">
			<div class="col-xs-2">
				<a href="javascript: history.back();" class="back font_color" style="display: block; line-height: 39px;">
				 <span class="glyphicon glyphicon-chevron-left"></span>
				</a>
			</div>
			<div>
				<h4 class="font_color">
					<c:if test="${driveRecodsVo ==null || driveRecodsVo.id==null }">添加</c:if><c:if test="${driveRecodsVo !=null && driveRecodsVo.id!=null }">修改</c:if>预约
				</h4>
			</div>

		</div>
	</section>
	<div class="container show">
		<section>

			<form class="form-horizontal" role="form">
				<input type="hidden" id="id" name="id" value="${driveRecodsVo.id }"/>
				<input type="hidden" id="customerId" name="customerId" value="${driveRecodsVo.customer.id }"/>
				<div class="customers">
					<div class="col-xs-12  compellation">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${driveRecodsVo.customer.name }</div>

				</div>
				<div class=" customeres">
					<div class="col-xs-12 compellations">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${driveRecodsVo.customer.mobile }</div>

				</div>
				
				
				<div class="form-group">
					<div class="col-xs-3 compellation">车&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型:</div>
					<div class="col-xs-9  distance">
						<select id="carType" class="form-control">
							<c:forEach items="${types}" var="row">
								<option value="${row[0] }" <c:if test="${driveRecodsVo.car.id eq row[0] }">selected="selected"</c:if>>${row[1] }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class=" customeres" style="display:">
					
				<div id="badTime" class="col-xs-12 compellations"></div>
				</div>
				
				
				<div class="form-group">
					<div class="col-xs-3 compellation" class="demos">试驾日期:</div>
					<div class="col-xs-9 distance">
						<input placeholder="选择日期" onchange="getBadTime()"  class="form-control" value="<fmt:formatDate value="${driveRecodsVo.orderStartTime}" type="date" pattern="yyyy-MM-dd" />" readonly name="appDate" id="orderDate" type="text" style="background: #FFF;">
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-3 compellation" class="demos">试驾时间:</div>
					<div class="col-xs-9 distance">
						<input onchange="getTime()" placeholder="选择开始时间" value="<fmt:formatDate value="${driveRecodsVo.orderStartTime}" type="time" pattern="HH:mm" />" class="form-control" readonly name="appTime" id="startTime" type="text" style="background: #FFF; width: 44%; display: inline;" required="required">
						 - <input placeholder="选择结束时间" value="<fmt:formatDate value="${driveRecodsVo.orderEndTime}" type="time" pattern="HH:mm" />" class="form-control" readonly name="appTime" id="endTime" type="text" style="background: #FFF; width: 44%; display: inline;" required="required">
					</div>
				</div>

				<div class="text-center">
					<a href="javascript: void(0);" onclick="save()" class="btn btn-primary" style="margin-bottom: 50px; margin-right: 20px;">保存</a>
					<a href="javascript: history.back();" type="button" class="btn btn-primary" style="margin-bottom: 50px;">取消</a>
				</div>
			</form>

		</section>
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
				<h6>车辆管理</h6>
			</div>
		</a>
	</footer>
</body>
<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<script src="js/mobiscroll_002.js" type="text/javascript"></script>
<script src="js/mobiscroll.js" type="text/javascript"></script>
<script src="js/mobiscroll_003.js" type="text/javascript"></script>
<script>

function getBadTime(){
	var orderDate = $("#orderDate").val();
	var drive ={
			"id" : $("#id").val(),
			"orderDate" : orderDate,
			"carId" : $("#carType").val()
	};
	$.ajax({
		url:"/mtestdrive/driveRecodsAction.action?getBadTime",
		type:"post",
		data:JSON.stringify(drive),
		dataType:"json",
		contentType:'application/json',
		success:function(data){
			var x=data.obj;
			var a = '';
			for(var o in x){
				a=a+'('+x[o].startTime+' '+x[o].endTime+')';
			}
			/* alert(a); */
			/* $('#badTime').html('占用时段:'+a); */
		}
	});
}

function save(){
	var carId = $("#carType").val();
	if(carId.length==0){
		layer.msg('请选择试驾车！');
		return false;
	}
	var orderDate = $("#orderDate").val();
	if(orderDate.length==0){
		layer.msg('请选择试驾日期！');
		return false;
	}
	var startTime = orderDate + " " + $("#startTime").val();
	var houseStartTime = $("#startTime").val();
	if(houseStartTime.length==0){
		layer.msg('请试驾开始时间！');
		return false;
	}
	var endTime = orderDate + " " + $("#endTime").val();
	var houseEndTime = $("#endTime").val();
	if(houseEndTime.length==0){
		layer.msg('请试驾结束时间！');
		return false;
	}
	var drive = {
			"id" : $("#id").val(),
			"customerId" : $("#customerId").val(),
			"startTime" : startTime,
			"endTime" : endTime,
	        "carId" : $("#carType").val()
		};
	
	$.ajax({
		url:"/mtestdrive/driveRecodsAction.action?check",
		type:"post",
		data:JSON.stringify(drive),
		dataType:"json",
		contentType:'application/json',
		success:function(data){
			if(data.obj=='1'){
				layer.msg("此时车辆已被预约，请重新选择！");
			}else if(data.obj=='0'){
				layer.msg('请选择车辆！');
			}else if(data.obj=='3'){
				layer.msg("此时车辆处在报备期间，请重新选择！");
			}else{
				$.ajax({
					url:"/mtestdrive/driveRecodsAction.action?save",
					type:"post",
					data:JSON.stringify(drive),
					dataType:"json",
					contentType:'application/json',
					success:function(data){
						layer.msg("操作成功！");
						setTimeout("location.href = '/mtestdrive/driveRecodsAction.action?index';", 2000);
					}
				});
			}
		}
	});
}

function getTime(){
	var startTime = $("#startTime").val();
	var strs= new Array();
	strs=startTime.split(":");
	var h = parseInt(strs[0])+1
	
	$("#endTime").val(h+":"+strs[1]);
}

 $(function () {
			var currYear = (new Date()).getFullYear();	
			var opt={};
			opt.date = {preset : 'date'};
			opt.datetime = {preset : 'datetime'};
			opt.time = {preset : 'time'};
			opt.default = {
				theme: 'android-ics light', //皮肤样式
		        display: 'modal', //显示方式 
		        mode: 'scroller', //日期选择模式
				dateFormat: 'yyyy-mm-dd',
				lang: 'zh',
				showNow: true,
				nowText: "今天",
		        startYear: currYear, //开始年份
		        endYear: currYear + 10 //结束年份
			};
			
			
			var opt1={};
			opt1.date = {preset : 'date'};
			opt1.datetime = {preset : 'datetime'};
			opt1.time = {preset : 'time'};
			opt1.default = {
				theme: 'android-ics light', //皮肤样式
		        display: 'modal', //显示方式 
		        mode: 'scroller', //日期选择模式
				dateFormat: 'yyyy-mm-dd',
				lang: 'zh',
				showNow: true,
				stepHour: 1,
                stepMinute: 60,
                 stepSecond: 60,
				nowText: "今天",
		        startYear: currYear, //开始年份
		        endYear: currYear + 10 //结束年份
			};
			
			
			
			

		  	$("#orderDate").mobiscroll($.extend(opt['date'], opt['default']));
		  	
		  	$("#appDate1").mobiscroll($.extend(opt['date'], opt['default']));
		  	var optTime = $.extend(opt1['time'], opt1['default']);
		    $("#startTime").mobiscroll(optTime).time(optTime);
		     /* $("#endTime").mobiscroll(optTime).time(optTime); */
        });
</script>


</html>