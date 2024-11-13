YUI({ fetchCSS: false }).use("test", "test-console", "node-event-simulate", "node-pluginhost", function (Y) {

    "use strict";


    let bookmarkLinkTestCase = new Y.Test.Case({

        name: "BookmarkLink Test Case",

        link: L.BookmarkLink,

        setUp: function () {
            this.link.setStatus("off");
        },

        testExists: function () {
            Y.Assert.isTrue(Y.Lang.isObject(this.link));
        },

        testDontShowAlreadyBookmarked: function () {
            Y.one("#alreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual("off", this.link.status);
        },

        testAnotherDontShowAlreadyBookmarked: function () {
            Y.one("#anotherAlreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual("off", this.link.status);
        },

        testTargetMouseover: function () {
            let bookmarkable = Y.one("#bookmarkable");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("ready", this.link.status);
            Y.Assert.areSame(bookmarkable._node, this.link.target);
        },

        testTargetBlock: function () {
            let bookmarkable = Y.one("#block");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("off", this.link.status);
        },

        testTargetNoBookmarking: function () {
            let bookmarkable = Y.one("a.no-bookmarking");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("off", this.link.status);
        },

        testTargetAncestorNoBookmarking: function () {
            let bookmarkable = Y.one("div.no-bookmarking a");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("off", this.link.status);
        },

        testTargetInlineBlock: function () {
            let bookmarkable = Y.one("#inline-block");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame("ready", this.link.status);
            Y.Assert.areSame(bookmarkable._node, this.link.target);
        },

        testTargetMouseout: function () {
            let bookmarkable = Y.one("#bookmarkable");
            this.link.target = bookmarkable._node;
            this.link.setStatus("ready");
            bookmarkable.simulate("mouseout");
            Y.Assert.areSame("timing", this.link.status);
        },

        testNodeMouseover: function () {
            let bookmarkable = Y.one("#bookmarkable");
            this.link.target = bookmarkable._node;
            this.link.setStatus("timing");
            let event = document.createEvent("MouseEvents");
            event.initMouseEvent("mouseover", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
            this.link._node.dispatchEvent(event);
            Y.Assert.areSame("active", this.link.status);
        },

        testNodeMouseout: function () {
            let bookmarkable = Y.one("#bookmarkable");
            this.link.target = bookmarkable._node;
            this.link.setStatus("active");
            let event = document.createEvent("MouseEvents");
            event.initMouseEvent("mouseout", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
            this.link._node.dispatchEvent(event);
            Y.Assert.areSame("active", this.link.status);
        },

        // testClick: function () {
        //     let bookmarkable = Y.one("#bookmarkable");
        //     this.link.target = bookmarkable._node;
        //     this.link.setStatus("active");
        //     let self = this;
        //     let status = "off";
        //     let data = "";
        //     // L.io 
        //     // L.io = function (url, config) {
        //     //     status = self.link.status;
        //     //     data = config.data;
        //     //     config.on.success.apply(config.context, [0, {}]);
        //     // };
        //     this.link._node.click();
        //     // Y.Assert.areSame("bookmarking", status);
        //     // Y.Assert.areSame("off", this.link.status);
        //     Y.Assert
        //         .areSame(
        //             '{"label":"bookmarkable","url":"http://www.example.com/"}',
        //             data);
        // },

        // testLocalClick: function () {
        //     let bookmarkable = Y.one("#local");
        //     this.link.target = bookmarkable._node;
        //     this.link.setStatus("active");
        //     let self = this;
        //     let data = "";
        //     let status = "off";
        //     L.io = function (url, config) {
        //         status = self.link.get.status;
        //         data = config.data;
        //         config.on.success.apply(config.context, [0, {}]);
        //     };
        //     this.link._node.click();
        //     // Y.Assert.areSame("bookmarking", status);
        //     // Y.Assert.areSame("off", this.link.status);
        //     let expected = '{"label":"local","url":"' + L.Model.get(L.Model.BASE_PATH) + '/foo?bar=baz"}';
        //     Y.Assert.areSame(expected, data);
        // },

        testTimer: function () {
            this.link.setStatus("timing");
            this.wait(function () {
                Y.Assert.areSame("off", this.link.status);
            }, this.link.hideDelay);
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(bookmarkLinkTestCase);
    Y.Test.Runner.masterSuite.name = "bookmark-link-test.js";
    Y.Test.Runner.run();

});
