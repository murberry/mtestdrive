<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="libList" title="字典" actionUrl="libController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="类型" field="type"   width="120"></t:dgCol>
   <t:dgCol title="编码" field="code"   width="120"></t:dgCol>
   <t:dgCol title="名称" field="name"   width="120"></t:dgCol>
   <t:dgCol title="值" field="value"   width="120"></t:dgCol>
   <t:dgCol title="状态 0:无效; 1:有效" field="status"   width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="libController.do?del&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="libController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="libController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="libController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>