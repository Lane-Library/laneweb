(function() {
    var hybridInput, laneSuggest,
        model = Y.lane.Model,
        basePath = model.get(model.BASE_PATH) || "",
        query = model.get(model.QUERY),
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        searchIndicator = Y.lane.SearchIndicator,
        // TODO: remove this commented out code once new history site is released
//        searchSource = Y.lane.Search.getSearchSource(),
    metasearch = function() {
        var searchElms, // the elements in need of hit counts
            searchables = [], // all engines to search
            searchRequests = [], // search timerIds so we can abort sleeping getResultCounts
            uberEngines = ['cro_', 'mdc_'], // engines with multiple resources
            startTime,
            getSearchUrl = function() {
                var add, i, y,
                appsSearch = "/apps/search/json?q=",
                // TODO: remove this commented out code once new history site is released
//                appsSearch = searchSource === "history-all" ? "/apps/search/noproxy/json?q=" : "/apps/search/json?q=",
                    searchUrl = basePath + appsSearch + encodedQuery;
                for (y = 0; y < searchables.length; y++) {
                    add = true;
                    for (i = 0; i < uberEngines.length; i++) {
                        // don't add if:
                        // - engine is uber and uber already on url
                        // - engine already on url
                        if (searchables[y].match(uberEngines[i]) && searchUrl.match(uberEngines[i])) {
                            add = false;
                        } else if (searchUrl.match("r=" + searchables[y] + "(&|$)")) {
                            add = false;
                        }
                    }
                    if (add) {
                        searchUrl += '&r=' + searchables[y];
                    }
                }
                searchUrl += '&rd=' + Math.random();
                return searchUrl;
            };
        return {
            initialize: function() {
                var i;
                searchElms = Y.all(".metasearch");
                for (i = 0; i < searchElms.size(); i++) {
                    if (Y.Array.indexOf(searchables, searchElms.item(i).get('id')) === -1) {
                        searchables.push(searchElms.item(i).get('id'));
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
                        success: function(id, o) {
                            var response = Y.JSON.parse(o.responseText), results = response.resources, needMore = false, i, y, result, updateables, resultSpan, sleepingTime, remainingTime;

                            for (i = 0; i < searchables.length; i++) {
                                updateables = Y.all('#' + searchables[i]); // search content may have more than one element with same ID
                                result = results[searchables[i]];
                                if (result === undefined || !result.status) {
                                    needMore = true;
                                    continue;
                                } else if (updateables.size() > 0) {
                                    for (y = 0; y < updateables.size(); y++) {
                                        resultSpan = updateables.item(y).get('parentNode').one('.searchCount');
                                        if (resultSpan === null) {
                                            resultSpan = Y.Node.create('<span class="searchCount"></span>');
                                            updateables.item(y).get('parentNode').insert(resultSpan);
                                        }
                                        if (result.status === 'successful') {
                                            // process display of each updateable node
                                            // once all processed, remove id from searchables
                                            //TODO: see search2json.xsl todo note about number value for hits
                                            resultSpan.setContent('&#160;' +
                                            Y.DataType.Number.format(parseInt(result.hits), {
                                                thousandsSeparator: ","
                                            }));
                                            result.name = (updateables.item(y).get('innerHTML')) ? updateables.item(y).get('innerHTML') : '';
                                            updateables.item(y).setAttribute('href', result.url);
                                            // fix for IE: @ in text of element will cause element text to be replaced by href value
                                            // relies on result.name being set before url is changed
                                            // http://www.quirksmode.org/bugreports/archives/2005/10/Replacing_href_in_links_may_also_change_content_of.html
                                            if (Y.UA.ie) {
                                                updateables.item(y).set('innerHTML',result.name);
                                            }
                                            updateables.item(y).setAttribute('target', '_blank');
                                            updateables.item(y).removeClass('metasearch');
                                        } else if (result.status === 'failed' || result.status === 'canceled') {
                                            resultSpan.setContent('&#160;? ');
                                            updateables.item(y).removeClass('metasearch');
                                        }
                                    }
                                    searchables.splice(i--, 1);
                                }
                            }
                            sleepingTime = 2000;
                            remainingTime = (new Date().getTime()) - startTime;
                            if (response.status !== 'successful' && needMore && searchables.length > 0 && (remainingTime <= 60 * 1000)) {
                                // at more than 20 seconds the sleeping time becomes 10 seconds
                                if (remainingTime > 20 * 1000) {
                                    sleepingTime = 10000;
                                }
                                searchRequests.push(setTimeout(metasearch.getResultCounts, sleepingTime));
                            } else {
                                searchIndicator.hide();
                            }
                        }// end request success definition
                    }//end on
                });// end io request
            }// end getResultCounts
        };
    }();

    // check for presence of search term and metasearch classNames
    if (Y.all('.metasearch').size() > 0 && query) {
        metasearch.initialize();
        metasearch.getResultCounts();
        searchIndicator.show();
    }
    // hybrid search page inputs
    hybridInput = Y.one('.laneSuggest');
    if (hybridInput) {
        laneSuggest = new Y.lane.Suggest(hybridInput);
        laneSuggest.on("select",function(){
            searchIndicator.show();
            hybridInput.ancestor("form").submit();
        });
    }
})();
