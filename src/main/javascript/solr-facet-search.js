(() => {

    "use strict";

    /**
     * Dynamically load Solr facets into the search results page 
     * if they have not already been xincluded on search.html.
     */
    const solrFacets = document.querySelector('.solrFacets');

    // do nothing if no facets container or facets are already loaded
    if (!solrFacets || solrFacets.querySelector('div')) return;

    const model = L.Model;
    const encodedQuery = model.get(model.URL_ENCODED_QUERY);
    // do nothing if no query
    if (!encodedQuery) return;

    const source = model.get(model.URL_ENCODED_SOURCE);
    const basePath = model.get(model.BASE_PATH) || "";
    const searchForm = document.querySelector(".search-form");
    const facetsInput = searchForm?.querySelector("input[name=facets]");
    const facetsQuery = facetsInput?.value ? `&facets=${encodeURIComponent(facetsInput.value)}` : '';

    const url = `${basePath}/apps/search/facets/html?q=${encodedQuery}${facetsQuery}&source=${source}`;
    fetch(url)
        .then(r => r.ok ? r.text() : Promise.reject(`HTTP error! status: ${r.status} ${r.statusText}`))
        .then(html => {
            solrFacets.innerHTML = html;
            L.fire('solrFacets:loaded', { facets: solrFacets });
        })
        .catch(err => console.error('Error loading Solr facets:', err));

})();

