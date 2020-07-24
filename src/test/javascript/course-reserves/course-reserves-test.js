YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var courseReservesFilterTest = new Y.Test.Case({

        name : "Course Reserves Filter TestCase",

        "test filter": function() {
            var input = document.querySelector("#course-reserves-search");
            var keyup = document.createEvent("Event");
            Y.Assert.areEqual("", document.querySelectorAll("tr").item(1).style.display);
            Y.Assert.areEqual("", document.querySelectorAll("tr").item(2).style.display);
            input.value = "cowa"
            keyup.initEvent("keyup", false, true); 
            input.dispatchEvent(keyup);
            Y.Assert.areEqual("", document.querySelectorAll("tr").item(1).style.display);
            Y.Assert.areEqual("none", document.querySelectorAll("tr").item(2).style.display);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(courseReservesFilterTest);
    Y.Test.Runner.masterSuite.name = "course-reserves-test.js";
    Y.Test.Runner.run();

});
