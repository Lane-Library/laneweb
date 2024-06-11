YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let searchIndicatorTestCase = new Y.Test.Case({

        name: "Lane Search Indicator Test Case",

        indicator : L.searchIndicator,

        node: document.querySelector(".search-indicator"),

        testShowAndHide: function() {
            this.indicator.show();
            Y.Assert.areEqual("search-indicator search-indicator-active", this.node.className);
            this.indicator.hide();
            Y.Assert.areEqual("search-indicator", this.node.className);
        },

        "test search:search event shows": function() {
            L.fire("search:search");
            Y.Assert.areEqual("search-indicator search-indicator-active", this.node.className);
        }
    });

    new Y.Test.Console().render();


    Y.Test.Runner.add(searchIndicatorTestCase);
    Y.Test.Runner.masterSuite.name = "search-indicator-test.js";
    Y.Test.Runner.run();

});
