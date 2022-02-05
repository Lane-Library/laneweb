(function() {
	"use strict";

	var slides = document.querySelectorAll(".slide"),
		container = document.getElementById("slide-show"),
		nextButton = document.getElementById("next-slide"),
		previousButton = document.getElementById("previous-slide"),
		slideIndex, padding = 24;




	var calculateImageNumberDisplayed = function() {
		var imageDiv = slides[slides.length - 1];
		var t = parseFloat(container.offsetWidth) / (parseFloat(imageDiv.offsetWidth + padding));
		return parseInt(t);
	}

		var previous = function() {
		if (slideIndex < slides.length - calculateImageNumberDisplayed()) {
			slides[slideIndex].className = "slide desactive-next";
			slideIndex = slideIndex +1;
		}
		toogleButton();
		
	}



	var next = function() {
		if (slideIndex >= 0 ) {
			slideIndex = slideIndex - 1;
			slides[slideIndex].className = "slide";
		}
		toogleButton();
	}
	

	var toogleButton = function(){
		if(slideIndex == 0){
			nextButton.className ="disable";
		}else{
			nextButton.className ="";	
		}
		if (slideIndex == slides.length - calculateImageNumberDisplayed()){
			previousButton.className = "disable";
		}else{
			previousButton.className = "";
		}
		
	}

	if (slides !== undefined && slides.length > 0) {
		slideIndex = 0;
		nextButton.className = "disable";
		previousButton.addEventListener(
			"click", function(e) {
				previous();
			}, false);

		nextButton.addEventListener(
			"click", function(e) {
				next();
			}, false);
	}


})();