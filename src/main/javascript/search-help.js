(function () {

    "use strict";

    let helpNode = document.querySelector(".search-help a"),

        view = function () {

            return {
                update: function (href) {
                    helpNode.href = window.model["base-path"] + href;
                }
            };

        }(),

        controller = function () {

            return {
                change: function (event) {
                    let newVal = event.newVal;
                    view.update(newVal[newVal.source].help);
                }
            };

        }();

    L.on("searchDropdown:change", controller.change);

})();
