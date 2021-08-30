YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var browzineTestCase = new Y.Test.Case({

        name: "Lane Browzine Testcase",

        "test 1": function() {
            var links = document.querySelectorAll("li[data-doi='1'] a"),
                lastLink = links[links.length - 1];
            Y.Assert.areEqual("fulltext-url", lastLink.getAttribute("href"))
            Y.Assert.areEqual(true, lastLink.isTrackableAsEvent)
        },

        "test 2": function() {
            var links = document.querySelectorAll("li[data-doi='2'] a"),
                lastLink = links[links.length - 1];
            Y.Assert.areEqual("fulltext-url", lastLink.getAttribute("href"))
            Y.Assert.areEqual(true, lastLink.isTrackableAsEvent)
        },
        
        "test 3 no doi": function() {
            Y.Assert.areEqual("old", document.querySelector("li:not([data-doi]) a").getAttribute("href"))
            Y.Assert.areEqual(1, document.querySelectorAll("li:not([data-doi]) a").length);
        },

        "test 4": function() {
            var links = document.querySelectorAll("li[data-doi='4'] a"),
                lastLink = links[links.length - 1];
            Y.Assert.areEqual("contentLocation", lastLink.getAttribute("href"))
            Y.Assert.areEqual(true, lastLink.isTrackableAsEvent)
        }


        
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(browzineTestCase);
    Y.Test.Runner.masterSuite.name = "browzine-test.js";
    Y.Test.Runner.run();

});
