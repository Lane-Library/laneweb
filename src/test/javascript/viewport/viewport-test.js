"use strict";

Y.lane.on("viewport:init", function(event) {

    var viewportTestCase = new Y.Test.Case({

        name: "Lane Viewport Testcase",

        viewport: event.viewport,

        "test viewport exists": function() {
            Y.Assert.isObject(this.viewport);
        },

        "test in viewport": function() {
            Y.Assert.isTrue(this.viewport.inView(document.querySelector("#top")));
        },

        "test not in viewport": function() {
            Y.Assert.isFalse(this.viewport.inView(document.querySelector("#bottom")));
        },

        "test scroll": function() {
            var a = new Y.Anim({
                node: "win",
                to: { scroll: [1000, 1000] },
                duration: 0.1,
                easing: Y.Easing.easeBoth
            });
            a.run();
            this.wait(function() {
                Y.Assert.isTrue(this.viewport.inView(document.querySelector("#bottom")));
            }, 200);
        },

        "test scroll event": function() {
            var topInView = this.viewport.inView(document.querySelector("#top"));
            Y.Assert.isFalse(topInView);
            Y.lane.once("viewport:scrolled", function(event) {
                topInView = event.viewport.inView(document.querySelector("#top"));
            });
            var a = new Y.Anim({
                node: "win",
                to: { scroll: [-1000, -1000] },
                duration: 0.1,
                easing: Y.Easing.easeBoth
            });
            a.run();
            this.wait(function() {
                Y.Assert.isTrue(topInView);
            }, 1200);
        }
    });

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(viewportTestCase);
    Y.Test.Runner.masterSuite.name = "viewport-test.js";
    Y.Test.Runner.run();
    
});
