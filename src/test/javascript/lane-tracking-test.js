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
            track: function(td){
                tracked = td.host + td.path + (td.query || '') + td.title;
            }
        });
    },
    testTrack: function() {
        var t = '';
        LANE.track.addTracker({
            track: function(td){
                t = td.host + td.path + (td.query || '') + td.title;
            }
        });
        LANE.track.track(document.getElementById('track'));
        Assert.areEqual('www.google.com/The Title', tracked);
        Assert.areEqual('www.google.com/The Title',t);
    }
});

var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANETrackingTestCase);
TestRunner.run();
})();
