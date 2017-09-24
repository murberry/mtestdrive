<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>试驾明细</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body >
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="driveRecodsController.do?save">
			
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							4S经销商:
						</label>
					</td>
					<td class="value">
					<input class="inputxt" id="Hagency.id" name="Hagency.id" ignore="ignore" 
							   value="${driveRecodsPage.agencyName}">
					<%-- <select id="agency.id" name="agency.id" >
						  		<option value="">--- 全部 ---</option>
						  </select>
						<input class="inputxt" id="Hagency.id" name="Hagency.id" ignore="ignore" type="hidden"
							   value="${driveRecodsPage.agency.id}">  --%>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾车:
						</label>
					</td>
					<td class="value">
					<select id="carId" name="carId" >
						  		<option value="">--- ${driveRecodsPage.carType} ---</option>
						  </select>
						<%-- <input class="inputxt" id="HcarId" name="HcarId" ignore="ignore" type="hidden"
							   value="${driveRecodsPage.carType}"> --%>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							销售代表:
						</label>
					</td>
					<td class="value">
					<input class="inputxt" id="salesman.name" name="salesman.name" ignore="ignore" 
							   value="${driveRecodsPage.salesmanName}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							驾驶员:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="customer.name"" name="customer.name"" ignore="ignore" 
							   value="${driveRecodsPage.customerName}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							行驶里程数:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="mileage" name="mileage" ignore="ignore"
							   value="${driveRecodsPage.mileage}" datatype="d">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾开始时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" style="width: 150px" id="driveStartTime" name="driveStartTime" ignore="ignore"
							     value="<fmt:formatDate value='${driveRecodsPage.driveStartTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾结束时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" style="width: 150px" id="driveEndTime" name="driveEndTime" ignore="ignore"
							     value="<fmt:formatDate value='${driveRecodsPage.driveEndTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾合同:
						</label>
					</td>
					<td class="value">
						<img id="contractPicPath" alt="" src=""  
						onerror="this.src='images/onError.jpg'"   width="300px" height="200px">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							试驾后合照:
						</label>
					</td>
					<td class="value">
						<img id ="endPicPath" alt="" src=""  
						onerror="this.src='images/onError.jpg'" width="300px" height="200px">
						<%-- <input class="inputxt" id="endPicPath" name="endPicPath" ignore="ignore"
							   value="${driveRecodsPage.endPicPath}"> --%>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				
			</table>
		</t:formvalid>
		<script type="text/javascript">
		window.onload=function(){
				/* $.post(
					    'driveRecodsController.do?getAgency',
						function(data) {
					    	var id=document.getElementById("Hagency.id").value;
					    	var d = $.parseJSON(data);
					    	 document.getElementById("agency.id").options.length=0; 
							 document.getElementById("agency.id").options.add(new Option('-- 全部 --',''));
							 for( var i = 0; i < d.length; i++){
								    var obj=d[i];
								    if(obj.id==id){
								    	document.getElementById("agency.id").options.add(new Option(obj.name,obj.id));
								    	document.getElementById("agency.id").options[i+1].selected='selected';
								    	}else{
								    	document.getElementById("agency.id").options.add(new Option(obj.name,obj.id));
								    	}
								}
							
						});	 */
				/* $.post(
					    'driveRecodsController.do?getCarInfo',
						function(data) {
					    	var id=document.getElementById("HcarId").value;
					    	var d = $.parseJSON(data);
					    	 document.getElementById("carId").options.length=0; 
							 document.getElementById("carId").options.add(new Option('-- 全部 --',''));
							 for( var i = 0; i < d.length; i++){
								    var obj=d[i];
								    if(obj.id==id){
								    	document.getElementById("carId").options.add(new Option(obj.name,obj.id));
								    	document.getElementById("carId").options[i+1].selected='selected';
								    	}else{
								    	document.getElementById("carId").options.add(new Option(obj.name,obj.id));
								    	}
								}
							
						}); */	
				/* $.post(
					    'driveRecodsController.do?getSalesmanInfo',
						function(data) {
					    	var id=document.getElementById("Hsalesman.id").value;
					    	var d = $.parseJSON(data);
					    	 document.getElementById("salesman.id").options.length=0; 
							 document.getElementById("salesman.id").options.add(new Option('-- 全部 --',''));
							 for( var i = 0; i < d.length; i++){
								    var obj=d[i];
								    if(obj.id==id){
								    	document.getElementById("salesman.id").options.add(new Option(obj.name,obj.id));
								    	document.getElementById("salesman.id").options[i+1].selected='selected';
								    	}else{
								    	document.getElementById("salesman.id").options.add(new Option(obj.name,obj.id));
								    	}
								}
							
						});	 */
				/* $.post(
					    'driveRecodsController.do?getCustomerInfo',
						function(data) {
					    	var id=document.getElementById("Hcustomer.id").value;
					    	var d = $.parseJSON(data);
					    	 document.getElementById("customer.id").options.length=0; 
							 document.getElementById("customer.id").options.add(new Option('-- 全部 --',''));
							 for( var i = 0; i < d.length; i++){
								    var obj=d[i];
								    if(obj.id==id){
								    	document.getElementById("customer.id").options.add(new Option(obj.name,obj.id));
								    	document.getElementById("customer.id").options[i+1].selected='selected';
								    	}else{
								    	document.getElementById("customer.id").options.add(new Option(obj.name,obj.id));
								    	}
								}
							
						}); */
			infofun();
		}
		
		function infofun(){
			 document.getElementById('contractPicPath').src="fileUploadController.do?view&fileName="+'${driveRecodsPage.contractPicPath}';
			 document.getElementById('endPicPath').src="fileUploadController.do?view&fileName="+'${driveRecodsPage.endPicPath}';
		}
		
		</script>
		
 </body>