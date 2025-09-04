{

    "use strict";

    const SEARCH_RESET = "search-reset";
    const SEARCH_RESET_ACTIVE = `${SEARCH_RESET}-active`;
    const CLICK = "click";
    const EMPTY = "";

    const resetElement = document.querySelector(`.${SEARCH_RESET}`);

    // Set up listeners and logic only if the reset element exists.
    // case 131334 javascript error on discovery login page
    if (resetElement) {

        /**
         * The View: responsible for showing and hiding the reset element.
         */
        const view = {
            hide() {
                resetElement.classList.remove(SEARCH_RESET_ACTIVE);
            },
            show() {
                resetElement.classList.add(SEARCH_RESET_ACTIVE);
            }
        };

        /**
         * The Controller: contains the application logic.
         * Responds to events from the view and other parts of the application.
         */
        const controller = {
            change(event) {
                if (event.newVal === EMPTY) {
                    view.hide();
                } else if (event.oldVal === EMPTY) {
                    view.show();
                }
            },

            reset() {
                // The `fire` method is added by L.addEventTarget
                this.fire("reset");
                L.fire("tracker:trackableEvent", {
                    category: "lane:searchFormReset",
                    action: location.pathname,
                });
            },
        };

        // --- Initialization and Event Wiring ---

        // Make the view and controller objects capable of firing/receiving events.
        L.addEventTarget(view);
        L.addEventTarget(controller, {
            prefix: "searchReset"
        });

        // Listen for a click on the DOM element and have the view fire a custom event.
        // This decouples the DOM from the controller.
        resetElement.addEventListener(CLICK, () => view.fire(CLICK));

        // The controller listens for the view's custom 'click' event.
        view.on(CLICK, () => controller.reset());

        // The controller listens for global query changes.
        L.on("search:queryChange", controller.change);
    }
}
