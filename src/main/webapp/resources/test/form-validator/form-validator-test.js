"use strict";

var form = Y.one("form"),

validator = new Y.lane.FormValidator(form),

formsTestCase = new Y.Test.Case({
    name: 'Lane Forms Test Case',

    form: Y.one("form"),

    testForClassIncorrect: function() {
        form.simulate("submit");
        Y.Assert.isFalse(validator.isValid());
        Y.Assert.isTrue(form.one("input").hasClass("incorrect"));
    },

    testForClassCorrect: function() {
        form.one("input").set("value", "foo");
        form.simulate("submit");
        Y.Assert.isTrue(validator.isValid());
        Y.Assert.isTrue(form.one("input").hasClass("correct"));
    },

    testDestroy: function() {
        validator.destroy();
    }
});

form.on("submit", function(event) {
    event.preventDefault();
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(formsTestCase);
Y.Test.Runner.masterSuite.name = "form-validator-test.js";
Y.Test.Runner.run();
