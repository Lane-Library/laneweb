YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var classesTestCase = new Y.Test.Case({

        name : 'Lane Classes Test Case',
        
        "test seats left": function() {
            Y.Assert.areEqual("Seats left: 1", document.querySelector(".remaining-seats").innerText)
        },
        
        "test wait list": function() {
            Y.Assert.areEqual("Waiting List", document.querySelector("#class-registration-button-2 span").innerText)
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(classesTestCase);
    Y.Test.Runner.masterSuite.name = "classes-test.js";
    Y.Test.Runner.run();

});