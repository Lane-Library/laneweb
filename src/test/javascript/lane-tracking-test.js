/**
 * @author ceyates
 */
(function() {
var Assert = YAHOO.util.Assert;
var TestRunner = YAHOO.tool.TestRunner;
var TestCase = YAHOO.tool.TestCase;
var UserAction = YAHOO.util.UserAction;

var LANETrackingTestCase = new TestCase({
    name: "Lane Tracking TestCase",
    tracked:'',
    testExists: function(){
        Assert.isObject(LANE.track);
    },
    testIsTrackable: function() {
        var i, m, t = LANE.track,
        a = document.getElementsByTagName('a');
        for (i = 0; i < a.length; i++) {
            if (a[i].rel == 'trackable') {
                Assert.isTrue(t.isTrackable(a[i]));
            } else {
                Assert.isFalse(t.isTrackable(a[i]));
            }
        }
        m = document.getElementsByTagName('img');
        for (i = 0; i < m.length; i++) {
            if (m[i].parentNode.rel == 'trackable') {
                Assert.isTrue(t.isTrackable(m[i]));
            } else {
                Assert.isFalse(t.isTrackable(m[i]));
            }
        }
    },
    testAddTracker: function() {
        try {
            LANE.track.addTracker({});
        } catch (ex) {
            Assert.areEqual(ex, 'tracker does not implement track()');
        }
        LANE.track.addTracker({
            track: function(trackable){
                tracked = 'http://' + trackable.host + trackable.uri + '?bar&title=' + trackable.title;
            }
        });
    },
    testTrack: function() {
        var t = '';
        LANE.track.addTracker({
            track: function(trackable){
                t = 'http://' + trackable.host + trackable.uri + '?foo&title=' + trackable.title;
            }
        });
        LANE.track.track(new LANE.track.Trackable());
        Assert.areEqual('http://www.google.com/foo/bar.html?bar&title=The Title', tracked);
        Assert.areEqual('http://www.google.com/foo/bar.html?foo&title=The Title',t);
    }
});

var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANETrackingTestCase);
TestRunner.run();
})();
