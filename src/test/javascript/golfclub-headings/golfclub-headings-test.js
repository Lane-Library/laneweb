YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var golfclubHeadingsTestCase = new Y.Test.Case({

        name: "Lane Golf Club Headings TestCase",

        test: function() {
            var heading = document.querySelector("h2");
            Y.Assert.areEqual('<span><span class="golfclub-left"></span>heading<span class="golfclub-right"></span></span>', heading.innerHTML);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(golfclubHeadingsTestCase);
    Y.Test.Runner.masterSuite.name = "golfclub-headings-test.js";
    Y.Test.Runner.run();

});
