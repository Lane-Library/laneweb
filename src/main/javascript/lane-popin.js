//check if there is a query
if (LANE.search.getEncodedSearchString()) {
    //check if popInContent elm present
    YAHOO.util.Event.onAvailable('popInContent',function() {
		//custom onPopin event and handler
    	LANE.search.popin = new YAHOO.util.CustomEvent("onPopin");
    	function onPopinHandler(type,el){
    		var i, activeEl = 99, popinElms = YAHOO.util.Dom.getChildren(YAHOO.util.Dom.getChildren('popInContent')[0]);
    		//display precedence is determined by order of elements w/in popInContent markup
    		for (i = 0; i < popinElms.length; i++){
    			if(popinElms[i].style.display == 'inline'){
    				activeEl = i;
    			}
    		}
    		for (i= 0; i < popinElms.length; i++){
    			if(el[0].id === popinElms[i].id && i <= activeEl){
    				activeEl = i;
    				popinElms[i].style.display = 'inline';
    			}
    			else if (i > activeEl){
    				popinElms[i].style.display = 'none';
    			}
    		}
    	}
		LANE.search.popin.subscribe(onPopinHandler);
    });
}
