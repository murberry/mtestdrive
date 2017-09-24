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
			});
			$("#carType").hide();
			$("#carType").change(function() {
				analyze();
			});
			initYearSel();
			initMonthSel();
			var yealMonth = $("#yealSel").val()+$("#monthSel").val();
			$("#yealMonth").val(yealMonth);
			 $("#region").change(function(){
				var type =$("#type").val();
				var yearMonth = {
						"year" : $("#yealSel").val(),
						"month" : $("#monthSel").val(),
						"wd" : $("#dimension").val(),
						"region" : $("#region").val()
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
			}); 
			$("#region").hide();
			analyze();
		})

		//初始化年份下拉框
		function initYearSel() {
			//前10年
			var nowYear = nowDate.getFullYear();
			var yearSel = $("#yealSel");

			for (var i = nowYear; i >= nowYear - 9; i--) {
				yearSel.append("<option>" + i + "</option>");
			}
		}

		//初始化月份下拉框
		function initMonthSel() {
			var nowMonth = nowDate.getMonth() + 1;
			var monthSel = $("#monthSel");

			for (var i = 1; i <= 12; i++) {
				if (i == nowMonth)
					monthSel.append("<option selected='selected'>" + i
							+ "</option>");
				else
					monthSel.append("<option >" + i + "</option>");

			}
		}
		
		function analyze() {
			var type =$("#type").val();
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
						"wd" : $("#dimension").val(),
						"region" : $("#region").val()
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
		 function detailById(id){
			 var yeal = $("#yealSel").val();
			 var month = $("#monthSel").val();
			 var type = $("#carType").val();
			 var url = 'carInfoController.do?addorupdate&load=detail&id='+id;
			 createdetailwindow('详情',url,null,null);
		 }
		 function initCharts(xVal, yVal){
				if(xVal == null || xVal.length <= 0 || yVal == null || yVal.length <= 0){
					$("#main").html("暂无数据");
					return false;
				}
					
				var titleText = $("#type option:selected").text();
				var xName = null;
				var yName = null;
				var seriesName = null;
				switch(titleText){
					case "单次试驾时长":
						seriesName = "时长（m）";
						yName = "时长（m）";
					break;
					case "试驾成交率":
						seriesName = "成交率";
						yName = "数值";
					break;
					case "无效试驾率":
						seriesName = "无效试驾率";
						yName = "数值";
					break;
					case "单次试驾里程":
						seriesName = "里程";
						yName = "里程（m）";
					break;
					case "非试乘试驾行驶率":
						seriesName = "行驶率";
						yName = "数值";
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
					        height:'300px',
					        width:'800px',
					        x:100
					    },
					    xAxis: {
					        type: 'value',
					        boundaryGap: [0, 0.01],
					        name:seriesName
					    },
					    yAxis: {
					        type: 'category',
					        data: xVal,
					        name:seriesName
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
	$(".weidu_super_admin").change(function(){
		if($(this).val() == "store"){
			$("#region").show();
		}else{
			$("#region").hide();
		}
	});
	function ExportXls(){
		 JeecgExcelExport("carInfoController.do?exportXls","carInfoList");
	 }