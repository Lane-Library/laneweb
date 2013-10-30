/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('dump', 'node-event-simulate', "console", 'test', function(Y){
    
    Y.on("click", function(event) {
        event.preventDefault();
        event._event.preventDefault();
    });

    var trackingTestCase = new Y.Test.Case({
        
        name: "Lane Tracking TestCase",
        
        event: null,
        
        pageView: null,
        
        setUp: function() {
            this.event = null;
            Y.on("lane:trackableEvent", this.eventCallback, this);
            this.pageView = null;
            Y.on("lane:trackablePageview", this.pageViewCallback, this);
        },
        
        eventCallback: function(event) {
            this.event = event;
        },
        
        pageViewCallback: function(event) {
            this.pageView = event;
        },
        
        tearDown: function() {
            Y.detach("lane:trackableEvent", this);
            Y.detach("lane:trackablePageview", this);
        },
        
        testBodyClickDoesNothing : function() {
            Y.one("body").simulate("click");
            Y.Assert.isNull(this.event);
            Y.Assert.isNull(this.pageView);
        },
        
        testSearchFacetClick: function() {
            Y.one(".searchFacet a").simulate("click");
            Y.Assert.areEqual("search facet", this.pageView.title);
        },
        
        testExpandyTriggerClick: function() {
            var link = Y.one('.yui3-accordion-item-trigger');
            link.simulate('click');
            Y.Assert.areEqual("Expandy:head", this.pageView.title);
        },
        
        testFavoritesClick: function() {
            var link = Y.one("#favorites a");
            link.simulate("click");
            Y.Assert.areEqual("favorites", this.event.label);
            Y.Assert.areEqual("lane:bookmarkClick", this.event.category);
        },
        
        testBookmarksClick: function() {
            var link = Y.one("#bookmarks a");
            link.simulate("click");
            Y.Assert.areEqual("bookmarks", this.event.label);
            Y.Assert.areEqual("lane:bookmarkClick", this.event.category);
        },
        
        testBookmarksEditorClick: function() {
            var link = Y.one(".yui3-bookmark-editor-content a");
            link.simulate("click");
            Y.Assert.areEqual("bookmarks editor", this.event.label);
            Y.Assert.areEqual("lane:bookmarkClick", this.event.category);
        },
        
        testLaneNavClick: function() {
            var link = Y.one("#laneNav a");
            link.simulate("click");
            Y.Assert.areEqual(link.get("textContent"), this.event.label);
            Y.Assert.areEqual("lane:laneNav-top", this.event.category);
            Y.Assert.areEqual(link.get("href"), this.event.action);
        },
        
        testQLinkClick: function() {
            var link = Y.one("#qlinks a");
            link.simulate("click");
            Y.Assert.areEqual(link.get("textContent"), this.event.label);
            Y.Assert.areEqual("lane:quickLinkClick", this.event.category);
            Y.Assert.areEqual(link.get("href"), this.event.action);
        },
        
        testTopResourcesClick: function() {
            var link = Y.one("#topResources a");
            link.simulate("click");
            Y.Assert.areEqual(link.get("textContent"), this.event.label);
            Y.Assert.areEqual("lane:topResources", this.event.category);
            Y.Assert.areEqual(link.get("href"), this.event.action);
        },
        
        testBannerContentClick: function() {
            var link = Y.one(".banner-content a");
            link.simulate("click");
            Y.Assert.areEqual(link.get("title"), this.event.label);
            Y.Assert.areEqual("lane:bannerClick", this.event.category);
            Y.Assert.areEqual(link.get("href"), this.event.action);
        },
        
        testSectionMenuClick: function() {
            var link = Y.one(".sectionMenu a");
            link.simulate("click");
            Y.Assert.areEqual(link.get("textContent"), this.event.label);
            Y.Assert.areEqual("lane:laneNav-sectionMenu", this.event.category);
            Y.Assert.areEqual(link.get("href"), this.event.action);
        },
        
        testLaneFooterClick: function() {
            var link = Y.one("#laneFooter a");
            link.simulate("click");
            Y.Assert.areEqual(link.get("textContent"), this.event.label);
            Y.Assert.areEqual("lane:laneNav-footer", this.event.category);
            Y.Assert.areEqual(link.get("href"), this.event.action);
        },
        
        testBrowseResultClick: function() {
            var link = Y.one(".lwSearchResults a");
            link.simulate("click");
            Y.Assert.areEqual(link.get("textContent"), this.event.label);
            Y.Assert.areEqual("lane:browseResultClick", this.event.category);
            Y.Assert.areEqual(document.location.pathname, this.event.action);
            Y.Assert.areEqual(101, this.event.value);
        },
        
        testSearchResultClick: function() {
            Y.lane.Model.set(Y.lane.Model.QUERY, "query");
            var link = Y.one(".lwSearchResults a");
            link.simulate("click");
            Y.Assert.areEqual(link.get("textContent"), this.event.label);
            Y.Assert.areEqual("lane:searchResultClick", this.event.category);
            Y.Assert.areEqual("query", this.event.action);
            Y.Assert.areEqual(101, this.event.value);
            Y.lane.Model.set(Y.lane.Model.QUERY, null);
        }
        
//        trackingData: {},
//        track: function(td) {
//            this.trackingData = td;
//        },
//        setUp: function() {
//            this.trackingData = {};
//        },
//        testExists: function() {
//            Y.Assert.isObject(LANE.tracking);
//        },
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
    Y.Test.Runner.masterSuite.name = "tracking-test.js";
    Y.Test.Runner.run();
});
