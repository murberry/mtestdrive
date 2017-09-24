<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>设备</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript">
	function obdSaveUser(){
		$("#formobj").submit();
	}
</script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="carInfoController.do?obdSaveUser">
	<input id="id" name="id" type="hidden" value="${carInfo.id }">
	<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		
		<tr>
			<td align="right" width="10%" nowrap><label class="Validform_label"> obd设备ID号: </label></td>
			<td class="value" width="10%">
                <input id="obdId" class="inputxt" name="obdId" value="${carInfo.obdId }" >
                <span class="Validform_checktip"></span>
            </td>
		</tr>
	</table>
	<a onclick="obdSaveUser();" class="l-btn">
		<span class="l-btn-left">
			<span class="l-btn-text">提交</span>
		</span>
	</a>
</t:formvalid>
</body>
