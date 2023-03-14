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
        },
        
        "test 3 no doi": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    nearView: function(node) {
                        return node.dataset.sid === 'bib-3';
                    }
                }
            });
            var links = document.querySelectorAll("li[data-sid='bib-3'] a"),
                lastLink = links[links.length - 1];
            Y.Assert.areEqual("sfx-link", lastLink.getAttribute("href"))
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
        },

        "test 5": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    nearView: function(node) {
                        return node.dataset.sid === 'pubmed-5';
                    }
                }
            });
            var links = document.querySelectorAll("li[data-sid='pubmed-5'] a"),
                lastLink = links[links.length - 1];
            Y.Assert.areEqual("sfx-link", lastLink.getAttribute("href"))
        },

        "test 6: retracted": function() {
            L.fire("viewport:scrolled", {
                viewport: {
                    nearView: function(node) {
                        return node.dataset.sid === 'pubmed-6';
                    }
                }
            });
            var links = document.querySelectorAll("li[data-doi='10.6'] a"),
                lastLink = links[links.length - 1];
            Y.Assert.areEqual("retractionNoticeUrl", lastLink.getAttribute("href"))
            Y.Assert.areEqual(true, lastLink.isTrackableAsEvent)
        }


        
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(browzineTestCase);
    Y.Test.Runner.masterSuite.name = "browzine-test.js";
    Y.Test.Runner.run();

});
