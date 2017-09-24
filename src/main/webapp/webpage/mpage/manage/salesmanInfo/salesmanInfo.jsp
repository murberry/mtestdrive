<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>销售顾问信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="salesmanInfoController.do?save">
			<input id="id" name="id" type="hidden" value="${salesmanInfoPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							工号:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="name" name="employeeNo" datatype="s2-10"
							   value="${salesmanInfoPage.employeeNo}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							中文名:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="name" name="name" datatype="s2-10"
							   value="${salesmanInfoPage.name}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							英文名:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="englishName" name="englishName" datatype="s2-10"
							   value="${salesmanInfoPage.englishName}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							手机号码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="mobile" name="mobile" autocomplete="off" datatype="n11-11"
							   value="${salesmanInfoPage.mobile}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							登录密码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="mobile" name="password" autocomplete="off" datatype="s6-18" type="password"
							   value="${salesmanInfoPage.password}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							入职日期:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" style="width: 150px" id="entryTime" name="entryTime" datatype="*15-20"
							   value="<fmt:formatDate value='${salesmanInfoPage.entryTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				
			</table>
		</t:formvalid>
 </body>