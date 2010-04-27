/**
 * @author ceyates
 */

YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var searchHistoryTestCase = new Y.Test.Case({
        name: 'Lane Search History Test Case'
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(searchHistoryTestCase);
    Y.Test.Runner.run();
});
