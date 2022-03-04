YUI({ fetchCSS: false }).use("test", "test-console", "dom", "node-event-simulate", function(Y) {

    "use strict";

    var searchHideDnlmFacetTestCase = new Y.Test.Case({

        name: 'Search Hide NLM Catalog Facet Test Case',

        testHide: function() {
            var header1 = document.getElementById('header1'), dnlm1 = document.getElementById('dnlm1'),
                header2 = document.getElementById('header2'), dnlm2 = document.getElementById('dnlm2');
            Y.Assert.areNotEqual("none", header1.style.display);
            Y.Assert.areEqual("none", dnlm1.style.display);
            Y.Assert.areEqual("none", header2.style.display);
            Y.Assert.areEqual("none", dnlm2.style.display);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(searchHideDnlmFacetTestCase);
    Y.Test.Runner.masterSuite.name = "search-hide-dnlm-facet.js";
    Y.Test.Runner.run();

});
