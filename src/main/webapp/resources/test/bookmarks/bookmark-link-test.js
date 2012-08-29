/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', function(Y) {
    
    bookmarkLinkTestCase = new Y.Test.Case({
        
        name : "BookmarkLink Test Case",
        
        link : null,
        
        setUp : function() {
            this.link = Y.lane.BookmarkLink;
        },
    
        testExists : function() {
            Y.Assert.isTrue(Y.Lang.isObject(this.link));
        }
        
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(bookmarkLinkTestCase);
    Y.Test.Runner.run();
});
