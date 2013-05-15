(function() {
    var form = Y.one("#search");//the form Element
    if (form) {
    LANE.Search = function() {
        var searchSourceSelect = form.one('#searchSource'),
            searchOptions = searchSourceSelect.all('option'),
            searchTipsLink = Y.one('#searchTips a'),
            selectedOption = searchOptions.item(searchSourceSelect.get('selectedIndex')),
            searchIndicator = Y.lane.SearchIndicator,
            searchTextInput = new Y.lane.TextInput(form.one('#searchTerms')),
            getLimitForSource = function(source) {
                var limit = "";
                if (source.match(/^(all|articles|catalog)/)) {
                    limit = "er-mesh";
                } else if (source.match(/^bioresearch/)) {
                    limit = "mesh";
                } else if (source.match(/^history/)) {
                    limit = "history";
                } else if (source.match(/^bassett/)) {
                    limit = "bassett";
                }
                return limit;
            },
            searchTermsSuggest = new Y.lane.Suggest(searchTextInput.getInput(), getLimitForSource(selectedOption.get("value"))),
            search;
        form.on('submit', function(submitEvent) {
            submitEvent.preventDefault();
            try {
                search.submitSearch();
            //TODO: popup instead of alert
            } catch (e) {
                alert(e);
            }
        });
        Y.publish("lane:searchSourceChange",{broadcast:1});
        Y.publish('lane:beforeSearchSubmit', {broadcast:1});
        Y.on('lane:searchSourceChange', function(event) {
            selectedOption = searchOptions.item(searchSourceSelect.get('selectedIndex'));
            searchTextInput.setHintText(selectedOption.get('title'));
            searchTipsLink.set('href',searchTipsLink.get('href').replace(/#.*/,'#'+searchSourceSelect.get('value')));
            form.one('#searchTerms').focus();
            searchTextInput.setValue(searchTextInput.getValue());
            searchTermsSuggest.setLimit(getLimitForSource(event.newVal));
        });
        searchTipsLink.set('href',searchTipsLink.get('href')+'#'+searchSourceSelect.get('value'));
        searchTextInput.setHintText(selectedOption.get('title'));
        searchSourceSelect.on('change', function(e) {
//            if (search.searchTermsPresent()) {
//                search.submitSearch();
//            } else {
                Y.fire('lane:searchSourceChange', {newVal:this.get("value")});
//            }
        });
        searchTermsSuggest.on("select",function(e){
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
                    if (!search.searchTermsPresent()) {
                        throw ('nothing to search for');
                    }
                    searchIndicator.show();
                    Y.fire('lane:beforeSearchSubmit', search);
                    form.submit();
                },
                searchTermsPresent : function() {
                    return searchTextInput.getValue() !== '';
                }
        };
        return search;
    }();
    }
})();
