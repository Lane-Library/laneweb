YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var browzineTestCase = new Y.Test.Case({

        name: "Lane Browzine Testcase",

        "test viewport:init": function() {
            var inview = 0;
            L.fire("viewport:init", {
                viewport: {
                    inView: function() {
                        return inview++ === 0;
                    }
                }
            });
            Y.Assert.areEqual("fulltext-url", document.querySelector("li[data-doi='1'] a").getAttribute("href"))
        },

        "test viewport:scrolled 1": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    inView: function() {
                        return true;
                    }
                }
            });
            Y.Assert.areEqual("fulltext-url", document.querySelector("li[data-doi='2'] a").getAttribute("href"))
        },
        
        "test viewport:scrolled 2": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    inView: function() {
                        return true;
                    }
                }
            });
            Y.Assert.areEqual("old", document.querySelector("li:not([data-doi]) a").getAttribute("href"))
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(browzineTestCase);
    Y.Test.Runner.masterSuite.name = "browzine-test.js";
    Y.Test.Runner.run();

});
