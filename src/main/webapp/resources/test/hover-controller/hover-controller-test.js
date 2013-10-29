Y.applyConfig({fetchCSS:true});
Y.use("node-event-simulate", "console", "test", function(Y){

    var searchHoverTextTestCase = new Y.Test.Case({
        name: "Lane Search Hover Text Test Case",
        testHoverTargetPresent: function() {
            Y.Assert.isNotNull(Y.one(".hvrTarg"));
        },
        testHoverTriggerPresent: function() {
            Y.Assert.isNotNull(Y.one(".hvrTrig"));
        },
        testHoverToggleActive: function() {
            var trigger = Y.one(".hvrTrig");
            trigger.simulate('mouseover');
            this.wait(function() {
                Y.Assert.isTrue(trigger.hasClass("active"));
                trigger.simulate('mouseout');
                Y.Assert.isFalse(trigger.hasClass("active"));
            }, 1500);
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
