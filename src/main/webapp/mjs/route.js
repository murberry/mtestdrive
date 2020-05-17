$(function() {
	$("#allmap").width($(window).width());
	$("#prevRoute").click(function() {
		if (nextRoute > 0)
			currentIndex--;			
		
		if (currentIndex == 0)
			setRouteInfo(currentIndex + 1);
		else
			setRouteInfo(currentIndex);

	});
	$("#nextRoute").click(function() {
		if (currentIndex < gathers.length - 1)
			currentIndex++;
		setRouteInfo(currentIndex + 1);
	});
})

function showMsg(){
	layer.msg("没有更多了");
}


function setRouteInfo(index) {
	// $("#routeInfo").html("路线" + index);
	printRoute(index - 1);
}

var begin = null;
var end = null;
var map = null;
// 解析路线数据
function printRoute(flag) {
	try{
		$("#routeId").val(gathers[flag].routeId);
		$("#routeInfo").html(gathers[flag].routeName);
		initMap(gathers[flag].gathers);
	}catch(e){
		$("#allmap").html("暂无线路数据 routeId="+gathers[flag].routeId);
	}
}

function initMap(jsonarr){
		var map = new BMap.Map("allmap");
	    var point = new BMap.Point(jsonarr[0].lon, jsonarr[0].lat);
	    map.centerAndZoom(point, 15);
	    map.enableScrollWheelZoom(); // 开启鼠标滚轮缩放
	    map.addControl(new BMap.ScaleControl()); // 添加比例尺控件
	    map.setMapStyle({style:'grayscale'});
	   
	    var pointArr = [];
	    var total = [];
	    translateCallback = function (data){
	    	total.push(new BMap.Point(data.points[0].lng,data.points[0].lat));
 		}
	    var convertor = new BMap.Convertor();
	    for(var i=0;i<jsonarr.length;i++){
	    	 pointArr = [];
	    	 pointArr.push(new BMap.Point(jsonarr[i].lon, jsonarr[i].lat));
			 convertor.translate(pointArr, 1, 5, translateCallback)
		}
	    
    	// 画线
    	 setTimeout(function(){
    		 map.centerAndZoom(total[0], 15);
    		 var polyline = new BMap.Polyline(total, {
			        strokeColor: "#1869AD",
			        strokeWeight: 5,
			        strokeOpacity: 1
			    });
			    map.addOverlay(polyline);
	    }, 3000);
}



