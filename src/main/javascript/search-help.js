(function() {

    "use strict";

    var helpNode = document.querySelector(".search-help"),

        view = function() {

            return {
                    update: function(href) {
                        helpNode.href = window.model["base-path"] + href;
                    }
                };

        }(),

        controller = function() {

            return {
                change: function(event) {
                    var newVal = event.newVal;
                    view.update(newVal[newVal.source].help);
                }
            };

        }();

    L.on("searchTabs:change", controller.change);

})();
