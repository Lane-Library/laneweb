/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('console', 'test', function(T) {

	var historyTestCase = new T.Test.Case({
		name : 'History Test Case',
		
		testInitialSize : function() {
			T.Assert.areEqual(3, Y.lane.History.get("links").length);
		},
		
		testAddItem : function() {
			Y.lane.History.addItem({label:"label",url:"url"});
			T.Assert.areEqual(4, Y.lane.History.get("links").length);
			T.Assert.areEqual("label", Y.lane.History.get("links")[0].label);
			T.Assert.areEqual(4, Y.one("#history").all("li").size());
			T.Assert.areEqual("url", Y.one("#history").all("a").item(0).getAttribute("href"));
		}
	});

    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    T.Test.Runner.add(historyTestCase);
    T.Test.Runner.run();
});
