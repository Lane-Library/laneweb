YUI().use('lane-search', 'node', 'event-custom', 'yui2-history', function(Y) {
    var searchString = LANE.search.getEncodedSearchString(),
        YH = Y.YUI2.util.History,
        facetChangeHandler = function(facetId) {
            var result = Y.one('#' + facetId + 'Facet').getData('result');// result facet to make active
            //TODO: this reads private attributes from Result ... change?
            if (result !== undefined) {
                if (result._state == 'initialized') {
                    result.show();
                } else if (result._state == 'searched') {
                    LANE.search.facets.getCurrentResult().hide();
                    LANE.search.facets.setCurrentResult(r);
                    result.show();
                } else {
                    // TODO: necessary?
                    //result._state = 'initialized';
                }
            }
        },
        initializeHistory = function() {
            var initialFacet = YH.getBookmarkedState("facet") || YH.getQueryStringParameter("source");
            YH.onReady(function() {
                facetChangeHandler(YH.getCurrentState("facet"));
            });
            YH.register("facet", initialFacet, facetChangeHandler);
            YH.initialize("yui-history-field", "yui-history-iframe");
        };
    //TODO: create these rather than rely on presence in markup
    if (searchString && Y.one('#yui-history-field') && Y.one('#yui-history-iframe')) {
        initializeHistory();
    }
});
