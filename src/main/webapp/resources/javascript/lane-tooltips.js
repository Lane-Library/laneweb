YUI().use('yui2-dom','yui2-container',function(Y) {
    var tooltips = [],
        destroyTooltips = function() {
            while (tooltips.length > 0) {
                tooltips.pop().destroy();
            }
        },
        createTooltips = function() {
            var tooltipContainerArray, // array of tooltip containers
                tooltipContainer, // tooltips
                tooltipId, // id of te element
                tooltipWidth, // width of tooltip
                simpleTooltipIds = [], // array of contexts that need tt made
                                       // from their title attribute
                tooltipElement,
                YUD = Y.YUI2.util.Dom,
                YUW = Y.YUI2.widget,
                i, j;
            
            tooltipContainerArray = YUD.getElementsByClassName('tooltips');
            
            for (i = 0; i < tooltipContainerArray.length; i++) {
                tooltipContainer = tooltipContainerArray[i].childNodes;
                for (j = 0; j < tooltipContainer.length; j++) {
                    if (tooltipContainer[j].nodeType == 1) {
                        tooltipId = tooltipContainer[j].id.replace(/Tooltip$/, '');
                        if (tooltipId && YUD.inDocument(tooltipId)) {
                            tooltipElement = document.getElementById(tooltipId);
                            tooltipElement.trackable = true;
                            // tooltips where content is in the markup
                            // (abstracts, etc.)
                            if (tooltipContainer[j].innerHTML) {
                                if (tooltipContainer[j].style.width) {
                                    tooltipWidth = tooltipContainer[j].style.width;
                                } else if (tooltipContainer[j].innerHTML.length > 500) {
                                    tooltipWidth = '60%';
                                } else {
                                    tooltipWidth = '25%';
                                }
                                tooltips.push(new YUW.Tooltip(tooltipId + "-yuitt", {
                                    context: tooltipElement,
                                    width: tooltipWidth,
                                    autodismissdelay: 60000,
                                    text: tooltipContainer[j].innerHTML
                                }));
                            } else {
                                // just use title attribute for all other
                                // tooltips
                                simpleTooltipIds.push(tooltipId);
                            }
                        }
                    }
                }
                // simple, single tooltip for all contexts using title
                // attribute
                if (simpleTooltipIds.length > 0) {
                    tooltips.push(new YUW.Tooltip("simpleTT-yui", {
                        context: simpleTooltipIds,
                        autodismissdelay: 60000
                    }));
                }
            }
        };
    LANE.core.getChangeEvent().subscribe(function() {
       destroyTooltips();
       createTooltips(); 
    });
    Y.YUI2.util.Event.onDOMReady(createTooltips);
    
});
