"use strict";

var placeholderCapable = document.createElement("input").placeholder !== undefined;

Y.Test.Runner.add(new Y.Test.Case({

    name: "Lane No Placeholder TextInputs Test Case",

    input: null,
    
    textInput: null,

    setUp: function() {
        this.input = Y.one("#input");
    },

    tearDown: function() {
        if (this.textInput !== null) {
            this.textInput.destroy();
            this.textInput = null;
        }
        this.input.set("value","");
    },

    testConstructorHintText : function() {
        Y.Assert.areEqual("", this.input.get("value"));
        this.textInput = new Y.lane.TextInput(this.input, "hint text", true);
        Y.Assert.isTrue(this.input.hasClass("inputHint"), "doesn't have class this.inputHint");
        Y.Assert.areEqual("hint text", this.input.get("value"));
        Y.Assert.isTrue(this.input.hasClass("inputHint"));
    },
    testSetHintText: function() {
        this.textInput = new Y.lane.TextInput(this.input, "hint text", true);
        Y.Assert.isTrue(this.input.hasClass("inputHint"), "doesn't have class this.inputHint");
        this.textInput.setHintText("new hint text");
        Y.Assert.areEqual("new hint text", this.input.get("value"));
        Y.Assert.isTrue(this.input.hasClass("inputHint"));
    },
    testFocus: function() {
        this.textInput = new Y.lane.TextInput(this.input, "hint text", true);
        Y.Assert.isTrue(this.input.hasClass("inputHint"), "doesn't have class this.inputHint");
        //have to do this.input.after("focus") safari value is not set on return from simulate.
        this.input.onceAfter("focus", function() {
            Y.Assert.areEqual("", this.input.get("value"), "value = " + this.input.get("value"));
            Y.Assert.isFalse(this.input.hasClass("inputHint"), "has class this.inputHint");
        }, this);
        this.input.simulate("focus");
    },
    testBlur: function() {
        this.textInput = new Y.lane.TextInput(this.input, "hint text", true);
        Y.Assert.isTrue(this.input.hasClass("inputHint"), "doesn't have class this.inputHint");
        //have to do this.input.after("focus") safari value is not set on return from simulate.
        this.input.onceAfter("blur", function() {
            Y.Assert.areEqual("hint text", this.input.get("value"));
            Y.Assert.isTrue(this.input.hasClass("inputHint"));
        }, this);
        this.input.simulate("blur");
    },
    testNoHintText: function() {
        this.textInput = new Y.lane.TextInput(this.input, null, true);
        Y.Assert.areEqual(this.input.get("value"), "");
        Y.Assert.isTrue(this.input.hasClass("inputHint"));
    },

    testSetGetValue: function() {
        this.textInput = new Y.lane.TextInput(this.input, null, true);
        this.textInput.setValue("value");
        Y.Assert.areEqual("value", this.textInput.getValue());
    },

    testFocusWithValue: function() {
        this.textInput = new Y.lane.TextInput(this.input, "input hint", true);
        this.textInput.setValue("value");
        this.input.onceAfter("focus", function() {
            Y.Assert.areEqual("value", this.input.get("value"));
            Y.Assert.isFalse(this.input.hasClass("inputHint"));
        }, this);
        this.input.simulate("focus");
    },

    testBlurWithoutValue: function() {
        this.textInput = new Y.lane.TextInput(this.input, "input hint", true);
        this.input.set("value", "");
        this.input.onceAfter("blur", function() {
            Y.Assert.areEqual("input hint", this.input.get("value"));
            Y.Assert.isTrue(this.input.hasClass("inputHint"));
        }, this);
        this.input.simulate("blur");
    },

    testDestroyWithoutValue: function() {
        this.textInput = new Y.lane.TextInput(this.input, "input hint", true);
        Y.Assert.areEqual("input hint", this.input.get("value"));
        Y.Assert.isTrue(this.input.hasClass("inputHint"));
        this.textInput.destroy();
        Y.Assert.areEqual("", this.input.get("value"));
        Y.Assert.isFalse(this.input.hasClass("inputHint"));
    }
}));

if (document.createElement("input").placeholder !== undefined) {
    Y.Test.Runner.add(new Y.Test.Case({

        input: null,

        setUp: function() {
            this.input = new Y.Node(document.createElement("input"));
        },
        name: "Lane Placeholder TextInputs Test Case",
        testConstructorHintText : function() {
            Y.Assert.areEqual("", this.input.get("value"));
            var textInput = new Y.lane.TextInput(this.input, "hint text");
            Y.Assert.areEqual("hint text", this.input.get("placeholder"));
        },
        testSetHintText: function() {
            var textInput = new Y.lane.TextInput(this.input, "hint text");
            textInput.setHintText("new hint text");
            Y.Assert.areEqual("new hint text", this.input.get("placeholder"));
        },

        testOtherInput: function() {
            this.textInput = new Y.lane.TextInput(this.input, "input hint", true);
            Y.Assert.areEqual("title", Y.one("input[title='title']").get("placeholder"));
        }
    }));
}

Y.one("body").addClass("yui3-skin-sam");
new Y.Console({
    newestOnTop: false
}).render("#log");


Y.Test.Runner.masterSuite.name = "textinputs-test.js";
Y.Test.Runner.run();
