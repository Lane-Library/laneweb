
(function() {

	"use strict";


	var dropdown = document.querySelector(".search-dropdown");

	if (dropdown) {
		dropdown.addEventListener("change", function(e) {
			var el = e.target,
				selectedText = el.options[el.selectedIndex].text;
			document.querySelector('.search-form .general-dropdown-trigger span').innerHTML = selectedText;
			
			
			if(el.value == 'clinical-all'){
				alert(el.value);
				document.querySelector('.search-info').classList.add("search-info-active");				
			}else{
				document.querySelector('.search-info').classList.remove("search-info-active");
			}
			
		});
	}


})();

