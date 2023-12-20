YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let descriptionToggleTestCase = new Y.Test.Case({

        name: "description toggle Test Case",

        testTriggerContentPresent: function() {
            let triggers = Y.all(".descriptionTrigger");
            Y.Assert.areEqual("Abstract", triggers.item(0).get("text"));
            Y.Assert.areEqual("Read Full Description", triggers.item(1).get("text"));
            Y.Assert.areEqual(2, Y.all(".descriptionTrigger").size());
        },

        testToggleDescriptionOn: function() {
            let triggers = Y.all(".descriptionTrigger");
            let items = Y.all("#searchResults li");
            triggers.each(function(node) {
                node.simulate("click");
            });
            items.each(function(node) {
                Y.Assert.isTrue(node.hasClass("active"));
            });
            triggers.each(function(node, index) {
                if (index === 0) {
                    Y.Assert.areEqual("Abstract ", node.get("text"));
                } else {
                    Y.Assert.areEqual(" Read Full Description ", node.get("text"));
                }
            });
        },

        testToggleDescriptionOff: function() {
            let triggers = Y.all(".descriptionTrigger");
            let items = Y.all("#searchResults li");
            triggers.each(function(node) {
                node.simulate("click");
            });
            items.each(function(node) {
                Y.Assert.isFalse(node.hasClass("active"));
            });
            triggers.each(function(node, index) {
                if (index === 0) {
                    Y.Assert.areEqual("Abstract ", node.get("text"));
                } else {
                    Y.Assert.areEqual(" Read Full Description ", node.get("text"));
                }
            });
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(descriptionToggleTestCase);
    Y.Test.Runner.masterSuite.name = "description-toggle-test.js";
    Y.Test.Runner.run();

});