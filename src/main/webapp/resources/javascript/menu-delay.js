/*
 * A simple script that delays the visibility of the drop downs for a short time
 */
(function() {
    
    Y.all("#laneNav>li, #somNav>li").on("mouseenter", function(event) {
    	var list = event.currentTarget.one("ul");
    	if (list) {
    		list.setStyle("visibility", "hidden");
    		setTimeout(function() {
    			list.setStyle("visibility", "visible");
    		});
    	}
    });

})();