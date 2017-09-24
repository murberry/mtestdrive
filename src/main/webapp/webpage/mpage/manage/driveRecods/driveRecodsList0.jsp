<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<style>
div.edui-box {
    position: relative;
    display: -moz-inline-box !important;
    display: inline-block !important;
    vertical-align: top;
}
</style>
<script type="text/javascript">
/* $(function(){
	driveRecodsListsearch();
}) */
</script>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid autoLoadData="false" name="driveRecodsList" title="预约管理" actionUrl="driveRecodsController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="id" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="销售顾问" field="salesman.name" width="120"></t:dgCol>
   <t:dgCol title="客户姓名" field="customer.name" width="120"></t:dgCol>
  <%--  <t:dgCol title="客户电话" field="customer.mobile" width="120"></t:dgCol> --%>
   <t:dgCol title="预约日期" field="orderDate" formatter="yyyy年MM月dd日"  width="120"></t:dgCol>
   <t:dgCol title="开始时间" field="orderStartTime" formatter="hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="结束时间" field="orderEndTime" formatter="hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgFunOpt funname="detailById(id)" title="详情"></t:dgFunOpt>
   <%-- <t:dgDelOpt title="删除" url="driveRecodsController.do?del&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="driveRecodsController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="driveRecodsController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="driveRecodsController.do?addorupdate" funname="detail"></t:dgToolBar> --%>
  </t:datagrid>
   <div style="padding:5px;height:90px">
    
    <div name="searchColums" style="float:left;padding-left:10px;">
              <input type="hidden" name="status" value="0">
              <t:dictSelect field="regionId" typeGroupCode="region" extendJson="{style:'height:30px'}"	title="区域" divClass="edui-box"></t:dictSelect>
              
              <label class="Validform_label" >经销商: </label>
              <input type="text" name="agencyCode" >
              
              <label class="Validform_label" >销售顾问: </label>
              <input type="text" name="salesman.name">
              
              <label class="Validform_label" >客户姓名: </label>
              <input type="text" name="customer.name">
              
             <!--  <label class="Validform_label" >客户电话: </label>
              <input type="text" name="customerMobile"> -->
             <br/>
             <br/>
              <label class="Validform_label" >预约时间: </label>
              <input type="text" name="startTime" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});">~
              <input type="text" name="endTime" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});">
         <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="driveRecodsListsearch();" style="text-align:right;margin-left:574px;">查询</a>
         <!-- <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-putout" onclick="exportExcel();">导出excel</a> -->
    </div>
   </div>
  </div>
 </div>
 
 
 <script type="text/javascript">
 function detailById(id){
	 var url = 'driveRecodsController.do?addorupdate&load=detail&id='+id;
	 createdetailwindow('详情',url,null,null);
 }
 $(function(){
 	$(".icon-search").click();
	 
 })
</script>