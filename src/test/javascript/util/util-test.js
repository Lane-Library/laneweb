YUI({filter:"debug"}).use("test", "test-console", function(Y) {

    "use strict";

    var laneTestCase = new Y.Test.Case({

        name: "Util TestCase",

        lane: L,

        "test L exists" : function() {
            Y.Assert.isObject(L);
        },

        "test activate": function() {
            L.activate(document.querySelector(".test", "test"));
            Y.Assert.isTrue(Y.one(".test").hasClass("test-active"));
        },

        "test deactivate": function() {
            Y.one(".test").addClass("test-active");
            L.deactivate(document.querySelector(".test"), "test");
            Y.Assert.isFalse(Y.one(".test").hasClass("test-active"));
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