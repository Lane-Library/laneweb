//TODO: should this stop polling when all facets are complete? currently polls until search app done or timeout
(function() {
    var searchString = LANE.search.getEncodedSearchString(),
        startTime = new Date().getTime(),
        facets, requestString, j,
        updateHits = function(o) {
            var response = YAHOO.lang.JSON.parse(o.responseText),
                hitLink, hits, sleepingTime,
                remainingTime, searchStatus, engineStatus;
            
            for (j = 0; j < facets.length; j++) {
            	hits = null;
                hitLink = facets[j].getElementsByTagName('a')[0];
                if (undefined != response.results.facets[facets[j].facetId]) {
                    hits = response.results.facets[facets[j].facetId].hits;
                    engineStatus = response.results.facets[facets[j].facetId].status;
                }
                if (!facets[j].facetId.match("-all") && engineStatus == 'successful' && hitLink !== null && hits === 0) {
                	YAHOO.util.Dom.addClass(hitLink.parentNode,'inactiveFacet');
                	YAHOO.util.Dom.removeClass(hitLink.parentNode,'searchableFacet');
                	hitLink.setAttribute('title','no search results for '+hitLink.innerHTML);
                }
            }
            sleepingTime = 2000;
            remainingTime = (new Date().getTime()) - startTime;
            searchStatus = response.results.status;
            if (searchStatus != 'successful' && (remainingTime <= 60 * 1000)) { // at more than 20 seconds the sleeping time becomes 10 seconds
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
            facets = YAHOO.util.Dom.getElementsByClassName('searchFacet');
            requestString = '';
            for (j = 0; j < facets.length; j++) {
                facets[j].facetId = facets[j].id.substring(0, facets[j].id.indexOf('Facet'));
                if(YAHOO.util.Dom.hasClass(facets[j],'searchableFacet')){
                	requestString+=facets[j].facetId+',';
                }
            }
            if(requestString !== ''){
            	YAHOO.util.Connect.asyncRequest('GET', '/././apps/search/facets/json?q=' + searchString + '&f=' + requestString + '&rd=' + Math.random(), responseHandler);
            }
    };
    if (searchString) {
        YAHOO.util.Event.onAvailable('searchFacets', makeRequest);
    }
})();