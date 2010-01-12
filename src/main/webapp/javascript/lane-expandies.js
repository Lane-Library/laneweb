(function(){
    LANE.namespace('expandies');
    LANE.expandies = function(){
        return {
            initialize: function(){
                var i, j, k, items, expanded, anchors, anchor = document.location.hash, expandies = YAHOO.util.Dom.getElementsByClassName('expandy');
                if (anchor) {
                    anchor = anchor.substring(1);
                }
                for (i = 0; i < expandies.length; i++) {
                    expanded = [];
                    items = YAHOO.util.Dom.getChildren(expandies[i]);
                    for (j = 0; j < items.length; j++) {
                        if (anchor) {
                            anchors = items[j].getElementsByTagName('A');
                            for (k = 0; k < anchors.length; k++) {
                                if (anchors[i].name == anchor) {
                                    YAHOO.util.Dom.addClass(items[j], 'expanded');
                                }
                            }
                        }
                        if (YAHOO.util.Dom.hasClass(items[j], 'expanded')) {
                            expanded.push(j);
                        }
                    }
                    new YAHOO.widget.AccordionView(expandies[i],{expandItems: expanded});
                    expandies[i].style.visibility = 'visible';
                }
            }
        };
    }();
        
    YAHOO.util.Event.onDOMReady(function(){
        LANE.expandies.initialize();
    });
})();