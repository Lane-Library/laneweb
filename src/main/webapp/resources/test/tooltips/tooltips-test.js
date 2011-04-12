/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("overlay", 'node-event-simulate', 'console', 'test', function(T){

    var tooltipTestCase = new T.Test.Case({
        name: 'Lane Tooltip Test Case',
        testStaysOnPage: function() {
        	var b = T.one("#b");
        	var right = T.DOM.viewportRegion().right;
        	var inside = false;
        	var eventHandle = Y.lane.ToolTips.after("visibleChange", function(e) {
        		var boundingBox = this.get("boundingBox");
        		inside = T.DOM.inViewportRegion(T.Node.getDOMNode(boundingBox));
                eventHandle.detach();
        	});
            b.simulate("mouseover",{clientX:right,pageX:right});
            this.wait(function() {
            	T.Assert.isTrue(inside);
            }, 500);
            b.simulate("mouseout");
        },
        
        testOnlyOneWidget : function() {
        	T.Assert.areEqual(1, T.all(".yui3-tooltip").size());
        	Y.fire("lane:change");
        	T.Assert.areEqual(1, T.all(".yui3-tooltip").size());
        }
    });
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    T.Test.Runner.add(tooltipTestCase);
    T.Test.Runner.run();
});