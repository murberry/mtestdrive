<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>出车明细</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body >
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="driveRecodsController.do?save">
			<input id="id" name="id" type="hidden" value="${driveRecodsPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							经销商:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="agency.name" name="agency.name" ignore="ignore" value="${driveRecodsPage.agency.name}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾车:
						</label>
					</td>
					<td class="value">
					<t:dictSelect field="carId" dictTable="t_car_info" dictField="id" dictText="name" defaultVal="${driveRecodsPage.carId }" dictCondition=" where agency_id='${driveRecodsPage.agency.id}'"></t:dictSelect>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							销售代表:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="salesman.name" name="salesman.name" ignore="ignore" value="${driveRecodsPage.salesman.name}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							客户姓名:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="customer.name" name="customer.name" ignore="ignore" value="${driveRecodsPage.customer.name}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							开始时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" style="width:150px" id="orderStartTime" name="orderStartTime" ignore="ignore" value="<fmt:formatDate value='${driveRecodsPage.orderStartTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							结束时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" style="width:150px" id="orderEndTime" name="orderEndTime" ignore="ignore" value="<fmt:formatDate value='${driveRecodsPage.orderEndTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							驾驶员:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="driver" name="driver" ignore="ignore" value="${driveRecodsPage.driver}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
		<script type="text/javascript">
		window.onload=function(){}
		
		
		</script>
		
 </body>