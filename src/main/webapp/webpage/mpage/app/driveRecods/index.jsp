<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!doctype html>
<html>

<head>
<base href="<%=basePath%>/" />
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<%@include file="/context/ico.jsp"%>
<title>试驾管理</title>
<link rel="stylesheet"
	href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/maser.css" />
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bottomnav.css" />
<style>
.panel-body a {
	color: #000000;
}

.panel-body .text-center a {
	color: white;
}

.panel-body .text-center a:hover {
	color: black;
}
</style>

</head>

<body>
	<section class="navbar-fixed-top">

		<div class="container" style="background: #0f1d3a;">
			<div class="table-responsive" style="border: none;">
				<table class="table search" style="border: none;">
					<tr>
						<td><select onchange="submit('carType')" id="carType"
							name="carType" class="form-control" style="padding: 0px">
								<option value="" style="display: none">车型</option>
								<c:forEach items="${types}" var="row">
									<option value="${row[0] }"
										<c:if test="${carType eq row[0] }">selected="selected"</c:if>>${row[1] }</option>
								</c:forEach>
						</select></td>
						<td><select onchange="submit('carId')" id="carId"
							name="carId" class="form-control" style="padding: 0px">
								<option value="" style="display: none">车牌</option>
								<c:forEach items="${carList}" var="row">
									<option value="${row.id }"
										<c:if test="${carId eq row.id }">selected="selected"</c:if>>${row.plateNo }</option>
								</c:forEach>
						</select></td>
						<td>
							<!-- <select class="form-control" style="padding: 0px">
								<option value="" style="display:none">试驾状态</option>
						</select> -->
						</td>
						<!-- <td><a href="" style="color: #FFFFFF; line-height: 35px;">日期</a> -->
						</td>
					</tr>
				</table>
			</div>

		</div>
	</section>

	<!--
          描述：这里是试驾信息的展示 : 一个section就表示一条信息
   -->
   <div id="testDiv">
	<div class="panel  testMag" style="margin-top: 60px">
		<div class="panel-heading">
			<h3 class="panel-title">试驾列表</h3>
		</div>
		<div class="panel-body">
			<div class="bs-example" data-example-id="collapse-accordion">
				<div class="panel-group" id="accordion" role="tablist">
					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingOne">
							<a role="button" data-toggle="collapse" data-parent="#accordion"
								href="#collapseOne" aria-expanded="false"
								aria-controls="collapseOne" class="collapsed">
								<h4 class="panel-title">
									潜在试驾 <span id="potentialNumber"
										class="label label-default pull-right">${potentialDrive.size() }</span>
								</h4>
							</a>
						</div>
						<div id="collapseOne" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="headingOne"
							aria-expanded="false">
							<div class="panel-body">
								<div>
									<c:if test="${empty potentialDrive }">
										<div class="alert alert-warning text-center" role="alert">暂无数据</div>
									</c:if>
									<c:forEach items="${potentialDrive }" var="row">
										<table class="table">
											<tbody>
												<tr>
													<td>姓名:${row.customer.name }</td>
													<td><fmt:formatDate value="${row.orderStartTime}"
															type="date" pattern="yyyy.MM.dd  HH:mm" /> -<fmt:formatDate
															value="${row.orderEndTime}" type="date" pattern="HH:mm" />
													</td>
												</tr>
												<tr>
													<td>车型:${row.car.type }</td>
													<td>电话:<a href="tel:${row.customer.mobile }">${row.customer.mobile }</a></td>
												</tr>
												<tr>
													<td>状态:待确认</td>
													<td>车况:<span class="text-primary"> <c:choose>
																<c:when test="${row.car.status==1 }">试驾中</c:when>
																<c:when test="${row.car.status==2 }">空闲</c:when>
																<c:when test="${row.car.status==3 }">外出</c:when>
																<c:when test="${row.car.status==4 }">外出</c:when>
																<c:when test="${row.car.status==5 }">外出</c:when>
																<c:when test="${row.car.status==6 }">外出</c:when>
																<c:otherwise>未知</c:otherwise>
															</c:choose></span></td>
												</tr>
												<tr class="text-center">
													<td><a href="javascript: void(0);"
														onclick="confirm('${row.id}',this)"
														class="btn btn-primary">已电话确认</a></td>
													<td><a
														href="/mtestdrive/driveRecodsAction.action?add&id=${row.id}"
														class="anniu">
															<button type="button" class="btn btn-primary">修改预约</button>
													</a></td>
												</tr>
											</tbody>
										</table>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingTwo">
							<a class="collapsed" role="button" data-toggle="collapse"
								data-parent="#accordion" href="#collapseTwo"
								aria-expanded="false" aria-controls="collapseTwo">
								<h4 class="panel-title">
									已确认 <span class="label label-default pull-right">${confirmedDrive.size() }</span>
								</h4>
							</a>
						</div>
						<div id="collapseTwo" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="headingTwo">
							<div class="panel-body">
								<div>
									<c:if test="${empty confirmedDrive }">
										<div class="alert alert-warning text-center" role="alert">暂无数据</div>
									</c:if>
									<c:forEach items="${confirmedDrive }" var="row">
										<table class="table">
											<tbody>
												<tr>
													<td>姓名:${row.customer.name }</td>
													<td><fmt:formatDate value="${row.orderStartTime}"
															type="date" pattern="yyyy.MM.dd  HH:mm" /> -<fmt:formatDate
															value="${row.orderEndTime}" type="date" pattern="HH:mm" />
													</td>
												</tr>
												<tr>
													<td>车型:${row.car.type }</td>
													<td>电话:${row.customer.mobile }</td>
												</tr>
												<tr>
													<td>状态:确认预约</td>
													<td>车况:<span class="text-primary"> <c:choose>
																<c:when test="${row.car.status==1 }">试驾中</c:when>
																<c:when test="${row.car.status==2 }">空闲</c:when>
																<c:when test="${row.car.status==3 }">外出</c:when>
																<c:when test="${row.car.status==4 }">外出</c:when>
																<c:when test="${row.car.status==5 }">外出</c:when>
																<c:when test="${row.car.status==6 }">外出</c:when>
																<c:otherwise>未知</c:otherwise>
															</c:choose></span></td>
												</tr>
												<tr class="text-center">
													<td><a
														href="/mtestdrive/carInfoAction.action?condition&carId=${row.car.id }&driveId=${row.id}"
														class="anniu">
															<button type="button" class="btn btn-primary">车况确认</button>
													</a></td>
													<td><a
														href="/mtestdrive/driveRecodsAction.action?add&id=${row.id}"
														class="anniu">
															<button type="button" class="btn btn-primary">修改预约</button>
													</a></td>
												</tr>
											</tbody>
										</table>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingsx">
							<a class="collapsed" role="button" data-toggle="collapse"
								data-parent="#accordion" href="#collapsesx"
								aria-expanded="false" aria-controls="collapsesx">
								<h4 class="panel-title">
									手续办理 <span class="label label-default pull-right">${formalitiesDrive.size() }</span>
								</h4>
							</a>
						</div>
						<div id="collapsesx" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="headingsx">
							<div class="panel-body">
								<div>
									<c:if test="${empty formalitiesDrive }">
										<div class="alert alert-warning text-center" role="alert">暂无数据</div>
									</c:if>
									<c:forEach items="${formalitiesDrive }" var="row">
										<table class="table">
											<tbody>
												<tr>
													<td>姓名:${row.customer.name }</td>
													<td><fmt:formatDate value="${row.orderStartTime}"
															type="date" pattern="yyyy.MM.dd  HH:mm" /> -<fmt:formatDate
															value="${row.orderEndTime}" type="date" pattern="HH:mm" />
													</td>
												</tr>
												<tr>
													<td>车型:${row.car.type }</td>
													<td>电话:${row.customer.mobile }</td>
												</tr>
												<tr>
													<td>状态:车辆已准备</td>
													<td>车况:<span class="text-primary"> <c:choose>
																<c:when test="${row.car.status==1 }">试驾中</c:when>
																<c:when test="${row.car.status==2 }">空闲</c:when>
																<c:when test="${row.car.status==3 }">外出</c:when>
																<c:when test="${row.car.status==4 }">外出</c:when>
																<c:when test="${row.car.status==5 }">外出</c:when>
																<c:when test="${row.car.status==6 }">外出</c:when>
																<c:otherwise>未知</c:otherwise>
															</c:choose></span></td>
												</tr>
												<tr class="text-center">
													<td colspan="2">
													<c:if test="${row.status == 2 }">
															<a
																href="driveRecodsAction.action?informations&id=${row.id}"
																class="anniu">
																<button class="btn btn-primary">手续办理</button>
															</a>
														</c:if>
														 <c:if test="${row.status == 3 }">
															<a
																href="carInfoAction.action?monitor&driveStart=1&id=${row.id}"
																class="anniu">
																<button class="btn btn-primary">开始试驾</button>
															</a>
														</c:if>
														</td>
												</tr>
											</tbody>
										</table>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingThree">
							<a class="collapsed" role="button" data-toggle="collapse"
								data-parent="#accordion" href="#collapseThree"
								aria-expanded="false" aria-controls="collapseThree">
								<h4 class="panel-title">
									正在试驾<span class="label label-default pull-right">${testDrive.size() }</span>
								</h4>
							</a>
						</div>
						<div id="collapseThree" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="headingThree">
							<div class="panel-body">
								<div>
									<c:if test="${empty testDrive }">
										<div class="alert alert-warning text-center" role="alert">暂无数据</div>
									</c:if>
									<c:forEach items="${testDrive }" var="row">
										<table class="table">
											<tbody>
												<tr>
													<td>姓名:${row.customer.name }</td>
													<td><fmt:formatDate value="${row.orderStartTime}"
															type="date" pattern="yyyy.MM.dd  HH:mm" /> -<fmt:formatDate
															value="${row.orderEndTime}" type="date" pattern="HH:mm" />
													</td>
												</tr>
												<tr>
													<td>车型:${row.car.type }</td>
													<td>电话:${row.customer.mobile }</td>
												</tr>
												<tr>
													<td>状态:试驾中</td>
													<td>车况:<span class="text-primary"> <c:choose>
																<c:when test="${row.car.status==1 }">试驾中</c:when>
																<c:when test="${row.car.status==2 }">空闲</c:when>
																<c:when test="${row.car.status==3 }">外出</c:when>
																<c:when test="${row.car.status==4 }">外出</c:when>
																<c:when test="${row.car.status==5 }">外出</c:when>
																<c:when test="${row.car.status==6 }">外出</c:when>
																<c:otherwise>未知</c:otherwise>
															</c:choose></span></td>
												</tr>
												<tr class="text-center">
													<td colspan="2"><a
														href="/mtestdrive/carInfoAction.action?monitor&id=${row.id}"
														class="btn btn-primary " style="color: white;">&nbsp;&nbsp;&nbsp;查&nbsp;&nbsp;看&nbsp;&nbsp;&nbsp;</a></td>
												</tr>
											</tbody>
										</table>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
					<div class="panel panel-default" style="margin-bottom: 50px">
						<div class="panel-heading" role="tab" id="heading4">
							<a class="collapsed" role="button" data-toggle="collapse"
								data-parent="#accordion" href="#collapse4" aria-expanded="false"
								aria-controls="collapse4">
								<h4 class="panel-title">
									试驾已完成 <span class="label label-default pull-right">${finishDrive.size() }</span>
								</h4>
							</a>
						</div>
						<div id="collapse4" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="heading4">
							<div class="panel-body">
								<div>
									<c:if test="${empty finishDrive }">
										<div class="alert alert-warning text-center" role="alert">暂无数据</div>
									</c:if>
									<c:forEach items="${finishDrive }" var="row">
										<table class="table">
											<tbody>
												<tr>
													<td>姓名:${row.customer.name }</td>
													<td><fmt:formatDate value="${row.orderStartTime}"
															type="date" pattern="yyyy.MM.dd  HH:mm" /> -<fmt:formatDate
															value="${row.orderEndTime}" type="date" pattern="HH:mm" />
													</td>
												</tr>
												<tr>
													<td>车型:${row.car.type }</td>
													<td>电话:${row.customer.mobile }</td>
												</tr>
												<tr>
													<td>状态:试驾完成</td>
													<td>车况:<span class="text-primary"> <c:choose>
																<c:when test="${row.car.status==1 }">试驾中</c:when>
																<c:when test="${row.car.status==2 }">空闲</c:when>
																<c:when test="${row.car.status==3 }">外出</c:when>
																<c:when test="${row.car.status==4 }">外出</c:when>
																<c:when test="${row.car.status==5 }">外出</c:when>
																<c:when test="${row.car.status==6 }">外出</c:when>
																<c:otherwise>未知</c:otherwise>
															</c:choose></span></td>
												</tr>
												<tr class="text-center">
													<td colspan="2">
															<a href="/mtestdrive/driveRecodsAction.action?report&id=${row.id}"
															class="btn btn-primary" style="color: white;">查看报告</a>
													</td>
												</tr>
											</tbody>
										</table>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
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
			<div class="col-xs-3 actives">
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

</div>
</body>
<script type="text/javascript"
	src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript"
	src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<script type="text/javascript" src="plug-in/jquery/jquery.slimscroll.min.js" ></script>
<script type="text/javascript">
	
var hig=$(window).height(); //浏览器当前窗口可视区域高度


$('#testDiv').slimscroll({
alwaysVisible: true,
height: hig,
size: '5px'
});

	
	var potentialNumber = null;
	try{
		potentialNumber = ${potentialDrive.size()};
	}catch(e){
		potentialNumber = 0;
	}
	
	function confirm(id, a) {
		layer.confirm('确认此条预约信息？', {
			btn : [ '确认', '取消' ]
		//按钮
		}, function() {
			$.ajax({
				url : "/mtestdrive/driveRecodsAction.action?confirm",
				type : "post",
				data : JSON.stringify({
					"id" : id
				}),
				dataType : "json",
				contentType : 'application/json',
				success : function(data) {
					layer.msg('已确认！', {
						icon : 1
					});
					$(a).parent().parent().parent().parent().hide();
					$("#potentialNumber").html(--potentialNumber);
				}
			});
		}, function() {

		});
	}

	function submit(condition) {
		var url = "/mtestdrive/driveRecodsAction.action?index";
		var carId = $("#carId").val();
		if ('carId' == condition) {
			url += "&carId=" + carId;
		}
		var carType = $("#carType").val();
		if ('carType' == condition) {
			url += "&carType=" + carType;
		}
		location.href = url;
	}
</script>
</html>