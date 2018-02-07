YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var clinicalToggleTestCase = new Y.Test.Case({

        name : "Clinical Toggle TestCase",

        "test indicator active on click" : function() {
            var toggle = document.querySelector(".clinical-toggle");
            var indicator = document.querySelector(".search-indicator");
            Y.Assert.isFalse(indicator.classList.contains("search-indicator-active"));
            toggle.click();
            Y.Assert.isTrue(indicator.classList.contains("search-indicator-active"));
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(clinicalToggleTestCase);
    Y.Test.Runner.masterSuite.name = "clinical-toggle-test.js";
    Y.Test.Runner.run();

});
