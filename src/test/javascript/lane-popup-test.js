/**
 * @author ceyates
 */
(function() {

    var lanePopupTestCase = new YAHOO.tool.TestCase({
        name: 'Lane Popup Test Case',
        testFoo: function() {
            YAHOO.util.Assert.isTrue(true);
        }
    });
    YAHOO.tool.TestRunner.add(lanePopupTestCase);
    new YAHOO.tool.TestLogger();
    YAHOO.util.Event.addListener(this, 'load', function() {
        YAHOO.tool.TestRunner.run();
    });
})();
