"use strict";

var searchIndicatorTestCase = new Y.Test.Case({
    name: "Lane Search Indicator Test Case",

    indicator : Y.lane.SearchIndicator,

    testShowAndHide: function() {
        var node = Y.one(".searchIndicator");
        this.indicator.show();
        Y.Assert.isTrue(node.hasClass("show"));
        this.indicator.hide();
        Y.Assert.isFalse(node.hasClass("show"));
    }
});

Y.one("body").addClass("yui3-skin-sam");
new Y.Console({
    newestOnTop: false
}).render("#log");


Y.Test.Runner.add(searchIndicatorTestCase);
Y.Test.Runner.masterSuite.name = "search-indicator-test.js";
Y.Test.Runner.run();
