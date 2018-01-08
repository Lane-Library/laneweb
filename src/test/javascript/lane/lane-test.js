"use strict";

var laneTestCase = new Y.Test.Case({
    name: "Lane TestCase",
    "test L exists" : function() {
        Y.Assert.isObject(L);
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(laneTestCase);
Y.Test.Runner.masterSuite.name = "lane-test.js";
Y.Test.Runner.run();
