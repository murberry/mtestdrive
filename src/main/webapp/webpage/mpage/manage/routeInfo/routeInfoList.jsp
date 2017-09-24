<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="routeInfoList" title="试驾路线" actionUrl="routeInfoController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="经销商名称" field="agencyId" dictionary="t_agency_info,id,name"  width="120"></t:dgCol>
   <t:dgCol title="线路名称" field="name"  width="120" ></t:dgCol>
   <t:dgCol title="路线图存放地址" field="routePicPath"  width="120" hidden="true"></t:dgCol>
   <t:dgCol title="线路描述" field="remark" autocomplete="-"  width="120"></t:dgCol>
   <t:dgCol title="创建时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="修改时间" field="updateTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="routeInfoController.do?del&id={id}" operationCode="DELROUTE"/>
   <t:dgToolBar operationCode="ADDROUTE" title="录入" icon="icon-add" url="routeInfoController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar operationCode="EDITROUTE" title="编辑" icon="icon-edit" url="routeInfoController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="routeInfoController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script>
 function add(title,addurl,gname,width,height) {
		gridname=gname;
		createwindow(title, addurl,"1000",height);
	}
 /**
  * 更新事件打开窗口
  * @param title 编辑框标题
  * @param addurl//目标页面地址
  * @param id//主键字段
  */
 function update(title,url, id,width,height,isRestful) {
 	gridname=id;
 	var rowsData = $('#'+id).datagrid('getSelections');
 	if (!rowsData || rowsData.length==0) {
 		tip('请选择编辑项目');
 		return;
 	}
 	if (rowsData.length>1) {
 		tip('请选择一条记录再编辑');
 		return;
 	}
 	if(isRestful!='undefined'&&isRestful){
 		url += '/'+rowsData[0].id;
 	}else{
 		url += '&id='+rowsData[0].id;
 	}
 	createwindow(title,url,"1000",height);
 }
 /**
  * 查看详细事件打开窗口
  * @param title 查看框标题
  * @param addurl//目标页面地址
  * @param id//主键字段
  */
 function detail(title,url, id,width,height) {
 	var rowsData = $('#'+id).datagrid('getSelections');
// 	if (rowData.id == '') {
// 		tip('请选择查看项目');
// 		return;
// 	}
 	
 	if (!rowsData || rowsData.length == 0) {
 		tip('请选择查看项目');
 		return;
 	}
 	if (rowsData.length > 1) {
 		tip('请选择一条记录再查看');
 		return;
 	}
     url += '&load=detail&id='+rowsData[0].id;
 	createdetailwindow(title,url,"1000",height);
 }
 </script>