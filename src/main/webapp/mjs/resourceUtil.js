/*
 * type 车型，imagePath 照片, title标题，subhead副标题，config配置，d
 * displacement排量，RPM最大功率，maxSpeed最大速度
 * */

var resourceUtil = {
	getDetailByType : function(type){
		var obj = null;
		for(var i=0; i<carTypes.length; i++){
			obj = carTypes[i];
			if(obj.type == type){
				return obj;
			}
		}
	}
}


var carTypes =  [
	{"type":"GC", "imagePath":"fileUpload.action?view&fileName=carType/GranCabrio-Sport.jpg", "title":"GranCabrio敞篷跑车系列","subhead":"随心所“驭”，自由驰骋", "config":{"displacement":"4.7","RPM":"7000","maxSpeed":"289"}},
	{"type":"GT", "imagePath":"fileUpload.action?view&fileName=carType/GT-Sport.jpg", "title":"GranTurismo跑车系列","subhead":"意式激情的完美演绎", "config":{"displacement":"4.7","RPM":"7000","maxSpeed":"303"}},
	{"type":"Ghibli", "imagePath":"fileUpload.action?view&fileName=carType/ghibli_2.jpg.jpg", "title":"Ghibli轿车系列","subhead":"卓尔不凡", "config":{"displacement":"3.0","RPM":"5000","maxSpeed":"285"}},
	{"type":"Ghibli Q4", "imagePath":"fileUpload.action?view&fileName=carType/ghibli_2.jpg.jpg", "title":"Ghibli轿车系列","subhead":"卓尔不凡", "config":{"displacement":"3.0","RPM":"5500","maxSpeed":"285"}},
	{"type":"Ghibli S", "imagePath":"fileUpload.action?view&fileName=carType/ghibli_2.jpg.jpg", "title":"Ghibli轿车系列","subhead":"卓尔不凡", "config":{"displacement":"3.0T","RPM":"5500","maxSpeed":"285"}},
	{"type":"QP V8", "imagePath":"fileUpload.action?view&fileName=carType/QP-SQ4-GranSport-Grigio-Maratea.jpg", "title":"新款Quattroporte总裁轿车系列","subhead":"玛莎拉蒂匠心之作", "config":{"displacement":"3.8","RPM":"6800","maxSpeed":"307"}},
	{"type":"QP V6", "imagePath":"fileUpload.action?view&fileName=carType/QP-SQ4-GranSport-Grigio-Maratea.jpg", "title":"新款Quattroporte总裁轿车系列","subhead":"玛莎拉蒂匠心之作", "config":{"displacement":"3.8","RPM":"6800","maxSpeed":"307"}},
	{"type":"QP 410", "imagePath":"fileUpload.action?view&fileName=carType/QP-SQ4-GranSport-Grigio-Maratea.jpg", "title":"新款Quattroporte总裁轿车系列","subhead":"玛莎拉蒂匠心之作", "config":{"displacement":"3.0","RPM":"286","maxSpeed":"286"}},
	{"type":"Levante", "imagePath":"fileUpload.action?view&fileName=carType/Grigia_sport_chrome_02.jpg", "title":"Levante","subhead":"THE MASERATI OF SUV", "config":{"displacement":"3.0T","RPM":"5750","maxSpeed":"264"}},
	{"type":"Levante S", "imagePath":"fileUpload.action?view&fileName=carType/Grigia_sport_chrome_02.jpg", "title":"Levante","subhead":"THE MASERATI OF SUV", "config":{"displacement":"3.0T","RPM":"5750","maxSpeed":"264"}},
	{"type":"MC20", "imagePath":"fileUpload.action?view&fileName=carType/MC20_front.jpg", "title":"MC20","subhead":"超级跑车", "config":{"displacement":"3.0L V6","RPM":"8000","maxSpeed":"325"}},
	{"type":"Grecale", "imagePath":"fileUpload.action?view&fileName=carType/gr_front.jpg", "title":"Grecale","subhead":"中型SUV", "config":{"displacement":"2.0T","RPM":"4500","maxSpeed":"240"}}

];
