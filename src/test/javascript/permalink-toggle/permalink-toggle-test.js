YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    var permalinkToggleTestCase = new Y.Test.Case({

        name: "permalink toggle Test Case",

        testTogglePermalinks: function() {
            var permaSpans = Y.all(".permalink"),
                links = Y.all(".permalink a"),
                items = Y.all("#searchResults li");
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
        },
        testCopyPermalinks: function() {
            Y.one(".permalink a").simulate('click');
            var expandedPermalinkDiv = Y.one('.expandedPermalink'),
                expandedPermalink = expandedPermalinkDiv.one('a');
            Y.Assert.isObject(expandedPermalinkDiv);
            Y.Assert.areEqual("Copy permanent link to clipboard", expandedPermalink.get("text").trim());
            expandedPermalink.simulate("click");
            Y.Assert.areEqual("Permanent link copied", expandedPermalinkDiv.all('div').item(1).get("text").trim());
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(permalinkToggleTestCase);
    Y.Test.Runner.masterSuite.name = "permalink-toggle-test.js";
    Y.Test.Runner.run();

});