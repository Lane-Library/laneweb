YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var popinTestCase = new Y.Test.Case({

        name: 'Lane Popin Test Case',

        testPopinEvent: function() {
            var node = Y.one("#spellCheck");
            node.setStyle("display", "inline");
            L.fire("lane:popin", "spellCheck");
            Y.Assert.isTrue(node.hasClass("popin-active"));
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(popinTestCase);
    Y.Test.Runner.masterSuite.name = "popin-test.js";
    Y.Test.Runner.run();

});
