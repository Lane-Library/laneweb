/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use("node-event-simulate", "console", "test", function(T){

    var searchIndicatorTestCase = new T.Test.Case({
        name: "Lane Search Indicator Test Case",
        setUp: function() {
            var indicator = T.one("#searchIndicator");
            if (indicator) {
                indicator.remove();
            }
        },
        testImageNotPresent: function() {
            T.Assert.isNull(T.one("#searchIndicator"));
        },
        testConstructorCreatesImage: function() {
            new Y.lane.SearchIndicator();
            T.Assert.isObject(T.one("#searchIndicator"));
        },
        testShowAndHide: function() {
            var indicator = new Y.lane.SearchIndicator();
            var node = T.one("#searchIndicator");
            indicator.show();
            T.Assert.areEqual("block", node.getStyle("display"));
            indicator.hide();
            T.Assert.areEqual("none", node.getStyle("display"));
        },
        testTwoIndicatorsPlayNice: function() {
            var indicator1 = new Y.lane.SearchIndicator();
            var indicator2 = new Y.lane.SearchIndicator();
            T.Assert.areEqual(1, T.all("#searchIndicator").size());
            var node = T.one("#searchIndicator");
            indicator1.show();
            T.Assert.areEqual("block", node.getStyle("display"));
            indicator2.hide();
            T.Assert.areEqual("none", node.getStyle("display"));
        }
    });
    
    T.one("body").addClass("yui3-skin-sam");
    new T.Console({
        newestOnTop: false
    }).render("#log");
    
    
    T.Test.Runner.add(searchIndicatorTestCase);
    T.Test.Runner.run();
});
