if (document.querySelector(".search-form")) {
    (() => {

        "use strict";

        /**
         * A controller class for the main search form.
         * It encapsulates state, DOM interactions, and event handling for the
         * search query, source, and related UI elements.
         */
        class SearchFormController {
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
                this.query = this.queryInput.value;
                this.source = this.sourceInput.value;
                this.isSearching = false;

                this._bindEvents();
            }

            /**
             * Bind internal and external event listeners
             */
            _bindEvents() {
                this.form.addEventListener("submit", this._handleSubmit);
                this.queryInput.addEventListener("focus", this._handleFocus);
                this.queryInput.addEventListener("input", this._handleInputChange);

                // listen for custom global events
                L.on("searchDropdown:change", this._handleSearchDropdownChange);
                L.on("searchReset:reset", this.reset);
            }

            getQuery() { return this.query; }

            getSource() { return this.source; }

            getSearching() { return this.isSearching; }

            /**
             * Sets the search query, updates the input, and fires an event.
             * @param {string} newQuery - The new search term.
             */
            setQuery(newQuery) {
                if (typeof newQuery !== "string" || newQuery === this.query) return;

                const oldQuery = this.query;
                this.query = newQuery;
                this.queryInput.value = this.query; // Directly update the view

                this.fire("queryChange", { newVal: this.query, oldVal: oldQuery });
            }

            /**
             * Sets the search source, updates the input, resets facets, and fires an event.
             * @param {string} newSource - The new search source.
             */
            setSource(newSource) {
                if (typeof newSource !== "string" || newSource === this.source) return;

                const oldSource = this.source;
                this.source = newSource;
                this.sourceInput.value = this.source;

                // Reset facets when the source changes
                if (this.facetsInput.value) {
                    this.facetsInput.value = '';
                    this.facetsInput.disabled = true;
                }

                this.fire("sourceChange", { newVal: this.source, oldVal: oldSource });
            }

            /**
             * Programmatically trigger a search
             */
            search() {
                if (this.query) {
                    this.isSearching = true;
                    this.fire("search");
                    this.form.submit();
                }
            }

            /**
             * Reset search form inputs
             */
            reset = () => {
                this.setQuery("");
                if (this.facetsInput) this.facetsInput.disabled = true;
                if (this.sortInput) this.sortInput.disabled = true;
            }

            _handleSubmit = (event) => {
                event.preventDefault();
                this.search();
            }

            _handleInputChange = () => {
                this.setQuery(this.queryInput.value);
            }

            _handleFocus = () => {
                this.fire("activeChange", { active: true });
            }

            _handleSearchDropdownChange = (event) => {
                this.setSource(event.newVal.source);
                this.search();
            }
        }

        // instantiate the controller and assign it to the global L.search
        const searchForm = document.querySelector(".search-form");
        L.search = new SearchFormController(searchForm);

    })();
}
