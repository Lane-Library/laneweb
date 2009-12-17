LANE.search = LANE.search ||  function() {
    var d = document,
        form, //the form Element
        Event = YAHOO.util.Event, //shorthand for Event
        Dom = YAHOO.util.Dom, //shorthand for Dom
        searching = false, //searching state
        searchString,
        encodedString,
        source,
        ind = d.getElementById('searchIndicator'),
        initialText,
        // publicly available functions:
        o = {
            startSearch: function(){
                //TODO: revisit this, do we want to prevent a new search when one in progress?
//                if (searching) {
//                    throw('already searching');
//                }
                if (!form.q.value || form.q.value == initialText) {
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
            	Dom.getElementsByClassName('active',null,elm.parentNode,function(el){
            		Dom.removeClass(el,'active');
            	});
        		Dom.addClass(elm,'active');
                o.setSearchSource(elm.id+'-all');
            	if(form.q.value && form.q.value !== initialText){
            		o.startSearch();
            		form.submit();
            	}
            	o.setInitialText();
            },
            setInitialText: function(){
            	var oldInitialText = initialText;
            	YAHOO.util.Dom.getElementsByClassName('active',null,form,function(el){
            		if(el.title){
            			initialText = el.title;
            		}
            	});
    	        if (!form.q.value || form.q.value == oldInitialText){
    	        	form.q.value = initialText;
    	        	form.q.title = initialText;
    	        }
            }
        };

    Event.onContentReady('search',function(){
        form = this;
        o.setInitialText();
            Event.addListener(form, 'submit', function(e){
                try {
                    o.startSearch();
                } catch (ex) {
                    alert(ex);
                    Event.preventDefault(e);
                }
            });
            Event.addFocusListener(form.q, function(e) {
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