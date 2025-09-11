(() => {

    "use strict";

    // exit early if not on search results page
    const searchResultsContainer = document.querySelector("#searchResults");
    if (!searchResultsContainer) {
        return;
    }

    const LABELS = {
        eresource: "Read Full Description",
        searchContent: "Abstract"
    };
    const ICONS = {
        up: "fa-regular fa-angle-up",
        down: "fa-regular fa-angle-down"
    };

    /**
     * Updates a trigger's inner HTML based on its type and whether it's active.
     * @param {HTMLElement} trigger - The .descriptionTrigger element.
     * @param {boolean} isActive - Whether the parent <li> is active.
     */
    const updateTriggerState = (trigger, isActive) => {
        const isEresource = trigger.classList.contains("eresource");

        // Determine the correct label and icon based on state.
        const label = isEresource ? LABELS.eresource : LABELS.searchContent;
        const iconClass = isActive ? ICONS.up : ICONS.down;

        trigger.innerHTML = `<a href="#">${label}<i class="${iconClass}"></i></a>`;
    };

    /**
     * Sets the initial state for all description toggles on the page.
     */
    const initializeDescriptionToggles = () => {
        searchResultsContainer.querySelectorAll(".descriptionTrigger").forEach(trigger => {
            // triggers start in the inactive (down) state
            updateTriggerState(trigger, false);
        });
    };

    /**
     * Handles the click event on a description trigger.
     * @param {HTMLElement} trigger - The clicked .descriptionTrigger element.
     * @param {Event} event - The original click event.
     */
    const handleClick = (trigger, event) => {
        event.preventDefault();

        const ancestor = trigger.closest("li");
        const wasActive = ancestor.classList.contains("active");

        // toggle active on the parent `li`
        ancestor.classList.toggle("active");

        // update the trigger's HTML to reflect the new state (opposite of the old state)
        updateTriggerState(trigger, !wasActive);

        // fire the tracking event
        L.fire("tracker:trackableEvent", {
            category: "lane:descriptionTrigger",
            action: trigger.textContent,
            label: ancestor.querySelector('.primaryLink')?.textContent
        });
    };

    // --- Event Listener Setup ---

    // Use event delegation on the main container
    searchResultsContainer.addEventListener("click", (event) => {
        // find the closest trigger element that was clicked
        const trigger = event.target.closest(".descriptionTrigger");
        if (trigger) {
            handleClick(trigger, event);
        }
    });

    // Reinitialize when new content is loaded into the page
    L.on("lane:new-content", initializeDescriptionToggles);

    // Run the initialization for the initial page load
    initializeDescriptionToggles();

})();
