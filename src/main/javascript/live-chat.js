(function() {

    "use strict";
	
	var livechatbutton = document.querySelector("#live-chat-sticky-button"); 

    if (livechatbutton) {
		document.addEventListener("scroll", function() {
			livechatbutton.style.display ='block';
		})
	}

})();
