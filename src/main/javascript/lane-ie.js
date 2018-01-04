(function() {

    "use strict";

    if (Y.UA.ie) {

        //toggle bookmarklet instructions for IE on favorites page
        document.querySelectorAll('#bookmarkletNotIE, .bookmarklet-not-ie').forEach(function(node) {
            node.style.display = 'none';
        });

        document.querySelectorAll('#bookmarkletIE, .bookmarklet-ie').forEach(function(node) {
            node.style.display = 'block';
        });

    }

    if (/Edge/.test(window.navigator.userAgent)) {

        //toggle bookmarklet instructions for IE on favorites page
        document.querySelectorAll('#bookmarkletNotIE, .bookmarklet-not-ie').forEach(function(node) {
            node.style.display = 'none';
        });

        document.querySelectorAll('.bookmarklet-edge').forEach(function(node) {
            node.style.display = 'block';
        });

        Node.prototype.remove = function() {
            
        }
    }

})();
