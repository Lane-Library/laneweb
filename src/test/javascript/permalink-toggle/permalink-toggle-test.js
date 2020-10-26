YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    var permalinkToggleTestCase = new Y.Test.Case({

        name: "permalink toggle Test Case",

        testTogglePermalinkToggle: function() {
            var permaSpans = Y.all(".permalink");
            var links = Y.all(".permalink a");
            var items = Y.all("#searchResults li");
            Y.Assert.areEqual(2, links.size());
            Y.Assert.areEqual("Permalink", links.item(0).get("text").trim());
            Y.Assert.areEqual("Permalink", links.item(1).get("text").trim());
            links.each(function(node) {
                node.simulate("click");
            });
            permaSpans.each(function(node) {
                Y.Assert.isTrue(node.hasClass("active"));
            });
            items.each(function(node) {
                Y.Assert.isObject(node.one('.expandedPermalink'));
            });
            links.each(function(node) {
                node.simulate("click");
            });
            permaSpans.each(function(node) {
                Y.Assert.isFalse(node.hasClass("active"));
            });
            items.each(function(node) {
                Y.Assert.isNull(node.one('.expandedPermalink'));
            });
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(permalinkToggleTestCase);
    Y.Test.Runner.masterSuite.name = "permalink-toggle-test.js";
    Y.Test.Runner.run();

});