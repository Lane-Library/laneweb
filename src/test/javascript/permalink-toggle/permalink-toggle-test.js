YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let permalinkToggleTestCase = new Y.Test.Case({

        name: "permalink toggle Test Case",

        testTogglePermalinks: function() {
            let permaSpans = Y.all(".permalink"),
                links = Y.all(".permalink a");
            Y.Assert.areEqual(2, links.size());
            links.each(function(node) {
                Y.Assert.areEqual("Get a shareable link", node.get("text").trim());
                node.simulate("click");
            });
            permaSpans.each(function(node) {
                Y.Assert.areEqual("Link copied", node.get("text").trim());
            });
            this.wait(function(){
                permaSpans.each(function(node) {
                    Y.Assert.areEqual("Get a shareable link", node.get("text").trim());
                });
            }, 2200);
            
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(permalinkToggleTestCase);
    Y.Test.Runner.masterSuite.name = "permalink-toggle-test.js";
    Y.Test.Runner.run();

});