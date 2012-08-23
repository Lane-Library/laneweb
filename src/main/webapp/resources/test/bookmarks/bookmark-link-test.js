/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('console', 'test', function(T) {
    
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

    T.Test.Runner.add(bookmarkLinkTestCase);
    T.Test.Runner.run();
});
