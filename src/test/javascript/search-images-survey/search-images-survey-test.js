YUI().use("test", "test-console", function(Y) {

    "use strict";

    var searchImagesSurveyTestCase = new Y.Test.Case({

        name: "Images Survey TestCase",

// JS unit tests pass locally but fail with grover-maven-plugin

//        "test click survey link": function() {
//            var link = document.querySelector(".surveyLinks a"), 
//            parent = link.parentNode, 
//            event,
//            handle = L.on("tracker:trackableEvent", function(e) {
//                event = e;
//            });
//            Y.Assert.areEqual("", parent.style.display);
//            link.click();
//            Y.Assert.areNotEqual("", parent.style.display);
//            Y.Assert.areEqual("lane:imageSearchSurvey", event.category);
//            Y.Assert.areEqual("response", event.action);
//            Y.Assert.areEqual(" Yes", event.label);
//        },
//
//        "test tracking images in viewport": function() {
//            var event;
//            var handle = L.on("tracker:trackableEvent", function(e) {
//                event = e;
//            });
//            L.fire("viewport:init", {
//                viewport: {
//                    inView: function() {
//                        return true;
//                    }
//                }
//            });
//            Y.Assert.areEqual("lane:imageSearchPromo", event.category);
//            Y.Assert.areEqual("images viewed", event.action);
//        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(searchImagesSurveyTestCase);
    Y.Test.Runner.masterSuite.name = "search-images-survey-test.js";
    Y.Test.Runner.run();

});