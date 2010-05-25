YUI({
    gallery: 'gallery-2010.04.02-17-26'
}).use("lane", 'gallery-node-accordion', 'plugin', 'node', 'anim', "io", function(Y) {


    var expandies = Y.all('.faq-expandy'), expandy, i, j, anchor, href, faqId, children, panel, eventHandle;
    for (i = 0; i < expandies.size(); i++) {
        expandy = expandies.item(i).one("ul");
        expandy.addClass('yui3-accordion');
        children = expandy.get('children');
        for (j = 0; j < children.size(); j++) {
            panel = children.item(j);
            anchor = panel.one("a");
            href = anchor.get("href");
            faqId = parseInt(href.substring(href.indexOf("=") + 1), 10);
            if (Y.Lang.isNumber(faqId)) {
                panel.addClass('yui3-accordion-item');
                anchor.addClass('yui3-accordion-item-trigger yui3-accordion-item-hd');
                panel.append("<div class='yui3-accordion-item-bd'/>")
                Y.io("/././plain/howto/index.html?id=" + faqId, {
                    on: {
                        success: function(id, o, arguments) {
                            var result = o.responseXML.documentElement;
                            result = document.importNode(result, true);
                            result = new Y.Node(result);
                            result = result.all("div").item(5);
                            arguments.panel.append(result);
                        },
                        failure: function(id, o, arguments) {
                            arguments.panel.set("innerHTML", "faq not found");
                        }
                    },
                    arguments: {
                        panel: panel.one(".yui3-accordion-item-bd")
                    }
                });
            }
//            else {
//                eventHandle = anchor.on("click", function(e) {
//                    e.preventDefault();
//                    Y.io("/././plain/howto/index.html?" + href.substring(href.indexOf("?") + 1), {
//                        on: {
//                            success: function(id, o, arguments) {
//                                var i, items, j, present, item = arguments.anchor.get("parentNode"), list = item.get("parentNode"), initialItems = list.all("li");
//                                arguments.anchor.detach(eventHandle);
//                                items = o.responseXML.documentElement;
//                                items = document.importNode(items, true);
//								items = new Y.Node(items);
//								items = items.all("li");
//                                for (i = 0; i < items.size(); i++) {
//                                    present = false;
//                                    item = items.item(i);
//                                    for (j = 0; j < initialItems.size() - 1; j++) {
//                                        if (initialItems.item(j).one("a").get("href") == item.one("a").get("href")) {
//                                            present = true;
//                                            break;
//                                        }
//                                    }
//                                    if (!present) {
//                                        list.append(items.item(i));
//                                    }
//                                }
//                            },
//                            failure: function() {
//                                document.location = argument.anchor.get("href");
//                            }
//                        },
//                        arguments: {
//                            anchor: this
//                        }
//                    });
//                });
//            }
        }
        expandy.plug(Y.Plugin.NodeAccordion, {
            anim: Y.Easing.backIn
        });
    }
});
