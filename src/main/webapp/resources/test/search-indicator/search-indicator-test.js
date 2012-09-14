/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use("node-event-simulate", "console", "test", function(Y){

    var searchIndicatorTestCase = new Y.Test.Case({
        name: "Lane Search Indicator Test Case",
        
        indicator : Y.lane.SearchIndicator,
        
        testShowAndHide: function() {
            var node = Y.one("#searchIndicator");
            this.indicator.show();
            Y.Assert.isTrue(node.hasClass("on"));
            this.indicator.hide();
            Y.Assert.isFalse(node.hasClass("on"));
        }
    });
    
    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");
    
    
    Y.Test.Runner.add(searchIndicatorTestCase);
    Y.Test.Runner.run();
});
