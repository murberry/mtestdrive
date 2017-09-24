<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>试驾路线</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript" scr="mjs/routeInfo.js"></script>
<script type="text/javascript" src="plug-in/layer/layer.js"></script>

<style>
</style>
</head>
<body style="overflow-y: hidden" scroll="no">
	<table>
		<tr>
			<td><t:formvalid formid="formobj" dialog="true"
					usePlugin="password" layout="table"
					action="routeInfoController.do?save">
					<input id="id" name="id" type="hidden" value="${routeInfoPage.id }">
					<input id="startTime" name="startTime" type="hidden"
						value="${routeInfoPage.startTime }">
					<input id="endTime" name="endTime" type="hidden"
						value="${routeInfoPage.endTime }">
					<input id="termId" name="termId" type="hidden"
						value="${routeInfoPage.termId }">
					<input id="plateNo" name="plateNo" type="hidden"
						value="${routeInfoPage.plateNo }">
					<table style="width: 600px;" cellpadding="0" cellspacing="1"
						class="formtable">

						<tr>
							<td align="right"><label class="Validform_label">
									线路名称: </label></td>
							<td class="value"><input class="inputxt" id="name"
								name="name" datatype="*2-50" value="${routeInfoPage.name}">
								<span class="Validform_checktip"></span></td>
						</tr>
						<tr>
							<td align="right"><label class="Validform_label">
									车型: </label></td>
							<td class="value"><t:dictSelect field="carType" id="carType"
									typeGroupCode="carType" hasLabel="false" datatype="*2-10"
									defaultVal="${routeInfoPage.carType}"></t:dictSelect> <span
								style="color: red">*</span></td>
						</tr>
						<tr>
							<td align="right"><label class="Validform_label">
									车辆: </label></td>
							<td class="value"><select id="plateNoSel" datatype="*2-50">
									<option plateNo="${routeInfoPage.plateNo }"
										obdId="${routeInfoPage.termId }"
										value="${routeInfoPage.plateNo }">
										${routeInfoPage.plateNo }</option>
							</select> <span style="color: red">*</span></td>

						</tr>

						<tr>
							<td align="right"><label class="Validform_label">
									备注: </label></td>
							<td class="value"><textarea
									style="width: 300px; height: 50px;" class="inputxt" id="remark"
									name="remark" value="${routeInfoPage.remark}">${routeInfoPage.remark}</textarea></td>
						</tr>

						<tr>
							<td align="center" colspan="2" class="status_tab">
								<table style="width: 100%">
									<tr>
										<td>
											<table style="width: 100%;">
												<tr>
													<td align="right"><a type="button"
														href="javascript:gatherStart()" class="operation_route" title="点击开始按钮，系统将立即采集数据欧">
															<img alt="" src="images/hand_right.png" width="20px">
															开始
													</a></td>
													<td style="width: 216px;">
														<div id="status_info" style="display: none">
															<img alt="" src="images/5-121204193R7-51.gif">
															<div style="color: #c9302c">数据采集中，请勿关闭当前页面...</div>
														</div> <label style="color: #398439; display: none"
														id="gather_suc">数据采集完毕</label>
													</td>
													<td><a type="button" class="operation_route" href="javascript:gatherEnd()"
														title="点击结束按钮，系统将停止采集数据欧"> 结束 <img alt=""
															src="images/hand_left.png" width="20px">
													</a></td>
												</tr>
											</table>
										</td>
										<td style="width: 150px;">
											<div style="width: 150px; color: #31b0d5; font-size: 11px;">
												提示：点击开始按钮，系统将立即采集所选车辆的行驶轨迹作为当前线路的行驶依据，点击结束按钮停止采集。</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>

					</table>
				</t:formvalid></td>
			<td>
				<div>
					<div id="allmap" style="width: 390px; height: 318px;"></div>
				</div>
			</td>
		</tr>
	</table>

	<script type="text/javascript"
		src="http://api.map.baidu.com/api?v=2.0&ak=4lb48bMFrQh09NzCuuul4W2e7YpAGrNG"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js"></script>
	<script type="text/javascript">
	var car;
		initRouteInfo();
		function initRouteInfo() {
			//初始化路线
			$.ajax({
						url : 'routeInfoController.do?getRouteById&id=${routeInfoPage.id }',
						type : 'GET',
						timeout : 5000, // 超时时间
						dataType : 'json',
						async:'false',
						success : function(data) {
							initMap(data);
						},
						error : function(xhr, textStatus) {
							showErrorMsg();
						}
					})
		}
	</script>


	<script type="text/javascript">
		$(function() {
			
			//如果是查看，就禁用按钮
			if($("#carType").attr("disabled")){
				$(".operation_route").removeAttr("href");
			}
			
			
			$("#carType").change(function() {
				ajaxInitPlateNo($(this).val());
			});

		})
		$("#plateNoSel").change(function() {
			for (var i = 0; i < car.length; i++) {
				if(car[i].plateNo==$(this).val()){
					$("#termId").val(car[i].obdId);
					$("#plateNo").val(car[i].plateNo);
				}
					
			}
		});
		//异步加载车牌下拉框内的数据
		function ajaxInitPlateNo(carType) {
			$("#plateNoSel").html("");
			$.ajax({
						type : 'GET',
						url : 'carInfoController.do?getCarByType&type='
								+ carType,// 请求的action路径
						dataType : 'JSON',
						error : function() {// 请求失败处理函数
						},
						success : function(data) {
							var cars = data.obj;
							if (cars) {
								$("#plateNoSel").html(" ");
								for (var i = 0; i < cars.length; i++) {
									$("#plateNoSel")
											.append(
													"<option plateNo='"+cars[i].plateNo+"' obdId='"+cars[i].obdId+"' value='"+cars[i].plateNo+"'>"
															+ cars[i].plateNo
															+ "</option>");
								}
								car = cars ;
								$("#termId").val(cars[0].obdId);
								$("#plateNo").val(cars[0].plateNo);
							}
						}
					});
		}

		//开始采集数据
		function gatherStart() {
			//询问框
			layer.confirm('您确定要开始采集数据吗？', {
				btn : [ '确定', '取消' ]
			//按钮
			}, function() {
				$("#status_info").show();
				$("#gather_suc").hide();
				$("#startTime").val(getNowFormatDate1());
				layer.closeAll();
				$("#allmap").html("数据采集中");
			}, function() {
				layer.closeAll();
			});

		}

		//数据采集结束
		function gatherEnd() {
			//询问框
			layer.confirm('您确定要终止采集数据吗？', {
				btn : [ '确定', '取消' ]
			//按钮
			}, function() {
				$("#status_info").hide();
				$("#gather_suc").show();
				$("#endTime").val(getNowFormatDate1());
				layer.closeAll();
				//初始化路线
		        $.ajax({
					url : 'routeInfoController.do?getObdDatasByTimeQuantum&obdId='+$("#termId").val()+'&startTime='+$("#startTime").val()+'&endTime='+$("#endTime").val(),
					type : 'GET',
					timeout : 5000, // 超时时间
					async:false,
					dataType : 'json',
					success : function(data) {
						initMap(data);
					},
					error : function(xhr, textStatus) {
						showErrorMsg();
					}
				})
				
				
				
			}, function() {
				layer.closeAll();
			});
		}
		//渲染地图页面
		function initMap(data){
			if (data.success && data.obj.length > 0) {
				var jsonarr = data.obj;
				var map = new BMap.Map("allmap");
			    var point = new BMap.Point(jsonarr[0].lon,jsonarr[0].lat);
			    map.centerAndZoom(point, 15);
			    map.enableScrollWheelZoom(); // 开启鼠标滚轮缩放
			    map.addControl(new BMap.ScaleControl()); // 添加比例尺控件
			    var pointArr = [];
			    var total = [];
			    var x = data.obj[0].Mileage;
			    var a = $("#remark").val();
			    $("#remark").val(a+"   路线："+x+"公里");
			    
			    translateCallback = function (data){
			    	total.push(new BMap.Point(data.points[0].lng,data.points[0].lat));
		 		}
			    var convertor = new BMap.Convertor();
			    for(var i=0;i<jsonarr.length;i++){
			    	 var c=new Convertor();
				     var r1=c.WGS2BD09({lng:jsonarr[i].lon,lat:jsonarr[i].lat});
			     	 console.log(r1);
				     
				     total.push(new BMap.Point(r1.lng,r1.lat));
				}
		    	// 画线
		    	setTimeout(function(){
					var plateNo = '';
		    		var marker;
		 		    map.enableScrollWheelZoom();
		 		    map.centerAndZoom();
		 		    var lushu;
		 		   
		 		    var arrPois=total;
		 		                map.setViewport(arrPois);
		 		                   marker=new BMap.Marker(arrPois[0],{//images/car.png
		 		                      icon  : new BMap.Icon('images/car.png', new BMap.Size(52,26),{anchor : new BMap.Size(27, 13)})
		 		                   });
		 		      var label = new BMap.Label(plateNo,{offset:new BMap.Size(0,-30)});
		 		      label.setStyle({border:"1px solid rgb(204, 204, 204)",color: "rgb(0, 0, 0)",borderRadius:"10px",padding:"5px",background:"rgb(255, 255, 255)",});
		 		                marker.setLabel(label);
		 		                 
		 		      map.addOverlay(marker);
		 			  map.enableScrollWheelZoom(); // 开启鼠标滚轮缩放
		 					    map.addControl(new BMap.ScaleControl()); // 添加比例尺控件
		 		     BMapLib.LuShu.prototype._move=function(initPos,targetPos,effect) {
		 		            var pointsArr=[initPos,targetPos];  //点数组
		 		            var me = this,
		 		                //当前的帧数
		 		                currentCount = 0,
		 		                //步长，米/秒
		 		                timer = 5,
		 		                step = this._opts.speed / (1000 / timer),
		 		                //初始坐标
		 		                init_pos = this._projection.lngLatToPoint(initPos),
		 		                //获取结束点的(x,y)坐标
		 		                target_pos = this._projection.lngLatToPoint(targetPos),
		 		                //总的步长
		 		                count = Math.round(me._getDistance(init_pos, target_pos) / step);
		 		                 //显示折线 syj201607191107
		 		            this._map.addOverlay(new BMap.Polyline(pointsArr, { 
		 		                strokeColor : "#111", 
		 		                strokeWeight : 5, 
		 		                strokeOpacity : 0.5 
		 		            })); // 画线 
		 		            //如果小于1直接移动到下一点
		 		            if (count < 1) {
		 		                me._moveNext(++me.i);
		 		                return;
		 		            }
		 		            me._intervalFlag = setInterval(function() {
		 		            //两点之间当前帧数大于总帧数的时候，则说明已经完成移动
		 		                if (currentCount >= count) {
		 		                    clearInterval(me._intervalFlag);
		 		                    //移动的点已经超过总的长度
		 		                    if(me.i > me._path.length){
		 		                        return;
		 		                    }
		 		                    //运行下一个点
		 		                    me._moveNext(++me.i);
		 		                }else {
		 		                        currentCount++;
		 		                        var x = effect(init_pos.x, target_pos.x, currentCount, count),
		 		                            y = effect(init_pos.y, target_pos.y, currentCount, count),
		 		                            pos = me._projection.pointToLngLat(new BMap.Pixel(x, y));
		 		                        //设置marker
		 		                        if(currentCount == 1){
		 		                            var proPos = null;
		 		                            if(me.i - 1 >= 0){
		 		                                proPos = me._path[me.i - 1];
		 		                            }
		 		                            if(me._opts.enableRotation == true){
		 		                                 me.setRotation(proPos,initPos,targetPos);
		 		                            }
		 		                            if(me._opts.autoView){
		 		                                if(!me._map.getBounds().containsPoint(pos)){
		 		                                    me._map.setCenter(pos);
		 		                                }  
		 		                            }
		 		                        }
		 		                        //正在移动
		 		                        me._marker.setPosition(pos);
		 		                        //设置自定义overlay的位置
		 		                        me._setInfoWin(pos);  
		 		                    }
		 		            },timer);
		 		        };
		 		                lushu = new BMapLib.LuShu(map,arrPois,{
		 		                defaultContent:plateNo,//"从天安门到百度大厦"  images/car.png
		 		                autoView:true,//是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
		 		                icon  : new BMap.Icon('images/car.png', new BMap.Size(52,26),{anchor : new BMap.Size(27, 13)}),
		 		                speed: 2500,
		 		                enableRotation:true//是否设置marker随着道路的走向进行旋转                                   
		 		                }); 
		 		         
		 		      marker.addEventListener("click",function(){
		 		        marker.enableMassClear();   //设置后可以隐藏改点的覆盖物
		 		        marker.hide();
		 		        lushu.start();
		 		        //map.clearOverlays();  //清除所有覆盖物
		 		      });
		 		     
		 		      
		 		      marker.enableMassClear(); //设置后可以隐藏改点的覆盖物
		 		      marker.hide();
		 		      lushu.start();
			    }, 1000);
			} else {
				$("#allmap").html("暂无路线信息");
			} 
		}
		
		function getNowFormatDate1() {

		    var date = new Date();

		    var seperator1 = "-";

		    var seperator2 = ":";

		    var year = date.getFullYear();

		    var month = date.getMonth() + 1;

		    var strDate = date.getDate();
		    
		    var hour = date.getHours();
		    
		    var minutes = date.getMinutes();
		    
		    var seconds = date.getSeconds();

		    if (month >= 1 && month <= 9) {

		        month = "0" + month;

		    }

		    if (strDate >= 0 && strDate <= 9) {

		        strDate = "0" + strDate;

		    }
		    
		    if (hour >= 0 && hour <= 9) {

		    	hour = "0" + hour;

		    }
		    
		    if (minutes >= 0 && minutes <= 9) {

		    	minutes = "0" + minutes;

		    }
		    
		    if (seconds >= 0 && seconds <= 9) {

		    	seconds = "0" + seconds;

		    }
		    var currentdate = year + seperator1 + month + seperator1 + strDate

		            + " " + hour + seperator2 + minutes

		            + seperator2 + seconds;

		    return currentdate;

		}
		
		/**
		 * 坐标系转换函数
		 * WGS->GCJ
		 * WGS->BD09
		 * GCJ->BD09
		 */
		function Convertor(ak) {
		    this.stepCount = 100;
		    this.pointCount = [];
		    this.Result = [];
		    this.NoisIndex = [];
		    this.Time = new Date();
		    this.AK = ak;
		    this.M_PI = 3.14159265358979324;
		    this.A = 6378245.0;
		    this.EE = 0.00669342162296594323;
		    this.X_PI = this.M_PI * 3000.0 / 180.0;
		}
		Convertor.prototype.outofChine = function(p) {
		    if (p.lng < 72.004 || p.lng > 137.8347) {
		        return true;
		    }
		    if (p.lat < 0.8293 || p.lat > 55.8271) {
		        return true;
		    }
		    return false;
		}
		;
		Convertor.prototype.WGS2GCJ_lat = function(x, y) {
		    var ret1 = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		    ret1 += (20.0 * Math.sin(6.0 * x * this.M_PI) + 20.0 * Math.sin(2.0 * x * this.M_PI)) * 2.0 / 3.0;
		    ret1 += (20.0 * Math.sin(y * this.M_PI) + 40.0 * Math.sin(y / 3.0 * this.M_PI)) * 2.0 / 3.0;
		    ret1 += (160.0 * Math.sin(y / 12.0 * this.M_PI) + 320 * Math.sin(y * this.M_PI / 30.0)) * 2.0 / 3.0;
		    return ret1;
		}
		;
		Convertor.prototype.WGS2GCJ_lng = function(x, y) {
		    var ret2 = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		    ret2 += (20.0 * Math.sin(6.0 * x * this.M_PI) + 20.0 * Math.sin(2.0 * x * this.M_PI)) * 2.0 / 3.0;
		    ret2 += (20.0 * Math.sin(x * this.M_PI) + 40.0 * Math.sin(x / 3.0 * this.M_PI)) * 2.0 / 3.0;
		    ret2 += (150.0 * Math.sin(x / 12.0 * this.M_PI) + 300.0 * Math.sin(x / 30.0 * this.M_PI)) * 2.0 / 3.0;
		    return ret2;
		}
		;
		Convertor.prototype.WGS2GCJ = function(poi) {
		    if (this.outofChine(poi)) {
		        return;
		    }
		    var poi2 = {};
		    var dLat = this.WGS2GCJ_lat(poi.lng - 105.0, poi.lat - 35.0);
		    var dLon = this.WGS2GCJ_lng(poi.lng - 105.0, poi.lat - 35.0);
		    var radLat = poi.lat / 180.0 * this.M_PI;
		    var magic = Math.sin(radLat);
		    magic = 1 - this.EE * magic * magic;
		    var sqrtMagic = Math.sqrt(magic);
		    dLat = (dLat * 180.0) / ((this.A * (1 - this.EE)) / (magic * sqrtMagic) * this.M_PI);
		    dLon = (dLon * 180.0) / (this.A / sqrtMagic * Math.cos(radLat) * this.M_PI);
		    poi2.lat = poi.lat + dLat;
		    poi2.lng = poi.lng + dLon;
		    return poi2;
		}
		;
		Convertor.prototype.GCJ2BD09 = function(poi) {
		    var poi2 = {};
		    var x = poi.lng
		      , y = poi.lat;
		    var z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * this.X_PI);
		    var theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * this.X_PI);
		    poi2.lng = z * Math.cos(theta) + 0.0065;
		    poi2.lat = z * Math.sin(theta) + 0.006;
		    return poi2;
		}
		;
		/**
		* WGS->百度坐标系
		*/
		Convertor.prototype.WGS2BD09 = function(poi) {
		    //WGS->GCJ
		    var poi2 = this.WGS2GCJ(poi);
		    if (typeof poi2 === "undefined") {
		        return;
		    }
		    //GCJ->百度坐标系
		    return this.GCJ2BD09(poi2);
		}
	</script>
</body>