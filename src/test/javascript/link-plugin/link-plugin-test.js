YUI({fetchCSS:false}).use("test", "test-console", "node-pluginhost", function(Y) {

    "use strict";

    var linkTestCase = new Y.Test.Case({
        name: "Lane Link Test Case",

        testIsLocalLink : function() {
            var anchor = Y.one("#local");
            Y.Assert.isTrue(anchor.link.get("local"));
        },

        testIsProxyLoginLink : function () {
            var anchor = Y.one("#proxylogin");
            Y.Assert.isTrue(anchor.link.get("proxyLogin"));
        },

        testIsProxyLink : function() {
            var anchor1 = Y.one("#proxyurl1"), anchor2 = Y.one("#proxyurl2");
            Y.Assert.isTrue(anchor1.link.get("proxy"));
            Y.Assert.isTrue(anchor2.link.get("proxy"));
        },

        testProxyLoginIsNotLocal : function() {
            var anchor = Y.one("#proxylogin");
            Y.Assert.isFalse(anchor.link.get("local"));
        },

        testGetURL : function() {
            var anchor = Y.one("#example");
            Y.Assert.areEqual("http://www.example.com/example", anchor.link.get("url"));
        },

        testGetProxiedLoginURL : function() {
            var anchor = Y.one("#proxylogin");
            Y.Assert.areEqual("http://www.nejm.org/", anchor.link.get("url"));
        },

        testGetProxiedURL : function() {
            var anchor1 = Y.one("#proxyurl1"), anchor2 = Y.one("#proxyurl2");
            Y.Assert.areEqual("http://www.nejm.org/", anchor1.link.get("url"));
            Y.Assert.areEqual("http://www.nejm.org/", anchor2.link.get("url"));
        },

        testCookiesFetchIsNotLocal : function() {
            var anchor = Y.one("#cookiesFetch");
            Y.Assert.isFalse(anchor.link.get("local"));
        },

        testGetCookiesFetchURL : function() {
            var anchor = Y.one("#cookiesFetch");
            Y.Assert.areEqual("http://www.thomsonhc.com/carenotes/librarian/", anchor.link.get("url"));
        },

        testGetTitle: function() {
            var i, anchor, anchors = Y.all("#testGetTitle a");
            for (i = 0; i < anchors.size(); i++) {
                anchor = anchors.item(i);
                Y.Assert.areEqual(anchor.get("rel"), anchor.link.get("title"));
            }
        }
    });

    Y.all("a").plug(L.LinkPlugin);

    new Y.Test.Console().render();

    Y.Test.Runner.add(linkTestCase);
    Y.Test.Runner.masterSuite.name = "link-plugin-test.js";
    Y.Test.Runner.run();

});