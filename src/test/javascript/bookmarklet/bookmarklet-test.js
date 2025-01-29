YUI({ fetchCSS: false }).use("test", "test-console", function (Y) {

    "use strict";

    let ieTestCase = new Y.Test.Case({

        name: 'Lane IE Test Case',

        testBookmarkletNotIEDisplayNone: function () {
            Y.Assert.areEqual("none", document.querySelector("#bookmarklet").style.display);
        },

        testBookmarkletEdgeDisplayBlock: function () {
            Y.Assert.areEqual("block", document.querySelector("#bookmarklet-edge").style.display);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(ieTestCase);
    Y.Test.Runner.masterSuite.name = "bookmarklet-test.js";
    Y.Test.Runner.run();

});
