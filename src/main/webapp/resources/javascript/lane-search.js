YUI().add('lane-search', function(Y) {
    LANE.namespace('search');
    LANE.search.Result = LANE.search.Result || function() {
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
    LANE.search.Search = LANE.search.Search || function() {
        var searching = false, //searching state
            form = Y.one('#search'), //the form Element
            searchTermsInput = Y.one('#searchTerms'),
            searchSourceSelect = Y.one('#searchSource'),
            searchOptions = searchSourceSelect.all('option'),
            selectedOption = searchOptions.item(searchSourceSelect.get('selectedIndex')),
            searchIndicator = Y.one('#searchIndicator'),
			searchTermsPresent = function() {
				var value = searchTermsInput.get('value');
				return (value && value != searchTermsInput.get('title'));
			},
            setInitialText = function() {
                var initialText = selectedOption.get('title');
                searchTermsInput.set('value', initialText);
                searchTermsInput.set('title', initialText);
        	};
        form.on('submit', function(submitEvent) {
            submitEvent.preventDefault();
            try {
                LANE.search.submitSearch();
            } catch (e) {
                alert(e);
            }
        });
        Y.publish("lane:searchSourceChange",{broadcast:2});
        Y.on('lane:searchSourceChange', function() {
            selectedOption = searchOptions.item(searchSourceSelect.get('selectedIndex'));
			setInitialText();
        });
        searchSourceSelect.on('change', function(e) {
            if (searchTermsPresent()) {
                LANE.search.submitSearch();
            } else {
                Y.fire('lane:searchSourceChange', this.get('value'));
            }
        }); 
        
        return {
            startSearch: function() {
                searching = true;
                searchIndicator.setStyle('visibility', 'visible');
            },
            stopSearch: function() {
                searching = false;
                searchIndicator.setStyle('visibility', 'hidden');
            },
            isSearching: function() {
                return searching;
            },
			getSearchTerms: function() {
				var value = searchTermsInput.get('value');
				return value == searchTermsInput.get('title') ? '' : value;
			},
			setSearchTerms: function(searchString) {
				searchTermsInput.set('value', searchString);
			},
            submitSearch: function() {
                if (!searchTermsPresent()) {
                    throw ('nothing to search for');
                }
                LANE.search.startSearch();
                form.submit();
            }
        };
    }();
}, '1.11.0-SNAPSHOT', {requires:['lane', 'node']});
