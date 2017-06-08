/**
 * @author ryanmax
 */
"use strict";

var shibbolethSfxTestCase = new Y.Test.Case({
    name: "Lane Shibboleth SFX Testcase",
    testFramedSHC: function() {
        if (window.self !== window.top) {
            var shc = Y.one("#SHC");
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

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(shibbolethSfxTestCase);
Y.Test.Runner.masterSuite.name = "shibboleth-sfx-test.js";
Y.Test.Runner.run();
