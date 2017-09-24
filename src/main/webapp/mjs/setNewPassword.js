$(function(){
	$("#submitBtn").click(function(){
		// 发送请求，校验  验证码
		$.ajax({
			type : 'POST',
			url : 'login.action?setNewPassword&' + $("#usrInfoForm").serialize(),
			dataType : 'JSON',
			success : function(data) {
				if (!data.success) {
					layer.msg(data.msg);
				} else {
					layer.msg("修改成功");
					setTimeout(function(){
						location.href="login.action?login&mobile="+mobile;
					},1500);
				}
			},
			error : function() {
				layer.msg("请求失败，请联系我们");
			}
		});
	});
})