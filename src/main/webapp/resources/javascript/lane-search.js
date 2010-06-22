YUI().use('lane', "lane-search-indicator", 'lane-textinputs', 'lane-suggest', 'node','event-custom', function(Y) {
    LANE.Search = function() {
        var searching = false, //searching state
            form = Y.one('#search'), //the form Element
            searchSourceSelect = form.one('#searchSource'),
            searchOptions = searchSourceSelect.all('option'),
            searchTipsLink = Y.one('#searchTips'),
            selectedOption = searchOptions.item(searchSourceSelect.get('selectedIndex')),
            searchIndicator = new Y.lane.SearchIndicator(),
            searchTextInput = new Y.lane.TextInput(form.one('#searchTerms')),
            searchTermsSuggest = new Y.lane.Suggest(searchTextInput.getInput()),
            search,
            searchTermsPresent = function() {
                return searchTextInput.getValue() !== '';
            };
        form.on('submit', function(submitEvent) {
            submitEvent.preventDefault();
            try {
                search.submitSearch();
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
            searchTipsLink.set('href',searchTipsLink.get('href').replace(/#.*/,'#'+searchSourceSelect.get('value')));
        });
        searchTipsLink.set('href',searchTipsLink.get('href')+'#'+searchSourceSelect.get('value'));
        searchTextInput.setHintText(selectedOption.get('title'));
        searchSourceSelect.on('change', function(e) {
            if (searchTermsPresent()) {
                search.submitSearch();
            } else {
                Y.fire('lane:searchSourceChange', {newVal:this.get("value")});
            }
        });
        searchTermsSuggest.on("lane:suggestSelect",function(e){
            search.submitSearch();
        });
        search =  {
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
                    searchIndicator.show();
                    Y.fire('lane:beforeSearchSubmit', search);
                    form.submit();
                }
        };
        return search;
    }();
});
