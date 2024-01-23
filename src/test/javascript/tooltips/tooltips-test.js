YUI({fetchCSS:false}).use("test", "test-console", "dom", "node-event-simulate", function(Y) {

    "use strict";

    let tooltipTestCase = new Y.Test.Case({

        name: 'Lane Tooltip Test Case',

        testStaysOnPage: function() {
            let b = Y.one("#b");
            let right = Y.DOM.viewportRegion().right;
            let inside = false;
            let eventHandle = L.ToolTips.after("visibleChange", function(e) {
                let boundingBox = this.get("boundingBox");
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
