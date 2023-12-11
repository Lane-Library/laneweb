YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let laneYouTubeTestCase = new Y.Test.Case({

        name: 'Lane YouTube Test Case',

        testNoIframes: function() {
            Y.Assert.areEqual(0, document.querySelectorAll("iframe").length);
            Y.Assert.areEqual(2, document.querySelectorAll("img").length);
        },

        testImgClick : function() {
            Y.one("img").simulate("click");
            Y.Assert.areEqual(1, document.querySelectorAll("iframe").length);
            Y.Assert.areEqual(1, document.querySelectorAll("img").length);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(laneYouTubeTestCase);
    Y.Test.Runner.masterSuite.name = "youtube-test.js";
    Y.Test.Runner.run();

});
