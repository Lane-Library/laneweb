(() => {

    "use strict";

    /**
     * Handles validation and submission for the Solr date range facet form.
     * When this form is submitted, it validates the dates, updates hidden 'facets'
     * input in the main search form, and then submits the main search form.
     */
    const dateSolrForm = document.querySelector("#solr-date-form");

    // do nothing if solr-date-form is not found
    if (!dateSolrForm) {
        return;
    }

    const searchForm = document.querySelector(".search-form");
    const startYearInput = document.querySelector(".date.start");
    const endYearInput = document.querySelector(".date.end");
    const facetsInput = searchForm.querySelector("input[name=facets]");
    const errorMessage = document.querySelector("#facet-error-message");

    const ERROR_MESSAGE_YEAR_START_GREATER_THAN_YEAR_END = "The start year should be smaller than the end year";

    /**
     * main handler for the solr date form
     * @param {Event} event - form submission event
     */
    const handleDateSubmit = (event) => {
        event.preventDefault();
        // clear any previous errors
        errorMessage.textContent = "";

        const startYear = startYearInput.value;
        const endYear = endYearInput.value;

        // --- Validation ---
        if (!startYearInput.checkValidity()) {
            errorMessage.textContent = startYearInput.validationMessage;
            return;
        }
        if (!endYearInput.checkValidity()) {
            errorMessage.textContent = endYearInput.validationMessage;
            return;
        }
        // ensure that the start year is not after the end year
        // since both inputs are required, could be simplified to remove the check for empty years
        if (startYear && endYear && parseInt(startYear, 10) > parseInt(endYear, 10)) {
            errorMessage.textContent = ERROR_MESSAGE_YEAR_START_GREATER_THAN_YEAR_END;
            return;
        }

        // --- update facets input ---
        const allFacets = facetsInput.value ? facetsInput.value.split('::') : [];
        const nonYearFacets = allFacets.filter(facet => !facet.startsWith('year:['));

        // add the new year facet if either input has a value
        // defaults (*, NOW) are provided but likely not necessary assuming inputs are required and of type number
        if (startYear || endYear) {
            const newYearFacet = `year:[${startYear || '*'} TO ${endYear || 'NOW'}]`;
            nonYearFacets.push(newYearFacet);
        }

        facetsInput.value = nonYearFacets.join('::');

        // will likely never be true assuming inputs are required and of type number
        facetsInput.disabled = nonYearFacets.length === 0;

        searchForm.submit();
    };

    dateSolrForm.addEventListener("submit", handleDateSubmit);

})();
