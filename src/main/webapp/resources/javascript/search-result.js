(function() {
	
	var SearchResult = Y.namespace("lane.SearchResult");
	
	/**
	 * Get the search terms from a query string passed or the location.search
	 * @method getSearchTerms
	 * @static
	 * @param queryString {String}
	 * @return the decoded q parameter or undefined
	 */
	SearchResult.getSearchTerms = function(queryString) {
		queryString = queryString || location.search;
		return Y.QueryString.parse(queryString).q;
	};
	
	/**
	 * Get the source parameter from a query string passed or the location.search
	 * @method getSearchSource
	 * @static
	 * @param queryString {String}
	 * @return the source parameter or undefined
	 */
	SearchResult.getSearchSource = function(queryString) {
		queryString = queryString || location.search;
		return Y.QueryString.parse(queryString).source;
	};
	
})();
