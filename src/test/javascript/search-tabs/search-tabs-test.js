"use strict";

Y.Test.Runner.add(new Y.Test.Case({

    name: "Search Tabs TestCase",

    "test tab click activates": function() {
        var event = document.createEvent("UIEvent");
        event.initEvent("click", true, false);
        Array.prototype.forEach.call(document.querySelectorAll(".search-tab"), function(tab) {
            tab.dispatchEvent(event);
            Y.Assert.areEqual("search-tab search-tab-active", tab.className);
        });
    },

    "test tab select bubbles": function() {
        var event = document.createEvent("UIEvent");
        event.initEvent("click", true, false);
        var activeTab = document.querySelector(".search-tab-active");
        var lastSource = Y.lane.getData(activeTab, "source");
        Array.prototype.forEach.call(document.querySelectorAll(".search-tab"), function(tab) {
            var newSource, oldSource;
            Y.lane.once("searchTabs:change", function(event) {
                newSource = event.newVal.source;
                oldSource = event.oldVal.source;
            });
            tab.dispatchEvent(event);
            Y.Assert.areEqual(oldSource, lastSource);
            Y.Assert.areEqual(Y.lane.getData(tab, "source"), newSource);
            lastSource = newSource;
        });
    },

    "test tracking": function() {
        var activeTab = document.querySelector(".search-tab-active");
        var lastSource = Y.lane.getData(activeTab, "source");
        var trackEvent;
        var handler = Y.lane.on("tracker:trackableEvent", function(e) {
            trackEvent = e;
        });
        var event = document.createEvent("UIEvent");
        event.initEvent("click", true, false);
        Array.prototype.forEach.call(document.querySelectorAll(".search-tab"), function(tab) {
            tab.dispatchEvent(event);
            Y.Assert.areEqual("tracker:trackableEvent", trackEvent.type);
            Y.Assert.areEqual("lane:searchDropdownSelection", trackEvent.category);
            Y.Assert.areEqual(Y.lane.getData(tab, "source"), trackEvent.action);
            Y.Assert.areEqual("from " + lastSource + " to " + Y.lane.getData(tab, "source"), trackEvent.label);
            lastSource = Y.lane.getData(tab, "source");
        });
        handler.detach();
    }

}));

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.masterSuite.name = "search-tabs-test.js";
Y.Test.Runner.run();
