/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var teletypeTestCase = new Y.Test.Case({
        name: 'Lane Teletype Test Case' 
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    Y.Test.Runner.add(teletypeTestCase);
    Y.Test.Runner.run();
});
