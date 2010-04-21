
YUI().use('yui2-event','yui2-dom', function(Y) {
    Y.YUI2.util.Event.onDOMReady(function() {
        var createExpandies = function() {
            var i, j, k, items,
                expanded, anchors, expandy,
                anchor = document.location.hash,
                expandies = Y.YUI2.util.Dom.getElementsByClassName('expandy'),
                YUD = Y.YUI2.util.Dom;
            if (anchor) {
                anchor = anchor.substring(1);
            }
            for (i = 0; i < expandies.length; i++) {
                //create only if no id (id means already created):
                if (!expandies[i].id) {
                    expanded = [];
                    items = YUD.getChildren(expandies[i]);
                    for (j = 0; j < items.length; j++) {
                        if (anchor) {
                            anchors = items[j].getElementsByTagName('A');
                            for (k = 0; k < anchors.length; k++) {
                                if (anchors[i].name == anchor) {
                                    YUD.addClass(items[j], 'expanded');
                                }
                            }
                        }
                        if (YUD.hasClass(items[j], 'expanded')) {
                            expanded.push(j);
                        }
                    }
                    expandy = new LANE.expandy.AccordionView(expandies[i], {
                        expandItems: expanded
                    });
                    expandy.addListener('panelOpen', function(object) {
                        YUD.addClass(object.panel,'expanded');
                    });
                    expandy.addListener('afterPanelClose', function(object) {
                        YUD.removeClass(object.panel,'expanded');
                    });
                }
            }
        };
        createExpandies();
        LANE.core.getChangeEvent().subscribe(createExpandies);
    });
    });
