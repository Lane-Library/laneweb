"use strict";

var finditTestCase = new Y.Test.Case({
    name: 'Lane Findit Test Case',

    testFindIt: function() {
        var node = Y.one("#findIt").one("a");
        Y.Assert.areEqual("http://www.example.com/openurl", node.get("href"));
        Y.Assert.areEqual("title", node.get("text"));
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(finditTestCase);
Y.Test.Runner.masterSuite.name = "findit-test.js";
Y.Test.Runner.run();
