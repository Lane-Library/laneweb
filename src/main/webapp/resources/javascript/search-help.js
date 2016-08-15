(function() {

    "use strict";

    var lane = Y.lane,

        helpNode = document.querySelector(".search-help"),

        model = function(href) {
            return {
                href: href
            };
        }(helpNode.href),

        view = function(helpNode) {

            return {
                    update: function(href) {
                        helpNode.href = window.model["base-path"] + href;
                    }
                };

        }(helpNode),

        controller = function(model, view) {

            return {
                change: function(event) {
                    var newVal = event.newVal;
                    model.href = newVal[newVal.source].help;
                    view.update(model.href);
                }
            };

        }(model, view);

        lane.on("searchTabs:change", controller.change);

})();