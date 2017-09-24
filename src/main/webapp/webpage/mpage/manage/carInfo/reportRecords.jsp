<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>报备信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="reportRecordsController.do?save">
			<input id="id" name="id" type="hidden" value="${reportRecordsPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							报备原因类型:
						</label>
					</td>
					<td class="value">
					<input type="hidden" name="carId" value="${carId}">
					<t:dictSelect hasLabel="false" field="type" id="causeType" typeGroupCode="reportType" defaultVal="${reportRecordsPage.type}"></t:dictSelect>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							报备开始时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" readonly="readonly" required="required" datatype="*"
						onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" style="width: 150px" id="startTime" name="startTime" 
							     value="<fmt:formatDate value='${reportRecordsPage.startTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							报备结束时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" readonly="readonly" required="required"  datatype="*"
						onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" style="width: 150px" id="endTime" name="endTime" 
							     value="<fmt:formatDate value='${reportRecordsPage.endTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							具体原因:
						</label>
					</td>
					<td class="value">
						<input style="width:152px" type="text" id="remark" name="remark" datatype="*"
							   value="${reportRecordsPage.remark}" >
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
	<script type="text/javascript">
		$(function(){
			$("#causeType").find("option:selected").remove(); 
			//$("#causeType option[index='0']").remove(); 
		})
	</script>		
 </body>