YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    Y.Test.Runner.add(new Y.Test.Case({

        name: "Search Tabs TestCase",


        "test tracking": function() {
 			    Y.Assert.areEqual("tracker:trackableEvent", "tracker:trackableEvent");
            
        }

    }));

    new Y.Test.Console().render();


    Y.Test.Runner.masterSuite.name = "search-tabs-test.js";
    Y.Test.Runner.run();

});
