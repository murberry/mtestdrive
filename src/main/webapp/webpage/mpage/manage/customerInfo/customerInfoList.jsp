<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<style>


/* .quyuSel{
	width:100px
}
.datagrid-view{
	height:482px !important;
}
.datagrid-body{
height:475px !important;
} */
</style>
<link rel="stylesheet" type="text/css" href="mcss/customerInfoList.css" />
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:0px;border:0px">
<t:datagrid name="customerInfoList" title="客户信息"   
	actionUrl="customerInfoController.do?datagrid" idField="id" fit="true" queryMode="group">
	<t:dgCol title="编号" field="id" hidden="true" ></t:dgCol>
	<t:dgCol title="区域"  field="region" hidden="true" dictionary="region" width="60" query="true"></t:dgCol>
	<t:dgCol title="经销商" field="agencyName"  width="120" hidden="true" query="true"></t:dgCol>
	<t:dgCol title="客户来源 " field="source" width="120" replace="saleforce_1,展厅_2,未知来源_0"></t:dgCol>
	<t:dgCol title="用户姓名" field="name"  query="true"></t:dgCol>
	<t:dgCol title="性别" field="gender"  replace="男_1,女_2,未知_0"></t:dgCol>
	<t:dgCol title="出生日期" field="birthday" formatter="yyyy-MM-dd" width="120" autocomplete="-"></t:dgCol>
	<%-- <t:dgCol title="身份证号" field="idCard" width="120"></t:dgCol> --%>
	<t:dgCol title="手机号码" field="mobile" width="120" hidden="true" query="true"></t:dgCol>
	<%-- <t:dgCol title="驾照号码" field="drivingLicense" width="120"></t:dgCol> --%>
	<t:dgCol title="客户类型" field="type" autocomplete="-" width="120"></t:dgCol>
	<%--  <t:dgCol title="驾照照片存放地址" field="drivingLicensePicPath"   width="120"></t:dgCol> --%>
	<%-- <t:dgCol title="所有人" field="name" ></t:dgCol> --%>
	<t:dgCol title="创建时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss" width="120"></t:dgCol>
	<%-- <t:dgCol title="修改时间" field="updateTime" formatter="yyyy-MM-dd hh:mm:ss" width="120"></t:dgCol> --%>
	<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	<t:dgDelOpt title="删除" url="customerInfoController.do?del&id={id}" operationCode="DELCUSTO"/>
	<%-- <t:dgToolBar title="录入"  icon="icon-add" url="customerInfoController.do?addorupdate" funname="add" operationCode="ADDCUSTOMER"></t:dgToolBar> --%>
	<t:dgToolBar title="编辑" operationCode="EDITCUSTO" icon="icon-edit" url="customerInfoController.do?addorupdate"  funname="update"></t:dgToolBar>
	<t:dgToolBar title="查看" icon="icon-search" url="customerInfoController.do?addorupdate" funname="detail"></t:dgToolBar>
</t:datagrid>
</div>
</div>
<script type="text/javascript">
    $(document).ready(function(){
        /* $("input").css("height", "24px"); */
        $("#region").change(function(){
        	customerInfoListsearch();
        });
    });
    /* function customerInfoListsearch(){
    	alert(1);
    	var region = $("#region").val();
    	var dealerName = $("#dealerName").val();
    	var salesConsultantName = $("#salesConsultantName").val();
    	var customerName = $("#customerName").val();
    	//var customerMobile = $("#customerMobile").val();
    	$("#customerInfoList").datagrid('load',{
			region : region,
			dealerName : dealerName,
			salesConsultantName : salesConsultantName,
			customerName : customerName
			//customerMobile : customerMobile
    	});
    } */
    
    function clearSearch(){
    	$("#region").val(0);
    	$(".queryCondition").val("");
    }
</script>