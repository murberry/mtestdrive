$(function() {
	//初始化车型信息
	var carType = $("#carTypeSpan").html();//本次试驾的车型
	
	var typeObj = resourceUtil.getDetailByType(carType);
	try{
		$("#titleLabel").html(typeObj.title);
		$("#subheadLabel").html(typeObj.subhead);
		$("#carImg").attr("src", typeObj.imagePath);
		$("#displacementSpan").html(typeObj.config.displacement);
		$("#rpmSpan").html(typeObj.config.RPM);
		$("#maxSpeed").html(typeObj.config.maxSpeed);
	}catch(e){
		console.log("车型信息有误");
	}

	
	initChars();
	
	//初始化路线
	$.ajax({
		url : 'routeInfoAction.action?getRouteById&id='+ $("#routeId").val(),
		type : 'GET',
		timeout : 5000, // 超时时间
		dataType : 'json',
		success : function(data) {
			if(data.success && data.obj.length > 0){
				var jsonarr = data.obj;
				var map = new BMap.Map("allmap");
				map.disableDragging();// 禁止拖动
				map.setMapStyle({
					style : 'light'
				});
				// 百度地图API功能

				// map.centerAndZoom(new BMap.Point(116.404, 39.915), 15);
				var bounds = null;
				var linesPoints = null;
				var spoi2 = new BMap.Point(data.obj[0].lon, data.obj[0].lat); // 起点2
				var epoi = new BMap.Point(
						data.obj[data.obj.length - 1].lon,
						data.obj[data.obj.length - 1].lat); // 终点
				var myIcon = new BMap.Icon(
						"http://developer.baidu.com/map/jsdemo/img/Mario.png",
						new BMap.Size(32, 70), {
							imageOffset : new BMap.Size(0, 0)
						});
				function initLine() {
					bounds = new Array();
					linesPoints = new Array();
					map.clearOverlays(); // 清空覆盖物
					var driving4 = new BMap.DrivingRoute(map, {
						onSearchComplete : drawLine
					}); // 驾车实例,并设置回调
					driving4.search(spoi2, epoi); // 搜索一条线路
				}

				function drawLine(results) {
					var opacity = 0.45;
					var planObj = results.getPlan(0);
					var b = new Array();
					var addMarkerFun = function(point, imgType, index,
							title) {
						var url;
						var width;
						var height
						var myIcon;
						// imgType:1的场合，为起点和终点的图；2的场合为车的图形
						if (imgType == 1) {
							url = "http://developer.baidu.com/map/jsdemo/img/dest_markers.png";
							width = 42;
							height = 34;
							myIcon = new BMap.Icon(url, new BMap.Size(
									width, height), {
								offset : new BMap.Size(14, 32),
								imageOffset : new BMap.Size(0, 0 - index
										* height)
							});
						} else {
							url = "http://developer.baidu.com/map/jsdemo/img/trans_icons.png";
							width = 22;
							height = 25;
							var d = 25;
							var cha = 0;
							var jia = 0
							if (index == 2) {
								d = 21;
								cha = 5;
								jia = 1;
							}
							myIcon = new BMap.Icon(url, new BMap.Size(
									width, d), {
								offset : new BMap.Size(10, (11 + jia)),
								imageOffset : new BMap.Size(0, 0 - index
										* height - cha)
							});
						}

						var marker = new BMap.Marker(point, {
							icon : myIcon
						});
						if (title != null && title != "") {
							marker.setTitle(title);
						}
						// 起点和终点放在最上面
						if (imgType == 1) {
							marker.setTop(true);
						}
						map.addOverlay(marker);
					}
					var addPoints = function(points) {
						for (var i = 0; i < points.length; i++) {
							bounds.push(points[i]);
							b.push(points[i]);
						}
					}
					// 绘制驾车步行线路
					for (var i = 0; i < planObj.getNumRoutes(); i++) {
						var route = planObj.getRoute(i);
						if (route.getDistance(false) <= 0) {
							continue;
						}
						addPoints(route.getPath());
						// 驾车线路
						if (route.getRouteType() == BMAP_ROUTE_TYPE_DRIVING) {
							map.addOverlay(new BMap.Polyline(route
									.getPath(), {
								strokeColor : "#0030ff",
								strokeOpacity : opacity,
								strokeWeight : 6,
								enableMassClear : true
							}));
						} else {
							// 步行线路有可能为0
							map.addOverlay(new BMap.Polyline(route
									.getPath(), {
								strokeColor : "#30a208",
								strokeOpacity : 0.75,
								strokeWeight : 4,
								enableMassClear : true
							}));
						}
					}
					map.setViewport(bounds);
					// 终点
					addMarkerFun(results.getEnd().point, 1, 1);
					// 开始点
					addMarkerFun(results.getStart().point, 1, 0);
					linesPoints[linesPoints.length] = b;
				}
				initLine();
			}else{
				$("#allmap").html("暂无路线信息");
			}
			
		},
		error : function(xhr, textStatus) {
			showErrorMsg();
		}
	})
			

})

function showErrorMsg(){
	layer.msg("系统异常，请联系我们");
}

//初始化速度与路线
function initChars(){
	//请求数据
	$.ajax({
		url : 'driveRecodsAction.action?getGatherInfoByRecodId&recodsId='+ $("#recodsId").val(),
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