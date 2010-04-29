YUI().add('lane-search', function(Y) {
    LANE.search = LANE.search || function() {
        var searching = false, //searching state
            searchString,
            encodedString,
            form = Y.one('#search'), //the form Element
            searchTermsInput = Y.one('#searchTerms'),
            searchSourceSelect = Y.one('#searchSource'),
            searchOptions = searchSourceSelect.all('option'),
            selectedOption = searchOptions.item(searchSourceSelect.get('selectedIndex')),
            source,
            searchIndicator = Y.one('#searchIndicator'),
            initialText,
            setInitialText = function() {
                var oldInitialText = initialText;
                    initialText = selectedOption.get('title');
                if (!searchTermsInput.get('value') || searchTermsInput.get('value') == oldInitialText) {
                    searchTermsInput.set('value', initialText);
                    searchTermsInput.set('title', initialText);
                }
        	};
        setInitialText();
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
            searchTermsInput.set('value', selectedOption.get('title'));
            searchTermsInput.set('title', selectedOption.get('title'));
        });
        searchSourceSelect.on('change', function(e) {
            if (searchTermsInput.get('value') && searchTermsInput.get('value') != searchTermsInput.get('title')) {
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
            getSearchString: function() {
                if (searchString === undefined) {
                    if (encodedString === undefined) {
                        this.getEncodedSearchString();
                    }
                    searchString = decodeURIComponent(encodedString);
                }
                return searchString;
            },
            getEncodedSearchString: function() {
                var query, vars, pair, i;
                if (encodedString === undefined) {
                    query = location.search.substring(1);
                    vars = query.split('&');
                    for (i = 0; i < vars.length; i++) {
                        pair = vars[i].split('=');
                        if (pair[0] == 'q') {
                            encodedString = pair[1];
                            break;
                        }
                    }
                    if (encodedString === undefined) {
                        encodedString = '';
                    }
                }
                return encodedString;
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
                        source = searchSourceSelect.get('value');
                    }
                    if (source === undefined) {
                        source = '';
                    }
                }
                return source;
            },
            submitSearch: function() {
                if (!searchTermsInput.get('value') || searchTermsInput.get('value') == initialText) {
                    throw ('nothing to search for');
                }
                LANE.search.startSearch();
                form.submit();
            }
        };
    }();
}, '1.11.0-SNAPSHOT', {requires:['lane', 'node']});
