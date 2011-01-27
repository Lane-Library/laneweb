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
        }
    });
    
    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");

    Y.Test.Runner.add(bookmarksTestCase);
    Y.Test.Runner.run();
});
