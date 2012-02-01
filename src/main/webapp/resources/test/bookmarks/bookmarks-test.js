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
        
        config : {label:"label",url:"url"},
        
        bookmark : null,
        
        setUp : function() {
            this.bookmark = new Bookmark(this.config);
        },
        
        testNewBookmarkNoConfig : function() {
            this.bookmark = null;
            try {
                this.bookmark = new Bookmark();
            } catch(e) {}
            T.Assert.isNull(this.bookmark);
        },
        
        testNewBookmarkNoUrl : function() {
            var bookmark = null, config = {url:"url"};
            try {
                bookmark = new Bookmark(config);
            } catch(e) {}
            T.Assert.isNull(bookmark);
        },
        
        testNewBookmarkNoLabel : function() {
            var bookmark = null, config = {label:"label"};
            try {
                bookmark = new Bookmark(config);
            } catch(e) {}
            T.Assert.isNull(bookmark);
        },
        
        testSetNullValue : function() {
            this.bookmark.setValue(null);
            T.Assert.areEqual("label", this.bookmark.getLabel());
        },
        
        testSetBadValue : function() {
            this.bookmark.setValue({url:null});
            T.Assert.areEqual("url", this.bookmark.getUrl());
        },
        
        testGetSetLabel : function() {
            this.bookmark.setValue({label:"newlabel"});
            T.Assert.areEqual("newlabel", this.bookmark.getLabel());
        },
        
        testGetSetUrl : function() {
            this.bookmark.setValue({url:"newurl"});
            T.Assert.areEqual("newurl", this.bookmark.getUrl());    
        },
        
        testGetSetBoth : function() {
            this.bookmark.setValue({label:"newlabel", url:"newurl"});
            T.Assert.areEqual("newlabel", this.bookmark.getLabel());
            T.Assert.areEqual("newurl", this.bookmark.getUrl());   
        },
        
        testChangeEvent : function() {
        	var value = {label : this.bookmark.getLabel(), url : this.bookmark.getUrl()};
        	this.bookmark.on("valueChange", function(event) {
        		value = event.newVal;
        	});
            this.bookmark.setValue({label:"newlabel", url:"newurl"});
            T.Assert.areEqual(value.label, "newlabel");
            T.Assert.areEqual(value.url, "newurl");   
        },
        
        testPreventDefault : function() {
        	this.bookmark.on("valueChange", function(event) {
        		event.preventDefault();
        	});
            this.bookmark.setValue({label:"newlabel", url:"newurl"});
            T.Assert.areEqual("label", this.bookmark.getLabel());
            T.Assert.areEqual("url", this.bookmark.getUrl());   
        }
        
    }),
    
    Bookmarks = Y.lane.Bookmarks,
    
    bookmarksTestCase = new T.Test.Case({
        
        name : 'Bookmarks Test Case',
        
        bookmarks : null,
        
        initialSize : 0,
        
        eventHander : null,
        
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
            this.bookmarks.detach(this.eventHandle);
        },
        
        testConstructor : function() {
        	T.Assert.isObject(this.bookmarks);
        },
        
        testAddBookmark : function() {
        	this.bookmarks.addBookmark(new Bookmark({label:"label1",url:"url1"}));
            T.Assert.areEqual(this.initialSize + 1, this.bookmarks.size());
        },
        
        testAddBadBookmark : function() {
        	try {
            	this.bookmarks.addBookmark(null);
        	} catch(e) {}
            T.Assert.areEqual(this.initialSize, this.bookmarks.size());
        },
        
        testAddBookmarkEvent : function() {
            var added = null, bookmark = new Bookmark({label:"label2",url:"url2"});
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
            this.bookmarks.addBookmark(new Bookmark({label:"label3",url:"url3"}));
            T.Assert.areEqual(this.initialSize, this.bookmarks.size());
        },
        
        testGetBookmark : function() {
        	var bookmark = new Bookmark({label:"label4",url:"url4"});
        	this.bookmarks.addBookmark(bookmark);
        	T.Assert.areEqual(bookmark, this.bookmarks.getBookmark(0));
        },
        
        testRemoveBookmark : function() {
            this.bookmarks.addBookmark(new Bookmark({label:"label5",url:"url5"}));
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
            this.bookmarks.addBookmark(new Bookmark({label:"label6",url:"url6"}));
            this.eventHandle = this.bookmarks.on("removeSync", function(event) {
                position = event.position;
            });
            this.bookmarks.removeBookmark(0);
            T.Assert.areEqual(0, position);
        },
        
        testRemoveBookmarkEventPrevent : function() {
            this.bookmarks.addBookmark(new Bookmark({label:"label7",url:"url7"}));
            this.eventHandle = this.bookmarks.on("remove", function(event) {
                event.preventDefault();
            });
            this.bookmarks.removeBookmark(0);
            T.Assert.areEqual(this.initialSize + 1, this.bookmarks.size());
        },
        
        testUpdateBookmark : function() {
            var b = null, p = -1, bookmark = new Bookmark({label:"label8",url:"url8"});
            this.bookmarks.addBookmark(bookmark);
            this.eventHandle = this.bookmarks.on("update", function(event) {
                b = event.bookmark;
                p = event.position;
            });
            bookmark.setValue({label : "newlabel"});
            T.Assert.areSame(bookmark, b);
            T.Assert.areEqual(0, p);
            T.Assert.areEqual("newlabel", bookmark.getLabel());
        },
        
        testSetValue : function() {
            var bookmark = new Bookmark({label:"label9",url:"url9"});
            this.bookmarks.addBookmark(bookmark);
            this.bookmarks.setValue(0, {label:"newlabel"});
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
            this.bookmarks.addBookmark(new Bookmark({label:"label",url:"url"}));
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
                bookmark = new Bookmark({label:"label",url:"url"});
            this.bookmarks.addBookmark(bookmark);
            bookmark.setValue({label:"newlabel"});
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

//    T.Test.Runner.add(bookmarkTestCase);
//    T.Test.Runner.add(bookmarksTestCase);
//    T.Test.Runner.add(bookmarksWidgetTestCase);
    T.Test.Runner.add(bookmarkLinkTestCase);
    T.Test.Runner.run();
});
