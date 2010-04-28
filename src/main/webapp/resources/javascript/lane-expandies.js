YUI().add('lane-expandies', function(Y){
//YUI().use('accordionview', 'event','node', function(Y) {
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
                if (!expandies.item(i).get('id')) {
                    expanded = [];
                    items = expandies.item(i).get('children');//YUD.getChildren(expandies[i]);
                    for (j = 0; j < items.size(); j++) {
                        if (anchor) {
                            anchors = items.item(j).all('A');
                            for (k = 0; k < anchors.size(); k++) {
                                if (anchors.item(k).get('name') == anchor) {
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
        Y.Global.on('lane:change', createExpandies);
},'1.11.0-SNAPSHOT',{requires:['accordionview', 'event','node']});
