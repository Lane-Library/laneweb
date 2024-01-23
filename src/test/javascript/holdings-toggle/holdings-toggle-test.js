YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let holdingsToggleTestCase = new Y.Test.Case({

        name: "holdings toggle Test Case",

        testTriggerContentPresent: function() {
            let triggers = document.querySelectorAll(".hldgsTrigger");
            Y.Assert.areEqual(2, triggers.length);
            triggers.forEach(function(trigger){
                Y.Assert.areEqual(1, trigger.querySelectorAll('.fa-solid.fa-angle-down').length);
            });
        },

        testToggleHoldings: function() {
            let triggers = document.querySelectorAll(".hldgsTrigger"),
                trackEvent,
                handler = L.on("tracker:trackableEvent", function(e) {
                    trackEvent = e;
                });
            triggers.forEach(function(trigger){
                trigger.click();
                Y.Assert.areEqual(1, trigger.querySelectorAll('.fa-solid.fa-angle-up').length);
                Y.Assert.areEqual("tracker:trackableEvent", trackEvent.type);
                Y.Assert.areEqual("lane:hldgsTrigger", trackEvent.category);
                Y.Assert.isTrue(trackEvent.action.indexOf(' -- close') > -1);
                Y.Assert.areEqual("Lancet", trackEvent.label);
                handler.detach();
            });
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(holdingsToggleTestCase);
    Y.Test.Runner.masterSuite.name = "holdings-toggle-test.js";
    Y.Test.Runner.run();

});