YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let menuTestCase = new Y.Test.Case({

        name: 'Lane Menu Test Case',

        testCloseItem: function() {
            let up = Y.one(".fa-chevron-up");
            up.simulate("click");
            Y.Assert.isTrue(up._node.className === 'fa fa-chevron-down');
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(menuTestCase);
    Y.Test.Runner.masterSuite.name = "menu-test.js";
    Y.Test.Runner.run();

});