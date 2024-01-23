YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let linkTestCase = new Y.Test.Case({
        name: "Lane LinkInfo Test Case",

        testIsLocalLink : function() {
            let anchor = document.querySelector("#local");
            Y.Assert.isTrue(new L.LinkInfo(anchor).local);
        },

        testIsProxyLoginLink : function () {
            let anchor = document.querySelector("#proxylogin");
            Y.Assert.isTrue(new L.LinkInfo(anchor).proxyLogin);
        },

        testIsProxyLink : function() {
            let anchor1 = document.querySelector("#proxyurl1"),
                anchor2 = document.querySelector("#proxyurl2");
            Y.Assert.isTrue(new L.LinkInfo(anchor1).proxy);
            Y.Assert.isTrue(new L.LinkInfo(anchor2).proxy);
        },

        testProxyLoginIsNotLocal : function() {
            let anchor = document.querySelector("#proxylogin");
            Y.Assert.isFalse(new L.LinkInfo(anchor).local);
        },

        testGetURL : function() {
            let anchor = document.querySelector("#example");
            Y.Assert.areEqual("http://www.example.com/example", new L.LinkInfo(anchor).url);
        },

        testGetProxiedLoginURL : function() {
            let anchor = document.querySelector("#proxylogin");
            Y.Assert.areEqual("http://www.nejm.org/", new L.LinkInfo(anchor).url);
        },

        testGetProxiedURL : function() {
            let anchor1 = document.querySelector("#proxyurl1"), anchor2 = document.querySelector("#proxyurl2");
            Y.Assert.areEqual("http://www.nejm.org/", new L.LinkInfo(anchor1).url);
            Y.Assert.areEqual("http://www.nejm.org/", new L.LinkInfo(anchor2).url);
        },
        testGetTitle: function() {
            let i, anchor, anchors = document.querySelectorAll("#testGetTitle a");
            for (i = 0; i < anchors.length; i++) {
                anchor = anchors.item(i);
                Y.Assert.areEqual(anchor.rel, new L.LinkInfo(anchor).title);
            }
        },
        
        "test linkHost with port": function() {
            let anchor = document.getElementById("link-host");
            Y.Assert.areEqual("linkhost", new L.LinkInfo(anchor).linkHost);
        },
        
        "test query": function() {
            let anchor = document.getElementById("query");
            Y.Assert.areEqual("?foo=bar", new L.LinkInfo(anchor).query);
        },
        
        "test trackable": function() {
            let anchor = document.getElementById("trackable");
            Y.Assert.isTrue(new L.LinkInfo(anchor).trackable);
        },
        
        "test trackingData": function() {
            let anchor = document.getElementById("tracking-data");
            let trackingData = new L.LinkInfo(anchor).trackingData;
            Y.Assert.isTrue(trackingData.external);
            Y.Assert.areEqual("www.example.com", trackingData.host);
            Y.Assert.areEqual("/path.html", trackingData.path);
            Y.Assert.areEqual("", trackingData.query);
            Y.Assert.areEqual("tracking data", trackingData.title);
        },
        
        "test trackingData proxyLogin": function() {
            let anchor = document.getElementById("proxylogin");
            let trackingData = new L.LinkInfo(anchor).trackingData;
            Y.Assert.isTrue(trackingData.external);
            Y.Assert.areEqual("www.nejm.org", trackingData.host);
            Y.Assert.areEqual("/", trackingData.path);
            Y.Assert.areEqual("", trackingData.query);
            Y.Assert.areEqual("proxy login", trackingData.title);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(linkTestCase);
    Y.Test.Runner.masterSuite.name = "link-info-test.js";
    Y.Test.Runner.run();

});