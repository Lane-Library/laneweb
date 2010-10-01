/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var searchOptionsTestCase = new Y.Test.Case({
        name: 'Lane Search Options Test Case' 
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    Y.Test.Runner.add(searchOptionsTestCase);
    Y.Test.Runner.run();
});
