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
            track: function(node){
                while (!node.href) node = node.parentNode;
                tracked = node.href + '?bar';
            }
        });
    },
    testTrack: function() {
        var node = {
            parentNode: {
                href: 'http://www.google.com/',
                nodeName: 'A'
            },
            nodeName: 'IMG'
        }, t = '';
        LANE.track.addTracker({
            track: function(node){
                while (!node.href) node = node.parentNode;
                t = node.href + '?foo';
            }
        });
        LANE.track.track(node);
        Assert.areEqual('http://www.google.com/?bar', tracked);
        Assert.areEqual('http://www.google.com/?foo',t);
    }
});

var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANETrackingTestCase);
TestRunner.run();
})();
