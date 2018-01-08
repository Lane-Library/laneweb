"use strict";

var Bookmark = L.Bookmark,

bookmarkTestCase = new Y.Test.Case({

    name : 'Bookmark Test Case',

    bookmark : null,

    setUp : function() {
        this.bookmark = new Bookmark("label", "url");
    },

    testNewBookmarkNoParams : function() {
        this.bookmark = null;
        try {
            this.bookmark = new Bookmark();
        } catch(e) {}
        Y.Assert.isNull(this.bookmark);
    },

    testNewBookmarkNoUrl : function() {
        var bookmark = null;
        try {
            bookmark = new Bookmark(null, "url");
        } catch(e) {}
        Y.Assert.isNull(bookmark);
    },

    testNewBookmarkNoLabel : function() {
        var bookmark = null;
        try {
            bookmark = new Bookmark("label", null);
        } catch(e) {}
        Y.Assert.isNull(bookmark);
    },

    testSetNullValues : function() {
        try {
            this.bookmark.setValues(null, null);
        } catch(e) {}
        Y.Assert.areEqual("label", this.bookmark.getLabel());
        Y.Assert.areEqual("url", this.bookmark.getUrl());
    },

    testGetSetLabel : function() {
        this.bookmark.setLabel("newlabel");
        Y.Assert.areEqual("newlabel", this.bookmark.getLabel());
    },

    testGetSetUrl : function() {
        this.bookmark.setUrl("newurl");
        Y.Assert.areEqual("newurl", this.bookmark.getUrl());
    },

    testGetSetValues : function() {
        this.bookmark.setValues("newlabel", "newurl");
        Y.Assert.areEqual("newlabel", this.bookmark.getLabel());
        Y.Assert.areEqual("newurl", this.bookmark.getUrl());
    },

    testChangeEventSetLabel : function() {
        var label = this.bookmark.getLabel();
        this.bookmark.on("valueChange", function(event) {
            label = event.newLabel;
        });
        this.bookmark.setLabel("newlabel");
        Y.Assert.areEqual(label, "newlabel");
    },

    testSetLabelPreventDefault : function() {
        var label = this.bookmark.getLabel();
        this.bookmark.on("valueChange", function(event) {
            event.preventDefault();
        });
        this.bookmark.setLabel("newlabel");
        Y.Assert.areEqual(label, this.bookmark.getLabel());
    },

    testChangeEventSetUrl : function() {
        var url = this.bookmark.getUrl();
        this.bookmark.on("valueChange", function(event) {
            url = event.newUrl;
        });
        this.bookmark.setUrl("newurl");
        Y.Assert.areEqual(url, "newurl");
    },

    testSetUrlPreventDefault : function() {
        var url = this.bookmark.getUrl();
        this.bookmark.on("valueChange", function(event) {
            event.preventDefault();
        });
        this.bookmark.setUrl("newurl");
        Y.Assert.areEqual(url, this.bookmark.getUrl());
    },

    testChangeEventSetValues : function() {
        var label = this.bookmark.getLabel();
        var url = this.bookmark.getUrl();
        this.bookmark.on("valueChange", function(event) {
            label = event.newLabel;
            url = event.newUrl;
        });
        this.bookmark.setValues("newlabel", "newurl");
        Y.Assert.areEqual(label, "newlabel");
        Y.Assert.areEqual(url, "newurl");
    },

    testSetValuesPreventDefault : function() {
        var label = this.bookmark.getLabel();
        var url = this.bookmark.getUrl();
        this.bookmark.on("valueChange", function(event) {
            event.preventDefault();
        });
        this.bookmark.setValues("newlabel", "newurl");
        Y.Assert.areEqual(label, this.bookmark.getLabel());
        Y.Assert.areEqual(url, this.bookmark.getUrl());
    }

});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(bookmarkTestCase);
Y.Test.Runner.masterSuite.name = "bookmark-test.js";
Y.Test.Runner.run();
