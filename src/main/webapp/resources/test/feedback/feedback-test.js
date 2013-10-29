/**
 * @author ceyates
 */

Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    var feedbackTestCase = new Y.Test.Case({
        name: 'Lane Feedback Test Case'
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(feedbackTestCase);
    Y.Test.Runner.masterSuite.name = "feedback-test.js";
    Y.Test.Runner.run();
});
