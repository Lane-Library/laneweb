(function() {
    var model = Y.lane.Model,
        basePath = model.get(model.BASE_PATH) || "",
        query = model.get(model.QUERY),
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        searchIndicator = Y.lane.SearchIndicator,
    metasearch = function() {
        var resourceIds = [],
            searchRequests = [],
            startTime,
            containsUberId = function(url, resourceId) {
                var containsUberId = false,
                    mdc = "mdc_",
                    cro = "cro_";
                if (resourceId.match(/^(mdc_|cro_)/)) {
                    if (resourceId.indexOf(mdc) === 0) {
                        containsUberId = url.indexOf(mdc) > 0;
                    } else {
                        containsUberId = url.indexOf(cro) > 0;
                    }
                }
                return containsUberId;
            },
            getSearchUrl = function() {
                var i, resourceId,
                    appsSearch = "/apps/search/json?q=",
                    searchUrl = basePath + appsSearch + encodedQuery;
                for (i = 0; i < resourceIds.length; i++) {
                    resourceId = resourceIds[i];
                    if (!containsUberId(searchUrl, resourceId)) {
                        searchUrl += '&r=' + resourceId;
                    }
                }
                searchUrl += '&rd=' + Math.random();
                return searchUrl;
            },
            updateNode = function(result, node) {
                var resultSpan = node.get('parentNode').one('.searchCount');
                if (resultSpan === null) {
                    resultSpan = Y.Node.create('<span class="searchCount"></span>');
                    node.get('parentNode').insert(resultSpan);
                }
                if (result.status === 'successful') {
                    // process display of each updateable node
                    // once all processed, remove id from resourceIds
                    resultSpan.setContent('&#160;' +
                    Y.DataType.Number.format(result.hits), {
                        thousandsSeparator: ","
                    });
                    result.name = (node.get('innerHTML')) ? node.get('innerHTML') : '';
                    node.setAttribute('href', result.url);
                    // fix for IE: @ in text of element will cause element text to be replaced by href value
                    // relies on result.name being set before url is changed
                    // http://www.quirksmode.org/bugreports/archives/2005/10/Replacing_href_in_links_may_also_change_content_of.html
                    if (Y.UA.ie) {
                        node.set('innerHTML',result.name);
                    }
                    node.setAttribute('target', '_blank');
                    node.removeClass('metasearch');
                } else if (result.status === 'failed' || result.status === 'canceled') {
                    resultSpan.setContent('&#160;? ');
                    node.removeClass('metasearch');
                }
            }
            successful = function(id, o) {
                var response = Y.JSON.parse(o.responseText),
                    results = response.resources,
                    needMore = false, i, y, result, updatableNodes,
                    sleepingTime, remainingTime;

                for (i = 0; i < resourceIds.length; i++) {
                    // search content may have more than one element with same ID
                    updatableNodes = Y.all('#' + resourceIds[i]);
                    result = results[resourceIds[i]];
                    if (result === undefined || !result.status) {
                        needMore = true;
                    } else if (updatableNodes.size() > 0) {
                        for (y = 0; y < updatableNodes.size(); y++) {
                            updateNode(result, updatableNodes.item(y));
                        }
                        resourceIds.splice(i--, 1);
                    }
                }
                sleepingTime = 2000;
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
            };
        return {
            initialize: function() {
                var i, searchElms = Y.all(".metasearch");
                for (i = 0; i < searchElms.size(); i++) {
                    if (Y.Array.indexOf(resourceIds, searchElms.item(i).get('id')) === -1) {
                        resourceIds.push(searchElms.item(i).get('id'));
                    }
                }
                for (i = 0; i < searchRequests.length; i++) {
                    clearTimeout(searchRequests[i]);
                    searchRequests.splice(i, 1);
                }
                startTime = new Date().getTime();
            },
            getResultCounts: function() {
                Y.io(getSearchUrl(),{
                    on: {
                        success: successful
                    }
                });
            }
        };
    }();

    // check for presence of search term and metasearch classNames
    if (Y.all('.metasearch').size() > 0 && query) {
        metasearch.initialize();
        metasearch.getResultCounts();
        searchIndicator.show();
    }
})();
