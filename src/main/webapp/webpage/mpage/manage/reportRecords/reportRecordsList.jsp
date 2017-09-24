<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
   
 
  <t:datagrid name="reportRecordsList" title="报备信息" actionUrl="reportRecordsController.do?datagrid" idField="id" fit="true"  queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="区域"  field="regionId" hidden="true" dictionary="region" width="60" query="true"></t:dgCol>
   <t:dgCol title="经销商" field="dealerName"  width="120" hidden="true" query="true"></t:dgCol>
   <t:dgCol title="车型" field="carType" hidden="true" dictionary="carType" width="120" query="true"></t:dgCol>
   <t:dgCol title="车牌号" field="plateNo" hidden="true" query="true"></t:dgCol>
   <t:dgCol title="车牌号" field="carId" dictionary="t_car_info,id,plate_no" width="120" ></t:dgCol>
   <t:dgCol title="报备原因类型" field="type" dictionary="reportType" width="120"></t:dgCol>
   <t:dgCol title="报备日期" field="createTime" formatter="yyyy年MM月dd日" width="120"></t:dgCol>
   <t:dgCol title="开始时间" field="startTime" formatter="yyyy/MM/dd hh:mm:ss" width="120"></t:dgCol>
   <t:dgCol title="结束时间" field="endTime" formatter="yyyy/MM/dd hh:mm:ss" width="120"></t:dgCol>
   <t:dgCol title="具体原因" field="remark" width="120"></t:dgCol>
   <t:dgCol title="状态" field="status" width="120" dictionary="REPORTSTA" ></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgFunOpt  funname="cancleReport(id)" title="<font color='green'>关闭</font>"></t:dgFunOpt>
   <%-- <t:dgDelOpt title="删除" url="reportRecordsController.do?del&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="reportRecordsController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="reportRecordsController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="reportRecordsController.do?addorupdate" funname="detail"></t:dgToolBar> --%>
  </t:datagrid>
  </div>
 </div>
 
 <script>
 	function cancleReport(id){
 		gridname=name;
 		createdialog('关闭确认 ', '确定关闭该报备吗 ?',"reportRecordsController.do?cancleReport&reportId="+id ,name);
 	}
 	function createdialog(title, content, url,name) {
 		$.dialog.setting.zIndex = getzIndex(true);
 		$.dialog.confirm(content, function(){
 			doSubmit(url,name);
 			rowid = '';
 			$('#reportRecordsList').datagrid('reload');
 		}, function(){
 		});
 	}
 </script>