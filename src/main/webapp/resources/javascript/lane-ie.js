(function() {
    var Y = LANE.Y, i, j, menus, nodes, node;
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
        //add ie6Feedback to feedbackLink to get position : absolute from feedback.css
        node = Y.one("#feedbackLink");
        if (node) {
        	node.addClass("ie6Feedback");
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
    	//add rounded bottom left corner to highlighted resources
    	node = Y.one("#highlightedResources");
    	if (node) {
    		node.append("<img id='highlightedResourcesIECorner' src='/././resources/images/highlightedResourcesIECorner.png'/>");
    		//TODO: possible further IE6 corrections
    	}
    }
})();
