(function() {

    /* javascript to set the min-height of a group of boxes to the that of the tallest one.
     * The boxes are grouped using the same-height-n class where n is a number 0-9 */

    "use strict";

    var maxHeight,
        // get all the nodes with same-height-n
        nodes = document.querySelectorAll("*[class^='same-height-'], *[class*=' same-height-']"),
        heights,
        i,
        clazz,
        classes = {},
        regex = /.*(same-height-\d).*/,
        matches,
        smallmedia = window.matchMedia("(max-width: 738px)")
        ;

    
    if( ! smallmedia){
		 // find the class for each group
		    
	    for (i = 0; i < nodes.length; i++) {
	        matches = regex.exec(nodes.item(i).className);
	        if (matches.length === 2) {
	            classes[matches[1]] = "." + matches[1];
	        }
	    }
	
	    for (clazz in classes) {
	        if (classes.hasOwnProperty(clazz)) {
	            // for each group find the tallest
	            nodes = document.querySelectorAll(classes[clazz]);
	            heights = [];
	            nodes.forEach(function(node) {
	                heights.push(node.clientHeight);
	            });
	            maxHeight = Math.max.apply(null, heights);
	            // set all of the group to the tallest's height
	            nodes.forEach(function(node) {
	                node.style.minHeight = maxHeight + "px";
	            });
	        }
	    }
	}

})();
