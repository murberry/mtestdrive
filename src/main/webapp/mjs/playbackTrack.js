try {
	gathers = JSON.parse(gathers);
} catch (e) {
	gathers = new Array();
}

// 重新播放
function repeatPlay() {
	initLine();
}

$(function(){
	initLine();
})

function initLine(){
	if(!gathers || gathers.length == 0){
		$("#errorMsg").show();
		return false;
	}
	var pointArr = [];
    var total = [];
    translateCallback = function (data){
    	
    	total.push(new BMap.Point(data.points[0].lng,data.points[0].lat));
	}
    
    var convertor = new BMap.Convertor();
    for(var i=0;i<gathers.length;i++){
    	
    	
    	 var c=new Convertor();
	     var r1=c.WGS2BD09({lng:gathers[i].lon,lat:gathers[i].lat});
//	     console.log(r1);
	     
	     total.push(new BMap.Point(r1.lng,r1.lat));
    	 
    	 
//		 convertor.translate(pointArr, 1, 5, translateCallback)
	}
    
    console.log(total)
    
	// 画线
	 setTimeout(function(){
		 
		    var marker;
		    var map = new BMap.Map('currMap');
		    map.enableScrollWheelZoom();
		    map.centerAndZoom();
		    var lushu;
		    var arrPois=total;
		    
		                map.setViewport(arrPois);
		                   marker=new BMap.Marker(arrPois[0],{
		                      icon  : new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26),{anchor : new BMap.Size(27, 13)})
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
		                defaultContent:plateNo,//"从天安门到百度大厦"
		                autoView:true,//是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
		                icon  : new BMap.Icon('http://developer.baidu.com/map/jsdemo/img/car.png', new BMap.Size(52,26),{anchor : new BMap.Size(27, 13)}),
		                speed: 1000,
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
 
 
