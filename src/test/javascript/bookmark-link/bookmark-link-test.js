YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", "node-pluginhost", function(Y) {

    "use strict";

    var bookmarkLinkTestCase = new Y.Test.Case({

        name : "BookmarkLink Test Case",

        link : L.BookmarkLink,

        setUp : function() {
            this.link.set("status", 0);
        },

        testExists : function() {
            Y.Assert.isTrue(Y.Lang.isObject(this.link));
        },

        testDontShowAlreadyBookmarked : function() {
            Y.one("#alreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual(0, this.link.get("status"));
        },

        testAnotherDontShowAlreadyBookmarked : function() {
            Y.one("#anotherAlreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual(0, this.link.get("status"));
        },

        testTargetMouseover : function() {
            var bookmarkable = Y.one("#bookmarkable");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame(1, this.link.get("status"));
            Y.Assert.areSame(bookmarkable._node, this.link.get("target")._node);
        },

        testTargetBlock : function() {
            var bookmarkable = Y.one("#block");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame(0, this.link.get("status"));
        },

        testTargetNoBookmarking : function() {
            var bookmarkable = Y.one("a.no-bookmarking");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame(0, this.link.get("status"));
        },

        testTargetAncestorNoBookmarking : function() {
            var bookmarkable = Y.one("div.no-bookmarking a");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame(0, this.link.get("status"));
        },

        testTargetInlineBlock : function() {
            var bookmarkable = Y.one("#inline-block");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame(1, this.link.get("status"));
            Y.Assert.areSame(bookmarkable._node, this.link.get("target")._node);
        },

        testTargetMouseout : function() {
            var bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", 1);
            bookmarkable.simulate("mouseout");
            Y.Assert.areSame(5, this.link.get("status"));
        },

        testNodeMouseover : function() {
            var bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", 5);
            var event = document.createEvent("MouseEvents");
            event.initMouseEvent("mouseover")
            this.link.get("node")._node.dispatchEvent(event);
            Y.Assert.areSame(2, this.link.get("status"));
        },

        testNodeMouseout : function() {
            var bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", 2);
            var event = document.createEvent("MouseEvents");
            event.initMouseEvent("mouseout")
            this.link.get("node")._node.dispatchEvent(event);
            Y.Assert.areSame(5, this.link.get("status"));
        },

        testClick : function() {
            var bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", 2);
            var self = this;
            var status = 0;
            var data = "";
            L.io = function(url, config) {
                status = self.link.get("status");
                data = config.data;
                config.on.success.apply(config.context, [ 0, {} ]);
            };
            this.link.get("node")._node.click();
            Y.Assert.areSame(3, status);
            Y.Assert.areSame(4, this.link.get("status"));
            Y.Assert
            .areSame(
                    '{"label":"bookmarkable","url":"http://www.example.com/"}',
                    data);
        },

        testLocalClick : function() {
            var bookmarkable = Y.one("#local");
            this.link.set("target", bookmarkable);
            this.link.set("status", 2);
            var self = this;
            var data = "";
            var status = 0;
            L.io = function(url, config) {
                status = self.link.get("status");
                data = config.data;
                config.on.success.apply(config.context, [ 0, {} ]);
            };
            this.link.get("node")._node.click();
            Y.Assert.areSame(3, status);
            Y.Assert.areSame(4, this.link.get("status"));
            Y.Assert.areSame('{"label":"local","url":"/foo?bar=baz"}', data);
        },

        testTimer : function() {
            this.link.set("status", 5);
            this.wait(function() {
                Y.Assert.areSame(0, this.link.get("status"));
            }, this.link.get("hideDelay"));
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(bookmarkLinkTestCase);
    Y.Test.Runner.masterSuite.name = "bookmark-link-test.js";
    Y.Test.Runner.run();

});
