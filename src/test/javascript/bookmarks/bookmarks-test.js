"use strict";

var Bookmark = L.Bookmark,

Bookmarks = L.Bookmarks,

bookmarksTestCase = new Y.Test.Case({

    name : 'Bookmarks Test Case',

    bookmarks : null,

    initialSize : 0,

    eventHandle : null,

    alert: function(message) {
        bookmarksTestCase.message = message;
    },

    ioSuccess : function() {
        var bar = arguments[1].on.success;
        var args = arguments[1]["arguments"];
        var rcv = arguments[1].context;
        bar.apply(rcv,[args]);
    },

    setUp : function() {
        var bm = [];
        bm.push(new Bookmark("0","0"));
        bm.push(new Bookmark("1","1"));
        bm.push(new Bookmark("2","2"));
        bm.push(new Bookmark("3","3"));
        this.bookmarks =  new Bookmarks(bm);
        this.initialSize = this.bookmarks.size();
        Y.io = this.ioSuccess;
        this.message = null;
        window.alert = this.alert;
    },

    tearDown : function() {
        if (this.eventHandle) {
            this.bookmarks.detach(this.eventHandle);
        }
    },

    testConstructor : function() {
        Y.Assert.isObject(this.bookmarks);
    },

    testAddBookmark : function() {
        this.bookmarks.addBookmark(new Bookmark("label1","url1"));
        Y.Assert.areEqual(this.initialSize + 1, this.bookmarks.size());
    },

    testAddBadBookmark : function() {
        try {
            this.bookmarks.addBookmark(null);
        } catch(e) {}
        Y.Assert.areEqual(this.initialSize, this.bookmarks.size());
    },

    testAddBookmarkEvent : function() {
        var added = null, bookmark = new Bookmark("label2", "url2");
        this.eventHandle = this.bookmarks.on("addSync", function(event) {
            added = event.bookmark;
        });
        this.bookmarks.addBookmark(bookmark);
        Y.Assert.areSame(added, bookmark);
    },

    testAddBookmarkEventPrevent : function() {
        this.eventHandle = this.bookmarks.on("add", function(event) {
            event.preventDefault();
        });
        this.bookmarks.addBookmark(new Bookmark("label3", "url3"));
        Y.Assert.areEqual(this.initialSize, this.bookmarks.size());
    },

    testGetBookmark : function() {
        var bookmark = new Bookmark("label4", "url4");
        this.bookmarks.addBookmark(bookmark);
        Y.Assert.areEqual(bookmark, this.bookmarks.getBookmark(0));
    },

    testRemoveBookmarks : function() {
        this.bookmarks.addBookmark(new Bookmark("label5", "url5"));
        this.bookmarks.removeBookmarks([0]);
        Y.Assert.areEqual(this.initialSize, this.bookmarks.size());
    },

    testRemoveBookmarksBadPosition : function() {
        try {
            this.bookmarks.removeBookmark(null);
        } catch (e) {}
    },

    testRemoveBookmarksEvent : function() {
        var positions = null;
        this.bookmarks.addBookmark(new Bookmark("label6", "url6"));
        this.eventHandle = this.bookmarks.on("removeSync", function(event) {
            positions = event.positions;
        });
        this.bookmarks.removeBookmarks([0]);
        Y.Assert.areEqual(1, positions.length);
        Y.Assert.areEqual(0, positions[0]);
    },

    testRemoveBookmarksEventPrevent : function() {
        this.bookmarks.addBookmark(new Bookmark("label7", "url7"));
        this.eventHandle = this.bookmarks.on("remove", function(event) {
            event.preventDefault();
        });
        this.bookmarks.removeBookmarks([0]);
        Y.Assert.areEqual(this.initialSize + 1, this.bookmarks.size());
    },

    testUpdateBookmark : function() {
        var b = null, p = -1, bookmark = new Bookmark("label8", "url8");
        this.bookmarks.addBookmark(bookmark);
        this.eventHandle = this.bookmarks.on("update", function(event) {
            b = event.bookmark;
            p = event.position;
        });
        bookmark.setLabel("newlabel");
        Y.Assert.areSame(bookmark, b);
        Y.Assert.areEqual(0, p);
        Y.Assert.areEqual("newlabel", bookmark.getLabel());
    },

    testMoveBookmarkUp : function() {
        this.bookmarks.moveBookmark(0, 2);
        Y.Assert.areEqual("2", this.bookmarks.getBookmark(0).getLabel());
        Y.Assert.areEqual("0", this.bookmarks.getBookmark(1).getLabel());
    },

    testMoveBookmarkDown : function() {
        this.bookmarks.moveBookmark(3, 0);
        Y.Assert.areEqual("0", this.bookmarks.getBookmark(3).getLabel());
        Y.Assert.areEqual("1", this.bookmarks.getBookmark(0).getLabel());
    },

    testBadConfig: function() {
        try {
            (new Bookmarks({}));
        } catch(e) {
            Y.Assert.areSame("bad config", e);
        }
    },

    testIndexOf: function() {
        var bookmark = this.bookmarks.getBookmark(2);
        Y.Assert.areSame(2, this.bookmarks.indexOf(bookmark));
    },

    testAddFail: function() {
        Y.io = function(url, config) {
            config.on.failure.apply(config.context, [0]);
        };
        this.bookmarks.addBookmark(new Bookmark("label9", "url9"));
        Y.Assert.areSame("Sorry, add bookmark failed. Please try again later.", this.message);
    },

    testMoveFail: function() {
        Y.io = function(url, config) {
            config.on.failure.apply(config.context, [0]);
        };
        this.bookmarks.moveBookmark(3, 0);
        Y.Assert.areSame("Sorry, move bookmark failed. Please try again later.", this.message);
    },

    testRemoveFail: function() {
        Y.io = function(url, config) {
            config.on.failure.apply(config.context, [0]);
        };
        this.bookmarks.removeBookmarks([0]);
        Y.Assert.areSame("Sorry, delete bookmark failed. Please try again later.", this.message);
    },

    testUpdateFail: function() {
        Y.io = function(url, config) {
            config.on.failure.apply(config.context, [0]);
        };
        this.bookmarks.getBookmark(0).setLabel("foo");
        Y.Assert.areSame("Sorry, update bookmark failed. Please try again later.", this.message);
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(bookmarksTestCase);
Y.Test.Runner.masterSuite.name = "bookmarks-test.js";
Y.Test.Runner.run();
