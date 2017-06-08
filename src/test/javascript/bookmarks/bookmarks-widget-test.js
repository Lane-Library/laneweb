"use strict";

var Bookmark = Y.lane.Bookmark,

bookmarksWidgetTestCase = new Y.Test.Case({

    name : 'BookmarksWidget Test Case',

    widget : null,

    bookmarks : null,

    items : null,

    ioSuccess : function() {
        var bar = arguments[1].on.success;
        var args = arguments[1]["arguments"];
        var rcv = arguments[1].context;
        bar.apply(rcv,[args]);
    },

    setUp : function() {
        this.widget = Y.lane.BookmarksWidget;
        this.bookmarks = this.widget.get("bookmarks");
        this.items = this.widget.get("items");
        Y.io = this.ioSuccess;
    },

    testExists : function() {
        Y.Assert.isTrue(Y.Lang.isObject(this.widget));
    },

    testHasBookmarks : function() {
        Y.Assert.isTrue(Y.Lang.isObject(this.bookmarks));
    },

    testHasItems : function() {
        Y.Assert.isTrue(Y.Lang.isObject(this.items));
    },

    testAddBookmark : function() {
        var size = Y.all("#bookmarks li").size();
        this.bookmarks.addBookmark(new Bookmark("label", "url"));
        Y.Assert.isTrue(Y.all("#bookmarks li").size() == size + 1);
        Y.Assert.areEqual("label", Y.one("#bookmarks a").get("innerHTML"));
    },

    testRemoveBookmark : function() {
        var size = Y.all("#bookmarks li").size();
        this.bookmarks.removeBookmarks([size - 1]);
        Y.Assert.isTrue(Y.all("#bookmarks li").size() == size - 1);
    },

    testUpdateBookmark : function() {
        var size = Y.all("#bookmarks li").size(),
        bookmark = new Bookmark("label", "url");
        this.bookmarks.addBookmark(bookmark);
        bookmark.setLabel("newlabel");
        Y.Assert.areEqual("newlabel", Y.one("#bookmarks a").get("innerHTML"));
    },

    testMoveBookmarkUp : function() {
        this.bookmarks.moveBookmark(0, 3);
        var anchors = Y.all("#bookmarks a");
        Y.Assert.areEqual("Paget disease of bone", anchors.item(0).get("innerHTML"));
        Y.Assert.areEqual("newlabel", anchors.item(1).get("innerHTML"));
    },

    testMoveBookmarkDown : function() {
        this.bookmarks.moveBookmark(3, 0);
        var anchors = Y.all("#bookmarks a");
        Y.Assert.areEqual("Paget disease of bone", anchors.item(3).get("innerHTML"));
        Y.Assert.areEqual("newlabel", anchors.item(0).get("innerHTML"));
    },

    testHiddenItems: function() {
        var displayLimit = this.widget.get("displayLimit");
        var hidden = Y.all("li").item(displayLimit);
        Y.Assert.areSame("none", hidden.getStyle("display"));
    },

    testToString: function() {
        Y.Assert.areSame("BookmarksWidget:Bookmarks[Bookmark{label:", this.widget.toString().substring(0, 41));
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(bookmarksWidgetTestCase);
Y.Test.Runner.masterSuite.name = "bookmarks-widget-test.js";
Y.Test.Runner.run();
