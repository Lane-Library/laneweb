// guard clause: exit if no toggle present
if (document.querySelector(".pico-toggle")) {

    (() => {
        "use strict";

        // --- Constants ---
        const ACTIVE = "-active";
        const PICO_TOGGLE = "search-pico";
        const PICO_ON = "pico-on";
        const PICO_OFF = "pico-off";
        const PICO_TOGGLE_ACTIVE = `${PICO_TOGGLE}${ACTIVE}`;
        const PICO_ON_ACTIVE = `${PICO_ON}${ACTIVE}`;
        const PICO_OFF_ACTIVE = `${PICO_OFF}${ACTIVE}`;

        const toggleElement = document.querySelector(`.${PICO_TOGGLE}`);
        if (!toggleElement) return;

        const onButton = toggleElement.querySelector(`.${PICO_ON}`);
        const offButton = toggleElement.querySelector(`.${PICO_OFF}`);
        if (!onButton || !offButton) return;

        const view = {
            activate() {
                if (!toggleElement.classList.contains(PICO_TOGGLE_ACTIVE)) {
                    toggleElement.classList.add(PICO_TOGGLE_ACTIVE);
                    onButton.classList.add(PICO_ON_ACTIVE);
                    offButton.classList.remove(PICO_OFF_ACTIVE);
                }
            },
            deactivate() {
                toggleElement.classList.remove(PICO_TOGGLE_ACTIVE);
                onButton.classList.remove(PICO_ON_ACTIVE);
                offButton.classList.remove(PICO_OFF_ACTIVE);
            },
            reset() {
                onButton.classList.add(PICO_ON_ACTIVE);
                offButton.classList.remove(PICO_OFF_ACTIVE);
            }
        };

        const controller = {
            activeChange(event) {
                if (!event.active) view.reset();
            },
            tabChange(event) {
                if (event.newVal.source === "clinical-all") {
                    view.activate();
                } else {
                    view.deactivate();
                }
            }
        };

        // make the objects event aware
        L.addEventTarget(view);
        L.addEventTarget(controller, { prefix: "picoToggle" });

        // "Post definition" pattern. Attach the special methods that need to call .fire() on the object itself.
        // They use the `view` and `controller` constants from the closure above.
        view.picoOn = () => {
            onButton.classList.remove(PICO_ON_ACTIVE);
            offButton.classList.add(PICO_OFF_ACTIVE);
            view.fire("on");
        };
        view.picoOff = () => {
            offButton.classList.remove(PICO_OFF_ACTIVE);
            onButton.classList.add(PICO_ON_ACTIVE);
            view.fire("off");
        };
        controller.on = () => controller.fire("change", { active: true });
        controller.off = () => controller.fire("change", { active: false });

        // --- Event Wiring ---
        onButton.addEventListener("click", view.picoOn);
        offButton.addEventListener("click", view.picoOff);

        view.on("on", controller.on);
        view.on("off", controller.off);

        L.on("search:activeChange", controller.activeChange);
        L.on("searchDropdown:change", controller.tabChange);

    })();
}
