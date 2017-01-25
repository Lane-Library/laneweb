(function() {

    "use strict";

    var SEARCH_RESET = "search-reset",
        CLICK = "click",
        EMPTY = "",

        lane = Y.lane,

        view = function(reset) {

            var v = {
                    hide: function() {
                        lane.deactivate(reset, SEARCH_RESET);
                    },
                    show: function() {
                        lane.activate(reset, SEARCH_RESET);
                    },
                    click: function() {
                        v.fire(CLICK);
                    }
                };

            Y.augment(v, Y.EventTarget);

            if (reset) {
                reset.addEventListener(CLICK, v.click);
            }

            return v;

        }(document.querySelector("." + SEARCH_RESET)),

        controller = function(view) {

            return {
                change: function(event) {
                    if (event.newVal === EMPTY) {
                        view.hide();
                    } else if (event.oldVal === EMPTY) {
                        view.show();
                    }
                },
                reset: function() {
                    controller.fire("reset");
                    lane.fire("tracker:trackableEvent", {
                        category: "lane:searchFormReset",
                        action: location.pathname
                    });
                }
            };
        }(view);

    Y.augment(controller, Y.EventTarget, false, null, {
        prefix: "searchReset",
        emitFacade: true
    });

    controller.addTarget(lane);

    lane.on("search:queryChange", controller.change);

    view.on(CLICK, controller.reset);

})();