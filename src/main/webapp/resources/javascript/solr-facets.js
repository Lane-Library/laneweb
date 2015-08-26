(function() {
    var model = Y.lane.Model,
        doc = Y.one("doc"),
        query = model.get(model.URL_ENCODED_QUERY),
        locationSearch = location.search,
        basePath = model.get(model.BASE_PATH) || "",
        facetsContainer = Y.one('.solrFacets'),
        handleArrowKey = function(event, direction) {
            var browseFacetNavContainer = Y.one(".s-pagination.facetBrowse"),
                selectorString = ".pagingButton." + direction,
                pagingContainer;
            if (undefined != browseFacetNavContainer && browseFacetNavContainer.getStyle('visibility') == 'visible') {
                pagingContainer = browseFacetNavContainer;
            }
            if (pagingContainer) {
                pagingContainer.one(selectorString)._node.click();
            } 
        },
        toggleHeader = function(nodeOrEvent) {
            var node = undefined != nodeOrEvent.currentTarget ? nodeOrEvent.currentTarget : nodeOrEvent, next = node.next('li'), anim;
            if (node.hasClass('open')) {
                node.removeClass('open');
                node.removeClass('openIE');
            } else {
                node.addClass('open');
                if (Y.UA.ie && Y.UA.ie <= 9) {
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
            Y.io(basePath + '/apps/search/facets/html' + locationSearch, {
                on: {
                    success:function(id, o) {
                        facetsContainer.append(o.responseText);
                        Y.all('.facetHeader').each(function(node){toggleHeader(node)});
                        Y.all('.facetHeader').on('click',toggleHeader);
                        // fade in facets container
                        new Y.Anim({
                            node: facetsContainer,
                            to:{opacity:1}
                        }).run();
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