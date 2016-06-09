(function() {

    "use strict";

    var Model = Y.lane.Model,
        basePath = Model.get(Model.BASE_PATH) || "",
        encodedQuery = Model.get(Model.URL_ENCODED_QUERY),
        queryMapping = Y.one('#queryMapping'),
        queryMappingDescriptor = Y.one('#queryMappingDescriptor'),
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
        updatePageAndCheckForMore = function(results, resource) {
            var needMore = false, result = results.resources[resource.id];
            if (result !== undefined && result.url) {
                resource.anchor.href = result.url;
            }
            if (result === undefined || !result.status) {
                needMore = (results.status !== 'successful');
            } else{
                resource.status = result.status;
                if (result.status === 'successful') {
                    resource.anchor.parentNode.appendChild(document.createTextNode(': ' + result.hits + ' '));
                }
            }
            return needMore;
        },
        resultSuccess = function(id, o) {
            var results = Y.JSON.parse(o.responseText),
            i, needMore = false;
        for (i = 0; i < queryMapResources.length; i++) {
            if (!queryMapResources[i].status) {
                needMore = updatePageAndCheckForMore(results, queryMapResources[i]);
            }
        }
        if (needMore) {
            setTimeout(getResultCounts, 2000);
        }
        Y.fire('lane:popin', queryMapping);
    },
        mapSuccess = function(id, o) {
        var anchor, span, i,
        resourceMap = o.responseText ? Y.JSON.parse(o.responseText) : null;
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
            if (queryMappingDescriptor) {
                queryMappingDescriptor.append(resourceMap.descriptor.descriptorName);
                Y.lane.fire("lane:new-content");
            }
            getResultCounts();
            // track mapped term, descriptor, and resources
            Y.lane.fire("tracker:trackableEvent", {
                category: "lane:queryMapping",
                action: "query=" + decodeURIComponent(encodedQuery) + "; descriptor=" + resourceMap.descriptor.descriptorName,
                label: "resources=" + getResourcesString()
            });
        }
    };
    if (encodedQuery && queryMapping) {
        Y.io(basePath + '/apps/querymap/json?q=' + encodedQuery, {
            on:{
                success: mapSuccess
            }
        });
    }
})();

