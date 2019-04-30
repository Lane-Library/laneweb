YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var bookcoversTestCase = new Y.Test.Case({

        name: "Lane Bookcovers Testcase",

        "test viewport:init": function() {
            var inview = 0;
            L.fire("viewport:init", {
                viewport: {
                    inView: function() {
                        return inview++ === 0;
                    }
                }
            });
            Y.Assert.areEqual("//www.example.com/srcbib-1", document.querySelector("div[data-bcid='bib-1'] img").getAttribute("src"))
        },

        "test viewport:scrolled": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    inView: function() {
                        return true;
                    }
                }
            });
            Y.Assert.areEqual("//www.example.com/srcsul-2", document.querySelector("div[data-bcid='sul-2'] img").getAttribute("src"))
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(bookcoversTestCase);
    Y.Test.Runner.masterSuite.name = "bookcovers-test.js";
    Y.Test.Runner.run();

});
