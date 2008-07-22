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
        var i, t = LANE.track,
        a = document.getElementsByTagName('a');
        for (i = 0; i < a.length; i++) {
            if (a[i].rel == 'trackable') {
                Assert.isTrue(t.isTrackable(a[i]));
            } else {
                Assert.isFalse(t.isTrackable(a[i]));
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
            track: function(node){
                tracked = node.href + '?bar';
            }
        });
    },
    testTrack: function() {
        var node = {href:'http://www.google.com/'},
            t = '';
        LANE.track.addTracker({
            track: function(node){
                t = node.href + '?foo';
            }
        });
        LANE.track.track(node);
        Assert.areEqual(tracked,'http://www.google.com/?bar');
        Assert.areEqual(t,'http://www.google.com/?foo');
    }
});

var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANETrackingTestCase);
TestRunner.run();
})();
