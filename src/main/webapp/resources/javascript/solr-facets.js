(function() {
    var model = Y.lane.Model,
        query = model.get(model.QUERY),
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "",
        facets = Y.all('.solrFacet'),
        makeRequest = function() {
            Y.io(basePath + '/apps/search/facets?q=' + encodedQuery + '&rd=' + Math.random(), {
                on: {
                    success:function(id, o) {
                        var response = Y.JSON.parse(o.responseText), facetId, j, url, sibling;
                        for (j = 0; j < facets.size(); j++) {
                            sibling = facets.item(j).get('nextSibling');
                            facetId = facets.item(j).getAttribute('id');
                            if (undefined !== response.facets[facetId]) {
                                for (var p in response.facets[facetId]) {
                                    url = basePath + '/search.html?source=all-all&q=' + encodedQuery + '+' + facetId + ':"' + escape(p) + '"';
                                    sibling.insert("<li><a href='"+ url + "'>" + p + "</a> - " + response.facets[facetId][p] + "</li>",'before');
                                }
                            }
                        }
                    }
                }
            });
        };

        if (query && facets.size() > 0) {
            makeRequest();
        }
})();
