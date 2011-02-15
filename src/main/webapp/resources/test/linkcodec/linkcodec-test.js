/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("test", "console", function(T){
    

    var linkCodecTestCase = new T.Test.Case({
        name: "Lane Bookmarks Test Case",
        
        testIsLocalLink : function() {
        	var link = Y.all("a").item(0);
        	T.Assert.isTrue(Y.lane.LinkCodec.isLocal(link));
        },
        
        testIsProxyLoginLink : function () {
        	var link = Y.all("a").item(1);
        	T.Assert.isTrue(Y.lane.LinkCodec.isProxyLogin(link));
        },
        
        testIsProxyLink : function() {
        	var link = Y.all("a").item(2);
        	T.Assert.isTrue(Y.lane.LinkCodec.isProxy(link));
        },
        
        testProxyLoginIsNotLocal : function() {
        	var link = Y.all("a").item(1);
        	T.Assert.isFalse(Y.lane.LinkCodec.isLocal(link));
        }
    });
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");

    T.Test.Runner.add(linkCodecTestCase);
    T.Test.Runner.run();
});
