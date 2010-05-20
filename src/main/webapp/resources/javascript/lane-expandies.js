YUI({
    gallery: 'gallery-2010.04.02-17-26'
}).use('gallery-node-accordion', 'plugin', 'node', 'anim', function(Y) {

    var expandies = Y.all('.expandy'),
        anchor = document.location.hash ? document.location.hash.substring(1) : false,
        i,
        ExpandyList = function(list) {
            var items, item, anchors, i, j, containsAnchor;
            list.addClass("yui3-accordion");
            items = list.get("children");
            for (i = 0; i < items.size(); i++) {
                item = items.item(i);
                if (item.get("children").size() == 2) {
                    containsAnchor = false;
                    if (anchor) {
                        anchors = item.all('a');
                        for (j = 0; j < anchors.size(); j++) {
                            if (anchor == anchors.item(j).get('name')) {
                                containsAnchor = true;
                                break;
                            }
                        }
                    }
                    new ExpandyItem(item, item.hasClass("expanded") || containsAnchor);
                }
            }
            return list;
        },
        ExpandyItem = function(item, expanded) {
            var i, anchor, anchors, children = item.get("children"),
                trigger = children.item(0);
            item.addClass("yui3-accordion-item");
            if (expanded) {
                item.addClass("yui3-accordion-item-active");
            }
            trigger.addClass("yui3-accordion-item-trigger yui3-accordion-item-hd");
            anchors = trigger.all("a");
            for (i = 0; i < anchors.size(); i++) {
                anchor = anchors.item(i);
                if (anchor.get("href")) {
                    anchor.on("click", function(event) {event.stopPropagation()});
                }
            }
            children.item(1).addClass("yui3-accordion-item-bd");
        };
    for (i = 0; i < expandies.size(); i++) {
        new ExpandyList(expandies.item(i)).plug(Y.Plugin.NodeAccordion, { anim: Y.Easing.backIn });
    }
});
