<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="obdDriveRecodsList" title="OBD试驾表" actionUrl="obdDriveRecodsController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="4S经销商id" field="agencyId"   width="120"></t:dgCol>
   <t:dgCol title="试驾车id" field="carId"   width="120"></t:dgCol>
   <t:dgCol title="销售代表id" field="salesmanId"   width="120"></t:dgCol>
   <t:dgCol title="客户id" field="customerId"   width="120"></t:dgCol>
   <t:dgCol title="预约开始时间" field="orderStartTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="预约结束时间" field="orderEndTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="试驾开始时间" field="driveStartTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="时间结束时间" field="driveEndTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="开始前照片存放地址" field="startPicPath"   width="120"></t:dgCol>
   <t:dgCol title="结束后照片存放地址" field="endPicPath"   width="120"></t:dgCol>
   <t:dgCol title="行驶里程数" field="mileage"   width="120"></t:dgCol>
   <t:dgCol title="试驾反馈" field="feedback"   width="120"></t:dgCol>
   <t:dgCol title="创建时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="修改时间" field="updateTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="0-无效试驾，1-有效试驾，2-非预约试驾" field="status"   width="120"></t:dgCol>
   <t:dgCol title="试驾合同照片" field="contractPicPath"   width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="obdDriveRecodsController.do?del&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="obdDriveRecodsController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="obdDriveRecodsController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="obdDriveRecodsController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>