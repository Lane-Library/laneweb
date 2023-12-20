YUI({ fetchCSS: false }).use("test", "test-console", function(Y) {

	"use strict";

	let dropdown_label = document.querySelector('.search-form .general-dropdown-trigger span');


	Y.Test.Runner.add(new Y.Test.Case({

		name: "Search Dropdown TestCase",

		"test dropdown  click activates": function() {
			let dropdown = document.querySelector("#main-search"),
			event = document.createEvent("UIEvent");
			event.initEvent("change", true, false);
			Y.Assert.areEqual("All", dropdown_label.innerHTML);
			dropdown[2].selected = 'selected';
			dropdown.dispatchEvent(event);
			
			Y.Assert.areEqual("Bio-Images", dropdown_label.innerHTML);
		},

		"test dropdown select bubbles": function() {
			let event = document.createEvent("UIEvent");
			event.initEvent("change", true, false);
			let dropdown = document.querySelector("#main-search"),
			lastSource = dropdown[dropdown.selectedIndex].value;
			let newSource, oldSource;
			L.once("searchDropdown:change", function(event) {
				newSource = event.newVal.source;
				oldSource = event.oldVal.source;
			});
			dropdown[0].selected = 'selected';
			dropdown.dispatchEvent(event);
			Y.Assert.areEqual(oldSource, lastSource);
			Y.Assert.areEqual(dropdown[dropdown.selectedIndex].value, newSource);
			

		},


		"test tracking": function() {
			let dropdown = document.querySelector("#main-search"),
				lastSource = dropdown[dropdown.selectedIndex].value,
				trackEvent,
				handler = L.on("tracker:trackableEvent", function(e) {
					trackEvent = e;
				});
			let event = document.createEvent("UIEvent");
			event.initEvent("change", true, false);
			dropdown[2].selected = 'selected';
			dropdown.dispatchEvent(event);
			Y.Assert.areEqual("tracker:trackableEvent", trackEvent.type);
			Y.Assert.areEqual("lane:searchDropdownSelection", trackEvent.category);
			Y.Assert.areEqual(dropdown[dropdown.selectedIndex].value, trackEvent.action);
			Y.Assert.areEqual("from all-all to images-all", trackEvent.label);
			handler.detach();
		}



	}));

	new Y.Test.Console().render();


	Y.Test.Runner.masterSuite.name = "search-dropdown-test.js";
	Y.Test.Runner.run();

});
