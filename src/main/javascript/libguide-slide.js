(function() {
	"use strict";

	var slides = document.getElementsByClassName("slide"), slideIndex, container, containerHeight, padding, style,
		h3_height = 60;

	var setSlideContainerHeight = function(slide){
		container = document.getElementById("libguide-slide-container");
		style = container.currentStyle || window.getComputedStyle(container);
	    padding = parseFloat(style.paddingTop) + parseFloat(style.paddingBottom) ;
	    //The container height have to be the slide height + the padding from the slidescontainer + the height the
	    // the h3 title
		containerHeight = slide.clientHeight + padding + h3_height;
		container.style.minHeight = containerHeight +"px";
	}

	var next = function() {
		slides[slideIndex].className = "slide desactive-next";
		slideIndex = (slides.length - 1 <= slideIndex) ? 0 : slideIndex + 1;
		slides[slideIndex].className = "slide active-next";
		setSlideContainerHeight(slides[slideIndex]);

	}

	var previous = function() {
		slides[slideIndex].className = "slide desactive-previous";
		slideIndex = (0 >= slideIndex) ? slides.length - 1 : (slideIndex - 1);
		slides[slideIndex].className = "slide active-previous";
		setSlideContainerHeight(slides[slideIndex]);
	}

	if (slides !== undefined && slides.length > 0) {
		slideIndex = Math.floor(Math.random() * slides.length);
		slides[slideIndex].className += " active";
		setSlideContainerHeight(slides[slideIndex]);
		document.getElementById("previous-libguide-slide").addEventListener(
				"click", function(e) {
					previous();
				}, false);

		document.getElementById("next-libguide-slide").addEventListener(
				"click", function(e) {
					next();

				}, false);
	}



})();