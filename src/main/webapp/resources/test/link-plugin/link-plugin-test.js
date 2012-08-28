/**
 * @author ceyates
 */
YUI({
	filter : "debug",
	debug : true,
    logInclude: {
        TestRunner: true
    }
}).use("test", "console", function(T){
    

    var linkTestCase = new T.Test.Case({
        name: "Lane Link Test Case",
        
        testIsLocalLink : function() {
        	var anchor = Y.one("#local");
        	T.Assert.isTrue(anchor.link.get("local"));
        },
        
        testIsProxyLoginLink : function () {
        	var anchor = Y.one("#proxylogin");
        	T.Assert.isTrue(anchor.link.get("proxyLogin"));
        },
        
        testIsProxyLink : function() {
        	var anchor = Y.one("#proxyurl");
        	T.Assert.isTrue(anchor.link.get("proxy"));
        },
        
        testProxyLoginIsNotLocal : function() {
        	var anchor = Y.one("#proxylogin");
        	T.Assert.isFalse(anchor.link.get("local"));
        },
        
        testGetURL : function() {
        	var anchor = Y.one("#example");
        	T.Assert.areEqual("http://www.example.com/example", anchor.link.get("url"));
        },
        
        testGetProxiedLoginURL : function() {
        	var anchor = Y.one("#proxylogin");
        	T.Assert.areEqual("http://www.nejm.org/", anchor.link.get("url"));
        }, 
        
        testGetProxiedURL : function() {
        	var anchor = Y.one("#proxyurl");
        	T.Assert.areEqual("http://www.nejm.org/", anchor.link.get("url"));
        },
        
        testCookiesFetchIsNotLocal : function() {
        	var anchor = Y.one("#cookiesFetch");
        	T.Assert.isFalse(anchor.link.get("local"));
        },
        
        testGetCookiesFetchURL : function() {
        	var anchor = Y.one("#cookiesFetch");
        	T.Assert.areEqual("http://www.thomsonhc.com/carenotes/librarian/", anchor.link.get("url"));
        },
        
        testGetTitle: function() {
        	var i, anchor, anchors = Y.all("#testGetTitle a");
        	for (i = 0; i < anchors.size(); i++) {
        		anchor = anchors.item(i);
        		T.Assert.areEqual(anchor.get("rel"), anchor.link.get("title"));
        	}
        }
    });
    
	Y.all("a").plug(Y.lane.LinkPlugin);
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");

    T.Test.Runner.add(linkTestCase);
    T.Test.Runner.run();
});
