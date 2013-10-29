/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    var spellCheckTestCase = new Y.Test.Case({
        name: 'Lane Spellcheck Test Case'
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(spellCheckTestCase);
    Y.Test.Runner.masterSuite.name = "spellcheck-test.js";
    Y.Test.Runner.run();
});
