/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("test", "console", "node-event-simulate", function(T){
    

    var linkTestCase = new T.Test.Case({
        name: "Lane Link Test Case",
        
        testIsLocalLink : function() {
        	var anchor = T.all("a").item(0);
        	anchor.simulate("click");
        	T.Assert.isTrue(anchor.link.isLocal());
        },
        
        testIsProxyLoginLink : function () {
        	var anchor = T.all("a").item(1);
        	anchor.simulate("click");
        	T.Assert.isTrue(anchor.link.isProxyLogin());
        },
        
        testIsProxyLink : function() {
        	var anchor = T.all("a").item(2);
        	anchor.simulate("click");
        	T.Assert.isTrue(anchor.link.isProxy());
        },
        
        testProxyLoginIsNotLocal : function() {
        	var anchor = T.all("a").item(1);
        	anchor.simulate("click");
        	T.Assert.isFalse(anchor.link.isLocal());
        }
    });
    
	T.all("a").on("click", function(e) {
		e.preventDefault();
		e.target.plug(Y.lane.LinkPlugin);
		var foo = e.target;
		var bar = foo;
//		T.mix(e.target, Y.lane.Link);
	});
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");

    T.Test.Runner.add(linkTestCase);
    T.Test.Runner.run();
});
