//TODO: this needs some cleaning up
//check if there is a query
if (LANE.search.getEncodedSearchString()) {
    //check if there is id=queryMapping
    YAHOO.util.Event.onAvailable('queryMapping', function(){
        YAHOO.util.Connect.asyncRequest('GET', '/././apps/querymap/json?q=' + LANE.search.getEncodedSearchString(), {
            success: function(o){
                var anchor, span, img, i, queryMapContainer = document.getElementById('queryMapping');
                LANE.search.querymap = YAHOO.lang.JSON.parse(o.responseText);
                if (LANE.search.querymap.resourceMap) {
                    LANE.search.querymap.getResultCounts = function(){
                        var url = '/././apps/search/proxy/json?q=' + LANE.search.getEncodedSearchString(), i;
                        for (i = 0; i < LANE.search.querymap.resourceMap.resources.length; i++) {
                            if (!LANE.search.querymap.resourceMap.resources[i].status) {
                                url += '&r=' + LANE.search.querymap.resourceMap.resources[i].id;
                            }
                        }
                        url += '&rd=' + Math.random();
                        YAHOO.util.Connect.asyncRequest('GET', url, {
                            success: function(o){
                                var results = YAHOO.lang.JSON.parse(o.responseText), rs = LANE.search.querymap.resourceMap.resources, i, needMore = false, result;
                                for (i = 0; i < rs.length; i++) {
                                    if (!rs[i].status) {
                                        result = results.resources[rs[i].id];
                                        if (!result.status) {
                                            needMore = true;
                                        }
                                        rs[i].status = result.status;
                                        if (result.status == 'successful') {
                                            rs[i].anchor.parentNode.appendChild(document.createTextNode(': ' + result.hits + ' '));
                                        }
                                        rs[i].anchor.href = result.url;
                                    }
                                }
                                if (needMore) {
                                    setTimeout(LANE.search.querymap.getResultCounts, 2000);
                                }
                                //queryMapContainer.style.display = 'inline';
                                LANE.search.popin.fire(queryMapContainer);
                            }
                        });
                    };
                    for (i = 0; i < LANE.search.querymap.resourceMap.resources.length; i++) {
                        LANE.search.querymap.resourceMap.resources[i].status = '';
                        span = document.createElement('span');
                        anchor = document.createElement('a');
                        anchor.title = 'QueryMapping: ' + LANE.search.querymap.resourceMap.resources[i].label;
                        span.appendChild(anchor);
                        anchor.appendChild(document.createTextNode(LANE.search.querymap.resourceMap.resources[i].label));
                        queryMapContainer.appendChild(span);
                        LANE.search.querymap.resourceMap.resources[i].anchor = anchor;
                    }
                    if (document.getElementById('queryMappingDescriptor')) {
                        document.getElementById('queryMappingDescriptor').appendChild(document.createTextNode(LANE.search.querymap.resourceMap.descriptor));
                    }
                    // track mapped term and descriptor
                    img = document.createElement('img');
                    img.style.display = "none";
                    img.src = "/././graphics/spacer.gif?log=QM&d=" + LANE.search.querymap.resourceMap.descriptor + "&k=" + LANE.search.getEncodedSearchString();
                    queryMapContainer.appendChild(img);
                    
                    LANE.search.querymap.getResultCounts();
                }
            }
        });
    });
}

