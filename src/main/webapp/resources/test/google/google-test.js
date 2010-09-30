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
}).use('lane-tracking','node-event-simulate', 'console', 'test', function(Y){

    var googleTestCase = new Y.Test.Case({
        name: 'Lane Google Test Case',
        testTrack: function() {
            var event;
            var handler = function(e) {
                e.preventDefault();
                event = e;
            };
            var nodes = Y.all('img, a'), i;
            for (i = 0; i < nodes.size(); i++) {
                nodes.item(i).on('click', handler);
                nodes.item(i).simulate('click');
                if (LANE.tracking.isTrackable(event)) {
                    LANE.tracking.trackEvent(event);
                }
                nodes.item(i).detach(handler);
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