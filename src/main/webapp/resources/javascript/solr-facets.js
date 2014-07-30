(function() {
    var model = Y.lane.Model,
        query = model.get(model.QUERY),
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "",
        facets = Y.all('.solrFacet'),
        encodeAndEscape = function(string) {
            return encodeURIComponent(string).replace(/'/g,'%27');
        },
        makeRequest = function() {
            Y.io(basePath + '/apps/search/facets?q=' + encodedQuery + '&rd=' + Math.random(), {
                on: {
                    success:function(id, o) {
                        var response = Y.JSON.parse(o.responseText), facetId, facetValue, facetCount, facetIdAndValue, j, url, urlFacetOff, sibling, facetProp = null;
                        for (j = 0; j < facets.size(); j++) {
                            sibling = facets.item(j).get('nextSibling');
                            facetId = facets.item(j).getAttribute('id').split(':')[0];
                            facetValue = facets.item(j).getAttribute('id').split(':')[1];
                            if (undefined !== response.facets[facetId]) {
                                for (facetProp in response.facets[facetId]) {
                                    facetCount = Y.DataType.Number.format(response.facets[facetId][facetProp], {
                                        thousandsSeparator: ","
                                    });
                                    facetIdAndValue = facetId + ':"?' + facetProp + '"?';
                                    url = basePath + '/search.html?source=all-all&q=' + encodedQuery + '+' + facetId + ':"' + encodeAndEscape(facetProp) + '"';
                                    urlFacetOff = basePath + '/search.html?source=all-all&q=' + encodeAndEscape(query.replace(new RegExp(facetIdAndValue),'').trim());
                                    if (facetProp == facetValue) {
                                        facets.item(j).setStyle('display','block');
                                        facets.item(j).append("&nbsp;(" + facetCount + ")");
                                        if (query.match(facetIdAndValue)) {
                                            facets.item(j).setHTML(facets.item(j).get('textContent') + " [<a title='remove constraint' href='"+ urlFacetOff + "'> remove </a>]");
                                        }
                                    }
                                    else if (undefined == facetValue) {
                                        facets.item(j).setStyle('display','block');
                                        if (query.match(facetIdAndValue)) {
                                            sibling.insert("<li>" + facetProp + " (" + facetCount + ") [<a title='remove constraint' href='"+ urlFacetOff + "'> remove </a>]</li>",'before');
                                        } else {
                                            sibling.insert("<li><a href='"+ url + "'>" + facetProp + "</a> (" + facetCount + ")</li>",'before');
                                        }
                                    }
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
