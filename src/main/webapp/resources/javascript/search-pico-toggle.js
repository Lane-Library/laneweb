(function() {

    "use strict";

    var PICO_ON = "pico-on",
        PICO_OFF = "pico-off",
        PICO_TOGGLE = "pico-toggle",

        lane = Y.lane,

        view = function(toggle) {

            var on = toggle.querySelector("." + PICO_ON),
                off = toggle.querySelector("." + PICO_OFF),
                v = {
                    activate: function() {
                        lane.activate(toggle, PICO_TOGGLE);
                        lane.activate(on, PICO_ON);
                        lane.deactivate(off, PICO_OFF);
                    },
                    deactivate: function() {
                        lane.deactivate(toggle, PICO_TOGGLE);
                        lane.deactivate(on, PICO_ON);
                        lane.deactivate(off, PICO_OFF);
                    },
                    picoOn: function() {
                        lane.deactivate(on, PICO_ON);
                        lane.activate(off, PICO_OFF);
                        v.fire("on");
                    },
                    picoOff: function() {
                        lane.deactivate(off, PICO_OFF);
                        lane.activate(on, PICO_ON);
                        v.fire("off");
                    },
                    reset: function() {
                        lane.activate(on, PICO_ON);
                        lane.deactivate(off, PICO_OFF);
                    }
                };

            on.addEventListener("click", v.picoOn);
            off.addEventListener("click", v.picoOff);
            Y.augment(v, Y.EventTarget);

            return v;

        }(document.querySelector("." + PICO_TOGGLE));

        (function(view) {
            var controller = {
                on: function() {
                    controller.fire("change", {active:true});
                },
                off: function() {
                    controller.fire("change", {active:false});
                },
                activeChange: function(event) {
                    if (!event.active) {
                        view.reset();
                    }
                },
                tabChange: function(event) {
                    if (event.newVal.source === "clinical-all") {
                        view.activate();
                    } else {
                        view.deactivate();
                    }
                }
            };

            view.on("on", controller.on);

            view.on("off", controller.off);

            lane.on("search:activeChange", controller.activeChange);

            lane.on("searchTabs:change", controller.tabChange);

            Y.augment(controller, Y.EventTarget, false, null, {
                prefix: "picoToggle",
                emitFacade: true
            });
            controller.addTarget(lane);

        })(view);

})();