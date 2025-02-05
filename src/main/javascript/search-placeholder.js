if (document.querySelector(".search-form")) {

    (function () {
        (function () {

            "use strict";
            "use strict";

            let PLACEHOLDER = "placeholder", SEARCH_DROPDOWN = 'main-search',
                let PLACEHOLDER = "placeholder", SEARCH_DROPDOWN = 'main-search',

                inputNode = document.querySelector(".search-form input[name=q]"),
                dropdown = document.querySelector("#" + SEARCH_DROPDOWN),
                inputNode = document.querySelector(".search-form input[name=q]"),
                dropdown = document.querySelector("#" + SEARCH_DROPDOWN),

                model = function (def, current) {
                    return {
                        def: def,
                        current: current
                    };
                }(inputNode.dataset[PLACEHOLDER],
                    dropdown.options[dropdown.selectedIndex].dataset.placeholder),
                model = function (def, current) {
                    return {
                        def: def,
                        current: current
                    };
                }(inputNode.dataset[PLACEHOLDER],
                    dropdown.options[dropdown.selectedIndex].dataset.placeholder),

                view = function () {
                    view = function () {

                        return {
                            setPlaceholder: function (placeholder) {
                                return {
                                    setPlaceholder: function (placeholder) {
                                        inputNode.placeholder = placeholder;
                                    }
                                };

                            }(),
                        }(),

                            controller = function () {
                                controller = function () {

                                    return {
                                        activeChange: function (event) {
                                            view.setPlaceholder(event.active ? model.current : model.def);
                                        },
                                        return {
                                            activeChange: function (event) {
                                                view.setPlaceholder(event.active ? model.current : model.def);
                                            },

                                            tabChange: function (event) {
                                                let newVal = event.newVal;
                                                model.current = newVal[newVal.source].placeholder;
                                                view.setPlaceholder(model.current);
                                            }
                                        };
                                    }();
                                    tabChange: function (event) {
                                        let newVal = event.newVal;
                                        model.current = newVal[newVal.source].placeholder;
                                        view.setPlaceholder(model.current);
                                    }
                                };
                            }();

                        L.on("searchDropdown:change", controller.tabChange);
                        L.on("search:activeChange", controller.activeChange);
                        L.on("searchDropdown:change", controller.tabChange);
                        L.on("search:activeChange", controller.activeChange);


                    }) ();
                })();

        }
