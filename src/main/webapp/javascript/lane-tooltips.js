(function(){
    LANE.namespace('tooltips');
    LANE.tooltips = function(){
    	return {
    		initialize: function(){
	            var tc, //tooltip container Elements
		            tt, //tooltips
		            e, //id of te element
		            w, //width of tooltip
		            i, j;
			        tc = YAHOO.util.Dom.getElementsByClassName('tooltips');
		
		        for(i = 0; i < tc.length; i++) {
		            tt = tc[i].childNodes;
		            for(j = 0; j < tt.length; j++) {
		                if(tt[j].nodeType == 1) {
		                    e = tt[j].id.replace(/Tooltip$/,'');
		                    if(e && YAHOO.util.Dom.inDocument(e) && !YAHOO.util.Dom.inDocument(e+"-yuitt")) {
		                        document.getElementById(e).trackable = true;
		                        w = tt[j].style.width || '25%';
		                        new YAHOO.widget.Tooltip(
		                                e+"-yuitt", 
		                                {
		                                    context:e,
		                                    width:w,
		                                    autodismissdelay:60000,
		                                    text:tt[j].innerHTML
		                                });
		                    }
		                }
		            }
		        }
    		}
    	}
    }();
    	
    YAHOO.util.Event.addListener(window,'load', function() {
    	LANE.tooltips.initialize();
    });
    
})();