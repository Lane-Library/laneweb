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
            Y.Assert.isTrue(Y.Lang.isObject(Y.one('#a-yuitt')));
		},
        testSimpleTooltipPresent: function() {
            Y.Assert.isTrue(Y.Lang.isObject(Y.one('#simpleTT-yui')));
        },
        testRemoveAndFireEvent: function() {
            var aTooltip = Y.one('#aTooltip');
            var tooltips = aTooltip.get('parent');
            tooltips.removeChild(aTooltip);
            Y.fire('lane:change');
            Y.isFalse(Y.isObject(Y.one('#a-yuitt')));
        }
	});
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(tooltipTestCase);
    Y.Test.Runner.run();
});