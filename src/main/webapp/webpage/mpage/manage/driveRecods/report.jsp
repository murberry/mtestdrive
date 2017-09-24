<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>试驾报告</title>
<link rel="stylesheet"
	href="plug-in/bootstrap.min/css/bootstrap.min.css" />
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<style type="text/css">
		html,body{
			width:100%;
			height:100%;
			margin:0;
			font-family:"微软雅黑";
		}
		.thumbnail{
		width:100%;
		padding:15px;
		
		
		}
		.thumbnail>img{
			width:80% ;
			height:150px ;
			display: block;
			margin: 0 auto;
			border-radius: 10px;
		}
		.towSide .col-xs-6{
			padding:0 10px ;
			float:left;
			width:46%;
			
		}
		a:active{ text-decoration: none;}
a:hover{ text-decoration: none;}
.Distance_img{margin-top:20px ;}
.Distance{margin-bottom:20px ;}
.Speed_title{border-bottom:1px solid #cccccc ;}
.Speed{float: right; margin-right:15PX ; margin-top:10px ;}
.speeds{margin-right:15px ;}
.speeds_color{color:red ;}
.footers{background:#0f1d3a ; text-align: center;}
.footers_nav{padding-top:20px; padding-bottom: 10px ;}
.footers_nav p{color:#FFF; font-size:12px ; line-height:0.7;}
table th,table td{
	font-size: 12px;
	
}
.wrap_share{
	position: fixed;
	z-index: 99999;
	right: 25px;
	top: 25px;
}
.text-center{
text-align: center;}
.table tr>td,
.table tr>th {
	padding: 8px;
	line-height: 1.42857143;
	vertical-align: top;
	text-align: center;
}
table tr{
 border-width: 1px;  
    padding: 8px;    

}
table tr {
width:100%}
table tr th,td {
margin-right:5px;}
table{
width:100%;
margin: 0 auto !important;
}
table th{
   border-width: 1px;  
    padding: 8px; 
    font-weight: inherit;
  
}
.table{
margin:0 auto;
display: block;
width:95%
}
		.container{
		
		width: 100%;
		}
		
		.clearfix:after{content:".";display:block;height:0;clear:both;visibility:hidden}
.clearfix{*+height:1%;}
	</style>
</head>
<body>
	<header>
			<div class="container" style="padding-top: 10px;">
				
				<img src="images/heard_logo.png" width="100%" class="Distance_img" height="100" />
			</div>

		<div class=" container">
			
				<h5 id="titleLabel" class=" text-center" style="margin:0;padding:2px">新款Quattroporte总裁轿车系列</h5>
			  <h4 id="subheadLabel" class="text-center" style="margin:0;padding:2px">玛莎拉蒂匠心之作</h4>
			
			<div class="towSide clearfix">
			<div class="col-xs-6 ">
				<div class="thumbnail">
				<img id="carImg" src=""  height="100" onerror="this.src='images/onError.jpg'"/>
				</div>
			</div>
			<div class="col-xs-6">
				<div class="thumbnail">
			     <img id="endPicPath" src=""  height="100" onerror="this.src='images/onError.jpg'"/>
			     <input type="hidden" name="recodsId" id="recodsId" value="${driveRecodsVo.id }"/>
			</div>
			</div>
			</div>
	</div>
	
	<div class="table">
	
	<table width="100%">
           <tr>
          <th >客户姓名:</th>
           <th >销售顾问:</th>
           <th>车       型:</th>
           <th>车       牌：</th>
           <th>试驾时间：</th>
           <th>持续时间</th>
           <th>试驾里程（km）</th>
           <th>RPM：</th>
           <th>排  量(L)：</th>
        	<th>最高时速(km/h)：</th>
        </tr>
        <tr>
          <td>${driveRecodsVo.customer.name }</td>
          <td>${driveRecodsVo.salesman.name }</td>
          <td><span id="carTypeSpan">${driveRecodsVo.car.type }</span></td>
          <td>${driveRecodsVo.car.plateNo }</td>
          <td><fmt:formatDate value="${driveRecodsVo.driveStartTime}" pattern="yyyy-MM-dd" /></td>
          <td><span id="continueTime"></span></td>
           <td>${info.mileage}</td>
          <td><span id="rpmSpan"></span></td>
           <td><span id="displacementSpan"></span></td>
           <td><span id="maxSpeed"></span></td>
        </tr>
        
       </table>
	
	</div>
	<div class="Speed_title">
				<p class="text-info" style="font-size: 12px;font-style: italic;">行驶轨迹
				：</p> 
			</div>
		<div id="currMap" style=" height:280px;margin:0 auto">
					<div id="errorMsg" style="display: none">暂无数据</div>
				</div>
	
		 <div class="container" style="margin-top:15px ; width:100%"> 
			<div class="Speed_title">
				<p class="text-info" style="font-size: 12px;font-style: italic;">你的表现如下：</p> 
			</div>
			<div class="row Distance">
				<!-- <div class="Speed">
					<span class="speeds">●Speed</span>
					<span class="speeds_color">●RPM</span>
				</div> -->
		    	<div id="charts" style="height: auto; width:100%;"></div>
			</div>
		</div> 
		<div class="footers container " >
			<div class="footers_nav ">
				<p>www.maserati.it</p>
				<p>Maserati sp viale Ciro Menotti,322.41121 Modena .Italy</p>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="plug-in/bootstrap/js/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="plug-in/bootstrap/js/bootstrap.min.js"></script>
	<script src="http://api.map.baidu.com/api?v=2.0&ak=D2b4558ebed15e52558c6a766c35ee73"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js"></script>
    <script type="text/javascript" src="plug-in/echarts/echarts.min.js"></script>
	<!-- <script type="text/javascript" src="js/Chart.js" ></script>
	<script type="text/javascript" src="js/script.js" ></script> -->
	 <script type="text/javascript" src="mjs/resourceUtil.js"></script>
			<script type="text/javascript" src="mjs/report.js"></script>
	<script type="text/javascript">
		/* var poinstr="[[121.367824,31.118941],[121.377023,31.101132],[121.378173,31.095195],[121.383922,31.079361],[121.39772,31.053626],[121.403469,31.047686],[121.402319,31.035805],[121.441413,31.047686],[121.494306,31.055606],[121.509253,31.057586],[121.53455,31.057586],[121.596641,31.088268],[121.642634,31.096185],[121.617338,31.093216],[121.663331,31.094206],[121.66678,31.121909],[121.651833,31.144659],[121.634585,31.155537],[121.60239,31.157515],[121.537999,31.146637],[121.511553,31.140703],[121.446013,31.135757],[121.404619,31.135757],[121.385072,31.141692],[121.423016,31.142681],[121.469009,31.149604],[121.500055,31.15356],[121.493156,31.156526]]";
		var pointArr=	JSON.parse(poinstr);
			// 百度地图API功能
			var map = new BMap.Map("container");
			map.centerAndZoom(new BMap.Point(121.642634,  31.096185), 11);
			map.enableScrollWheelZoom();
			var points = [];
			for(var i=0;i<pointArr.length;i++){
				points.push(new BMap.Point(pointArr[i][0],pointArr[i][1])) ;
			}
			var curve = new BMapLib.CurveLine(points, {strokeColor:"red", strokeWeight:8, strokeOpacity:0.5}); //创建弧线对象
			map.addOverlay(curve); */ //添加到地图中
			//curve.enableEditing(); //开启编辑功能
			//缓存定位点数据
		var gathers = '${info.obdGathers}';
		var plateNo = '${info.plateNo }';
		
		var timeDifference = "${timeDifference}";
		var data = timeDifference/1000/60;
		var minute = parseInt(data);
		var second = parseInt((timeDifference-minute*60*1000)/1000)
		$("#continueTime").html(minute+"'"+second+"'");
		
		$("#charts").ready(function(){
			$("#charts").css({"width":"880px","height":"250px"});
		});
		
	
		initChars();
		function initChars(){
			//请求数据
			$.ajax({
				url : 'obdDriveRecodsController.do?getGatherInfoByRecodId&recodsId='+ $("#recodsId").val(),
				type : 'GET',
				timeout : 5000, // 超时时间
				dataType : 'json',
				success : function(data) {
					var xVal = new Array();
					var yVal = new Array();
					if(data && data.obj){
						for(var i=0; i<data.obj.length; i++){
							xVal[i] = i;
							yVal[i] = data.obj[i].obdSpd;
						}
						var myChart = echarts.init($('#charts')[0]);
						var option = {
								  title: {
								        text: '速度',
								        left:'right',
								        textStyle: {
								        	color: '#333',
								        	fontStyle: 'normal',
								        	fontWeight: 'normal',
								        	fontFamily: 'sans-serif',
								        	fontSize: 12,
								        },
								        
								    },
						    tooltip : {
						        trigger: 'axis',
						        show:false
						    },
						  grid: {
								     //height:'100px'
							     y2:6,
							     y:35
								    },
						    legend: {
						        data:['邮件营销'],
						      show:false
						    },
						    toolbox: {
						        show : false
						    },
						    calculable : true,
						    xAxis : [
						        {
						           show:true,
						            type : 'category',
						            boundaryGap : false,
						            data : xVal,
						            splitLine:{  
						                show:true
						            }
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value',
						            name:'km/h'
						        }
						    ],
						    series : [
						        {
						            type:'line',
						            symbol:'none',
						            smooth:true,
						            data:yVal
						        }
						    ]	
						}
						myChart.setOption(option); 
					}
				},
				error:function(){
					showErrorMsg();
				}
			});
		}
		window.onload=function(){
		infofun();
	}
		function infofun(){
			 document.getElementById('endPicPath').src="fileUploadController.do?view&fileName="+'${driveRecodsVo.endPicPath}';
		}
		
</script>
 <script type="text/javascript"
			src="mjs/playbackTrack.js"></script> 
			