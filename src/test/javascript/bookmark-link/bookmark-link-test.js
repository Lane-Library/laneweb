YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", "node-pluginhost", function(Y) {

    "use strict";

    let bookmarkLinkTestCase = new Y.Test.Case({

        name : "BookmarkLink Test Case",

        link : L.BookmarkLink,

        setUp : function() {
            this.link.set("status", "off");
        },

        testExists : function() {
            Y.Assert.isTrue(Y.Lang.isObject(this.link));
        },

        testDontShowAlreadyBookmarked : function() {
            Y.one("#alreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual("off", this.link.get("status"));
        },

        testAnotherDontShowAlreadyBookmarked : function() {
            Y.one("#anotherAlreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual("off", this.link.get("status"));
        },

        testTargetMouseover : function() {
            let bookmarkable = Y.one("#bookmarkable");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("ready", this.link.get("status"));
            Y.Assert.areSame(bookmarkable._node, this.link.get("target")._node);
        },

        testTargetBlock : function() {
            let bookmarkable = Y.one("#block");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("off", this.link.get("status"));
        },

        testTargetNoBookmarking : function() {
            let bookmarkable = Y.one("a.no-bookmarking");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("off", this.link.get("status"));
        },

        testTargetAncestorNoBookmarking : function() {
            let bookmarkable = Y.one("div.no-bookmarking a");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("off", this.link.get("status"));
        },

        testTargetInlineBlock : function() {
            let bookmarkable = Y.one("#inline-block");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("ready", this.link.get("status"));
            Y.Assert.areSame(bookmarkable._node, this.link.get("target")._node);
        },

        testTargetMouseout : function() {
            let bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", "ready");
            bookmarkable.simulate("mouseout");
            Y.Assert.areSame("timing", this.link.get("status"));
        },

        testNodeMouseover : function() {
            let bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", "timing");
            let event = document.createEvent("MouseEvents");
            event.initMouseEvent("mouseover", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
            this.link.get("node")._node.dispatchEvent(event);
            Y.Assert.areSame("active", this.link.get("status"));
        },

        testNodeMouseout : function() {
            let bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", "active");
            let event = document.createEvent("MouseEvents");
            event.initMouseEvent("mouseout", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
            this.link.get("node")._node.dispatchEvent(event);
            Y.Assert.areSame("timing", this.link.get("status"));
        },

        testClick : function() {
            let bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", "active");
            let self = this;
            let status = "off";
            let data = "";
            L.io = function(url, config) {
                status = self.link.get("status");
                data = config.data;
                config.on.success.apply(config.context, [ 0, {} ]);
            };
            this.link.get("node")._node.click();
            Y.Assert.areSame("bookmarking", status);
            Y.Assert.areSame("off", this.link.get("status"));
            Y.Assert
            .areSame(
                    '{"label":"bookmarkable","url":"http://www.example.com/"}',
                    data);
        },

        testLocalClick : function() {
            let bookmarkable = Y.one("#local");
            this.link.set("target", bookmarkable);
            this.link.set("status", "active");
            let self = this;
            let data = "";
            let status = "off";
            L.io = function(url, config) {
                status = self.link.get("status");
                data = config.data;
                config.on.success.apply(config.context, [ 0, {} ]);
            };
            this.link.get("node")._node.click();
            Y.Assert.areSame("bookmarking", status);
            Y.Assert.areSame("off", this.link.get("status"));
            let expected = '{"label":"local","url":"' + L.Model.get(L.Model.BASE_PATH) + '/foo?bar=baz"}';
            Y.Assert.areSame(expected, data);
        },

        testTimer : function() {
            this.link.set("status", "timing");
            this.wait(function() {
                Y.Assert.areSame("off", this.link.get("status"));
            }, this.link.get("hideDelay"));
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(bookmarkLinkTestCase);
    Y.Test.Runner.masterSuite.name = "bookmark-link-test.js";
    Y.Test.Runner.run();

});
