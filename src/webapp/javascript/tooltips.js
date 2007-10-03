YAHOO.util.Event.addListener(window,'load',initializeTooltips);

function initializeTooltips(e){
try {
    var tooltipEls = YAHOO.util.Dom.getElementsByClassName('tooltips');

    if(tooltipEls){
        for(var y = 0; y<tooltipEls.length; y++){
            var tooltips = tooltipEls[y].childNodes;
            for(var i=0; i<tooltips.length; i++){
                if(tooltips[i].nodeType == 1){
                    var contextId = tooltips[i].id.replace(/Tooltip$/,'');
                    if(contextId.length && YAHOO.util.Dom.inDocument(contextId)){
                        new YAHOO.widget.Tooltip(YAHOO.util.Dom.generateId(), { context:contextId, width:'25%', text:document.getElementById(tooltips[i].id).innerHTML } );
                    }
                }
            }
        }
    }
   } catch(exception) { handleException(exception) }    
}
