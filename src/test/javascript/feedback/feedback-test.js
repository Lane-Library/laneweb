YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    L.io = function(url, config) {
        config.on.success.apply(config.context);
    };

    var feedbackTestCase = new Y.Test.Case({
        name: 'Lane Feedback Test Case',

        resetContent: function() {
            L.Lightbox.set("hash", "#feedback1");
            L.Lightbox.setContent("<div id='feedback'>" + Y.one("#xfeedback").get("innerHTML") + "</div>");
        },

        testSendFeedback: function() {
            this.resetContent();
            document.querySelector("form").dispatchEvent(new Event("submit"));
            Y.Assert.areEqual("Thank you for your feedback.", Y.one(".feedback-contents").get("text"));
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(feedbackTestCase);
    Y.Test.Runner.masterSuite.name = "feedback-test.js";
    Y.Test.Runner.run();

});
