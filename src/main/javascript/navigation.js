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



		document.querySelector("#nav-toggle-on").addEventListener(
			"click", function(e) {
				document.querySelector("section.content").style.display = "none";
				document.querySelector("header:first-of-type").style.display = "none";
			}, false);

		document.querySelector("#nav-toggle-off").addEventListener(
			"click", function(e) {
				document.querySelector("section.content").style.display = "initial";
				document.querySelector("header:first-of-type").style.display = "block";
			}, false);
	}

})();


