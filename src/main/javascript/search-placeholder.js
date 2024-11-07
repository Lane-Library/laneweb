if (document.querySelector(".search-form")) {

    (function () {

        "use strict";

        let inputNode = document.querySelector(".search-form input[name=q]"),


            view = function () {

                return {
                    setPlaceholder: function (placeholder) {
                        inputNode.placeholder = placeholder;
                    }
                };

            }(),

            controller = function () {

                return {
                    tabChange: function (event) {
                        let placeholder = event.option.placeholder;
                        view.setPlaceholder(placeholder);
                    }
                };
            }();

        L.on("searchDropdown:change", controller.tabChange);


    })();

}
