"use strict";

var authorsToggleTestCase = new Y.Test.Case({
    name: "authors toggle Test Case",
    testToggleAuthors: function() {
        var trigger = Y.one(".authorsTrigger"), 
        parent = trigger.get('parentNode');
        Y.Assert.areEqual("some authors ... show all authors, more authors", parent.get("text"));
        trigger.simulate('click');
        Y.Assert.areEqual("some authors, more authors", parent.get("text"));
        Y.Assert.areEqual('', trigger.get("text"));
    }
});

Y.one("body").addClass("yui3-skin-sam");
new Y.Console({
    newestOnTop: false
}).render("#log");


Y.Test.Runner.add(authorsToggleTestCase);
Y.Test.Runner.masterSuite.name = "authors-toggle-test.js";
Y.Test.Runner.run();
