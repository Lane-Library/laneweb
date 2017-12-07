(function() {

    "use strict";

    var lane = Y.lane,

        helpNode = document.querySelector(".search-help"),

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

    lane.on("searchTabs:change", controller.change);

})();
