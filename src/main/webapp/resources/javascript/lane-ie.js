(function() {
    var node, ie = Y.UA.ie;
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
        if (ie < 9) {
            //this rotates the feedback link text
            node = Y.one(".feedback-link span");
            if (node) {
                node.setStyles({"filter": "progid:DXImageTransform.Microsoft.BasicImage(rotation=3)","width":"90px","top":"8px","left":"7px"});
            }
        }
        if (!Date.now) {
            Date.now = function() {
                return new Date().valueOf();
            }
        }
    }

})();
