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
<link rel="stylesheet" href="css/maser.css" />
<link href="css/mobiscroll_002.css" rel="stylesheet" type="text/css" />
<link href="css/mobiscroll.css" rel="stylesheet" type="text/css" />
<link href="css/mobiscroll_003.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<!--顶部标题开始 -->
	<header>
		<div class="container top">
			<div ass="row">
				<div class="col-xs-2">
					<a href="customerInfoAction.action?index" class="back"><span
						class="glyphicon glyphicon-chevron-left"></span></a>

				</div>
				<div class="col-xs-8">
					<div class="add_customer">客&nbsp;&nbsp;&nbsp;户</div>
				</div>
			</div>
		</div>
	</header>
	<!--顶部标题结束 -->
	<div class="container show" style="padding-top: 35px;">
		<section>
			<form id="form1" class="form-horizontal text-center" role="form" action="/mtestdrive/customerInfoAction.action?save" method="post">
				<div class="form-group ">
				<input type="hidden" id="id" name="id" class="form-control" value="${customerInfo.id }">
					<div class="col-xs-4  compellation">渠&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;道:</div>
					<div class="col-xs-8  distance">
						<select id="quarry" name="quarry" class="form-control">
						<c:forEach items="${ quarry}" var="row">
						<c:if test="${customerInfo.quarry ==row.typecode }">
						<option value="${row.typecode }" selected="selected">${row.typename }</option>
						</c:if>
						<c:if test="${customerInfo.quarry !=row.typecode }">
						<option value="${row.typecode }">${row.typename }</option>
						</c:if>
						</c:forEach>
							

						</select>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-4  compellation">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</div>
					<div class="col-xs-8  distance">
						<input id="name" type="text" name="name" class="form-control" value="${customerInfo.name }">
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-4 compellation">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别:</div>
					<div class="col-xs-4 sex_distance">
						<input type="radio" name="gender" <c:if test="${customerInfo.gender==1 }">checked="checked"</c:if> value="1"><span>男</span>
					</div>
					<div class="col-xs-4 sex_distance">
						<input type="radio" name="gender" <c:if test="${customerInfo.gender==2 }">checked="checked"</c:if> value="2"><span>女</span>
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-4 compellation">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话:</div>
					<div class="col-xs-8  distance">
						<input id="mobile" type="text" name="mobile" class="form-control" value="${customerInfo.mobile }">
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-4 compellation" class="demos">出生日期:</div>
					<div class="col-xs-8 distance">
						<input placeholder="选择日期" value="<fmt:formatDate value="${customerInfo.birthday}" type="both" pattern="yyyy-MM-dd"/>" class="form-control" readonly name="birthday" id="appDate" type="text" style="background: #FFF;">
					</div>
				</div>
				<%-- <div class="form-group">
					<div class="col-xs-4 compellation" class="demos">驾&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;照:</div>
					<div class="col-xs-8 distance" style="padding-right: 15px; margin-top: 10px;">
						<div class="photo">
							<div class="pic">
								<input id = "drivingLicensePicPath" name="drivingLicensePicPath" type="text" value="${customerInfo.drivingLicensePicPath }">
								<input id="img" type="file"  data-url="fileUpload.action?upImg" accept="image/*" capture="camera" style="border: 1px solid red" />
								<c:if test="${empty customerInfo.drivingLicensePicPath }">
									<span>上传驾照</span>
								</c:if>
							</div>
							<div id="photo" style="border: 1px solid #ccc;width: 100%;border-radius:5px; "></div>
						</div>

					</div>
				</div> --%>
				<div class="form-group">
					<div class="col-xs-4">&nbsp;&nbsp;&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;注:</div>
					<div class="col-xs-8" style="padding-left: 0px;">
						<textarea id="remark" style="margin-bottom: 15px;"  class="form-control" name="remark" >${customerInfo.remark }</textarea>
					</div>
				</div>
				<div class="text-center btn_kongxi "
					style="margin-bottom: 55px; margin-top: 0px;">
					<div class="row">
						<div class="col-xs-6">
							<button type="button" id="sub" class="btn btn-primary ensure">
								确定
							</button>
						</div>
						<div class="col-xs-6">
							<a href="/mtestdrive/customerInfoAction.action?index" type="button" class="btn btn-primary ensure">取消</a>
						</div>
					</div>

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

<script type="text/javascript" src="plug-in/bootstrap.min/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="plug-in/bootstrap.min/js/bootstrap.min.js"></script>
<script src="plug-in/jquery-plugs/fileupload/js/jquery.1.9.1.min.js"></script>
<script src="plug-in/jquery-plugs/fileupload/js/vendor/jquery.ui.widget.js"></script>
<script src="plug-in/jquery-plugs/fileupload/js/jquery.iframe-transport.js"></script>
<script src="plug-in/jquery-plugs/fileupload/js/jquery.fileupload.js"></script>
<script src="js/mobiscroll_002.js" type="text/javascript"></script>
<script src="js/mobiscroll.js" type="text/javascript"></script>
<script src="js/mobiscroll_003.js" type="text/javascript"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>


<script type="text/javascript">
	$(function() {
			var currYear = (new Date()).getFullYear();
			var opt = {};
			opt.date = {
				preset: 'date'
			};
			opt.datetime = {
				preset: 'datetime'
			};
			opt.time = {
				preset: 'time'
			};
			opt.default = {
				theme: 'android-ics light', //皮肤样式
				display: 'modal', //显示方式 
				mode: 'scroller', //日期选择模式
				dateFormat: 'yyyy-mm-dd',
				stepHour: 1,
                stepMinute: 60,
                 stepSecond: 60,
				lang: 'zh',
				showNow: true,
				nowText: "今天",
				startYear: currYear - 60, //开始年份
				endYear: currYear + 10, //结束年份

			};

			$("#appDate").mobiscroll($.extend(opt['date'], opt['default']));
			if($("#appDate").val()){
				$("#appDate").scroller('setDate', new Date($("#appDate").val()), true);
			}
		  	
		  	$("#appDate1").mobiscroll($.extend(opt['date'], opt['default']));
			  	var optTime = $.extend(opt['time'], opt['default']);
			  $("#appTime").mobiscroll(optTime).time(optTime);
			   $("#appTime1").mobiscroll(optTime).time(optTime);

		});
	$(function() {
		var Path= '${customerInfo.drivingLicensePicPath }'!='';
		if(Path){
			var src = 'fileUpload.action?view&fileName='+'${customerInfo.drivingLicensePicPath }';
			$('#photo').html('<img src="' + src + '" alt="" />');
		}
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
			autoUpload : "ture",
			dataType:'JSON',
			done:function(result,data){
				$("#drivingLicensePicPath").val(data.result[0].fileName);
			}
		})
		
		
		$("#sub").bind("click",function(){
			var i = 1;
			var quarry = $("#quarry").val();
			if(quarry.length==0){
				layer.msg('请选择渠道！');
				return false;
			}
			var name = $("#name").val();
			if(name.length==0){
				layer.msg('请填写姓名！');
				return false;
			}
			/* var  gender =$("input[name='gender']:checked").val();
			if(gender!=1 && gender!=2){
				layer.msg('请选择性别！');
				return false;
			} */
			var mobile = $("#mobile").val();
			if(!validatemobile(mobile)){
				$("#mobile").focus();
				return false;
			}
			/* var appDate = $("#appDate").val();
			if(appDate.length==0){
				layer.msg('请填写生日！');
				return false;
			} */
			/* var drivingLicensePicPath = $("#drivingLicensePicPath").val();
			if(drivingLicensePicPath.length==0){
				layer.msg('请上传驾照！');
				return false;
			} */
			/* var remark = $("#remark").val();
			if(remark.length==0){
				
			} */
			//添加手机号为依据是否存在相同的客户
			//先看下是不是修改
			var id = $("#id").val();
			if(id.length==0){
				//没有ID 新增
				var drive = {
				        "mobile" : mobile
					};
				$.ajax({
					url:"customerInfoAction.action?check",
					type:"post",
					data:JSON.stringify(drive),
					dataType:"json",
					contentType:'application/json',
					success:function(data){
						if(data.obj=="2"){
							layer.msg("手机号已重复！");
							return false;
						}
						if(data.obj=="1"){
							layer.msg("操作成功");
							 $('#form1').submit();
						}
					}
				});
				
			}else{
				//修改
				var drive = {
						"id" : id,
				        "mobile" : mobile
					};
				$.ajax({
					url:"/mtestdrive/customerInfoAction.action?checkById",
					type:"post",
					data:JSON.stringify(drive),
					dataType:"json",
					contentType:'application/json',
					success:function(data){
						if(data.obj=="2"){
							layer.msg("手机号已重复！");
							return false;
						}
						if(data.obj=="1"){
							layer.msg("操作成功");
							 $('#form1').submit();
						}
					}
				});
				
			}
			
			
			
			/* layer.msg('操作成功！');
			return true;  */
			
			
		})
	})
			
	
	function validatemobile(mobile) 
   { 
       if(mobile.length==0) 
       { 
    	  layer.msg('请输入电话！'); 
          return false; 
       }     
       if(mobile.length!=11) 
       { 
    	   layer.msg('请输入有效的电话！'); 
           return false; 
       } 
       var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
       if(!myreg.test(mobile)) 
       { 
    	   layer.msg('请输入有效的电话！'); 
           return false; 
       } 
       return true;
   } 
	
	
</script>


</html>