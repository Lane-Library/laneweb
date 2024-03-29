(function(){

    "use strict";

    // view handles interactions with the DOM, created a with a NodeList
    // div nodes that have class bookcover.
    let view = function(bookImageNodes) {

            // an object that maps bookcover ids (bcids) to img nodes
            let imageMap = {};

            // initialize the imageMap
            bookImageNodes.forEach(function(imageNode) {
                let bcids = imageNode.dataset.bcids ? imageNode.dataset.bcids.split(',') : [];
                // course reserves and equipment records will have a data-bibid (change to data-bcid?)
                if (imageNode.dataset.bibid) {
                    bcids.push("bib-" + imageNode.dataset.bibid);
                }
                bcids.forEach(function(bcid) {
                    imageMap[bcid] = imageMap[bcid] || [];
                    imageMap[bcid].push(imageNode);
                });
            });

            // make a couple of view functions available
            return {

                // receive notification that the viewport has changed and return a list of
                // img nodes that are in the viewport and requiring src attributes.
                getImgsForUpdate: function(viewport) {
                    let imagesForUpdate = [],
                        bcid, i;
                    for (bcid in imageMap) {
                        if (imageMap.hasOwnProperty(bcid)) {
                            for (i = 0; i < imageMap[bcid].length; i++) {
                                if (viewport.nearView(imageMap[bcid][i],3)) {
                                    imagesForUpdate.push(bcid);
                                    break;
                                }
                            }
                        }
                    }
                    return imagesForUpdate;
                },

                // add src attributes to imag nodes
                update: function(updates) {
                    let bcid, src, i;
                    for (bcid in updates) {
                        if (updates[bcid]) {
                            for (i = 0; i < imageMap[bcid].length; i++) {
                                // case 132771 use protocol relative urls for images
                                // from the bookcover database (substring(5))
                                src = updates[bcid];
                                src = src.substring(src.indexOf(":") + 1);
                                imageMap[bcid][i].innerHTML = "<img src='" + src + "'/>";
                            }
                        }
                        delete imageMap[bcid];
                    }
                }
            };
        }(document.querySelectorAll(".bookcover[data-bcids],.bookcover[data-bibid]")),

        // communicates with the server to get bookcover thumbnail urls for bcids
        bookcoverService = function() {

            let baseURL = window.model["base-path"] + "/apps/bookcovers?",

                service = {

                    // get thumbnail urls for bcids from the server
                    getBookCoverURLs: function(bcids) {
                        let i, request, url = baseURL;
                        // don't get more than 20
                        for (i = 0; i < bcids.length && i < 20; i++) {
                            url += "bcid=" + bcids[i] + "&";
                        }

                        request = new XMLHttpRequest();
                        request.open("GET", url, true);
                        request.onload = function() {
                            if (request.status >= 200 && request.status < 400) {
                                service.fire("covers", {covers: JSON.parse(request.responseText)});
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

            // handler for the service covers event, sends the covers to the view
            covers: function(event) {
                view.update(event.covers);
            },

            // handler for the viewport update event, gets bcids from the view
            // and asks the service of bookcover urls for them.
            update: function(viewport) {
                let bcids = view.getImgsForUpdate(viewport);
                if (bcids.length > 0) {
                    bookcoverService.getBookCoverURLs(bcids);
                }
            }
        };

    bookcoverService.on("covers", controller.covers);

    L.on(["viewport:init","viewport:scrolled"], function(event){
        controller.update(event.viewport);
    });

})();
