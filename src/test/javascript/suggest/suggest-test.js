YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let suggestTestCase = new Y.Test.Case({

        name: "Lane Suggest Testcase"

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(suggestTestCase);
    Y.Test.Runner.masterSuite.name = "suggest-test.js";
    Y.Test.Runner.run();

});
