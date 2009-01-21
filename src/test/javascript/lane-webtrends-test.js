/**
 * @author ceyates
 */
//this really isn't a unit test, but relys on setting the gDomain variable
//in the webtrends file to a local server so you can observer the requests
//by tailing the log.
(function() {
var Assert = YAHOO.util.Assert;
var TestRunner = YAHOO.tool.TestRunner;
var TestCase = YAHOO.tool.TestCase;
var UserAction = YAHOO.util.UserAction;

var LANEWebtrendsTestCase = new TestCase({
    name: "Lane Webtrends TestCase",
    testTrack: function() {
        var e = document.body.getElementsByTagName('img'), i;
        for (i = 0; i < e.length; i++) {
            if (LANE.tracking.isTrackable(e[i])) {
                LANE.tracking.track(e[i]);
            }
        }
        e = document.body.getElementsByTagName('a');
        for (i = 0; i < e.length; i++) {
            if (LANE.tracking.isTrackable(e[i])) {
                LANE.tracking.track(e[i]);
            }
        }
    }
});

var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANEWebtrendsTestCase);
TestRunner.run();
})();
