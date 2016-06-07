(function() {

    "use strict";

    var node;
    if (Y.UA.ie) {

        //toggle bookmarklet instructions for IE on favorites page
        node = document.querySelector("#bookmarkletNotIE");
        if (node) {
            node.style.display = "none";
        }
        node = document.querySelector("#bookmarkletIE");
        if (node) {
            node.style.display = "block";
        }
    }

})();
