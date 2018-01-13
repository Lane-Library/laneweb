YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var messageTestCase = new Y.Test.Case({

        name: "Lane Message TestCase",

        test: function() {
            var message = "";
            window.alert = function(m) {
                message = m;
            }
            L.showMessage("message");
            Y.Test.Assert.areSame("message", message);
        }
    });

    new Y.Test.Console().render();


    Y.Test.Runner.add(messageTestCase);
    Y.Test.Runner.masterSuite.name = "message-test.js";
    Y.Test.Runner.run();

});
