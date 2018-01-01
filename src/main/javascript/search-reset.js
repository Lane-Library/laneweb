(function() {

    "use strict";

    var SEARCH_RESET = "search-reset",
        CLICK = "click",
        EMPTY = "",

        view = function(reset) {

            var v = {
                    hide: function() {
                        L.deactivate(reset, SEARCH_RESET);
                    },
                    show: function() {
                        L.activate(reset, SEARCH_RESET);
                    },
                    click: function() {
                        v.fire(CLICK);
                    }
                };

            L.addEventTarget(v);

            // case 131334 javascript error on discovery login page
            if (reset) {
                reset.addEventListener(CLICK, v.click);
            }

            return v;

        }(document.querySelector("." + SEARCH_RESET)),

        controller = function() {

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
                    L.fire("tracker:trackableEvent", {
                        category: "lane:searchFormReset",
                        action: location.pathname
                    });
                }
            };
        }();

    L.addEventTarget(controller, {
        prefix: "searchReset",
        emitFacade: true
    });

    controller.addTarget(L);

    L.on("search:queryChange", controller.change);

    view.on(CLICK, controller.reset);

})();
