/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var tooltipTestCase = new Y.Test.Case({
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
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(tooltipTestCase);
    Y.Test.Runner.run();
});