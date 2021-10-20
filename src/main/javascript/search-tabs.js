
(function() {

	"use strict";


	var dropdown = document.querySelector(".search-dropdown");

	if (dropdown) {
		dropdown.addEventListener("change", function(e) {
			var el = e.target,
				selectedValue = el.options[el.selectedIndex].text;
			document.querySelector('.search-form .general-dropdown-trigger span').innerHTML = selectedValue;
		});
	}


})();

