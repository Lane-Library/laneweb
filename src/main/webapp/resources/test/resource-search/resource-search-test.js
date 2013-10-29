/**
 * @author ceyates
 */

Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', function(Y){

    var resourceSearchTestCase = new Y.Test.Case({
        name: 'Lane ResourceSearch Test Case'
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(resourceSearchTestCase);
    Y.Test.Runner.masterSuite.name = "resource-search-test.js";
    Y.Test.Runner.run();
});
