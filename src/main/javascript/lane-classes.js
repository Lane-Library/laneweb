(function() {

	"use strict";

	var Model = L.Model, SEATS_LEFT = "Seats left: ",
		url = Model.get(Model.BASE_PATH) || "" + "/classes/all-open-seat-by-category.xml";

	if (document.querySelectorAll(".remaining-seats").length > 0) {
		L.io(url, {
			on : {
				success : succesHandle,
			}
		})
	}

	function succesHandle(id, o) {
		var events = o.responseXML, 
		classId, remainingSeat, i, link, href, seats, 
		classEvents = document.querySelectorAll(".remaining-seats");
		for (i = 0; i < classEvents.length; i++) {
			classId = classEvents[i].id;
			if (classId){
				remainingSeat = events.querySelector("event[id^='"+classId+"']");
				if(remainingSeat){
					seats = remainingSeat.textContent; 
					if ( seats != 0) {
						classEvents[i].innerText = SEATS_LEFT + seats;
					} else { //Waiting LIST 
						link = document.getElementById("class-registration-button-" + classId);
						href = link.getAttribute("data-help");
						link.setAttribute("href" , href);
						link.getElementsByTagName("span")[0].innerText = "Waiting List";
						link.classList.add("waitlist");
					}
				}
			}
		}
	}

	
	
})()	;