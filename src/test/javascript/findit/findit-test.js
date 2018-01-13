YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var finditTestCase = new Y.Test.Case({
        name: 'Lane Findit Test Case',

        testFindIt: function() {
            var node = Y.one("#findIt").one("a");
            Y.Assert.areEqual("http://www.example.com/openurl", node.get("href"));
            Y.Assert.areEqual("title", node.get("text"));
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(finditTestCase);
    Y.Test.Runner.masterSuite.name = "findit-test.js";
    Y.Test.Runner.run();

});