"use strict";

var authorsToggleTestCase = new Y.Test.Case({
    name: "authors toggle Test Case",
    testToggleAuthors: function() {
        var trigger = Y.one(".authorsTrigger"), 
        parent = trigger.get('parentNode'),
        hiddenAuthors = parent.one('.authors-hide');

        Y.Assert.areEqual(hiddenAuthors.getStyle('display'), 'none');
        trigger.simulate('click');
        Y.Assert.areNotEqual(hiddenAuthors.getStyle('display'), 'none');
        Y.Assert.areEqual(' - show less ', trigger.get("text"));
        trigger.simulate('click');
        Y.Assert.areEqual(hiddenAuthors.getStyle('display'), 'none');
        Y.Assert.areEqual(' ... show more ', trigger.get("text"));
    }
});

Y.one("body").addClass("yui3-skin-sam");
new Y.Console({
    newestOnTop: false
}).render("#log");


Y.Test.Runner.add(authorsToggleTestCase);
Y.Test.Runner.masterSuite.name = "authors-toggle-test.js";
Y.Test.Runner.run();
