"use strict";

Y.on("click", function(event) {
    event.preventDefault();
    event._event.preventDefault();
});

Y.lane.Location.on("hrefChange", function(event) {
    event.preventDefault();
});

var trackingTestCase = new Y.Test.Case({

    name: "Lane Tracking TestCase",

    event: null,

    pageView: null,

    fixPath: function(path) {
        return path.charAt(0) === "/" ? path : "/" + path;
    },

    setUp: function() {
        this.event = null;
        this.pageView = null;
    },

    eventCallback: function(event) {
        this.event = event;
    },

    pageViewCallback: function(event) {
        this.pageView = event;
    },

    testBodyClickDoesNothing : function() {
        Y.one("body").simulate("click");
        Y.Assert.isNull(this.event);
        Y.Assert.isNull(this.pageView);
    },

    testSearchFacetClick: function() {
        Y.one(".searchFacet a").simulate("click");
        Y.Assert.areEqual("search facet", this.pageView.title);
        Y.Assert.isNull(this.event);
    },

    testExpandyTriggerClick: function() {
        var link = Y.one('.yui3-accordion-item-trigger');
        link.simulate('click');
        Y.Assert.areEqual("Expandy:head", this.pageView.title);
        Y.Assert.isNull(this.event);
    },

    testFavoritesClick: function() {
        Y.lane.Model.set(Y.lane.Model.AUTH, "auth");
        var link = Y.one(".favorites a");
        link.simulate("click");
        Y.lane.Model.set(Y.lane.Model.AUTH, null);
        Y.Assert.areEqual(link.get("text"), this.event.label);
        Y.Assert.areEqual("lane:bookmarkClick", this.event.category);
        Y.Assert.areEqual("auth", this.event.action);
        Y.Assert.areEqual("/" , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
    },

    testBookmarksClick: function() {
        Y.lane.Model.set(Y.lane.Model.AUTH, "auth");
        var link = Y.one("#bookmarks a");
        link.simulate("click");
        Y.lane.Model.set(Y.lane.Model.AUTH, null);
        Y.Assert.areEqual(link.get("text"), this.event.label);
        Y.Assert.areEqual("auth", this.event.action);
        Y.Assert.areEqual("lane:bookmarkClick", this.event.category);
        Y.Assert.areEqual("/" , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
    },

    testBookmarksEditorClick: function() {
        Y.lane.Model.set(Y.lane.Model.AUTH, "auth");
        var link = Y.one(".yui3-bookmark-editor-content a");
        link.simulate("click");
        Y.lane.Model.set(Y.lane.Model.AUTH, null);
        Y.Assert.areEqual(link.get("text"), this.event.label);
        Y.Assert.areEqual("lane:bookmarkClick", this.event.category);
        Y.Assert.areEqual("auth", this.event.action);
        Y.Assert.areEqual("/" , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
    },

    testLaneNavClick: function() {
        var link = Y.one(".lane-nav a");
        link.simulate("click");
        Y.Assert.areEqual(link.get("text"), this.event.label);
        Y.Assert.areEqual("lane:laneNav-top", this.event.category);
        Y.Assert.areEqual(link.get("href"), this.event.action);
        Y.Assert.areEqual("/" , this.pageView.path);
        Y.Assert.areEqual("laneNav: " + link.get("text"), this.pageView.title);
    },

    testQLinkClick: function() {
        var link = Y.one(".qlinks a");
        link.simulate("click");
        Y.Assert.areEqual(link.get("text"), this.event.label);
        Y.Assert.areEqual("lane:quickLinkClick", this.event.category);
        Y.Assert.areEqual(link.get("href"), this.event.action);
        Y.Assert.areEqual(this.fixPath(link.get("pathname")) , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
    },

    testBannerContentClick: function() {
        var link = Y.one(".banner-content a");
        link.simulate("click");
        Y.Assert.areEqual(link.get("title"), this.event.label);
        Y.Assert.areEqual("lane:bannerClick", this.event.category);
        Y.Assert.areEqual(link.get("href"), this.event.action);
        Y.Assert.areEqual(this.fixPath(link.get("pathname")) , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
    },

    testLaneFooterClick: function() {
        var link = Y.one("#laneFooter a");
        link.simulate("click");
        Y.Assert.areEqual(link.get("text"), this.event.label);
        Y.Assert.areEqual("lane:laneNav-footer", this.event.category);
        Y.Assert.areEqual(link.get("href"), this.event.action);
        Y.Assert.areEqual(this.fixPath(link.get("pathname")) , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
    },

    testBrowseResultClick: function() {
        var link = Y.one(".lwSearchResults a");
        link.simulate("click");
        Y.Assert.areEqual(link.get("text"), this.event.label);
        Y.Assert.areEqual("lane:browseResultClick", this.event.category);
        Y.Assert.areEqual(Y.lane.Location.get("pathname"), this.event.action);
        Y.Assert.areEqual(101, this.event.value);
        Y.Assert.areEqual(this.fixPath(link.get("pathname")) , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
    },

    testSearchResultClick: function() {
        Y.lane.Model.set(Y.lane.Model.URL_ENCODED_QUERY, "foo%20bar");
        var link = Y.one(".lwSearchResults a");
        link.simulate("click");
        Y.Assert.areEqual(link.get("text"), this.event.label);
        Y.Assert.areEqual("lane:searchResultClick", this.event.category);
        Y.Assert.areEqual("foo bar", this.event.action);
        Y.Assert.areEqual(101, this.event.value);
        Y.Assert.areEqual(this.fixPath(link.get("pathname")) , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
        Y.lane.Model.set(Y.lane.Model.URL_ENCODED_QUERY, null);
    },

    testClickOnImage: function() {
        var link = Y.one("#eventImageLink");
        link.simulate("click");
        Y.Assert.areEqual(this.fixPath(link.get("parentNode").get("pathname")) , this.pageView.path);
        Y.Assert.areEqual(link.get("alt"), this.pageView.title);
        Y.Assert.isNull(this.event);
    },

    testClickOnImageNoAlt: function() {
        var link = Y.one("#eventImageNoAlt");
        link.simulate("click");
        Y.Assert.areEqual(this.fixPath(link.get("parentNode").get("pathname")) , this.pageView.path);
        Y.Assert.areEqual(link.get("src"), this.pageView.title);
        Y.Assert.isNull(this.event);
    },

    testClickPopup: function() {
        var link = Y.one("#popup");
        link.simulate("click");
        Y.Assert.areEqual(Y.lane.Location.get("host"), this.pageView.host);
        Y.Assert.areEqual(Y.lane.Location.get("pathname") , this.pageView.path);
        Y.Assert.areEqual("YUI Pop-up [local]: " + link.get("text"), this.pageView.title);
        Y.Assert.isNull(this.event);
    },

    testClickCookieFetch: function() {
        var link = Y.one("#testTrackCookiesFetch");
        link.simulate("click");
        Y.Assert.areEqual("www.thomsonhc.com", this.pageView.host);
        Y.Assert.areEqual("/carenotes/librarian/" , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
        Y.Assert.isNull(this.event);
    },

    testClickProxyCredential: function() {
        var link = Y.one("#proxylink");
        link.simulate("click");
        Y.Assert.areEqual("www.google.com", this.pageView.host);
        Y.Assert.areEqual("/foo/bar" , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
        Y.Assert.isNull(this.event);
    },

    testClickProxyCme: function() {
        var link = Y.one("#cmelink");
        link.simulate("click");
        Y.Assert.areEqual("www.uptodate.com", this.pageView.host);
        Y.Assert.areEqual("/online" , this.pageView.path);
        Y.Assert.areEqual(link.get("text"), this.pageView.title);
        Y.Assert.isNull(this.event);
    },

    testClickLaneproxy: function() {
        var link1 = Y.one("#laneproxy1"), link2 = Y.one("#laneproxy2");
        link1.simulate("click");
        Y.Assert.areEqual("www.nejm.org", this.pageView.host);
        Y.Assert.areEqual("/" , this.pageView.path);
        Y.Assert.areEqual(link1.get("text"), this.pageView.title);
        Y.Assert.isNull(this.event);
        link2.simulate("click");
        Y.Assert.areEqual("www.nejm.org", this.pageView.host);
        Y.Assert.areEqual("/" , this.pageView.path);
        Y.Assert.areEqual(link2.get("text"), this.pageView.title);
        Y.Assert.isNull(this.event);
    },

    testClickEndsWithHtml: function() {
        var link = Y.one("#endswithhtml");
        link.simulate("click");
        Y.Assert.isNull(this.pageView);
        Y.Assert.isNull(this.event);
    }
});

Y.lane.on("tracker:trackableEvent", trackingTestCase.eventCallback, trackingTestCase);
Y.lane.on("tracker:trackablePageview", trackingTestCase.pageViewCallback, trackingTestCase);

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(trackingTestCase);
Y.Test.Runner.masterSuite.name = "tracking-test.js";
Y.Test.Runner.run();
