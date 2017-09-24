<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div id="main_depart_list" class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="carInfoList" title="车辆信息" queryMode="group" actionUrl="carInfoController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="区域" field="agency.regionId" dictionary="region" query="true" width="120" hidden="true"></t:dgCol>
   <t:dgCol title="编号" field="code" autocomplete="-" width="120"></t:dgCol>
   <t:dgCol title="经销商" field="agency.name" query="true" width="120"></t:dgCol>
   <t:dgCol title="VIN码 车架号" field="vin" width="120"></t:dgCol>
   <t:dgCol title="试驾车名称" field="name" autocomplete="-" width="120"></t:dgCol>
   <t:dgCol title="车型" field="type" query="true" width="120" dictionary="carType"></t:dgCol>
   <t:dgCol title="车牌" field="plateNo" query="true" width="120"></t:dgCol>
  <%--  <t:dgCol title="形象照存放地址" field="picPath"   width="120"></t:dgCol> --%>
   <t:dgCol title="使用日期" field="saleYear" formatter="yyyy年MM月dd日"  width="120"></t:dgCol>
   <t:dgCol title="试驾次数" field="driveTotal" width="120"></t:dgCol>
   <t:dgCol title="状态" field="status" width="120" dictionary="car_status" query="true"></t:dgCol>
   <%-- <t:dgCol title="创建时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss" width="120"></t:dgCol>
   <t:dgCol title="修改时间" field="updateTime" formatter="yyyy-MM-dd hh:mm:ss" width="120"></t:dgCol> --%>
   <t:dgCol title="操作" field="opt" width="200"></t:dgCol>
   <t:dgFunOpt operationCode="REPORTCAR" funname="queryReportRecords(id)" title="<font color='green'>报备</font>"></t:dgFunOpt>
   <t:dgFunOpt funname="openMonitoringPage(id)" title="<font color='green'>监控</font>"></t:dgFunOpt>
   <t:dgFunOpt funname="obd(id)" operationCode="FACILITYCAR" title="<font color='green'>设备</font>"></t:dgFunOpt>
   <t:dgDelOpt title="删除" url="carInfoController.do?del&id={id}" operationCode="DELCAR"/>
   <t:dgToolBar title="录入" icon="icon-add" url="carInfoController.do?addorupdate" funname="add" operationCode="ADDCAR"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="carInfoController.do?addorupdate" funname="update" operationCode="EDITCAR"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="carInfoController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 
<!--  <div data-options="region:'east',
	title:'报备',
	collapsed:true,
	split:true,
	border:false,
	onExpand : function(){
		li_east = 1;
	},
	onCollapse : function() {
	    li_east = 0;
	}"
	style="width: 500px; overflow: hidden;" id="eastPanel">
    <div class="easyui-panel" style="padding:0px;border:0px" fit="true" border="false" id="userListpanel"></div>
</div> -->
 
 
 <script type="text/javascript">
 function queryReportRecords(carId){
       /*  var title = '报备';
        if(li_east == 0 || $('#main_depart_list').layout('panel','east').panel('options').title != title){
            $('#main_depart_list').layout('expand','east');
        }
        $('#main_depart_list').layout('panel','east').panel('setTitle', title);
        $('#main_depart_list').layout('panel','east').panel('resize', {width: 500});
        $('#userListpanel').panel("refresh", "carInfoController.do?reportRecordsList&carId=" + carId); */
        createwindow("报备","carInfoController.do?reportRecordsList&carId=" + carId);
  }
 
 //打开车辆监控页面
 function openMonitoringPage(id) {debugger;
	 
		createdetailwindow("车辆监控","carInfoController.do?jumpToMonitoringPage&id="+id);
	}
 function obd(id){
	 createdetailwindow("车辆设备","carInfoController.do?obd&id="+id);
 }
</script>
 