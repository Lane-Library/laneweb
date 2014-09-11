(function() {
    var model = Y.lane.Model,
        query = model.get(model.QUERY),
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "",
        facets = Y.all('.solrFacet'),
        facetsContainer = Y.one('#searchFacets'),
        fadein = new Y.Anim({
            node: facetsContainer,
            to:{opacity:1}
        }),
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
                                            facets.item(j).setHTML(facets.item(j).get('textContent') + "&nbsp;[<a title='remove constraint' href='"+ urlFacetOff + "'>&nbsp;remove&nbsp;</a>]");
                                        }
                                    }
                                    else if (undefined == facetValue) {
                                        facets.item(j).setStyle('display','block');
                                        if (query.match(facetIdAndValue)) {
                                            sibling.insert("<li>" + facetProp + " (" + facetCount + ")&nbsp;[<a title='remove constraint' href='"+ urlFacetOff + "'>&nbsp;remove&nbsp;</a>]</li>",'before');
                                        } else {
                                            sibling.insert("<li><a href='"+ url + "'>" + facetProp + "</a> (" + facetCount + ")</li>",'before');
                                        }
                                    }
                                }
                            }
                        }
                        fadein.run();
                    }
                }
            });
        };

        if (query && facets.size() > 0) {
            makeRequest();
        }
})();
        
// TODO: find a home for this pagination-related js
(function() {
        Y.all('form[name=pagination]').on('submit', function (e) {
            var p = e.target.get('p'), page = p.get('value').replace(/[^\d]/g,'') - 1, pages = e.target.get('pages');
            if (page < 0 || page > pages.get('value')) {
                e.preventDefault();
                alert("page out of range");
                return;
            }
            p.insert(p.get('value'),'after');
            e.target.append('<input type="hidden" name="page" value="' +  page + '"/>');
            p.remove();
            pages.remove();
          }, this);
        
})();