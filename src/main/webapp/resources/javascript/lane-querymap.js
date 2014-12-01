(function() {

    var Model = Y.lane.Model,
        query = Model.get(Model.QUERY),
        basePath = Model.get(Model.BASE_PATH) || "",
        encodedQuery = Model.get(Model.URL_ENCODED_QUERY),
        queryMapping = Y.one('#queryMapping'),
        getResultCounts,
        queryMapResources,
        getResourcesString = function() {
            var i, separator = "; ", string = "";
            for (i = 0; i < queryMapResources.length; i++) {
                string += queryMapResources[i].label;
                if(i !== queryMapResources.length-1){
                    string += separator;
                }
            }
            return string;
        },
        resultSuccess = function(id, o) {
            var results = Y.JSON.parse(o.responseText),
            i, needMore = false, result;
        for (i = 0; i < queryMapResources.length; i++) {
            if (!queryMapResources[i].status) {
                result = results.resources[queryMapResources[i].id];
                if (result !== undefined && result.url) {
                    queryMapResources[i].anchor.href = result.url;
                }
                if (result === undefined || !result.status) {
                    needMore = (results.status !== 'successful');
                }
                else{
                    queryMapResources[i].status = result.status;
                    if (result.status === 'successful') {
                        queryMapResources[i].anchor.parentNode.appendChild(document.createTextNode(': ' + result.hits + ' '));
                    }
                }
            }
        }
        if (needMore) {
            setTimeout(getResultCounts, 2000);
        }
        Y.fire('lane:popin', queryMapping);
    },
        mapSuccess = function(id, o) {
        var anchor, span, i,
        queryMap = Y.JSON.parse(o.responseText),
        resourceMap = queryMap.resourceMap;
    if (resourceMap) {
        queryMapResources = resourceMap.resources;
        getResultCounts = function() {
            var url = basePath + '/apps/search/json?q=' + encodedQuery, i;
            for (i = 0; i < queryMapResources.length; i++) {
                if (!queryMapResources[i].status) {
                    url += '&r=' + queryMapResources[i].id;
                }
            }
            url += '&rd=' + Math.random();
            Y.io(url, {
                on:{
                    success: resultSuccess
                    }
                });
            };
            for (i = 0; i < queryMapResources.length; i++) {
                queryMapResources[i].status = '';
                span = document.createElement('span');
                anchor = document.createElement('a');
                anchor.title = 'QueryMapping: ' + queryMapResources[i].label;
                span.appendChild(anchor);
                anchor.appendChild(document.createTextNode(queryMapResources[i].label));
                queryMapping.append(span);
                queryMapResources[i].anchor = anchor;
            }
            if (document.getElementById('queryMappingDescriptor')) {
                document.getElementById('queryMappingDescriptor').appendChild(document.createTextNode(resourceMap.descriptor));
            }
            getResultCounts();
            // track mapped term, descriptor, and resources
            Y.lane.fire("tracker:trackableEvent", {
                category: "lane:queryMapping",
                action: "query=" + query + "; descriptor=" + resourceMap.descriptor.descriptorName,
                label: "resources=" + getResourcesString()
            });
        }
    };
    if (query && queryMapping) {
        Y.io(basePath + '/apps/querymap/json?q=' + encodedQuery, {
            on:{
                success: mapSuccess
            }
        });
    }
})();

