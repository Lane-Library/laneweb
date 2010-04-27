/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var suggestTestCase = new Y.Test.Case({
        name: "Lane Suggest Testcase",
        testAddACElements: function() {
            var searchTermsElm = document.getElementById('searchTerms');
            YAHOO.util.Assert.areEqual('acContainer yui-ac', searchTermsElm.parentNode.className);
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(suggestTestCase);
    Y.Test.Runner.run();
});