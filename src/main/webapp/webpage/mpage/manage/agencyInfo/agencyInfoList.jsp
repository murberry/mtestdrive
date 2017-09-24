<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div id="main_depart_list" class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="agencyInfoList" title="经销商信息" queryMode="group" actionUrl="agencyInfoController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="经销商名称" field="name" query="true" width="130"></t:dgCol>
   <t:dgCol title="经销商编码" field="code" autocomplete="-"  width="80"></t:dgCol>
   <t:dgCol title="区域"  field="regionId" dictionary="region" width="60" query="true"></t:dgCol>
   <t:dgCol title="集团"  field="dearlerGroup" dictionary="dearlerGroup" width="60" query="true"></t:dgCol>

   <t:dgCol title="省份" field="provinceId" dictionary="china,id,name" query="true" width="60"></t:dgCol>
   <t:dgCol title="城市" field="cityId" dictionary="china,id,name" query="true" width="70"></t:dgCol>
   <t:dgCol title="地址" field="address" autocomplete="-"  width="180"></t:dgCol>
   <t:dgCol title="座机号码" field="telephone" autocomplete="-"  width="80"></t:dgCol>
   <t:dgCol title="店联系人" field="contact" autocomplete="-"  width="80"></t:dgCol>
   <t:dgCol title="店联系人手机号" field="contactMobile" autocomplete="-"  width="80"></t:dgCol>
   <%-- <t:dgCol title="创建时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120"></t:dgCol> --%>
   <%-- <t:dgCol title="修改时间" field="updateTime" formatter="yyyy-MM-dd hh:mm:ss"  width="120" ></t:dgCol> --%>
   <t:dgCol title="操作" field="opt" width="120"></t:dgCol>
    <t:dgFunOpt funname="queryUsersByDepart(id)" title="<font color='green'>查看管理员</font>" ></t:dgFunOpt>
   <t:dgDelOpt title="删除" url="agencyInfoController.do?del&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="agencyInfoController.do?addorupdate" funname="add" operationCode="ADDAGENCY"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="agencyInfoController.do?addorupdate" funname="update" operationCode="EDITAGENCY"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="agencyInfoController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 
 <div data-options="region:'east',
	title:'管理员列表',
	collapsed:true,
	split:true,
	border:false,
	onExpand : function(){
		li_east = 1;
	},
	onCollapse : function() {
	    li_east = 0;
	}"
	style="width: 400px; overflow: hidden;" id="eastPanel">
    <div class="easyui-panel" style="padding:0px;border:0px" fit="true" border="false" id="userListpanel"></div>
</div>
 
 <script type="text/javascript">
 	var p = document.getElementsByName("provinceId");
	var c = document.getElementsByName("cityId");
	 $(function() {
	        var li_east = 0;
	    });
	$(document).ready(function(){
		 
		p[0].setAttribute("onchange","getCity(this)");
		$.post(
			    'agencyInfoController.do?getProvince',
				function(data) {
			    	var d = $.parseJSON(data);
			    	p[0].options.length=0; 
			    	p[0].options.add(new Option('-- 全部 --',''));
			    	c[0].options.length=0; 
			    	c[0].options.add(new Option('-- 全部 --',''));
					 for( var i = 0; i < d.length; i++){
						    var obj=d[i];
						    		p[0].options.add(new Option(obj.name,obj.id));
						}
					
				});	
		
	});
	
	function getCity(){
		var index = p[0].selectedIndex;
		var value = p[0].options[index].value;
		$.post(
			    'agencyInfoController.do?getCity',
			    {pId:value},
				function(data) {
			    	c[0].options.length=0; 
			    	c[0].options.add(new Option('-- 全部 --',''));
			    	var d = $.parseJSON(data);
					 for( var i = 0; i < d.length; i++){
						 var obj=d[i];
						 c[0].options.add(new Option(obj.name,obj.id));
						}
					
				});	
	}
	
       
    
	
	function queryUsersByDepart(agenctid){
        var title = '管理员列表';
        if(li_east == 0 || $('#main_depart_list').layout('panel','east').panel('options').title != title){
            $('#main_depart_list').layout('expand','east');
        }
        $('#main_depart_list').layout('panel','east').panel('setTitle', title);
        $('#main_depart_list').layout('panel','east').panel('resize', {width: 500});
        $('#userListpanel').panel("refresh", "agencyInfoController.do?userList&agenctid=" + agenctid);
    }
</script>
 
 