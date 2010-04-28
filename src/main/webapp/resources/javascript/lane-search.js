YUI().add('lane-search', function(Y) {
    LANE.search = function() {
        var searching = false, //searching state
            searchString,
            encodedString,
            form = Y.one('#search'), //the form Element
            searchTermsInput = Y.one('#searchTerms'),
            searchSourceInput = Y.one('#searchSource'),
            source,
            searchIndicator = Y.one('#searchIndicator'),
            initialText,
            i,
            picoInputs = ['p', 'i', 'c', 'o'],
            setInitialText = function() {
                var oldInitialText = initialText;
                    initialText = Y.one('#searchSource').one('option').get('title');
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
            searchSourceInput.on('change', function(e) {
            	var options, selectedOption, nav;
                if (searchTermsInput.get('value') && searchTermsInput.get('value') != searchTermsInput.get('title')) {
                    LANE.search.submitSearch();
                } else {
                    nav = Y.one('#laneNav');
                	options = e.currentTarget.all('option');
                	selectedOption = options.item(e.currentTarget.get('selectedIndex'));
                    searchTermsInput.set('value', selectedOption.get('title'));
                    searchTermsInput.set('title', selectedOption.get('title'));
                    if (selectedOption.get('value') == 'clinical-all') {
                        form.addClass('clinical');
                        nav.addClass('clinical');
                    } else {
                        form.removeClass('clinical');
                        nav.removeClass('clinical');
                    }
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
                        source = searchSourceInput.value;
                    }
                    if (source === undefined) {
                        source = '';
                    }
                }
                return source;
            },
            setSearchSource: function(source) {
                searchSourceInput.set('value', source);
            },
            setActiveTab: function(elm) {
                if (!elm.hasClass('active')) {
                    // if this is not already active tab and there's a form value, submit search
                    if (searchTermsInput.get('value') && searchTermsInput.get('value') != initialText) {
                    	LANE.search.setSearchSource(elm.get('id') + '-all');
                        LANE.search.submitSearch();
                    } else {
                        Y.fire('lane:searchTabChange', elm);
                    }
                }
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
