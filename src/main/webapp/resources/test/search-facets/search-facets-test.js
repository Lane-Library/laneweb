/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var searchFacetsTestCase = new Y.Test.Case({
        name: 'Lane Search Facets Test Case' 
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    Y.Test.Runner.add(searchFacetsTestCase);
    Y.Test.Runner.run();
});
