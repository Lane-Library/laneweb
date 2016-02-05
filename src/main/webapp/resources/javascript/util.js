/*
 * This file contains utility functions attached to the Y.lane object that are overwritten
 * in the case of IE where the properties or functions are not available.  Calls to these
 * functions can be replaced when IE9 is no longer a significant part of our traffic.
 */
(function() {

    "use strict";

    var lane = Y.lane;

    lane.activate = function(node, clazz) {
        node.classList.add(clazz +"-active");
    };

    lane.deactivate = function(node, clazz) {
        node.classList.remove(clazz + "-active");
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

            lane.activate = function(node, clazz) {
                var activeClass = " " + clazz + "-active";
                if (node.className.indexOf(activeClass) === -1) {
                    node.className += activeClass;
                }
            };
            lane.deactivate = function(node, clazz) {
                var re = new RegExp(" " + clazz + "-active");
                node.className = node.className.replace(re, "");
            };
        }
    }

})();