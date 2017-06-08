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

})();