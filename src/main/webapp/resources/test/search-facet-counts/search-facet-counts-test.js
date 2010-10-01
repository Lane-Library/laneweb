/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var searchFacetCountsTestCase = new Y.Test.Case({
        name: 'Lane search-facet-counts Test Case' 
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    Y.Test.Runner.add(searchFacetCountsTestCase);
    Y.Test.Runner.run();
});
