(function() {

    "use strict";

    var i, expandies = Y.all(".expandy");

    //only do this if there are expandies
    if (expandies.size() > 0) {

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
                            hash = Y.lane.Location.get("hash");
                        if (node.hasClass("expanded")) {
                            return true;
                        } else if (hash) {
                            hash = hash.substring(1);
                            return node.one("a[name=" + hash + "]") !== null;
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
                        anchor.on("click", this._handleTriggerClick);
                    }
                }
                this.get("hd").addClass("yui3-accordion-item-hd");
                this.get("bd").addClass("yui3-accordion-item-bd");
            },

            _handleTriggerClick : function(event) {
                event.stopPropagation();
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
                }
                node.addClass("yui3-accordion");
                node.plug(Y.Plugin.NodeAccordion, { anim: Y.Easing.backIn });
            }
        });

        // save an array of lists
        Y.lane.ExpandyLists = [];

        // instantiate them
        for (i = 0; i < expandies.size(); i++) {
            Y.lane.ExpandyLists.push(new ExpandyList({node : expandies.item(i)}));
        }

    }
})();
