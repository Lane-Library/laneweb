(function() {

	"use strict";


	if (document.querySelectorAll(".nav-menu") !== undefined) {

		

		document.querySelectorAll(".nav-menu").forEach(function(node) {
			node.addEventListener("click", function(event) {
				var smallmedia = window.matchMedia("(min-width: 1100px)"),
					nav = event.currentTarget, navContent;
				if (!smallmedia.matches) {
					navContent = nav.querySelector('.dropdown-content');
					nav.classList.toggle('nav-menu-active-on-click');
					navContent.classList.toggle('dropdown-content-on-click');
				}
			});
		});
			
			
		document.querySelector(".menu-overlay").addEventListener(
			"click", function(e) {
				window.location.hash = "#";				
			}, false);

	}

})();


