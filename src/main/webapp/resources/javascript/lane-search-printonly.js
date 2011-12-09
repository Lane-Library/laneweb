/*
 * Display PRINT MATERIALS (catalog-lois) filter if print results are only results present
 */
(function() {
    var printId = 'catalog-lois', 
    noHits = Y.one('#noHitsText'), 
    printFacet = Y.one('#'+printId+'Facet');
    if(noHits && printFacet && !printFacet.hasClass('inactiveFacet')){
        LANE.search.facets.setActiveFacet(printId);
        LANE.Search.History.addValue("facet", printId);
        Y.one('#all-allFacet').addClass('inactiveFacet');
        Y.fire('lane:change');
    }
})();