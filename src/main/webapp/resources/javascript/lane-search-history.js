(function() {
	//LANE.Search may be undefined
	if (LANE.Search) {
		var Y = LANE.Y,
		    history,
		    searchFacets = Y.one('#searchFacets');
		
		LANE.Search.History = function() {
			if (searchFacets) {
				history = new Y.HistoryHash();
				if (history.get('facet')) {
					LANE.search.facets.setActiveFacet(history.get('facet'));
				}
				history.on("facetChange", function(e) {
					LANE.search.facets.setActiveFacet(e.newVal);
				});
				history.on("facetRemove", function(e) {
					LANE.search.facets.setActiveFacet(LANE.Search.getSearchSource());
				});
			}
			return history;
		}();

	}

})();
