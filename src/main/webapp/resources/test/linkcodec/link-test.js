/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("test", "console", "node-event-simulate", function(T){
    

    var linkTestCase = new T.Test.Case({
        name: "Lane Bookmarks Test Case",
        
        setUp : function() {
        	target = null;
        },
        
        testIsLocalLink : function() {
        	var link = T.all("a").item(0);
        	link.simulate("click");
        	T.Assert.isTrue(target.isLocal());
        },
        
        testIsProxyLoginLink : function () {
        	var link = T.all("a").item(1);
        	link.simulate("click");
        	T.Assert.isTrue(target.isProxyLogin());
        },
        
        testIsProxyLink : function() {
        	var link = T.all("a").item(2);
        	link.simulate("click");
        	T.Assert.isTrue(target.isProxy());
        },
        
        testProxyLoginIsNotLocal : function() {
        	var link = T.all("a").item(1);
        	link.simulate("click");
        	T.Assert.isFalse(target.isLocal());
        }
    }),
    
    target = null;
    
    T.on("click", function(e){
    	target = e.target;
    });


	T.all("a").on("click", function(e) {
		T.mix(e.target, Y.lane.Link);
	});
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");

    T.Test.Runner.add(linkTestCase);
    T.Test.Runner.run();
});
