(function() {
    var node;
    if (!String.prototype.trim) {
        String.prototype.trim=function(){return this.replace(/^\s+|\s+$/g, '');};
    }
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
