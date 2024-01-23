YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let ieTestCase = new Y.Test.Case({

        name: 'Lane IE Test Case',

        testBookmarkletNotIEDisplayNone: function() {
            Y.Assert.areEqual("none", document.querySelector("#bookmarkletNotIE").style.display);
        },

        testBookmarkletIEDisplayBlock: function() {
            Y.Assert.areEqual("block", document.querySelector("#bookmarkletIE").style.display);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(ieTestCase);
    Y.Test.Runner.masterSuite.name = "ie-test.js";
    Y.Test.Runner.run();

});
