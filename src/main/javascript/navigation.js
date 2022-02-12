(function() {

	"use strict";

	window.model = window.model || {};



	var smallmedia = window.matchMedia("(min-width: 1100px)");

	if (document.querySelectorAll(".nav-menu") !== undefined) {


		var resetNav = function() {
			smallmedia = window.matchMedia("(min-width: 1100px)");
			document.querySelectorAll(".nav-menu").forEach(function(node) {
					node.classList.remove('nav-menu-active');						
					if(node.querySelector(".nav-menu-content") != null){
						node.querySelector(".nav-menu-content").style.display = null;
					}										
				});
		}


		document.querySelectorAll(".nav-menu").forEach(function(node) {
			node.addEventListener("click", function(event) {
				if (!smallmedia.matches) {
					var nav = event.currentTarget,
						navContent = nav.querySelector(".nav-menu-content"),
						display = navContent.style.display;
					if (display == '') {
						nav.classList.add('nav-menu-active');
						navContent.style.display = "block";
					} else {
						navContent.style.display = null;
						nav.classList.remove('nav-menu-active');
					}
				}

			});
		});
		
		window.addEventListener('resize', resetNav);
	}






})();


