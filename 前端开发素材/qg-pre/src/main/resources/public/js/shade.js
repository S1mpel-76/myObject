function ShowDiv(show_div,bg_div){
	document.getElementById(show_div).style.display='block';
	document.getElementById(bg_div).style.display='block' ;
	var bgdiv = document.getElementById(bg_div);
	var width = document.body.scrollWidth;
	bgdiv.style.width = width;
	$("#"+bg_div).height($(document).height());
};
function CloseDiv(show_div,bg_div){
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
};
