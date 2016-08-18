(function() {

    "use strict";

    var lane = Y.lane,

        helpNode = document.querySelector(".search-help"),

        view = function(helpNode) {

            return {
                    update: function(href) {
                        helpNode.href = window.model["base-path"] + href;
                    }
                };

        }(helpNode),

        controller = function(view) {

            return {
                change: function(event) {
                    var newVal = event.newVal;
                    view.update(newVal[newVal.source].help);
                }
            };

        }(view);

    lane.on("searchTabs:change", controller.change);

})();