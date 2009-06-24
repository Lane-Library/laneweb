LANE.search = LANE.search ||  function() {
    var d = document, initialized = false,
    getSourceFromTab = function(tab){
        var path;
        if (tab.id) {
            return tab.id.substring(0, tab.id.indexOf('SearchTab'));
        }
        path = tab.getElementsByTagName('A')[0].pathname;
        if (path.indexOf('/') !== 0) {
            path = '/' + path;
        }
        return path.substring(path.indexOf('/portals'));
    },
    getTabForSource = function(source){
        var links, path, i, tabs = d.getElementById('searchTabs').getElementsByTagName('LI');
        for (i = 0; i < tabs.length; i++) {
            if (tabs[i].id && tabs[i].id == source + 'SearchTab') {
                return tabs[i];
            }
            links = tabs[i].getElementsByTagName('A');
            if (links.length == 1) {
                path = links[0].pathname;
                if (path.indexOf('/') !== 0) {
                    path = '/' + path;
                }
                if (path.indexOf(source) > -1) {
                    return tabs[i];
                }
            }
        }
    },
    switchActiveTab = function(s){
        var i, tabs = Dom.getElementsByClassName('activeSearchTab', 'LI', searchTabs);
        for (i = 0; i < tabs.length; i++) {
            Dom.removeClass(tabs[i], 'activeSearchTab');
        }
        Dom.addClass(getTabForSource(s), 'activeSearchTab');
    },
/*
    togglePico = function(s) {
        if (pico && s == '/portals/lpch-cerner.html') {
            Dom.setStyle(pico, 'display', 'block');
        } else {
            Dom.setStyle(pico, 'display', 'none');
        }
    },
        pico, //the pico fieldset
*/
        form, //the form Element
        submit, //the submit input
        label, //the label span for substituting text
        terms, //the search terms input
        Event = YAHOO.util.Event, //shorthand for Event
        Dom = YAHOO.util.Dom,
        searching, //searching state
        searchString,
        encodedString,
        source,
        indicator,
        //the ul containing the search tabs
        searchTabs,
        //mapping between source and label, needs to by kept in sync with laneweb.xsl
        sourceNameMap = {
        'ej':'eJournals',
        'book':'eBooks',
        'cc':'Calculators',
        'database':'Databases',
        'software':'Software',
        'video':'Videos',
        'lanesite':'Lane Site',
        'bassett':'Bassett',
        'peds':'Pediatrics',
        'history':'History',
        'research':'Bioresearch',
        'all':'All',
        'clinical':'Clinical',
        'history':'History',
        '/portals/pharmacy.html':'Pharmacy',
        '/portals/anesthesia.html':'Anesthesia',
        '/portals/cardiology.html':'Cardiology',
        '/portals/hematology.html':'Hematology',
        '/portals/internal-medicine.html':'Internal Medicine',
        '/portals/lpch-cerner.html':'LPCH LINKS Tool',
        '/portals/pulmonary.html':'Pulmonary',
        '/portals/emergency.html':'Emergency',
        '/portals/ethics.html':'Ethics'
        },
    // initialize when search form content ready
    lazyInit = function(){
        if (!initialized) {
            form = d.getElementById('search');
            label = d.getElementById('searchLabel');
            submit = d.getElementById('searchSubmit');
            indicator = d.getElementById('searchIndicator');
            source = d.getElementById('searchSource');
            terms = d.getElementById('searchTerms');
            searchTabs = d.getElementById('searchTabs');
/*
            pico = d.getElementById('pico');
*/
            //change submit button image mouseover/mouseout
/*
            submit.activate = function(e){
                this.src = this.src.replace('search_btn.gif', 'search_btn_f2.gif');
            };
            submit.deactivate = function(e){
                this.src = this.src.replace('search_btn_f2.gif', 'search_btn.gif');
            };
*/
            searching = false;
            

            var i, href, tabs = form.getElementsByTagName('LI');
            for (i = 0; i < tabs.length; i++) {
                //if there is more than one A child it is otherSearches
                if (tabs[i].getElementsByTagName('A').length == 1) {
                    //add clicked functions to the tabs
                    tabs[i].clicked = function(event){
                        //if there has been a search and there is something in the search input
                        if (lanesearch.getSearchString() && terms.value) {
                            href = this.getElementsByTagName('A')[0].href;
                            //special handling for catalog search, proxy if necessary
                            if (this.id == 'catalogSearchTab') {
                                if (href.indexOf('laneproxy') > 0 || href.indexOf('secure/login') > 0) {
                                    href = href.substring(0, href.indexOf('url=') + 4);
                                } else {
                                    href = '';
                                }
                                href += 'http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?DB=local&SL=none&BOOL1=all+of+these&FLD1=Keyword+Anywhere++[LKEY]+(LKEY)&GRP1=AND+with+next+set&SAB2=&BOOL2=all+of+these&FLD2=ISSN+[with+hyphen]+(ISSN)&GRP2=AND+with+next+set&SAB3=&BOOL3=all+of+these&FLD3=ISSN+[with+hyphen]+(ISSN)&CNT=50&SAB1=';
                            } else {
                                href = this.getElementsByTagName('A')[0].href;
                                href = href.substring(0, href.indexOf('q=') + 2);
                            }
                            href += encodeURIComponent(terms.value);
                            document.location = href;
                            Event.preventDefault(event);
                        //if no search and one of these tabs just change source input and label
                        } else if (this.id == 'softwareSearchTab' || this.id == 'videoSearchTab' || this.id == 'lanesiteSearchTab'){
                            lanesearch.setSearchSource(getSourceFromTab(this));
                            Event.preventDefault(event);
                        }
                    };
                }
            }

            Event.addListener(form, 'submit', function(e){
                try {
                    lanesearch.startSearch();
                } 
                catch (ex) {
                    alert(ex);
                    Event.preventDefault(e);
                }
            });
            initialized = true;
/*
            togglePico(lanesearch.getSearchSource());
*/
        }
        },
        // publicly available functions:
        lanesearch = {
            startSearch: function(){
                //TODO: revisit this, do we want to prevent a new search when one in progress?
//                if (searching) {
//                    throw('already searching');
//                }
                if (!terms.value) {
                    throw('nothing to search for');
                }
                searching = true;
                Dom.setStyle(indicator,'visibility','visible');
            },
            stopSearch: function(){
                searching = false;
                Dom.setStyle(indicator,'visibility','hidden');
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
            //get the search source, maybe initialize because event timing might allow this
            //to be called before onContentReady event initializes.
            getSearchSource: function() {
                lazyInit();
                return source.value;
            },
            setSearchSource: function(s) {
                switchActiveTab(s);
                label.innerHTML = sourceNameMap[s];
/*
                togglePico(s);
*/
                source.value = s;
            }
        };
        //initialize when content ready.
        //TODO: revisit yui event scheduling issues, shouldn't have to do this but browsers differ
    Event.onContentReady('search',function(){lazyInit();});
    return lanesearch;
}();