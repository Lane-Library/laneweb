(function() {
    LANE.namespace('search.popin');

	// custom onPopin event
	LANE.search.popin = new YAHOO.util.CustomEvent("onPopin");
	
	//list of all possible popin elements, in display precedence order
    var popinElms = ['spellCheck','queryMapping','findIt'],
		// TODO: could make markup order dictate precedence 
		//       (find elm and then fetch parent of elm to see if other popin children
		//       popInContent id NOT always present when popin required)
		onPopinHandler = function(type,el){
		var i, activeEl = 99, elms = YAHOO.util.Dom.get(popinElms);
		for (i = 0; i < elms.length; i++){
			if(elms[i].style.display == 'inline'){
				activeEl = i;
			}
		}
		for (i= 0; i < elms.length; i++){
			if(el[0].id === elms[i].id && i <= activeEl){
				activeEl = i;
				elms[i].style.display = 'inline';
			}
			else if (i > activeEl){
				elms[i].style.display = 'none';
			}
		}
	};
	LANE.search.popin.subscribe(onPopinHandler);
})();

/*
//TODO: revert ... don't want to call onAvailable function once for each element 
// //check if there is a query
if (LANE.search.getEncodedSearchString()) {
	//list of all possible popin elements, in display precendence order
    var popinElms = ['spellCheck','queryMapping','findIt'];
    //check if any of our popin content elements are present in the DOM
    YAHOO.util.Event.onAvailable(popinElms,function() {
		//custom onPopin event and handler
    	LANE.search.popin = new YAHOO.util.CustomEvent("onPopin");
    	function onPopinHandler(type,el){
    		//TODO: make order of elements dictate display precendence
    		var i, activeEl = 99, elms = YAHOO.util.Dom.get(popinElms);
    		for (i = 0; i < elms.length; i++){
    			if(elms[i].style.display == 'inline'){
    				activeEl = i;
    			}
    		}
    		for (i= 0; i < elms.length; i++){
    			if(el[0].id === elms[i].id && i <= activeEl){
    				activeEl = i;
    				elms[i].style.display = 'inline';
    			}
    			else if (i > activeEl){
    				elms[i].style.display = 'none';
    			}
    		}
    	}
		LANE.search.popin.subscribe(onPopinHandler);
    });
}
*/