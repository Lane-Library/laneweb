(function() {
    var i, j, menus, nodes, node;
    if (Y.UA.ie && Y.UA.ie < 7) {
        //instead of :first-child selector remove background image:
        node = Y.one('#stanfordMedicineBrand');
        if (node) {
            node.setStyle('backgroundImage', 'none');
        }
        //set up hover class on somNav and laneNav menus:
        menus = Y.all("#somNav, #laneNav");
        for (j = 0; j < menus.size(); j++) {
            node = menus.item(j);
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
        nodes = Y.all(".type1 .details > div");
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).addClass("child");
        }
        node = Y.one("#feedbackLink");
        if (node) {
        	node.setStyle("position", "absolute");
        	node.setStyle("right", "0px");
        	node.setStyle("top", "200px");
        }
    }
    if (Y.UA.ie && Y.UA.ie <= 8) {
        //mimic .module:after so subsequent elements are cleared
        nodes = Y.all(".module");
        for (i = 0; i < nodes.size(); i++) {
            nodes.item(i).append(Y.Node.create("<span class='after'/>"));
        }
    }
    //TODO: figure out why this doesn't work:
    if (Y.UA.ie) {
    	//IE doesn't know about rgba, set background color without opacity:
    	node = Y.one(".banner-nav-content");
    	if (node) {
    		node.on("mouseover", function() {this.setStyle("background-color","black")});
    	}
    }
})();
