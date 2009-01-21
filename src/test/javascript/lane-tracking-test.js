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
        Assert.isObject(LANE.tracking);
    },
    testIsTrackable: function() {
        var i, m, t = LANE.tracking,
        a = document.getElementsByTagName('a'),
        e = {type:'click'};
        for (i = 0; i < a.length; i++) {
            e.target = a[i];
            if (a[i].rel == 'trackable') {
                Assert.isTrue(t.isTrackable(e));
            } else {
                Assert.isFalse(t.isTrackable(e));
            }
        }
        m = document.getElementsByTagName('img');
        for (i = 0; i < m.length; i++) {
            e.target = m[i];
            if (m[i].parentNode.rel == 'trackable') {
                Assert.isTrue(t.isTrackable(e));
            } else {
                Assert.isFalse(t.isTrackable(e));
            }
        }
    },
    testAddTracker: function() {
        try {
            LANE.tracking.addTracker({});
        } catch (ex) {
            Assert.areEqual(ex, 'tracker does not implement track()');
        }
        LANE.tracking.addTracker({
            track: function(td){
                LANETrackingTestCase.tracked = td.host + td.path + (td.query || ' ') + td.title;
            }
        });
    },
    testTrack: function() {
        var t = '';
        LANE.tracking.addTracker({
            track: function(td){
                t = td.host + td.path + (td.query || ' ') + td.title;
            }
        });
        LANE.tracking.track({host:'www.google.com',path:'/trackable',title:'external'});
        Assert.areEqual('www.google.com/trackable external', LANETrackingTestCase.tracked);
        Assert.areEqual('www.google.com/trackable external',t);
    }
});

var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANETrackingTestCase);
TestRunner.run();
})();
