var lineChartData = {
	//X������
	labels : ["20","40","60","80","100"],
	datasets : [

		{
			fillColor : "rgba(255,255,255,0)",
			strokeColor : "rgba(92, 184, 92, 1)",
			pointColor : "rgba(23, 126, 23, 1)",
			pointStrokeColor : "#fff",
			pointHighlightFill : "#fff",
			pointHighlightStroke : "rgba(151,187,205,1)",
			data : [1,2,1,4,2,4,5,7,8,8,4,3]
		}
	]

}

window.onload = function(){
	var ctx = document.getElementById("canvas").getContext("2d");
	window.myLine = new Chart(ctx).Line(lineChartData, {
		responsive: true
	});
}