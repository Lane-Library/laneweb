(function() {

    LANE.namespace('search.metasearch');
    LANE.search.metasearch = function() {
        var searchElms, // the elements in need of hit counts
            searchables = [], // all engines to search
            searchRequests = [], // search timerIds so we can abort sleeping getResultCounts
            uberEngines = ['cro_', 'mdc_', 'ovid-'], // engines with multiple resources
            startTime,
            getSearchUrl = function() {
                var i, y, searchUrl = '/././apps/search/json?q=' + LANE.search.getEncodedSearchString();
                for (y = 0; y < searchables.length; y++) {
                    var add = true;
                    for (i = 0; i < uberEngines.length; i++) {
                        // don't add if: 
                        // - engine is uber and uber already on url
                        // - engine already on url
                        if (searchables[y].match(uberEngines[i]) && searchUrl.match(uberEngines[i])) {
                            add = false;
                        } else if (searchUrl.match("r=" + searchables[y] + "(&|$)")) {
                            add = false;
                        }
                    }
                    if (add) {
                        searchUrl += '&r=' + searchables[y];
                    }
                }
                searchUrl += '&rd=' + Math.random();
                return searchUrl;
            };
        return {
            initialize: function() {
                var i;
                searchElms = YAHOO.util.Dom.getElementsByClassName("metasearch");
                for (i = 0; i < searchElms.length; i++) {
                    if (searchables.indexOf(searchElms[i].id) == -1) {
                        searchables.push(searchElms[i].id);
                    }
                }
                for (i = 0; i < searchRequests.length; i++) {
                    clearTimeout(searchRequests[i]);
                    searchRequests.splice(i, 1);
                }
                startTime = new Date().getTime();
            },
            getResultCounts: function() {
                YAHOO.util.Connect.asyncRequest('GET', getSearchUrl(), {
                    success: function(o) {
                        var response = YAHOO.lang.JSON.parse(o.responseText),
                            results = response.resources,
                            needMore = false, 
                            i,result, updateable, resultSpan, sleepingTime, remainingTime;
                        
                        for (i = 0; i < searchables.length; i++) {
                            updateable = document.getElementById(searchables[i]);
                            result = results[searchables[i]];
                            if (result === undefined || !result.status) {
                                needMore = true;
                                continue;
                            } else if (updateable && result.status == 'successful') {
                                // process display of each updateable node
                                // once all processed, remove id from searchables
                                resultSpan = YAHOO.util.Dom.getElementsByClassName('searchCount', 'span', updateable.parentNode)[0];
                                resultSpan.innerHTML = '&#160;' +
                                YAHOO.util.Number.format(result.hits, {
                                    thousandsSeparator: ","
                                });
                                if (!updateable.href) {
                                    updateable.setAttribute('href', result.url);
                                    updateable.setAttribute('target', '_blank');
                                }
                                YAHOO.util.Dom.removeClass(updateable, 'metasearch');
                                searchables.splice(i, 1);
                            } else if (updateable && (result.status == 'failed' || result.status == 'canceled')) {
                                resultSpan = YAHOO.util.Dom.getElementsByClassName('searchCount', 'span', updateable.parentNode)[0];
                                resultSpan.innerHTML = ' ? ';
                                YAHOO.util.Dom.removeClass(updateable, 'metasearch');
                                searchables.splice(i, 1);
                            }
                        }
                        sleepingTime = 2000;
                        remainingTime = (new Date().getTime()) - startTime;
                        if (response.status != 'successful' &&
                        needMore &&
                        searchables.length > 0 &&
                        (remainingTime <= 60 * 1000)) { // at more than 20 seconds the sleeping time becomes 10 seconds
                            if (remainingTime > 20 * 1000) {
                                sleepingTime = 10000;
                            }
                            searchRequests.push(setTimeout(LANE.search.metasearch.getResultCounts, sleepingTime));
                        } else {
                            LANE.search.stopSearch();
                        }
                    }// end request success definition
                });// end async request
            }// end getResultCounts
        };
    }();
    
    YAHOO.util.Event.onDOMReady(function() {
    
        // check for presence of search term and metasearch classNames
        if (LANE.search.getEncodedSearchString() && YAHOO.util.Dom.getElementsByClassName('metasearch').length > 0) {
            LANE.search.metasearch.initialize();
            LANE.search.metasearch.getResultCounts();
            LANE.search.startSearch();
        }
        
    });//end onDOMReady
})();
