(() => {

    "use strict";

    const PICO_FIELDS = "pico-fields";
    const picoFieldsElement = document.querySelector(`.${PICO_FIELDS}`);

    // guard clause: exit if no pico fields container present
    if (!picoFieldsElement) {
        return;
    }

    const PICO_FIELDS_ACTIVE = `${PICO_FIELDS}-active`;

    const view = {
        hide() {
            picoFieldsElement.classList.remove(PICO_FIELDS_ACTIVE);
        },
        show() {
            picoFieldsElement.classList.add(PICO_FIELDS_ACTIVE);
        }
    };

    const controller = {
        searchDropdownChange(event) {
            // hide the fields if user switches away from clinical-all
            if (event.newVal.source !== "clinical-all") {
                view.hide();
                this.fire("change", { active: false });
            }
        },
        toggleChange(event) {
            // show/hide the fields based on pico toggle state
            if (event.active) {
                view.show();
                this.fire("change", { active: true });
            } else {
                view.hide();
                this.fire("change", { active: false });
            }
        }
    };

    // --- Initialization and Event Wiring ---
    L.addEventTarget(controller, { prefix: "picoFields" });

    // Arrow functions ensure `this` inside the controller methods refers to the `controller` object.
    L.on("searchDropdown:change", event => controller.searchDropdownChange(event));
    L.on("picoToggle:change", event => controller.toggleChange(event));

})();