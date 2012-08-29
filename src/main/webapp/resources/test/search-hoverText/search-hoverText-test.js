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
        testHoverTargetVisibleThenHidden: function() {
            var trigger = Y.one(".hvrTrig"),
                target = trigger.one(".hvrTarg");
            trigger.simulate('mouseover');
            this.wait(function() {
                Y.Assert.areEqual("block", target.getStyle("display"));
                trigger.simulate('mouseout');
                Y.Assert.areEqual("none", target.getStyle("display"));
            }, 1500);
        }
    });
    
    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");
    
    
    Y.Test.Runner.add(searchHoverTextTestCase);
    Y.Test.Runner.run();
});
