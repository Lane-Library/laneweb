/**
 * @author ryanmax
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    var shibbolethSfxTestCase = new Y.Test.Case({
        name: "Lane Shibboleth SFX Testcase",
            testFramedSHC: function() {
                if (window.self !== window.top) {
                    var shc = Y.one("#SHC");
                    var currentTopHref = Y.lane.TopLocation.get("href");
                    var newHref = "";
                    var hrefChangeHandle = Y.lane.TopLocation.on("hrefChange", function(event) {
                        event.preventDefault();
                        newHref = event.newVal;
                    });
                    var clickHandle = shc.on("click", function(event) {
                        event.preventDefault();
                    });
                    shc.simulate("click");
                    Y.Assert.isTrue(newHref != currentTopHref);
                    Y.Assert.isTrue(newHref.indexOf("adfs.stanfordmed.org") > -1);
                    hrefChangeHandle.detach();
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
});