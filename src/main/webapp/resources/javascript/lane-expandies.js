YUI({
    gallery: 'gallery-2010.04.02-17-26'
}).use('gallery-node-accordion', 'plugin', 'node', 'anim', function(Y) {

    var expandies = Y.all('.expandy'),
        expandy, i, j, children, panel;
    for (i = 0; i < expandies.size(); i++) {
        expandy = expandies.item(i);
        expandy.addClass('yui3-accordion');
        children = expandy.get('children');
        for (j = 0; j < children.size(); j++) {
            panel = children.item(j);
            panel.addClass('yui3-accordion-item');
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
