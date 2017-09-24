<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>车辆信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <style>
  	.carInfoTab tr td:first-child{
  		text-align:right;
  	}
  </style>
 </head>
 <body style="overflow:hidden ">
 	<table>
 		<tr>
 			<td style="text-align: left;vertical-align: top;">
 				<table cellspacing="1" class="formtable	carInfoTab">
			 		<tr>
			 			<td><label>车型：</label></td>
			 			<td><span>${mvInfo.type }</span></td>
			 		</tr>
			 		<tr>
			 			<td><label>VIN：</label></td>
			 			<td><span>${mvInfo.vin }</span></td>
			 		</tr>
			 		<tr>
			 			<td><label>车牌号：</label></td>
			 			<td>${mvInfo.plateNo }</td>
			 		</tr>
			 		<tr>
			 			<td><label>当前状态：</label></td>
			 			<td>
			 				<c:if test="${mvInfo.status == 0 }">报备</c:if> 
			 				<c:if test="${mvInfo.status == 1 }">正常</c:if> 
			 			</td>
			 		</tr>
			 		<tr>
			 			<td><label>当前时速(km/h)：</label></td>
			 			<td>${mvInfo.spd }</td>
			 		</tr>
			 		<tr>
			 			<td><label>海拔(M)：</label></td>
			 			<td>${mvInfo.alt }</td>
			 		</tr>
			 		<tr>
			 			<td><label>累计行驶里程(km)：</label></td>
			 			<td>${mvInfo.mileage/1000 }</td>
			 		</tr>
			 	</table>
 			</td>
 			<td>
 				<div id="currMap" style="width: 450px;height: 380px;"></div>
 			</td>
 		</tr>
 	</table>
 	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=4lb48bMFrQh09NzCuuul4W2e7YpAGrNG"></script>
 	<script type="text/javascript">
 	$(function(){
 		if("${mvInfo.longitude}" == "" || "${mvInfo.latitude}" == ""){
 			$("#currMap").html("暂无位置信息");
 		}else{
 			var map = new BMap.Map("currMap");
 			var ggPoint = new BMap.Point("${mvInfo.longitude}","${mvInfo.latitude}");
 			map.enableScrollWheelZoom(true);
 		    //坐标转换完之后的回调函数
 		    translateCallback = function (data){
 		      if(data.status === 0) {
 		    	    var new_point = new BMap.Point(data.points[0].lng,data.points[0].lat);  
 		    	    map.centerAndZoom(new_point,11);
 		 			map.enableScrollWheelZoom(true);
 		 		    var marker = new BMap.Marker(new_point);  // 创建标注
 		 		    map.addOverlay(marker);              // 将标注添加到地图中
 		 		    map.panTo(new_point);    
 		 		    map.addControl(new BMap.ScaleControl()); // 添加比例尺控件
 		      }
 		    }
 			var convertor = new BMap.Convertor();
	        var pointArr = [];
	        pointArr.push(ggPoint);
	        convertor.translate(pointArr, 1, 5, translateCallback)
 		}
 	})
 	</script>
 </body>