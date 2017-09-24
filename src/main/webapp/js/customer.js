var total = 10000;
		var breaker = 100;
		var turn = 100 / (total / breaker);
		var progress = 0;
		var timer = setInterval(function() {
			progress = progress + turn;
			$("#aa").html(progress + "% 完成");
			$("#processbar").attr("style", "width:" + progress + "%");
			if(progress >= 100) {
				clearInterval(timer);
			}
		}, breaker);
	
$(document).ready(function() {
			$('.nav li').click(function() {
				$(this).addClass("selected").siblings().removeClass();
				$("div >.tab").eq($('.nav li ').index(this)).show().siblings().hide();

			});
			$('.footer ul li').click(function() {
				$(this).addClass("selected selecte").siblings().removeClass(); 
				
			})
		});
	$(function() {
			var currYear = (new Date()).getFullYear();
			var opt = {};
			opt.date = {
				preset: 'date'
			};
			opt.datetime = {
				preset: 'datetime'
			};
			opt.time = {
				preset: 'time'
			};
			opt.default = {
				theme: 'android-ics light', //皮肤样式
				display: 'modal', //显示方式 
				mode: 'scroller', //日期选择模式
				dateFormat: 'yyyy-mm-dd',
				lang: 'zh',
				showNow: true,
				nowText: "今天",
				startYear: currYear - 10, //开始年份
				endYear: currYear + 10, //结束年份

			};

			$("#appDate").mobiscroll($.extend(opt['date'], opt['default']));
		  	
		  	$("#appDate1").mobiscroll($.extend(opt['date'], opt['default']));
			  	var optTime = $.extend(opt['time'], opt['default']);
			  $("#appTime").mobiscroll(optTime).time(optTime);
			   $("#appTime1").mobiscroll(optTime).time(optTime);

		});
