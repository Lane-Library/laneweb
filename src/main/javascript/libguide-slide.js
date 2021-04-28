(function() {
	"use strict";

var slideIndex = 0,
	slides = document.getElementsByClassName("slide");
	slides[0].className += " active";



var next = function() {
	slides[slideIndex].className = slides[slideIndex].className.replace(
			new RegExp('( |^)' + "active" + '( |$)', 'g'), ' ').trim();
	slideIndex = (slides.length - 1 <= slideIndex) ? 0 : slideIndex + 1;
	slides[slideIndex].className += " active";
}

var previous = function() {
	slides[slideIndex].className = slides[slideIndex].className.replace(
			new RegExp('( |^)' + "active" + '( |$)', 'g'), ' ').trim();
	slideIndex = (0 >= slideIndex) ? slides.length - 1 : (slideIndex - 1);
	slides[slideIndex].className += " active";
}


document.getElementById("previous-libguide-slide").addEventListener("click",
		function(e) {
			previous();
		}, false);

document.getElementById("next-libguide-slide").addEventListener("click",
		function(e) {
			next();
		}, false);
})();