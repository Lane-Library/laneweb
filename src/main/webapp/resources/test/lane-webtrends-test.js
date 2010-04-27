/**
 * @author ceyates
 */
//this really isn't a unit test, but relys on setting the gDomain variable
//in the webtrends file to a local server so you can observer the requests
//by tailing the log.
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var webtrendsTestCase = new Y.Test.Case({
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
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(webtrendsTestCase);
    Y.Test.Runner.run();
});