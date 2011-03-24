/**
 * @author ceyates
 */

YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(T){

    var feedbackTestCase = new T.Test.Case({
        name: 'Lane Feedback Test Case'
    });
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    
    T.Test.Runner.add(feedbackTestCase);
    T.Test.Runner.run();
});
