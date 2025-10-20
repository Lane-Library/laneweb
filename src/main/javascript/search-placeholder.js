{

    "use strict";

    const dropdown = document.querySelector("#main-search");
    const inputNode = document.querySelector(".search-form input[name=q]");

    if (dropdown && inputNode) {

        const model = {
            defaultPlaceholder: inputNode.dataset.placeholder,
            currentPlaceholder: dropdown.options[dropdown.selectedIndex].dataset.placeholder
        }

        const view = {
            /**
             * Sets the placeholder text on the search input field.
             * @param {string} placeholder - The text to display.
             */
            setPlaceholder(placeholder) {
                inputNode.placeholder = placeholder;
            }
        }

        const controller = {
            /**
             * Handles when the search UI changes (e.g., on focus/blur).
             * @param {object} event - The event object, containing an 'active' boolean.
             */
            activeChange(event) {
                view.setPlaceholder(event.active ? model.currentPlaceholder : model.defaultPlaceholder);
            },
            /**
             * Handles when the user selects a new search source from the dropdown.
             * @param {object} event - The event object from the L.on listener.
             */
            tabChange(event) {
                // Use destructuring with a computed property to cleanly extract data.
                // For an event.newVal like { source: 'catalog', catalog: { placeholder: '...' } }:
                // - `source` becomes 'catalog'
                // - `sourceData` becomes the object at the 'catalog' key
                const { source, [source]: sourceData } = event.newVal;
                model.currentPlaceholder = sourceData.placeholder;
                view.setPlaceholder(model.currentPlaceholder);
            }

        };

        // --- Event Wiring ---
        L.on("searchDropdown:change", controller.tabChange);
        L.on("search:activeChange", controller.activeChange);
    }

}
