(function() {
	
	var Lane = Y.lane;
	
	var searchTerms = new Lane.TextInput(Y.one("#searchTerms"));
	
//	var SearchSource = Y.namespace("lane.SearchSource");
	
//	Y.augment(SearchSource, Y.EventTarget);
	
	var Search = Y.namespace("lane.Search");
	
//	Y.augment(Search, Y.EventTarget);
	
	var searchIndicator = Lane.SearchIndicator;
	
	var form = Y.one("#search");
	
	var searchSourceSelect = Y.one("#searchSource");
	
	var searchSelectWidget = new Lane.SearchSelectWidget({srcNode:searchSourceSelect,render:true});
	
	var selectModel = searchSelectWidget.get("model");
	
	Search.submitSearch = function() {
		if (!searchTerms.getValue()) {
            throw ('nothing to search for');
		}
		searchIndicator.show();
		form.submit();
	};
	
	Search.getSearchTerms = function() {
		return searchTerms.getValue();
	};
	
	Search.setSearchTerms = function(terms) {
		searchTerms.setValue(terms);
	};
})();
