//TODO: this needs some cleaning up
//check if there is a query
(function() {
	
	var Model = Y.lane.Model,
	    query = Model.get(Model.QUERY),
	    basePath = Model.get(Model.BASE_PATH) || "",
	    encodedQuery = Model.get(Model.URL_ENCODED_QUERY),
	    queryMapping = Y.one('#queryMapping');
        if (query && queryMapping) {
            Y.io(basePath + '/apps/querymap/json?q=' + encodedQuery, {
                on:{
                success: function(id, o) {
                    var anchor, span, i,
                        queryMap = Y.JSON.parse(o.responseText);
                    if (queryMap.resourceMap) {
                        queryMap.getResourcesString = function() {
                            var i, separator = "; ", string = "";
                            for (i = 0; i < queryMap.resourceMap.resources.length; i++) {
                                string += queryMap.resourceMap.resources[i].label;
                                if(i != queryMap.resourceMap.resources.length-1){
                                    string += separator;
                                }
                            }
                            return string;
                        };
                        queryMap.getResultCounts = function() {
                            var url = basePath + '/apps/search/json?q=' + encodedQuery, i;
                            for (i = 0; i < queryMap.resourceMap.resources.length; i++) {
                                if (!queryMap.resourceMap.resources[i].status) {
                                    url += '&r=' + queryMap.resourceMap.resources[i].id;
                                }
                            }
                            url += '&rd=' + Math.random();
                            Y.io(url, {
                                on:{
                                    success: function(id, o) {
                                            var results = Y.JSON.parse(o.responseText),
                                                rs = queryMap.resourceMap.resources,
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
                                                setTimeout(queryMap.getResultCounts, 2000);
                                            }
                                            //queryMapping.style.display = 'inline';
                                            Y.fire('lane:popin', queryMapping);
                                        }
                                    }
                                });
                            };
                            for (i = 0; i < queryMap.resourceMap.resources.length; i++) {
                                queryMap.resourceMap.resources[i].status = '';
                                span = document.createElement('span');
                                anchor = document.createElement('a');
                                anchor.title = 'QueryMapping: ' + queryMap.resourceMap.resources[i].label;
                                span.appendChild(anchor);
                                anchor.appendChild(document.createTextNode(queryMap.resourceMap.resources[i].label));
                                queryMapping.append(span);
                                queryMap.resourceMap.resources[i].anchor = anchor;
                            }
                            if (document.getElementById('queryMappingDescriptor')) {
                                document.getElementById('queryMappingDescriptor').appendChild(document.createTextNode(queryMap.resourceMap.descriptor));
                            }
                            queryMap.getResultCounts();
                            // track mapped term, descriptor, and resources
                            Y.fire("lane:trackableEvent", {
                                category: "lane:queryMapping",
                                action: "query=" + query + "; descriptor=" + queryMap.resourceMap.descriptor.descriptorName,
                                label: "resources=" + queryMap.getResourcesString()
                            });
                        }
                    }
                }
            });
        }
})();

