/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var trackingTestCase = new Y.Test.Case({
        name: "Lane Tracking TestCase",
        trackingData: {},
        track: function(td) {
            trackingData = td;
        },
        setup: function() {
            trackingData = {};
        },
        testExists: function() {
            YAHOO.util.Assert.isObject(LANE.tracking);
        },
        testAddTracker: function() {
            try {
                LANE.tracking.addTracker({});
            } catch (ex) {
                YAHOO.util.Assert.areEqual(ex, 'tracker does not implement track()');
            }
            LANE.tracking.addTracker(this);
        },
        testGetTrackingTitle: function() {
            var i, event = {
                type: 'click'
            }, anchors = document.getElementById('testGetTrackedTitle').getElementsByTagName('a');
            for (i = 0; i < anchors.length; i++) {
                event.target = anchors[i];
                LANE.tracking.trackEvent(event);
                YAHOO.util.Assert.areEqual(anchors[i].rel, trackingData.title);
            }
        },
        testIsTrackable: function() {
            var i, images = document.getElementById('testIsTrackable').getElementsByTagName('img'), anchors = document.getElementById('testIsTrackable').getElementsByTagName('a'), event = {
                type: 'click'
            };
            for (i = 0; i < anchors.length; i++) {
                event.target = anchors[i];
                if (anchors[i].rel == 'trackable') {
                    YAHOO.util.Assert.isTrue(LANE.tracking.isTrackable(event));
                } else {
                    YAHOO.util.Assert.isFalse(LANE.tracking.isTrackable(event));
                }
            }
            for (i = 0; i < images.length; i++) {
                event.target = images[i];
                if (images[i].parentNode.rel == 'trackable') {
                    YAHOO.util.Assert.isTrue(LANE.tracking.isTrackable(event));
                } else {
                    YAHOO.util.Assert.isFalse(LANE.tracking.isTrackable(event));
                }
            }
        },
        testTrack: function() {
            LANE.tracking.track({
                host: 'www.google.com',
                path: '/path',
                title: 'title'
            });
            YAHOO.util.Assert.areEqual('www.google.com', trackingData.host);
            YAHOO.util.Assert.areEqual('/path', trackingData.path);
            YAHOO.util.Assert.areEqual('title', trackingData.title);
        },
        testExpandyIsTrackable: function() {
            var expandy = document.getElementById("testTrackExpandy").children[0];
            YAHOO.util.Assert.isTrue(LANE.tracking.isTrackable({
                type: 'click',
                target: expandy.firstChild
            }), 'event not trackable');
        },
        testTrackExpandy: function() {
            var expandy = document.getElementById("testTrackExpandy").children[0];
            var event = {
                type: 'click',
                target: expandy.firstChild
            };
            LANE.tracking.trackEvent(event);
            YAHOO.util.Assert.areEqual('Expandy:' + expandy.firstChild.textContent, trackingData.title, 'actual:' + trackingData.title);
            YAHOO.util.Assert.isFalse(trackingData !== undefined && trackingData.external);
            
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(trackingTestCase);
    Y.Test.Runner.run();
});
