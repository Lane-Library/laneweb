YUI({ fetchCSS: false }).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let altmetricTestCase = new Y.Test.Case({

        name: "altmetric Test Case",

        testAltmetric: function() {
            this.wait(function() {
                let loadedBadgeJs = document.querySelectorAll("script[src='https://badge.dimensions.ai/badge.js'], script[src='https://d1bxh8uas1mnw7.cloudfront.net/assets/embed.js']");
                Y.Assert.isTrue(loadedBadgeJs.length == 2);
            }, 200);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(altmetricTestCase);
    Y.Test.Runner.masterSuite.name = "altmetric-test.js";
    Y.Test.Runner.run();

});
