/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', "node-event-simulate", function(Y) {
    
    var bookmarkLinkTestCase = new Y.Test.Case({
        
        name : "BookmarkLink Test Case",
        
        link : Y.lane.BookmarkLink,
    
        testExists : function() {
            Y.Assert.isTrue(Y.Lang.isObject(this.link));
        },
        
        testDontShowAlreadyBookmarked : function() {
            Y.one("#alreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual(0, this.link.get("status"));
        },
        
        testAnotherDontShowAlreadyBookmarked : function() {
            Y.one("#anotherAlreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual(0, this.link.get("status"));
        }
        
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(bookmarkLinkTestCase);
    Y.Test.Runner.masterSuite.name = "bookmark-link-test.js";
    Y.Test.Runner.run();
});
