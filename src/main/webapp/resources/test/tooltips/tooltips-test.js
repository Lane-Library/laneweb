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
        testRestoresHeightWidth: function() {
            var a = Y.one('#a');
            var tooltip = Y.one(".yui3-tooltip");
            Y.Assert.areEqual(0, tooltip.get("clientWidth"));
            Y.Assert.areEqual(0, tooltip.get("clientHeight"));
            var eventHandle1 = a.after("mouseover", function() {
                Y.Assert.areNotEqual(0, tooltip.get("clientWidth"));
                Y.Assert.areNotEqual(0, tooltip.get("clientHeight"));
            });
            var eventHandle2 = a.after("mouseout", function() {
                Y.Assert.areEqual(0, tooltip.get("clientWidth"));
                Y.Assert.areEqual(0, tooltip.get("clientHeight"));
            });
            a.simulate("mouseover");
            a.simulate("mouseout");
            eventHandle1.detach();
            eventHandle2.detach();
        },
        testMouseover: function() {
            var a = Y.one('#a');
            var tooltip = Y.one(".yui3-tooltip");
            Y.Assert.isTrue(tooltip.hasClass("yui3-tooltip-hidden"));
            var eventHandle = a.after("mouseover", function() {
                Y.Assert.isFalse(tooltip.hasClass("yui3-tooltip-hidden"));
            });
            a.simulate("mouseover");
            a.simulate("mouseout");
            eventHandle.detach();
        },
        testRemoveAndFireEvent: function() {
            var a = Y.one('#a');
            var tooltip = Y.one(".yui3-tooltip");
            Y.one('#aTooltip').remove();
            Y.fire('lane:change');
            Y.Assert.isTrue(tooltip.hasClass("yui3-tooltip-hidden"));
            var eventHandle = a.after("mouseover", function() {
                Y.Assert.isTrue(tooltip.hasClass("yui3-tooltip-hidden"));
            });
            a.simulate("mouseover");
            a.simulate("mouseout");
            eventHandle.detach();
        },
        testStaysOnPage: function() {
        	var b = Y.one("#b");
        	var tooltip = Y.one(".yui3-tooltip");
        	var right = Y.DOM.viewportRegion().right;
            var eventHandle = b.after("mouseover", function(e) {
            	//TODO:???
            });
            b.simulate("mouseover", {pageX:right,clientX:right});
            eventHandle.detach();
            b.simulate("mouseout");
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(tooltipTestCase);
    Y.Test.Runner.run();
});