/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('console', 'test', function(T) {
    
    var Bookmark = Y.lane.Bookmark,
    
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
            var bm = [];
            bm.push(new Bookmark("1","1"));
            bm.push(new Bookmark("2","2"));
            bm.push(new Bookmark("3","3"));
            this.bookmarks =  new Bookmarks(bm);
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
        
        testRemoveBookmarks : function() {
            this.bookmarks.addBookmark(new Bookmark("label5", "url5"));
            this.bookmarks.removeBookmarks([0]);
            T.Assert.areEqual(this.initialSize, this.bookmarks.size());
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
            T.Assert.areEqual(1, positions.length);
            T.Assert.areEqual(0, positions[0]);
        },
        
        testRemoveBookmarksEventPrevent : function() {
            this.bookmarks.addBookmark(new Bookmark("label7", "url7"));
            this.eventHandle = this.bookmarks.on("remove", function(event) {
                event.preventDefault();
            });
            this.bookmarks.removeBookmarks([0]);
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
    });

    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');

    T.Test.Runner.add(bookmarksTestCase);
    T.Test.Runner.run();
});
