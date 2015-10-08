(function() {
    var model = Y.lane.Model,
        doc = Y.one("doc"),
        ie = Y.UA.ie,
        query = model.get(model.URL_ENCODED_QUERY),
        locationSearch = location.search,
        basePath = model.get(model.BASE_PATH) || "",
        facetsContainer = Y.one('.solrFacets'),
        handleArrowKey = function(event, direction) {
            var browseFacetNavContainer = Y.one(".facetBrowse .s-pagination"),
                selectorString = ".pagingButton." + direction,
                pagingContainer;
            if (undefined !== browseFacetNavContainer && browseFacetNavContainer.getStyle('visibility') === 'visible') {
                pagingContainer = browseFacetNavContainer;
            }
            if (pagingContainer) {
                pagingContainer.one(selectorString)._node.click();
            }
        },
        processEnabledFacets = function(facetsContainer) {
            var enabledFacets = facetsContainer.all('.enabled'),
                limitsContainer = Y.one('#solrLimits'),
                allCount = Y.one('#solrAllCount'),
                count = 0,
                html = '', i, facet, label, url;
            if (allCount) {
                count = allCount.get('textContent');
            }
            for (i = 0; i < enabledFacets.size(); i++) {
                facet = enabledFacets.item(i);
                label = facet.one('.facetLabel').get('textContent');
                url = facet.one('a').get('href');
                html += '<span>' + label + '<a title="remove filter" href="' + url + '"> <i class="fa fa-times-circle fa-lg"></i></a></span>';
            }
            if (enabledFacets.size() > 0) {
                html += '<span class="clearLimits"><a href="' + basePath + '/search.html?source=all-all&q=' + query + '">Clear all</a> to show ' + count + ' results</span>';
                limitsContainer.append(html);
            }
        },
        makeRequest = function() {
            Y.io(basePath + '/apps/search/facets/html' + locationSearch, {
                on: {
                    success:function(id, o) {
                        facetsContainer.append(o.responseText);
                        // fade in facets container
                        // text blurry in IE 8 so skip animation
                        if (!ie || ie > 8) {
                            new Y.Anim({
                                node: facetsContainer,
                                to:{opacity:1},
                                duration: 0.5
                            }).run();
                        }
                        processEnabledFacets(facetsContainer);
                        Y.lane.fire("lane:new-content");
                    }
                }
            });
        };
        if (query && facetsContainer) {
            makeRequest();
            // listeners for left/right arrows
            doc.on("key", handleArrowKey, "up:37", this, "previous");
            doc.on("key", handleArrowKey, "up:39", this, "next");
            // close button on facet browse lightbox
            Y.lane.Lightbox.on("contentChanged", function() {
                var browseFacetClose = Y.one(".facetBrowse .close");
                if (browseFacetClose) {
                    browseFacetClose.on('click', function() {
                        Y.lane.Lightbox.hide();
                    });
                }
            });
        }
})();

// TODO: move this to search.js once solr is live
Y.lane.on("search:reset",  function() {
    Y.one("#search").all('input[type=hidden]').each(function(){
        if(this.get("name").match(/sort|facets/)){
            this.remove();
        }
    });
});

// TODO: find a home for this pagination-related js
(function() {
        Y.all('form[name=pagination]').on('submit', function (e) {
            var form = e.target, p = form.get('page'), page = Number(p.get('value').replace(/[^\d]/g,'')), pages = form.get('pages');
            p.set('value',page);
            if (page < 1 || page > Number(pages.get('value'))) {
                e.preventDefault();
                form.get('parentNode').appendChild('<div style="margin:10px 80px;color:red;font-size:20px;">ERROR: page out of range</div>');
                return;
            }
            pages.remove();
          }, this);
})();