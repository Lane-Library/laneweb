/**
 * @author ryanmax
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    var shibbolethSfxTestCase = new Y.Test.Case({
        name: "Lane Shibboleth SFX Testcase"
    });

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(shibbolethSfxTestCase);
    Y.Test.Runner.masterSuite.name = "shibboleth-sfx-test.js";
    Y.Test.Runner.run();
});