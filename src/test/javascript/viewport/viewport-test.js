YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    L.on("viewport:init", function(event) {

        let viewportTestCase = new Y.Test.Case({

            name: "Lane Viewport Testcase",

            viewport: event.viewport,

            "test viewport exists": function() {
                Y.Assert.isObject(this.viewport);
            },

            "test in viewport": function() {
                Y.Assert.isTrue(this.viewport.inView(document.querySelector("#top")));
                Y.Assert.isTrue(this.viewport.nearView(document.querySelector("#bottom"),10));
            },

            "test not in viewport": function() {
                Y.Assert.isFalse(this.viewport.inView(document.querySelector("#bottom")));
            },

            "test scroll": function() {
                window.scrollTo(0, 1000);
                Y.Assert.isTrue(this.viewport.inView(document.querySelector("#bottom")));
            },

            "test scroll event": function() {
                let topInView = this.viewport.inView(document.querySelector("#top"));
                Y.Assert.isFalse(topInView);
                L.once("viewport:scrolled", function(event) {
                    topInView = event.viewport.inView(document.querySelector("#top"));
                });
                window.scrollTo(0, 0);
                this.wait(function() {
                    Y.Assert.isTrue(topInView);
                }, 1200);
            }
        });

        new Y.Test.Console().render();

        Y.Test.Runner.add(viewportTestCase);
        Y.Test.Runner.masterSuite.name = "viewport-test.js";
        Y.Test.Runner.run();

    });

});
