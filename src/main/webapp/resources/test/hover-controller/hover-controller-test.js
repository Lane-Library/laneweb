Y.applyConfig({fetchCSS:true});
Y.use("node-event-simulate", "console", "test", function(Y){

    var searchHoverTextTestCase = new Y.Test.Case({
        name: "Lane Search Hover Text Test Case",
        testDescriptionTriggerPresent: function() {
            Y.Assert.isNotNull(Y.one(".descriptionTrigger"));
        },
        testToggleDescriptionOn: function() {
            var trigger = Y.one(".descriptionTrigger");
            var item = Y.one("#searchResults li");
            trigger.simulate('click');
            Y.Assert.isTrue(item.hasClass("active"));
        },
        testToggleDescriptionOff: function() {
            var trigger = Y.one(".descriptionTrigger");
            var item = Y.one("#searchResults li");
            trigger.simulate('click');
            Y.Assert.isFalse(item.hasClass("active"));
        }
    });

    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");


    Y.Test.Runner.add(searchHoverTextTestCase);
    Y.Test.Runner.masterSuite.name = "hover-controller-test.js";
    Y.Test.Runner.run();
});
