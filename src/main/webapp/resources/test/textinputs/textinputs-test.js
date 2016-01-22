"use strict";

var placeholderCapable = 'placeholder' in Y.Node.getDOMNode(Y.one("#input1"));
var textinputsTestCase = new Y.Test.Case({
    name: "Lane TextInputs Test Case",
    testConstructorHintText : function() {
        var input = Y.one("#input1");
        Y.Assert.areEqual("", input.get("value"));
        var textInput = new Y.lane.TextInput(input, "hint text");
        Y.Assert.areEqual("", textInput.getValue());
        if (placeholderCapable) {
            Y.Assert.areEqual("hint text", input.get("placeholder"));
        } else {
            Y.Assert.areEqual("hint text", input.get("value"));
            Y.Assert.isTrue(input.hasClass("inputHint"));
        }
    },
    testSetHintText: function() {
        var input = Y.one("#input2");
        var textInput = new Y.lane.TextInput(input, "hint text");
        textInput.setHintText("new hint text");
        if (placeholderCapable) {
            Y.Assert.areEqual("new hint text", input.get("placeholder"));
        } else {
            Y.Assert.areEqual("new hint text", input.get("value"));
            Y.Assert.isTrue(input.hasClass("inputHint"));
        }
    },
    testFocus: function() {
        var input = Y.one("#input3");
        var textInput = new Y.lane.TextInput(input, "hint text");
        if (placeholderCapable) {
            input.simulate("focus");
            Y.Assert.areEqual("", input.get("value"), "value = " + input.get("value"));
        } else {
            Y.Assert.isTrue(input.hasClass("inputHint"), "doesn\"t have class inputHint");
            //have to do input.after("focus") safari value is not set on return from simulate.
            var focusHandle = input.after("focus", function() {
                input.detach(focusHandle);
                Y.Assert.areEqual("", input.get("value"), "value = " + input.get("value"));
                Y.Assert.isFalse(input.hasClass("inputHint"), "has class inputHint");
            });
            input.simulate("focus");
        }
    },
    testBlur: function() {
        var input = Y.one("#input4");
        var textInput = new Y.lane.TextInput(input, "hint text");
        if (placeholderCapable) {
            input.simulate("blur");
            Y.Assert.areEqual("hint text", input.get("placeholder"));
        } else {
            Y.Assert.isTrue(input.hasClass("inputHint"), "doesn\"t have class inputHint");
            //have to do input.after("focus") safari value is not set on return from simulate.
            var blurHandle = input.after("blur", function() {
                input.detach(blurHandle);
                Y.Assert.areEqual("hint text", input.get("value"));
                Y.Assert.isTrue(input.hasClass("inputHint"));
            });
            var focusHandle = input.after("focus", function() {
                input.detach(focusHandle);
                input.simulate("blur");
            });
            input.simulate("focus");
        }
    },
    testNoHintText: function() {
        var input = Y.one("#input5");
        var textInput = new Y.lane.TextInput(input);
        if (placeholderCapable) {
            Y.Assert.areEqual(input.get("placeholder"), "");
        } else {
            Y.Assert.areEqual(input.get("value"), "");
            Y.Assert.isTrue(input.hasClass("inputHint"));
        }
    }
});

Y.one("body").addClass("yui3-skin-sam");
new Y.Console({
    newestOnTop: false
}).render("#log");


Y.Test.Runner.add(textinputsTestCase);
Y.Test.Runner.masterSuite.name = "textinputs-test.js";
Y.Test.Runner.run();
