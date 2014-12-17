(function() {
    var node, ie = Y.UA.ie;
    if (!String.prototype.trim) {
        String.prototype.trim=function(){return this.replace(/^\s+|\s+$/g, '');};
    }
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
                node.setStyles({"filter": "progid:DXImageTransform.Microsoft.BasicImage(rotation=3)","width":"70px","top":"16px","left":"7px"});
            }
        }
    }

})();
