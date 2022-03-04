(function() {

    "use strict";

    // LANEWEB-10903: hide NLM Catalog facet label to not give impression
    // Lane Search includes all of NLM
    // "NLM Catalog" should still appear under "Filters applied"
    // for requests like /view/dnlm/101194519
    var FACET_HEADER_CLASS = "solrFacet facetHeader",
        hideNlmCatalog = function() {
            document.querySelectorAll(".facetLabel").forEach(function(node) {
                var ancestor = node.closest("li"),
                prevLi = ancestor.previousElementSibling,
                nextLi = ancestor.nextElementSibling;
                if ("NLM Catalog" == node.textContent) {
                    ancestor.style.display = "none";
                }
                // hide facet header "Results from" if NLM is only visible facet
                if (FACET_HEADER_CLASS == prevLi.className && FACET_HEADER_CLASS == nextLi.className) {
                    prevLi.style.display = "none";
                }
            });
        }

    // reinitialize when content has changed
    L.on('lane:new-content', function() {
        hideNlmCatalog();
    });

    // added for unit test
    if (document.querySelector('.solrFacet')) {
        hideNlmCatalog();
    }

})();
