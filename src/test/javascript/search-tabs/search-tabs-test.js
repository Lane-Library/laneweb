YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

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
            var lastSource = activeTab.dataset.source;
            Array.prototype.forEach.call(document.querySelectorAll(".search-tab"), function(tab) {
                var newSource, oldSource;
                L.once("searchTabs:change", function(event) {
                    newSource = event.newVal.source;
                    oldSource = event.oldVal.source;
                });
                tab.dispatchEvent(event);
                Y.Assert.areEqual(oldSource, lastSource);
                Y.Assert.areEqual(tab.dataset.source, newSource);
                lastSource = newSource;
            });
        },

        "test tracking": function() {
            var activeTab = document.querySelector(".search-tab-active");
            var lastSource = activeTab.dataset.source;
            var trackEvent;
            var handler = L.on("tracker:trackableEvent", function(e) {
                trackEvent = e;
            });
            var event = document.createEvent("UIEvent");
            event.initEvent("click", true, false);
            Array.prototype.forEach.call(document.querySelectorAll(".search-tab"), function(tab) {
                tab.dispatchEvent(event);
                Y.Assert.areEqual("tracker:trackableEvent", trackEvent.type);
                Y.Assert.areEqual("lane:searchDropdownSelection", trackEvent.category);
                Y.Assert.areEqual(tab.dataset.source, trackEvent.action);
                Y.Assert.areEqual("from " + lastSource + " to " + tab.dataset.source, trackEvent.label);
                lastSource = tab.dataset.source;
            });
            handler.detach();
        }

    }));

    new Y.Test.Console().render();


    Y.Test.Runner.masterSuite.name = "search-tabs-test.js";
    Y.Test.Runner.run();

});
