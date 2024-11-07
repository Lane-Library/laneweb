if (document.querySelector(".search-form")) {

	(function () {

		"use strict";

		let CHANGE = "change",
			SEARCH_DROPDOWN = "main-search",
			dropdown = document.querySelector("#" + SEARCH_DROPDOWN),
			options = dropdown.querySelectorAll("option"),
			dropdown_label = document.querySelector('.search-form .general-dropdown-trigger span'),
			model = function (source) {

				let m = {
					source: source
				};

				options.forEach(function (option) {
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

			view = function () {
				let v = {
					change: function (label) {
						dropdown_label.innerHTML = label;
					},
					click: function () {
						let selectedValue = dropdown[dropdown.selectedIndex].value;
						view.fire(CHANGE, { source: selectedValue });
					}
				}
				dropdown.addEventListener(CHANGE, v.click);
				return v;
			}(),


			controller = function () {

				return {
					update: function (event) {
						let prop,
							previousVal = model.source,
							text = model[event.source].text;

						view.change(text);
						L.fire("tracker:trackableEvent", {
							category: "lane:searchDropdownSelection",
							action: event.source,
							label: "from " + previousVal + " to " + event.source
						});
						controller.fire(CHANGE, { newVal: event.source, option: model[event.source] });
					}
				};

			}();

		L.addEventTarget(view);

		L.addEventTarget(controller, {
			prefix: "searchDropdown"
		});

		controller.addTarget(L);
		view.on(CHANGE, controller.update)



	})();

}
