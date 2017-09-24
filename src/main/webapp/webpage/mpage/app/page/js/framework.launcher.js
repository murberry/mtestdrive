// JavaScript Document

$(window).load(function() { // makes sure the whole site is loaded
	$("#status").fadeOut(); // will first fade out the loading animation
	$("#preloader").delay(350).fadeOut("slow"); // will fade out the white DIV that covers the website.
})

$(document).ready(function(){
	
	//Detect if iOS WebApp Engaged and permit navigation without deploying Safari
	(function(a,b,c){if(c in b&&b[c]){var d,e=a.location,f=/^(a|html)$/i;a.addEventListener("click",function(a){d=a.target;while(!f.test(d.nodeName))d=d.parentNode;"href"in d&&(d.href.indexOf("http")||~d.href.indexOf(e.host))&&(a.preventDefault(),e.href=d.href)},!1)}})(document,window.navigator,"standalone")
	
	
	// Custom Navigation Events
	$(".next-slider").click(function(){
	  owl.trigger('owl.next');
	  return false;
	});
	$(".prev-slider").click(function(){
	  owl.trigger('owl.prev');
	  return false;
	});
	
	
	
	

	
	// Custom Navigation Events
	$(".next-quote").click(function(){
	  owlQuoteControls.trigger('owl.next');
	  return false;
	});
	$(".prev-quote").click(function(){
	  owlQuoteControls.trigger('owl.prev');
	  return false;
	});
	

	
	
	$(".swipebox").swipebox({
		useCSS : true, // false will force the use of jQuery for animations
		hideBarsDelay : 3000 // 0 to always show caption and action bar
	});
	
	$(".swipebox-wide").swipebox({
		useCSS : true, // false will force the use of jQuery for animations
		hideBarsDelay : 3000 // 0 to always show caption and action bar
	});
	
	


});
