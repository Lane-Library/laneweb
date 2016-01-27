/*
 * This file contains utility functions attached to the Y.lane object that are overwritten
 * in the case of IE where the properties or functions are not available.  Calls to these
 * functions can be replaced when IE9 is no longer a significant part of our traffic.
 */
(function() {

    "use strict";

    var lane = Y.lane;

    lane.activate = function(node) {
        node.classList.add("active");
    };

    lane.deactivate = function(node) {
        node.classList.remove("active");
    };

    lane.getData = function(node, name) {
        return node.dataset[name];
    };

    if (Y.UA.ie) {

        // rewrite lane.getData to use getAttribute()
        if (!document.body.dataset) {
            lane.getData = function(node, name) {
                return node.getAttribute("data-" + name);
            };
        }

        // rewrite lane.activate and lane.deactivate to use className
        if (!document.body.classList) {
            lane.activate = function(node) {
                if (node.className.indexOf(" active") === -1) {
                    node.className += " active";
                }
            };
            lane.deactivate = function(node) {
                node.className = node.className.replace(/ active/, "");
            };
        }
    }

})();