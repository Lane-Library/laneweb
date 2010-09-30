/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("lane-search-indicator", "node-event-simulate", "console", "test", function(Y){

    var searchIndicatorTestCase = new Y.Test.Case({
        name: "Lane Search Indicator Test Case",
        setUp: function() {
            var indicator = Y.one("#searchIndicator");
            if (indicator) {
                indicator.remove();
            }
        },
        testImageNotPresent: function() {
            Y.Assert.isNull(Y.one("#searchIndicator"));
        },
        testConstructorCreatesImage: function() {
            new LANE.SearchIndicator();
            Y.Assert.isObject(Y.one("#searchIndicator"));
        },
        testShowAndHide: function() {
            var indicator = new LANE.SearchIndicator();
            var node = Y.one("#searchIndicator");
            indicator.show();
            Y.Assert.areEqual("block", node.getStyle("display"));
            indicator.hide();
            Y.Assert.areEqual("none", node.getStyle("display"));
        },
        testTwoIndicatorsPlayNice: function() {
            var indicator1 = new LANE.SearchIndicator();
            var indicator2 = new LANE.SearchIndicator();
            Y.Assert.areEqual(1, Y.all("#searchIndicator").size());
            var node = Y.one("#searchIndicator");
            indicator1.show();
            Y.Assert.areEqual("block", node.getStyle("display"));
            indicator2.hide();
            Y.Assert.areEqual("none", node.getStyle("display"));
        }
    });
    
    Y.one("body").addClass("yui3-skin-sam");
    new Y.Console({
        newestOnTop: false
    }).render("#log");
    
    
    Y.Test.Runner.add(searchIndicatorTestCase);
    Y.Test.Runner.run();
});
