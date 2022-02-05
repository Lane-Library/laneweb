(function() {
	"use strict";

	var slides = document.querySelectorAll(".slide"),
		container = document.getElementById("slide-show"),
		nextButton = document.getElementById("next-slide"),
		previousButton = document.getElementById("previous-slide"),
		padding = 24,



		model = function(slides) {

			var m = {
				slides: slides,
				index: 0,
				maxPreviousClick: slides.length - 1
			};
			return m;

		}(slides),

		view = function() {
			nextButton.className = "disable";
			return {
				updateButton: function(index, slidesLength) {
					if (index >= slidesLength) {
						previousButton.className = "disable";
					} else {
						previousButton.className = "";
					}
					if (index <= 0) {
						nextButton.className = "disable";
					} else {
						nextButton.className = "";
					}
				}
			}
		}(),

		controller = function() {

			return {
				next: function() {
					model.index--;
					model.slides[model.index].className = "slide";
					view.updateButton(model.index, model.maxPreviousClick);
				},
				previous: function() {
					model.slides[model.index].className = "slide desactive-next";
					model.index++;
					view.updateButton(model.index, model.maxPreviousClick);
				},
				maxPreviousClicks: function() {
					var imageDiv = model.slides[model.slides.length - 1];
					var t = parseFloat(container.offsetWidth) / (parseFloat(imageDiv.offsetWidth + padding));
					model.maxPreviousClick = model.slides.length - parseInt(t);
				}
			}
		}();



	if (slides !== undefined && slides.length > 0) {
		controller.maxPreviousClicks();
		previousButton.addEventListener(
			"click", function(e) {
				controller.previous();
			}, false);

		nextButton.addEventListener(
			"click", function(e) {
				controller.next();
			}, false);

	}




})();