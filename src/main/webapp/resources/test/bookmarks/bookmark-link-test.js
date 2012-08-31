/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', "node-event-simulate", function(Y) {
    
    bookmarkLinkTestCase = new Y.Test.Case({
        
        name : "BookmarkLink Test Case",
        
        link : null,
        
        setUp : function() {
            this.link = Y.lane.BookmarkLink;
        },
    
        testExists : function() {
            Y.Assert.isTrue(Y.Lang.isObject(this.link));
        },
        
        testDontShowAlreadyBookmarked : function() {
            Y.one("#alreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual(0, this.link.get("state"));
        }
        
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(bookmarkLinkTestCase);
    Y.Test.Runner.run();
});
