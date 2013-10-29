/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    var ieTestCase = new Y.Test.Case({
        name: 'Lane IE Test Case'
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(ieTestCase);
    Y.Test.Runner.masterSuite.name = "ie-test.js";
    Y.Test.Runner.run();
});
