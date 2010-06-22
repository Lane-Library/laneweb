//TODO: this needs some cleaning up
//check if there is a query
YUI().use('lane-search-result', 'node', 'event-custom', 'io-base', 'json-parse', function(Y) {
    if (Y.lane.SearchResult.getEncodedSearchTerms()) {
        //check if there is id=queryMapping
        var queryMapping = Y.one('#queryMapping');
        if (queryMapping) {
            Y.io('/././apps/querymap/json?q=' + Y.lane.SearchResult.getEncodedSearchTerms(), {
                on:{
                success: function(id, o) {
                    var anchor, span, img, i;
                    LANE.search.querymap = Y.JSON.parse(o.responseText);
                    if (LANE.search.querymap.resourceMap) {
                        LANE.search.querymap.getResultCounts = function() {
                            var url = '/././apps/search/json?q=' + Y.lane.SearchResult.getEncodedSearchTerms(), i;
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
                                                    if (result == undefined || !result.status) {
                                                        needMore = true;
                                                    }
                                                    else{
                                                        rs[i].status = result.status;
                                                        if (result.status == 'successful') {
                                                            rs[i].anchor.parentNode.appendChild(document.createTextNode(': ' + result.hits + ' '));
                                                        }
                                                        rs[i].anchor.href = result.url;
                                                    }
                                                }
                                            }
                                            if (needMore) {
                                                setTimeout(LANE.search.querymap.getResultCounts, 2000);
                                            }
                                            //queryMapping.style.display = 'inline';
                                            Y.Global.fire('lane:popin', queryMapping);
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
                                queryMapping.appendChild(span);
                                LANE.search.querymap.resourceMap.resources[i].anchor = anchor;
                            }
                            if (document.getElementById('queryMappingDescriptor')) {
                                document.getElementById('queryMappingDescriptor').appendChild(document.createTextNode(LANE.search.querymap.resourceMap.descriptor));
                            }
                            // track mapped term and descriptor
                            img = document.createElement('img');
                            img.style.display = "none";
                            img.src = "/././graphics/spacer.gif?log=QM&d=" + LANE.search.querymap.resourceMap.descriptor + "&k=" + Y.lane.SearchResult.getEncodedSearchTerms();
                            queryMapping.appendChild(img);
                            
                            LANE.search.querymap.getResultCounts();
                        }
                    }
                }
            });
        }
    }
});

