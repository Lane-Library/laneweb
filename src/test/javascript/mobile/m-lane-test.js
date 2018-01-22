YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var laneMobileTestCase = new Y.Test.Case({
        name: "Lane Mobile TestCase",
        "test $.LANE exists" : function() {
            Y.Assert.isObject($.LANE);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(laneMobileTestCase);
    Y.Test.Runner.masterSuite.name = "m-lane-test.js";
    Y.Test.Runner.run();

});
