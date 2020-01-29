(function() {

    "use strict";

    var model = L.Model,
        query = model.get(model.URL_ENCODED_QUERY),
        locationSearch = model.get(model.QUERY_STRING),
        basePath = model.get(model.BASE_PATH) || "",
        facetsContainer = document.querySelector('.solrFacets'),
        handleKeyDown = function(event) {
            var browseFacetNavContainer = document.querySelector(".facetBrowse .s-pagination"),
                pagingNode, direction;
            if (browseFacetNavContainer) {
                if (event.keyCode === 37 || event.key === "ArrowLeft") {
                    direction = "previous";
                } else if (event.keyCode === 39 || event.key === "ArrowRight") {
                    direction = "next";
                }
                pagingNode = browseFacetNavContainer.querySelector(".pagingButton." + direction);
                if (pagingNode) {
                    event.preventDefault();
                    pagingNode.click();
                }
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
                html += '<span>'
                    + label
                    + '<a title="remove filter" href="'
                    + url
                    + '"> <i class="fa fa-times-circle fa-lg"></i></a></span>';
            });
            if (enabledFacets.length > 0) {
                html += '<span class="clearLimits"><a href="'
                    + basePath
                    + '/search.html?source=all-all&q='
                    + query
                    + '">Clear all <i class="fa fa-times-circle fa-lg"></i></a> to show '
                    + count
                    + ' results</span>';
                limitsContainer.insertAdjacentHTML("beforeEnd", html);
            }
        },
        makeRequest = function() {
            L.io(basePath + '/apps/search/facets/html?' + locationSearch, {
                on: {
                    success:function(id, o) {
                        facetsContainer.insertAdjacentHTML("beforeEnd", o.responseText);
                        // fade in facets container
                        facetsContainer.classList.add("solrFacets-active");
                        processEnabledFacets(facetsContainer);
                        L.fire("lane:new-content");
                    }
                }
            });
        };
        if (query && facetsContainer && !document.querySelector("#bassettContent") ) {
            makeRequest();
            // listener for left/right arrows
            document.addEventListener("keydown", handleKeyDown);
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
