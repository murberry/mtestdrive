$(function() {
	setImg();
	// 如果是添加或者编辑，隐藏区域信息、经销商信息、销售顾问信息
	if (location.href.indexOf("addorupdate") != -1
			&& location.href.indexOf("load=detail") == -1) {
		$(".common_info").hide();
		$(".drivingLicense_img").remove();
	} else {
		$(".drivingLicense").remove();
	}
	
	$('#file').live('change',function(){
		var filePath = $(this).val();
		if(filePath !=  null && filePath.length > 0){
			//有选中值，开始异步上传
			uploadFile();
		}
	});

})
function preview(file) {
	var prevDiv = $('#preview');
	if (file.files && file.files[0]) {
		var reader = new FileReader();
		reader.onload = function(evt) {
			prevDiv.html('<img src="' + evt.target.result + '" />');
		}
		reader.readAsDataURL(file.files[0]);
	} else {
		prevDiv.html('<div class="img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\''
				+ file.value + '\'"></div>');
	}
}
function setImg(){
	var imgPath = $("#drivingLicensePicPath").val();
	if(imgPath != null && imgPath.length > 0){
		$('#preview').html('<img src="fileUploadController.do?view&fileName=' + imgPath + '" />');
	}
}


function uploadFile(){
	var formData = new FormData();
	formData.append('file[]', $('#file')[0].files[0]);
	$.ajax({
	    url: 'fileUploadController.do?upload',
	    type: 'POST',
	    cache: false,
	    data: formData,
	    processData: false,
	    contentType: false,
	    dataType:'JSON'
	})
	.done(function(res) {
		try{
			$("#drivingLicensePicPath").val(res[0].fileName);
		}catch(err){
			alertTip("文件上传失败，请联系管理员","提示信息");
		}
	})
	.fail(function(res) {
		alertTip("文件上传失败，请联系管理员","提示信息");
	}); 
}