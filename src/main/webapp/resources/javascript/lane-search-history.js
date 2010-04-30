YUI().use('lane-search', 'node', 'event-custom', 'history', function(Y) {
	var searchString = LANE.search.Result.getEncodedSearchTerms(),
	initializeHistory = function() {
		var initialFacet = Y.History.getBookmarkedState("facet") || Y.History.getQueryStringParameter("source");
		Y.History.register("facet", initialFacet).on("history:moduleStateChange", LANE.search.facets.setActiveFacet);
		Y.History.on("history:ready",function() {
			LANE.search.facets.setActiveFacet(Y.History.getCurrentState("facet"));
		});
		Y.History.initialize("#yui-history-field", "#yui-history-iframe");
	};
	
	if (searchString) {
		Y.one('body').insert(Y.Node.create('<iframe id="yui-history-iframe" src="/graphics/spacer.gif"></iframe>'),0);
		Y.one('body').insert(Y.Node.create('<input id="yui-history-field" type="hidden"/>'),1);
		Y.Global.on('lane:searchFacetsReady', initializeHistory);
	}
});
