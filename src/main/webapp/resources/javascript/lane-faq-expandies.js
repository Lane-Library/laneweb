if (!document.ELEMENT_NODE) {
    document.ELEMENT_NODE = 1;
    document.ATTRIBUTE_NODE = 2;
    document.TEXT_NODE = 3;
    document.CDATA_SECTION_NODE = 4;
    document.ENTITY_REFERENCE_NODE = 5;
    document.ENTITY_NODE = 6;
    document.PROCESSING_INSTRUCTION_NODE = 7;
    document.COMMENT_NODE = 8;
    document.DOCUMENT_NODE = 9;
    document.DOCUMENT_TYPE_NODE = 10;
    document.DOCUMENT_FRAGMENT_NODE = 11;
    document.NOTATION_NODE = 12;
}

document._importNode = function(node, allChildren) {
    /* find the node type to import */
    switch (node.nodeType) {
        case document.ELEMENT_NODE:
            /* create a new element */
            var newNode = document.createElement(node.nodeName);
            /* does the node have any attributes to add? */
            if (node.attributes && node.attributes.length > 0)
                /* add all of the attributes */
                for (var i = 0, il = node.attributes.length; i < il;) {
//                    alert(node.nodeName + "\n" + node.attributes[i].nodeName + "=" + node.getAttribute(node.attributes[i].nodeName));
                    newNode.setAttribute(node.attributes[i].nodeName, node.getAttribute(node.attributes[i++].nodeName));
                }
            /* are we going after children too, and does the node have any? */
            if (allChildren && node.childNodes && node.childNodes.length > 0)
                /* recursively get all of the child nodes */
                for (var i = 0, il = node.childNodes.length; i < il;)
                    newNode.appendChild(document._importNode(node.childNodes[i++], allChildren));
            return newNode;
            break;
        case document.TEXT_NODE:
        case document.CDATA_SECTION_NODE:
        case document.COMMENT_NODE:
            return document.createTextNode(node.nodeValue);
            break;
    }
};
YUI({
    gallery: 'gallery-2010.04.02-17-26'
}).use('gallery-node-accordion', 'plugin', 'node', 'anim', "io", function(Y) {


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
                            var result;
                                result = o.responseXML.documentElement;
                                if (document.importNode) {
                                    result = document.importNode(result, true);
                                } else  {
                                    result = document._importNode(result, true);
                                }
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
            } else {
                eventHandle = anchor.on("click", function(e) {
                    e.preventDefault();
                    Y.io("/././plain/howto/index.html?" + href.substring(href.indexOf("?") + 1), {
                        on: {
                            success: function(id, o, arguments) {
                                var i, items, j, present, item = arguments.anchor.get("parentNode"), list = item.get("parentNode"), initialItems = list.all("li");
                                arguments.anchor.detach(eventHandle);
                                items = new Y.Node(o.responseXML).all(".middleColumn .bd li");
                                for (i = 0; i < items.size(); i++) {
                                    present = false;
                                    item = items.item(i);
                                    for (j = 0; j < initialItems.size() - 1; j++) {
                                        if (initialItems.item(j).one("a").get("href") == item.one("a").get("href")) {
                                            present = true;
                                            break;
                                        }
                                    }
                                    if (!present) {
                                        list.append(items.item(i).cloneNode(true));
                                    }
                                }
                            },
                            failure: function() {
                                document.location = argument.anchor.get("href");
                            }
                        },
                        arguments: {
                            anchor: this
                        }
                    });
                });
            }
        }
        expandy.plug(Y.Plugin.NodeAccordion, {
            anim: Y.Easing.backIn
        });
    }
});
