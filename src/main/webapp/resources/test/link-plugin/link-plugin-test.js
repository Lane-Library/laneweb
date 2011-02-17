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
        	var anchor = T.one("#local");
        	T.Assert.isTrue(anchor.link.isLocal());
        },
        
        testIsProxyLoginLink : function () {
        	var anchor = T.one("#proxylogin");
        	T.Assert.isTrue(anchor.link.isProxyLogin());
        },
        
        testIsProxyLink : function() {
        	var anchor = T.one("#proxyurl");
        	T.Assert.isTrue(anchor.link.isProxy());
        },
        
        testProxyLoginIsNotLocal : function() {
        	var anchor = T.one("#proxylogin");
        	T.Assert.isFalse(anchor.link.isLocal());
        },
        
        testGetURL : function() {
        	var anchor = T.one("#example");
        	T.Assert.areEqual("http://www.example.com/example", anchor.link.getURL());
        },
        
        testGetProxiedLoginURL : function() {
        	var anchor = T.one("#proxylogin");
        	T.Assert.areEqual("http://www.nejm.org/", anchor.link.getURL());
        }, 
        
        testGetProxiedURL : function() {
        	var anchor = T.one("#proxyurl");
        	T.Assert.areEqual("http://www.nejm.org/", anchor.link.getURL());
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
