YUI().use('yui2-event','node',function(Y) {
LANE.search = LANE.search ||
function() {
    var Event = Y.YUI2.util.Event, //shorthand for Event
        searching = false, //searching state
        searchString,
        encodedString,
        form, //the form Element
        searchTermsInput,
        searchSourceInput,
        source,
        searchIndicator,
        initialText,
        picoInputs = ['p', 'i', 'c', 'o'];
    
    // TODO: since this acts on all text inputs w/ initial input values
    // move out of search JS - for now I just made it only work on the search form.
    Event.onContentReady('search', function() {
    	var textInputs, i,
        YE = Y.YUI2.util.Event,
        filterFormTextInputs = function(el){
        	if(el.tagName == "INPUT" && el.type == "text"){
        		return true;
        	}
        };
        textInputs = new Y.Node(this).all('input[type="text"]');
	    for (i = 0; i < textInputs.size(); i++){
	    	// clear input if it matches title (help text) value
	    	YE.addListener(Y.Node.getDOMNode(textInputs.item(i)), 'focus', function(){
	    	    if (this.value == this.title){
	    	        this.value = '';
	    	    }
	    	});
	    	// if input value is blank, set to title (help text)
	    	YE.addListener(Y.Node.getDOMNode(textInputs.item(i)), 'blur', function(){
	    	    if (this.value === ''){
	    	        this.value = this.title;
	    	    }
	    	});
	    }
    });
    
    Event.onContentReady('search', function() {
        form = new Y.Node(this);
        searchTermsInput = Y.one('#searchTerms');
        searchSourceInput = Y.one('#searchSource');
        LANE.search.setInitialText();
        Event.addListener(this, 'submit', function(submitEvent) {
            Event.preventDefault(submitEvent);
            try {
                LANE.search.submitSearch();
            } catch (e) {
                alert(e);
            }
        });
        var tabs = Y.one('#searchTabs').all('li'), i;
        for (i = 0; i < tabs.size(); i++) {
            Y.Node.getDOMNode(tabs.item(i)).clicked = function(e) {
                Event.preventDefault(e);
                LANE.search.setActiveTab(this);
            };
        }
    });
    
    Event.onContentReady('searchIndicator', function(){
        searchIndicator = new Y.Node(this);
    });
    
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
            
            searchSourceInput.setAttribute('value',source);
        },
        setActiveTab: function(elm) {
            var alreadyActive = elm.hasClass('active');
            elm.get('parentNode').get('children').removeClass('active');
            elm.addClass('active');
            LANE.search.setSearchSource(elm.getAttribute('id') + '-all');
            // if this is not already active tab and there's a form value, submit search
            if (!alreadyActive && searchTermsInput.hasAttribute('value') && searchTermsInput.getAttribute('value') != initialText) {
                LANE.search.submitSearch();
                form.submit();
                return false;
            }
            LANE.search.setInitialText();
            if (elm.getAttribute('id') == 'clinical') {
                Y.one('#search').addClass('clinicalSearch');
                Y.one('#breadcrumb').addClass('clinicalSearch');
            } else {
                Y.one('#search').removeClass('clinicalSearch');
                Y.one('#breadcrumb').removeClass('clinicalSearch');
            }
        },
        setInitialText: function() {
            var oldInitialText = initialText;
            initialText = Y.one('#searchTabs').one('.active').getAttribute('title');
            if (!searchTermsInput.hasAttribute('value') || searchTermsInput.getAttribute('value') == oldInitialText) {
                searchTermsInput.setAttribute('value', initialText);
                searchTermsInput.setAttribute('title', initialText);
            }
        },
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
            if (!searchTermsInput.hasAttribute('value') || searchTermsInput.getAttribute('value') == initialText) {
                throw ('nothing to search for');
            }
            LANE.search.startSearch();
            //                    LANE.suggest.collapse();
            form.submit();
        }
    };
}();
});
