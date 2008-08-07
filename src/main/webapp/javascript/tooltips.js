YAHOO.util.Event.addListener(window,'load',initializeTooltips);

function initializeTooltips(e){
    var tooltipEls = YAHOO.util.Dom.getElementsByClassName('tooltips'),
        y, i, tooltips, contextId, width;

    if(tooltipEls){
        for(y = 0; y<tooltipEls.length; y++){
            tooltips = tooltipEls[y].childNodes;
            for(i=0; i<tooltips.length; i++){
                if(tooltips[i].nodeType == 1){
                    contextId = tooltips[i].id.replace(/Tooltip$/,'');
                    if(contextId.length && YAHOO.util.Dom.inDocument(contextId)){
                        width = '25%';
                        if(tooltips[i].style.width){
                            width = tooltips[i].style.width;
                        }
                        new YAHOO.widget.Tooltip(
                                YAHOO.util.Dom.generateId(), 
                                {
                                    context:contextId,
                                    width:width,
                                    autodismissdelay:30000,
                                    text:tooltips[i].innerHTML
                                }
                            );
                    }
                }
            }
        }
    }
}
