YUI({ fetchCSS: false }).use("test", "test-console", function (Y) {

    "use strict";

    let spellCheckTestCase = new Y.Test.Case({

        name: 'Lane Spellcheck Test Case',

        testSpellCheck: function () {
            let node = document.querySelector(".spellCheck a");
            Y.Assert.areEqual("suggestion", node.text);
            Y.Assert.areEqual(location.href, node.getAttribute("href"));
            Y.Assert.areEqual("inline", node.style, display);
        }
    });

    new Y.Test.Console().render();


    Y.Test.Runner.add(spellCheckTestCase);
    Y.Test.Runner.masterSuite.name = "spellcheck-test.js";
    Y.Test.Runner.run();

});
