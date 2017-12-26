"use strict";

var spellCheckTestCase = new Y.Test.Case({
    name: 'Lane Spellcheck Test Case',
    testSpellCheck: function() {
        var node = Y.one("#spellCheck").one("a");
        Y.Assert.areEqual("suggestion", node.get("text"));
        Y.Assert.areEqual(location.href, node.get("href"));
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(spellCheckTestCase);
Y.Test.Runner.masterSuite.name = "spellcheck-test.js";
Y.Test.Runner.run();
