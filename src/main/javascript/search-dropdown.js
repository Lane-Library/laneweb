if (document.querySelector("#main-search")) {

    (() => {
        "use strict";

        const searchDropdown = document.querySelector("#main-search");
        const options = searchDropdown.querySelectorAll("option");
        const dropdownLabel = document.querySelector(".search-form .general-dropdown-trigger span");

        // --- Static Data Model ---
        // Create a lookup object once from all <option> elements using their data attributes.
        const searchOptionsData = Array.from(options).reduce((data, option) => {
            data[option.value] = {
                placeholder: option.dataset.placeholder,
                help: option.dataset.help,
                tip: option.title,
                text: option.text,
            };
            return data;
        }, {});

        // --- State Management ---
        // A function to create the full model object needed by the event listeners.
        // Adds the current source to the static options data above.
        const createModel = (source) => ({
            source,
            ...searchOptionsData
        });

        // Initialize the state with the dropdown's current value.
        let currentModel = createModel(searchDropdown.value);

        // --- Event Handling ---
        const handleDropdownChange = () => {
            const newSource = searchDropdown.value;
            const newModel = createModel(newSource);

            // Update the UI
            dropdownLabel.textContent = newModel[newSource].text;

            L.fire("tracker:trackableEvent", {
                category: "lane:searchDropdownSelection",
                action: newSource,
                label: `from ${currentModel.source} to ${newSource}`
            });

            L.fire("searchDropdown:change", { newVal: newModel, oldVal: currentModel });

            // Update the state for the next change event.
            currentModel = newModel;
        };

        searchDropdown.addEventListener("change", handleDropdownChange);

    })();
}