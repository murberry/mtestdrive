<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/apptags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	    <base href="<%=basePath%>/" />
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
		<%@include file="/context/ico.jsp"%>
		<title>调查问卷</title>
		<link rel="stylesheet" href="plug-in/bootstrap.min/css/bootstrap.min.css" />
		<link rel="stylesheet" href="css/maser.css" />
	</head>

	<body>
		<!--
    	描述：响应式logo图片
    -->
		<header>
			<div class="container text-center "  >
				<div style="margin: 0 auto;   width:80%;height:90px; background-image: url(images/logo.jpg);background-position:center;background-size:contain ; background-repeat:no-repeat; ">
				<input name="driveId" id="driveId" type="hidden" value="${driveId }">
            </div>
			</div>
		</header>
		<section class="container-fluid">
			<form>
				<table class="table reporter">
					<thead>

					</thead>
					<tbody>
						<tr>
							<th colspan="3">试驾时您对车辆的整体表现是否满意？</th>
						</tr>
						<tr>
							<td>
								<div class="radio">
									<label>
        <input type="radio"  value="1" name="group_1">不满意
  </label>
								</div>
							</td>
							<td>
								<div class="radio">
									<label>
    <input type="radio" value="2" name="group_1">满意
  </label>
								</div>
							</td>
							
						</tr>
						<tr>
							<th colspan="3">试驾后您是否考虑购买？</th>
						</tr>
						<tr>
							<td>
								<div class="radio">
									<label>
        <input type="radio"  value="1" name="group_2">是
  </label>
								</div>
							</td>
							<td>
								<div class="radio">
									<label>
    <input type="radio" value="2" name="group_2">不确定
  </label>
								</div>
							</td>
							<td>
								<div class="radio">
									<label>
    <input type="radio" value="3" name="group_2">否
  </label>
								</div>
							</td>
						</tr>
						<tr>
							<th colspan="3">我们的试驾路线您是否满意？</th>
						</tr>
						<tr>
							<td>
								<div class="radio">
									<label>
        <input type="radio"  value="1" name="group_3">非常满意
  </label>
								</div>
							</td>
							<td>
								<div class="radio">
									<label>
    <input type="radio" value="2" name="group_3">一般满意
  </label>
								</div>
							</td>
							<td>
								<div class="radio">
									<label>
    <input type="radio" value="3" name="group_3">不满意
  </label>
								</div>
							</td>
						</tr>

						<tr>
							<th colspan="3">试驾过程中我们的销售是否讲解专业？</th>
						</tr>
						<tr>
							<td>
								<div class="radio">
									<label>
        <input type="radio"  value="1" name="group_4">非常专业
  </label>
								</div>
							</td>
							<td>
								<div class="radio">
									<label>
    <input type="radio" value="2" name="group_4">一般专业
  </label>
								</div>
							</td>
							<td>
								<div class="radio">
									<label>
    <input type="radio" value="3" name="group_4">不专业
  </label>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				
				<div class="container text-center" style="    margin-bottom: 20px;">
				<button type="button" class="btn btn-primary btn-lg theme_color" onclick="report()">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;完成
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</button>
				</div>
				
			</form>
		</section>

	</body>
    <script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="plug-in/layer/layer.js"></script>
	<script type="text/javascript">
		function report(){
			var  group_1 =$("input[name='group_1']:checked").val();
			var  group_2 =$("input[name='group_2']:checked").val();
			var  group_3 =$("input[name='group_3']:checked").val();
			var  group_4 =$("input[name='group_4']:checked").val();
			var driveId ='${param.testDriveId}';
			var group = {
					"group_1" : group_1,
					"group_2" : group_2,
					"group_3" : group_3,
					"group_4" : group_4,
					"driveId" : driveId
				};	
			$.ajax({
				url:"/mtestdrive/sysAction.action?report",
				type:"post",
				data:JSON.stringify(group),
				dataType:"json",
				contentType:'application/json',
				success:function(data){
					layer.msg("提交成功！");
					setTimeout("location.href = 'http://www.maserati.com.cn/maserati/cn/zh/';", 2000);
				}
			});
			
		}
	</script>
</html>