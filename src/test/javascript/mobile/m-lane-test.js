"use strict";

YUI().use("*", function(Y) {

    var laneMobileTestCase = new Y.Test.Case({
        name: "Lane Mobile TestCase",
        "test $.LANE exists" : function() {
            Y.Assert.isObject($.LANE);
        }
    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(laneMobileTestCase);
    Y.Test.Runner.masterSuite.name = "m-lane-test.js";
    Y.Test.Runner.run();
});
