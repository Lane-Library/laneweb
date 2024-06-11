if (document.querySelector(".search-form")) {

	(function() {

		"use strict";

		let CHANGE = "change",
			SEARCH_DROPDOWN = "main-search",
			dropdown = document.querySelector("#" + SEARCH_DROPDOWN),
			options = dropdown.querySelectorAll("option"),
			dropdown_label = document.querySelector('.search-form .general-dropdown-trigger span'),
			model = function(source) {

				let m = {
					source: source
				};

				options.forEach(function(option) {
					m[option.value] = {
						placeholder: option.dataset.placeholder,
						source: option.value,
						help: option.dataset.help,
						tip: option.title,
						text: option.text
					};
				});

				return m;

			}(dropdown[dropdown.selectedIndex].value),

			view = function() {
				let v = {
					change: function(label) {
						dropdown_label.innerHTML = label;
					},
					click: function() {
						view.fire(CHANGE, dropdown[dropdown.selectedIndex].value);
					}
				}

				L.addEventTarget(v);
				dropdown.addEventListener(CHANGE, v.click);
				return v;
			}(),


			controller = function() {

				return {
					update: function(source) {
						let prop, newVal = {};
						for (prop in model) {
							newVal[prop] = model[prop];
						}
						newVal.source = source;
						view.change(newVal[source].text);
						L.fire("tracker:trackableEvent", {
							category: "lane:searchDropdownSelection",
							action: source,
							label: "from " + model.source + " to " + source
						});
						this.fire(CHANGE, { newVal: newVal, oldVal: model });
						model = newVal;
					}
				};

			}();

		L.addEventTarget(controller, {
			prefix: "searchDropdown",
			emitFacade: true
		});

		controller.addTarget(L);

		view.on(CHANGE, controller.update, controller);

	})();

}
