(function(){

    "use strict";

    // view handles interactions with the DOM, created a with a NodeList
    // of search result li nodes with data-doi attributes
    let view = function(searchResultNodes) {

            // an object that maps dois to data nodes
            let doiMap = {};

            // initialize the doiMap
            searchResultNodes.forEach(function(searchResultNode) {
                let doi = searchResultNode.dataset.doi.toLowerCase();
                doiMap[doi] = searchResultNode;
            });

            // make a couple of view functions available
            return {

                // return a list of search result nodes that require article lookups
                getDoisForUpdate: function(viewport) {
                    let doisForUpdate = [],
                        doi;
                    for (doi in doiMap) {
                        if (doiMap.hasOwnProperty(doi)) {
                            if (!doiMap[doi].fetched && viewport.nearView(doiMap[doi],3)) {
                                doisForUpdate.push(doi);
                            }
                        }
                    }
                    return doisForUpdate;
                },

                // add PDF link search result nodes
                update: function(article) {
                    let retractionNoticeUrl = article.data.retractionNoticeUrl,
                        fulltextUrl = article.data.fullTextFile,
                        contentLocation = article.data.contentLocation,
                        doi = article.data.doi ? article.data.doi.toLowerCase() : null,
                        coverImageUrl = (article.included[0] && article.included[0].coverImageUrl) ? article.included[0].coverImageUrl : null;
                    if (doiMap[doi]) {
                        doiMap[doi].fetched = true;
                        if (retractionNoticeUrl) {
                            addRetractedArticleLink(doiMap[doi],'xmark','Retracted Article',retractionNoticeUrl);
                        }
                        else if (fulltextUrl) {
                            addFulltextLink(doiMap[doi],'Direct to PDF',fulltextUrl);
                        }
                        else if (contentLocation) {
                            addFulltextLink(doiMap[doi],'Direct to Full Text',contentLocation);
                        }
                        if (coverImageUrl) {
                            doiMap[doi].querySelector('.bookcover').innerHTML = '<image src="' + coverImageUrl + '"/>';
                        }
                    }
                    // appended links should be trackable as searchResultClick events
                    document.querySelectorAll(".bzFT").forEach(function(node) {
                        node.isTrackableAsEvent = true;
                    });
                    delete doiMap[doi];
                }
            };
        }(document.querySelectorAll("li[data-doi]")),

        addFulltextLink = function(node, label, url) {
           let link = node.querySelector('.resource-detail .hldgsContainer span a'),
               span = link.querySelector('span');
            link.href = url;
            span.textContent = label;
        },
        
        addRetractedArticleLink = function(node, type, label, url) {
            node.querySelector('.sourceInfo').insertAdjacentHTML("beforeend",
                '<div><a class="bzFT" href="' + url + '">' + 
                '<i class="fa-light fa-file-' + type + '"></i>' + 
                label + '</a></div>'
            )
        },

        // service to communicate with the server to fetch article data for each DOI
        articleLookupService = function() {

            let baseURL = window.model["base-path"] + "/apps/browzine/doi/",

                service = {

                    // fetch article data from API
                    getArticleData: function(doi) {
                        let request, url = baseURL + doi;
                        request = new XMLHttpRequest();
                        request.open("GET", url, true);
                        request.onload = function() {
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
                view.update(event.article);
            },

            // handler for the viewport update event, gets dois from the view
            // and asks the service of bookcover urls for them.
            update: function(viewport) {
                let dois = view.getDoisForUpdate(viewport);
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
