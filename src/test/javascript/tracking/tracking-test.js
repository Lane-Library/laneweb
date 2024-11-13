YUI({ fetchCSS: false }).use("test", "test-console", "node-event-simulate", function (Y) {

    "use strict";

    document.addEventListener("click", function (event) {
        event.preventDefault();
    });

    L.setLocationHref = function () { };

    var pageView = null,
        event = null;


    let trackingTestCase = new Y.Test.Case({

        name: "Lane Tracking TestCase",




        fixPath: function (path) {
            return path.charAt(0) === "/" ? path : "/" + path;
        },

        setUp: function () {
            event = null;
            pageView = null;
        },

        eventCallback: function (e) {
            event = e;
        },

        pageViewCallback: function (event) {
            pageView = event;
        },


        testBodyClickDoesNothing: function () {
            Y.one("body").simulate("click");
            Y.Assert.isNull(event);
            Y.Assert.isNull(pageView);
        },

        testSearchFacetClick: function () {
            Y.one(".searchFacet a").simulate("click");
            Y.Assert.areEqual("search facet", pageView.title);
            Y.Assert.isNull(event);
        },


        testBookmarksClick: function () {
            L.Model.set(L.Model.AUTH, "auth");
            let link = Y.one("#bookmarks a");
            link.simulate("click");
            L.Model.set(L.Model.AUTH, null);
            Y.Assert.areEqual(link.get("text"), event.label);
            Y.Assert.areEqual("auth", event.action);
            Y.Assert.areEqual("lane:bookmarkClick", event.category);
            Y.Assert.isUndefined(event.path);
            Y.Assert.areEqual(link.get("text"), event.label);
            Y.Assert.isNull(pageView);
        },

        testBookmarksEditorClick: function () {
            L.Model.set(L.Model.AUTH, "auth");
            let link = Y.one(".bookmark-editor-content a");
            link.simulate("click");
            L.Model.set(L.Model.AUTH, null);
            Y.Assert.areEqual(link.get("text"), event.label);
            Y.Assert.areEqual("lane:bookmarkClick", event.category);
            Y.Assert.areEqual("auth", event.action);
            Y.Assert.isUndefined(event.path);
            Y.Assert.isNull(pageView);
        },

        testBrowseResultClick: function () {
            let link = Y.one(".lwSearchResults a");
            link.simulate("click");
            Y.Assert.areEqual("id-123 -> Primary Type -> " + link.get("text"), event.label);
            Y.Assert.areEqual("lane:browseResultClick", event.category);
            Y.Assert.areEqual(location.pathname, event.action);
            Y.Assert.areEqual(101, event.value);
            Y.Assert.areEqual(link.get("pathname"), pageView.path);
            Y.Assert.areEqual(link.get("text"), pageView.title);
        },

        testSearchResultClick: function () {
            L.Model.set(L.Model.URL_ENCODED_QUERY, "foo%20bar");
            let link = Y.one(".lwSearchResults a");
            link.simulate("click");
            Y.Assert.areEqual("id-123 -> Primary Type -> " + link.get("text"), event.label);
            Y.Assert.areEqual("lane:searchResultClick", event.category);
            Y.Assert.areEqual("foo bar", event.action);
            Y.Assert.areEqual(101, event.value);
            Y.Assert.areEqual(link.get("pathname"), pageView.path);
            Y.Assert.areEqual(link.get("text"), pageView.title);
            L.Model.set(L.Model.URL_ENCODED_QUERY, null);
        },

        testClickOnImage: function () {
            let link = Y.one("#eventImageLink");
            link.simulate("click");
            Y.Assert.areEqual(this.fixPath(link.get("parentNode").get("pathname")), pageView.path);
            Y.Assert.areEqual(link.get("alt"), pageView.title);
            Y.Assert.isNull(event);
        },

        testClickOnImageNoAlt: function () {
            let link = Y.one("#eventImageNoAlt");
            link.simulate("click");
            Y.Assert.areEqual(this.fixPath(link.get("parentNode").get("pathname")), pageView.path);
            Y.Assert.areEqual(link.get("src"), pageView.title);
            Y.Assert.isNull(event);
        },

        testClickPopup: function () {
            let link = Y.one("#popup");
            link.simulate("click");
            Y.Assert.areEqual(location.host, pageView.host);
            Y.Assert.areEqual(location.pathname, pageView.path);
            Y.Assert.areEqual("YUI Pop-up [local]: " + link.get("text"), pageView.title);
            Y.Assert.isFalse(pageView.external);
            Y.Assert.isNull(event);
        },

        testClickProxyCredential: function () {
            let link = Y.one("#proxylink");
            link.simulate("click");
            Y.Assert.areEqual("www.google.com", pageView.host);
            Y.Assert.areEqual("/foo/bar", pageView.path);
            Y.Assert.areEqual(link.get("text"), pageView.title);
            Y.Assert.isNull(event);
        },

        testClickProxyCme: function () {
            let link = Y.one("#cmelink");
            link.simulate("click");
            Y.Assert.areEqual("www.uptodate.com", pageView.host);
            Y.Assert.areEqual("/online", pageView.path);
            Y.Assert.areEqual(link.get("text"), pageView.title);
            Y.Assert.isNull(event);
        },

        testClickSecureVideo: function () {
            let link = Y.one("#secureVideo");
            link.simulate("click");
            Y.Assert.areEqual("lane.stanford.edu", pageView.host);
            Y.Assert.areEqual("/secure/edtech/", pageView.path);
            Y.Assert.areEqual(link.get("text"), pageView.title);
            Y.Assert.isNull(event);
        },

        testClickSeeAll: function () {
            let link = Y.one("#seeAll");
            link.simulate("click");
            Y.Assert.areEqual("lane:searchSeeAllClick", event.category);
            Y.Assert.areEqual("Resource Type see all", event.label);
            Y.Assert.areEqual("?source=all-all&q=test&facet=type&page=1", event.action);
            Y.Assert.isUndefined(event.value);
            Y.Assert.isNull(pageView);
        },

        testClickLaneproxy: function () {
            let link1 = Y.one("#laneproxy1"), link2 = Y.one("#laneproxy2");
            link1.simulate("click");
            Y.Assert.areEqual("www.nejm.org", pageView.host);
            Y.Assert.areEqual("/", pageView.path);
            Y.Assert.areEqual(link1.get("text"), pageView.title);
            Y.Assert.isNull(event);
            link2.simulate("click");
            Y.Assert.areEqual("www.nejm.org", pageView.host);
            Y.Assert.areEqual("/", pageView.path);
            Y.Assert.areEqual(link2.get("text"), pageView.title);
            Y.Assert.isNull(event);
        },

        testClickEndsWithHtml: function () {
            let link = Y.one("#endswithhtml");
            link.simulate("click");
            Y.Assert.isNull(pageView);
            Y.Assert.isNull(event);
        }

    });

    L.on("tracker:trackableEvent", trackingTestCase.eventCallback);
    L.on("tracker:trackablePageview", trackingTestCase.pageViewCallback);
    L.mergeEvents();

    new Y.Test.Console().render();

    Y.Test.Runner.add(trackingTestCase);
    Y.Test.Runner.masterSuite.name = "tracking-test.js";
    Y.Test.Runner.run();

});
