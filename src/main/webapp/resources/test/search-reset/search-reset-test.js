"use strict";

Y.lane.tracker = {fire: function(){}};

Y.Test.Runner.add(new Y.Test.Case({

    name: "Search Reset TestCase",

    reset: document.querySelector(".search-reset"),

    setUp: function() {
        this.reset.className = "search-reset";
    },

    "test Reset Click Deactivates": function() {
        this.reset.className = "search-reset search-reset-active";
        var event = document.createEvent("UIEvent");
        event.initEvent("click", true, false);
        Y.lane.once("searchReset:reset", function() {
            Y.lane.fire("search:queryChange", {newVal:"", oldVal:"query"});
        });
        this.reset.dispatchEvent(event);
        Y.Assert.areEqual("search-reset", this.reset.className);
    },

    "test Query Change Activates": function() {
        Y.lane.fire("search:queryChange", {newVal:"q", oldVal:""});
        Y.Assert.areEqual("search-reset search-reset-active", this.reset.className);
    },

    "test Query Additional Change remains active": function() {
        Y.lane.fire("search:queryChange", {newVal:"q", oldVal:""});
        Y.lane.fire("search:queryChange", {newVal:"qu", oldVal:"q"});
        Y.Assert.areEqual("search-reset search-reset-active", this.reset.className);
    },

    "test tracking": function() {
        var trackEvent;
        Y.lane.once("tracker:trackableEvent", function(e) {
            trackEvent = e;
        });
        this.reset.className = "search-reset search-reset-active";
        var event = document.createEvent("UIEvent");
        event.initEvent("click", true, false);
        this.reset.dispatchEvent(event);
        Y.Assert.areEqual("tracker:trackableEvent", trackEvent.type);
        Y.Assert.areEqual(location.pathname, trackEvent.action);
        Y.Assert.areEqual("lane:searchFormReset", trackEvent.category);
    }

}));

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.masterSuite.name = "search-reset-test.js";
Y.Test.Runner.run();