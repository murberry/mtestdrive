<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:datagrid name="departUserList" title="common.operation"
            actionUrl="agencyInfoController.do?userDatagrid&agenctid=${agenctid}" fit="true" fitColumns="true" idField="id" queryMode="group">
	<t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
	<t:dgCol title="common.username" sortable="false" field="userName" query="true"></t:dgCol>
	<%-- <t:dgCol title="common.real.name" field="realName" query="true"></t:dgCol> --%>
	<t:dgCol title="common.status" sortable="true" field="status" replace="common.active_1,common.inactive_0,super.admin_-1"></t:dgCol>
	<t:dgCol title="common.operation" field="opt"></t:dgCol>
	<t:dgDelOpt title="common.delete" url="userController.do?del&id={id}&userName={userName}" />
	<t:dgToolBar title="录入管理员" langArg="common.user" icon="icon-add" url="agencyInfoController.do?addorup&agenctid=${agenctid}" funname="add"></t:dgToolBar>
	<t:dgToolBar title="编辑" langArg="common.user" icon="icon-edit" url="agencyInfoController.do?addorup&agenctid=${agenctid}" funname="update"></t:dgToolBar>
	<%-- <t:dgToolBar title="common.edit.param" langArg="common.user" icon="icon-edit" url="userController.do?addorup&departid=${agenctid}" funname="update"></t:dgToolBar> --%>
	<%-- <t:dgToolBar title="common.add.exist.user" icon="icon-add" url="agencyInfoController.do?goAddUserToOrg&orgId=${agenctid}" funname="add" width="500"></t:dgToolBar> --%>
</t:datagrid>
