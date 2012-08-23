/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('console', 'test', function(T) {
    
    var Bookmark = Y.lane.Bookmark,
    
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
            this.bookmarks.removeBookmarks([size - 1]);
            T.Assert.isTrue(Y.all("#bookmarks li").size() == size - 1);
        },
        
        testUpdateBookmark : function() {
            var size = Y.all("#bookmarks li").size(),
                bookmark = new Bookmark("label", "url");
            this.bookmarks.addBookmark(bookmark);
            bookmark.setLabel("newlabel");
            T.Assert.areEqual("newlabel", Y.one("#bookmarks a").get("innerHTML"));
        },
        
        testMoveBookmarkUp : function() {
            this.bookmarks.moveBookmark(0, 3);
            var anchors = Y.all("#bookmarks a");
            T.Assert.areEqual("Paget disease of bone", anchors.item(0).get("innerHTML"));
            T.Assert.areEqual("newlabel", anchors.item(1).get("innerHTML"));
        },
        
        testMoveBookmarkDown : function() {
            this.bookmarks.moveBookmark(3, 0);
            var anchors = Y.all("#bookmarks a");
            T.Assert.areEqual("Paget disease of bone", anchors.item(3).get("innerHTML"));
            T.Assert.areEqual("newlabel", anchors.item(0).get("innerHTML"));
        }
    });

    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');

    T.Test.Runner.add(bookmarksWidgetTestCase);
    T.Test.Runner.run();
});
