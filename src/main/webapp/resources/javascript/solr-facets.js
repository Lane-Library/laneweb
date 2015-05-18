/*
 * quick and dirty facet UI: replace once UI specs known
 */
(function() {
    var model = Y.lane.Model,
        query = model.get(model.QUERY),
        locationSearch = location.search,
        locationSearchDecoded = decodeURIComponent(locationSearch),
        encodedQuery = model.get(model.URL_ENCODED_QUERY),
        basePath = model.get(model.BASE_PATH) || "",
        facets = Y.all('.solrFacet'),
        facetsContainer = Y.one('.solrFacets'),
        cleanFacetProp = function(facetId, facetProp) {
            // replace year 0 with "Unknown" 
            if ("year" == facetId && facetProp == 0) {
                facetProp = 'Unknown';
            }
            else if ("isEnglish" == facetId) {
                if (facetProp == "true") {
                    facetProp = 'English Only';
                } else {
                    facetProp = 'Non English';
                }
            } else if ("isRecent" == facetId) {
                if (facetProp == "true") {
                    facetProp = 'Last 10 Years';
                } else {
                    facetProp = 'More Than 10 Years Old';
                }
            }
            return facetProp;
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
            Y.io(basePath + '/apps/search/facets' + locationSearch, {
                on: {
                    success:function(id, o) {
                        var response = Y.JSON.parse(o.responseText), facetId, facetValue, facetCount, facetIdAndValuePattern, j, url, joiner, urlFacetOff, sibling, facetProp = null, headersToToggle = {};
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
                                    joiner = (locationSearch.indexOf('&f=') > -1) ? '::' : '&f=';
                                    url = location.pathname + locationSearch + joiner + facetId + ':"' + encodeURIComponent(facetProp).replace(/'/g,'%27') + '"';
                                    urlFacetOff = location.pathname + locationSearchDecoded.replace(new RegExp(facetIdAndValuePattern),'').replace(/'/g,'%27');
                                    urlFacetOff = urlFacetOff.replace(/(&f=|::)::/,'$1');
                                    urlFacetOff = urlFacetOff.replace(/(&f=|::)$/,'');
                                    facetProp = cleanFacetProp(facetId, facetProp);
                                    if (facetProp == facetValue) {
                                        facets.item(j).setStyle('display','block');
                                        facets.item(j).append("&nbsp;(" + facetCount + ")");
                                        if (locationSearchDecoded.match(facetIdAndValuePattern)) {
                                            headersToToggle[facets.item(j).previous('.facetHeader')] = facets.item(j).previous('.facetHeader');
                                            facets.item(j).setHTML(facets.item(j).get('textContent') + "&nbsp;[<a title='remove constraint' href='"+ urlFacetOff + "'>&nbsp;remove&nbsp;</a>]");
                                        }
                                    }
                                    else if (undefined == facetValue) {
                                        facets.item(j).setStyle('display','block');
                                        if (locationSearchDecoded.match(new RegExp(facetIdAndValuePattern))) {
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
            var form = e.target; p = form.get('page'), page = Number(p.get('value').replace(/[^\d]/g,'')), pages = form.get('pages');
            p.set('value',page);
            if (page < 1 || page > Number(pages.get('value'))) {
                e.preventDefault();
                form.get('parentNode').appendChild('<div style="margin:10px 80px;color:red;font-size:20px;">ERROR: page out of range</div>');
                return;
            }
            pages.remove();
          }, this);
        
})();