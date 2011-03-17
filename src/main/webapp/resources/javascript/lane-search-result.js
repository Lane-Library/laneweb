(function() {
    Y.lane.SearchResult = function() {
        var searchString,
            encodedSearchString,
            source;
        return {
            getSearchTerms: function() {
                if (searchString === undefined) {
                    if (encodedSearchString === undefined) {
                        this.getEncodedSearchTerms();
                    }
                    searchString = decodeURIComponent(encodedSearchString).replace('+',' ');
                }
                return searchString;
            },
            getEncodedSearchTerms: function() {
                var query, vars, pair, i;
                if (encodedSearchString === undefined) {
                    query = location.search.substring(1);
                    vars = query.split('&');
                    for (i = 0; i < vars.length; i++) {
                        pair = vars[i].split('=');
                        if (pair[0] == 'q') {
                            encodedSearchString = pair[1];
                            break;
                        }
                    }
                    if (encodedSearchString === undefined) {
                        encodedSearchString = '';
                    }
                }
                return encodedSearchString;
            },
            getSearchSource: function() {
                var query, vars, pair, i;
                if (source === undefined) {
                    query = location.search.substring(1);
                    vars = query.split('&');
                    for (i = 0; i < vars.length; i++) {
                        pair = vars[i].split('=');
                        if (pair[0] == 'source') {
                            source = pair[1];
                            break;
                        }
                    }
                    if (source === undefined) {
                        source = '';
                    }
                }
                return source;
            }
        };
    }();
})();
