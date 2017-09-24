<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>设备</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript">
	function updateSaleman(){
		$("#formobj").submit();
	}
</script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="driveDistributionController.do?updateSaleman">
	<input id="id" name="id" type="hidden" value="${driveRecods.id }">
	<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		
		<tr>
			<td align="right" width="10%" nowrap><label class="Validform_label"> 经销商顾问: </label></td>
			<td class="value" width="10%">
			<select id="salemanId" name="salemanId">
						  		<option value="">--- 全部 ---</option>
						  		<c:forEach items="${salesmanList }" var="row">
						  		<option value="${row.id }">--- ${row.name }---</option>
						  		</c:forEach>
						  		</select>
                <span class="Validform_checktip"></span>
            </td>
		</tr>
	</table>
	<a onclick="updateSaleman();" class="l-btn">
		<span class="l-btn-left">
			<span class="l-btn-text">提交</span>
		</span>
	</a>
</t:formvalid>
</body>
