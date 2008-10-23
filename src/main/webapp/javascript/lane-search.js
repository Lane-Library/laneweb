LANE.search = LANE.search ||  function() {
    var d = document,
        form, //the form Element
        submit, //the submit input
        select, //the select Element
        selected, //the selected option
        E = YAHOO.util.Event, //shorthand for Event
        searching = false, //searching state
        searchString,
        encodedString,
        source,
        ind = document.getElementById('searchIndicator'),
        // publicly available functions:
        o = {
            startSearch: function(){
                //TODO: revisit this, do we want to prevent a new search when one in progress?
//                if (searching) {
//                    throw('already searching');
//                }
                if (!form.q.value) {
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
                        source = '';
                    }
                }
                return source;
            },
            setSearchType: function(type) {
                for (var i = 0; i < select.options.length; i++) {
                    if (select.options[i].value == type) {
                        select.selectedIndex = i;
                        break;
                    }
                }
            }
        };
    // initialize on load
    E.addListener(this,'load',function(){
        form = d.getElementById('searchForm');
        if (form) {
            submit = d.getElementById('searchSubmit');
            select = d.getElementById('searchSelect');
            selected = select.options[select.selectedIndex];
            //change submit button image mouseover/mouseout
            submit.activate = function(e){
                this.src = this.src.replace('search_btn.gif', 'search_btn_f2.gif');
            };
            submit.deactivate = function(e){
                this.src = this.src.replace('search_btn_f2.gif', 'search_btn.gif');
            };
            E.addListener(form, 'submit', function(e){
                try {
                    o.startSearch();
                } catch (ex) {
                    alert(ex);
                    E.preventDefault(e);
                }
            });
            E.addListener(select, 'change', function(){
                if (this.options[this.selectedIndex].disabled) {
                    this.selectedIndex = selected.index;
                } else {
                    selected = this.options[this.selectedIndex];
                }
            });
        }
    });
    return o;
}();