YUI({ fetchCSS: false }).use("test", "test-console", function (Y) {

    "use strict";



    let bookcoversTestCase = new Y.Test.Case({

        name: "Lane Bookcovers Testcase",

        "test viewport:init": function () {
            L.fire("viewport:init", {
                viewport: {
                    nearView: function (node) {
                        return node.dataset.bcids === "bib-1";
                    }
                }
            });
            Y.Assert.areEqual("//www.example.com/srcbib-1", document.querySelector("div[data-bcids='bib-1'] img").getAttribute("src"))
        },

        "test viewport:scrolled 1": function () {
            L.fire("viewport:scrolled", {
                viewport: {
                    nearView: function (node) {
                        return node.dataset.bcids === "sul-2";
                    }
                }
            });
            Y.Assert.areEqual("//www.example.com/srcsul-2", document.querySelector("div[data-bcids='sul-2'] img").getAttribute("src"))
        },

        "test viewport:scrolled 2": function () {
            L.fire("viewport:scrolled", {
                viewport: {
                    nearView: function (node) {
                        return node.dataset.bibid === "3";
                    }
                }
            });
            Y.Assert.areEqual("//www.example.com/srcbib-3", document.querySelector("div[data-bibid='3'] img").getAttribute("src"))
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(bookcoversTestCase);
    Y.Test.Runner.masterSuite.name = "bookcovers-test.js";
    Y.Test.Runner.run();

});
