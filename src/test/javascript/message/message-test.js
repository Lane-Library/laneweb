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

document.querySelector("body").className = "yui3-skin-sam";
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(messageTestCase);
Y.Test.Runner.masterSuite.name = "message-test.js";
Y.Test.Runner.run();
