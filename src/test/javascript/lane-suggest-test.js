/**
 * @author ceyates
 */
(function() {
    var suggestTestCase = new YAHOO.tool.TestCase({
        name:"Lane Suggest Testcase",
        testAddACElements:function(){
            var searchTermsElm = document.getElementById('searchTerms');
            YAHOO.util.Assert.areEqual(searchTermsElm.parentNode.className, 'acContainer yui-ac');
        }
    });
    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(suggestTestCase);
    YAHOO.tool.TestRunner.run();
})();