/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("test", "console", "node-event-simulate", function(T){
	
	var bookmarks = Y.lane.Bookmarks;
    
    var bookmarksTestCase = new T.Test.Case({
        name: "Lane Bookmarks Test Case",
        
        testNotEditing : function() {
        	T.Assert.isFalse(bookmarks.get("editing"));
        },
        
        testForTwoBookmarks : function() {
        	T.Assert.areEqual(2, bookmarks.get("bookmarks").length);
        },
        
        testBookmarkLabel : function() {
        	T.Assert.areEqual("Google", bookmarks.get("bookmarks")[0].get("label"));
        },
        
        testSetEditing : function() {
        	var editing = bookmarks.get("editing");
        	var toggle = T.one("h3 a");
        	toggle.simulate("click");
        	T.Assert.isFalse(editing === bookmarks.get("editing"));
        },
        
        testAddBookmark : function() {
        	var size = T.all("li").size();
        	bookmarks.addBookmark({label:"MDConsult",url:"http://mdconsult.com"});
        	T.Assert.areEqual(size + 1, T.all("li").size());
        },
        
        testRemoveBookmark : function() {
        	var size = T.all("li").size();
        	bookmarks.removeBookmark(1);
        	T.Assert.areEqual(size - 1, T.all("li").size());
        },
        
        testClickDeleteBookmark : function() {
        	var size = T.all("li").size();
        	T.one(".yui3-bookmark-edit").simulate("click");
        	T.Assert.areEqual(size - 1, T.all("li").size());
        },
        
        testClickAddBookmark : function() {
        	var size = T.all("li").size();
        	T.one("input[name='label']").set("value","SlashDot");
        	T.one("input[name='url']").set("value","http://slashdot.org/");
        	T.one("input[type='submit']").simulate("click");
        	T.Assert.areEqual(size + 1, T.all("li").size());
        },
        
        testCorrectBookmarkDeleted : function() {
        	var label = T.one("li").get("textContent");
        	T.all(".yui3-bookmark-edit").item(1).simulate("click");
        	T.Assert.areEqual(label, T.one("li").get("textContent"));
        }
        
//        testMoveUp : function() {
//        	var li = T.all("li");
//        	var first = li.item(0).get("textContent");
//        	var second = li.item(1).get("textContent");
//        	bookmarks.moveUp(1);
//        	li = T.all("li");
//        	T.Assert.areEqual(second, li.item(0).get("textContent"));
//        	T.Assert.areEqual(first, li.item(1).get("textContent"));
//        },
//
//        testMoveDown : function() {
//        	var li = T.all("li");
//        	var first = li.item(0).get("textContent");
//        	var second = li.item(1).get("textContent");
//        	bookmarks.moveDown(0);
//        	li = T.all("li");
//        	T.Assert.areEqual(second, li.item(0).get("textContent"));
//        	T.Assert.areEqual(first, li.item(1).get("textContent"));
//        }
    });
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");

//  T.Test.Runner.add(bookmarkTestCase);
  T.Test.Runner.add(bookmarksTestCase);
    T.Test.Runner.run();
});
