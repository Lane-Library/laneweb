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

    /*
     * polyfill for NodeList.prototype.forEach() from
     * https://github.com/imagitama/nodelist-foreach-polyfill
     */
    if (window.NodeList && !NodeList.prototype.forEach) {
        NodeList.prototype.forEach = function (callback, thisArg) {
            thisArg = thisArg || window;
            for (var i = 0; i < this.length; i++) {
                callback.call(thisArg, this[i], i, this);
            }
        };
    }
})();
