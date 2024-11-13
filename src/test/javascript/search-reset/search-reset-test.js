YUI({ fetchCSS: false }).use("test", "test-console", function (Y) {

    "use strict";

    L.tracker = { fire: function () { } };

    Y.Test.Runner.add(new Y.Test.Case({

        name: "Search Reset TestCase",

        reset: document.querySelector(".search-reset"),

        setUp: function () {
            this.reset.className = "search-reset";
        },

        "test Reset Click Deactivates": function () {
            document.addEventListener("DOMContentLoaded", function () {
                this.reset.className = "search-reset search-reset-active";
                let event = document.createEvent("UIEvent");
                event.initEvent("click", true, false);
                L.on("searchReset:reset", function () {
                    L.fire("search:queryChange", { newVal: "", oldVal: "query" });
                });
                this.reset.dispatchEvent(event);
                Y.Assert.areEqual("search-reset", this.reset.className);
            })
        },

        "test Query Change Activates": function () {
            L.fire("search:queryChange", { newVal: "q", oldVal: "" });
            Y.Assert.areEqual("search-reset search-reset-active", this.reset.className);
        },

        "test Query Additional Change remains active": function () {
            L.fire("search:queryChange", { newVal: "q", oldVal: "" });
            L.fire("search:queryChange", { newVal: "qu", oldVal: "q" });
            Y.Assert.areEqual("search-reset search-reset-active", this.reset.className);
        },

        "test tracking": function () {
            document.addEventListener("DOMContentLoaded", function () {
                let trackEvent;
                L.on("tracker:trackableEvent", function (e) {
                    trackEvent = e;
                });
                this.reset.className = "search-reset search-reset-active";
                let event = document.createEvent("UIEvent");
                event.initEvent("click", true, false);
                this.reset.dispatchEvent(event);
                Y.Assert.areEqual("tracker:trackableEvent", trackEvent.type);
                Y.Assert.areEqual(location.pathname, trackEvent.action);
                Y.Assert.areEqual("lane:searchFormReset", trackEvent.category);
            })
        }

    }));

    new Y.Test.Console().render();


    Y.Test.Runner.masterSuite.name = "search-reset-test.js";
    Y.Test.Runner.run();

});
