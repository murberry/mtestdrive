<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>/" />
<meta charset="utf-8">
<title>首页</title>
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%@include file="/context/ico.jsp"%>
<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/swiper.min.css" />
<link rel="stylesheet" href="css/index.css" />
<link rel="stylesheet" href="css/bottomnav.css" />
</head>
<style type="text/css">
body {
	background: #ffffff;
	font-family: Helvetica Neue, Helvetica, Arial, sans-serif;
	color: #000;
	margin: 0;
	padding: 0;
}
.glyphicon {
	position: relative !important;
	top: 1px !important;
	display: inline-block !important;
	font-family: 'Glyphicons Halflings';
	font-style: normal !important;
	font-weight: 400 !important;
	line-height: 1 !important;
	-webkit-font-smoothing: antialiased !important;
	-moz-osx-font-smoothing: grayscale !important;
}
</style>
</head>

<body >
	<!-- Swiper -->
	<div class="swiper-container">
		<div class="swiper-wrapper" style="position: absolute">
			<div class="swiper-slide menu">
				<div class="left_nav">
					<ul class="left_nav">
						<div style="margin: 0 auto; text-align: center">
							<img src="plug-in/login/images/touxiang.png" style="width: 100px; height: 100px; margin-top:40px; margin-bottom:20px;border-radius:100px ;">
						</div>
						<li>工号：${sessionScope.SalesmanInfo.employeeNo}</li>
						<li>姓名：${sessionScope.SalesmanInfo.name}</li>
						<li>门店：${sessionScope.SalesmanInfo.agencyName}</li>
						<li><a>
							<div class="success_rate">预约成功率:</div>
							<div class="graph"><strong id="bar" style="width: 1%;">${sessionScope.SalesmanInfo.ceil}</strong></div>
						</a></li>
						<!-- <li>转化率：70%</li> -->
						<li><a href="javascript:void(0);" onclick="out()">注销</a></li>
					</ul>
				</div>
			</div>
			<div class="swiper-slide content">
			<div id="testDiv">
				<div>
				<div style=" margin-top:10px;relative; width: 100%; ">
					<img src="images/appLogo.png" style="width: 100%;height: 80px;">
				</div>
				<div class="menu-button" style="float: left;position: static;margin-top: -70px;">
					<div class="bar"></div>
					<div class="bar"></div>
					<div class="bar"></div>
				</div>
				</div>
				<div class="panel theme_color" style="background: #FFF; margin-top: -10px;margin-bottom: 80px;">
					<div class="panel-heading">
						<h3 class="panel-title">今日任务</h3>
					</div>
					<div class="panel-body">
						<div class="bs-example" data-example-id="collapse-accordion">
							<div class="panel-group" id="accordion" role="tablist">
								<div class="panel panel-default">
									<div class="panel-heading" role="tab" id="headingOne">
										<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="false" aria-controls="collapseOne" class="collapsed">
											<h4 class="panel-title">潜在试驾 <span class="label label-default pull-right">${potentialDrive.size() }</span></h4>
										</a>
									</div>
									<div id="collapseOne" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne" aria-expanded="false">
										<div class="panel-body">
											<div >
												<c:if test="${empty potentialDrive }">
													<div class="alert alert-warning text-center" role="alert">暂无数据</div>
												</c:if>
												<c:forEach items="${potentialDrive }" var="row">
													<table class="table">
														<tbody>
															<tr>
																<td>姓名:${row.customer.name }</td>
																<td>
																	<fmt:formatDate value="${row.orderStartTime}" type="date" pattern="yyyy.MM.dd  HH:mm" />
																	-<fmt:formatDate value="${row.orderEndTime}" type="date" pattern="HH:mm" />
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
																<td><a href="javascript: void(0);" onclick="confirm('${row.id}',this)" class="btn btn-primary">已电话确认</a></td>
																<td><a href="/mtestdrive/driveRecodsAction.action?add&id=${row.id}" class="anniu">
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
										<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
											<h4 class="panel-title">已确认 <span class="label label-default pull-right">${confirmedDrive.size() }</span></h4>
										</a>
									</div>
									<div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
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
																<td>
																	<fmt:formatDate value="${row.orderStartTime}" type="date" pattern="yyyy.MM.dd  HH:mm" />
																	-<fmt:formatDate value="${row.orderEndTime}" type="date" pattern="HH:mm" />
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
																<td><a href="/mtestdrive/carInfoAction.action?condition&carId=${row.car.id }&driveId=${row.id}" class="anniu">
																	<button type="button" class="btn btn-primary">车况确认</button>
																</a></td>
																<td><a href="/mtestdrive/driveRecodsAction.action?add&id=${row.id}" class="anniu">
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
										<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapsesx" aria-expanded="false" aria-controls="collapsesx">
											<h4 class="panel-title">手续办理 <span class="label label-default pull-right">${formalitiesDrive.size() }</span></h4>
										</a>
									</div>
									<div id="collapsesx" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingsx">
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
																<td>
																	<fmt:formatDate value="${row.orderStartTime}" type="date" pattern="yyyy.MM.dd  HH:mm" />
																	-<fmt:formatDate value="${row.orderEndTime}" type="date" pattern="HH:mm" />
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
																<%-- <a href="/mtestdrive/driveRecodsAction.action?informations&id=${row.id}" class="anniu">
																	<c:if test="${row.status == 2 }">
																		<button class="btn btn-primary">手续办理</button>																	
																	</c:if>
																	<c:if test="${row.status == 3 }">
																		<button class="btn btn-primary">开始试驾</button>																	
																	</c:if>
																</a> --%></td>
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
										<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
											<h4 class="panel-title">正在试驾<span class="label label-default pull-right">${testDrive.size() }</span></h4>
										</a>
									</div>
									<div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
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
																<td>
																	<fmt:formatDate value="${row.orderStartTime}" type="date" pattern="yyyy.MM.dd  HH:mm" />
																	-<fmt:formatDate value="${row.orderEndTime}" type="date" pattern="HH:mm" />
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
																<td colspan="2">
																	<a href="/mtestdrive/carInfoAction.action?monitor&id=${row.id}" class="btn btn-primary " style="color: white;">&nbsp;&nbsp;&nbsp;查&nbsp;&nbsp;看&nbsp;&nbsp;&nbsp;</a>
																</td>
															</tr>
														</tbody>
													</table>
												</c:forEach>
											</div>
										</div>
									</div>
								</div>
								<div class="panel panel-default" style="margin-bottom:10px;">
									<div class="panel-heading" role="tab" id="heading4">
										<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse4" aria-expanded="false" aria-controls="collapse4">
											<h4 class="panel-title">试驾已完成 <span class="label label-default pull-right">${finishDrive.size() }</span></h4>
										</a>
									</div>
									<div id="collapse4" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading4">
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
																<td>
																	<fmt:formatDate value="${row.orderStartTime}" type="date" pattern="yyyy.MM.dd  HH:mm" />
																	-<fmt:formatDate value="${row.orderEndTime}" type="date" pattern="HH:mm" />
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
																<td colspan="2"><a href="vehicle_condition.html">
																	<a href="/mtestdrive/driveRecodsAction.action?report&id=${row.id}" class="btn btn-primary" style="color: white;">查看报告</a>																	
																</a></td>
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
	
			</div>
	
		
			<!--<div style="height: 100px;"></div>-->
				
					<footer class="text-center" style="background:#FFF; width:100%; position:fixed;bottom:0px;">
					<a href="/mtestdrive/login.action?page" style="display: inline-block; float: left; width:25% ;margin-bottom: 8px;font-weight: bold !important;color: #000000; " >
						<div class="col-xs-3 nenuActi" style=" width:100%;height: 100% ">
							<span class="glyphicon glyphicon-home" ></span>
							<h6>首页</h6>
						</div>
						<div class="clearfix"></div>
					</a> <a href="/mtestdrive/driveRecodsAction.action?index" style="display: inline-block;float: left; width:25% ;font-weight: bold !important; color: #000000; ">
						<div class="col-xs-3" style="width:100%;height: 100%" >
							<span class="glyphicon glyphicon-credit-card "></span>
							<h6>试驾管理</h6>
						</div>
						<div class="clearfix"></div>
					</a>
					 <a href="/mtestdrive/customerInfoAction.action?index" style="display: inline-block;float: left; width:25% ;font-weight: bold !important; color: #000000; ">
						<div class="col-xs-3" style="width:100%;height: 100%" >
							<span class="glyphicon glyphicon-user"></span>
							<h6>我的客户</h6>
						</div>
					</a> <a href="/mtestdrive/carInfoAction.action?index" style="display: inline-block;  float: left; width:25%;font-weight: bold !important; color: #000000; ">
						<div class="col-xs-3" style="width:100%;height: 100%">
							<span class="glyphicon glyphicon-list"></span>
							<h6>车辆管理</h6>
						</div>
						<div class="clearfix"></div>
					</a>
					<div class="clearfix"></div>
				</footer>
			

		
			</div>
			
		</div>
	</div>
<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/swiper.min.js"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>
<script type="text/javascript" src="plug-in/jquery/jquery.slimscroll.min.js" ></script>
	<script type="text/javascript">
	var hig=	$(window).height(); //浏览器当前窗口可视区域高度
	
	 $('#testDiv').slimscroll({
		 alwaysVisible: true,
		 height: hig,
		 size: '5px'
	 });
	
	
	
			var potentialNumber = null;
			try{
				potentialNumber = ${potentialDrive.size() };
			}catch(e){
				potentialNumber = 0;
			}
			function confirm(id,a){
				layer.confirm('确认此条预约信息？', {
					  btn: ['确认','取消'] //按钮
					}, function(){
						$.ajax({
							url:"/mtestdrive/driveRecodsAction.action?confirm",
							type:"post",
							data:JSON.stringify({"id" : id}),
							dataType:"json",
							contentType:'application/json',
							success:function(data){
								layer.msg('已确认！', {icon: 1});
								$(a).parent().parent().parent().parent().hide();
								$("#potentialNumber").html(--potentialNumber);
							}
						});
					}, function(){
					  
					});
			}
			var i = 1;
			setInterval(function() {
				if(i < '${sessionScope.SalesmanInfo.ceil}') {
					i++;
				}
				$("#bar").css("width", i + "%");
				$("#bar").html(i + "%");

			}, 100);

			var toggleMenu = function() {
					if(swiper.previousIndex == 0)
						swiper.slidePrev()
				},
				menuButton = document.getElementsByClassName('menu-button')[0],
				swiper = new Swiper('.swiper-container', {
					slidesPerView: 'auto',
					initialSlide: 1,
					resistanceRatio: .00000000000001,
					onSlideChangeStart: function(slider) {
						if(slider.activeIndex == 0) {
							menuButton.classList.add('cross');
							menuButton.removeEventListener('click', toggleMenu, false)
						} else
							menuButton.classList.remove('cross')
					},
					onSlideChangeEnd: function(slider) {
						if(slider.activeIndex == 0)
							menuButton.removeEventListener('click', toggleMenu, false)
						else
							menuButton.addEventListener('click', toggleMenu, false)
					},
					slideToClickedSlide: true
				})
				
			  function out(){
					layer.confirm('确定注销？',{
						btn: ['确定','取消']
					},function(){
						window.location.href="/mtestdrive/login.action?out";
					},function(){
						
					});
					
					//window.location.href="/mtestdrive/login.action?out";
				}
</script>
</body>
</html>