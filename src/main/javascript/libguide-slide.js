(function() {
	"use strict";

	var slides = document.getElementsByClassName("slide"), slideIndex;

	if (slides !== undefined && slides.length > 0) {
		slideIndex = Math.floor(Math.random() * slides.length);
		slides[slideIndex].className += " active";

		document.getElementById("previous-libguide-slide").addEventListener(
				"click", function(e) {
					previous();
				}, false);

		document.getElementById("next-libguide-slide").addEventListener(
				"click", function(e) {
					next();
				}, false);
	}

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

})();