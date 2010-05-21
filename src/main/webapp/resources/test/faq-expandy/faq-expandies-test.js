/**
 * @author ceyates
 */
YUI({
     filter:"debug",
     gallery: 'gallery-2010.04.02-17-26'
}).use("lane", 'gallery-node-accordion', 'node', 'node-event-simulate', 'console', 'test', function(Y){

    var faqExpandiesTestCase = new Y.Test.Case({
        name: 'FAQ Expandies TestCase'
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(faqExpandiesTestCase);
    Y.Test.Runner.run();
});