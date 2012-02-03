/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('console', 'test', function(T) {
    
    var Bookmark = Y.lane.Bookmark,

    bookmarkTestCase = new T.Test.Case({
        
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
            T.Assert.isNull(this.bookmark);
        },
        
        testNewBookmarkNoUrl : function() {
            var bookmark = null;
            try {
                bookmark = new Bookmark(null, "url");
            } catch(e) {}
            T.Assert.isNull(bookmark);
        },
        
        testNewBookmarkNoLabel : function() {
            var bookmark = null;
            try {
                bookmark = new Bookmark("label", null);
            } catch(e) {}
            T.Assert.isNull(bookmark);
        },
        
        testSetNullValues : function() {
            try {
                this.bookmark.setValues(null, null);
            } catch(e) {}
            T.Assert.areEqual("label", this.bookmark.getLabel());
            T.Assert.areEqual("url", this.bookmark.getUrl());
        },
        
        testGetSetLabel : function() {
            this.bookmark.setLabel("newlabel");
            T.Assert.areEqual("newlabel", this.bookmark.getLabel());
        },
        
        testGetSetUrl : function() {
            this.bookmark.setUrl("newurl");
            T.Assert.areEqual("newurl", this.bookmark.getUrl());    
        },
        
        testGetSetValues : function() {
            this.bookmark.setValues("newlabel", "newurl");
            T.Assert.areEqual("newlabel", this.bookmark.getLabel());
            T.Assert.areEqual("newurl", this.bookmark.getUrl());   
        },
        
        testChangeEventSetLabel : function() {
            var label = this.bookmark.getLabel();
            this.bookmark.on("valueChange", function(event) {
                label = event.newLabel;
            });
            this.bookmark.setLabel("newlabel");
            T.Assert.areEqual(label, "newlabel");
        },
        
        testSetLabelPreventDefault : function() {
            var label = this.bookmark.getLabel();
            this.bookmark.on("valueChange", function(event) {
                event.preventDefault();
            });
            this.bookmark.setLabel("newlabel");
            T.Assert.areEqual(label, this.bookmark.getLabel());
        },
        
        testChangeEventSetUrl : function() {
            var url = this.bookmark.getUrl();
            this.bookmark.on("valueChange", function(event) {
                url = event.newUrl;
            });
            this.bookmark.setUrl("newurl");
            T.Assert.areEqual(url, "newurl");   
        },
        
        testSetUrlPreventDefault : function() {
            var url = this.bookmark.getUrl();
            this.bookmark.on("valueChange", function(event) {
                event.preventDefault();
            });
            this.bookmark.setUrl("newurl");
            T.Assert.areEqual(url, this.bookmark.getUrl());   
        },
        
        testChangeEventSetValues : function() {
            var label = this.bookmark.getLabel();
            var url = this.bookmark.getUrl();
            this.bookmark.on("valueChange", function(event) {
                label = event.newLabel;
                url = event.newUrl;
            });
            this.bookmark.setValues("newlabel", "newurl");
            T.Assert.areEqual(label, "newlabel");
            T.Assert.areEqual(url, "newurl");   
        },
        
        testSetValuesPreventDefault : function() {
            var label = this.bookmark.getLabel();
            var url = this.bookmark.getUrl();
            this.bookmark.on("valueChange", function(event) {
                event.preventDefault();
            });
            this.bookmark.setValues("newlabel", "newurl");
            T.Assert.areEqual(label, this.bookmark.getLabel());
            T.Assert.areEqual(url, this.bookmark.getUrl());   
        }
        
    }),
    
    Bookmarks = Y.lane.Bookmarks,
    
    bookmarksTestCase = new T.Test.Case({
        
        name : 'Bookmarks Test Case',
        
        bookmarks : null,
        
        initialSize : 0,
        
        eventHandle : null,
        
        ioSuccess : function() {
            var bar = arguments[1].on.success;
            var args = arguments[1]["arguments"];
            var rcv = arguments[1].context;
            bar.apply(rcv,[args]);
        },
        
        setUp : function() {
            this.bookmarks = Y.lane.BookmarksWidget.get("bookmarks");
            this.initialSize = this.bookmarks.size();
            Y.io = this.ioSuccess;
        },
        
        tearDown : function() {
            if (this.eventHandle) {
                this.bookmarks.detach(this.eventHandle);
            }
        },
        
        testConstructor : function() {
        	T.Assert.isObject(this.bookmarks);
        },
        
        testAddBookmark : function() {
        	this.bookmarks.addBookmark(new Bookmark("label1","url1"));
            T.Assert.areEqual(this.initialSize + 1, this.bookmarks.size());
        },
        
        testAddBadBookmark : function() {
        	try {
            	this.bookmarks.addBookmark(null);
        	} catch(e) {}
            T.Assert.areEqual(this.initialSize, this.bookmarks.size());
        },
        
        testAddBookmarkEvent : function() {
            var added = null, bookmark = new Bookmark("label2", "url2");
            this.eventHandle = this.bookmarks.on("addSync", function(event) {
                added = event.bookmark;
            });
            this.bookmarks.addBookmark(bookmark);
            T.Assert.areSame(added, bookmark);
        },
        
        testAddBookmarkEventPrevent : function() {
            this.eventHandle = this.bookmarks.on("add", function(event) {
                event.preventDefault();
            });
            this.bookmarks.addBookmark(new Bookmark("label3", "url3"));
            T.Assert.areEqual(this.initialSize, this.bookmarks.size());
        },
        
        testGetBookmark : function() {
        	var bookmark = new Bookmark("label4", "url4");
        	this.bookmarks.addBookmark(bookmark);
        	T.Assert.areEqual(bookmark, this.bookmarks.getBookmark(0));
        },
        
        testRemoveBookmark : function() {
            this.bookmarks.addBookmark(new Bookmark("label5", "url5"));
            this.bookmarks.removeBookmark(0);
            T.Assert.areEqual(this.initialSize - 1, this.bookmarks.size());
        },
        
        testRemoveBookmarkBadPosition : function() {
            try {
                this.bookmarks.removeBookmark(null);
            } catch (e) {}
        },
        
        testRemoveBookmarkEvent : function() {
            var position = null;
            this.bookmarks.addBookmark(new Bookmark("label6", "url6"));
            this.eventHandle = this.bookmarks.on("removeSync", function(event) {
                position = event.position;
            });
            this.bookmarks.removeBookmark(0);
            T.Assert.areEqual(0, position);
        },
        
        testRemoveBookmarkEventPrevent : function() {
            this.bookmarks.addBookmark(new Bookmark("label7", "url7"));
            this.eventHandle = this.bookmarks.on("remove", function(event) {
                event.preventDefault();
            });
            this.bookmarks.removeBookmark(0);
            T.Assert.areEqual(this.initialSize + 1, this.bookmarks.size());
        },
        
        testUpdateBookmark : function() {
            var b = null, p = -1, bookmark = new Bookmark("label8", "url8");
            this.bookmarks.addBookmark(bookmark);
            this.eventHandle = this.bookmarks.on("update", function(event) {
                b = event.bookmark;
                p = event.position;
            });
            bookmark.setLabel("newlabel");
            T.Assert.areSame(bookmark, b);
            T.Assert.areEqual(0, p);
            T.Assert.areEqual("newlabel", bookmark.getLabel());
        }
    }),
    
    bookmarksWidgetTestCase = new T.Test.Case({
        
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
            T.Assert.isTrue(T.Lang.isObject(this.widget));
        },
        
        testHasBookmarks : function() {
            T.Assert.isTrue(T.Lang.isObject(this.bookmarks));
        },
        
        testHasItems : function() {
            T.Assert.isTrue(T.Lang.isObject(this.items));
        },
        
        testAddBookmark : function() {
            var size = Y.all("#bookmarks li").size();
            this.bookmarks.addBookmark(new Bookmark("label", "url"));
            T.Assert.isTrue(Y.all("#bookmarks li").size() == size + 1);
            T.Assert.areEqual("label", Y.one("#bookmarks a").get("innerHTML"));
        },
        
        testRemoveBookmark : function() {
            var size = Y.all("#bookmarks li").size();
            this.bookmarks.removeBookmark(size - 1);
            T.Assert.isTrue(Y.all("#bookmarks li").size() == size - 1);
        },
        
        testUpdateBookmark : function() {
            var size = Y.all("#bookmarks li").size(),
                bookmark = new Bookmark("label", "url");
            this.bookmarks.addBookmark(bookmark);
            bookmark.setLabel("newlabel");
            T.Assert.areEqual("newlabel", Y.one("#bookmarks a").get("innerHTML"));
        }
    }),
    
    bookmarkLinkTestCase = new T.Test.Case({
        
        name : "BookmarkLink Test Case",
        
        link : null,
        
        setUp : function() {
            this.link = Y.lane.BookmarkLink;
        },
    
        testExists : function() {
            T.Assert.isTrue(T.Lang.isObject(this.link));
        }
        
    });

    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');

    T.Test.Runner.add(bookmarkTestCase);
    T.Test.Runner.add(bookmarksTestCase);
    T.Test.Runner.add(bookmarksWidgetTestCase);
    T.Test.Runner.add(bookmarkLinkTestCase);
    T.Test.Runner.run();
});
