(function() {

    "use strict";

    var userAgent = L.getUserAgent();

    if (/MSIE/.test(userAgent)) {

        //toggle bookmarklet instructions for IE on favorites page
        document.querySelectorAll('#bookmarkletNotIE, .bookmarklet-not-ie').forEach(function(node) {
            node.style.display = 'none';
        });

        document.querySelectorAll('#bookmarkletIE, .bookmarklet-ie').forEach(function(node) {
            node.style.display = 'block';
        });
    }

    if (/Edge/.test(userAgent)) {

        //toggle bookmarklet instructions for IE on favorites page
        document.querySelectorAll('#bookmarkletNotIE, .bookmarklet-not-ie').forEach(function(node) {
            node.style.display = 'none';
        });

        document.querySelectorAll('.bookmarklet-edge').forEach(function(node) {
            node.style.display = 'block';
        });
    }

})();
