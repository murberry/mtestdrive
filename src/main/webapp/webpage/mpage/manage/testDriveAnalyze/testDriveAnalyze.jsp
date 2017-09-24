<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>试驾指标</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<style>
div.edui-box {
    position: relative;
    display: -moz-inline-box !important;
    display: inline-block !important;
    vertical-align: top;
}  
.datagrid .datagrid-pager {
  margin: 0;
  border-width: 0 0 0 0;
  border-style: solid;
  margin-top:-200px;  
  z-index: 111111111;
  position: relative; 
}
</style>
</head>
<!-- <body style="overflow-y: hidden; overflow-x: hidden;"> -->
<body style="overflow: scroll;">
	<div name="searchColums" style="text-align: center; height: 30px; line-height: 40px;margin-bottom: 20px;">
		<label> 类型:</label> 
		<select id="type">
			<option value="testDriveTimeLong">单次试驾时长</option>
			<option value="testDriveMileage">单次试驾里程</option>
			<option value="effective">无效试驾率</option>
			<option value="fallow">非活跃车辆</option>
			<option value="illegal">非试乘试驾行驶率</option>
			<option value="turnoverRatio">试驾成交率</option>
			<option value="satisfied">试驾满意度</option>
		</select> 
		
		<label id="dimensionOrCarType"> 维度:</label> 
		<select id="dimension" class="" onchange="analyze()">
			<option value="motorcycle">车型</option>
			<option value="consultant">销售顾问</option>
		</select> 
        <t:dictSelect field="type" typeGroupCode="carType" hasLabel="false" title="车型" divClass="edui-box" id="carType"></t:dictSelect>
        
        <input type="hidden" id="yealMonth" name="month" value="">
		
		<label> 年份:</label> 
		<select id="yealSel" onchange="analyze()"></select> 
		
		<label> 月份:</label> 
		<select id="monthSel" onchange="analyze()"></select>
	</div>
	<div>
<div id="main_depart_list" class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
	   <t:datagrid title="非活跃车辆" name="carInfoList" queryMode="group" actionUrl="carInfoController.do?datagrid" idField="id" fit="true">
		   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
		   <t:dgCol title="区域" field="regionId" dictionary="sales_area" width="120" hidden="true"></t:dgCol>
		   <t:dgCol title="经销商" field="agency.name" width="120"></t:dgCol>
		   <t:dgCol title="VIN码 车架号" field="vin" width="120"></t:dgCol>
		   <t:dgCol title="试驾车名称" field="name" width="120"></t:dgCol>
		   <t:dgCol title="车型" field="type" width="120" dictionary="carType"></t:dgCol>
		   <t:dgCol title="车牌" field="plateNo" width="120"></t:dgCol>
		   <t:dgCol title="无试驾起始日期" field="fallowStartTime" formatter="MM月dd日" width="120"></t:dgCol>
		   <t:dgCol title="无试驾结束日期" field="fallowStopTime" formatter="MM月dd日" width="120"></t:dgCol>
		   <t:dgCol title="无效试驾次数" field="driveTotal" width="120"></t:dgCol>
		   <t:dgCol title="状态" field="status" width="120" dictionary="car_status"></t:dgCol>
		   <%-- <t:dgCol title="操作" field="opt" width="200"></t:dgCol>
		   <t:dgFunOpt funname="detailById(id)" title="详情"></t:dgFunOpt> --%>
	   </t:datagrid>
	   <div id="departListtb" style="padding: 3px; height: 25px">
            <div style="float: left;">
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-putout" onclick="ExportXls()"><t:mutiLang langKey="excelOutput" langArg="common.department"/></a>
            </div>
        </div>
  </div>
</div>
</div>
	<div id="main" style="width: 1200px; height: 880px;margin-left: -10px;margin:auto"></div>



	<script type="text/javascript" src="plug-in/echarts/echarts.min.js"></script>
	<script type="text/javascript">
		//全局变量
		var nowDate = new Date;

		$(function() {
			$("#main_depart_list").hide();
			$("#type").change(function() {
				var type = $("#type").val();
				if ("fallow"==type) {
					$("#carType").show();
					$("#dimension").hide();
					$("#dimensionOrCarType").html("车型:");
				} else {
					$("#dimension").show();
					$("#carType").hide();
					$("#dimensionOrCarType").html("维度:");
				}
				$("#dimension").val("motorcycle");
				$("#carType").val("");
				initYearSel();
				initMonthSel();
				analyze();
			});
			$("#carType").hide();
			$("#carType").change(function() {
				analyze();
			});
			initYearSel();
			initMonthSel();
			var yealMonth = $("#yealSel").val()+$("#monthSel").val();
			$("#yealMonth").val(yealMonth);
			analyze();
		})

		//初始化年份下拉框
		function initYearSel() {
			//前10年
			var nowYear = nowDate.getFullYear();
			var yearSel = $("#yealSel");
			yearSel.html("");
			for (var i = nowYear; i >= nowYear - 9; i--) {
				yearSel.append("<option>" + i + "</option>");
			}
		}

		//初始化月份下拉框
		function initMonthSel() {
			var nowMonth = nowDate.getMonth() + 1;
			var monthSel = $("#monthSel");
			monthSel.html("");
			for (var i = 1; i <= 12; i++) {
				if (i == nowMonth)
					monthSel.append("<option selected='selected'>" + i
							+ "</option>");
				else
					monthSel.append("<option >" + i + "</option>");

			}
		}
		
		//访问接口，返回数据并渲染图表
		function analyze() {
			var type = $("#type").val();
			if ("fallow"==type) {
				var yealMonth = $("#yealSel").val()+$("#monthSel").val();
				$("#yealMonth").val(yealMonth);
				//跑后台 放session
				var yearMonth = {
						"year" : $("#yealSel").val(),
						"month" : $("#monthSel").val(),
						"type" : $("#carType").val()
				};
				
				$.ajax({
					url:"testDriveAnalyzeController.do?setSession",
					type:"post",
					data:JSON.stringify(yearMonth),
					dataType:"json",
					contentType:'application/json',
					success:function(){
						
					}
				});
				
				
				$("#main").hide();
				$("#main_depart_list").show();
				carInfoListsearch();
			} else {
				$("#main").show();
				$("#main_depart_list").hide();
				var yearMonth = {
						"year" : $("#yealSel").val(),
						"month" : $("#monthSel").val(),
						"wd" : $("#dimension").val()
				};
				$.ajax({
					url:"testDriveAnalyzeController.do?"+type,
					type:"post",
					data:JSON.stringify(yearMonth),
					dataType:"json",
					contentType:'application/json',
					success:function(data){
						if(data){
							var obj = data.obj;
							var xArr = new Array();
							var yArr = new Array();
							var i=0;
							for(var key in obj){
								xArr[i] = key;
								yArr[i] = obj[key];
								i++;
							}
							initCharts(xArr, yArr);
						}
						
					}
				});
			}	
		}

	function check(){
		$(".datagrid-view").height(430);
	}
	
	function initCharts(xVal, yVal){
		if(xVal == null || xVal.length <= 0 || yVal == null || yVal.length <= 0){
			$("#main").html("暂无数据");
			return;
		}
			
		var titleText = $("#type option:selected").text();
		var xName = null;
		var yName = null;
		var seriesName = null;
		switch(titleText){
			case "单次试驾时长":
				seriesName = "时长（m）";
				xName = "时长（m）";
			break;
			case "试驾成交率":
				seriesName = "成交率";
				xName = "数值";
			break;
			case "无效试驾率":
				seriesName = "无效试驾率";
				xName = "数值";
			break;
			case "单次试驾里程":
				seriesName = "里程";
				xName = "里程（m）";
			break;
			case "非试乘试驾行驶率":
				seriesName = "行驶率";
				xName = "数值";
			break;
			
		}
		
		
		var myChart = echarts.init(document.getElementById('main'));
		var option = {
				title: {
			        text: titleText,
			        left:'center'
			        
			    },
			    tooltip: {
			        trigger: 'axis',
			        axisPointer: {
			            type: 'shadow'
			        }
			    },
			    toolbox:{
			        show : true,
			            feature : {
			                saveAsImage : {show: true}
			            }
			    },
			    legend: {
			        data: [titleText],
			        show:false
			    },
			    grid: {
			        height:'800px',
			        width:'1000px',
			        x:200
			    },
			    xAxis: {
			        type: 'value',
			        boundaryGap: [0, 0.01],
			        name:seriesName
			    },
			    yAxis: {
			        type: 'category',
			        data: xVal
			    },
			    series: [
			        
			        {
			            name: seriesName,
			            type: 'bar',
			            data: yVal
			        }
			    ]
		}
		myChart.setOption(option); 
	}
	 function detailById(id){
		 var url = 'carInfoController.do?addorupdate&load=detail&id='+id;
		 createdetailwindow('详情',url,null,null);
	 }
	 function ExportXls(){
 		 JeecgExcelExport("carInfoController.do?exportXls","carInfoList");
 	 }
	</script>
</body>