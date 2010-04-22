YUI().use('yui2-event','yui2-dom',function(Y) {
LANE.search = LANE.search ||
function() {
    var Event = Y.YUI2.util.Event, //shorthand for Event
        Dom = Y.YUI2.util.Dom, //shorthand for Dom
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
    // move out of search JS
    Event.onContentReady('search', function() {
    	var textInputs, i,
        YD = Y.YUI2.util.Dom,
        YE = Y.YUI2.util.Event,
        filterFormTextInputs = function(el){
        	if(el.tagName == "INPUT" && el.type == "text"){
        		return true;
        	}
        };
        textInputs = YD.getElementsBy(filterFormTextInputs,"input",document);
	    for (i = 0; i < textInputs.length; i++){
	    	// clear input if it matches title (help text) value
	    	YE.addListener(textInputs[i], 'focus', function(){
	    	    if (this.value == this.title){
	    	        this.value = '';
	    	    }
	    	});
	    	// if input value is blank, set to title (help text)
	    	YE.addListener(textInputs[i], 'blur', function(){
	    	    if (this.value === ''){
	    	        this.value = this.title;
	    	    }
	    	});
	    }
    });
    
    Event.onContentReady('search', function() {
        form = this;
        searchTermsInput = document.getElementById('searchTerms');
        searchSourceInput = document.getElementById('searchSource');
        LANE.search.setInitialText();
        Event.addListener(form, 'submit', function(submitEvent) {
            Event.preventDefault(submitEvent);
            try {
                LANE.search.submitSearch();
            } catch (e) {
                alert(e);
            }
        });
        var tabs = document.getElementById('searchTabs').getElementsByTagName('li'), i;
        for (i = 0; i < tabs.length; i++) {
            tabs[i].clicked = function(e) {
                Event.preventDefault(e);
                LANE.search.setActiveTab(this);
            };
        }
    });
    
    Event.onContentReady('searchIndicator', function(){
        searchIndicator = this;
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
            searchIndicator.style.visibility = 'visible';
        },
        stopSearch: function() {
            searching = false;
            searchIndicator.style.visibility = 'hidden';
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
            searchSourceInput.value = source;
        },
        setActiveTab: function(elm) {
            var alreadyActive = Dom.hasClass(elm, 'active');
            Dom.getElementsByClassName('active', null, elm.parentNode, function(el) {
                Dom.removeClass(el, 'active');
            });
            Dom.addClass(elm, 'active');
            LANE.search.setSearchSource(elm.id + '-all');
            // if this is not already active tab and there's a form value, submit search
            if (!alreadyActive && searchTermsInput.value && searchTermsInput.value !== initialText) {
                LANE.search.submitSearch();
                form.submit();
                return false;
            }
            LANE.search.setInitialText();
            if (elm.id == 'clinical') {
                Dom.addClass(['search', 'breadcrumb'], 'clinicalSearch');
            } else {
                Dom.removeClass(['search', 'breadcrumb'], 'clinicalSearch');
            }
        },
        setInitialText: function() {
            var oldInitialText = initialText;
            initialText = Y.YUI2.util.Dom.getElementsByClassName('active', 'LI', 'searchTabs')[0].title;
            if (!searchTermsInput.value || searchTermsInput.value == oldInitialText) {
                searchTermsInput.value = initialText;
                searchTermsInput.title = initialText;
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
            if (!searchTermsInput.value || searchTermsInput.value == initialText) {
                throw ('nothing to search for');
            }
            LANE.search.startSearch();
            //                    LANE.suggest.collapse();
            form.submit();
        }
    };
}();
});
