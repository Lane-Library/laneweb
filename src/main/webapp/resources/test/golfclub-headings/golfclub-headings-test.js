"use strict";

var golfclubHeadingsTestCase = new Y.Test.Case({
    name: "Lane Selections TestCase",
    test: function() {
        var heading = document.querySelector("h2");
        Y.Assert.areEqual("<span><span>heading</span></span>", heading.innerHTML);
    }
});

document.querySelector("body").className = "yui3-skin-sam";
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(golfclubHeadingsTestCase);
Y.Test.Runner.masterSuite.name = "golfclub-headings-test.js";
Y.Test.Runner.run();
