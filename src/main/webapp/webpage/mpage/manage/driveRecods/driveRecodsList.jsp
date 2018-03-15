<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="driveRecodsList" title="出车明细" queryMode="group" actionUrl="driveRecodsController.do?obdDatagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="4S经销商" field="agencyId" dictionary="t_agency_info,id,name" width="120"></t:dgCol>
   <t:dgCol title="销售代表" field="salesmanId" dictionary="t_salesman_info,id,name" align="center" width="120"></t:dgCol>
   <t:dgCol title="客户" field="customerId" dictionary="t_customer_info,id,name" align="center" width="120"></t:dgCol>
   <t:dgCol title="车型" field="carId" dictionary="t_car_info,id,type"  width="80"></t:dgCol>
   <t:dgCol title="状态" field="status" replace="无效试驾_2,有效试驾_1" width="80"></t:dgCol>
    <t:dgCol title="原因" field="description"  width="120"></t:dgCol>
   <t:dgCol title="试驾开始时间" field="driveStartTime" formatter="yyyy-MM-dd hh:mm:ss"  width="140"></t:dgCol>
   <t:dgCol title="试驾结束时间" field="driveEndTime" formatter="yyyy-MM-dd hh:mm:ss"  width="140"></t:dgCol>
   <t:dgCol title="行驶里程数" field="mileage"   width="80"></t:dgCol>
   <t:dgCol title="试驾公里数达成率" field="achievement"  width="80"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="200"></t:dgCol>
   <t:dgFunOpt funname="playbackTrack(id)" title="<font color='green'>回放</font>" ></t:dgFunOpt>
   <t:dgFunOpt funname="openDetailWindow(id)" title="<font color='green'>详情</font>"></t:dgFunOpt>
   <t:dgFunOpt funname="openReport(id)" title="<font color='green'>试驾报告</font>"></t:dgFunOpt>
   <t:dgDelOpt title="删除" url="driveRecodsController.do?del&id={id}" operationCode="DELRECODS"/>
  </t:datagrid>
  <div style="padding:5px;height:90px">
    
    <div name="searchColums" style="float:left;padding-left:10px;">
              <label class="Validform_label" >经销商: </label>
              <input type="text" name="agencyName" >
              
              <label class="Validform_label" >销售顾问: </label>
              <input type="text" name="salesmanName">
              
              <label class="Validform_label" >客户姓名: </label>
              <input type="text" name="customerName">
              
              <label class="Validform_label" >状态: </label>
              <select name="status" id="status"> 
                <option value="">请选择</option>   
		        <option value="1">有效试驾</option>  
		        <option value="2">无效试驾</option> 
		      </select>   
              
              <label class="Validform_label" >车型: </label>
              <t:dictSelect hasLabel="false" field="type" typeGroupCode="carType"  datatype="s2-15"></t:dictSelect>
						<span class="Validform_checktip"></span>  
              
             <br/>
             <br/>
              <label class="Validform_label" >试驾开始时间: </label>
              <input type="text" name="startTime" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});">~
              <input type="text" name="endTime" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});">
         <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="driveRecodsListsearch();" style="text-align:right;margin-left:574px;">查询</a>
    </div>
   </div>
 		<div id="departListtb" style="padding: 3px; height: 25px">
            <div style="float: left;">
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-putout" onclick="ExportXls()"><t:mutiLang langKey="excelOutput" langArg="common.department"/></a>
            </div>
        </div>
  </div>
 </div>
 <script>
 function openDetailWindow(id){
		createdetailwindow("详情","driveRecodsController.do?addorupdateByObd&load=detail&id="+id);
	}
 	function playbackTrack(id){
 		createdetailwindow("试驾回放","driveRecodsController.do?jumpToplaybackTrackPageByObd&id="+id, 900, 500);
 	}
 	function openReport(id){
 		createdetailwindow("试驾报告","obdDriveRecodsController.do?openReport&id="+id, 900, 500);
 	}
 	
 	 $(function(){
 		$(".icon-search").click();
 		 /* $(".icon-search").each(function(){
 			if($(this).html() == "查询"){
 	 	 		$(this).click();
 	 	 	}
 		}); */
 	 })
 	 
 	 /* function ExportXls(){
 		alert(1);
 	 } */
 	 
 	 function ExportXls(){
 		 JeecgExcelExport("driveRecodsController.do?exportXls","driveRecodsList");
 	 }
 </script>