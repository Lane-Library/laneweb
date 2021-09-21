YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var browzineTestCase = new Y.Test.Case({

        name: "Lane Browzine Testcase",

        "test 1": function() {
            L.fire("viewport:init", {
                viewport: {
                    nearView: function(node) {
                        return node.dataset.doi === '10.1';
                    }
                }
            });
            var links = document.querySelectorAll("li[data-doi='10.1'] a"),
                lastLink = links[links.length - 1];
            Y.Assert.areEqual("fulltext-url", lastLink.getAttribute("href"))
            Y.Assert.areEqual(true, lastLink.isTrackableAsEvent)
        },

        "test 2": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    nearView: function(node) {
                        return node.dataset.doi === '10.2';
                    }
                }
            });
            var links = document.querySelectorAll("li[data-doi='10.2'] a"),
                lastLink = links[links.length - 1];
            Y.Assert.areEqual("fulltext-url", lastLink.getAttribute("href"))
            Y.Assert.areEqual(true, lastLink.isTrackableAsEvent)
        },
        
        "test 3 no doi": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    nearView: function(node) {
                        return node.dataset.sid === 'bib-3';
                    }
                }
            });
            Y.Assert.areEqual("old", document.querySelector("li[data-sid='bib-3'] a").getAttribute("href"))
        },

        "test 4": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    nearView: function(node) {
                        return node.dataset.doi === '10.4';
                    }
                }
            });
            var links = document.querySelectorAll("li[data-doi='10.4'] a"),
                lastLink = links[links.length - 1];
            Y.Assert.areEqual("contentLocation", lastLink.getAttribute("href"))
            Y.Assert.areEqual(true, lastLink.isTrackableAsEvent)
        },

        "test 5": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    nearView: function(node) {
                        return node.dataset.sid === 'pubmed-5';
                    }
                }
            });
            Y.Assert.areEqual("old", document.querySelector("li[data-sid='pubmed-5'] a").getAttribute("href"))
        }


        
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(browzineTestCase);
    Y.Test.Runner.masterSuite.name = "browzine-test.js";
    Y.Test.Runner.run();

});
