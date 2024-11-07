if (document.querySelector(".pico-toggle")) {

    (function () {

        "use strict";

        let ACTIVE = "-active",
            PICO_ON = "pico-on",
            PICO_ON_ACTIVE = PICO_ON + ACTIVE,
            PICO_OFF = "pico-off",
            PICO_OFF_ACTIVE = PICO_OFF + ACTIVE,
            PICO_TOGGLE = "search-pico",
            PICO_TOGGLE_ACTIVE = PICO_TOGGLE + ACTIVE,

            picoView = function (toggle) {

                let on = toggle.querySelector("." + PICO_ON),
                    off = toggle.querySelector("." + PICO_OFF),
                    v = {
                        activate: function () {
                            // don't activate if already active
                            if (!toggle.classList.contains(PICO_TOGGLE_ACTIVE)) {
                                toggle.classList.add(PICO_TOGGLE_ACTIVE);
                                on.classList.add(PICO_ON_ACTIVE);
                                off.classList.remove(PICO_OFF_ACTIVE);
                            }
                        },
                        deactivate: function () {
                            toggle.classList.remove(PICO_TOGGLE_ACTIVE);
                            on.classList.remove(PICO_ON_ACTIVE);
                            off.classList.remove(PICO_OFF_ACTIVE);
                        },
                        picoOn: function () {
                            on.classList.remove(PICO_ON_ACTIVE);
                            off.classList.add(PICO_OFF_ACTIVE);
                            v.fire("on");
                        },
                        picoOff: function () {
                            off.classList.remove(PICO_OFF_ACTIVE);
                            on.classList.add(PICO_ON_ACTIVE);
                            v.fire("off");
                        },
                        reset: function () {
                            on.classList.add(PICO_ON_ACTIVE);
                            off.classList.remove(PICO_OFF_ACTIVE);
                        }
                    };

                on.addEventListener("click", v.picoOn);
                off.addEventListener("click", v.picoOff);


                return v;

            }(document.querySelector("." + PICO_TOGGLE));

        (function (view) {
            let controller = {
                on: function () {
                    controller.fire("change", { active: true });
                },
                off: function () {
                    controller.fire("change", { active: false });
                },
                activeChange: function (event) {
                    if (!event.active) {
                        view.reset();
                    }
                },
                tabChange: function (event) {
                    if (event.newVal === "clinical-all") {
                        view.activate();
                    } else {
                        view.deactivate();
                    }
                }
            };

            L.addEventTarget(view);
            view.on("on", controller.on);

            view.on("off", controller.off);

            L.on("search:activeChange", controller.activeChange);

            L.on("searchDropdown:change", controller.tabChange);

            L.addEventTarget(controller, {
                prefix: "picoToggle"
            });
            controller.addTarget(L);

        })(picoView);

    })();

}
