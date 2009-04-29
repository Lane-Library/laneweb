LANE.search = LANE.search ||  function() {
    var d = document,
    getSourceFromTab = function(tab) {
        var path;
        if (tab.id) {
            return tab.id.substring(0,tab.id.indexOf('SearchTab'));
        }
        path = tab.getElementsByTagName('A')[0].pathname;
        if (path.indexOf('/') !== 0) {
            path = '/' + path;
        }
        return path.substring(path.indexOf('/portals'));
    },
    getTabForSource = function(source) {
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
    getActiveSearchTab = function() {
        var tabs = d.getElementById('searchTabs').getElementsByTagName('LI'), i;
        for (i = 0; i < tabs.length; i++) {
            if (tabs[i].className == 'activeSearchTab') {
                return tabs[i];
            }
        }
        return null;
    },
/*
    togglePico = function(source) {
        if (source == 'clinical') {
            pico.style.display = 'block';
        } else {
            pico.style.display = 'none';
        }
    },
*/
/*
        pico, //the pico fieldset
*/
        form, //the form Element
        submit, //the submit input
        label, //the label span for substituting text
        terms, //the search terms input
        Event = YAHOO.util.Event, //shorthand for Event
        searching, //searching state
        searchString,
        encodedString,
        source,
        indicator,
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
                indicator.style.visibility = 'visible';
            },
            stopSearch: function(){
                searching = false;
                indicator.style.visibility = 'hidden';
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
                return source.value;
            },
            setSearchSource: function(s) {
                var searchTab = getActiveSearchTab();
                if (searchTab) {
                    searchTab.className = '';
                }
                searchTab = getTabForSource(s);
                if (searchTab) {
                    searchTab.className = 'activeSearchTab';
                }
                label.innerHTML = sourceNameMap[s];
/*
                togglePico(s);
*/
                source.value = s;
            }
        };
    // initialize when search form content ready
    Event.onContentReady('search',function(){
          form = this;
          label = d.getElementById('searchLabel');
            submit = d.getElementById('searchSubmit');
            indicator = d.getElementById('searchIndicator');
            source = d.getElementById('searchSource');
            terms = d.getElementById('searchTerms');
/*
            pico = d.getElementById('pico');
*/
            //change submit button image mouseover/mouseout
            submit.activate = function(e){
                this.src = this.src.replace('search_btn.gif', 'search_btn_f2.gif');
            };
            submit.deactivate = function(e){
                this.src = this.src.replace('search_btn_f2.gif', 'search_btn.gif');
            };
            searching = false;
            var i, tabs = form.getElementsByTagName('LI');


            for (i = 0; i < tabs.length; i++) {
                if (tabs[i].getElementsByTagName('A').length == 1) {
                    tabs[i].clicked = function(event){
                        lanesearch.setSearchSource(getSourceFromTab(this));
                        if (event.target) {
                            event.target.blur();
                        } else {
                            event.srcElement.blur();
                        }
                        Event.preventDefault(event);
                    };
                }
            }
            Event.addListener(form, 'submit', function(e){
                try {
                    lanesearch.startSearch();
                } catch (ex) {
                    alert(ex);
                    Event.preventDefault(e);
                }
            });
        }
    );
    return lanesearch;
}();