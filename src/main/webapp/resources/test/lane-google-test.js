/**
 * @author ceyates
 */
//this really isn't a unit test, but relys on setting pageTracker._setLocalServerMode();
//in the lane-google file so you can observer the requests
//by tailing the log.
    //need to wait for ga.js to load
//    var timer = function() {
//        if (typeof _gat === 'undefined') {
//            setTimeout(timer, 1000);
//        } else {
//            YAHOO.tool.TestRunner.run();
//        }
//    };
//    timer();
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var googleTestCase = new Y.Test.Case({
        name: 'Lane Google Test Case',
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
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(googleTestCase);
    Y.Test.Runner.run();
});