(() => {

    "use strict";

    /**
     * Initialize suggestion component for each facet search input.
     * When a user selects a suggestion, add the selection as a new facet
     * to the main search form's hidden facet input and submit the form.
     */

    // bail if facets container not present
    if (!document.querySelector(".solrFacets")) {
        return;
    }

    const searchForm = document.querySelector(".search-form");
    const facetsInput = searchForm.querySelector("input[name=facets]");
    const RESULT_NOT_FOUND = "No match found";

    document.querySelectorAll(".facet-suggestion").forEach(input => {
        // get data from facet input attributes
        const { facet, searchterm, facets } = input.dataset;

        // construct the suggestion source URL
        const sourceBase = `/apps/solr/facet/suggest?q=${searchterm}&contains={query}&facet=${facet}&facets=${encodeURI(facets)}`;

        // initialize the suggestion component
        const suggest = new L.Suggest(input, sourceBase, {
            minQueryLength: 1,
        });

        // make the suggest instance an event target
        L.addEventTarget(suggest);

        // event handler for suggestion selection
        const handleSuggestionSelect = (event) => {
            const selectedValue = event.suggestion;

            // handle the special case where "no match found" is selected and clear input
            if (selectedValue === RESULT_NOT_FOUND) {
                input.value = '';
                return;
            }

            // update the facets input value on the main search form
            const currentFacets = facetsInput.value ? facetsInput.value.split('::') : [];
            const newFacet = `${facet}:"${selectedValue}"`;

            currentFacets.push(newFacet);
            facetsInput.value = currentFacets.join('::');
            facetsInput.disabled = false;

            searchForm.submit();
        };

        suggest.on("suggest:select", handleSuggestionSelect);
    });

})();
