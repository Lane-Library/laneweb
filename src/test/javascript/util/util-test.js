YUI({filter:"debug"}).use("test", "test-console", function(Y) {

    "use strict";

    let laneTestCase = new Y.Test.Case({

        name: "Util TestCase",

        lane: L,

        "test L exists" : function() {
            Y.Assert.isObject(L);
        },

        "test NodeList.forEach": function() {
            Y.Assert.areEqual(typeof document.querySelectorAll("*").forEach, "function");
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(laneTestCase);
    Y.Test.Runner.masterSuite.name = "util-test.js";
    Y.Test.Runner.run();

});