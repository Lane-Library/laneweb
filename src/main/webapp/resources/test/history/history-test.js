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
		
		setUp : function() {
			Y.lane.HistoryTracker.set("io", function(){});
		},
		
		testInitialSize : function() {
			T.Assert.areEqual(3, Y.lane.History.get("model").length);
		},
		
		testAddItem : function() {
			T.Assert.isTrue(Y.lane.History.addItem({label:"label",url:"url"}));
			T.Assert.areEqual(4, Y.lane.History.get("model").length);
			T.Assert.areEqual("label", Y.lane.History.get("model")[0].label);
			T.Assert.areEqual(4, Y.lane.History.get("view").all("li").size());
			T.Assert.areEqual("url", Y.lane.History.get("view").all("a").item(0).getAttribute("href"));
		},
		
		testAddSameItem : function() {
			T.Assert.isFalse(Y.lane.History.addItem({label:"label",url:"url"}));
			T.Assert.areEqual(4, Y.lane.History.get("model").length);
			T.Assert.areEqual(4, Y.lane.History.get("view").all("li").size());
		}
	});

    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    T.Test.Runner.add(historyTestCase);
    T.Test.Runner.run();
});
