YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let historyPhotosTestCase = new Y.Test.Case({

        name: 'History Photos Test Case'

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(historyPhotosTestCase);
    Y.Test.Runner.masterSuite.name = "history-photos-test.js";
    Y.Test.Runner.run();

});
