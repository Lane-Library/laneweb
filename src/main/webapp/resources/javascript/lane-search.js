YUI().add('lane-search', function(Y) {
    LANE.namespace('search');
    LANE.search.Search = LANE.search.Search || function() {
        var searching = false, //searching state
            form = Y.one('#search'), //the form Element
            searchSourceSelect = form.one('#searchSource'),
            searchOptions = searchSourceSelect.all('option'),
            selectedOption = searchOptions.item(searchSourceSelect.get('selectedIndex')),
            searchIndicator = form.one('#searchIndicator'),
            searchTextInput = new Y.lane.TextInput(form.one('#searchTerms')),
			searchTermsPresent = function() {
				return searchTextInput.getValue() !== '';
			},
            search = {
                startSearch: function() {
                    searching = true;
                    searchIndicator.setStyle('visibility', 'visible');
                },
                stopSearch: function() {
                    searching = false;
                    searchIndicator.setStyle('visibility', 'hidden');
                },
                getSearchSource: function() {
                    return searchSourceSelect.get('value');
                },
                getSearchTerms: function() {
                    return searchTextInput.getValue();
                },
                setSearchTerms: function(searchString) {
                    searchTextInput.setValue(searchString);
                },
                submitSearch: function() {
                    if (!searchTermsPresent()) {
                        throw ('nothing to search for');
                    }
                    LANE.search.Search.startSearch();
                    Y.fire('lane:beforeSearchSubmit', search);
                    form.submit();
                }
            };
        form.on('submit', function(submitEvent) {
            submitEvent.preventDefault();
            try {
                LANE.search.Search.submitSearch();
            //TODO: popup instead of alert
            } catch (e) {
                alert(e);
            }
        });
        Y.publish("lane:searchSourceChange",{broadcast:2});
        Y.publish('lane:beforeSearchSubmit', {broadcast:2});
        Y.on('lane:searchSourceChange', function() {
            selectedOption = searchOptions.item(searchSourceSelect.get('selectedIndex'));
            searchTextInput.setHintText(selectedOption.get('title'));
        });
        searchTextInput.setHintText(selectedOption.get('title'));
        searchSourceSelect.on('change', function(e) {
            if (searchTermsPresent()) {
                LANE.search.Search.submitSearch();
            } else {
                Y.fire('lane:searchSourceChange', search);
            }
        });
        new Y.lane.Suggest(searchTextInput.getInput());
        return search;
    }();
}, '1.11-0-SNAPSHOT', {requires:['lane', 'lane-textinputs', 'lane-suggest', 'node']});
