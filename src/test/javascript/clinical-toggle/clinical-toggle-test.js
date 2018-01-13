YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    var clinicalToggleTestCase = new Y.Test.Case({

        name : "Clinical Toggle TestCase",

        setUp: function() {
            L.searchIndicator.hide();
            Y.one(".clinical-toggle").set("className", "clinical-toggle");
            Y.one(".clinical-toggle-circle").set("className", "clinical-toggle-circle clinical-toggle-circle-all");
        },

        "test indicator active on click" : function() {
            var toggle = Y.one(".clinical-toggle");
            var indicator = Y.one(".search-indicator");
            Y.Assert.isFalse(indicator.hasClass("search-indicator-active"));
            toggle.simulate("click");
            Y.Assert.isTrue(indicator.hasClass("search-indicator-active"));
        },

        "test circle changes from all to peds on click": function() {
            var toggle = Y.one(".clinical-toggle");
            var circle = Y.one(".clinical-toggle-circle");
            Y.Assert.isFalse(circle.hasClass("clinical-toggle-circle-peds"));
            toggle.simulate("click");
            Y.Assert.isTrue(circle.hasClass("clinical-toggle-circle-peds"));
        },

        "test circle changes from peds to all on click": function() {
            var toggle = Y.one(".clinical-toggle");
            var circle = Y.one(".clinical-toggle-circle");
            circle.set("className", "clinical-toggle-circle clinical-toggle-circle-peds");
            Y.Assert.isTrue(circle.hasClass("clinical-toggle-circle-peds"));
            toggle.simulate("click");
            Y.Assert.isFalse(circle.hasClass("clinical-toggle-circle-peds"));
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(clinicalToggleTestCase);
    Y.Test.Runner.masterSuite.name = "clinical-toggle-test.js";
    Y.Test.Runner.run();

});
