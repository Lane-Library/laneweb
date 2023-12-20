YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let modelTestCase = new Y.Test.Case({

        name: 'Lane Model Test Case',

        model: L.Model,

        testGetValue : function() {
            Y.Assert.areEqual(this.model.get("key"), "value");
        },

        testSetGetValue: function() {
            this.model.set("key", "newvalue");
            Y.Assert.areEqual(this.model.get("key"), "newvalue");
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(modelTestCase);
    Y.Test.Runner.masterSuite.name = "model-test.js";
    Y.Test.Runner.run();

});
