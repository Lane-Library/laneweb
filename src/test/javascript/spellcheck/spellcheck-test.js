YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var spellCheckTestCase = new Y.Test.Case({

        name: 'Lane Spellcheck Test Case',

        testSpellCheck: function() {
            var node = Y.one(".spellCheck").one("a");
            Y.Assert.areEqual("suggestion", node.get("text"));
            Y.Assert.areEqual(location.href, node.get("href"));
            Y.Assert.areEqual("inline", node.getStyle("display"));
        }
    });

    new Y.Test.Console().render();


    Y.Test.Runner.add(spellCheckTestCase);
    Y.Test.Runner.masterSuite.name = "spellcheck-test.js";
    Y.Test.Runner.run();

});
