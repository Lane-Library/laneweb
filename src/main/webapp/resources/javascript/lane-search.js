YUI().use('node', function(Y) {
    Y.Global.on('lane:ready', function() {
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
                tabs = Y.all('#searchTabs > li'),
                i,
                picoInputs = ['p', 'i', 'c', 'o'],
                setInitialText = function() {
                    var oldInitialText = initialText;
                    initialText = Y.one('#searchTabs').one('.active').get('title');
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
            for (i = 0; i < tabs.size(); i++) {
                Y.on('click', function(event) {
                    event.preventDefault();
                    LANE.search.setActiveTab(this);
                }, tabs.item(i));
            }
            Y.publish("lane:searchTabChange",{broadcast:2});
            Y.on('lane:searchTabChange', function(elm) {
                tabs.removeClass('active');
                elm.addClass('active');
                LANE.search.setSearchSource(elm.get('id') + '-all');
                setInitialText();
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
        
        Y.publish('lane:searchready', {
            broadcast: 2,
            fireOnce: true
        });
        Y.fire('lane:searchready');
    });
});
