if (document.querySelector(".pico-toggle"))  {

(function() {

    "use strict";

    var PICO_ON = "pico-on",
        PICO_OFF = "pico-off",
        PICO_TOGGLE = "pico-toggle",

        view = function(toggle) {

            var on = toggle.querySelector("." + PICO_ON),
                off = toggle.querySelector("." + PICO_OFF),
                v = {
                    activate: function() {
                        L.activate(toggle, PICO_TOGGLE);
                        L.activate(on, PICO_ON);
                        L.deactivate(off, PICO_OFF);
                    },
                    deactivate: function() {
                        L.deactivate(toggle, PICO_TOGGLE);
                        L.deactivate(on, PICO_ON);
                        L.deactivate(off, PICO_OFF);
                    },
                    picoOn: function() {
                        L.deactivate(on, PICO_ON);
                        L.activate(off, PICO_OFF);
                        v.fire("on");
                    },
                    picoOff: function() {
                        L.deactivate(off, PICO_OFF);
                        L.activate(on, PICO_ON);
                        v.fire("off");
                    },
                    reset: function() {
                        L.activate(on, PICO_ON);
                        L.deactivate(off, PICO_OFF);
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
                    } else if (document.location.pathname.indexOf('portals/peds.html') > 0) {
                        view.picoOn();
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

            L.on("search:activeChange", controller.activeChange);

            L.on("searchTabs:change", controller.tabChange);

            Y.augment(controller, Y.EventTarget, false, null, {
                prefix: "picoToggle",
                emitFacade: true
            });
            controller.addTarget(L);

        })(view);

})();

}
