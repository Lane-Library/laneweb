YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let authorsToggleTestCase = new Y.Test.Case({

        name: "authors toggle Test Case",

        testToggleAuthors: function() {
            let trigger = document.querySelector(".authorsTrigger"), 
                parent = trigger.parentNode,
                hiddenAuthors = parent.querySelector(".authors-hide");

            Y.Assert.areEqual("", hiddenAuthors.style.display);
            trigger.click();
            Y.Assert.areNotEqual("", hiddenAuthors.style.display);
            Y.Assert.areEqual("Show Less", trigger.textContent.trim());
            trigger.click();
            Y.Assert.areEqual("none", hiddenAuthors.style.display);
            Y.Assert.areEqual("Show More", trigger.textContent.trim());
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(authorsToggleTestCase);
    Y.Test.Runner.masterSuite.name = "authors-toggle-test.js";
    Y.Test.Runner.run();

});
