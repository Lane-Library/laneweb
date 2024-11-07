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
                    let help = event.option.help;
                    view.update(help);
                }
            };

        }();

    L.on("searchDropdown:change", controller.change);

})();
