
(function() {

	"use strict";


	var dropdown = document.querySelector(".search-dropdown");

	if (dropdown) {
		dropdown.addEventListener("change", function(e) {
			var el = e.target,
				placeholdervalue = el.options[el.selectedIndex].dataset.placeholder;
			document.querySelector(".search-form input[name=q]").placeholder = placeholdervalue;
		});
	}


})();

