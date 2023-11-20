YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let altmetricTestCase = new Y.Test.Case({

        name: "altmetric Test Case",

        testAltmetricAuthd: function() {
            let scripts = document.getElementsByTagName('script');
            Y.Assert.isTrue(scripts.length > 8);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(altmetricTestCase);
    Y.Test.Runner.masterSuite.name = "altmetric-test.js";
    Y.Test.Runner.run();

});
