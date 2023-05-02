YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    document.addEventListener("click", function(event) {
        event.preventDefault();
    });

    L.setLocationHref = function() {};

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

        testBookmarksClick: function() {
            L.Model.set(L.Model.AUTH, "auth");
            var link = Y.one("#bookmarks a");
            link.simulate("click");
            L.Model.set(L.Model.AUTH, null);
            Y.Assert.areEqual(link.get("text"), this.event.label);
            Y.Assert.areEqual("auth", this.event.action);
            Y.Assert.areEqual("lane:bookmarkClick", this.event.category);
            Y.Assert.isUndefined(this.event.path);
            Y.Assert.areEqual(link.get("text"), this.event.label);
            Y.Assert.isNull(this.pageView);
        },

        testBookmarksEditorClick: function() {
            L.Model.set(L.Model.AUTH, "auth");
            var link = Y.one(".yui3-bookmark-editor-content a");
            link.simulate("click");
            L.Model.set(L.Model.AUTH, null);
            Y.Assert.areEqual(link.get("text"), this.event.label);
            Y.Assert.areEqual("lane:bookmarkClick", this.event.category);
            Y.Assert.areEqual("auth", this.event.action);
            Y.Assert.isUndefined(this.event.path);
            Y.Assert.areEqual(link.get("text"), this.event.label);
            Y.Assert.isNull(this.pageView);
        },

        testBrowseResultClick: function() {
            var link = Y.one(".lwSearchResults a");
            link.simulate("click");
            Y.Assert.areEqual("id-123 -> Primary Type -> " + link.get("text"), this.event.label);
            Y.Assert.areEqual("lane:browseResultClick", this.event.category);
            Y.Assert.areEqual(location.pathname, this.event.action);
            Y.Assert.areEqual(101, this.event.value);
            Y.Assert.areEqual(this.fixPath(link.get("pathname")) , this.pageView.path);
            Y.Assert.areEqual(link.get("text"), this.pageView.title);
        },

        testSearchResultClick: function() {
            L.Model.set(L.Model.URL_ENCODED_QUERY, "foo%20bar");
            var link = Y.one(".lwSearchResults a");
            link.simulate("click");
            Y.Assert.areEqual("id-123 -> Primary Type -> " + link.get("text"), this.event.label);
            Y.Assert.areEqual("lane:searchResultClick", this.event.category);
            Y.Assert.areEqual("foo bar", this.event.action);
            Y.Assert.areEqual(101, this.event.value);
            Y.Assert.areEqual(this.fixPath(link.get("pathname")) , this.pageView.path);
            Y.Assert.areEqual(link.get("text"), this.pageView.title);
            L.Model.set(L.Model.URL_ENCODED_QUERY, null);
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
            Y.Assert.areEqual(location.host, this.pageView.host);
            Y.Assert.areEqual(location.pathname , this.pageView.path);
            Y.Assert.areEqual("YUI Pop-up [local]: " + link.get("text"), this.pageView.title);
            Y.Assert.isFalse(this.pageView.external);
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

        testClickSecureVideo: function() {
            var link = Y.one("#secureVideo");
            link.simulate("click");
            Y.Assert.areEqual("lane.stanford.edu", this.pageView.host);
            Y.Assert.areEqual("/secure/edtech/" , this.pageView.path);
            Y.Assert.areEqual(link.get("text"), this.pageView.title);
            Y.Assert.isNull(this.event);
        },

        testClickSeeAll: function() {
            var link = Y.one("#seeAll");
            link.simulate("click");
            Y.Assert.areEqual("lane:searchSeeAllClick", this.event.category);
            Y.Assert.areEqual("Resource Type see all", this.event.label);
            Y.Assert.areEqual("?source=all-all&q=test&facet=type&page=1", this.event.action);
            Y.Assert.isUndefined(this.event.value);
            Y.Assert.isNull(this.pageView);
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

    L.on("tracker:trackableEvent", trackingTestCase.eventCallback, trackingTestCase);
    L.on("tracker:trackablePageview", trackingTestCase.pageViewCallback, trackingTestCase);

    new Y.Test.Console().render();

    Y.Test.Runner.add(trackingTestCase);
    Y.Test.Runner.masterSuite.name = "tracking-test.js";
    Y.Test.Runner.run();

});
