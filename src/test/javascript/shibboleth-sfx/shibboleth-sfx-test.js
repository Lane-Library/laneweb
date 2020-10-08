/**
 * @author ryanmax
 */
YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    var shibbolethSfxTestCase = new Y.Test.Case({

        name: "Lane Shibboleth SFX Testcase",

        testFramedSHC: function() {
            if (window.self !== window.top) {
                var shc = Y.one("a[title='SSO SHC']");
                var currentTopHref = window.top.location.href;
                window.onbeforeunload = function(e) {
                    e.preventDefault();
                    Y.Assert.isTrue(e.target.location.href != currentTopHref);
                }
                var clickHandle = shc.on("click", function(event) {
                    event.preventDefault();
                });
                shc.simulate("click");
                clickHandle.detach();
            }
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(shibbolethSfxTestCase);
    Y.Test.Runner.masterSuite.name = "shibboleth-sfx-test.js";
    Y.Test.Runner.run();

});
