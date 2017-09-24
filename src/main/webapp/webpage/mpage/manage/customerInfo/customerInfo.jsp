<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>客户信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style>
 #preview, .img, img  
 {  
 width:156px;  
 height:156px;  
 }  
 #preview  
 {  
  
} 
  </style>
 </head>
 <body style="overflow-y: auto;overflow-x: hidden;" >
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="customerInfoController.do?save">
			<input id="id" name="id" type="hidden" value="${customerInfoPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr class="common_info">
					<td align="right">
						<label class="Validform_label">
							区域:
						</label>
					</td>
					<td class="value">
						<t:dictSelect field="regionId" id="region"  typeGroupCode="region"  defaultVal="${customerInfoPage.regionName}"	hasLabel="false" datatype="s2-10"></t:dictSelect>
					</td>
				</tr>
				
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							客户姓名:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="name" name="name" datatype="s2-10"
							   value="${customerInfoPage.name}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							性别 :
						</label>
					</td>
					<td class="value">
					    <input type="radio" name="gender" value="1"
					    <c:if test="${customerInfoPage.gender == 1}">checked</c:if>
					    />男
					    <input type="radio" name="gender" value="2"
					    <c:if test="${customerInfoPage.gender == 2}">checked</c:if>
					    />女
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							出生日期:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker()" type="text" style="width: 150px" id="birthday" name="birthday" datatype="*5-15"
							   value="<fmt:formatDate value='${customerInfoPage.birthday}' type="date" pattern="yyyy-MM-dd"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<%-- <tr>
					<td align="right">
						<label class="Validform_label">
							身份证号:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="idCard" name="idCard" ignore="ignore"
							   value="${customerInfoPage.idCard}">
						<span class="Validform_checktip"></span>
					</td>
				</tr> --%>
				<%-- <tr>
					<td align="right">
						<label class="Validform_label">
							手机号码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="mobile" name="mobile" ignore="ignore"
							   value="${customerInfoPage.mobile}">
						<span class="Validform_checktip"></span>
					</td>
				</tr> --%>
				<%-- <tr>
					<td align="right">
						<label class="Validform_label">
							驾照号码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="drivingLicense" name="drivingLicense" ignore="ignore"
							   value="${customerInfoPage.drivingLicense}">
						<span class="Validform_checktip"></span>
					</td>
				</tr> --%>
				
				
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							驾照:
						</label>
					</td>
					<td class="value">
						<div id="preview" style="border:1px solid #000;"></div>  
						<input type="file" onchange="preview(this)" id="file" style="width:154px !important;"/>  
						<input class="inputxt " type="hidden" id="drivingLicensePicPath" name="drivingLicensePicPath" datatype="s2-50"
							   value="${customerInfoPage.drivingLicensePicPath}">
					</td>
				</tr>
				
			</table>
		</t:formvalid>
		<script type="text/javascript" src="plug-in/jquery-plugs/fileupload/js/jquery.fileupload.js"></script>
		<script type="text/javascript" src="mjs/customerInfo.js"></script>
 </body>