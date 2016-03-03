(function(){

    "use strict";

    var lane = Y.lane,

        // view handles interactions with the DOM, created a with a NodeList
        // img nodes that have data-bibid attributes.
        view = function(bookImageNodes) {

            // an object that maps bibids to img nodes
            var imageMap = {};

            // initialize the imageMap
            [].forEach.call(bookImageNodes, function(imageNode) {
                imageMap[lane.getData(imageNode, "bibid")] = imageNode;
            });

            // make a couple of view functions available
            return {

                // receive notification that the viewport has changed and return a list of
                // img nodes that are in the viewport and requiring src attributes.
                getImgsForUpdate: function(viewport) {
                    var imagesForUpdate = [],
                        bibid;
                    for (bibid in imageMap) {
                        if (!imageMap[bibid].src && viewport.inView(imageMap[bibid])) {
                            imagesForUpdate.push(bibid);
                        };
                    };
                    return imagesForUpdate;
                },

                // add src attributes to imag nodes
                update: function(updates) {
                    for (var bibid in updates) {
                        if (updates[bibid]) {
                            imageMap[bibid].src = updates[bibid];
                            lane.activate(imageMap[bibid], "bookcover");
                        }
                        delete imageMap[bibid];
                    }
                }
            };
        }(document.querySelectorAll("img[data-bibid]")),

        // communicates with the server to get bookcover thumbnail urls for bibids
        bookcoverService = function() {

            var baseURL = window.model["base-path"] + "/apps/bookcovers?",

                // working is a comma separated list of bibids being looked up, or empty
                working = "",

                service = {

                    // get thumbnail urls for bibids from the server
                    getBookCoverURLs: function(bibids) {
                        var i, request, url = baseURL;
                        if (working) {
                            throw new Error("still working on " + working);
                        }
                        // don't get more than 20
                        for (i = 0; i < bibids.length && i < 20; i++) {
                            url = url += "bibid=" + bibids[i] + "&";
                            working += bibids[i] + ",";
                        }

                        request = new XMLHttpRequest();
                        request.open("GET", url, true);
                        request.onload = function() {
                            working = "";
                            if (request.status >= 200 && request.status < 400) {
                                service.fire("covers", {covers: JSON.parse(request.responseText)});
                            }
                        };
                        request.send();
                    }
                };

            // make the service an EventTarget
            Y.augment(service, Y.EventTarget);
            return service;
        }(),

        // controls the communication between the viewport, service and view.
        controller = {

            // handler for the service covers event, sends the covers to the view
            covers: function(event) {
                view.update(event.covers);
            },

            // handler for the viewport update event, gets bibids from the view
            // and asks the service of bookcover urls for them.
            update: function(viewport) {
                var bibids = view.getImgsForUpdate(viewport);
                if (bibids.length > 0) {
                    bookcoverService.getBookCoverURLs(bibids);
                }
            }
        };

    bookcoverService.on("covers", controller.covers);

    lane.on(["viewport:init","viewport:scrolled"], function(event){
        controller.update(event.viewport);
    });

})();