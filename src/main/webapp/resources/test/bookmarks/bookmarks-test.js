/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("test", "console", "bookmarks",  function(Y){
	
    var bookmarks = new Y.Bookmarks({srcNode:"#bookmarks"});
    
    bookmarks.render();
    
    var bookmarksTestCase = new Y.Test.Case({
        name: "Lane Bookmarks Test Case",
        
        testNotEditing : function() {
        	Y.Assert.isFalse(bookmarks.get("editing"));
        },
        
        testBookmarkLabel : function() {
        	alert(bookmarks.get("editing"));
        	alert(bookmarks.get("bookmarks")[0].label)
        }
    });
    
    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");

    Y.Test.Runner.add(bookmarksTestCase);
    Y.Test.Runner.run();
});
