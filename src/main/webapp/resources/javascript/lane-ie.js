(function() {
    var i, nodes, node;
    if (Y.UA.ie && Y.UA.ie < 7) {
        node = Y.one("#laneNav");
        nodes = node.all('ul');
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).get('parentNode').on('mouseover', function() {
                this.addClass('hover');
            });
            nodes.item(i).get('parentNode').on('mouseout', function() {
                this.removeClass('hover');
            });
        }
        //add hover class to favorites menu
        node = Y.one("#favorites");
        if (node) {
            node.on('mouseover', function() {
                this.addClass('hover');
            });
            node.on('mouseout', function() {
                this.removeClass('hover');
            });
        }
        //instead of :first-child selector remove left border on .nav2, #login and #libraryContact menus:
        nodes = Y.all('.nav2, #libraryContact, #login');
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).one('li').setStyle('borderLeft', 'none');
        }
    }
    if (Y.UA.ie && Y.UA.ie <= 8) {
        //mimic .module:after so subsequent elements are cleared
        nodes = Y.all(".module");
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).append("<span class='after'/>");
        }
        node = Y.one("#topResources");
        if (node) {
            node.insert("<span class='after'/>", "after");
        }
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
