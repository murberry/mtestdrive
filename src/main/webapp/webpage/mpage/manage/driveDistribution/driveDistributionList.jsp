<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="driveRecodsList" title="出车明细" queryMode="group" actionUrl="driveDistributionController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol field="status" title="预约试驾"  defaultVal="" hidden="true" query="true"></t:dgCol>
   <t:dgCol title="4S经销商" field="agency.name" query="true" width="120"></t:dgCol>
   <%-- <t:dgCol title="试驾车" field="carId" dictionary="t_car_info,id,name" width="120"></t:dgCol> --%>
   <t:dgCol title="销售代表" field="salesman.name" width="120"></t:dgCol>
   <t:dgCol title="客户" field="customer.name" query="true" width="120"></t:dgCol>
   <%-- <t:dgCol title="驾驶员" field="driver" width="120"></t:dgCol> --%>
   <t:dgCol title="预约开始时间" field="orderStartTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="预约结束时间" field="orderEndTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol>
   <t:dgCol title="车型" field="carType" width="120"></t:dgCol>
   <t:dgCol title="车牌" field="carId" dictionary="t_car_info,id,plate_no"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgFunOpt funname="distribution(id)" operationCode="FACILITYCAR" title="<font color='green'>分配</font>"></t:dgFunOpt>
   <t:dgFunOpt funname="openDetailWindow(id)" title="<font color='green'>详情</font>"></t:dgFunOpt>
   <t:dgDelOpt title="删除" url="driveRecodsController.do?del&id={id}" operationCode="DELRECODS"/>
  </t:datagrid>
  </div>
 </div>
 <script>
 function openDetailWindow(id){
		createdetailwindow("详情","driveRecodsController.do?addorupdate&load=detail&id="+id);
	}
 	function playbackTrack(id){
 		createdetailwindow("试驾回放","driveRecodsController.do?jumpToplaybackTrackPage&id="+id);
 	}
 	 $(function(){
 		$(".icon-search").click();
 		 /* $(".icon-search").each(function(){
 			if($(this).html() == "查询"){
 	 	 		$(this).click();
 	 	 	}
 		}); */
 	 })
 	  function distribution(id){
	 createdetailwindow("分配销售顾问","driveDistributionController.do?distribution&id="+id);
 }
 </script>