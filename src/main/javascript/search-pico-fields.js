(function () {

    "use strict";

    let PICO_FIELDS = "pico-fields",
        PICO_FIELDS_ACTIVE = PICO_FIELDS + "-active",

        picoView = function (pico) {

            return {
                hide: function () {
                    pico.classList.remove(PICO_FIELDS_ACTIVE);
                },
                show: function () {
                    pico.classList.add(PICO_FIELDS_ACTIVE);
                }
            };

        }(document.querySelector("." + PICO_FIELDS)),

        controller = function (view) {

            return {
                activeChange: function (event) {
                    if (!event.active) {
                        view.hide();
                        controller.fire("change", { active: false });
                    }
                },
                tabChange: function (event) {
                    if (event.newVal.source !== "clinical-all") {
                        view.hide();
                        controller.fire("change", { active: false });
                    }
                },
                toggleChange: function (event) {
                    if (event.active) {
                        view.show();
                        controller.fire("change", { active: true });
                    } else {
                        view.hide();
                        controller.fire("change", { active: false });
                    }
                }
            };

        }(picoView);


    L.addEventTarget(controller, {
        prefix: "picoFields",
        emitFacade: true
    });


    L.on("search:activeChange", controller.activeChange);
    L.on("searchDropdown:change", controller.tabChange);
    L.on("picoToggle:change", controller.toggleChange);

    controller.addTarget(L);

})();
