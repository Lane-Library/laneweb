(function() {
    var node;
    if (Y.UA.ie) {

        //toggle bookmarklet instructions for IE on favorites page
        node = Y.one("#bookmarkletNotIE");
        if (node) {
            node.setStyle("display", "none");
        }
        node = Y.one("#bookmarkletIE");
        if (node) {
            node.setStyle("display", "block");
        }
    }

})();
