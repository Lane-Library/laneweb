/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("test", "console", function(T){
    

    var linkTestCase = new T.Test.Case({
        name: "Lane Link Test Case",
        
        testIsLocalLink : function() {
        	var anchor = T.all("a").item(0);
        	T.Assert.isTrue(anchor.link.isLocal());
        },
        
        testIsProxyLoginLink : function () {
        	var anchor = T.all("a").item(1);
        	T.Assert.isTrue(anchor.link.isProxyLogin());
        },
        
        testIsProxyLink : function() {
        	var anchor = T.all("a").item(2);
        	T.Assert.isTrue(anchor.link.isProxy());
        },
        
        testProxyLoginIsNotLocal : function() {
        	var anchor = T.all("a").item(1);
        	T.Assert.isFalse(anchor.link.isLocal());
        }
    });
    
	T.all("a").plug(Y.lane.LinkPlugin);
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");

    T.Test.Runner.add(linkTestCase);
    T.Test.Runner.run();
});
