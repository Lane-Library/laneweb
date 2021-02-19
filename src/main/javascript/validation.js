(function() {

	"use strict";

	document.querySelectorAll('[type="submit"]').forEach(function(node) {
		node.addEventListener("click", function(e) {
			var form = event.target.form;
			form.classList.add("submitted");
		});
	});

})();
