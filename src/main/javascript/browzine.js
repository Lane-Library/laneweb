(function(){

    "use strict";

    // view handles interactions with the DOM, created a with a NodeList
    // of search result li nodes with data-doi attributes
    var view = function(searchResultNodes) {

            // an object that maps dois to data nodes
            var doiMap = {};

            // initialize the doiMap
            searchResultNodes.forEach(function(searchResultNode) {
                var doi = searchResultNode.dataset.doi;
                doiMap[doi] = doiMap[doi] || [];
                doiMap[doi].push(searchResultNode);
            });

            // make a couple of view functions available
            return {

                // receive notification that the viewport has changed and return a list of
                // search result nodes that are in the viewport and requiring article lookups
                getDoisForUpdate: function(viewport) {
                    var doisForUpdate = [],
                        doi, i;
                    for (doi in doiMap) {
                        if (doiMap.hasOwnProperty(doi)) {
                            for (i = 0; i < doiMap[doi].length; i++) {
                                if (!doiMap[doi][i].fetched && viewport.inView(doiMap[doi][i])) {
                                    doisForUpdate.push(doi);
                                    break;
                                }
                            }
                        }
                    }
                    return doisForUpdate;
                },

                // add PDF link search result nodes
                update: function(article) {
                    var doi, i, url;
                    if (article.fullTextFile) {
                        doi = article.doi;
                        url = article.fullTextFile;
                        for (i = 0; i < doiMap[doi].length; i++) {
                            doiMap[doi][i].fetched = true;
                            doiMap[doi][i].querySelector('.primaryLink').href = url;
                            doiMap[doi][i].querySelector('.resultInfo').insertAdjacentHTML("beforeend",
                                "<span><i class=\"fa fa-file-pdf-o\" aria-hidden=\"true\"></i> " +
                                "<a href=\"" + url + "\">Fulltext</a>" +
                                "</span>"
                            )
                        }
                    }
                    delete doiMap[doi];
                }
            };
        }(document.querySelectorAll("li[data-doi]")),

        // service to communicate with the server to fetch article data for each DOI
        articleLookupService = function() {

            var baseURL = window.model["base-path"] + "/apps/search/browzine/doi.json?q=",

                // working is a doi being looked up, or empty
                working = "",

                service = {

                    // fetch article data from API
                    getArticleData: function(doi) {
                        var request, url = baseURL + doi;
                        if (working) {
                            throw new Error("still working on " + working);
                        }

                        request = new XMLHttpRequest();
                        request.open("GET", url, true);
                        request.onload = function() {
                            working = "";
                            if (request.status >= 200 && request.status < 400) {
                                service.fire("article", {article: JSON.parse(request.responseText)});
                            }
                            // API returns a 404 when it doesn't know about a DOI
                            // don't fetch 404s more than once
                            if (request.status == 404) {
                                document.querySelector("li[data-doi='" + doi + "']").fetched = true;
                            }
                        };
                        request.send();
                    }
                };

            // make the service an EventTarget
            L.addEventTarget(service);
            return service;
        }(),

        // controls the communication between the viewport, service and view.
        controller = {

            // handler for the service covers event, sends the article data to the view
            article: function(event) {
                view.update(event.article.data);
            },

            // handler for the viewport update event, gets dois from the view
            // and asks the service of bookcover urls for them.
            update: function(viewport) {
                var dois = view.getDoisForUpdate(viewport);
                if (dois.length > 0) {
                    dois.forEach(function(doi){
                        if (doi.trim()) {
                            articleLookupService.getArticleData(doi);
                        }
                    });
                }
            }
        };

    articleLookupService.on("article", controller.article);

    L.on(["viewport:init","viewport:scrolled"], function(event){
            controller.update(event.viewport);
    });

})();
