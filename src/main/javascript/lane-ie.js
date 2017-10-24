(function() {

    "use strict";

    if (Y.UA.ie) {

        //toggle bookmarklet instructions for IE on favorites page
        [].forEach.call(document.querySelectorAll('#bookmarkletNotIE, .bookmarklet-not-ie'), function(node) {
            node.style.display = 'none';
        });

        [].forEach.call(document.querySelectorAll('#bookmarkletIE, .bookmarklet-ie'), function(node) {
            node.style.display = 'block';
        });

    }

    if (/Edge/.test(window.navigator.userAgent)) {

        //toggle bookmarklet instructions for IE on favorites page
        [].forEach.call(document.querySelectorAll('#bookmarkletNotIE, .bookmarklet-not-ie'), function(node) {
            node.style.display = 'none';
        });

        [].forEach.call(document.querySelectorAll('.bookmarklet-edge'), function(node) {
            node.style.display = 'block';
        });

    }

})();
