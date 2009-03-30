/**
 * @author ceyates
 */
(function() {
	var Assert = YAHOO.util.Assert;
	var TestRunner = YAHOO.tool.TestRunner;
	var TestCase = YAHOO.tool.TestCase;
	var UserAction = YAHOO.util.UserAction;

    var EresourcesTestCase = new TestCase({
        name: "Eresources TestCase",
        testFoo: function() {
    		var result = {};
    		LANE.search.eresources.setCurrentResult(result);
    		Assert.areEqual(result, LANE.search.eresources.getCurrentResult());
    	}
    });



	new YAHOO.tool.TestLogger();
	TestRunner.add(EresourcesTestCase);
	TestRunner.run();
})();