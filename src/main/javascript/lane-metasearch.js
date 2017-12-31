(function() {

    "use strict";

    var model = L.Model,
        basePath = model.get(model.BASE_PATH) || "",
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        searchIndicator = L.searchIndicator,
    metasearch = function() {
        var resourceIds = [],
            searchRequests = [],
            startTime,
            getSearchUrl = function() {
                var i,
                    appsSearch = "/apps/search/json?q=",
                    searchUrl = basePath + appsSearch + encodedQuery;
                for (i = 0; i < resourceIds.length; i++) {
                    searchUrl += '&r=' + resourceIds[i];
                }
                searchUrl += '&rd=' + Math.random();
                return searchUrl;
            },
            updateNode = function(result, node) {
                var resultSpan = node.parentNode.querySelector('.searchCount'),
                    template;
                if (resultSpan === null) {
                    template = document.createElement("div");
                    template.innerHTML = '<span class="searchCount"></span>';
                    node.parentNode.appendChild(template.firstChild);
                    resultSpan = node.parentNode.querySelector('.searchCount');
                }
                node.href = result.url;
                if (result.status === 'successful') {
                    // process display of each updateable node
                    // once all processed, remove id from resourceIds
                    resultSpan.innerHTML = '&#160;' + result.hits.toLocaleString();
                    node.target = '_blank';
                    node.classList.remove('metasearch');
                } else if (result.status === 'failed' || result.status === 'canceled') {
                    resultSpan.innerHTML = '&#160;? ';
                    node.classList.remove('metasearch');
                }
            },
            updateNodes = function(results) {
                var i, j, updateableNodes, result, needMore = false;
                for (i = 0; i < resourceIds.length; i++) {
                    // search content may have more than one element with same ID
                    updateableNodes = document.querySelectorAll('#' + resourceIds[i]);
                    result = results[resourceIds[i]];
                    if (result === undefined || result.status === 'running') {
                        needMore = true;
                    } else if (updateableNodes.length > 0) {
                        for (j = 0; j < updateableNodes.length; j++) {
                            updateNode(result, updateableNodes[j]);
                        }
                        resourceIds.splice(i--, 1);
                    }
                }
                return needMore;
            },
            maybeGetMore = function(response, needMore) {
                var sleepingTime = 2000,
                    remainingTime = (new Date().getTime()) - startTime;
                if (response.status !== 'successful' && needMore && resourceIds.length > 0 && (remainingTime <= 60 * 1000)) {
                    // at more than 20 seconds the sleeping time becomes 10 seconds
                    if (remainingTime > 20 * 1000) {
                        sleepingTime = 10000;
                    }
                    searchRequests.push(setTimeout(metasearch.getResultCounts, sleepingTime));
                } else {
                    searchIndicator.hide();
                }
            },
            successful = function(id, o) {
                var response = JSON.parse(o.responseText),
                    results = response.resources,
                    needMore;

                needMore = updateNodes(results);
                maybeGetMore(response, needMore);
            };
        return {
            initialize: function() {
                var i, searchElms = document.querySelectorAll(".metasearch");
                for (i = 0; i < searchElms.length; i++) {
                    if (resourceIds.indexOf(searchElms[i].id) === -1) {
                        resourceIds.push(searchElms[i].id);
                    }
                }
                for (i = 0; i < searchRequests.length; i++) {
                    clearTimeout(searchRequests[i]);
                    searchRequests.splice(i, 1);
                }
                startTime = new Date().getTime();
            },
            getResultCounts: function() {
                L.io(getSearchUrl(),{
                    on: {
                        success: successful
                    }
                });
            }
        };
    }();

    // check for presence of search term and metasearch classNames
    if (document.querySelectorAll('.metasearch').length > 0 && encodedQuery) {
        metasearch.initialize();
        metasearch.getResultCounts();
        searchIndicator.show();
    }
})();
