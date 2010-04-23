YUI().use('node',function(Y) {
    Y.Global.on('lane:ready', function() {
LANE.search = LANE.search ||
function() {
    var searching = false, //searching state
        searchString,
        encodedString,
        form = Y.one('#search'), //the form Element
        searchTermsInput = Y.one('#searchTerms'),
        searchSourceInput = Y.one('#searchSource'),
        source,
        searchIndicator = Y.one('#searchIndicator'),
        initialText,
        tabs = Y.all('#searchTabs > li'), i,
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
        Y.on('submit', function(submitEvent) {
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
    
    return {
        startSearch: function() {
            //TODO: revisit this, do we want to prevent a new search when one in progress?
            //                if (searching) {
            //                    throw('already searching');
            //                }
            //if (!searchTermsInput.value || searchTermsInput.value == initialText) {
            //    throw ('nothing to search for');
            //}
            searching = true;
            searchIndicator.setStyle('visibility', 'visible');
        },
        stopSearch: function() {
            searching = false;
            searchIndicator.setStyle('visibility','hidden');
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
            
            searchSourceInput.set('value',source);
        },
        setActiveTab: function(elm) {
            var alreadyActive = elm.hasClass('active');
            elm.get('parentNode').get('children').removeClass('active');
            elm.addClass('active');
            LANE.search.setSearchSource(elm.get('id') + '-all');
            // if this is not already active tab and there's a form value, submit search
            if (!alreadyActive && searchTermsInput.get('value') && searchTermsInput.get('value') != initialText) {
                LANE.search.submitSearch();
                form.submit();
                return false;
            }
            LANE.search.setInitialText();
            if (elm.get('id') == 'clinical') {
                Y.one('#search').addClass('clinicalSearch');
                //Y.one('#breadcrumb').addClass('clinicalSearch');
            } else {
                Y.one('#search').removeClass('clinicalSearch');
                //Y.one('#breadcrumb').removeClass('clinicalSearch');
            }
        },
        setInitialText: setInitialText,
//        setInitialText: function() {
//            var oldInitialText = initialText;
//            initialText = Y.one('#searchTabs').one('.active').get('title');
//            if (!searchTermsInput.get('value') || searchTermsInput.get('value') == oldInitialText) {
//                searchTermsInput.set('value', initialText);
//                searchTermsInput.set('title', initialText);
//            }
//        },
        submitSearch: function() {
            //                var i;
            // strip PICO values if not set
            //                for (i = 0; i < picoInputs.length; i++) {
            //                    if (form[picoInputs[i]] && form[picoInputs[i]].value == form[picoInputs[i]].title) {
            //                        form[picoInputs[i]].parentNode.removeChild(form[picoInputs[i]]);
            //                    }
            //                }
            // hide q input so form doesn't bounce
            //                searchTermsInput.style.visibility = 'hidden';
            if (!searchTermsInput.get('value') || searchTermsInput.get('value') == initialText) {
                throw ('nothing to search for');
            }
            LANE.search.startSearch();
            //                    LANE.suggest.collapse();
            form.submit();
        }
    };
}();

        Y.publish('lane:searchready', {broadcast: 2});
        Y.fire('lane:searchready');
});
});
