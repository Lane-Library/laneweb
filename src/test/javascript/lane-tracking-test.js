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
	trackingData:{},
	track: function(td) {
		trackingData = td;
	},
	setup: function() {
		trackingData = {};
	},
    testExists: function(){
        Assert.isObject(LANE.track);
    },
    testAddTracker: function() {
        try {
            LANE.tracking.addTracker({});
        } catch (ex) {
            Assert.areEqual(ex, 'tracker does not implement track()');
        }
        LANE.tracking.addTracker(this);
    },
	testGetTrackingTitle: function() {
		var i, event = {type:'click'},
		    a = document.getElementById('testGetTrackedTitle').getElementsByTagName('a'),
			t = LANE.tracking;
		for (i = 0; i < a.length; i++) {
			event.target = a[i];
			LANE.tracking.trackEvent(event);
			Assert.areEqual(a[i].rel, trackingData.title);
		}
	},
    testIsTrackable: function() {
        var i, m, t = LANE.tracking,
        a = document.getElementById('testIsTrackable').getElementsByTagName('a'),
        e = {type:'click'};
        for (i = 0; i < a.length; i++) {
            e.target = a[i];
            if (a[i].rel == 'trackable') {
                Assert.isTrue(t.isTrackable(e));
            } else {
                Assert.isFalse(t.isTrackable(e));
            }
        }
        m = document.getElementById('testIsTrackable').getElementsByTagName('img');
        for (i = 0; i < m.length; i++) {
            e.target = m[i];
            if (m[i].parentNode.rel == 'trackable') {
                Assert.isTrue(t.isTrackable(e));
            } else {
                Assert.isFalse(t.isTrackable(e));
            }
        }
    },
    testTrack: function() {
        var t = '';
        LANE.tracking.addTracker({
            track: function(td){
                t = td.host + td.path + (td.query || ' ') + td.title;
            }
        });
        LANE.tracking.track({host:'www.google.com',path:'/path',title:'title'});
		Assert.areEqual('www.google.com', trackingData.host);
		Assert.areEqual('/path', trackingData.path);
		Assert.areEqual('title', trackingData.title);
    }
});

var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANETrackingTestCase);
TestRunner.run();
})();
