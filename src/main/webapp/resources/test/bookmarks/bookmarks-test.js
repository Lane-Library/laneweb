/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("test", "console", "node-event-simulate", "bookmarks",  function(Y){
	
    var bookmarks = new Y.Bookmarks({srcNode:"#bookmarks"});
    
    bookmarks.render();
    
    var bookmarksTestCase = new Y.Test.Case({
        name: "Lane Bookmarks Test Case",
        
        testNotEditing : function() {
        	Y.Assert.isFalse(bookmarks.get("editing"));
        },
        
        testBookmarkLabel : function() {
        	Y.Assert.areEqual("Google", bookmarks.get("bookmarks")[0].label);
        },
        
        testSetEditing : function() {
        	var editing = bookmarks.get("editing");
        	var toggle = Y.one("h3 a");
        	toggle.simulate("click");
        	Y.Assert.isFalse(editing === bookmarks.get("editing"));
        },
        
        testAddBookmark : function() {
        	var size = Y.all("li").size();
        	bookmarks.addBookmark({label:"MDConsult",url:"http://mdconsult.com"});
        	Y.Assert.areEqual(size + 1, Y.all("li").size());
        },
        
        testRemoveBookmark : function() {
        	var size = Y.all("li").size();
        	bookmarks.removeBookmark(1);
        	Y.Assert.areEqual(size - 1, Y.all("li").size());
        },
        
        testMoveUp : function() {
        	var li = Y.all("li");
        	var first = li.item(0).get("textContent");
        	var second = li.item(1).get("textContent");
        	bookmarks.moveUp(1);
        	li = Y.all("li");
        	Y.Assert.areEqual(second, li.item(0).get("textContent"));
        	Y.Assert.areEqual(first, li.item(1).get("textContent"));
        },

        testMoveDown : function() {
        	var li = Y.all("li");
        	var first = li.item(0).get("textContent");
        	var second = li.item(1).get("textContent");
        	bookmarks.moveDown(0);
        	li = Y.all("li");
        	Y.Assert.areEqual(second, li.item(0).get("textContent"));
        	Y.Assert.areEqual(first, li.item(1).get("textContent"));
        }
    });
    
    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");

    Y.Test.Runner.add(bookmarksTestCase);
    Y.Test.Runner.run();
});
