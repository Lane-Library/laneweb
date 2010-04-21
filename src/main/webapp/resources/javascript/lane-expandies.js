YUI().use('yui2-event','node', function(Y) {
    Y.YUI2.util.Event.onDOMReady(function() {
        var createExpandies = function() {
            var i, j, k, items,
                expanded, anchors, expandy,
                anchor = document.location.hash,
                expandies = Y.all('.expandy');
            if (anchor) {
                anchor = anchor.substring(1);
            }
            for (i = 0; i < expandies.size(); i++) {
                //create only if no id (id means already created):
                if (!expandies.item(i).getAttribute('id')) {
                    expanded = [];
                    items = expandies.item(i).get('children');//YUD.getChildren(expandies[i]);
                    for (j = 0; j < items.size(); j++) {
                        if (anchor) {
                            anchors = items.item(j).all('A');
                            for (k = 0; k < anchors.size(); k++) {
                                if (anchors.item(k).getAttribute('name') == anchor) {
                                    items.get(j).addClass('expanded');
                                }
                            }
                        }
                        if (items.item(j).hasClass('expanded')) {
                            expanded.push(j);
                        }
                    }
                    expandy = new LANE.expandy.AccordionView(Y.Node.getDOMNode(expandies.item(i)), {
                        expandItems: expanded
                    });
                    expandy.addListener('panelOpen', function(object) {
                        new Y.Node(object.panel).addClass('expanded');
                    });
                    expandy.addListener('afterPanelClose', function(object) {
                        new Y.Node(object.panel).removeClass('expanded');
                    });
                }
            }
        };
        createExpandies();
        LANE.core.getChangeEvent().subscribe(createExpandies);
    });
});
