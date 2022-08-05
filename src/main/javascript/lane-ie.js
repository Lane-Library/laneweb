(function() {

    "use strict";

    var userAgent = L.getUserAgent();


     /* IE 10 and Earlier and IE 11*/
    if (/MSIE|Trident/.test(userAgent)) {

        //toggle bookmarklet instructions for IE on favorites page
        document.querySelectorAll('#bookmarkletNotIE, .bookmarklet-not-ie').forEach(function(node) {
            node.style.display = 'none';
        });

        document.querySelectorAll('#bookmarkletIE, .bookmarklet-ie').forEach(function(node) {
            node.style.display = 'block';
        });
        
        if (document.querySelector('#live-chat-sticky-button')) {
            document.querySelector('#live-chat-sticky-button').style.display = 'none';
        }
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
