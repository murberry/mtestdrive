<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="salesmanInfoList" title="销售顾问信息" actionUrl="salesmanInfoController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
  <%--  <t:dgCol title="销售区域id" field="regionId"   width="120" hidden="true"></t:dgCol>
   <t:dgCol title="4S销售商id" field="agencyId"   width="120" hidden="true"></t:dgCol> --%>
   <t:dgCol title="姓名" field="name"   width="120" query="true"></t:dgCol>
   <t:dgCol title="英文名" field="englishName"   width="120" query="true"></t:dgCol>
   <t:dgCol title="工号" field="employeeNo"   width="120" query="true"></t:dgCol>
   <t:dgCol title="经销商" field="agencyId" dictionary="t_agency_info,id,name" query="true"  width="120"></t:dgCol>
   <%-- <t:dgCol title="座机号码" field="telephone"   width="120" query="true"></t:dgCol> --%>
   <t:dgCol title="手机号码" field="mobile"   width="120" query="true"></t:dgCol>
   <t:dgCol title="头像" field="headPortraitPicPath"   width="120" hidden="true"></t:dgCol>
   <t:dgCol title="状态 0:无效; 1:有效" field="status"   width="120" hidden="true"></t:dgCol>
   <t:dgCol title="创建时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="修改时间" field="updateTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="入职时间" field="entryTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt operationCode="DELSALESMAN" title="删除" url="salesmanInfoController.do?del&id={id}" />
   <t:dgToolBar operationCode="ADDSALESMAN" title="录入" icon="icon-add" url="salesmanInfoController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar operationCode="EDITSALESMAN" title="编辑" icon="icon-edit" url="salesmanInfoController.do?addorupdate" funname="update"></t:dgToolBar>
   <%-- <t:dgToolBar title="查看" icon="icon-search" url="salesmanInfoController.do?addorupdate" funname="detail"></t:dgToolBar> --%>
  </t:datagrid>
  </div>
 </div>