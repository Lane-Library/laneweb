(function() {

    "use strict";

    var PICO_FIELDS = "pico-fields",
        lane = Y.lane,

        view = function(pico) {

            return {
                    hide: function() {
                        lane.deactivate(pico, PICO_FIELDS);
                    },
                    show: function() {
                        lane.activate(pico, PICO_FIELDS);
                    }
                };

        }(document.querySelector("." + PICO_FIELDS)),

        controller = function(view) {

            return {
                activeChange: function(event) {
                    if (!event.active) {
                        view.hide();
                        controller.fire("change", {active: false});
                    }
                },
                tabChange: function(event) {
                    if (event.newVal.source !== "clinical-all") {
                        view.hide();
                        controller.fire("change", {active: false});
                    }
                },
                toggleChange: function(event) {
                    if (event.active) {
                        view.show();
                        controller.fire("change", {active: true});
                    } else {
                        view.hide();
                        controller.fire("change", {active: false});
                    }
                }
            };

        }(view);

        Y.augment(controller, Y.EventTarget, false, null, {
            prefix: "picoFields",
            emitFacade: true
        });

        controller.addTarget(lane);

        lane.on("search:activeChange", controller.activeChange);
        lane.on("searchTabs:change", controller.tabChange);
        lane.on("picoToggle:change", controller.toggleChange);

})();