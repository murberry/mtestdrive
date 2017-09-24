var countdown = 60;
var changeTimeInterval = null;
function sendAuthCode(obj) {
	if($(obj).attr("disabled") == undefined || $(obj).attr("disabled") != "disabled"){
		// 发送请求
		$.ajax({
			type : 'POST',
			url : 'sysAction.action?sendAuthCode&tel=' + $("#phone").val(),
			dataType : 'JSON',
			success : function(data) {
				if (!data.success) {
					layer.msg(data.msg);
				} else {
					console.log("发送成功");
					changeTimeInterval = window.setInterval(function(){
						if(countdown == 0){
							countdown = 60;
							$(obj).attr("disabled", false);
							$(obj).val("获取验证码");
							window.clearInterval(changeTimeInterval);
						}else{
							$(obj).attr("disabled", true);
							$(obj).val("重新发送（"+countdown+"S）");
							countdown--;
						}
					},1000); 
				}
			},
			error : function() {
				layer.msg("发送失败，请联系我们");
			}
		});
	}
}

$(function(){
	$(".accountInfo").keydown(function(){
		var isNotEmpty = true; 
		$(".accountInfo").each(function(){
			if($(this).val() == "" || $(this).val() == null || $(this).val() == undefined){
				isNotEmpty = false;
				return false;
			}
		});
		if(isNotEmpty){
			$("#submitBtn").removeAttr("disabled");
		}else{
			$("#submitBtn").attr("disabled", true);
		}
	});
	$(".accountInfo").keyup(function(){
		var isNotEmpty = true; 
		$(".accountInfo").each(function(){
			if($(this).val() == "" || $(this).val() == null || $(this).val() == undefined){
				isNotEmpty = false;
				return false;
			}
		});
		if(isNotEmpty){
			$("#submitBtn").removeAttr("disabled");
		}else{
			$("#submitBtn").attr("disabled", true);
		}
	});
	$("#submitBtn").click(function(){
		// 发送请求，校验  验证码
		$.ajax({
			type : 'POST',
			url : 'sysAction.action?checkPhoneAndAhthCode',
			dataType : 'JSON',
			data:{
				"phone":$("#phone").val(),
				"code":$("#code").val()
			},
			success : function(data) {
				if (!data.success) {
					layer.msg(data.msg);
				} else {
					layer.msg(data.msg);
					location.href="login.action?jumpToSetNewPasswordPage&mobile="+$("#phone").val();
				}
			},
			error : function(e) {
				console.log(JSON.stringify(e));
				layer.msg("校验码错误");
			}
		});
	});
})