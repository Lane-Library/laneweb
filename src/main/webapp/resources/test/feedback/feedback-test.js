(function(){

    Y.io = function(url, config) {
        config.on.success.apply(config.context);
    };

    var feedbackTestCase = new Y.Test.Case({
        name: 'Lane Feedback Test Case',

        resetContent: function() {
            Y.lane.Lightbox.set("hash", "#feedback1");
            Y.lane.Lightbox.setContent("<div id='feedback'>" + Y.one("#xfeedback").get("innerHTML") + "</div>");
        },

        testSendFeedback: function() {
            this.resetContent();
            var feedback = Y.Widget.getByNode("#feedback");
            feedback.sendFeedback(Y.one("form"));
            Y.Assert.areEqual(feedback.get("thanks"), feedback.get("contentBox").one(".feedback-contents").get("text"));
        }
    });

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(feedbackTestCase);
    Y.Test.Runner.masterSuite.name = "feedback-test.js";
    Y.Test.Runner.run();

})();
