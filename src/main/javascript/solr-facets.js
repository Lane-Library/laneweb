(function() {

    "use strict";

    var model = L.Model,
        query = model.get(model.URL_ENCODED_QUERY),
        locationSearch = location.search,
        basePath = model.get(model.BASE_PATH) || "",
        facetsContainer = document.querySelector('.solrFacets'),
        handleArrowKey = function(direction) {
            var browseFacetNavContainer = document.querySelector(".facetBrowse .s-pagination"),
                selectorString = ".pagingButton." + direction,
                pagingNode;
            if (browseFacetNavContainer && browseFacetNavContainer.style.visibility === 'visible') {
                pagingNode = browseFacetNavContainer.querySelector(selectorString);
            }
            if (pagingNode) {
                pagingNode.click();
            }
        },
        processEnabledFacets = function(facetsContainer) {
            var enabledFacets = facetsContainer.querySelectorAll('.enabled'),
                limitsContainer = document.querySelector('#solrLimits'),
                allCount = document.querySelector('#solrAllCount'),
                count = 0,
                html = '', label, url;
            if (allCount) {
                count = allCount.textContent;
            }
            enabledFacets.forEach(function(facet) {
                label = facet.querySelector('.facetLabel').textContent;
                url = facet.querySelector('a').href;
                html += '<span>' + label + '<a title="remove filter" href="' + url + '"> <i class="fa fa-times-circle fa-lg"></i></a></span>';
            });
            if (enabledFacets.length > 0) {
                html += '<span class="clearLimits"><a href="' + basePath + '/search.html?source=all-all&q=' + query + '">Clear all <i class="fa fa-times-circle fa-lg"></i></a> to show ' + count + ' results</span>';
                limitsContainer.insertAdjacentHTML("beforeEnd", html);
            }
        },
        makeRequest = function() {
            Y.io(basePath + '/apps/search/facets/html' + locationSearch, {
                on: {
                    success:function(id, o) {
                        facetsContainer.insertAdjacentHTML("beforeEnd", o.responseText);
                        // fade in facets container
                        new Y.Anim({
                            node: facetsContainer,
                            to:{opacity:1},
                            duration: 0.5
                        }).run();
                        processEnabledFacets(facetsContainer);
                        L.fire("lane:new-content");
                    }
                }
            });
        };
        if (query && facetsContainer && !document.querySelector("#bassettContent") ) {
            makeRequest();
            // listener for left/right arrows
            document.addEventListener("keyup", function(event) {
                if (event.key === "ArrowLeft") {
                    handleArrowKey("previous");
                } else if (event.key === "ArrowRight") {
                    handleArrowKey("next");
                }
            });
            // close button on facet browse lightbox
            L.Lightbox.on("contentChanged", function() {
                var browseFacetClose = document.querySelector(".facetBrowse .close");
                if (browseFacetClose) {
                    browseFacetClose.addEventListener('click', function() {
                        L.Lightbox.hide();
                    });
                }
            });
        }
})();
