(function () {

    "use strict";

    let userAgent = L.getUserAgent();



    if (/Edge/.test(userAgent)) {

        //toggle bookmarklet instructions for IE on favorites page
        document.querySelectorAll('#bookmarklet').forEach(function (node) {
            node.style.display = 'none';
        });

        document.querySelectorAll('.bookmarklet-edge').forEach(function (node) {
            node.style.display = 'block';
        });
    }

})();
