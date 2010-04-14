/**
 * @author ceyates
 */
(function() {
	
	var tooltipTestCase = new YAHOO.tool.TestCase({
		name: 'Lane Tooltip Test Case',
		testFoo: function() {
			var yuitt = document.getElementById('a-yuitt');
			var a = document.getElementById('a');
			YAHOO.util.Assert.areEqual('hidden', yuitt.style.visibility);
			YAHOO.util.UserAction.mouseover(a);
//			YAHOO.util.Assert.areEqual('visible', yuitt.style.visibility);
			YAHOO.util.UserAction.mouseout(a);
//			YAHOO.util.Assert.areEqual('hidden', yuitt.style.visibility);
		}
	})
    new YAHOO.tool.TestLogger();
	YAHOO.tool.TestRunner.add(tooltipTestCase);
	YAHOO.util.Event.addListener(window, 'load', function() {
    	YAHOO.tool.TestRunner.run();
	});
})();
