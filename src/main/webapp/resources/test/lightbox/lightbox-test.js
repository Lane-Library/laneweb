/**
 * @author ceyates
 */

Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    var lightboxTestCase = new Y.Test.Case({
        name: 'Lane Feedback Test Case'
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(lightboxTestCase);
    Y.Test.Runner.masterSuite.name = "lightbox-test.js";
    Y.Test.Runner.run();
});
