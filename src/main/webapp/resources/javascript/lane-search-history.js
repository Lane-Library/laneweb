YUI().use('lane-search-result', 'lane-search-facets', 'node', 'event-custom', 'history', function(Y) {
	var searchString = Y.lane.SearchResult.getEncodedSearchTerms(),
	facetNodes = Y.one('#searchFacets'),
	initializeHistory = function() {
		var initialFacet = Y.History.getBookmarkedState("facet") || Y.History.getQueryStringParameter("source");
		Y.History.register("facet", initialFacet).on("history:moduleStateChange", LANE.search.facets.setActiveFacet);
		Y.History.on("history:ready",function() {
			LANE.search.facets.setActiveFacet(Y.History.getCurrentState("facet"));
		});
		Y.History.initialize("#yui-history-field", "#yui-history-iframe");
	};
	
	if (searchString && facetNodes) {
		Y.one('body').insert(Y.Node.create('<iframe id="yui-history-iframe" src="/graphics/spacer.gif"></iframe>'),0);
		Y.one('body').insert(Y.Node.create('<input id="yui-history-field" type="hidden"/>'),1);
		initializeHistory();
	}
});
