/**
 * @author ceyates
 */
(function() {
    var suggestTestCase = new YAHOO.tool.TestCase({
        name: "Lane Suggest Testcase",
        testAddACElements: function() {
            var searchTermsElm = document.getElementById('searchTerms');
            YAHOO.util.Assert.areEqual('acContainer yui-ac', searchTermsElm.parentNode.className);
        }
    });
    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(suggestTestCase);
    YAHOO.util.Event.addListener(window, 'load', function() {
        YAHOO.tool.TestRunner.run();
    });
})();
