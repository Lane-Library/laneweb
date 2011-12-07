(function() {

    var i, anims, expandies = Y.all(".expandy");

    //only do this if there are expandies
    if (expandies.size() > 0) {
        
        if (Y.UA.ie && Y.UA.ie < 8) {
            //IE 7 fails to redraw footer, etc unless we do this:
            anims  = {};
            Y.Plugin.NodeAccordion.prototype._animate = function(id, conf, fn) {
            	var anim = anims[id], nodes = Y.all(".sb-tb");
            	if ((anim) && (anim.get ('running'))) {
            		anim.stop();
            	}
            	if (Y.Lang.isFunction(this.get("anim"))) {
            		conf.easing = this.get("anim");
            	}
            	anim = new Y.Anim(conf);
            	anim.on('end', fn, this);
            	if (nodes) {
            		//toggle display style to force redraw
            		anim.on("end", function() {
            			nodes.setStyle("display", "none");
            			nodes.setStyle("display", "block");
            		}, this);
            	}
            	anim.run();
            	anims[id] = anim;
            	return anim;
            };
        }

        /**
         * An ExpandyItem represents a single item in an expandy list
         * @constructor
         * @base Y.Base
         */
        var ExpandyItem = function() {
            ExpandyItem.superclass.constructor.apply(this, arguments);
        };

        /**
         * @final
         * the class name
         */
        ExpandyItem.NAME = "expandy-item";

        /**
         * @final
         * the default attributes
         */
        ExpandyItem.ATTRS = {
                node : {
                    value : null
                },
                expanded : {
                    valueFn : function() {
                        var node = this.get("node"),
                            hash = document.location.hash ? document.location.hash.substring(1) : "",
                            anchors, i, size;
                        if (node.hasClass("expanded")) {
                            return true;
                        } else if (hash) {
                            anchors = node.all("a");
                            size = anchors.size();
                            for (i = 0; i < size; i++) {
                                if (hash === anchors.item(i).get("name")) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                },
                trigger : {
                    valueFn : function() {
                        return this.get("node").get("children").item(0);
                    }
                },
                hd : {
                    valueFn : function() {
                        return this.get("node").get("children").item(0);
                    }
                },
                bd : {
                    valueFn : function() {
                        return this.get("node").get("children").item(1);
                    }
                }
        };

        /**
         * extend Y.Base
         */
        Y.extend(ExpandyItem, Y.Base, {
            
            /**
             * the initializer adds the appropriate class names
             */
            initializer : function() {
                var trigger = this.get("trigger"),
                    node = this.get("node"),
                    anchors, anchor, i, size;
                node.addClass("yui3-accordion-item");
                if (this.get("expanded")) {
                    node.addClass("yui3-accordion-item-active");
                }
                trigger.addClass("yui3-accordion-item-trigger");
                //stop propogation (expandy actions) if click is on link in trigger:
                anchors = trigger.all("a");
                size = anchors.size();
                for (i = 0; i < size; i++) {
                    anchor = anchors.item(i);
                    if (anchor.get("href") && !anchor.get("rel")) {
                        anchor.on("click", function(event) {event.stopPropagation();});
                    }
                }
                this.get("hd").addClass("yui3-accordion-item-hd");
                this.get("bd").addClass("yui3-accordion-item-bd");
            }
        });

        /**
         * An expandy list
         * @constructor
         * @base Y.Base
         */
        var ExpandyList = function() {
            ExpandyList.superclass.constructor.apply(this, arguments);
        };

        /**
         * @final
         * the class name
         */
        ExpandyList.NAME = "expandy";

        /**
         * @final
         * default attributes
         */
        ExpandyList.ATTRS = {
                items : {
                    value : []
                },
                node : {
                    value : null
                }
        };

        /**
         * extend Y.Base
         */
        Y.extend(ExpandyList, Y.Base, {
            
            /**
             * creates the individual ExpandyItems and plugs in NodeAccordion
             */
            initializer : function() {
                var node = this.get("node"),
                    children = node.get("children"),
                    size = children.size(),
                    items = this.get("items"),
                    i, child;
                for (i = 0; i < size; i++) {
                    child = children.item(i);
                    //don't create ExpandyItem if not two children:
                    if (child.get("children").size() === 2) {
                        items.push(new ExpandyItem({node : child}));
                    }
                };
                node.addClass("yui3-accordion");
                node.plug(Y.Plugin.NodeAccordion, { anim: Y.Easing.backIn });
            }
        });

        // save an array of lists
        Y.lane.ExpandyLists = [];

        // instatiate them
        for (i = 0; i < expandies.size(); i++) {
            Y.lane.ExpandyLists.push(new ExpandyList({node : expandies.item(i)}));
        }
        
    //TODO: tighten this up a bit:
    var externalTriggers = Y.all(".expandy-trigger");
    for (i = 0; i < externalTriggers.size(); i++) {
        externalTriggers.item(i).on("click", function(event) {
            event.preventDefault();
            var hash = event.target.get("hash");
            var anchor = Y.one(hash);
            var item = anchor.ancestor(".yui3-accordion-item");
            var trigger = item.one(".yui3-accordion-item-trigger");
            var accordion = item.ancestor(".yui3-accordion");
            accordion.accordion.expandItem(item);
            var href = event.target.get("href");
            var setLocation = function() {
                window.location = href;
            };
            window.setTimeout(setLocation, 500);
        });
    }

    }
})();
