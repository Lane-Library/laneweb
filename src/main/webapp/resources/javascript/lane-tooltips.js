(function(){
    LANE.namespace('tooltips');
    LANE.tooltips = function(){
        return {
            initialize: function(){
                var tc, //tooltip container Elements
                    tt, //tooltips
                    e, //id of te element
                    w, //width of tooltip
                    i, j,
                    simpleTtIds = [], // array of contexts that need tt made from their title attribute 
                    YUD = YAHOO.util.Dom,
                    YUW = YAHOO.widget;

                tc = YUD.getElementsByClassName('tooltips');
        
                for(i = 0; i < tc.length; i++) {
                    tt = tc[i].childNodes;
                    for(j = 0; j < tt.length; j++) {
                        if(tt[j].nodeType == 1) {
                            e = tt[j].id.replace(/Tooltip$/,'');
                            if(e && YUD.inDocument(e) && !YUD.inDocument(e+"-yuitt")) {
                                document.getElementById(e).trackable = true;
                                // tooltips where content is in the markup (abstracts, etc.)
                                if(tt[j].innerHTML && tt[j].innerHTML != e.title){
                                	w = tt[j].style.width || '25%';
                                	new YUW.Tooltip(
                                			e+"-yuitt", 
                                			{
                                				context:e,
                                				width:w,
                                				autodismissdelay:60000,
                                				text:tt[j].innerHTML
                                			});
                                }
                                // just use title attribute for all other tooltips
                                else{
                                	simpleTtIds.push(e);
                                }
                            }
                        }
                    }
                    // simple, single tooltip for all contexts using title attribute
                	new YUW.Tooltip("simpleTT-yui", 
                			{
                				context:simpleTtIds,
                				autodismissdelay:60000
                			}
                	);
                }
            }
        }
    }();

    YAHOO.util.Event.onDOMReady(LANE.tooltips.initialize);
    
})();