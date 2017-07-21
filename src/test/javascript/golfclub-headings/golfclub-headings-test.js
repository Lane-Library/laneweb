"use strict";

var golfclubHeadingsTestCase = new Y.Test.Case({
    name: "Lane Golf Club Headings TestCase",
    test: function() {
        var heading = document.querySelector("h2");
        Y.Assert.areEqual('<span><span class="golfclub-left"></span>heading<span class="golfclub-right"></span></span>', heading.innerHTML);
    }
});

document.querySelector("body").className = "yui3-skin-sam";
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(golfclubHeadingsTestCase);
Y.Test.Runner.masterSuite.name = "golfclub-headings-test.js";
Y.Test.Runner.run();
