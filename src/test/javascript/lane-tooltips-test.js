/**
 * @author ceyates
 */
(function() {
	
	var tooltipTestCase = new YAHOO.tool.TestCase({
		name: 'Lane Tooltip Test Case',
		testTooltipPresent: function() {
            YAHOO.util.Assert.isTrue(YAHOO.lang.isObject(document.getElementById('a-yuitt')));
		},
        testSimpleTooltipPresent: function() {
            YAHOO.util.Assert.isTrue(YAHOO.lang.isObject(document.getElementById('simpleTT-yui')));
        },
        testRemoveAndFireEvent: function() {
            var aTooltip = document.getElementById('aTooltip');
            var tooltips = aTooltip.parentNode;
            tooltips.removeChild(aTooltip);
            LANE.core.getChangeEvent().fire();
            YAHOO.util.Assert.isFalse(YAHOO.lang.isObject(document.getElementById('a-yuitt')));
        }
	});
    new YAHOO.tool.TestLogger();
	YAHOO.tool.TestRunner.add(tooltipTestCase);
	YAHOO.util.Event.addListener(window, 'load', function() {
    	YAHOO.tool.TestRunner.run();
	});
})();
