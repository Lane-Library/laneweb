(function() {
    var searchString = LANE.search.getEncodedSearchString(),
        startTime = new Date().getTime(),
        updateHits = function(o) {
            var response = YAHOO.lang.JSON.parse(o.responseText),
                j, tabName, tab, hitSpan, hits, sleepingTime,
                remainingTime, status;
            for (j = 0; j < response.results.tabs.length; j++) {
                tabName = response.results.tabs[j].resource;
                tab = document.getElementById(tabName + "Tab");
                if (tab !== null) {
                    hitSpan = tab.getElementsByTagName('span')[0];
                    hits = response.results.tabs[j].hits;
                    if (hitSpan !== undefined && hits !== '') {
                        hitSpan.innerHTML = hits;
                    }
                }
            }
            sleepingTime = 2000;
            remainingTime = (new Date().getTime()) - startTime;
            status = response.results.status;
            if (status != 'successful' && (remainingTime <= 60 * 1000)) { // at more than 20 seconds the sleeping time becomes 10 seconds
                if (remainingTime > 20 * 1000) {
                    sleepingTime = 10000;
                }
                setTimeout(makeRequest,sleepingTime);
            }
        },
        responseHandler = {
            success:updateHits
        },
        makeRequest = function() {
            YAHOO.util.Connect.asyncRequest('GET', '/././apps/search/tabs/json?q=' + searchString + '&rd=' + Math.random(), responseHandler);
    };
    if (searchString) {
        YAHOO.util.Event.onAvailable('eLibraryTabs', makeRequest);
    }
})();
