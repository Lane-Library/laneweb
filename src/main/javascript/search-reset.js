(function() {

    "use strict";

    let SEARCH_RESET = "search-reset",
        SEARCH_RESET_ACTIVE = SEARCH_RESET + "-active",
        CLICK = "click",
        EMPTY = "",

        view = function(reset) {

            let v = {
                    hide: function() {
                        reset.classList.remove(SEARCH_RESET_ACTIVE);
                    },
                    show: function() {
                        reset.classList.add(SEARCH_RESET_ACTIVE);
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
