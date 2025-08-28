(() => {
    "use strict";

    const picoInputs = document.querySelectorAll(".pico-fields input");

    // guardian to halt processing if no pico inputs are found
    if (!picoInputs.length) {
        return;
    }

    const sourceBase = '/apps/suggest/getSuggestionList?q={query}&l=mesh';

    /**
     * Represents a single PICO search field with suggestion capabilities.
     */
    class PicoField {
        constructor(input, fieldsEventBus) {
            this.input = input;

            // Set up search suggestions for this field.
            const suggest = new L.Suggest(this.input, sourceBase);
            L.addEventTarget(suggest);

            // When a suggestion is selected or text is typed, notify the event bus.
            const onInput = () => fieldsEventBus.fire("input");
            suggest.on("suggest:select", onInput);
            this.input.addEventListener("input", onInput);
        }

        /**
         * Enables or disables the input field.
         * @param {boolean} isEnabled - True to enable, false to disable.
         */
        enable(isEnabled) {
            this.input.disabled = !isEnabled;
        }

        /**
         * @returns {string} The current value of the input field.
         */
        getValue() {
            return this.input.value;
        }

        /**
         * Clears the input field's value.
         */
        reset() {
            this.input.value = "";
        }
    }

    // This array will hold all PicoField instances and also act as an event bus.
    const fields = [];
    L.addEventTarget(fields);

    // --- Initialization ---

    // Create a PicoField instance for each input element.
    picoInputs.forEach(input => {
        fields.push(new PicoField(input, fields));
    });

    // --- Event Listeners ---

    // Listen for global events to enable/disable or reset all fields.
    L.on("picoFields:change", event => {
        fields.forEach(field => field.enable(event.active));
    });

    L.on("searchReset:reset", () => {
        fields.forEach(field => field.reset());
    });

    // Listen for the custom "input" event fired by any PicoField.
    fields.on("input", () => {
        // Get all non-empty values from fields
        const values = fields
            .map(field => field.getValue())
            .filter(Boolean);

        let query = "";
        if (values.length === 1) {
            // If only one value, use it directly
            query = values[0];
        } else if (values.length > 1) {
            // If multiple values, wrap each in () and join with " AND "
            query = values.map(value => `(${value})`).join(" AND ");
        }

        // Update the main search query input
        L.search.setQuery(query);
    });

})();