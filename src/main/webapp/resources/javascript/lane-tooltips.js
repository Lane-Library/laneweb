YUI().use('node','yui2-container',function(Y) {
    var tooltips = [],
        destroyTooltips = function() {
            while (tooltips.length > 0) {
                tooltips.pop().destroy();
            }
        },
        createTooltips = function() {
            var tooltipContainerNodeList, // array of tooltip containers
                tooltipContainer, // tooltips
                tooltipId, // id of te element
                tooltipWidth, // width of tooltip
                simpleTooltipIds = [], // array of contexts that need tt made
                                       // from their title attribute
                tooltipElement,
                YUW = Y.YUI2.widget,
                i, j;
            
            tooltipContainerNodeList = Y.all('.tooltips');
            
            for (i = 0; i < tooltipContainerNodeList.size(); i++) {
                tooltipContainer = tooltipContainerNodeList.item(i).get('childNodes');
                for (j = 0; j < tooltipContainer.size(); j++) {
                    if (tooltipContainer.item(j).get('nodeType') == 1) {
                        tooltipId = tooltipContainer.item(j).get('id').replace(/Tooltip$/, '');
                        tooltipElement = Y.one('#' + tooltipId);
                        if (tooltipElement) {
                            tooltipElement.setAttribute('trackable',true);
                            // tooltips where content is in the markup
                            // (abstracts, etc.)
                            if (tooltipContainer.item(j).get('innerHTML')) {
                                if (tooltipContainer.item(j).getStyle('width') != 'auto') {
                                    tooltipWidth = tooltipContainer.item(j).getStyle('width');
                                } else if (tooltipContainer.item(j).get('innerHTML').length > 500) {
                                    tooltipWidth = '60%';
                                } else {
                                    tooltipWidth = '25%';
                                }
                                tooltips.push(new YUW.Tooltip(tooltipId + "-yuitt", {
                                    context: Y.Node.getDOMNode(tooltipElement),
                                    width: tooltipWidth,
                                    autodismissdelay: 60000,
                                    text: tooltipContainer.item(j).get('innerHTML')
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
