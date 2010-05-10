YUI({
    gallery: 'gallery-2010.04.02-17-26'
}).use('gallery-node-accordion', 'plugin', 'node', 'anim', function(Y) {

    var expandies = Y.all('.expandy'),
        anchor = document.location.hash,
        anchors, expandy, i, j, k, children, panel;
        
    if (anchor) {
        anchor = anchor.substring(1);
    }
    for (i = 0; i < expandies.size(); i++) {
        expandy = expandies.item(i);
        expandy.addClass('yui3-accordion');
        children = expandy.get('children');
        for (j = 0; j < children.size(); j++) {
            panel = children.item(j);
            panel.addClass('yui3-accordion-item');
            if (anchor) {
                anchors = panel.all('a');
                for (k = 0; k < anchors.size(); k++) {
                    if (anchor == anchors.item(k).get('name')) {
                        panel.addClass('expanded');
                        break;
                    }
                }
            }
            if (panel.hasClass('expanded')) {
                panel.addClass(' yui3-accordion-item-active');
            }
            panel.get('children').item(0).addClass('yui3-accordion-item-trigger yui3-accordion-item-hd');
            panel.get('children').item(1).addClass('yui3-accordion-item-bd');
        }
        expandy.plug(Y.Plugin.NodeAccordion, {
            anim: Y.Easing.backIn
        });
    }
});
