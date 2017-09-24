<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>轨迹回放</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<style>
.carInfoTab tr td:first-child {
	text-align: right;
}
</style>
</head>
<body style="overflow: hidden">
	<table>
		<tr>
			<td style="text-align: left; vertical-align: top;">
				<table cellspacing="1" class="formtable	carInfoTab" style="width: 100%">
					<tr>
						<td><label>车型：</label></td>
						<td><span>${info.type }</span></td>
					</tr>
					<tr>
						<td><label>车牌号：</label></td>
						<td>${info.plateNo }</td>
					</tr>
					<tr>
						<td><label>开始时间：</label></td>
						<td><fmt:formatDate value="${info.driveStartTime }"
								pattern="yyyy-MM-dd HH:mm" /></td>
					</tr>
					<tr>
						<td><label>结束时间：</label></td>
						<td><fmt:formatDate value="${info.driveEndTime }"
								pattern="yyyy-MM-dd HH:mm" /></td>
					</tr>
					<tr>
						<td><label>行驶里程数：</label></td>
						<td>${info.mileage }</td>
					</tr>
					<tr>
						<td><a href="javascript:repeatPlay();"
							style="text-decoration: underline; color: blue;">重新播放</a></td>
					</tr>
				</table>
			</td>
			<td>
				<div id="currMap" style="width: 660px; height: 480px;">
					<div id="errorMsg" style="display: none">暂无数据</div>
				</div>
			</td>
		</tr>
	</table>
	
	 <script src="http://api.map.baidu.com/api?v=2.0&ak=D2b4558ebed15e52558c6a766c35ee73"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js"></script>
	<script type="text/javascript">
	    //缓存定位点数据
		var gathers = '${info.obdGathers}';
		var plateNo = '${info.plateNo }';
	</script>
	<script type="text/javascript"
		src="mjs/playbackTrack.js"></script>
</body>