"use strict";

var laneTestCase = new Y.Test.Case({

    name: "Lane TestCase",

    lane: Y.lane,

    "test Y.lane exists" : function() {
        Y.Assert.isObject(Y.lane);
    },

    "test activate": function() {
        Y.lane.activate(document.querySelector(".test"));
        Y.Assert.isTrue(Y.one(".test").hasClass("active"));
    },

    "test deactivate": function() {
        Y.one(".test").addClass("active");
        Y.lane.deactivate(document.querySelector(".test"));
        Y.Assert.isFalse(Y.one(".test").hasClass("active"));
    },

    "test getData": function() {
        Y.Assert.areEqual("test", Y.lane.getData(document.querySelector(".test"), "test"));
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(laneTestCase);
Y.Test.Runner.masterSuite.name = "lane-test.js";
Y.Test.Runner.run();
