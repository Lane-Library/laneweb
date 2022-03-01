(function() {

	"use strict";

	if (document.querySelectorAll(".slide") !== undefined && document.querySelectorAll(".slide").length > 0) {


		var container = document.getElementById("slide-show"),
			previousButton = document.getElementById("previous-slide"),
			nextButton = document.getElementById("next-slide"),
			padding = 24,


			model = function(slides) {

				var m = {
					slides: slides,
					index: 0,
					imageDisplayedNumber: 1
				}
				return m;

			}(document.querySelectorAll(".slide")),

			view = function() {
				previousButton.className = "disable";
				return {
					showSlide: function() {
						model.slides[model.index].className = "slide";
					},
					hideSlide: function() {
						model.slides[model.index].className = "slide desactive-next";
					},
					updateButton: function() {
						if (model.index + model.imageDisplayedNumber >= model.slides.length) {
							nextButton.className = "disable";
						} else {
							nextButton.className = "";
						}
						if (model.index <= 0) {
							previousButton.className = "disable";
						} else {
							previousButton.className = "";
						}
					}
				}
			}(),

			controller = function() {
				return {
					next: function() {
						for (var i = model.imageDisplayedNumber; i > 0 && model.index > 0; i--) {
							model.index--;
							view.showSlide();
						}
						view.updateButton();
					},
					previous: function() {
						for (var i = 0; i < model.imageDisplayedNumber && model.index + model.imageDisplayedNumber < model.slides.length; i++) {
							view.hideSlide();
							model.index++;
						}
						view.updateButton();
					},
					calculateImageDisplayed: function() {
						var imageDiv = model.slides[model.slides.length - 1],
							t = parseFloat(container.offsetWidth + padding) / (parseFloat(imageDiv.offsetWidth + padding));
						model.imageDisplayedNumber = parseInt(t);
					}
				}
			}();




		window.addEventListener("load", function() {
			view.updateButton();
			controller.calculateImageDisplayed();
		}, false);


		nextButton.addEventListener(
			"click", function(e) {
				controller.previous();
			}, false);

		previousButton.addEventListener(
			"click", function(e) {
				controller.next();
			}, false);

	}




})();