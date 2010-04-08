LANE.search = LANE.search ||  function() {
    var d = document,
        form, //the form Element
        Event = YAHOO.util.Event, //shorthand for Event
        Dom = YAHOO.util.Dom, //shorthand for Dom
        searching = false, //searching state
        searchString,
        encodedString,
        searchTermsInput,
        source,
        ind = d.getElementById('searchIndicator'),
        initialText,
        picoInputs = ['p','i','c','o'],
        // publicly available functions:
        o = {
            startSearch: function(){
                //TODO: revisit this, do we want to prevent a new search when one in progress?
//                if (searching) {
//                    throw('already searching');
//                }
                if (!searchTermsInput.value || searchTermsInput.value == initialText) {
                    throw('nothing to search for');
                }
                searching = true;
                ind.style.visibility = 'visible';
            },
            stopSearch: function(){
                searching = false;
                ind.style.visibility = 'hidden';
            },
            isSearching: function(){
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
                        source = d.getElementById('searchSource').value;
                    }
                    if (source === undefined) {
                        source = '';
                    }
                }
                return source;
            },
            setSearchSource: function(source) {
                d.getElementById('searchSource').value = source;
            },
            setActiveTab: function(elm){
                var alreadyActive = Dom.hasClass(elm,'active');
                Dom.getElementsByClassName('active',null,elm.parentNode,function(el){
                    Dom.removeClass(el,'active');
                });
                Dom.addClass(elm,'active');
                o.setSearchSource(elm.id+'-all');
                // if this is not already active tab and there's a form value, submit search
                if(!alreadyActive && searchTermsInput.value && searchTermsInput.value !== initialText){
                    o.submitSearch();
                    form.submit();
                    return false;
                }
                o.setInitialText();
                if(elm.id == 'clinical'){
                    Dom.addClass(['search','breadcrumb'], 'clinicalSearch');
                }
                else{
                    Dom.removeClass(['search','breadcrumb'], 'clinicalSearch');
                }
            },
            setInitialText: function(){
                var oldInitialText = initialText;
                initialText = activeTab = YAHOO.util.Dom.getElementsByClassName('active','LI','searchTabs')[0].title;
                if (!searchTermsInput.value || searchTermsInput.value == oldInitialText){
                    searchTermsInput.value = initialText;
                }
            },
            submitSearch: function(e){
                var i;
                try {
                    // strip PICO values if not set
                    for (i = 0; i < picoInputs.length; i++){
                        if (form[picoInputs[i]] && form[picoInputs[i]].value == form[picoInputs[i]].title) {
                            form[picoInputs[i]].parentNode.removeChild(form[picoInputs[i]]);
                        }
                    }
                    // hide q input so form doesn't bounce
                    searchTermsInput.style.visibility = 'hidden';
                    o.startSearch();
//                    LANE.suggest.collapse();
                    //form.submit();
                } catch (ex) {
                    alert(ex);
                    Event.preventDefault(e);
                }
            }
        };

    Event.onContentReady('searchForm',function(){
        form = this;
        searchTermsInput = document.getElementById('searchTerms');
        o.setInitialText();
        Event.addListener(form, 'submit', o.submitSearch);
        Event.addFocusListener(searchTermsInput, function(e) {
            if (this.value == initialText) {
                this.value = '';
            }
        });
    });

    Event.onContentReady('searchTabs',function(){
        var tabs = this.getElementsByTagName('li'), i;
        for (i = 0; i < tabs.length; i++){
           tabs[i].clicked = function(e){
                Event.preventDefault(e);
                try {
                    o.setActiveTab(this);
                } catch (ex) {
                    alert(ex);
                }
            };
        }
    });
    return o;
}();