/**
 * @author ceyates
 */
//this really isn't a unit test, but relys on setting pageTracker._setLocalServerMode();
//in the lane-google file so you can observer the requests
//by tailing the log.
(function() {

    var LANEWebtrendsTestCase = YAHOO.tool.TestCase({
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
    YAHOO.tool.TestRunner.add(LANEWebtrendsTestCase);
    //need to wait for ga.js to load
    var timer = function() {
        if (typeof _gat === 'undefined') {
            setTimeout(timer, 1000);
        } else {
            YAHOO.tool.TestRunner.run();
        }
    };
    timer();
})();
