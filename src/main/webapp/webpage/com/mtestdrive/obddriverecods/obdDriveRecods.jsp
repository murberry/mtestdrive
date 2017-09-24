<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>OBD试驾表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="obdDriveRecodsController.do?save">
			<input id="id" name="id" type="hidden" value="${obdDriveRecodsPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							4S经销商id:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="agencyId" name="agencyId" ignore="ignore"
							   value="${obdDriveRecodsPage.agencyId}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾车id:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="carId" name="carId" ignore="ignore"
							   value="${obdDriveRecodsPage.carId}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							销售代表id:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="salesmanId" name="salesmanId" ignore="ignore"
							   value="${obdDriveRecodsPage.salesmanId}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							客户id:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="customerId" name="customerId" ignore="ignore"
							   value="${obdDriveRecodsPage.customerId}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							预约开始时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="orderStartTime" name="orderStartTime" ignore="ignore"
							     value="<fmt:formatDate value='${obdDriveRecodsPage.orderStartTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							预约结束时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="orderEndTime" name="orderEndTime" ignore="ignore"
							     value="<fmt:formatDate value='${obdDriveRecodsPage.orderEndTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾开始时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="driveStartTime" name="driveStartTime" ignore="ignore"
							     value="<fmt:formatDate value='${obdDriveRecodsPage.driveStartTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							时间结束时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="driveEndTime" name="driveEndTime" ignore="ignore"
							     value="<fmt:formatDate value='${obdDriveRecodsPage.driveEndTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							开始前照片存放地址:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="startPicPath" name="startPicPath" ignore="ignore"
							   value="${obdDriveRecodsPage.startPicPath}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							结束后照片存放地址:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="endPicPath" name="endPicPath" ignore="ignore"
							   value="${obdDriveRecodsPage.endPicPath}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							行驶里程数:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="mileage" name="mileage" ignore="ignore"
							   value="${obdDriveRecodsPage.mileage}" datatype="d">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾反馈:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="feedback" name="feedback" ignore="ignore"
							   value="${obdDriveRecodsPage.feedback}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							创建时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="createTime" name="createTime" ignore="ignore"
							     value="<fmt:formatDate value='${obdDriveRecodsPage.createTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							修改时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width: 150px" id="updateTime" name="updateTime" ignore="ignore"
							     value="<fmt:formatDate value='${obdDriveRecodsPage.updateTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							0-无效试驾，1-有效试驾，2-非预约试驾:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="status" name="status" ignore="ignore"
							   value="${obdDriveRecodsPage.status}" datatype="n">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾合同照片:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="contractPicPath" name="contractPicPath" ignore="ignore"
							   value="${obdDriveRecodsPage.contractPicPath}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>