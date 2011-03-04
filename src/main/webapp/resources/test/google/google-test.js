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
//            T.HOO.tool.TestRunner.run();
//        }
//    };
//    timer();
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(T) {

    var googleTestCase = new T.Test.Case({
        name: 'Lane Google Test Case',
        testTrack: function() {
            var handler = function(e) {
                e.preventDefault();
            };
            var nodes = T.all('img, a'), i;
            for (i = 0; i < nodes.size(); i++) {
                nodes.item(i).on('click', handler);
                nodes.item(i).simulate('click');
                nodes.item(i).detach(handler);
            }
        }
    });
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    
    T.Test.Runner.add(googleTestCase);
    T.Test.Runner.run();
});