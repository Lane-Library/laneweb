YUI().use('node', function(Y) {
    if (Y.UA.ie && Y.UA.ie < 7) {
        //instead of :first-child selector remove background image:
        var i, nodes,
            node = Y.one('#stanfordMedicineBrand'),
            afterSpan = "<span class='after'/>";
        if (node) {
            node.setStyle('backgroundImage', 'none');
        }
        //set up hover class on somNav menu:
        node = Y.one('#somNav');
        if (node) {
            nodes = node.all('ul');
            for (i = 0; i < nodes.size(); i++) {
                nodes.item(i).get('parentNode').on('mouseover', function() {
                    this.addClass('hover');
                });
                nodes.item(i).get('parentNode').on('mouseout', function() {
                    this.removeClass('hover');
                });
            }
        }
        //instead of :first-child selector remove left border on .nav2 menus:
        nodes = Y.all('.nav2, #libraryContact');
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).one('li').setStyle('borderLeft', 'none');
        }
        //mimic .module:after so subsequent elements are cleared
        nodes = Y.all(".module");
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).append(Y.Node.create(afterSpan));
        }
    }
});
