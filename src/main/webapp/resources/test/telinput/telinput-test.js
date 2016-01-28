"use strict";

var telinputTestCase = new Y.Test.Case({
    name: "Lane telinput Testcase",
    input: Y.one("input[type='tel']"),

    setUp: function() {
        this.input.set("value","");
    },

    testFirstCharOne: function() {
        this.input.set("value", "1");
        this.input.simulate("keyup");
        Y.Assert.areEqual("1–", this.input.get("value"));
    },

    testFirstCharNotOne: function() {
        this.input.set("value", "2");
        this.input.simulate("keyup");
        Y.Assert.areEqual("2", this.input.get("value"));
    },

    testAreaCode: function() {
        this.input.set("value", "617");
        this.input.simulate("keyup");
        Y.Assert.areEqual("617–", this.input.get("value"));
    },

    testHas1dash: function() {
        this.input.set("value", "1–6");
        this.input.simulate("keyup");
        Y.Assert.areEqual("1–6", this.input.get("value"));
    },

    testHas1dashAreaCode: function() {
        this.input.set("value", "1–617");
        this.input.simulate("keyup");
        Y.Assert.areEqual("1–617–", this.input.get("value"));
    },

    testExchange: function() {
        this.input.set("value", "617–456");
        this.input.simulate("keyup");
        Y.Assert.areEqual("617–456–", this.input.get("value"));
    }
});

var ie7TestCase = new Y.Test.Case({
    name: "Doesn't work in IE7",

    input: Y.one("input[type='tel']"),

    testNoInput: function() {
        Y.Assert.isNull(this.input);
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

if (Y.UA.ie && Y.UA.ie < 8) {
    Y.Test.Runner.add(ie7TestCase);
} else {
    Y.Test.Runner.add(telinputTestCase);
}
Y.Test.Runner.masterSuite.name = "telinput-test.js";
Y.Test.Runner.run();
