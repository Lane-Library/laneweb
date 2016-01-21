(function() {
    var node,
        ie = Y.UA.ie,
        lane = Y.lane;
    if (ie) {

        //toggle bookmarklet instructions for IE on favorites page
        node = Y.one("#bookmarkletNotIE");
        if (node) {
            node.setStyle("display", "none");
        }
        node = Y.one("#bookmarkletIE");
        if (node) {
            node.setStyle("display", "block");
        }

        // rewrite lane.getData to use getAttribute()
        if (!document.body.dataset) {
            lane.getData = function(node, name) {
                return node.getAttribute("data-" + name);
            }
        };

        if (!document.body.classList) {
            lane.activate = function(node) {
                node.className += " active";
            }
            lane.deactivate = function(node) {
                node.className = node.className.replace(/ active/, "");
            }
        }
    }

})();
