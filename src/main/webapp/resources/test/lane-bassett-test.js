/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var bassettTestCase = new Y.Test.Case({
        name: 'Lane Basset Test Case'
    });
    
    var yconsole = new Y.Console({
        newestOnTop: false
    });
    yconsole.render('#log');
    
    
    Y.on('domready', function(){
        Y.Test.Runner.add(bassettTestCase);
        Y.Test.Runner.run();
    });
});
