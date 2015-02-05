/*
 * quick and dirty facet UI: replace once UI specs known
 */
(function() {
    var model = Y.lane.Model,
        query = model.get(model.QUERY),
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "",
        facets = Y.all('.solrFacet'),
        facetsContainer = Y.one('#searchFacets'),
        encodeAndEscape = function(string) {
            return encodeURIComponent(string).replace(/'/g,'%27');
        },
        toggleHeader = function(nodeOrEvent) {
            var node = undefined != nodeOrEvent.currentTarget ? nodeOrEvent.currentTarget : nodeOrEvent, next = node.next('li'), anim;
            if (node.hasClass('open')) {
                node.removeClass('open');
                node.removeClass('openIE');
            } else {
                node.addClass('open');
                if (Y.UA.ie) { 
                    node.addClass('openIE');
                }
            }
            while (next && !next.hasClass('facetHeader')) {
                if (next.getStyle('height').indexOf('0') == 0) {
                    anim = new Y.Anim({
                        node: next,
                        to: { height: function(node) { return node.get('scrollHeight');}
                            },
                        duration : 0.25,
                        easing: 'backIn'
                    });
                    anim.on('end',function(){
                        var n = this.get('node');
                        n.setStyle('overflow','visible');
                        n.setStyle('border-bottom','1px solid #e6e4dc');
                        })
                    anim.run();
                } else {
                    next.setStyle('overflow','hidden');
                    anim = new Y.Anim({
                        node: next,
                        to: { height: 0 },
                        duration : 0.25,
                        easing: 'backIn'
                    });
                    anim.on('end',function(){
                        this.get('node').setStyle('border-bottom','none');
                        })
                    anim.run();
                }
                next = next.next('li');
            }
        },
        makeRequest = function() {
            Y.io(basePath + '/apps/search/facets?q=' + encodedQuery + '&rd=' + Math.random(), {
                on: {
                    success:function(id, o) {
                        var response = Y.JSON.parse(o.responseText), facetId, facetValue, facetCount, facetIdAndValuePattern, j, url, urlFacetOff, sibling, facetProp = null, headersToToggle = {};
                        for (j = 0; j < facets.size(); j++) {
                            sibling = facets.item(j).get('nextSibling');
                            facetId = facets.item(j).getAttribute('id').split(':')[0];
                            facetValue = facets.item(j).getAttribute('id').split(':')[1];
                            if (undefined !== response.facets[facetId]) {
                                for (facetProp in response.facets[facetId]) {
                                    facetCount = Y.DataType.Number.format(response.facets[facetId][facetProp], {
                                        thousandsSeparator: ","
                                    });
                                    facetIdAndValuePattern = facetId + ':"?' + facetProp.replace(/([\(\)\[\]\+\.])/g,"\\$1") + '"?';
                                    url = basePath + '/search.html?source=all-all&q=' + encodedQuery + '+' + facetId + ':"' + encodeAndEscape(facetProp) + '"';
                                    urlFacetOff = basePath + '/search.html?source=all-all&q=' + encodeAndEscape(query.replace(new RegExp(facetIdAndValuePattern),'').trim());
                                    // replace year 0 with "Unknown" 
                                    if ("year" == facetId && facetProp == 0) {
                                        facetProp = 'Unknown';
                                    }
                                    if (facetProp == facetValue) {
                                        facets.item(j).setStyle('display','block');
                                        facets.item(j).append("&nbsp;(" + facetCount + ")");
                                        if (query.match(facetIdAndValuePattern)) {
                                            headersToToggle[facets.item(j).previous('.facetHeader')] = facets.item(j).previous('.facetHeader');
                                            facets.item(j).setHTML(facets.item(j).get('textContent') + "&nbsp;[<a title='remove constraint' href='"+ urlFacetOff + "'>&nbsp;remove&nbsp;</a>]");
                                        }
                                    }
                                    else if (undefined == facetValue) {
                                        facets.item(j).setStyle('display','block');
                                        if (query.match(new RegExp(facetIdAndValuePattern))) {
                                            headersToToggle[facets.item(j)] = facets.item(j);
                                            sibling.insert("<li>" + facetProp + " (" + facetCount + ")&nbsp;[<a title='remove constraint' href='"+ urlFacetOff + "'>&nbsp;remove&nbsp;</a>]</li>",'before');
                                        } else {
                                            sibling.insert("<li><a href='"+ url + "'>" + facetProp + "</a> (" + facetCount + ")</li>",'before');
                                        }
                                    }
                                }
                            }
                        }
                        for (n in headersToToggle) {
                            toggleHeader(headersToToggle[n]);
                        }
                        // fade in facets container
                        new Y.Anim({
                            node: facetsContainer,
                            to:{opacity:1}
                        }).run();
                    }
                }
            });
        };
        Y.all('.facetHeader').on('click',toggleHeader);
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