//TODO: this needs some cleaning up
//check if there is a query
(function() {
    if (LANE.SearchResult.getEncodedSearchTerms()) {
        //check if there is id=queryMapping
        var Y = LANE.Y,
            queryMapping = Y.one('#queryMapping');
        if (queryMapping) {
            Y.io('/././apps/querymap/json?q=' + LANE.SearchResult.getEncodedSearchTerms(), {
                on:{
                success: function(id, o) {
                    var anchor, span, img, i;
                    LANE.search.querymap = Y.JSON.parse(o.responseText);
                    if (LANE.search.querymap.resourceMap) {
                        LANE.search.querymap.getResultCounts = function() {
                            var url = '/././apps/search/json?q=' + LANE.SearchResult.getEncodedSearchTerms(), i;
                            for (i = 0; i < LANE.search.querymap.resourceMap.resources.length; i++) {
                                if (!LANE.search.querymap.resourceMap.resources[i].status) {
                                    url += '&r=' + LANE.search.querymap.resourceMap.resources[i].id;
                                }
                            }
                            url += '&rd=' + Math.random();
                            Y.io(url, {
                                on:{
                                    success: function(id, o) {
                                            var results = Y.JSON.parse(o.responseText),
                                                rs = LANE.search.querymap.resourceMap.resources,
                                                i, needMore = false, result;
                                            for (i = 0; i < rs.length; i++) {
                                                if (!rs[i].status) {
                                                    result = results.resources[rs[i].id];
                                                    if (result !== undefined && result.url) {
                                                        rs[i].anchor.href = result.url;
                                                    }
                                                    if (result === undefined || !result.status) {
                                                        needMore = (results.status != 'successful');
                                                    }
                                                    else{
                                                        rs[i].status = result.status;
                                                        if (result.status == 'successful') {
                                                            rs[i].anchor.parentNode.appendChild(document.createTextNode(': ' + result.hits + ' '));
                                                        }
                                                    }
                                                }
                                            }
                                            if (needMore) {
                                                setTimeout(LANE.search.querymap.getResultCounts, 2000);
                                            }
                                            //queryMapping.style.display = 'inline';
                                            Y.fire('lane:popin', queryMapping);
                                        }
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
                                queryMapping.append(span);
                                LANE.search.querymap.resourceMap.resources[i].anchor = anchor;
                            }
                            if (document.getElementById('queryMappingDescriptor')) {
                                document.getElementById('queryMappingDescriptor').appendChild(document.createTextNode(LANE.search.querymap.resourceMap.descriptor));
                            }
                            // track mapped term and descriptor
                            img = document.createElement('img');
                            img.style.display = "none";
                            img.src = "/././resources/images/spacer.gif?log=QM&d=" + LANE.search.querymap.resourceMap.descriptor + "&k=" + LANE.SearchResult.getEncodedSearchTerms();
                            queryMapping.append(img);
                            
                            LANE.search.querymap.getResultCounts();
                        }
                    }
                }
            });
        }
    }
})();

