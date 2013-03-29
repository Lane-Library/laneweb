/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('dump', 'lane-tracking', 'node-event-simulate', 'console', 'test', function(Y){

    var trackingTestCase = new Y.Test.Case({
        name: "Lane Tracking TestCase",
        trackingData: {},
        track: function(td) {
            this.trackingData = td;
        },
        setUp: function() {
            this.trackingData = {};
        },
        testExists: function() {
            Y.Assert.isObject(LANE.tracking);
        }//,
//        testIsTrackable: function() {
//            var i;
//            var images = Y.one('#testIsTrackable').all('img');
//            var anchors = Y.one('#testIsTrackable').all('a');
//            var failedNodes = [];
//            var trackable = function(event) {
//                event.halt();
//                if (!LANE.tracking.isTrackable(event)) {
//                    failedNodes.push(event.target);
//                }
//            };
//            var notTrackable = function(event) {
//                event.halt();
//                if (LANE.tracking.isTrackable(event)) {
//                    failedNodes.push(event.target);
//                }
//            };
//            for (i = 0; i < anchors.size(); i++) {
//                if (anchors.item(i).get('rel') == 'trackable') {
//                    anchors.item(i).on('click', trackable);
//                    anchors.item(i).simulate('click');
//                    anchors.item(i).detach(trackable);
//                } else {
//                    anchors.item(i).on('click', notTrackable);
//                    anchors.item(i).simulate('click');
//                    anchors.item(i).detach(notTrackable);
//                }
//            }
//            for (i = 0; i < images.size; i++) {
//                if (images.item(i).get('parentNode').get('rel') == 'trackable') {
//                    images.item(i).on('click', trackable);
//                    images.item(i).simulate('click');
//                    images.item(i).detach(trackable);
//                } else {
//                    images.item(i).on('click', notTrackable);
//                    images.item(i).simulate('click');
//                    images.item(i).detach(notTrackable);
//                }
//            }
//            Y.Assert.isTrue(0 === failedNodes.length, Y.dump(failedNodes));
//        },
//        testGetTrackeTitle: function() {
//            var i, anchors = Y.one('#testGetTrackedTitle').all('a');
//            var prevent = function(event) {
//                event.preventDefault();
//            };
//            for (i = 0; i < anchors.size(); i++) {
//                anchors.item(i).on('click', prevent);
//                anchors.item(i).simulate('click');
//                Y.Assert.areEqual(anchors.item(i).get('rel'), this.trackingData.title);
//                anchors.detach(prevent);
//            }
//        },
//        testTrack: function() {
//            LANE.tracking.track({
//                host: 'www.google.com',
//                path: '/path',
//                title: 'title'
//            });
//            Y.Assert.areEqual('www.google.com', this.trackingData.host);
//            Y.Assert.areEqual('/path', this.trackingData.path);
//            Y.Assert.areEqual('title', this.trackingData.title);
//        },
//        testExpandyIsTrackable: function() {
//            var expandy = Y.one('.yui3-accordion-item-trigger');
//            var event;
//            var handler = function(e) {
//                event = e;
//            };
//            expandy.on('click', handler);
//            expandy.simulate('click');
//            Y.Assert.isTrue(LANE.tracking.isTrackable(event), 'event not trackable');
//            expandy.detach(handler);
//        },
//        testTrackExpandy: function() {
//            var expandy = Y.one('.yui3-accordion-item-trigger');
////            var event;
////            var handler = function(e) {
////                event = e;
////            };
////            expandy.on('click', handler);
//            expandy.simulate('click');
//            Y.Assert.areEqual('Expandy:' + expandy.get('textContent'), this.trackingData.title, 'actual:' + this.trackingData.title);
//            Y.Assert.isFalse(this.trackingData !== undefined && this.trackingData.external);
////            expandy.detach(handler);
//        },
//        testTrackCookiesFetch: function() {
//            var cookiesFetch = Y.one("#testTrackCookiesFetch");
//            cookiesFetch.on('click', function(e) {e.preventDefault();});
//            cookiesFetch.simulate('click');
//            Y.Assert.areEqual("www.thomsonhc.com", this.trackingData.host);
//            Y.Assert.areEqual("/carenotes/librarian/", this.trackingData.path);
//            Y.Assert.areEqual("cookiesFetch", this.trackingData.title);
//        },
//        testTrackingDisabledEnabled : function() {
//        	var node = Y.all("a").item(1);
//            var event;
//            var handler = function(e) {
//                event = e;
//            };
//            node.on('click', handler);
//            LANE.tracking.disableTracking();
//            node.simulate('click');
//            Y.Assert.isFalse(LANE.tracking.isTrackable(event), 'event is trackable');
//            LANE.tracking.enableTracking();
//            node.simulate('click');
//            Y.Assert.isTrue(LANE.tracking.isTrackable(event), 'event is not trackable');
//        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(trackingTestCase);
    Y.Test.Runner.run();
});
