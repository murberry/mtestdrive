<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>车辆信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body >
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="carInfoController.do?save">
			<input id="id" name="id" type="hidden" value="${carInfoPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							编号:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="code" name="code" datatype="s2-10"
							   value="${carInfoPage.code}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<%-- <tr>
					<td align="right">
						<label class="Validform_label">
							经销商:
						</label>
					</td>
					<td class="value">
						<select id="agencyId" name="agencyId" >
						  		<option value="">--- 全部 ---</option>
						  </select>
						<input  id="HagencyId" name="HagencyId" type="hidden"
							   value="${carInfoPage.agency.id}">
						<span class="Validform_checktip"></span>
					</td>
				</tr> --%>
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							VIN码 车架号:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="vin" name="vin" datatype="s2-20"
							   value="${carInfoPage.vin}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾车名称:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="name" name="name" datatype="s2-10"
							   value="${carInfoPage.name}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							车型:
						</label>
					</td>
					<td class="value">
						
						<t:dictSelect hasLabel="false" field="type" typeGroupCode="carType" defaultVal="${carInfoPage.type}" datatype="s2-15"></t:dictSelect>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							车牌:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="plateNo" name="plateNo" datatype="s2-10"
							   value="${carInfoPage.plateNo}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<%-- <tr>
					<td align="right">
						<label class="Validform_label">
							形象照存放地址:
						</label>
					</td>
					<td class="value">
					
						<div class="form" id="filediv"></div>
	<div class="form"><t:upload name="file_upload" uploader="carInfoController.do?saveOrUpdateIcon" extend="*.png;" id="file_upload" formId="formobj"></t:upload></div>
					</td>
				</tr> --%>
				<tr>
					<td align="right">
						<label class="Validform_label">
							使用日期:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text" style="width: 150px" id="saleYear" name="saleYear" 
							     value="<fmt:formatDate value='${carInfoPage.saleYear}' type="date" pattern="yyyy-MM-dd"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<%-- <tr>
					<td align="right">
						<label class="Validform_label">
							试驾次数:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="driveTotal" name="driveTotal" ignore="ignore"
							   value="${carInfoPage.driveTotal}" datatype="n">
						<span class="Validform_checktip"></span>
					</td>
				</tr> --%>
				<tr>
					<td align="right">
						<label class="Validform_label">
							状态:
						</label>
					</td>
					<td class="value">
					<t:dictSelect hasLabel="false" field="status" typeGroupCode="car_status" defaultVal="${carInfoPage.status}" datatype="s1-10"></t:dictSelect>
						
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<%-- <tr>
					<td align="right">
						<label class="Validform_label">
							创建时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" style="width: 150px" id="createTime" name="createTime" ignore="ignore"
							     value="<fmt:formatDate value='${carInfoPage.createTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							修改时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" style="width: 150px" id="updateTime" name="updateTime" ignore="ignore"
							     value="<fmt:formatDate value='${carInfoPage.updateTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr> --%>
			</table>
		</t:formvalid>
		
		<script type="text/javascript">
		/* window.onload=function(){
			$.post(
					 'agencyInfoController.do?getAgency',	
					 function(data) {
						 var id=document.getElementById("HagencyId").value;
						 var d = $.parseJSON(data);
						 document.getElementById("agencyId").options.length=0; 
						 document.getElementById("agencyId").options.add(new Option('-- 全部 --',''));
						 for( var i = 0; i < d.length; i++){
							 var obj=d[i];
							 if(obj.id==id){
							    	document.getElementById("agencyId").options.add(new Option(obj.name,obj.id));
							    	document.getElementById("agencyId").options[i+1].selected='selected';
							    	}else{
							    	document.getElementById("agencyId").options.add(new Option(obj.name,obj.id));
							    	}
							 
						 }
					 }
			);
		} */
		</script>
		
 </body>