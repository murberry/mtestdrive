<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>经销商信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body >
  <t:formvalid formid="demoform" dialog="true" usePlugin="password" layout="table" action="agencyInfoController.do?save" >
			<input id="id" name="id" type="hidden" value="${agencyInfoPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							区域:
						</label>
					</td>
					<td class="value">
					<t:dictSelect field="regionId" id="region" defaultVal="${agencyInfoPage.regionId}"  typeGroupCode="region" 	hasLabel="false" datatype="s2-10"></t:dictSelect>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							集团:
						</label>
					</td>
					<td class="value">
					<t:dictSelect field="dearlerGroup" id="dearlerGroup" defaultVal="${agencyInfoPage.dearlerGroup}"  typeGroupCode="dearlerGroup" 	hasLabel="false" datatype="s2-10"></t:dictSelect>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							4S经销商编码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="code" name="code" 
							   value="${agencyInfoPage.code}" datatype="s2-10" >
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							4S经销商名称:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="name" name="name" datatype="*2-20"
							   value="${agencyInfoPage.name}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							省份:
						</label>
					</td>
					<td class="value">
						
							   <select id="provinceId" name="provinceId" onchange="getCity2(this)" datatype="s2-10">
						  		<option value="">--- 全部 ---</option>
						  		</select>
						<input class="inputxt" id="HprovinceId" name="HprovinceId" type="hidden" value="${agencyInfoPage.provinceId}" >
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							城市:
						</label>
					</td>
					<td class="value">
					<select id="cityId" name="cityId"  datatype="s2-10">
						  		<option value="">--- 全部 ---</option>
						  		</select>
						<input class="inputxt" id="HcityId" name="HcityId" type="hidden"
							   value="${agencyInfoPage.cityId}" >
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							4S地址:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="address" name="address" datatype="s2-30"
							   value="${agencyInfoPage.address}" >
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							4S座机号码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="telephone" name="telephone" 
							   value="${agencyInfoPage.telephone}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							4S店联系人:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="contact" name="contact" 
							   value="${agencyInfoPage.contact}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							4S店联系人手机号:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="contactMobile" name="contactMobile" 
							   value="${agencyInfoPage.contactMobile}">
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
							     value="<fmt:formatDate value='${agencyInfoPage.createTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
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
							     value="<fmt:formatDate value='${agencyInfoPage.updateTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr> --%>
			</table>
		</t:formvalid>
		<script type="text/javascript">
		

		window.onload=function(){

				/* $.post(
					    'agencyInfoController.do?getRegion',
						function(data) {
					    	var id=document.getElementById("HregionId").value;
					    	var d = $.parseJSON(data);
					    	 document.getElementById("regionId").options.length=0; 
							 document.getElementById("regionId").options.add(new Option('-- 全部 --',''));
							 for( var i = 0; i < d.length; i++){
								    var obj=d[i];
								    if(obj.id==id){
								    	document.getElementById("regionId").options.add(new Option(obj.name,obj.id));
								    	document.getElementById("regionId").options[i+1].selected='selected';
								    	}else{
								    	document.getElementById("regionId").options.add(new Option(obj.name,obj.id));
								    	}
								}
							
						});	 */

			  //省
				$.post(
					    'agencyInfoController.do?getProvince',
						function(data) {
					    	var id=document.getElementById("HprovinceId").value;
					    	var d = $.parseJSON(data);
					    	 document.getElementById("provinceId").options.length=0; 
							 document.getElementById("provinceId").options.add(new Option('-- 全部 --',''));
							 for( var i = 0; i < d.length; i++){
								    var obj=d[i];
								    if(obj.id==id){
								    	document.getElementById("provinceId").options.add(new Option(obj.name,obj.id));
								    	document.getElementById("provinceId").options[i+1].selected='selected';
								    	}else{
								    	document.getElementById("provinceId").options.add(new Option(obj.name,obj.id));
								    	}
								}
							
						});	
						
				getCity();
			 
		}
		//市
		function getCity(){
			var selected = document.getElementById("HprovinceId").value;
			if(selected < "0"){
				
				selected=$('#provinceId option:selected').val();
			}
			$.post(
				    'agencyInfoController.do?getCity',
				    {pId:selected},
					function(data) {
				    	var id=document.getElementById("HcityId").value;
				    	var d = $.parseJSON(data);
				    	 document.getElementById("cityId").options.length=0; 
						 document.getElementById("cityId").options.add(new Option('-- 全部 --',''));
						 for( var i = 0; i < d.length; i++){
							    var obj=d[i];
							    if(obj.id==id){
							    	document.getElementById("cityId").options.add(new Option(obj.name,obj.id));
							    	document.getElementById("cityId").options[i+1].selected='selected';
							    	}else{
							    	document.getElementById("cityId").options.add(new Option(obj.name,obj.id));
							    	}
							}
						
					});	
		}
		function getCity2(){
			
			var selected=$('#provinceId option:selected').val();
			
			$.post(
				    'agencyInfoController.do?getCity',
				    {pId:selected},
					function(data) {
				    	var id=document.getElementById("HcityId").value;
				    	var d = $.parseJSON(data);
				    	 document.getElementById("cityId").options.length=0; 
						 document.getElementById("cityId").options.add(new Option('-- 全部 --',''));
						 for( var i = 0; i < d.length; i++){
							    var obj=d[i];
							    if(obj.id==id){
							    	document.getElementById("cityId").options.add(new Option(obj.name,obj.id));
							    	document.getElementById("cityId").options[i+1].selected='selected';
							    	}else{
							    	document.getElementById("cityId").options.add(new Option(obj.name,obj.id));
							    	}
							}
						
					});	
		}
		</script>
		
 </body>