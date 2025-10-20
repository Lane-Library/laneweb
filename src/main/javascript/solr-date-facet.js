(() => {

    "use strict";

    /**
     * Handles validation and submission for the Solr date range facet form.
     * When this form is submitted, it validates the dates, updates hidden 'facets'
     * input in the main search form, and then submits the main search form.
     */
    class SolrDateFacet {

        static #ERROR_MESSAGE_YEAR_START_GREATER_THAN_YEAR_END = "The start year should be smaller than the end year";

        #dateForm;
        #searchForm;
        #startYearInput;
        #endYearInput;
        #facetsInput;
        #errorMessage;

        constructor(formElement) {
            this.#dateForm = formElement;
            this.#searchForm = document.querySelector(".search-form");
            this.#startYearInput = this.#dateForm.querySelector(".date.start");
            this.#endYearInput = this.#dateForm.querySelector(".date.end");
            this.#facetsInput = this.#searchForm.querySelector("input[name=facets]");
            this.#errorMessage = document.querySelector("#facet-error-message");

            this.#dateForm.addEventListener("submit", this.#handleDateSubmit);
        }

        /**
         * Main handler for the solr date form submission.
         * @param {Event} event - The form submission event.
         */
        #handleDateSubmit = (event) => {
            event.preventDefault();
            this.#errorMessage.textContent = "";

            const startYear = this.#startYearInput.value;
            const endYear = this.#endYearInput.value;

            if (!this.#validateInputs(startYear, endYear)) {
                return;
            }

            const allFacets = this.#facetsInput.value ? this.#facetsInput.value.split('::') : [];
            const nonYearFacets = allFacets.filter(facet => !facet.startsWith('year:['));

            // add the new year facet if either input has a value
            // defaults (*, NOW) are provided but likely not necessary assuming inputs are required and of type number
            if (startYear || endYear) {
                const newYearFacet = `year:[${startYear || '*'} TO ${endYear || 'NOW'}]`;
                nonYearFacets.push(newYearFacet);
            }

            this.#facetsInput.value = nonYearFacets.join('::');

            // will likely never be true assuming inputs are required and of type number
            this.#facetsInput.disabled = nonYearFacets.length === 0;

            this.#searchForm.submit();
        }

        /**
         * Validates the start and end year inputs.
         * @param {string} startYear - The start year value.
         * @param {string} endYear - The end year value.
         * @returns {boolean} - True if inputs are valid, false otherwise.
         */
        #validateInputs(startYear, endYear) {
            if (!this.#startYearInput.checkValidity()) {
                this.#errorMessage.textContent = this.#startYearInput.validationMessage;
                return false;
            }
            if (!this.#endYearInput.checkValidity()) {
                this.#errorMessage.textContent = this.#endYearInput.validationMessage;
                return false;
            }
            if (startYear && endYear && parseInt(startYear, 10) > parseInt(endYear, 10)) {
                this.#errorMessage.textContent = SolrDateFacet.#ERROR_MESSAGE_YEAR_START_GREATER_THAN_YEAR_END;
                return false;
            }
            return true;
        }
    }

    /**
     * Finds and initializes the Solr date facet form.
     * This function will only initialize the form once.
     */
    const initializeDateForm = () => {
        const dateSolrForm = document.querySelector("#solr-date-form");
        // do nothing if solr-date-form is not found or already initialized
        if (dateSolrForm && dateSolrForm.dataset.initialized !== "true") {
            dateSolrForm.dateFacet = new SolrDateFacet(dateSolrForm);
            dateSolrForm.dataset.initialized = "true";
        }
    };

    // Initialize on page load and when new facets are loaded.
    initializeDateForm();
    L.on("solrFacets:loaded", initializeDateForm);

})();
