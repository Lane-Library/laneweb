//TODO: should this stop polling when all facets are complete? currently polls until search app done or timeout
(function() {
    var searchString = LANE.search.getEncodedSearchString(),
        startTime = new Date().getTime(),
        facets, hitSpan, j,
        updateHits = function(o) {
            var response = YAHOO.lang.JSON.parse(o.responseText),
                hitSpan, hitLink, hits, sleepingTime,
                remainingTime, searchStatus, engineStatus, animation;
            
            for (j = 0; j < facets.length; j++) {
            	hits = null;
                hitSpan = document.getElementById(facets[j].facetId + 'HitSpan');
                hitLink = facets[j].getElementsByTagName('a')[0];
                if (undefined != response.results.facets[facets[j].facetId]) {
                    hits = response.results.facets[facets[j].facetId].hits;
                    engineStatus = response.results.facets[facets[j].facetId].status;
                }
                if (hitSpan !== null && hits !== 0 && hits !== '') {
                    hitSpan.innerHTML = hits;
                }
                // playing with fading animation ... fade when NOT xxx-all facet and zero hits
                if (!facets[j].facetId.match("-all") && engineStatus == 'successful' && hitLink !== null && hits == 0) {
                    animation = new YAHOO.util.ColorAnim(hitLink, {color: { to: '#C1C1C1' } });
                    animation.onComplete.subscribe(function() {this.getEl().parentNode.style.backgroundImage = 'none'});
                    animation.animate();
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
            for (j = 0; j < facets.length; j++) {
                facets[j].facetId = facets[j].id.substring(0, facets[j].id.indexOf('Facet'));
                hitSpan = document.getElementById(facets[j].facetId + 'HitCount');
                YAHOO.util.Dom.addClass(hitSpan,'searching');
            }
            YAHOO.util.Connect.asyncRequest('GET', '/././apps/search/facets/json?q=' + searchString + '&rd=' + Math.random(), responseHandler);
    };
    if (searchString) {
        YAHOO.util.Event.onAvailable('searchFacets', makeRequest);
    }
})();