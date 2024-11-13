YUI({ fetchCSS: false }).use("test", "test-console", function (Y) {

    "use strict";

    let Bookmark = L.Bookmark,

        bookmarkTestCase = new Y.Test.Case({

            name: 'Bookmark Test Case',

            bookmark: null,

            setUp: function () {
                this.bookmark = new Bookmark("label", "url");
            },

            testNewBookmarkNoParams: function () {
                this.bookmark = null;
                try {
                    this.bookmark = new Bookmark();
                } catch (e) { }
                Y.Assert.isNull(this.bookmark);
            },

            testNewBookmarkNoUrl: function () {
                let bookmark = null;
                try {
                    bookmark = new Bookmark(null, "url");
                } catch (e) { }
                Y.Assert.isNull(bookmark);
            },

            testNewBookmarkNoLabel: function () {
                let bookmark = null;
                try {
                    bookmark = new Bookmark("label", null);
                } catch (e) { }
                Y.Assert.isNull(bookmark);
            },

            testSetNullValues: function () {
                try {
                    this.bookmark.setValues(null, null);
                } catch (e) { }
                Y.Assert.areEqual("label", this.bookmark.getLabel());
                Y.Assert.areEqual("url", this.bookmark.getUrl());
            },

            testGetSetLabel: function () {
                this.bookmark.setLabel("newlabel");
                Y.Assert.areEqual("newlabel", this.bookmark.getLabel());
            },

            testGetSetUrl: function () {
                this.bookmark.setUrl("newurl");
                Y.Assert.areEqual("newurl", this.bookmark.getUrl());
            },

            testGetSetValues: function () {
                this.bookmark.setValues("newlabel", "newurl");
                Y.Assert.areEqual("newlabel", this.bookmark.getLabel());
                Y.Assert.areEqual("newurl", this.bookmark.getUrl());
            },

            testChangeEventSetLabel: function () {
                let label = this.bookmark.getLabel();
                this.bookmark.on("valueChange", function (event) {
                    label = event.newLabel;
                });
                this.bookmark.setLabel("newlabel");
                Y.Assert.areEqual(label, "newlabel");
            },


            testChangeEventSetUrl: function () {
                let url = this.bookmark.getUrl();
                this.bookmark.on("valueChange", function (event) {
                    url = event.newUrl;
                });
                this.bookmark.setUrl("newurl");
                Y.Assert.areEqual(url, "newurl");
            },



            testChangeEventSetValues: function () {
                let label = this.bookmark.getLabel();
                let url = this.bookmark.getUrl();
                this.bookmark.on("valueChange", function (event) {
                    label = event.newLabel;
                    url = event.newUrl;
                });
                this.bookmark.setValues("newlabel", "newurl");
                Y.Assert.areEqual(label, "newlabel");
                Y.Assert.areEqual(url, "newurl");
            },



        });

    new Y.Test.Console().render();

    Y.Test.Runner.add(bookmarkTestCase);
    Y.Test.Runner.masterSuite.name = "bookmark-test.js";
    Y.Test.Runner.run();

});
