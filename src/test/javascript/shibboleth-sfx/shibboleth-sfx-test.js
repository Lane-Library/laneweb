/**
 * @author ryanmax
 */
YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let shibbolethSfxTestCase = new Y.Test.Case({

        name: "Lane Shibboleth SFX Testcase",

        testFramedSHC: function() {
            let shc = Y.one("a[title='SSO SHC']");
            if (window.self !== window.top) {
                Y.Assert.isTrue("_blank" == shc.getAttribute('target'));
            }
            else {
                Y.Assert.isFalse("_blank" == shc.getAttribute('target'));
            }
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(shibbolethSfxTestCase);
    Y.Test.Runner.masterSuite.name = "shibboleth-sfx-test.js";
    Y.Test.Runner.run();

});
