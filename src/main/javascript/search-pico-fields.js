(function() {

    "use strict";

    var PICO_FIELDS = "pico-fields",
        PICO_FIELDS_ACTIVE = PICO_FIELDS + "-active",

        view = function(pico) {

            return {
                    hide: function() {
                        pico.classList.remove(PICO_FIELDS_ACTIVE);
                    },
                    show: function() {
                        pico.classList.add(PICO_FIELDS_ACTIVE);
                    }
                };

        }(document.querySelector("." + PICO_FIELDS)),

        controller = function(view) {

            return {
                activeChange: function(event) {
                    if (!event.active) {
                        view.hide();
                        controller.fire("change", {active: false});
                    } else if (document.location.pathname.indexOf('portals/peds.html') > 0) {
                        view.show();
                        controller.fire('change', {active: true});
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

        L.addEventTarget(controller, {
            prefix: "picoFields",
            emitFacade: true
        });

        controller.addTarget(L);

        L.on("search:activeChange", controller.activeChange);
        L.on("searchTabs:change", controller.tabChange);
        L.on("picoToggle:change", controller.toggleChange);

})();
