if (document.querySelector(".search-form")) {
    (() => {

        "use strict";

        /**
         * A controller class for the main search form.
         * It encapsulates state, DOM interactions, and event handling for the
         * search query, source, and related UI elements.
         */
        class SearchFormController {

            #query;
            #searching;
            #source;

            constructor(formElement) {
                this.form = formElement;

                // --- Cache DOM elements ---
                this.queryInput = this.form.querySelector("input[name=q]");
                this.sourceInput = this.form.querySelector("input[name=source]");
                this.facetsInput = this.form.querySelector("input[name=facets]");
                this.sortInput = this.form.querySelector("input[name=sort]");

                // --- Initialize event capabilities ---
                L.addEventTarget(this, { prefix: "search" });

                // --- Initialize state from the DOM ---
                this.#query = this.queryInput.value;
                this.#source = this.sourceInput.value;
                this.#searching = false;

                this.#bindEvents();
            }

            /**
             * Bind internal and external event listeners
             */
            #bindEvents() {
                this.form.addEventListener("submit", this.#handleSubmit);
                this.queryInput.addEventListener("focus", this.#handleFocus);
                this.queryInput.addEventListener("input", this.#handleInputChange);

                // listen for custom global events
                L.on("searchDropdown:change", this.#handleSearchDropdownChange);
                L.on("searchReset:reset", this.reset);
            }

            get query() { return this.#query; }

            get searching() { return this.#searching; }

            get source() { return this.#source; }

            /**
             * Sets the search query, updates the input, and fires an event.
             * @param {string} newQuery - The new search term.
             */
            set query(newQuery) {
                if (typeof newQuery !== "string" || newQuery === this.#query) return;

                const oldQuery = this.#query;
                this.#query = newQuery;
                this.queryInput.value = this.#query; // Directly update the view

                this.fire("queryChange", { newVal: this.#query, oldVal: oldQuery });
            }

            /**
             * Sets the search source, updates the input, resets facets, and fires an event.
             * @param {string} newSource - The new search source.
             */
            set source(newSource) {
                if (typeof newSource !== "string" || newSource === this.#source) return;

                const oldSource = this.#source;
                this.#source = newSource;
                this.sourceInput.value = this.#source;

                // Reset facets when the source changes
                if (this.facetsInput.value) {
                    this.facetsInput.value = '';
                    this.facetsInput.disabled = true;
                }

                this.fire("sourceChange", { newVal: this.#source, oldVal: oldSource });
            }

            /**
             * Programmatically trigger a search
             */
            search() {
                if (this.#query) {
                    this.#searching = true;
                    this.fire("search");
                    this.form.submit();
                }
            }

            /**
             * Reset search form inputs
             */
            reset = () => {
                this.query = "";
                if (this.facetsInput) this.facetsInput.disabled = true;
                if (this.sortInput) this.sortInput.disabled = true;
            }

            #handleSubmit = (event) => {
                event.preventDefault();
                this.search();
            }

            #handleInputChange = () => {
                this.query = this.queryInput.value;
            }

            #handleFocus = () => {
                this.fire("activeChange", { active: true });
            }

            #handleSearchDropdownChange = (event) => {
                this.source = event.newVal.source;
                this.search();
            }
        }

        // instantiate the controller and assign it to the global L.search
        const searchForm = document.querySelector(".search-form");
        L.search = new SearchFormController(searchForm);

    })();
}
