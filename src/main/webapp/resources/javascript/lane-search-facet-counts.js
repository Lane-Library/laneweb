//TODO: should this stop polling when all facets are complete? currently polls until search app done or timeout
YUI().use('lane-search', 'node','io-base','json-parse',function(Y) {
    
    var searchString = LANE.search.getEncodedSearchString(),
        facets = Y.all('.searchFacet'),
        startTime = new Date().getTime(),
        requestString, j,
        makeRequest = function() {
            requestString = '';
            for (j = 0; j < facets.size(); j++) {
                var id = facets.item(j).get('id');
                facets.item(j).setAttribute('facetId', id.substring(0, id.indexOf('Facet')));
                if(facets.item(j).hasClass('searchableFacet')){
                	requestString+=facets.item(j).getAttribute('facetId') + ',';
                }
            }
            if(requestString !== ''){
                Y.io('/././apps/search/facets/json?q=' + searchString + '&f=' + requestString + '&rd=' + Math.random(), {
                    on: {
                        success:function(id, o) {
                            var response = Y.JSON.parse(o.responseText),
                                hitLink, hits, sleepingTime,
                                remainingTime, searchStatus, engineStatus, facetId;
                            
                            for (j = 0; j < facets.size(); j++) {
                                hits = null;
                                hitLink = facets.item(j).one('a');
                                facetId = facets.item(j).getAttribute('facetId');
                                if (undefined != response.results.facets[facetId]) {
                                    hits = parseInt(response.results.facets[facetId].hits);
                                    engineStatus = response.results.facets[facetId].status;
                                }
                                if (!facetId.match("-all") && engineStatus == 'successful' && hitLink !== null && hits === 0) {
                                    hitLink.get('parentNode').addClass('inactiveFacet');
                                    hitLink.get('parentNode').removeClass('searchableFacet');
                                    hitLink.set('title','no search results for '+hitLink.get('innerHTML'));
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
                        }
                    }
                });
            }
    };
    if (searchString && facets.size() > 0) {
        makeRequest();
    }
});
