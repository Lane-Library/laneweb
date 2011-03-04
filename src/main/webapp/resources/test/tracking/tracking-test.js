/**
 * @author ceyates
 */
YUI({
    filter : "debug",
    logInclude: {
        TestRunner: true
    }
}).use('dump', 'node-event-simulate', 'console', 'test', function(T){
	
	//TODO: this ends up testing the LinkPlugin more than anything, move those tests to that test case.

    var trackingTestCase = new T.Test.Case({
        name: "Lane Tracking TestCase",
        trackingData: undefined,
        link : undefined,
        track: function(link, event) {
        	event.preventDefault();
            this.trackingData = link.get("trackingData");
            this.link = link;
        },
        setUp: function() {
            this.trackingData = undefined;
            this.link = undefined;
        },
        testIsTrackable: function() {
            var i;
            var images = T.one('#testIsTrackable').all('img');
            var anchors = T.one('#testIsTrackable').all('a');
            var failedNodes = [];
            var successfulNodes = [];
            var totalClicks = 0;
            for (i = 0; i < anchors.size(); i++) {
                if (anchors.item(i).get('rel') == 'trackable') {
                    anchors.item(i).simulate('click');
                    if (this.link.get("trackable")) {
                    	successfulNodes.push(anchors.item(i));
                    } else {
                    	failedNodes.push(anchors.item(i));
                    }
                    totalClicks++;
                } else {
                    anchors.item(i).simulate('click');
                    if (!this.link.get("trackable")) {
                    	successfulNodes.push(anchors.item(i));
                    } else {
                    	failedNodes.push(anchors.item(i));
                    }
                    totalClicks++;
                }
            }
            for (i = 0; i < images.size(); i++) {
                if (images.item(i).get('parentNode').get('rel') == 'trackable') {
                    images.item(i).simulate('click');
                    if (this.link.get("trackable")) {
                    	successfulNodes.push(anchors.item(i));
                    } else {
                    	failedNodes.push(anchors.item(i));
                    }
                    totalClicks++;
                } else {
                    images.item(i).simulate('click');
                    if (!this.link.get("trackable")) {
                    	successfulNodes.push(anchors.item(i));
                    } else {
                    	failedNodes.push(anchors.item(i));
                    }
                    totalClicks++;
                }
            }
            T.Assert.areEqual(0, failedNodes.length, "nodes with the wrong isTrackble status");
            T.Assert.areEqual(totalClicks, successfulNodes.length);
        },
        testGetTrackingTitle: function() {
            var i, anchors = T.one('#testGetTrackedTitle').all('a');
            for (i = 0; i < anchors.size(); i++) {
                anchors.item(i).simulate('click');
                T.Assert.areEqual(anchors.item(i).get('rel'), this.trackingData.title);
            }
        },
        testExpandyIsTrackable: function() {
            var expandy = T.one('.yui3-accordion-item-trigger');
            var event;
            expandy.setAttribute("trackable", true);
            expandy.simulate('click');
            T.Assert.isTrue(this.link.get("trackable"), 'event not trackable');
        },
        testTrackExpandy: function() {
            var expandy = T.one('.yui3-accordion-item-trigger');
            var event;
            var handler = function(e) {
                event = e;
            };
            expandy.on('click', handler);
            expandy.simulate('click');
            T.Assert.areEqual('Expandy:' + expandy.get('textContent'), this.trackingData.title, 'actual:' + this.trackingData.title);
            T.Assert.isFalse(this.trackingData !== undefined && this.trackingData.external);
            expandy.detach(handler);
        },
        testTrackCookiesFetch: function() {
            var cookiesFetch = T.one("#testTrackCookiesFetch");
            cookiesFetch.simulate('click');
            T.Assert.areEqual("www.thomsonhc.com", this.trackingData.host);
            T.Assert.areEqual("/carenotes/librarian/", this.trackingData.path);
            T.Assert.areEqual("cookiesFetch", this.trackingData.title);
        }
    });
    
    Y.on("trackable", function(link, event) {
    	trackingTestCase.track(link, event);
    });
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    
    T.Test.Runner.add(trackingTestCase);
    T.Test.Runner.run();
});
