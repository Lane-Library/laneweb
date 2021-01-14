(function() {

    "use strict";

    var model = L.Model,
        query = model.get(model.URL_ENCODED_QUERY),
        locationSearch = location.search,
        locationPath = location.pathname,
        basePath = model.get(model.BASE_PATH) || "",
        facetsContainer = document.querySelector('.solrFacets'),
        facetRequestQuery = function() {
            var rq = basePath + '/apps/search/facets/html';
            // support /view/<recordType>/<id> requests when location.search not present
            if (locationSearch){
                rq = rq + locationSearch;
            } else if (locationPath.indexOf('/view/') > -1) {
                rq = rq + locationPath.replace(/\/view\/([a-z]{3,10})\/(\d+)/,'?source=all-all&facets=recordType:"$1"&q=$2');
            }
            return rq;
        },
        processEnabledFacets = function(container) {
            var enabledFacets = container.querySelectorAll('.enabled'),
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
            L.io(facetRequestQuery(), {
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
