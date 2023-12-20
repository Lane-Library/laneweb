YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let laneTestCase = new Y.Test.Case({
        name: "Lane TestCase",
        "test L exists" : function() {
            Y.Assert.isObject(L);
        }
    });

    new Y.Test.Console().render();


    Y.Test.Runner.add(laneTestCase);
    Y.Test.Runner.masterSuite.name = "lane-test.js";
    Y.Test.Runner.run();

});