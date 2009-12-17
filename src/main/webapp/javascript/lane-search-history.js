(function() {
    var searchString = LANE.search.getEncodedSearchString(),
        YD = YAHOO.util.Dom,
        YE = YAHOO.util.Event,
        YH = YAHOO.util.History,
        facetChangeHandler = function(facetId){
            var r = document.getElementById(facetId+'Facet').result;// result facet to make active
            //TODO: this reads private attributes from Result ... change?
            if (r !== undefined){
            	if (r._state == 'initialized') {
            		r.show();
            	} else if (r._state == 'searched') {
            		LANE.search.facets.getCurrentResult().hide();
            		LANE.search.facets.setCurrentResult(r);
            		r.show();
            	} else {
            		// TODO: necessary?
            		//r._state = 'initialized';
            	}
            }
        },
        initializeHistory = function(){
            var initialFacet = YH.getBookmarkedState("facet") || YH.getQueryStringParameter("source");
            YH.onReady(function() {
            	facetChangeHandler(YH.getCurrentState("facet"));
            });
            YH.register("facet", initialFacet, facetChangeHandler);
            YH.initialize("yui-history-field", "yui-history-iframe");
        };

    if ( searchString && YD.inDocument('yui-history-field') && YD.inDocument('yui-history-iframe') ) {
        YE.addListener(this,'load',initializeHistory);
    }
})();