(() => {

    "use strict";

    /**
     * Manages the suggestion functionality for a single facet input.
     */
    class SolrFacetSuggest {

        static #RESULT_NOT_FOUND = "No match found";

        #input;
        #suggest;
        #searchForm;
        #facetsInput;

        constructor(inputElement) {
            this.#input = inputElement;
            this.#searchForm = document.querySelector(".search-form");
            this.#facetsInput = this.#searchForm.querySelector("input[name=facets]");

            const { facet, searchterm, facets } = this.#input.dataset;
            const sourceBase = `/apps/solr/facet/suggest?q=${searchterm}&contains={query}&facet=${facet}&facets=${encodeURI(facets)}`;

            this.#suggest = new L.Suggest(this.#input, sourceBase, { minQueryLength: 1 });

            L.addEventTarget(this.#suggest);
            this.#suggest.on("suggest:select", this.#handleSuggestionSelect);
        }

        #handleSuggestionSelect = (event) => {
            const selectedValue = event.suggestion;

            if (selectedValue === SolrFacetSuggest.#RESULT_NOT_FOUND) {
                this.#input.value = '';
                return;
            }

            const currentFacets = this.#facetsInput.value ? this.#facetsInput.value.split('::') : [];
            const newFacet = `${this.#input.dataset.facet}:"${selectedValue}"`;

            currentFacets.push(newFacet);
            this.#facetsInput.value = currentFacets.join('::');
            this.#facetsInput.disabled = false;

            this.#searchForm.submit();
        }
    }

    /**
     * Initialize suggestion component for each facet search input.
     */
    const initializeFacetSuggestions = () => {
        if (!document.querySelector(".solrFacets")) {
            return;
        }

        document.querySelectorAll(".facet-suggestion").forEach(input => {
            if (input.dataset.initialized !== "true") {
                input.facetSuggest = new SolrFacetSuggest(input);
                input.dataset.initialized = "true";
            }
        });
    };

    // Run on page load and when new facets are loaded.
    initializeFacetSuggestions();
    L.on("solrFacets:loaded", initializeFacetSuggestions);

})();
