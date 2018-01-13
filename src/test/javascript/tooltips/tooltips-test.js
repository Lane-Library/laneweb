YUI({fetchCSS:false}).use("test", "test-console", "dom", "node-event-simulate", function(Y) {

    "use strict";

    var tooltipTestCase = new Y.Test.Case({

        name: 'Lane Tooltip Test Case',

        testStaysOnPage: function() {
            var b = Y.one("#b");
            var right = Y.DOM.viewportRegion().right;
            var inside = false;
            var eventHandle = L.ToolTips.after("visibleChange", function(e) {
                var boundingBox = this.get("boundingBox");
                inside = Y.DOM.inViewportRegion(Y.Node.getDOMNode(boundingBox));
                eventHandle.detach();
            });
            b.simulate("mouseover",{clientX:right,pageX:right});
            this.wait(function() {
                Y.Assert.isTrue(inside);
            }, 500);
            b.simulate("mouseout");
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(tooltipTestCase);
    Y.Test.Runner.masterSuite.name = "tooltips-test.js";
    Y.Test.Runner.run();

});
